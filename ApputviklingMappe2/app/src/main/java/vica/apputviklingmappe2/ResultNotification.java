package vica.apputviklingmappe2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.ArrayList;

public class ResultNotification extends Activity {

    private Toolbar toolbar;
    private Helper helper;
    private Session session;

    private ListView restaurantList;
    private ArrayAdapter<String> listAdapter;
    private ArrayList<String> restaurantListArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        session = new Session(ResultNotification.this);
        if(session.getUserLevel() < 1) {
            finish();
            Intent intent = new Intent(ResultNotification.this, ActivityLogin.class);
            startActivity(intent);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        helper = new Helper();
        setupToolbar();
        setupFields();
        populateRestaurantList();
    }

    private void populateRestaurantList() {
        String order = getString(R.string.ORDER_ID) + " DESC";
        String selection = getString(R.string.ORDER_UserFK) + "=" + "'"+session.getEmail()+"'";
        Cursor cur = getContentResolver().query(DB.CONTENT_ORDER_URI, null, selection, null, order);
        listAdapter = new ArrayAdapter<>(this, R.layout.list_my_orders, R.id.my_orders_textview, restaurantListArray);
        restaurantList.setAdapter(listAdapter);
        if(cur != null && cur.moveToFirst()) {
            do {
                restaurantListArray.add((cur.getString(0)) + " " +
                        (cur.getString(1)) + ", " +
                        (cur.getString(2)) + ", " +
                        (cur.getString(3)) + ", " +
                        (cur.getString(4)));
                listAdapter.notifyDataSetChanged();
            }
            while(cur.moveToNext());
            cur.close();
        }
    }

    private void setupFields() {
        restaurantListArray = new ArrayList<>();
        restaurantList = (ListView)findViewById(R.id.my_order_list);
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_logged_in);
        toolbar.setTitle(getString(R.string.manage_restaurants));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(ResultNotification.this, ActivityPreferences.class);
                startActivity(intent);
                return true;
            }
        });
        toolbar.getMenu().getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                setResult(ResultCodes.RESULT_LOGOUT);
                finish();
                return true;
            }
        });
        toolbar.getMenu().getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                setResult(ResultCodes.RESULT_QUIT);
                helper.quit(ResultNotification.this);
                return true;
            }
        });
    }

}
