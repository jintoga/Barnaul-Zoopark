package com.dat.barnaulzoopark.pushnotification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.User;
import com.dat.barnaulzoopark.ui.bloganimaldetail.BlogAnimalDetailActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Map;

/**
 * Created by DAT on 5/15/2017.
 */

public class FirebaseMessageService extends FirebaseMessagingService {

    private static final String KEY_ANIMAL_UID = "animalUid";
    private static final String KEY_BLOG_UID = "blogUid";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> data = remoteMessage.getData();
        String animalUid = data.get(KEY_ANIMAL_UID);
        if (isSubscribedToAnimal(animalUid)) {
            String blogUid = data.get(KEY_BLOG_UID);
            createNotification(remoteMessage, blogUid);
        }
    }

    private boolean isSubscribedToAnimal(@NonNull String animalUid) {
        User user = BZApplication.get(this).getUser();
        return user != null && user.getSubscribedAnimals().containsKey(animalUid);
    }

    private void createNotification(RemoteMessage remoteMessage, String blogUid) {
        // Create Notification
        Intent intent = new Intent(this, BlogAnimalDetailActivity.class);
        intent.putExtra(BlogAnimalDetailActivity.EXTRA_BLOG_ANIMAL_UID, blogUid);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent =
            PendingIntent.getActivity(this, 1410, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder =
            new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_panda)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1410, notificationBuilder.build());
    }
}
