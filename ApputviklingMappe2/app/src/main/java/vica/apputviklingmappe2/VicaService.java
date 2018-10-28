package vica.apputviklingmappe2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;

public class VicaService extends Service {
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DB db = new DB();
        Helper helper = new Helper();
        ArrayList<String> friendSelectedListArray = intent.getStringArrayListExtra(getString(R.string.friendSelectedListArray));
        String notificationMessage = intent.getStringExtra(getString(R.string.notify_friends_message));
        Boolean notifyFriends = intent.getExtras().getBoolean(getString(R.string.notify_friends));
        Boolean personalReminder = intent.getExtras().getBoolean(getString(R.string.personal_reminder));

        if(notifyFriends) {
            String phone;
            for (String friend : friendSelectedListArray) {
                phone = db.getInfo(DB.CONTENT_FRIEND_URI, new String[]{getString(R.string.FRIEND_PHONE)}, getString(R.string.FRIEND_ID) + "=" + helper.parseNumbersFromString(friend), null, this);
                helper.sendSMS(phone, notificationMessage, null);
            }

        }
        if(personalReminder) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Intent i = new Intent(this, ActivityOrderHistory.class);
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, i, 0);
            Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle(getString(R.string.vica_restaurant))
                    .setContentText(getString(R.string.personal_reminder_message))
                    .setSmallIcon(R.drawable.ic_logo).setContentIntent(pIntent).build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(0, notification);
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
