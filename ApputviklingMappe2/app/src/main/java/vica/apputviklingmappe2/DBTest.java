package vica.apputviklingmappe2;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static vica.apputviklingmappe2.ActivitySignup.CONTENT_URI;

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
    public void showall(View v) {
        Cursor cur = getContentResolver().query(CONTENT_URI, null, null, null, null);

        String[] projection = {getString(R.string.USER_EMAIL)};
        String selection = "Email = \"" + "admin@gmail.no" + "\"";
        Cursor cur1 = getContentResolver().query(CONTENT_URI, projection, selection, null, null);
        StringBuilder stringBuilderQueryResult = new StringBuilder("");
        StringBuilder stringBuilderQueryResult1 = new StringBuilder("");

//        if(cur != null && cur.moveToFirst()) {
          if(cur1 != null && cur1.moveToFirst()) {
            do {
//                stringBuilderQueryResult.append(
//                        (cur.getString(1)) + " " +
//                        (cur.getString(2)) + " " +
//                        (cur.getString(3)) + " " +
//                        (cur.getString(4)) + " " + "\r\n");
                stringBuilderQueryResult1.append(
                        (cur1.getString(1))
                );
            }
            while(cur1.moveToNext());
            cur1.close();
            view_showall.setText(stringBuilderQueryResult1);
        }
        else{
            Toast.makeText(this, "Failed query!", Toast.LENGTH_SHORT).show();
        }
    }
}
