package vica.apputviklingmappe2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
}
