package vica.apputviklingmappe2;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static vica.apputviklingmappe2.ActivitySignup.CONTENT_URI;

public class DB extends Activity {
    TextView view_showall;
    Button showall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbtest);

        view_showall = (TextView)findViewById(R.id.textview_showall);
        showall = (Button)findViewById(R.id.showall);
    }

    public void showAllUser(View v) {

        Cursor cur = getContentResolver().query(CONTENT_URI, null, null, null, null);
        StringBuilder stringBuilderQueryResult = new StringBuilder("");

        if(cur != null && cur.moveToFirst()) {
            do {
                stringBuilderQueryResult.append(
                        (cur.getString(0)) + " " + // id (email)
                        (cur.getString(1)) + " " + // firstname
                        (cur.getString(2)) + " " + // lastname
                        (cur.getString(3)) + " " + // phone
                        (cur.getString(4)) + " " + "\r\n"); // password
            }
            while(cur.moveToNext());
            cur.close();
            view_showall.setText(stringBuilderQueryResult);
        }
        else{
            Toast.makeText(this, "Failed query!", Toast.LENGTH_SHORT).show();
        }
    }

    public String getEmail(String email) {
        String[] projection = {"Email"}; // table columns

        String selection = "Email = "+"'"+email+"'";

        Cursor cursor = getContentResolver().query(CONTENT_URI, projection, selection, null, null);
        StringBuilder stringBuilderQueryResult = new StringBuilder("");

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            stringBuilderQueryResult.append(cursor.getString(0));
            cursor.close();
        } else {
            Toast.makeText(this, "Failed query!", Toast.LENGTH_SHORT).show();
        }
        return stringBuilderQueryResult.toString();
    }

    public String getPassword(String password) {
        String[] projection = {getString(R.string.USER_ID)}; // table columns

        String selection = "Password = "+"'"+password+"'";

        Cursor cursor = getContentResolver().query(CONTENT_URI, projection, selection, null, null);
        StringBuilder stringBuilderQueryResult = new StringBuilder("");

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            stringBuilderQueryResult.append(cursor.getString(4));
            cursor.close();
        } else {
            Toast.makeText(this, "Failed query!", Toast.LENGTH_SHORT).show();
        }
        return stringBuilderQueryResult.toString();
    }
}
