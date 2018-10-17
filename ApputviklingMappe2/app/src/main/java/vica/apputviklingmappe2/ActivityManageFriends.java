package vica.apputviklingmappe2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.Iterator;

import static vica.apputviklingmappe2.DB.CONTENT_FRIEND_URI;

public class ActivityManageFriends extends Activity {

    private Toolbar toolbar;
    private Helper helper;
    private Session session;
    private DB db;

    private Button buttonAdd;
    private Button buttonDelete;

    private ListView friendList;
    private ArrayAdapter<String> listAdapter;
    private ArrayList<String> friendListArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        session = new Session(ActivityManageFriends.this);
        if(session.getUserLevel() < 1) {
            finish();
            Intent intent = new Intent(ActivityManageFriends.this, ActivityLogin.class);
            startActivity(intent);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_friends);

        helper = new Helper();
        db = new DB();
        setupToolbar();
        setupFields();
        populateFriendList();
    }

    private void populateFriendList() {
        String selection = getString(R.string.FRIEND_FK) + "=" + "'"+session.getEmail()+"'";
        Cursor cur = getContentResolver().query(CONTENT_FRIEND_URI, null, selection, null, null);
        listAdapter = new ArrayAdapter<>(this, R.layout.list_add_friend, R.id.friend_textview, friendListArray);
        friendList.setAdapter(listAdapter);
        if(cur != null && cur.moveToFirst()) {
            do {
                friendListArray.add((cur.getString(0)) + " " +
                        (cur.getString(1)) + " " +
                        (cur.getString(2)) + " " +
                        (cur.getString(3)));
                listAdapter.notifyDataSetChanged();
            }
            while(cur.moveToNext());
            cur.close();
        }
    }

    private void setupFields() {
        friendListArray = new ArrayList<>();
        buttonAdd = (Button) findViewById(R.id.friend_add_button);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ActivityManageFriends.this);
                View view = getLayoutInflater().inflate(R.layout.dialog_add_friend, null);
                final EditText friend_first_name = (EditText) view.findViewById(R.id.friend_dialog_first_name);
                final EditText friend_last_name = (EditText) view.findViewById(R.id.friend_dialog_last_name);
                final EditText friend_phone = (EditText) view.findViewById(R.id.friend_dialog_phone);
                Button friend_add_button = (Button) view.findViewById(R.id.friend_dialog_add_button);
                dialogBuilder.setView(view);
                final AlertDialog dialog = dialogBuilder.create();
                final String sql = getString(R.string.FRIEND_ID) + " DESC LIMIT 1";
                friend_add_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!friend_first_name.getText().toString().isEmpty() && !friend_last_name.getText().toString().isEmpty() && !friend_phone.getText().toString().isEmpty()) {
                            db.addFriend(ActivityManageFriends.this, friend_first_name.getText().toString(), friend_last_name.getText().toString(), friend_phone.getText().toString(), session.getEmail().toString());
                            friendListArray.add(Integer.parseInt(db.getInfo(CONTENT_FRIEND_URI, new String[]{getString(R.string.FRIEND_ID)}, null, sql,ActivityManageFriends.this))+ " "
                                    + friend_first_name.getText().toString() + " " + friend_last_name.getText().toString() + " " + friend_phone.getText().toString());
                            listAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });

        buttonDelete = (Button) findViewById(R.id.friend_delete_button);
        friendList = (ListView)findViewById(R.id.friend_list);
        friendList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(friendList.isItemChecked(position)) {
                    friendList.setItemChecked(position, true);
                    friendList.getChildAt(position).setBackgroundColor(Color.RED);
                } else {
                    friendList.setItemChecked(position, false);
                    friendList.getChildAt(position).setBackgroundColor(Color.WHITE);
                }
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray checked = friendList.getCheckedItemPositions();
                ArrayList<String> temp = new ArrayList<>();
                for(String s : friendListArray) {
                    if(checked.get(friendListArray.indexOf(s))) {
                        db.deleteFriend(ActivityManageFriends.this, helper.stringParser(s));
                    } else {
                        temp.add(s);
                    }
                }
                friendListArray.clear();
                friendListArray.addAll(temp);
                listAdapter.notifyDataSetChanged();
                friendList.setAdapter(listAdapter);
            }
        });
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_logged_in);
        toolbar.setTitle(getString(R.string.manage_friends));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(ActivityManageFriends.this, ActivityPreferences.class);
                startActivity(intent);
                return true;
            }
        });
        toolbar.getMenu().getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                setResult(ResultCodes.RESULT_LOGOUT);
                finish();
                return true;
            }
        });
        toolbar.getMenu().getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                setResult(ResultCodes.RESULT_QUIT);
                helper.quit(ActivityManageFriends.this);
                return true;
            }
        });
    }

}
