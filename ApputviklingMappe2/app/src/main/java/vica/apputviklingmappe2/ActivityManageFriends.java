package vica.apputviklingmappe2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

public class ActivityManageFriends extends Activity {

    private Toolbar toolbar;
    private Helper helper;
    private Session session;

    private Button buttonAdd;
    private Button buttonDelete;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_friends);

        session = new Session(ActivityManageFriends.this);
        helper = new Helper();
        setupToolbar();
        setupFields();

        if(session.getUserLevel() < 1) {
            finish();
            Intent intent = new Intent(ActivityManageFriends.this, ActivityLogin.class);
            startActivity(intent);
        }
    }

    private void setupFields() {
        buttonAdd = (Button) findViewById(R.id.friend_add_button);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ActivityManageFriends.this);
                View view = getLayoutInflater().inflate(R.layout.dialog_add_friend, null);
                final EditText friend_first_name = (EditText) view.findViewById(R.id.friend_dialog_first_name);
                final EditText friend_last_name = (EditText) view.findViewById(R.id.friend_dialog_last_name);
                final EditText friend_phone = (EditText) view.findViewById(R.id.friend_dialog_phone);
                Button friend_add_button = (Button) view.findViewById(R.id.friend_dialog_add_button);

                friend_add_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!friend_first_name.getText().toString().isEmpty() && !friend_last_name.getText().toString().isEmpty() && !friend_phone.getText().toString().isEmpty()) {

                        }
                    }
                });
                dialogBuilder.setView(view);
                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
            }
        });
        buttonDelete = (Button) findViewById(R.id.friend_delete_button);
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_logged_in);
        toolbar.setTitle(getString(R.string.manage_friends));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                setResult(ResultCodes.RESULT_LOGOUT);
                finish();
                return true;
            }
        });
        toolbar.getMenu().getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                setResult(ResultCodes.RESULT_QUIT);
                helper.quit(ActivityManageFriends.this);
                return true;
            }
        });
    }

}
