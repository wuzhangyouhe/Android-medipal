package sg.edu.nus.iss.se8.medipal.helpers;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import sg.edu.nus.iss.se8.medipal.R;

public class NotificationHelper {
    public static void makeNotification(Context context, String title, String text, Integer notificationId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_round_2))
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle(title)
                .setContentText(text);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, builder.build());
    }
}
