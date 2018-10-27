package vica.apputviklingmappe2;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {

    public void quit(final Activity activity) {
        AlertDialog confirm_quit = new AlertDialog.Builder(activity).create();
        confirm_quit.setTitle(activity.getResources().getString(R.string.quit));
        confirm_quit.setMessage(activity.getResources().getString(R.string.confirmation_quit1));
        confirm_quit.setButton(AlertDialog.BUTTON_POSITIVE, activity.getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                });
        confirm_quit.setButton(AlertDialog.BUTTON_NEGATIVE, activity.getResources().getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        confirm_quit.show();
    }

    public String hash(String unhashed) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashed = digest.digest(unhashed.getBytes(StandardCharsets.UTF_8));
        return new String(hashed);
    }

    public String parseNumbersFromString(String listString){
        if(listString != null){
            LinkedList<String> res = new LinkedList<>();
            Pattern p = Pattern.compile("\\d+");
            Matcher m = p.matcher(listString);
            while(m.find()){
                res.add(m.group());
            }
            listString = res.get(0);
            return listString;
        }else{
            return "";
        }
    }

    public void sendSMS(String phoneNo, String msg, Activity activity) {
        int MY_PERMISSIONS_REQUEST_SEND_SMS;
        if(activity!=null) {
            MY_PERMISSIONS_REQUEST_SEND_SMS = ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.SEND_SMS);
        } else {
            MY_PERMISSIONS_REQUEST_SEND_SMS = PackageManager.PERMISSION_GRANTED;
        }
        if(MY_PERMISSIONS_REQUEST_SEND_SMS == PackageManager.PERMISSION_GRANTED) {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.SEND_SMS}, 0);
        }
    }

    public void stoppPeriodisk(Context context) {
        Intent i = new Intent(context, VicaService.class);
        PendingIntent pintent = PendingIntent.getService(context, 0, i, 0);
        AlarmManager alarm =(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(alarm!= null) {
            alarm.cancel(pintent);
        }
    }
}
