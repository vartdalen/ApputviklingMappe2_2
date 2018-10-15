package vica.apputviklingmappe2;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DBTest extends Activity {

    TextView view_showall;
    Button showall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbtest);

        view_showall = (TextView)findViewById(R.id.textview_showall);
        showall = (Button)findViewById(R.id.showall);
    }

    // Retrieves data from database by using a stringbuilder and sends the string to the textview
//    public void showall(View v) {
//
//        Cursor cur = getContentResolver().query(CONTENT_USER_URI, null, null, null, null);
//        StringBuilder stringBuilderQueryResult = new StringBuilder("");
//
//        if(cur != null && cur.moveToFirst()) {
//            do {
//                stringBuilderQueryResult.append(
//                        (cur.getString(0)) + " " + // id (email)
//                        (cur.getString(1)) + " " + // firstname
//                        (cur.getString(2)) + " " + // lastname
//                        (cur.getString(3)) + " " + // phone
//                        (cur.getString(4)) + " " + "\r\n"); // password
//            }
//            while(cur.moveToNext());
//            cur.close();
//            view_showall.setText(stringBuilderQueryResult);
//        }
//        else{
//            Toast.makeText(this, "Failed query!", Toast.LENGTH_SHORT).show();
//        }
//    }

    public void showall(View v) {

        Cursor cur = getContentResolver().query(DB.CONTENT_FRIENDS_URI, null, null, null, null);
        StringBuilder stringBuilderQueryResult = new StringBuilder("");

        if(cur != null && cur.moveToFirst()) {
            do {
                stringBuilderQueryResult.append(
                        (cur.getString(0)) + " " + // id
                                (cur.getString(1)) + " " + // firstname
                                (cur.getString(2)) + " " + // lastname
                                (cur.getString(3)) + " " + // phone
                                (cur.getString(4)) + " " + "\r\n"); // user's email
            }
            while(cur.moveToNext());
            cur.close();
            view_showall.setText(stringBuilderQueryResult);
        }
        else{
            Toast.makeText(this, "Failed query!", Toast.LENGTH_SHORT).show();
        }
    }

//    public void showall(View v) {
//        String[] projection = {getString(R.string.USER_ID)}; // table columns
//
//        String selection = "Email = 'admin@live.no'";
//
//        Cursor cursor = getContentResolver().query(CONTENT_USER_URI, projection, selection, null, null);
//        StringBuilder stringBuilderQueryResult = new StringBuilder("");
//
//        if (cursor.moveToFirst()) {
//            cursor.moveToFirst();
//            stringBuilderQueryResult.append(cursor.getString(0));
//            view_showall.setText(stringBuilderQueryResult);
//            cursor.close();
//        } else {
//            Toast.makeText(this, "Failed query!", Toast.LENGTH_SHORT).show();
//        }
//    }

//    public void showall(View v) {
//        String[] projection = {getString(R.string.USER_PASSWORD)}; // table columns
//
//        String selection = "Password = '123123'";
//
//        Cursor cursor = getContentResolver().query(CONTENT_USER_URI, projection, selection, null, null);
//        StringBuilder stringBuilderQueryResult = new StringBuilder("");
//
//        if (cursor.moveToFirst()) {
//            cursor.moveToFirst();
//            stringBuilderQueryResult.append(cursor.getString(0));
//            view_showall.setText(stringBuilderQueryResult);
//            Log.d("PASSORD: ", stringBuilderQueryResult.toString());
//
//            cursor.close();
//        } else {
//            Toast.makeText(this, "Failed query!", Toast.LENGTH_SHORT).show();
//        }
//    }
}
