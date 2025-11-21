package com.example.housing.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.housing.R;
import com.example.housing.adapters.NotificationAdapter;
import com.example.housing.data.repositories.NotificationRepository;
import com.example.housing.models.Notification;
import com.supabase.client.Client;
import com.supabase.client.types.PostgrestError;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment to display the user's notification history, handle data fetching, 
 * and mark items as read.
 */
public class NotificationFragment extends Fragment implements NotificationAdapter.NotificationClickListener
{

    private RecyclerView recyclerView;
    private TextView emptyTextView;
    private NotificationAdapter adapter;
    private NotificationRepository repository;
    private Client supabaseClient;

    private static final String TAG = "NotifFragment";

    // !!! IMPORTANT: Replace with a real dynamic User ID from your Supabase Auth session !!!
    // In a production app, this would be retrieved from your current session/local storage.
    private static final String DEMO_USER_ID = "00000000-0000-0000-0000-000000000001";

    // --- Supabase Configuration ---
    // You MUST replace these with your actual keys and URL
    private static final String SUPABASE_URL = "https://your-project-id.supabase.co";
    private static final String SUPABASE_ANON_KEY = "your_anon_public_key";
    // ------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Supabase Client
        try {
            supabaseClient = new Client(SUPABASE_URL, SUPABASE_ANON_KEY);
            repository = new NotificationRepository(supabaseClient);
        } catch (Exception e) {
            Log.e(TAG, "Failed to initialize Supabase client: " + e.getMessage());
            Toast.makeText(getContext(), "Initialization Error: Check Supabase URL/Key.", Toast.LENGTH_LONG).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        recyclerView = view.findViewById(R.id.notifications_recycler_view);
        emptyTextView = view.findViewById(R.id.empty_notifications_text);

        setupRecyclerView();
        return view;
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Pass the fragment instance as the click listener
        adapter = new NotificationAdapter(getContext(), new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Load data every time the fragment is visible
        loadNotifications();
    }

    private void loadNotifications() {
        if (repository == null) {
            emptyTextView.setText(getString(R.string.error_repo_not_initialized));
            return;
        }

        recyclerView.setVisibility(View.GONE);
        emptyTextView.setVisibility(View.VISIBLE);
        emptyTextView.setText(getString(R.string.loading_notifications));

        repository.fetchNotifications(DEMO_USER_ID, new NotificationRepository.NotificationCallback() {
            @Override
            public void onSuccess(List<Notification> notifications) {
                // Ensure UI updates happen on the main thread
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        if (notifications.isEmpty()) {
                            emptyTextView.setText(getString(R.string.no_notifications));
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyTextView.setVisibility(View.GONE);
                            adapter.updateData(notifications);
                        }
                    });
                }
            }

            @Override
            public void onFailure(String error) {
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), getString(R.string.error_loading_history) + error, Toast.LENGTH_LONG).show();
                        emptyTextView.setText(getString(R.string.error_loading_history) + error);
                    });
                }
            }
        });
    }

    /**
     * Implements the click handler for the adapter.
     */
    @Override
    public void onNotificationClicked(Notification notification) {
        // Skip marking AI-generated notifications since they don't exist in Supabase yet (DEMO)
        if (notification.getId().length() > 36) { // Check for UUID generated by UUID.randomUUID()
            Toast.makeText(getContext(), "AI-generated notification clicked!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (notification.isRead()) {
            Toast.makeText(getContext(), "Notification already read.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Optimistically update local UI immediately
        notification.setRead(true);
        int index = adapter.getNotifications().indexOf(notification);
        if (index != -1) {
            adapter.notifyItemChanged(index);
        }

        // 2. Update Supabase asynchronously
        repository.markAsRead(notification.getId(), new PostgrestError.PostgrestCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Notification marked as read in Supabase successfully: " + notification.getId());
                // No need to revert if successful
            }

            @Override
            public void onError(PostgrestError error) {
                // If update fails, revert local change and inform user
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        notification.setRead(false);
                        int failIndex = adapter.getNotifications().indexOf(notification);
                        if (failIndex != -1) {
                            adapter.notifyItemChanged(failIndex);
                        }
                        Toast.makeText(getContext(), getString(R.string.error_mark_read) + error.getMessage(), Toast.LENGTH_LONG).show();
                    });
                }
            }
        });
    }
}