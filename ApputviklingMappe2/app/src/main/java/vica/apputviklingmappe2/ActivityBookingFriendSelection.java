package vica.apputviklingmappe2;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;


public class ActivityBookingFriendSelection extends Activity {

    private Toolbar toolbar;
    private Helper helper;
    private Session session;
    private DB db;

    private Button buttonConfirm;
    private String restaurantName;
    private String restaurantDate;
    private String restaurantTime;

    private ListView friendList;
    private ListView friendSelectedList;
    private ArrayAdapter<String> listAdapter;
    private ArrayAdapter<String> listSelectedAdapter;
    private ArrayList<String> friendListArray;
    private ArrayList<String> friendSelectedListArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        session = new Session(ActivityBookingFriendSelection.this);
        if(session.getUserLevel() < 1) {
            finish();
            Intent intent = new Intent(ActivityBookingFriendSelection.this, ActivityLogin.class);
            startActivity(intent);
        }
        restaurantName = getIntent().getStringExtra(getString(R.string.restaurant_name));
        restaurantDate = getIntent().getStringExtra(getString(R.string.date));
        restaurantTime = getIntent().getStringExtra(getString(R.string.time));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_friend_selection);

        helper = new Helper();
        db = new DB();
        setupToolbar();
        setupFields();
        populateFriendList();
    }

    private void populateFriendList() {
        String selection = getString(R.string.FRIEND_FK) + "=" + "'"+session.getEmail()+"'";
        Cursor cur = getContentResolver().query(DB.CONTENT_FRIEND_URI, null, selection, null, null);
        friendList.setAdapter(listAdapter);
        if(cur != null && cur.moveToFirst()) {
            do {
                friendListArray.add((cur.getString(0)) + " " +
                        (cur.getString(1)) + " " +
                        (cur.getString(2)) + " " +
                        (cur.getString(3)));
                listAdapter.notifyDataSetChanged();
            }
            while(cur.moveToNext());
            cur.close();
        }
    }

    private void getSelectedFriend(String friend){
        friendSelectedListArray.add(friend);
        friendSelectedList.setAdapter(listSelectedAdapter);
        listSelectedAdapter.notifyDataSetChanged();
    }
    private void setSelectedFriend(String friend){
        friendListArray.add(friend);
        friendList.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }

    private void setupFields() {
        friendListArray = new ArrayList<>();
        friendSelectedListArray = new ArrayList<>();
        listSelectedAdapter = new ArrayAdapter<>(this, R.layout.list_add_friend, R.id.friend_textview, friendSelectedListArray);
        listAdapter = new ArrayAdapter<>(this, R.layout.list_add_friend, R.id.friend_textview, friendListArray);

        buttonConfirm = (Button) findViewById(R.id.booking_confirm_button);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ActivityBookingFriendSelection.this);
                View view = getLayoutInflater().inflate(R.layout.dialog_booking_confirm, null);
                final TextView bookingRestaurantName = (TextView) view.findViewById(R.id.booking_dialog_restaurant_name);
                final TextView bookingDate = (TextView) view.findViewById(R.id.booking_dialog_restaurant_date);
                final TextView bookingTime = (TextView) view.findViewById(R.id.booking_dialog_restaurant_time);
                final TextView bookingFriendQuantity = (TextView) view.findViewById(R.id.booking_dialog_friend_quantity);

                bookingRestaurantName.setText(restaurantName);
                bookingDate.setText(restaurantDate);
                bookingTime.setText(restaurantTime);
                bookingFriendQuantity.setText(Integer.toString(friendSelectedListArray.size()));

                dialogBuilder.setView(view);
                final AlertDialog dialog = dialogBuilder.create();
                setupButtonsForDialog(dialog, view);
            }
        });

        friendList = (ListView)findViewById(R.id.friend_list);
        friendList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int listPos = friendList.getPositionForView(view);
                if(!friendList.isItemChecked(listPos)) {
                    friendList.setItemChecked(listPos, false);
                } else if (friendList.isItemChecked(listPos)){
                    friendList.setItemChecked(listPos, true);
                    getSelectedFriend(friendListArray.get(listPos));
                    friendListArray.remove(listPos);
                    listAdapter.notifyDataSetChanged();
                    friendList.setAdapter(listAdapter);
                }
            }
        });

        friendSelectedList = (ListView)findViewById(R.id.friend_selected_list);
        friendSelectedList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        friendSelectedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int listPos = friendSelectedList.getPositionForView(view);
                if(!friendSelectedList.isItemChecked(listPos)) {
                    friendSelectedList.setItemChecked(listPos, false);
                } else if (friendSelectedList.isItemChecked(listPos)){
                    friendSelectedList.setItemChecked(listPos, true);
                    setSelectedFriend(friendSelectedListArray.get(listPos));
                    friendSelectedListArray.remove(listPos);
                    listSelectedAdapter.notifyDataSetChanged();
                    friendSelectedList.setAdapter(listSelectedAdapter);
                }
            }
        });
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_logged_in);
        toolbar.setTitle(getString(R.string.booking_friend_selection));
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
                Intent intent = new Intent(ActivityBookingFriendSelection.this, ActivityPreferences.class);
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
                helper.quit(ActivityBookingFriendSelection.this);
                return true;
            }
        });
    }

    private void startService() {
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.notify_friends_and_personal_reminder_timing), session.getPrefTiming());
        intent.putExtra(getString(R.string.notify_friends_message), session.getPrefNotifyFriendsMessage());
        intent.putExtra(getString(R.string.friendSelectedListArray), friendSelectedListArray);
        intent.putExtra(getString(R.string.notify_friends), session.getPrefNotifyFriends());
        intent.putExtra(getString(R.string.personal_reminder), session.getPrefPersonalReminder());
        intent.setAction("vica.apputviklingmappe2");

        sendBroadcast(intent);
    }

    private void setupButtonsForDialog(final AlertDialog dialog, View view) {
        final String sql = getString(R.string.ORDER_ID) + " DESC LIMIT 1";
        final int orderId = db.getId(DB.CONTENT_ORDER_URI, new String[]{getString(R.string.ORDER_ID)}, null, sql, ActivityBookingFriendSelection.this);
        final int resId = Integer.parseInt(helper.parseNumbersFromString(restaurantName));
        Button booking_confirm_button = (Button) view.findViewById(R.id.booking_dialog_confirm_button);
        booking_confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (session.getPrefNotifyFriends() && !requestPermission()) {
                return;
            }
            final ProgressDialog progressDialog = new ProgressDialog(ActivityBookingFriendSelection.this);
            progressDialog.setMessage(getString(R.string.ordering));
            progressDialog.show();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (!db.createOrder(ActivityBookingFriendSelection.this, resId, session.getEmail(), restaurantDate, restaurantTime)) {
                        return;
                    } else {
                        for (String s : friendSelectedListArray) {
                            int friendId = Integer.parseInt(helper.parseNumbersFromString(s));
                            db.createOrderline(ActivityBookingFriendSelection.this, friendId, orderId);
                        }
                        startService();
                        setResult(ResultCodes.RESULT_ORDER_CONFIRMED);
                        finish();
                    }
                    progressDialog.dismiss();
                }
            }, 12000);
            dialog.dismiss();
            }
        });
        dialog.show();

        Button booking_cancel_button = (Button) view.findViewById(R.id.booking_dialog_cancel_button);
        booking_cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private boolean requestPermission() {
        int MY_PERMISSIONS_REQUEST_SEND_SMS = ActivityCompat.checkSelfPermission(ActivityBookingFriendSelection.this, Manifest.permission.SEND_SMS);
        if(MY_PERMISSIONS_REQUEST_SEND_SMS == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(ActivityBookingFriendSelection.this, new String[]{Manifest.permission.SEND_SMS}, 0);
            return true;
        }
    }

}
