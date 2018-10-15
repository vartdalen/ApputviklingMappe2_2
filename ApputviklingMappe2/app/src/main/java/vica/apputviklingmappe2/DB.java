package vica.apputviklingmappe2;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class DB extends Activity{

    public static String PROVIDER = "vica.contentprovider" ;
    public static final Uri CONTENT_USER_URI = Uri.parse("content://"+ PROVIDER + "/User");
    public static final Uri CONTENT_FRIENDS_URI = Uri.parse("content://" + PROVIDER + "/Friends");

    public String getEmail(String email, Context context) {
        String[] projection = {context.getString(R.string.USER_ID)}; // table columns
        String selection = context.getString(R.string.USER_ID) + "="+"'"+email+"'";

        Cursor cursor = context.getContentResolver().query(CONTENT_USER_URI, projection, selection, null, null);
        StringBuilder stringBuilderQueryResult = new StringBuilder("");
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            stringBuilderQueryResult.append(cursor.getString(0));
            cursor.close();
        }
        return stringBuilderQueryResult.toString();
    }

    private String getPassword(String email, Context context) {
        String[] projection = {context.getString(R.string.USER_PASSWORD)}; // table columns
        String selection = context.getString(R.string.USER_ID)+ "="+"'"+email+"'";

        Cursor cursor = context.getContentResolver().query(CONTENT_USER_URI, projection, selection, null, null);
        StringBuilder stringBuilderQueryResult = new StringBuilder("");

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            stringBuilderQueryResult.append(cursor.getString(0));
            cursor.close();
        }
        return stringBuilderQueryResult.toString();
    }

    private String getUserLevel(String email, Context context) {
        String[] projection = {context.getString(R.string.USER_LEVEL)}; // table columns
        String selection = context.getString(R.string.USER_ID)+ "="+"'"+email+"'";

        Cursor cursor = context.getContentResolver().query(CONTENT_USER_URI, projection, selection, null, null);
        StringBuilder stringBuilderQueryResult = new StringBuilder("");

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            stringBuilderQueryResult.append(cursor.getString(0));
            cursor.close();
        }
        return stringBuilderQueryResult.toString();
    }

    public String getInfo(Uri uri, String[] projection, String selection, Context context ) {

        Cursor cursor = context.getContentResolver().query(uri, projection, selection, null, null);
        StringBuilder stringBuilderQueryResult = new StringBuilder("");

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            stringBuilderQueryResult.append(cursor.getString(0));
            cursor.close();
        } else {
            System.out.println("SQL exception");
        }
        return stringBuilderQueryResult.toString();
    }

    public boolean verifyUser(String email, String password, Context context) {
        if(getEmail(email, context).length() > 5) {
            if(getPassword(email, context).equals(password)) {
                return true;
            }
        }
        return false;
    }
}
