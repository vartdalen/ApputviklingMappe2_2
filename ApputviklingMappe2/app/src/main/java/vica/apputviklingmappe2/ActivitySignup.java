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

    private Button signup_button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firstname = (EditText)findViewById(R.id.signup_first_name);
//        lastname = (EditText)findViewById(R.id.lastname);
//        email = (EditText)findViewById(R.id.signup_email);
//        phonenr = (EditText)findViewById(R.id.phonenr);
//        password  = (EditText)findViewById(R.id.signup_password);

        signup_button = (Button)findViewById(R.id.signup_button);

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

    public void signup_button(View v){
        ContentValues values = new ContentValues();

        String firstnameV = firstname.getText().toString();
//        String lastnameV = lastname.getText().toString();
//        String emailV = email.getText().toString();
//        String addressV = address.getText().toString();
//        String phonenrV = phonenr.getText().toString();
//        String passwordV = password.getText().toString();
//        String zipcodeV = zipcode.getText().toString();
//        String zipareaV = ziparea.getText().toString();

        values.put(USER_FIRSTNAME, firstnameV);
//        values.put(USER_LASTNAME, lastnameV);
//        values.put(USER_EMAIL, emailV);
//        values.put(USER_ADDRESS, addressV);
//        values.put(USER_PHONE, phonenrV);
//        values.put(USER_PASSWORD, passwordV);
//        values.put(ZIP_CODE, zipcodeV);
//        values.put(AREA, zipareaV);

        Uri uri = getContentResolver().insert( CONTENT_URI, values);
        firstname.setText("");
//        lastname.setText("");
//        email.setText("");
//        phonenr.setText("");
//        password.setText("");

        Toast.makeText(this, "Registered!", Toast.LENGTH_SHORT).show();
    }
}
