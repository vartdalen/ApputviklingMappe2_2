package vica.apputviklingmappe2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

public class ActivityMainMenu extends Activity {

    private Button addFriendButton;

    private Toolbar toolbar;
    private Helper helper;
    private Session session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        session = new Session(ActivityMainMenu.this);
        if(session.getUserLevel() < 1) {
            finish();
            Intent intent = new Intent(ActivityMainMenu.this, ActivityLogin.class);
            startActivity(intent);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        helper = new Helper();
        setupToolbar();
        setupFields();
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
                this.finish();
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
                session.destroy();
                finish();
                Intent intent = new Intent(ActivityMainMenu.this, ActivityLogin.class);
                startActivity(intent);
                return true;
            }
        });
        toolbar.getMenu().getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                helper.quit(ActivityMainMenu.this);
                return true;
            }
        });
    }

    private void setupFields() {
        addFriendButton = (Button) findViewById(R.id.main_menu_button_add_friends);
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMainMenu.this, ActivityAddFriend.class);
                startActivityForResult(intent, RequestCodes.REQUEST_MAIN_MENU);
            }
        });
    }

}
