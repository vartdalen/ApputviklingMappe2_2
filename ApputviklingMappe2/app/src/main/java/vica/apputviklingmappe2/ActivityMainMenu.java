package vica.apputviklingmappe2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

public class ActivityMainMenu extends Activity {

    private Button buttonBookTable;
    private Button buttonAddFriend;
    private Button buttonManageRestaurants;
    private Button buttonMyOrders;

    private Toolbar toolbar;
    private Helper helper;
    private Session session;
    private DB db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        session = new Session(ActivityMainMenu.this);
        helper = new Helper();
        db = new DB();
        if(session.getUserLevel() < 1) {
            finish();
            Intent intent = new Intent(ActivityMainMenu.this, ActivityLogin.class);
            startActivity(intent);
        }
        if(session.getPrefPersonalReminder()) {
            if (db.compareOrderDate(this, helper.parseSystemDateToDbFormat(), session.getEmail())) {
                helper.createNotification(ActivityMainMenu.this);
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        setupFields();
        setupToolbar();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCodes.REQUEST_MAIN_MENU)
            if (resultCode == ResultCodes.RESULT_LOGOUT) {
                session.destroy();
                finish();
                Intent intent = new Intent(ActivityMainMenu.this, ActivityLogin.class);
                startActivity(intent);
            }
            if (resultCode == ResultCodes.RESULT_QUIT) {
                finish();
            }
            if (resultCode == ResultCodes.ORDER_CONFIRMED) {
                Toast.makeText(this, getString(R.string.order_confirmed), Toast.LENGTH_LONG).show();
            }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            helper.quit(ActivityMainMenu.this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_logged_in);
        toolbar.setTitle(getString(R.string.main_menu));
        toolbar.setNavigationIcon(null);
        toolbar.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(ActivityMainMenu.this, ActivityPreferences.class);
                startActivity(intent);
                return true;
            }
        });
        toolbar.getMenu().getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                session.destroy();
                finish();
                Intent intent = new Intent(ActivityMainMenu.this, ActivityLogin.class);
                startActivity(intent);
                return true;
            }
        });
        toolbar.getMenu().getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                helper.quit(ActivityMainMenu.this);
                return true;
            }
        });
    }

    private void setupFields() {
        buttonBookTable = (Button) findViewById(R.id.main_menu_button_book_table);
        buttonBookTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMainMenu.this, ActivityBooking.class);
                startActivityForResult(intent, RequestCodes.REQUEST_MAIN_MENU);
            }
        });

        buttonAddFriend = (Button) findViewById(R.id.main_menu_button_manage_friends);
        buttonAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMainMenu.this, ActivityManageFriends.class);
                startActivityForResult(intent, RequestCodes.REQUEST_MAIN_MENU);
            }
        });

        buttonManageRestaurants = (Button) findViewById(R.id.main_menu_button_manage_restaurants);
        if(session.getUserLevel() < 2 ) {
            buttonManageRestaurants.setVisibility(View.GONE);
        } else {
            buttonManageRestaurants.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ActivityMainMenu.this, ActivityManageRestaurants.class);
                    startActivityForResult(intent, RequestCodes.REQUEST_MAIN_MENU);
                }
            });
        }

        buttonMyOrders = (Button) findViewById(R.id.main_menu_button_my_orders);
        buttonMyOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMainMenu.this, ActivityOrderHistory.class);
                startActivityForResult(intent, RequestCodes.REQUEST_MAIN_MENU);
            }
        });
    }
}
