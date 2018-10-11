package vica.apputviklingmappe2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    private EditText loginEmail;
    private EditText loginPassword;
    private Button loginButtonLogin;
    private Button loginButtonSignup;
    private Toolbar toolbar;

    SQLiteDatabase db;

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

    public String getEmail(String email) {
        String[] projection = {getString(R.string.USER_ID)}; // table columns

        String selection = getString(R.string.USER_ID) + "="+"'"+email+"'";

        Cursor cursor = getContentResolver().query(CONTENT_URI, projection, selection, null, null);
//        Cursor cursor = db.rawQuery("SELECT Email FROM User WHERE Email ="+"'"+email+"'",null);
        StringBuilder stringBuilderQueryResult = new StringBuilder("");

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            stringBuilderQueryResult.append(cursor.getString(0));
            cursor.close();
        } else {
            Log.d("EMAIL: ", stringBuilderQueryResult.toString());
        }
        return stringBuilderQueryResult.toString();
    }

    public String getPassword(String password) {
        String[] projection = {getString(R.string.USER_PASSWORD)}; // table columns

        String selection = getString(R.string.USER_PASSWORD)+ "="+"'"+password+"'";

        Cursor cursor = getContentResolver().query(CONTENT_URI, projection, selection, null, null);
//        Cursor cursor = db.rawQuery("SELECT Password FROM User WHERE Password ="+"'"+password+"'", null);
        StringBuilder stringBuilderQueryResult = new StringBuilder("");

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            stringBuilderQueryResult.append(cursor.getString(0));
            cursor.close();
        } else {
            Log.d("PASSORD: ", stringBuilderQueryResult.toString());
        }
        return stringBuilderQueryResult.toString();
    }

//    public Cursor getEmail(String email){
//        return db.rawQuery("SELECT Email FROM User WHERE Email ="+email,null);
//    }
//    public Cursor getPassword(String password){
//        return db.rawQuery("SELECT Password FROM User WHERE Password ="+password, null);
//    }

    public boolean validate() {
        boolean valid;

        if (getEmail(loginEmail.getText().toString()) == loginEmail.getText().toString() && getPassword(loginPassword.getText().toString()) == loginPassword.getText().toString()) {
            valid = true;
            System.out.println("Email true: " + getEmail(loginEmail.getText().toString()));
            System.out.println("Password true: " + getPassword(loginPassword.getText().toString()));
            return valid;
        } else {
            loginEmail.setError("Invalid email or password");
            System.out.println("Email false: " + getEmail(loginEmail.getText().toString()));
            System.out.println("Password false: " + getPassword(loginPassword.getText().toString()));
            System.out.println("Email input: " + loginEmail.getText().toString());
            System.out.println("Password input: " + loginPassword.getText().toString());
            valid = false;
            return valid;
        }
    }

    public void login(View v) {
        if (validate()) {
            Intent intent = new Intent(getApplicationContext(), DBTest.class);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Wrong password or email!", Toast.LENGTH_SHORT).show();
            System.out.println("Email validatefailed: " + getEmail(loginEmail.getText().toString()));
            System.out.println("Password validatefailed: " + getPassword(loginPassword.getText().toString()));
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
