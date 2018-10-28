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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.ArrayList;

public class ActivityOrderHistory extends Activity {

    private Button buttonOrderDetail;

    private Toolbar toolbar;
    private Helper helper;
    private Session session;
    private DB db;

    private ListView restaurantList;
    private ArrayAdapter<String> listAdapter;
    private ArrayList<String> restaurantListArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        session = new Session(ActivityOrderHistory.this);
        if(session.getUserLevel() < 1) {
            finish();
            Intent intent = new Intent(ActivityOrderHistory.this, ActivityLogin.class);
            startActivity(intent);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        helper = new Helper();
        db = new DB();
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
                restaurantListArray.add((cur.getString(4)) + ": " +
                        (cur.getString(2)) + ", " +
                        (cur.getString(3)));
                listAdapter.notifyDataSetChanged();
            }
            while(cur.moveToNext());
            cur.close();
        }
    }

    private void setupFields() {
        restaurantListArray = new ArrayList<>();
        restaurantList = (ListView)findViewById(R.id.my_order_list);
        restaurantList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        restaurantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int listPos = restaurantList.getPositionForView(view);
                if(!restaurantList.isItemChecked(listPos)) {
                    restaurantList.setItemChecked(listPos, false);
                } else if (restaurantList.isItemChecked(listPos)){
                    restaurantList.setItemChecked(listPos, true);
                }
            }
        });

        buttonOrderDetail = (Button) findViewById(R.id.order_detail_button);
        buttonOrderDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray checked = restaurantList.getCheckedItemPositions();
                int checkedCounter = 0;
                String id = "";
                for(int i = 0; i < restaurantListArray.size(); i++){
                    if(checked.get(i)){
                        checkedCounter++;
                    }
                }
                if(checkedCounter == 1) {
                    for (String s : restaurantListArray) {
                        if (checked.get(restaurantListArray.indexOf(s))) {
                            id = helper.parseNumbersFromString(s);
                        }
                    }
                }
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ActivityOrderHistory.this);
                View view = getLayoutInflater().inflate(R.layout.dialog_order_details, null);
                final TextView bookingRestaurantName = (TextView) view.findViewById(R.id.booking_dialog_restaurant_name);

                bookingRestaurantName.setText(db.getInfo(DB.CONTENT_RESTAURANT_URI, new String[]{getString(R.string.RESTAURANT_NAME)}, getString(R.string.RESTAURANT_ID)+"="+"'"+id+"'", null,ActivityOrderHistory.this));

                dialogBuilder.setView(view);
                final AlertDialog dialog = dialogBuilder.create();
                dialog.show();
            }
        });
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_logged_out);
        toolbar.setTitle(getString(R.string.Restaurant_booking_orders));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                setResult(ResultCodes.RESULT_QUIT);
                helper.quit(ActivityOrderHistory.this);
                return true;
            }
        });
    }

}
