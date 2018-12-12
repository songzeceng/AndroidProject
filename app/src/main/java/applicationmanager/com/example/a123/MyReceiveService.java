package applicationmanager.com.example.a123;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

import applicationmanager.com.example.a123.activity.BActivity;

public class MyReceiveService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String type = intent.getStringExtra("type");
        if (type.equals("start")) {
            Intent alarmIntent = new Intent(getApplicationContext(), MyReceiveService.class);
            alarmIntent.putExtra("type", "updateNotification");
            AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0,
                    alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            manager.set(AlarmManager.RTC_WAKEUP, 5 * 1000, pendingIntent);
        } else if (type.equals("updateNotification")) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Notification notification = new Notification();

            Intent alarmIntent = new Intent(getApplicationContext(), MyReceiveService.class);
            alarmIntent.putExtra("type", "startActivity");

            RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.activity_main);
            notification.contentView = remoteViews;
            notification.contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                    alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationManager.notify(0, notification);
        } else if (type.equals("startActivity")) {
            startActivity(new Intent(getApplication(), BActivity.class));
        }

        return super.onStartCommand(intent, flags, startId);
    }
}
