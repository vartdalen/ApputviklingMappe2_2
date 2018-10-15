package vica.apputviklingmappe2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class DB extends Activity{

    public static String PROVIDER = "vica.contentprovider" ;
    public static final Uri CONTENT_URI = Uri.parse("content://"+ PROVIDER + "/User");
    public static final Uri CONTENT_FRIENDS_URI = Uri.parse("content://" + PROVIDER + "/Friends");

    public String getEmail(String email, Context context) {
        String[] projection = {context.getString(R.string.USER_ID)}; // table columns
        String selection = context.getString(R.string.USER_ID) + "="+"'"+email+"'";

        Cursor cursor = context.getContentResolver().query(CONTENT_URI, projection, selection, null, null);
        StringBuilder stringBuilderQueryResult = new StringBuilder("");
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            stringBuilderQueryResult.append(cursor.getString(0));
            cursor.close();
        }
        return stringBuilderQueryResult.toString();
    }

    public String getPassword(String password, Context context) {
        String[] projection = {context.getString(R.string.USER_PASSWORD)}; // table columns
        String selection = context.getString(R.string.USER_PASSWORD)+ "="+"'"+password+"'";

        Cursor cursor = context.getContentResolver().query(CONTENT_URI, projection, selection, null, null);
        StringBuilder stringBuilderQueryResult = new StringBuilder("");

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            stringBuilderQueryResult.append(cursor.getString(0));
            cursor.close();
        }
        return stringBuilderQueryResult.toString();
    }

    public void addFriend(Context context, String firstname, String lastname, String phone){
        ContentValues values = new ContentValues();
        values.put(context.getString(R.string.FRIEND_FIRSTNAME), firstname);
        values.put(context.getString(R.string.FRIEND_LASTNAME), lastname);
        values.put(context.getString(R.string.FRIEND_PHONE), phone);
        values.put(context.getString(R.string.FRIEND_FK), "");

        context.getContentResolver().insert(CONTENT_FRIENDS_URI, values);
    }
}
