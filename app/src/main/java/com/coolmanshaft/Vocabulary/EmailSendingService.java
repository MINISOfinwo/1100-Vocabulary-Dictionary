package com.coolmanshaft.Vocabulary;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.AsyncTask;
import androidx.core.app.NotificationCompat;

public class EmailSendingService extends Service {

    private static final int NOTIFICATION_ID = 1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Create a simple notification
        Notification notification = new NotificationCompat.Builder(this, "your_channel_id")
                .setContentTitle("Sending Email")
                .setContentText("Your email is being sent in the background.")
                .setSmallIcon(R.drawable.ic_notification) // Make sure to replace with a valid icon
                .build();

        // Start the service in the foreground
        startForeground(NOTIFICATION_ID, notification);

        // Extract the email, subject, and message from the intent
        String email = intent.getStringExtra("email");
        String subject = intent.getStringExtra("subject");
        String message = intent.getStringExtra("message");

        // Send email in background using AsyncTask
        new EmailSender(email, subject, message).execute();

        // Make the service sticky, so it runs indefinitely
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // No binding necessary for this service
    }
}

