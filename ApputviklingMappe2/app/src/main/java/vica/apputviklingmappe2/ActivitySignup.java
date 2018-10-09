package vica.apputviklingmappe2;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivitySignup extends Activity {

    public final static String PROVIDER = "vica.contentprovider";
    public static final Uri CONTENT_URI = Uri.parse("content://"+ PROVIDER + "/User");

    // Label table user columns
    public final static String USER_FIRSTNAME = "Firstname";
    public final static String USER_LASTNAME = "Lastname";
    public final static String USER_EMAIL = "Email";
    public final static String USER_ADDRESS = "Address";
    public final static String USER_PHONE = "Phonenumber";
    public final static String USER_PASSWORD = "Password";
    public final static String ZIP_CODE = "Zip_Code";
    public final static String AREA = "Zip_Area";

    private EditText firstname;
    private EditText lastname;
    private EditText email;
    private EditText address;
    private EditText phonenr;
    private EditText password;
    private EditText zipcode;
    private EditText ziparea;

    private Button signup_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firstname = (EditText)findViewById(R.id.signup_first_name);
//        lastname = (EditText)findViewById(R.id.lastname);
        email = (EditText)findViewById(R.id.signup_email);
//        address = (EditText)findViewById(R.id.address);
//        phonenr = (EditText)findViewById(R.id.phonenr);
        password  = (EditText)findViewById(R.id.signup_password);
//        zipcode = (EditText)findViewById(R.id.zipcode);
//        ziparea = (EditText)findViewById(R.id.ziparea);

        signup_button = (Button)findViewById(R.id.signup_button);
    }

    public void signup_button(View v){
        ContentValues values = new ContentValues();

        String firstnameV = firstname.getText().toString();
//        String lastnameV = lastname.getText().toString();
        String emailV = email.getText().toString();
//        String addressV = address.getText().toString();
//        String phonenrV = phonenr.getText().toString();
        String passwordV = password.getText().toString();
//        String zipcodeV = zipcode.getText().toString();
//        String zipareaV = ziparea.getText().toString();

        values.put(USER_FIRSTNAME, firstnameV);
//        values.put(USER_LASTNAME, lastnameV);
        values.put(USER_EMAIL, emailV);
//        values.put(USER_ADDRESS, addressV);
//        values.put(USER_PHONE, phonenrV);
        values.put(USER_PASSWORD, passwordV);
//        values.put(ZIP_CODE, zipcodeV);
//        values.put(AREA, zipareaV);

        Uri uri = getContentResolver().insert( CONTENT_URI, values);
        firstname.setText("");
//        lastname.setText("");
        email.setText("");
//        address.setText("");
//        phonenr.setText("");
        password.setText("");
//        zipcode.setText("");
//        ziparea.setText("");

        Toast.makeText(this, "Registered!", Toast.LENGTH_SHORT).show();
    }
}
