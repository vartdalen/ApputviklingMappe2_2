package vica.apputviklingmappe2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import static vica.apputviklingmappe2.ActivitySignup.CONTENT_URI;

public class ActivityLogin extends Activity {
    private static final String TAG = "ActivityLogin";
    private static final int REQUEST_LOGIN = 10;
    private static final int REQUEST_TEST = 20;

    private EditText loginEmail;
    private EditText loginPassword;
    private Button loginButtonLogin;
    private Button loginButtonSignup;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail = (EditText) findViewById(R.id.login_email);
        loginPassword = (EditText) findViewById(R.id.login_password);
        loginButtonLogin = (Button) findViewById(R.id.login_button_login);
        loginButtonSignup = (Button) findViewById(R.id.login_button_signup);

        setupToolbar();

        loginButtonLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LOGIN) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
            if (resultCode == RESULT_CANCELED) {
                this.finish();
            }
        }else if(requestCode == REQUEST_TEST){
            if (resultCode == RESULT_OK){
                this.finish();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                quit();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void loadSignup(View v) {
        Intent intent = new Intent(getApplicationContext(), ActivitySignup.class);
        startActivityForResult(intent, REQUEST_LOGIN);
    }

    public void loadDBTest() {
        Intent intent = new Intent(getApplicationContext(), DBTest.class);
        startActivityForResult(intent, REQUEST_TEST);
    }

    public void quit() {
        AlertDialog confirm_quit = new AlertDialog.Builder(ActivityLogin.this).create();
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

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_logged_out);
        toolbar.setNavigationIcon(null);
        toolbar.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                quit();
                return true;
            }
        });
    }

//    @Override
//    public void onBackPressed() {
//        // disable going back to the MainActivity
//        moveTaskToBack(true);
//    }

//    public void onLoginSuccess() {
//        buttonLogin.setEnabled(true);
//        finish();
//    }

//    public void onLoginFailed() {
//        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
//        buttonLogin.setEnabled(true);
//    }

    public boolean validate() {
        boolean valid = true;
        // Input fra user i activity_login
        String email = loginEmail.getText().toString();
        String password = loginPassword.getText().toString();

        // Henter data fra databasen for å sammenligne
        Cursor curEmail = getContentResolver().query(CONTENT_URI, new String[]{email}, null, null, null);
        Cursor curPassword = getContentResolver().query(CONTENT_URI, new String[]{password}, null, null, null);

        if (curEmail == null) {
            loginEmail.setError("Enter a valid email address!");
            valid = false;
        } else if(email == curEmail.toString() && password == curPassword.toString()) {
            Toast.makeText(this, "Login successfully!", Toast.LENGTH_SHORT).show();
        }

//        if (password.isEmpty()) {
//            loginPassword.setError("Enter a valid password");
//            valid = false;
//        } else {
//            loginPassword.setError(null);
//        }

        return valid;
    }

    public void login() {
        Log.d(TAG, "Login");

        if (validate()) {
            loadDBTest();
        }else{
//            onLoginFailed();
            return;
        }

//        buttonLogin.setEnabled(false);

//        final ProgressDialog progressDialog = new ProgressDialog(ActivityLogin.this,
//                R.style.ThemeOverlay_AppCompat_Dark);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Authenticating...");
//        progressDialog.show();

        // TODO: Implement your own authentication logic here.

//        new Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onLoginSuccess or onLoginFailed
//                        onLoginSuccess();
//                        // onLoginFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
    }
}
