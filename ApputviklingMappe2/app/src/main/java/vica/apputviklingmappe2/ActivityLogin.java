package vica.apputviklingmappe2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.security.NoSuchAlgorithmException;

public class ActivityLogin extends Activity {

    private EditText email;
    private EditText password;
    private TextView feedback;
    private Button buttonLogin;
    private Button buttonSignup;

    private Toolbar toolbar;
    private Helper helper;
    private Session session;
    private DB db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        PreferenceManager.setDefaultValues(this, R.xml.activity_preferences, false);
        db = new DB();
        helper = new Helper();
        session = new Session(ActivityLogin.this);
        if(session.getUserLevel() > 0) {
            finish();
            Intent intent = new Intent(ActivityLogin.this, ActivityMainMenu.class);
            startActivity(intent);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
                finish();
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

    private void setupSession() {
        String sqlSelectionOnUserId = getString(R.string.USER_ID)+ "="+"'"+email.getText().toString()+"'";
        session.setEmail(email.getText().toString());
        session.setUserLevel(Integer.parseInt(db.getInfo(DB.CONTENT_USER_URI, new String[]{getString(R.string.USER_LEVEL)}, sqlSelectionOnUserId, null,this)));
        session.setFirstName(db.getInfo(DB.CONTENT_USER_URI, new String[]{getString(R.string.USER_FIRSTNAME)}, sqlSelectionOnUserId, null,this));
        session.setLastName(db.getInfo(DB.CONTENT_USER_URI, new String[]{getString(R.string.USER_LASTNAME)}, sqlSelectionOnUserId, null,this));
        session.setPhone(db.getInfo(DB.CONTENT_USER_URI, new String[]{getString(R.string.USER_PHONE)}, sqlSelectionOnUserId, null,this));
    }

    private void login() {
        final ProgressDialog progressDialog = new ProgressDialog(ActivityLogin.this);
        progressDialog.setMessage(getString(R.string.authenticating));
        progressDialog.show();

        new Handler().postDelayed(new Runnable() {
                    public void run() {
            try {
                if (!validate()) {
                    feedback.setTextColor(getColor(R.color.colorError));
                    feedback.setText(getString(R.string.error_login));
                } else {
                    onLoginSuccess();
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
            }
        }, 1500);
    }

    private void onLoginSuccess() {
        setupSession();
        finish();
        Intent intent = new Intent(ActivityLogin.this, ActivityMainMenu.class);
        startActivityForResult(intent, RequestCodes.REQUEST_LOGIN);
        Toast.makeText(this, "Welcome, " + session.getFirstName(), Toast.LENGTH_LONG).show();
    }

    private boolean validate() throws NoSuchAlgorithmException {
        if (db.verifyUser(email.getText().toString(), helper.hash(password.getText().toString()), this)) {
            return true;
        }
        return false;
    }

}
