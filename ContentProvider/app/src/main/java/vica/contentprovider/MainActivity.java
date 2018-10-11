package vica.contentprovider;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends Activity {

    public static String PROVIDER = "vica.contentprovider" ;
    public static final Uri CONTENT_URI = Uri.parse("content://"+ PROVIDER + "/User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ContentValues values = new ContentValues();
        values.put("Firstname", "Admin");
        values.put("Lastname", "Admin");
        values.put("Email", "admin@live.no");
        values.put("Phonenumber", "46842543");
        values.put("Password", "123123");

        getContentResolver().insert(CONTENT_URI, values);
    }
}
