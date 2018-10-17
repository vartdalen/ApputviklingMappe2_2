package vica.apputviklingmappe2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class DB extends Activity{

    public static String PROVIDER = "vica.contentprovider" ;
    public static final Uri CONTENT_USER_URI = Uri.parse("content://"+ PROVIDER + "/User");
    public static final Uri CONTENT_FRIEND_URI = Uri.parse("content://" + PROVIDER + "/Friend");

    public String getInfo(Uri uri, String[] projection, String selection, String sortOrder, Context context ) {
        Cursor cursor = context.getContentResolver().query(uri, projection, selection, null, sortOrder);
        StringBuilder stringBuilderQueryResult = new StringBuilder("");

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            stringBuilderQueryResult.append(cursor.getString(0));
            cursor.close();
        }
        return stringBuilderQueryResult.toString();
    }

    public String getEmail(String email, Context context) {
        String[] projection = {context.getString(R.string.USER_ID)}; // table columns
        String selection = context.getString(R.string.USER_ID) + "="+"'"+email+"'";

        Cursor cursor = context.getContentResolver().query(CONTENT_USER_URI, projection, selection, null, null);
        StringBuilder stringBuilderQueryResult = new StringBuilder("");
        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToFirst();
            stringBuilderQueryResult.append(cursor.getString(0));
            cursor.close();
        }
        return stringBuilderQueryResult.toString();
    }

    public void addFriend(Context context, String firstname, String lastname, String phone, String userEmail){
        ContentValues values = new ContentValues();
        values.put(context.getString(R.string.FRIEND_FIRSTNAME), firstname);
        values.put(context.getString(R.string.FRIEND_LASTNAME), lastname);
        values.put(context.getString(R.string.FRIEND_PHONE), phone);
        values.put(context.getString(R.string.FRIEND_FK), userEmail);

        context.getContentResolver().insert(CONTENT_FRIEND_URI, values);
    }

    public boolean verifyUser(String email, String password, Context context) {
        if(getEmail(email, context).length() > 5) {
            if(getInfo(CONTENT_USER_URI, new String[]{context.getString(R.string.USER_PASSWORD)}, context.getString(R.string.USER_ID)+ "="+"'"+email+"'",null, context).equals(password)) {
                return true;
            }
        }
        return false;
    }

    public void deleteFriend(Context context, String id){
        String selection = context.getString(R.string.FRIEND_ID)+"="+"'"+id+"'";
        context.getContentResolver().delete(CONTENT_FRIEND_URI, selection, null);
    }
}
