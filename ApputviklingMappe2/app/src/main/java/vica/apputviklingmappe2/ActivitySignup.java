package vica.apputviklingmappe2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;

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

    private EditText firstName;
    private TextView firstNameFeedback;
    private EditText lastName;
    private TextView lastNameFeedback;
    private EditText phone;
    private TextView phoneFeedback;
    private EditText email;
    private EditText emailConfirm;
    private TextView emailConfirmFeedback;
    private EditText password;
    private EditText passwordConfirm;
    private TextView passwordFeedback;
    private Button signupButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        setupToolbar();
        setupFields();
        setupListeners();
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

    private void setupListeners() {
        OnTextChangedListener firstNameOnTextChangedListener = new OnTextChangedListener(firstName, null, firstNameFeedback, "^[A-Z][A-Za-z '-]+$", getString(R.string.invalid_first_name), null);
        firstName.addTextChangedListener(firstNameOnTextChangedListener);
        OnTextChangedListener lastNameOnTextChangedListener = new OnTextChangedListener(lastName, null, lastNameFeedback, "^[A-Z][A-Za-z '-]+$", getString(R.string.invalid_last_name), null);
        lastName.addTextChangedListener(lastNameOnTextChangedListener);
        OnTextChangedListener phoneOnTextChangedListener = new OnTextChangedListener(phone, null, phoneFeedback, "^[0-9]{8}$", getString(R.string.invalid_phone), null);
        phone.addTextChangedListener(phoneOnTextChangedListener);
        OnTextChangedListener emailOnTextChangedListener = new OnTextChangedListener(email, emailConfirm, emailConfirmFeedback, "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$", getString(R.string.invalid_email1), getString(R.string.invalid_email12));
        email.addTextChangedListener(emailOnTextChangedListener);
        OnTextChangedListener emailConfirmOnTextChangedListener = new OnTextChangedListener(emailConfirm, email, emailConfirmFeedback, "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$", getString(R.string.invalid_email1), getString(R.string.invalid_email12));
        emailConfirm.addTextChangedListener(emailConfirmOnTextChangedListener);
        OnTextChangedListener passwordOnTextChangedListener = new OnTextChangedListener(password, passwordConfirm, passwordFeedback, "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,20}$", getString(R.string.invalid_password1), getString(R.string.invalid_password2));
        password.addTextChangedListener(passwordOnTextChangedListener);
        OnTextChangedListener passwordConfirmOnTextChangedListener = new OnTextChangedListener(passwordConfirm, password, passwordFeedback, "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,20}$", getString(R.string.invalid_password1), getString(R.string.invalid_password2));
        passwordConfirm.addTextChangedListener(passwordConfirmOnTextChangedListener);
    }

    private void setupFields() {
        firstName = (EditText)findViewById(R.id.signup_first_name);
        firstNameFeedback = (TextView)findViewById(R.id.signup_first_name_feedback);
        lastName = (EditText)findViewById(R.id.signup_last_name);
        lastNameFeedback = (TextView)findViewById(R.id.signup_last_name_feedback);
        phone = (EditText)findViewById(R.id.signup_phone);
        phoneFeedback = (TextView)findViewById(R.id.signup_phone_feedback);
        email = (EditText)findViewById(R.id.signup_email);
        emailConfirm = (EditText)findViewById(R.id.signup_email_confirm);
        emailConfirmFeedback = (TextView)findViewById(R.id.signup_email_confirm_feedback);
        password  = (EditText)findViewById(R.id.signup_password);
        passwordFeedback = (TextView)findViewById(R.id.signup_password_feedback);
        passwordConfirm  = (EditText)findViewById(R.id.signup_password_confirm);
        signupButton = (Button)findViewById(R.id.signup_button);
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

//    public void signup_button(View v){
//        ContentValues values = new ContentValues();
//
//        String firstnameV = firstName.getText().toString();
//        String lastnameV = lastName.getText().toString();
//        String emailV = email.getText().toString();
//        String phonenrV = phone.getText().toString();
//        String passwordV = password.getText().toString();
//
//        values.put(USER_FIRSTNAME, firstnameV);
//        values.put(USER_LASTNAME, lastnameV);
//        values.put(USER_EMAIL, emailV);
//        values.put(USER_PHONE, phonenrV);
//        values.put(USER_PASSWORD, passwordV);
//
//        Uri uri = getContentResolver().insert( CONTENT_URI, values);
//        firstName.setText("");
//        lastName.setText("");
//        email.setText("");
//        phone.setText("");
//        password.setText("");
//
//        Toast.makeText(this, "Registered!", Toast.LENGTH_SHORT).show();
//    }
}
