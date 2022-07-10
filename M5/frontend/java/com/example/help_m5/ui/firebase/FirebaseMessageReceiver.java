//package com.example.help_m5.ui.firebase;
//
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.media.RingtoneManager;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Handler;
//import android.os.Looper;
//import android.util.Log;
//import android.widget.RemoteViews;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.core.app.NotificationCompat;
//import androidx.core.app.NotificationManagerCompat;
//
//import com.example.help_m5.R;
//import com.example.help_m5.ui.home.HomeFragment;
//import com.google.firebase.messaging.FirebaseMessaging;
//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.google.firebase.messaging.RemoteMessage;
//
//public class FirebaseMessageReceiver extends FirebaseMessagingService {
//    final String TAG = "FirebaseMessageReceiver";
//    @Override
//    public void onNewToken(@NonNull String token) {
//
//        Log.d(TAG, "Refreshed token: " + token);
//
//        // If you want to send messages to this application instance or
//        // manage this apps subscriptions on the server side, send the
//        // FCM registration token to your app server.
////        sendRegistrationToServer(token);
//
//    }
//
//    private void sendRegistrationToServer(String token) {
//
//    }
//
////    @Override
////    public void onMessageReceived(RemoteMessage remoteMessage) {
////        super.onMessageReceived(remoteMessage);
////        notify(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
////    }
////
////    public void notify(String title, String message) {
////        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notification_channel")
////                .setSmallIcon(R.drawable.ic_launcher_background)
////                .setContentTitle(title)
////                .setContentText(message)
////                .setAutoCancel(true);
////        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
////        managerCompat.notify(123, builder.build());
////    }
//
//    // Override onMessageReceived() method to extract the
//    // title and
//    // body from the message passed in FCM
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        // First case when notifications are received via
//        // data event
//        // Here, 'title' and 'message' are the assumed names
//        // of JSON
//        // attributes. Since here we do not have any data
//        // payload, This section is commented out. It is
//        // here only for reference purposes.
//		/*if(remoteMessage.getData().size()>0){
//			showNotification(remoteMessage.getData().get("title"),
//						remoteMessage.getData().get("message"));
//		}*/
//
//        // Second case when notification payload is
//        // received.
//        if (remoteMessage.getNotification() != null) {
//            // Since the notification is received directly from
//            // FCM, the title and the body can be fetched
//            // directly as below.
//            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
//        }
//    }
//
//    // Method to get the custom Design for the display of
//    // notification.
//    private RemoteViews getCustomDesign(String title, String message) {
//        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification);
//        remoteViews.setTextViewText(R.id.title, title);
//        remoteViews.setTextViewText(R.id.message, message);
//        remoteViews.setImageViewResource(R.id.icon, R.drawable.ic_baseline_all_inclusive_24);
//        return remoteViews;
//    }
//
//    // Method to display the notifications
//    public void showNotification(String title, String message) {
//        // Pass the intent to switch to the MainActivity
//        Intent intent = new Intent(this, HomeFragment.class);
//        // Assign channel ID
//        String channel_id = "notification_channel";
//        // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
//        // the activities present in the activity stack,
//        // on the top of the Activity that is to be launched
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        // Pass the intent to PendingIntent to start the
//        // next Activity
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//
//        // Create a Builder object using NotificationCompat
//        // class. This will allow control over all the flags
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel_id).setSmallIcon(R.drawable.ic_baseline_all_inclusive_24).setAutoCancel(true).setVibrate(new long[]{1000, 1000, 1000, 1000, 1000}).setOnlyAlertOnce(true).setContentIntent(pendingIntent);
//
//        // A customized design for the notification can be
//        // set only for Android versions 4.1 and above. Thus
//        // condition for the same is checked here.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            builder = builder.setContent(getCustomDesign(title, message));
//        } // If Android Version is lower than Jelly Beans,
//        // customized layout cannot be used and thus the
//        // layout is set as follows
//        else {
//            builder = builder.setContentTitle(title).setContentText(message).setSmallIcon(R.drawable.ic_baseline_all_inclusive_24);
//        }
//        // Create an object of NotificationManager class to
//        // notify the
//        // user of events that happen in the background.
//        NotificationManager notificationManager
//                = (NotificationManager) getSystemService(
//                Context.NOTIFICATION_SERVICE);
//        // Check if the Android Version is greater than Oreo
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel = new NotificationChannel(channel_id, "web_app", NotificationManager.IMPORTANCE_HIGH);
//            notificationManager.createNotificationChannel(notificationChannel);
//        }
//        notificationManager.notify(0, builder.build());
//    }
//}