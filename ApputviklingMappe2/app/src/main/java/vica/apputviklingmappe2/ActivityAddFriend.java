package vica.apputviklingmappe2;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;

import static vica.apputviklingmappe2.DB.CONTENT_FRIENDS_URI;

public class ActivityAddFriend extends Activity {

    private Button deleteFriendButton;
    private Button addFriendButton;

    private ListView friendList;
    private ArrayAdapter<String> listAdapter;

    private Toolbar toolbar;
    private Helper helper;
    private Session session;
    private DB db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        session = new Session(ActivityAddFriend.this);
        helper = new Helper();
        db = new DB();
        setupToolbar();
        setupFields();
        setupListeners();
        populateFriendList();

        if(session.getUserLevel() < 1) {
            finish();
            Intent intent = new Intent(ActivityAddFriend.this, ActivityLogin.class);
            startActivity(intent);
        }
    }

    private void populateFriendList() {
        friendList = (ListView)findViewById(R.id.friend_listview);
        Cursor cur = getContentResolver().query(CONTENT_FRIENDS_URI, null, null, null, null);
        StringBuilder stringBuilderQueryResult = new StringBuilder("");
        ArrayList<String> friendListArray = new ArrayList<>();
        listAdapter = new ArrayAdapter<>(this, R.layout.activity_manage_friends, R.id.friend_textview, friendListArray);
        friendList.setAdapter(listAdapter);
        if(cur != null && cur.moveToFirst()) {
            do {
                friendListArray.add((cur.getString(0)) + " " +
                        (cur.getString(1)) + " " +
                        (cur.getString(2)) + " " +
                        (cur.getString(3)) + " " +
                        (cur.getString(4)));
                listAdapter.notifyDataSetChanged();
            }
            while(cur.moveToNext());
            cur.close();
        }
        else{
            Toast.makeText(this, "Failed query!", Toast.LENGTH_SHORT).show();
        }

    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_logged_in);
        toolbar.setTitle(getString(R.string.add_a_friend));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                setResult(ResultCodes.RESULT_LOGOUT);
                finish();
                return true;
            }
        });
        toolbar.getMenu().getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                setResult(ResultCodes.RESULT_QUIT);
                helper.quit(ActivityAddFriend.this);
                return true;
            }
        });
    }

    private void setupListeners() {
    }

    private void setupFields() {
        addFriendButton = (Button)findViewById(R.id.add_friend_button);
    }

//    public void addFriend(View v){
//        ContentValues values = new ContentValues();
//        values.put(getString(R.string.USER_FIRSTNAME), friendFirstName.getText().toString());
//        values.put(getString(R.string.USER_LASTNAME), friendLastName.getText().toString());
//        values.put(getString(R.string.USER_ID), email.getText().toString());
//        values.put(getString(R.string.USER_PHONE), friendPhone.getText().toString());
//
//        System.out.println(password.getText().length());
//        System.out.println(passwordConfirm.getText().length());
//
//        if(firstName.getText().length() > 0 && firstNameFeedback.getText().length() == 0
//                && lastName.getText().length() > 0 && lastNameFeedback.getText().length() == 0
//                && phone.getText().length() > 0 && phoneFeedback.getText().length() == 0
//                && email.getText().length() > 0 && emailConfirmFeedback.getText().length() == 0
//                && password.getText().length() > 0 && passwordFeedback.getText().length() == 0 ) {
//
//            if(db.getEmail(email.getText().toString(), this).equals(email.getText().toString())){
//                emailConfirmFeedback.setText(getString(R.string.error_email3));
//            }else{
//                if((getContentResolver().insert( CONTENT_FRIENDS_URI, values) != null)){
//                    setResult(RESULT_OK);
//                    this.finish();
//                }
//            }
//        }
//    }

}
