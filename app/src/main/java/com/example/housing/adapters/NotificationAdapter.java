package com.example.housing.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.housing.R;
import com.example.housing.models.Notification;
import com.example.housing.utils.TimeUtils;

import java.util.List;

/**
 * Adapter for displaying the list of notifications, including styling for read/unread state.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>
{

    private List<Notification> notificationsList;
    private final NotificationClickListener clickListener;
    private final Context context;

    public interface NotificationClickListener
    {
        void onNotificationClicked(Notification notification);
    }

    public NotificationAdapter(Context context, List<Notification> notificationsList, NotificationClickListener clickListener)
    {
        this.context = context;
        this.notificationsList = notificationsList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification_list, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position)
    {
        Notification notification = notificationsList.get(position);
        holder.bind(notification, clickListener, context);
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    /** Update the list data and refresh the RecyclerView. */
    public void updateData(List<Notification> newNotifications)
    {
        this.notificationsList = newNotifications;
        notifyDataSetChanged();
    }

    public List<Notification> getNotifications()
    {
        return notificationsList;
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView titleTextView;
        private final TextView bodyTextView;
        private final TextView timeTextView;
        private final View unreadIndicator;

        public NotificationViewHolder(@NonNull View itemView)
        {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.notification_title);
            bodyTextView = itemView.findViewById(R.id.notification_body);
            timeTextView = itemView.findViewById(R.id.notification_time);
            unreadIndicator = itemView.findViewById(R.id.unread_indicator);
        }

        public void bind(Notification notification, NotificationClickListener clickListener, Context context)
        {
            titleTextView.setText(notification.getTitle());
            bodyTextView.setText(notification.getBody());
            timeTextView.setText(TimeUtils.getFormattedTime(notification.getCreatedAt()));
            itemView.setOnClickListener(v -> clickListener.onNotificationClicked(notification));

//            if (!notification.isRead())
//            {
//                unreadIndicator.setVisibility(View.VISIBLE);
//                titleTextView.setTextColor(ContextCompat.getColor(context, R.color.black));
//                bodyTextView.setTextColor(ContextCompat.getColor(context, R.color.purple_500));
//                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.light_gray_or_off_white));
//            }
//            else
//            {
//                unreadIndicator.setVisibility(View.GONE);
//                titleTextView.setTextColor(ContextCompat.getColor(context, R.color.black));
//
//                bodyTextView.setTextColor(ContextCompat.getColor(context, R.color.black));
//                itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
//            }
        }
    }
}