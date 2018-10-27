package vica.apputviklingmappe2;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;


public class VicaPeriodiskService extends Service {
    @Nullable
    @Override public IBinder onBind(Intent intent) { return null; }
    @Override public int onStartCommand(Intent intent, int flags, int startId){
        java.util.Calendar cal = Calendar.getInstance();
        Intent i = new Intent(this, VicaService.class);

        i.putExtra(getString(R.string.friendSelectedListArray), intent.getStringArrayListExtra(getString(R.string.friendSelectedListArray)));
        i.putExtra(getString(R.string.notify_friends_message), intent.getStringExtra(getString(R.string.notify_friends_message)));
        i.putExtra(getString(R.string.notify_friends), intent.getExtras().getBoolean(getString(R.string.notify_friends)));
        i.putExtra(getString(R.string.personal_reminder), intent.getExtras().getBoolean(getString(R.string.personal_reminder)));

        PendingIntent pintent = PendingIntent.getService(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm =
                (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 5 * 1000, pintent); /*Hvert minutt*/
        return super.onStartCommand(intent, flags, startId);
    }
}
