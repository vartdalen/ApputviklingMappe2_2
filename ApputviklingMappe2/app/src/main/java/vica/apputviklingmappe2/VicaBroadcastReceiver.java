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
        ArrayList<String> friendSelectedListArray;
        friendSelectedListArray = intent.getStringArrayListExtra(context.getString(R.string.friendSelectedListArray));
        i.putExtra(context.getString(R.string.friendSelectedListArray), friendSelectedListArray);
        i.putExtra(context.getString(R.string.default_reminder_message), intent.getStringExtra(context.getString(R.string.default_reminder_message)));
        context.startService(i);
    }
}
