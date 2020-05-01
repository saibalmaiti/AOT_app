package com.example.aot;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.messaging.FirebaseMessagingService;
public class MyFirebaseMessagingService extends FirebaseMessagingService{

    private static final String TAG = "MyFirebaseMsgService";
    private String stdID;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        stdID = "0";
        String title = remoteMessage.getNotification().getTitle();
        String messege = remoteMessage.getNotification().getBody();
        String click_action = remoteMessage.getNotification().getClickAction();
        if (remoteMessage.getData().size() > 0) {
            stdID = remoteMessage.getData().get("UserId");
            sendNotification(title, messege, click_action, stdID);
        }
    }

    @Override
    public void onDeletedMessages() {

    }
    private void sendNotification(String title,String messege,String click_action,String data){
        /*Intent intent = new Intent(this,MainActivity.class);*/
        Intent intent = new Intent(click_action);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("messeage",messege);
        intent.putExtra("ID",data);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0/*Request code*/,intent,PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messege)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(messege);
            notificationManager.createNotificationChannel(channel);

        }
        notificationManager.notify(0,notificationBuilder.build());
    }
}
