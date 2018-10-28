package vica.contentprovider;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static vica.contentprovider.RestaurantProvider.CONTENT_FRIEND_URI;
import static vica.contentprovider.RestaurantProvider.CONTENT_ORDERLINE_URI;
import static vica.contentprovider.RestaurantProvider.CONTENT_ORDER_URI;
import static vica.contentprovider.RestaurantProvider.CONTENT_RESTAURANT_URI;
import static vica.contentprovider.RestaurantProvider.CONTENT_USER_URI;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Brukere
        initUser();
        // Venner
        initFriends();
        // Restaurant
        initRestaurant();
    }

    private void initUser(){
        ContentValues values1 = new ContentValues();
        values1.put("Userlevel", 2);
        values1.put("Firstname", "Admin");
        values1.put("Lastname", "Admin");
        values1.put("Email", "admin@live.no");
        values1.put("Phonenumber", "42842543");
        try {
            values1.put("Password", hash("123123"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        getContentResolver().insert(CONTENT_USER_URI, values1);

        ContentValues values2 = new ContentValues();
        values2.put("Userlevel", 1);
        values2.put("Firstname", "Bruce");
        values2.put("Lastname", "Willis");
        values2.put("Email", "bruce@gmail.com");
        values2.put("Phonenumber", "24240505");
        try {
            values2.put("Password", hash("123123"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        getContentResolver().insert(CONTENT_USER_URI, values2);
    }

    private void initFriends(){
        ContentValues values1 = new ContentValues();
        values1.put("Firstname", "Rasmus");
        values1.put("Lastname", "Nordmann");
        values1.put("Phonenumber", "48842541");
        values1.put("UserEmail", "admin@live.no");
        getContentResolver().insert(CONTENT_FRIEND_URI, values1);

        ContentValues values2 = new ContentValues();
        values2.put("Firstname", "Carlo");
        values2.put("Lastname", "Nguyen");
        values2.put("Phonenumber", "45842542");
        values2.put("UserEmail", "admin@live.no");
        getContentResolver().insert(CONTENT_FRIEND_URI, values2);

        ContentValues values3 = new ContentValues();
        values3.put("Firstname", "Viktor");
        values3.put("Lastname", "Vartdal");
        values3.put("Phonenumber", "46842543");
        values3.put("UserEmail", "bruce@gmail.com");
        getContentResolver().insert(CONTENT_FRIEND_URI, values3);
    }

    private void initRestaurant(){
        ContentValues values1 = new ContentValues();
        values1.put("Name", "Balkan Kebab");
        values1.put("Address", "Mauritz Hansens gate 5");
        values1.put("Phonenumber", "22599522");
        values1.put("Type", "Fast food");
        getContentResolver().insert(CONTENT_RESTAURANT_URI, values1);

        ContentValues values2 = new ContentValues();
        values2.put("Name", "Bislett Kebab");
        values2.put("Address", "Hegdehaugsveien 2");
        values2.put("Phonenumber", "22468044");
        values2.put("Type", "Fast food");
        getContentResolver().insert(CONTENT_RESTAURANT_URI, values2);

        ContentValues values3 = new ContentValues();
        values3.put("Name", "Jamie's Italian Aker Brygge");
        values3.put("Address", "Stranden 63");
        values3.put("Phonenumber", "22877777");
        values3.put("Type", "Fine dining");
        getContentResolver().insert(CONTENT_RESTAURANT_URI, values3);

        ContentValues values4 = new ContentValues();
        values4.put("Name", "Sumo Restaurant Solli Plass");
        values4.put("Address", "Henrik Ibsens gate 90");
        values4.put("Phonenumber", "22547171");
        values4.put("Type", "Fine dining");
        getContentResolver().insert(CONTENT_RESTAURANT_URI, values4);

        ContentValues values5 = new ContentValues();
        values5.put("Name", "Hereford Steakhouse");
        values5.put("Address", "Rosenkrantz' gate 7");
        values5.put("Phonenumber", "22333711");
        values5.put("Type", "Barbecue");
        getContentResolver().insert(CONTENT_RESTAURANT_URI, values5);

        ContentValues values6 = new ContentValues();
        values6.put("Name", "Way Down South");
        values6.put("Address", "Thorvald Meyers gate 47A");
        values6.put("Phonenumber", "94824131");
        values6.put("Type", "Barbecue");
        getContentResolver().insert(CONTENT_RESTAURANT_URI, values6);
    }

    public String hash(String unhashed) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashed = digest.digest(unhashed.getBytes(StandardCharsets.UTF_8));
        return new String(hashed);
    }
}
