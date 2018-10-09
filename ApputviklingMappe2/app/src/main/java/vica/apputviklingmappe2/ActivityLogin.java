package vica.apputviklingmappe2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.content.Intent;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

public class ActivityLogin extends Activity {
    private static final String TAG = "ActivityLogin";
    private static final int REQUEST_SIGNUP = 0;

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

//        loginButtonLogin.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                login();
//            }
//        });
    }

    public void loadSignup(View v) {
        Intent intent = new Intent(getApplicationContext(), ActivitySignup.class);
        startActivityForResult(intent, REQUEST_SIGNUP);
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
    }

//    public void login() {
//        Log.d(TAG, "Login");
//
//        if (!validate()) {
//            onLoginFailed();
//            return;
//        }
//
//        buttonLogin.setEnabled(false);
//
//        final ProgressDialog progressDialog = new ProgressDialog(ActivityLogin.this,
//                R.style.ThemeOverlay_AppCompat_Dark);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Authenticating...");
//        progressDialog.show();
//
//        String email = inputEmail.getText().toString();
//        String password = inputPassword.getText().toString();
//
//        // TODO: Implement your own authentication logic here.
//
//        new Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onLoginSuccess or onLoginFailed
//                        onLoginSuccess();
//                        // onLoginFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

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
        String email = loginEmail.getText().toString();
        String password = loginPassword.getText().toString();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginEmail.setError("enter a valid email address");
            valid = false;
        } else {
            loginEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            loginPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            loginPassword.setError(null);
        }

        return valid;
    }
}
