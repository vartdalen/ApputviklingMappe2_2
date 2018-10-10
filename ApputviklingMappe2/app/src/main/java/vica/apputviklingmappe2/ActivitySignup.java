package vica.apputviklingmappe2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivitySignup extends Activity {

    private Toolbar toolbar;

    public final static String PROVIDER = "vica.contentprovider";
    public static final Uri CONTENT_URI = Uri.parse("content://"+ PROVIDER + "/User");

    // Label table user columns
    public final static String USER_FIRSTNAME = "Firstname";
    public final static String USER_LASTNAME = "Lastname";
    public final static String USER_EMAIL = "Email";
    public final static String USER_PHONE = "Phonenumber";
    public final static String USER_PASSWORD = "Password";

    private EditText firstname;
    private EditText lastname;
    private EditText email;
    private EditText phonenr;
    private EditText password;
    private EditText emailCf;
    private EditText passwordCf;
    private Button signup_button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        setupSignUp();
        setupToolbar();
    }

    private void setupSignUp(){
        firstname = (EditText)findViewById(R.id.signup_first_name);
        lastname = (EditText)findViewById(R.id.signup_last_name);
        email = (EditText)findViewById(R.id.signup_email);
        emailCf = (EditText)findViewById(R.id.signup_confirm_email);
        phonenr = (EditText)findViewById(R.id.signup_phone);
        password  = (EditText)findViewById(R.id.signup_password);
        passwordCf = (EditText)findViewById(R.id.signup_confirm_password);
        signup_button = (Button)findViewById(R.id.signup_button);
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

    public void signup_button(View v){
        ContentValues values = new ContentValues();

        values.put(USER_FIRSTNAME, firstname.getText().toString());
        values.put(USER_LASTNAME, lastname.getText().toString());
        values.put(USER_EMAIL, email.getText().toString());
        values.put(USER_PHONE, phonenr.getText().toString());
        values.put(USER_PASSWORD, password.getText().toString());

        if((getContentResolver().insert( CONTENT_URI, values) != null)){
            firstname.setText("");
            lastname.setText("");
            email.setText("");
            emailCf.setText("");
            phonenr.setText("");
            password.setText("");
            passwordCf.setText("");

            Toast.makeText(this, "User created!", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Registration failed!!", Toast.LENGTH_SHORT).show();
        }
    }
}
