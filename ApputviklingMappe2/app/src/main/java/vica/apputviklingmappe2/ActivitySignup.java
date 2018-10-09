package vica.apputviklingmappe2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;

public class ActivitySignup extends Activity {

    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        setupToolbar();
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_logged_out);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_FIRST_USER); finish();
            }
        });
        toolbar.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                quit();
                return true;
            }
        });
    }

    public void quit() {
        AlertDialog confirm_quit = new AlertDialog.Builder(ActivitySignup.this).create();
        confirm_quit.setTitle(getString(R.string.quit));
        confirm_quit.setMessage(getString(R.string.confirmation_quit1));
        confirm_quit.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(RESULT_CANCELED);
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
