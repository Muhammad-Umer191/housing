//package com.example.housing.fragments;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.housing.R;
//import com.example.housing.adapters.NotificationAdapter;
//import com.example.housing.repository.NotificationRepository;
//import com.example.housing.models.Notification;
//import com.example.housing.network.RetrofitClient;
//import com.example.housing.utils.PrefManager;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class NotificationFragment extends Fragment implements NotificationAdapter.NotificationClickListener
//{
//    private RecyclerView recyclerView;
//    private TextView emptyTextView;
//    private NotificationAdapter adapter;
//    private NotificationRepository repository;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
//    {
//        View view = inflater.inflate(R.layout.fragment_notification, container, false);
//
//        recyclerView = view.findViewById(R.id.notifications_recycler_view);
//        emptyTextView = view.findViewById(R.id.empty_notifications_text);
//
//        adapter = new NotificationAdapter(getContext(), new ArrayList<>(), this);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(adapter);
//
////        repository = new NotificationRepository(RetrofitClient.getSupabaseApi(), getContext());
//
//        return view;
//    }
//
//    @Override
//    public void onResume()
//    {
//        super.onResume();
//        loadNotifications();
//    }
//
//    private void loadNotifications()
//    {
//        String userId = PrefManager.getInstance(getContext()).getUserId();
//        if (userId == null)
//        {
//            emptyTextView.setText("User not logged in");
//            return;
//        }
//
//        repository.fetchUserNotifications(userId, new NotificationRepository.FetchNotificationsCallback()
//        {
//            @Override
//            public void onSuccess(List<Notification> notifications)
//            {
//                if (notifications.isEmpty())
//                {
//                    emptyTextView.setText("No notifications");
//                }
//                else
//                {
//                    recyclerView.setVisibility(View.VISIBLE);
//                    emptyTextView.setVisibility(View.GONE);
//                    adapter.updateData(notifications);
//                }
//            }
//
//            @Override
//            public void onFailure(String error)
//            {
//                emptyTextView.setText("Failed to load notifications: " + error);
//                Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//
//    @Override
//    public void onNotificationClicked(Notification notification)
//    {
//        if (!notification.isRead())
//        {
//            repository.markNotificationAsRead(notification.getId(), new NotificationRepository.UpdateCallback()
//            {
//                @Override
//                public void onSuccess() { notification.setRead(true); adapter.notifyDataSetChanged(); }
//                @Override
//                public void onFailure(String error) { Toast.makeText(getContext(), "Failed to mark read", Toast.LENGTH_SHORT).show(); }
//            });
//        }
//    }
//}
