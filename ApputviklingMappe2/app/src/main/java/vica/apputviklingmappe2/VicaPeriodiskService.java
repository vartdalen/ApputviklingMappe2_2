package vica.apputviklingmappe2;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class VicaPeriodiskService extends Service {
    @Nullable
    @Override public IBinder onBind(Intent intent) { return null; }
    @Override public int onStartCommand(Intent intent, int flags, int startId){
        Intent i = new Intent(this, VicaService.class);

        i.putExtra(getString(R.string.friendSelectedListArray), intent.getStringArrayListExtra(getString(R.string.friendSelectedListArray)));
        i.putExtra(getString(R.string.notify_friends_message), intent.getStringExtra(getString(R.string.notify_friends_message)));
        i.putExtra(getString(R.string.notify_friends), intent.getExtras().getBoolean(getString(R.string.notify_friends)));
        i.putExtra(getString(R.string.personal_reminder), intent.getExtras().getBoolean(getString(R.string.personal_reminder)));

        PendingIntent pintent = PendingIntent.getService(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, parseTimeFromString(intent.getExtras().getString(getString(R.string.notify_friends_and_personal_reminder_timing))).getTimeInMillis(), AlarmManager.INTERVAL_DAY, pintent);
        return super.onStartCommand(intent, flags, startId);
    }

    private Calendar parseTimeFromString(String timeString) {
            Calendar calendar = Calendar.getInstance();
            LinkedList<String> res = new LinkedList<>();
            Pattern p = Pattern.compile("\\d+");
            Matcher m = p.matcher(timeString);

            while (m.find()) {
                res.add(m.group());
            }

            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(res.get(0)));
            calendar.set(Calendar.MINUTE, Integer.parseInt(res.get(1)));
            calendar.set(Calendar.SECOND, 0);

            return calendar;
    }
}
