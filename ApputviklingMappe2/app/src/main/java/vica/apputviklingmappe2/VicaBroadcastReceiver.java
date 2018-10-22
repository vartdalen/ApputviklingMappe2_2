package vica.apputviklingmappe2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class VicaBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /* Intent i = new Intent(context, MinService.class);*/
        Intent i = new Intent(context, VicaPeriodiskService.class);

        context.startService(i);
    }
}
