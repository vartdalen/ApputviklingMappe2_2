package vica.contentprovider;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static vica.contentprovider.RestaurantProvider.CONTENT_FRIENDS_URI;
import static vica.contentprovider.RestaurantProvider.CONTENT_USER_URI;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ContentValues values = new ContentValues();
        values.put("Userlevel", 1);
        values.put("Firstname", "Admin");
        values.put("Lastname", "Admin");
        values.put("Email", "admin@live.no");
        values.put("Phonenumber", "46842543");
        values.put("Password", "123123");

        ContentValues values1 = new ContentValues();
        values1.put("Firstname", "Rasmus");
        values1.put("Lastname", "Ola");
        values1.put("Phonenumber", "46842541");
        values1.put("UserEmail", "admin@live.no");

        ContentValues values2 = new ContentValues();
        values2.put("Firstname", "Carlo");
        values2.put("Lastname", "Nguyen");
        values2.put("Phonenumber", "46842542");
        values2.put("UserEmail", "admin@live.no");

        ContentValues values3 = new ContentValues();
        values3.put("Firstname", "Viktor");
        values3.put("Lastname", "Vartdal");
        values3.put("Phonenumber", "46842543");
        values3.put("UserEmail", "admin@live.no");

        getContentResolver().insert(CONTENT_USER_URI, values);
        getContentResolver().insert(CONTENT_FRIENDS_URI, values1);
        getContentResolver().insert(CONTENT_FRIENDS_URI, values2);
        getContentResolver().insert(CONTENT_FRIENDS_URI, values3);

    }
}
