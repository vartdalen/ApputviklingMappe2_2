package vica.apputviklingmappe2;

import android.app.Activity;
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
import android.widget.ListView;
import android.widget.Toolbar;

import java.util.ArrayList;

public class ActivityBooking extends Activity {
    private Toolbar toolbar;
    private Helper helper;
    private Session session;
    private DB db;

    private Button buttonNext;

    private ListView restaurantList;
    private ArrayAdapter<String> listAdapter;
    private ArrayList<String> restaurantListArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        session = new Session(ActivityBooking.this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        helper = new Helper();
        db = new DB();
        setupToolbar();
        setupFields();
        populateRestaurantList();
    }

    private void populateRestaurantList() {
        Cursor cur = getContentResolver().query(DB.CONTENT_RESTAURANT_URI, null, null, null, null);
        listAdapter = new ArrayAdapter<>(this, R.layout.list_add_restaurant, R.id.restaurant_textview, restaurantListArray);
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

        restaurantList = (ListView)findViewById(R.id.restaurant_list);
        restaurantList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        restaurantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!restaurantList.isItemChecked(restaurantList.getPositionForView(view))) {
                    restaurantList.setItemChecked(restaurantList.getPositionForView(view), false);
                } else {
                    restaurantList.setItemChecked(restaurantList.getPositionForView(view), true);
                }
            }
        });

        buttonNext = (Button) findViewById(R.id.restaurant_next_button);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray checked = restaurantList.getCheckedItemPositions();
                ArrayList<String> temp = new ArrayList<>();
                for(String s : restaurantListArray) {
                    if(checked.get(restaurantListArray.indexOf(s))) {
                        db.deleteRestaurant(ActivityBooking.this, helper.stringParser(s));
                    } else {
                        temp.add(s);
                    }
                }
                restaurantListArray.clear();
                restaurantListArray.addAll(temp);
                listAdapter.notifyDataSetChanged();
                restaurantList.setAdapter(listAdapter);
            }
        });
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_logged_in);
        toolbar.setTitle(getString(R.string.book_table));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(ActivityBooking.this, ActivityPreferences.class);
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
                helper.quit(ActivityBooking.this);
                return true;
            }
        });
    }

}