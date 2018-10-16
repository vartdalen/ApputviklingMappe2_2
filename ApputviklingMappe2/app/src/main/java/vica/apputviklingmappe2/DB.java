package vica.apputviklingmappe2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DB extends Activity{

    public static String PROVIDER = "vica.contentprovider" ;
    public static final Uri CONTENT_USER_URI = Uri.parse("content://"+ PROVIDER + "/User");
    public static final Uri CONTENT_FRIENDS_URI = Uri.parse("content://" + PROVIDER + "/Friends");

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

    public void addFriend(Context context, String firstname, String lastname, String phone, String userEmail){
        ContentValues values = new ContentValues();
        values.put(context.getString(R.string.FRIEND_FIRSTNAME), firstname);
        values.put(context.getString(R.string.FRIEND_LASTNAME), lastname);
        values.put(context.getString(R.string.FRIEND_PHONE), phone);
        values.put(context.getString(R.string.FRIEND_FK), userEmail);

        context.getContentResolver().insert(CONTENT_FRIENDS_URI, values);
    }

    public boolean verifyUser(String email, String password, Context context) {
        if(getEmail(email, context).length() > 5) {
            if(getPassword(email, context).equals(password)) {
                return true;
            }
        }
        return false;
    }

    private String stringParser(String listString){
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

    public void deleteFriend(Context context, String id){
        String selection = context.getString(R.string.FRIEND_ID)+ "= ?";
        String[] selectionArgs = {stringParser(id)};
        context.getContentResolver().delete(CONTENT_FRIENDS_URI, selection, selectionArgs);
    }
}
