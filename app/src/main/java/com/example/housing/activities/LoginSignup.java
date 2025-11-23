package com.example.housing.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.housing.BuildConfig;
import com.example.housing.R;
import com.example.housing.network.RetrofitClient;
import com.example.housing.network.UserApi;
import com.example.housing.utils.PrefManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public class LoginSignup extends AppCompatActivity
{
    private Button btnLogin, btnSignUp, btnContinueGoogle;
    private PrefManager prefManager;
    private SupabaseApi supabaseApi; // Internal Retrofit API client

    // --- Supabase Configuration ---
    private static final String SUPABASE_URL = BuildConfig.SUPABASE_URL;
    private static final String REDIRECT_URI = BuildConfig.REDIRECT_URI;
    private static final String SUPABASE_ANON_KEY = BuildConfig.SUPABASE_ANON_KEY;
    private static final String BASE_URL = SUPABASE_URL + "/";

    interface SupabaseApi {
        // Token refresh endpoint (form urlencoded)
        @FormUrlEncoded
        @POST("auth/v1/token")
        Call<JsonObject> refreshToken(
                @Field("grant_type") String grantType,
                @Field("refresh_token") String refreshToken,
                @Header("apikey") String apiKey
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);

        btnLogin = findViewById(R.id.log_in);
        btnSignUp = findViewById(R.id.btn_sign_up);
        btnContinueGoogle = findViewById(R.id.btn_continue_google);

        prefManager = PrefManager.getInstance(this);

        initRetrofit();

        // AUTO LOGIN: if refresh token exists, attempt silent refresh and go Home
        String existingRefresh = prefManager.getRefreshToken();
        if (existingRefresh != null) {
            silentRefreshAndProceed(existingRefresh);
            // We do not return here so the UI remains visible while refresh is happening.
            // If refresh succeeds, user will be forwarded to HomeActivity.
        }

        btnLogin.setOnClickListener(v ->
                startActivity(new Intent(LoginSignup.this, Login.class)));

        btnSignUp.setOnClickListener(v ->
                startActivity(new Intent(LoginSignup.this, Signup.class)));

        btnContinueGoogle.setOnClickListener(v -> openGoogleLogin());

        // Handle intent if activity launched via OAuth redirect
        handleOAuthCallback(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);
        handleOAuthCallback(intent);
    }

    // --- Retrofit Initialization ---
    private void initRetrofit() {
        OkHttpClient client = new OkHttpClient.Builder().build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        supabaseApi = retrofit.create(SupabaseApi.class);
    }

    // --- Google Login Flow ---
    private void openGoogleLogin()
    {
        // request openid, email and profile to ensure email & name are returned
        String url = SUPABASE_URL + "/auth/v1/authorize"
                + "?provider=google"
                + "&redirect_to=" + REDIRECT_URI
                + "&scopes=openid%20email%20profile"
                + "&prompt=select_account";

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    private void handleOAuthCallback(Intent intent) {
        if (intent == null) return;

        Uri uri = intent.getData();
        if(uri == null || REDIRECT_URI == null) return;
        if (!uri.toString().startsWith(REDIRECT_URI)) return;

        String fragment = uri.getFragment();
        if(fragment == null) {
            Toast.makeText(this, "Authentication failed (no fragment)", Toast.LENGTH_LONG).show();
            return;
        }
        if(fragment.contains("error=")) {
            Toast.makeText(this, "Authentication failed", Toast.LENGTH_LONG).show();
            return;
        }

        // parse fragment params (URL-decoded)
        String[] params = fragment.split("&");
        String accessToken = null;
        String refreshToken = null;
        String email = null;

        for (String param : params) {
            String[] kv = param.split("=", 2);
            if (kv.length != 2) continue;
            String key = kv[0];
            String value = urlDecode(kv[1]);
            if (key.equals("access_token")) accessToken = value;
            if (key.equals("refresh_token")) refreshToken = value;
            if (key.equals("email")) email = value;
        }

        if (accessToken != null && refreshToken != null) {
            // If email not provided in fragment, try to decode it from JWT access token.
            if (email == null) {
                JsonObject decoded = decodeJWT(accessToken);
                if (decoded != null && decoded.has("email") && !decoded.get("email").isJsonNull()) {
                    email = decoded.get("email").getAsString();
                }
            }

            // Get user id (sub) & full name from JWT
            String uid = null;
            String fullName = "";
            JsonObject decoded = decodeJWT(accessToken);
            if (decoded != null) {
                if (decoded.has("sub") && !decoded.get("sub").isJsonNull()) {
                    uid = decoded.get("sub").getAsString();
                }
                if (decoded.has("name") && !decoded.get("name").isJsonNull()) {
                    fullName = decoded.get("name").getAsString();
                } else if (decoded.has("full_name") && !decoded.get("full_name").isJsonNull()) {
                    fullName = decoded.get("full_name").getAsString();
                }
            }


            JsonObject userObj = new JsonObject();
            userObj.addProperty("p_email", email);
            UserApi userApi = RetrofitClient.getClient().create(UserApi.class);
            userApi.insertUser("Bearer " + accessToken,BuildConfig.SUPABASE_ANON_KEY, userObj)
                    .enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(LoginSignup.this, "Signup successful!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginSignup.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginSignup.this, "DB insert failed. Contact Support", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                        }
                    });



        saveLoginAndGoHome(accessToken, refreshToken, uid, email, fullName);
        } else {
            Toast.makeText(this, "Missing access or refresh token", Toast.LENGTH_LONG).show();
        }
    }

    // --- Save to PrefManager + navigate home ---
    private void saveLoginAndGoHome(@NonNull String accessToken,
                                    @NonNull String refreshToken,
                                    String uid,
                                    String email,
                                    String fullName)
    {
        // If uid missing, fallback to email-based id (not ideal but safe)
        String userIdToSave = uid != null ? uid : (email != null ? email : "google_uid_not_used");

        prefManager.saveLogin(accessToken, refreshToken, userIdToSave, email, "google");

        if (fullName != null && !fullName.isEmpty()) {
            prefManager.setFullName(fullName);
        }

        // go to Home
        startActivity(new Intent(LoginSignup.this, HomeActivity.class));
        finish();
    }

    // --- Silent refresh using refresh token ---
    private void silentRefreshAndProceed(String currentRefreshToken) {
        if (currentRefreshToken == null) return;

        supabaseApi.refreshToken("refresh_token", currentRefreshToken, SUPABASE_ANON_KEY)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            JsonObject body = response.body();
                            String newAccess = body.has("access_token") && !body.get("access_token").isJsonNull()
                                    ? body.get("access_token").getAsString() : null;
                            String newRefresh = body.has("refresh_token") && !body.get("refresh_token").isJsonNull()
                                    ? body.get("refresh_token").getAsString() : null;

                            // decode uid and name if present
                            String uid = null;
                            String fullName = "";
                            if (newAccess != null) {
                                JsonObject decoded = decodeJWT(newAccess);
                                if (decoded != null) {
                                    if (decoded.has("sub") && !decoded.get("sub").isJsonNull()) {
                                        uid = decoded.get("sub").getAsString();
                                    }
                                    if (decoded.has("name") && !decoded.get("name").isJsonNull()) {
                                        fullName = decoded.get("name").getAsString();
                                    }
                                }
                            }

                            // Save whichever tokens we have (prefer new ones)
                            String accessToSave = newAccess != null ? newAccess : prefManager.getAccessToken();
                            String refreshToSave = newRefresh != null ? newRefresh : prefManager.getRefreshToken();
                            String userIdToSave = uid != null ? uid : prefManager.getUserId();
                            String email = prefManager.getEmail();

                            if (accessToSave != null && refreshToSave != null) {
                                prefManager.saveLogin(accessToSave, refreshToSave, userIdToSave, email, prefManager.getProvider() != null ? prefManager.getProvider() : "google");
                                if (!fullName.isEmpty()) prefManager.setFullName(fullName);

                                // success: go to home
                                startActivity(new Intent(LoginSignup.this, HomeActivity.class));
                                finish();
                            }
                        } else {
                            // silently fail; user stays on login screen
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        // silent failure; user stays on login screen
                    }
                });
    }

    private JsonObject decodeJWT(String jwt) {
        if (jwt == null) return null;
        try {
            String[] parts = jwt.split("\\.");
            if (parts.length < 2) return null;
            String payload = parts[1];

            // Base64 URL-safe decode (handle padding)
            // Add padding if necessary
            int mod = payload.length() % 4;
            if (mod != 0) {
                int pads = 4 - mod;
                StringBuilder sb = new StringBuilder(payload);
                for (int i = 0; i < pads; i++) sb.append('=');
                payload = sb.toString();
            }

            byte[] decodedBytes = Base64.decode(payload, Base64.URL_SAFE | Base64.NO_WRAP);
            String json = new String(decodedBytes, "UTF-8");

            return JsonParser.parseString(json).getAsJsonObject();
        } catch (Exception e) {
            return null;
        }
    }

    private String urlDecode(String s) {
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return s;
        }
    }
}
