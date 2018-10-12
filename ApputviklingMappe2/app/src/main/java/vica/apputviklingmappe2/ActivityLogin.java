package vica.apputviklingmappe2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;

import android.content.Intent;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import static vica.apputviklingmappe2.DB.CONTENT_URI;

public class ActivityLogin extends Activity {

    private EditText email;
    private EditText password;
    private TextView feedback;
    private Button buttonLogin;
    private Button buttonSignup;

    private Toolbar toolbar;
    private Helper helper;
    private Session session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        session = new Session(ActivityLogin.this);
        if(session.getUserLevel() > 0) {
            finish();
            Intent intent = new Intent(ActivityLogin.this, ActivityMainMenu.class);
            startActivity(intent);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        helper = new Helper();
        setupToolbar();
        setupFields();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCodes.REQUEST_LOGIN) {
            if (resultCode == RESULT_OK) {
                feedback.setTextColor(getColor(R.color.colorOk));
                feedback.setText(getString(R.string.ok_signup));
            }
            if (resultCode == ResultCodes.RESULT_QUIT) {
                this.finish();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                helper.quit(ActivityLogin.this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_logged_out);
        toolbar.setTitle(getString(R.string.login));
        toolbar.setNavigationIcon(null);
        toolbar.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                helper.quit(ActivityLogin.this);
                return true;
            }
        });
    }

    private void setupFields() {
        email = (EditText) findViewById(R.id.login_email);
        password = (EditText) findViewById(R.id.login_password);
        feedback = (TextView) findViewById(R.id.login_feedback);
        buttonLogin = (Button) findViewById(R.id.login_button_login);
        buttonSignup = (Button) findViewById(R.id.login_button_signup);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLogin.this, ActivitySignup.class);
                startActivityForResult(intent, RequestCodes.REQUEST_LOGIN);
            }
        });
    }

    public void login() {
        final ProgressDialog progressDialog = new ProgressDialog(ActivityLogin.this);
        progressDialog.setMessage(getString(R.string.validating));
        progressDialog.show();
        // TODO: Implement your own authentication logic here.

        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if (!validate()) {
                            feedback.setTextColor(getColor(R.color.colorError));
                            feedback.setText(getString(R.string.error_login));
                        } else {
                            onLoginSuccess();
                        }
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public void onLoginSuccess() {
        session.setUserLevel(1);
        finish();
        Intent intent = new Intent(ActivityLogin.this, ActivityMainMenu.class);
        startActivityForResult(intent, RequestCodes.REQUEST_LOGIN);
    }

    public String getEmail(String email) {
        String[] projection = {getString(R.string.USER_ID)}; // table columns
        String selection = getString(R.string.USER_ID) + "="+"'"+email+"'";

        Cursor cursor = getContentResolver().query(CONTENT_URI, projection, selection, null, null);
        StringBuilder stringBuilderQueryResult = new StringBuilder("");
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            stringBuilderQueryResult.append(cursor.getString(0));
            cursor.close();
        }
        return stringBuilderQueryResult.toString();
    }

    public String getPassword(String password) {
        String[] projection = {getString(R.string.USER_PASSWORD)}; // table columns
        String selection = getString(R.string.USER_PASSWORD)+ "="+"'"+password+"'";

        Cursor cursor = getContentResolver().query(CONTENT_URI, projection, selection, null, null);
        StringBuilder stringBuilderQueryResult = new StringBuilder("");

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            stringBuilderQueryResult.append(cursor.getString(0));
            cursor.close();
        }
        return stringBuilderQueryResult.toString();
    }

    public boolean validate() {
        boolean valid;
        if (getEmail(email.getText().toString()).equals(email.getText().toString()) && email.getText().toString().length() > 0
                && getPassword(password.getText().toString()).equals(password.getText().toString()) && password.getText().toString().length() > 0) {
            valid = true;
            return valid;
        } else {
            valid = false;
            return valid;
        }
    }
}
