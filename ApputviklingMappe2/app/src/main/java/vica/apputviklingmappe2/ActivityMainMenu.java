package vica.apputviklingmappe2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

        setupToolbar();
        System.out.println(preferences.getInt(getString(R.string.user_level), 0));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            quit();
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
                quit();
                return true;
            }
        });
    }

    public void quit() {
        AlertDialog confirm_quit = new AlertDialog.Builder(ActivityMainMenu.this).create();
        confirm_quit.setTitle(getString(R.string.quit));
        confirm_quit.setMessage(getString(R.string.confirmation_quit1));
        confirm_quit.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        confirm_quit.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        confirm_quit.show();
    }

}
