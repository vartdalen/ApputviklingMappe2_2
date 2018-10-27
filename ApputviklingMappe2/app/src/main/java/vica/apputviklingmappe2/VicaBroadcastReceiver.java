package vica.apputviklingmappe2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

public class VicaBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /* Intent i = new Intent(context, MinService.class);*/
        Intent i = new Intent(context, VicaPeriodiskService.class);
        ArrayList<String> friendSelectedListArray = intent.getStringArrayListExtra(context.getString(R.string.friendSelectedListArray));
        i.putExtra(context.getString(R.string.notify_friends_and_personal_reminder_timing), intent.getStringExtra(context.getString(R.string.notify_friends_and_personal_reminder_timing)));
        i.putExtra(context.getString(R.string.friendSelectedListArray), friendSelectedListArray);
        i.putExtra(context.getString(R.string.notify_friends_message), intent.getStringExtra(context.getString(R.string.notify_friends_message)));
        i.putExtra(context.getString(R.string.notify_friends), intent.getExtras().getBoolean(context.getString(R.string.notify_friends)));
        i.putExtra(context.getString(R.string.personal_reminder), intent.getExtras().getBoolean(context.getString(R.string.personal_reminder)));

        context.startService(i);
    }
}
