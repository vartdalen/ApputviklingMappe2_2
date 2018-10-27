package vica.apputviklingmappe2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class VicaBootBroadcastReceiver extends BroadcastReceiver {
    private SharedPreferences prefs;

    @Override
    public void onReceive(Context context, Intent intent) {
        PreferenceManager.setDefaultValues(context, R.xml.activity_preferences, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(context);

        Intent i = new Intent(context, VicaPeriodiskService.class);
        i.putExtra(context.getString(R.string.notify_friends_message), prefs.getString(context.getString(R.string.notify_friends_message), context.getString(R.string.notify_friends_message_default)));
        i.putExtra(context.getString(R.string.notify_friends), prefs.getBoolean(context.getString(R.string.notify_friends), false));
        i.putExtra(context.getString(R.string.personal_reminder), prefs.getBoolean(context.getString(R.string.personal_reminder), true));
        Toast.makeText(context, "Booting Completed", Toast.LENGTH_LONG).show();

        context.startService(i);
    }
}
