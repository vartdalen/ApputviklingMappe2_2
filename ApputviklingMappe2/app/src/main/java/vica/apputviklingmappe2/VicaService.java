package vica.apputviklingmappe2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class VicaService extends Service {
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent i = new Intent(this, ResultNotification.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, i, 0);
        Notification notifikasjon = new NotificationCompat.Builder(this)
                .setContentTitle("Notification Title Here")
                .setContentText("Notification Content Msg Here")
                .setSmallIcon(R.mipmap.ic_launcher).setContentIntent(pIntent).build();
        notifikasjon.flags|= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, notifikasjon);

        return super.onStartCommand(intent, flags, startId);
    }
}
