package vica.apputviklingmappe2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;

import java.security.NoSuchAlgorithmException;

public class ActivitySignup extends Activity {

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
    private Button buttonSignup;

    private Toolbar toolbar;
    private Helper helper;
    private DB db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        db = new DB();
        helper = new Helper();
        setupToolbar();
        setupFields();
        setupListeners();
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_logged_out);
        toolbar.setTitle(getString(R.string.signup));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(ResultCodes.RESULT_FINISH);
                finish();
            }
        });
        toolbar.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                setResult(ResultCodes.RESULT_QUIT);
                helper.quit(ActivitySignup.this);
                return true;
            }
        });
    }

    private void setupListeners() {
        OnTextChangedListener firstNameOnTextChangedListener = new OnTextChangedListener(firstName, null, firstNameFeedback, "^[A-Z][A-Za-z '-]+$", getString(R.string.error_first_name), null);
        firstName.addTextChangedListener(firstNameOnTextChangedListener);
        OnTextChangedListener lastNameOnTextChangedListener = new OnTextChangedListener(lastName, null, lastNameFeedback, "^[A-Z][A-Za-z '-]+$", getString(R.string.error_last_name), null);
        lastName.addTextChangedListener(lastNameOnTextChangedListener);
        OnTextChangedListener phoneOnTextChangedListener = new OnTextChangedListener(phone, null, phoneFeedback, "^[0-9]{8}$", getString(R.string.error_phone), null);
        phone.addTextChangedListener(phoneOnTextChangedListener);
        OnTextChangedListener emailOnTextChangedListener = new OnTextChangedListener(email, emailConfirm, emailConfirmFeedback, "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$", getString(R.string.error_email1), getString(R.string.error_email2));
        email.addTextChangedListener(emailOnTextChangedListener);
        OnTextChangedListener emailConfirmOnTextChangedListener = new OnTextChangedListener(emailConfirm, email, emailConfirmFeedback, "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$", getString(R.string.error_email1), getString(R.string.error_email2));
        emailConfirm.addTextChangedListener(emailConfirmOnTextChangedListener);
        OnTextChangedListener passwordOnTextChangedListener = new OnTextChangedListener(password, passwordConfirm, passwordFeedback, "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,20}$", getString(R.string.error_password1), getString(R.string.error_password2));
        password.addTextChangedListener(passwordOnTextChangedListener);
        OnTextChangedListener passwordConfirmOnTextChangedListener = new OnTextChangedListener(passwordConfirm, password, passwordFeedback, "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,20}$", getString(R.string.error_password1), getString(R.string.error_password2));
        passwordConfirm.addTextChangedListener(passwordConfirmOnTextChangedListener);
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
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
        buttonSignup = (Button)findViewById(R.id.signup_button);
    }

    public void signup() {

        final ProgressDialog progressDialog = new ProgressDialog(ActivitySignup.this);
        progressDialog.setMessage(getString(R.string.validating));
        progressDialog.show();

        new Handler().postDelayed( new Runnable() {
            public void run() {
                try {
                    if (validate()) {
                        onSignupSuccess();
                    }
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, 1500);
    }

    private boolean validate() throws NoSuchAlgorithmException {
        try {
            ContentValues values = new ContentValues();
            values.put(getString(R.string.USER_LEVEL), 1);
            values.put(getString(R.string.USER_FIRSTNAME), firstName.getText().toString());
            values.put(getString(R.string.USER_LASTNAME), lastName.getText().toString());
            values.put(getString(R.string.USER_ID), email.getText().toString());
            values.put(getString(R.string.USER_PHONE), phone.getText().toString());
            values.put(getString(R.string.USER_PASSWORD), helper.hash(password.getText().toString()));

            if (firstName.getText().length() > 0 && firstNameFeedback.getText().length() == 0
                    && lastName.getText().length() > 0 && lastNameFeedback.getText().length() == 0
                    && phone.getText().length() > 0 && phoneFeedback.getText().length() == 0
                    && email.getText().length() > 0 && emailConfirmFeedback.getText().length() == 0
                    && password.getText().length() > 0 && passwordFeedback.getText().length() == 0) {

                if (db.getEmail(email.getText().toString(), this).equals(email.getText().toString())) {
                    emailConfirmFeedback.setText(getString(R.string.error_email3));
                    return false;
                } else {
                    if ((getContentResolver().insert(DB.CONTENT_USER_URI, values) != null)) {
                        return true;
                    } else {
                        passwordFeedback.setText(getString(R.string.error_signup2));
                        return false;
                    }
                }
            } else {
                passwordFeedback.setText(getString(R.string.error_signup1));
                return false;
            }
        }catch (Exception e){}
        Toast.makeText(this, getString(R.string.DB_Error_Message), Toast.LENGTH_SHORT).show();
        return false;
    }

    private void onSignupSuccess() {
        setResult(RESULT_OK);
        this.finish();
    }
}
