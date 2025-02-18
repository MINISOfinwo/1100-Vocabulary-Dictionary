package com.coolmanshaft.Vocabulary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Check if the broadcast is for boot completion
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            // Log or perform actions after boot
            Log.d("BootBroadcastReceiver", "Device rebooted. Starting email service.");

            // Start your service or work manager here
            // For example, start a foreground service
            Intent serviceIntent = new Intent(context, EmailSendingService.class);
            context.startService(serviceIntent);
        }
    }
}
