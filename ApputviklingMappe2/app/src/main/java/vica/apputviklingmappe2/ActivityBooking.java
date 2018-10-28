package vica.apputviklingmappe2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toolbar;

import java.util.ArrayList;

public class ActivityBooking extends FragmentActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    private Toolbar toolbar;
    private Helper helper;
    private Session session;
    private DB db;

    private ImageButton buttonDate;
    private ImageButton buttonTime;
    private Button buttonNext;
    private TextView textDate;
    private TextView textTime;
    private int hours;
    private int minutes;
    private int day;
    private int month;
    private int year;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCodes.REQUEST_BOOKING) {
            if (resultCode == ResultCodes.RESULT_ORDER_CONFIRMED) {
                setResult(ResultCodes.RESULT_ORDER_CONFIRMED);
                this.finish();
            }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int outYear, int outMonth, int outDay) {
        year = outYear;
        month = outMonth;
        day = outDay;
        String date = outDay + "/" + outMonth + "/" + outYear;
        textDate.setText(date);
    }

    @Override
    public void onTimeSet(TimePicker view, int outHours, int outMinutes) {
        hours = outHours;
        minutes = outMinutes;
        String time = outHours + ":";
        if(outHours < 10) {
            time = "0" + outHours + ":";
        }
        if (outMinutes < 10) {
            time = time + "0" + outMinutes;
        } else {
            time = time + outMinutes;
        }
        textTime.setText(time);
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

        textDate = (TextView) findViewById(R.id.restaurant_date);
        buttonDate = (ImageButton) findViewById(R.id.restaurant_date_button);
        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dateFragment = new FragmentDatePicker();
                dateFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        textTime = (TextView) findViewById(R.id.restaurant_time);
        buttonTime = (ImageButton) findViewById(R.id.restaurant_time_button);
        buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timeFragment = new FragmentTimePicker();
                timeFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

        buttonNext = (Button) findViewById(R.id.restaurant_next_button);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int indexSelected = -1;
                SparseBooleanArray checked = restaurantList.getCheckedItemPositions();
                for(String s : restaurantListArray) {
                    if(checked.get(restaurantListArray.indexOf(s))) {
                        indexSelected = restaurantListArray.indexOf(s);
                    }
                }
                if (indexSelected > -1 && !textDate.getText().toString().equals(getString(R.string.choose_date)) && !textTime.getText().toString().equals(getString(R.string.choose_time))) {
                    Intent intent = new Intent(ActivityBooking.this, ActivityBookingFriendSelection.class);
                    intent.putExtra(getString(R.string.restaurant_name), restaurantListArray.get(indexSelected));
                    intent.putExtra(getString(R.string.date), textDate.getText().toString());
                    intent.putExtra(getString(R.string.time), textTime.getText().toString());
                    startActivityForResult(intent, RequestCodes.REQUEST_BOOKING);
                }
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
                setResult(ResultCodes.RESULT_FINISH);
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