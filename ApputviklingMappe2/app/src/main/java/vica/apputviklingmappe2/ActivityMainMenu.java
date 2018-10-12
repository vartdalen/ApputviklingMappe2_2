package vica.apputviklingmappe2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toolbar;

public class ActivityMainMenu extends Activity {
    private static final int REQUEST_MAIN_MENU = 11;

    private Toolbar toolbar;
    private SharedPreferences preferences;
    private Helper helper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(preferences.getInt(getString(R.string.user_level), 0) < 1) {
            finish();
            Intent intent = new Intent(ActivityMainMenu.this, ActivityLogin.class);
            startActivityForResult(intent, REQUEST_MAIN_MENU);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        helper = new Helper();
        setupToolbar();
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
                preferences.edit().putInt(getString(R.string.user_level), 0).apply();
                finish();
                Intent intent = new Intent(ActivityMainMenu.this, ActivityLogin.class);
                startActivityForResult(intent, REQUEST_MAIN_MENU);
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

}
