package vica.apputviklingmappe2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.ArrayList;


public class ActivityManageFriends extends Activity {

    private Toolbar toolbar;
    private Helper helper;
    private Session session;
    private DB db;

    private Button buttonAdd;
    private Button buttonDelete;
    private Button buttonEdit;

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
        Cursor cur = getContentResolver().query(DB.CONTENT_FRIEND_URI, null, selection, null, null);
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
            final EditText friendFirstName = (EditText) view.findViewById(R.id.friend_dialog_first_name);
            final EditText friendLastName = (EditText) view.findViewById(R.id.friend_dialog_last_name);
            final EditText friendPhone = (EditText) view.findViewById(R.id.friend_dialog_phone);
            final TextView friendDialogFirstNameFeedback = (TextView) view.findViewById(R.id.friend_dialog_firstname_feedback);
            final TextView friendDialogLastNameFeedback = (TextView) view.findViewById(R.id.friend_dialog_lastname_feedback);
            final TextView friendDialogPhoneFeedback = (TextView) view.findViewById(R.id.friend_dialog_phone_feedback);
            Button friend_add_button = (Button) view.findViewById(R.id.friend_dialog_add_button);

            OnTextChangedListener firstNameOnTextChangedListener = new OnTextChangedListener(friendFirstName, null, friendDialogFirstNameFeedback, "^[A-Z][A-Za-z '-]+$", getString(R.string.error_first_name), null);
            friendFirstName.addTextChangedListener(firstNameOnTextChangedListener);
            OnTextChangedListener lastNameOnTextChangedListener = new OnTextChangedListener(friendLastName, null, friendDialogLastNameFeedback, "^[A-Z][A-Za-z '-]+$", getString(R.string.error_last_name), null);
            friendLastName.addTextChangedListener(lastNameOnTextChangedListener);
            OnTextChangedListener phoneOnTextChangedListener = new OnTextChangedListener(friendPhone, null, friendDialogPhoneFeedback, "^[0-9]{8}$", getString(R.string.error_phone), null);
            friendPhone.addTextChangedListener(phoneOnTextChangedListener);

            dialogBuilder.setView(view);
            final AlertDialog dialog = dialogBuilder.create();
            final String sql = getString(R.string.FRIEND_ID) + " DESC LIMIT 1";
            friend_add_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!friendFirstName.getText().toString().isEmpty() && friendDialogFirstNameFeedback.getText().length() == 0 &&
                            !friendLastName.getText().toString().isEmpty() && friendDialogLastNameFeedback.getText().length() == 0 &&
                            !friendPhone.getText().toString().isEmpty() && friendDialogPhoneFeedback.getText().length() == 0)
                    {
                        db.createFriend(ActivityManageFriends.this, friendFirstName.getText().toString(), friendLastName.getText().toString(), friendPhone.getText().toString(), session.getEmail().toString());
                        friendListArray.add(Integer.parseInt(db.getInfo(DB.CONTENT_FRIEND_URI, new String[]{getString(R.string.FRIEND_ID)}, null, sql,ActivityManageFriends.this))+ " "
                                + friendFirstName.getText().toString() + " " + friendLastName.getText().toString() + " " + friendPhone.getText().toString());
                        listAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }
            });
            dialog.show();
            }
        });

        friendList = (ListView)findViewById(R.id.friend_list);
        friendList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int listPos = friendList.getPositionForView(view);
            if(!friendList.isItemChecked(listPos)) {
                friendList.setItemChecked(listPos, false);
            } else if (friendList.isItemChecked(listPos)){
                friendList.setItemChecked(listPos, true);
            }
            }
        });

        buttonDelete = (Button) findViewById(R.id.friend_delete_button);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            SparseBooleanArray checked = friendList.getCheckedItemPositions();
            ArrayList<String> temp = new ArrayList<>();
            for(String s : friendListArray) {
                if(checked.get(friendListArray.indexOf(s))) {
                    db.deleteFriend(ActivityManageFriends.this, helper.parseNumbersFromString(s));
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

        buttonEdit = (Button) findViewById(R.id.friend_edit_button);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            SparseBooleanArray checked = friendList.getCheckedItemPositions();
            int checkedCounter = 0;
            String id = "";
            for(int i = 0; i < friendListArray.size(); i++){
                if(checked.get(i)){
                    checkedCounter++;
                }
            }
            if(checkedCounter == 1){
                for(String s : friendListArray) {
                    if(checked.get(friendListArray.indexOf(s))) {
                        id = helper.parseNumbersFromString(s);
                    }
                }
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ActivityManageFriends.this);
                View view = getLayoutInflater().inflate(R.layout.dialog_edit_friend, null);
                final EditText friendFirstName = (EditText) view.findViewById(R.id.friend_dialog_edit_first_name);
                final EditText friendLastName = (EditText) view.findViewById(R.id.friend_dialog_edit_last_name);
                final EditText friendPhone = (EditText) view.findViewById(R.id.friend_dialog_edit_phone);
                final TextView friendDialogFirstNameFeedback = (TextView) view.findViewById(R.id.friend_dialog_edit_firstname_feedback);
                final TextView friendDialogLastNameFeedback = (TextView) view.findViewById(R.id.friend_dialog_edit_lastname_feedback);
                final TextView friendDialogPhoneFeedback = (TextView) view.findViewById(R.id.friend_dialog_edit_phone_feedback);
                Button friend_dialog_edit_button = (Button) view.findViewById(R.id.friend_dialog_edit_button);

                OnTextChangedListener firstNameOnTextChangedListener = new OnTextChangedListener(friendFirstName, null, friendDialogFirstNameFeedback, "^[A-Z][A-Za-z '-]+$", getString(R.string.error_first_name), null);
                friendFirstName.addTextChangedListener(firstNameOnTextChangedListener);
                OnTextChangedListener lastNameOnTextChangedListener = new OnTextChangedListener(friendLastName, null, friendDialogLastNameFeedback, "^[A-Z][A-Za-z '-]+$", getString(R.string.error_last_name), null);
                friendLastName.addTextChangedListener(lastNameOnTextChangedListener);
                OnTextChangedListener phoneOnTextChangedListener = new OnTextChangedListener(friendPhone, null, friendDialogPhoneFeedback, "^[0-9]{8}$", getString(R.string.error_phone), null);
                friendPhone.addTextChangedListener(phoneOnTextChangedListener);

                friendFirstName.setText(db.getInfo(DB.CONTENT_FRIEND_URI, new String[]{getString(R.string.FRIEND_FIRSTNAME)}, getString(R.string.FRIEND_ID)+"="+id,null,ActivityManageFriends.this));
                friendLastName.setText(db.getInfo(DB.CONTENT_FRIEND_URI, new String[]{getString(R.string.FRIEND_LASTNAME)}, getString(R.string.FRIEND_ID)+"="+id,null,ActivityManageFriends.this));
                friendPhone.setText(db.getInfo(DB.CONTENT_FRIEND_URI, new String[]{getString(R.string.FRIEND_PHONE)}, getString(R.string.FRIEND_ID)+"="+id,null,ActivityManageFriends.this));

                dialogBuilder.setView(view);
                final AlertDialog dialog = dialogBuilder.create();
                final String finalId = id;
                friend_dialog_edit_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    if(!friendFirstName.getText().toString().isEmpty() && !friendLastName.getText().toString().isEmpty() && !friendPhone.getText().toString().isEmpty())
                    {
                        db.editFriend(ActivityManageFriends.this, finalId, friendFirstName.getText().toString(), friendLastName.getText().toString(), friendPhone.getText().toString());
                        friendListArray.clear();
                        listAdapter.notifyDataSetChanged();
                        populateFriendList();
                        dialog.dismiss();
                    }
                    }
                });
                dialog.show();
            }
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
                setResult(ResultCodes.RESULT_FINISH);
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
