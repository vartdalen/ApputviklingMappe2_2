package vica.apputviklingmappe2;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;

public class DB extends Activity{

    public static String PROVIDER = "vica.contentprovider" ;
    public static final Uri CONTENT_URI = Uri.parse("content://"+ PROVIDER + "/User");

    public String getEmail(String email) {
        String[] projection = {getString(R.string.USER_ID)}; // table columns
        String selection = getString(R.string.USER_ID) + "="+"'"+email+"'";

        Cursor cursor = getContentResolver().query(CONTENT_URI, projection, selection, null, null);
        StringBuilder stringBuilderQueryResult = new StringBuilder("");
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            stringBuilderQueryResult.append(cursor.getString(0));
            cursor.close();
        }
        return stringBuilderQueryResult.toString();
    }

    public String getPassword(String password) {
        String[] projection = {getString(R.string.USER_PASSWORD)}; // table columns
        String selection = getString(R.string.USER_PASSWORD)+ "="+"'"+password+"'";

        Cursor cursor = getContentResolver().query(CONTENT_URI, projection, selection, null, null);
        StringBuilder stringBuilderQueryResult = new StringBuilder("");

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            stringBuilderQueryResult.append(cursor.getString(0));
            cursor.close();
        }
        return stringBuilderQueryResult.toString();
    }
}
