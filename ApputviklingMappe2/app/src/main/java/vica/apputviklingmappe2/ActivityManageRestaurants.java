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


public class ActivityManageRestaurants extends Activity {

    private Toolbar toolbar;
    private Helper helper;
    private Session session;
    private DB db;

    private Button buttonAdd;
    private Button buttonDelete;
    private Button buttonEdit;

    private ListView restaurantList;
    private ArrayAdapter<String> listAdapter;
    private ArrayList<String> restaurantListArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        session = new Session(ActivityManageRestaurants.this);
        if(session.getUserLevel() < 2) {
            finish();
            Intent intent = new Intent(ActivityManageRestaurants.this, ActivityLogin.class);
            startActivity(intent);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_restaurants);

        helper = new Helper();
        db = new DB();
        setupToolbar();
        setupFields();
        populateRestaurantList();
    }

    private void populateRestaurantList() {
        Cursor cur = getContentResolver().query(DB.CONTENT_RESTAURANT_URI, null, null, null, null);
        listAdapter = new ArrayAdapter<>(this, R.layout.list_add_restaurant, R.id.restaurant_textview, restaurantListArray);
        restaurantList.setAdapter(listAdapter);
        if(cur != null && cur.moveToFirst()) {
            do {
                restaurantListArray.add((cur.getString(0)) + " " +
                        (cur.getString(1)) + ", " +
                        (cur.getString(2)) + ", " +
                        (cur.getString(3)) + ", " +
                        (cur.getString(4)));
                listAdapter.notifyDataSetChanged();
            }
            while(cur.moveToNext());
            cur.close();
        }
    }

    private void setupFields() {
        restaurantListArray = new ArrayList<>();
        buttonAdd = (Button) findViewById(R.id.restaurant_add_button);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ActivityManageRestaurants.this);
                View view = getLayoutInflater().inflate(R.layout.dialog_add_restaurant, null);
                final EditText restaurantName = (EditText) view.findViewById(R.id.restaurant_dialog_name);
                final EditText restaurantAddress = (EditText) view.findViewById(R.id.restaurant_dialog_address);
                final EditText restaurantPhone = (EditText) view.findViewById(R.id.restaurant_dialog_phone);
                final EditText restaurantType = (EditText) view.findViewById(R.id.restaurant_dialog_type);
                final TextView restaurantDialogNameFeedback = (TextView) view.findViewById(R.id.restaurant_dialog_name_feedback);
                final TextView restaurantDialogAddressFeedback = (TextView) view.findViewById(R.id.restaurant_dialog_address_feedback);
                final TextView restaurantDialogPhoneFeedback = (TextView) view.findViewById(R.id.restaurant_dialog_phone_feedback);
                final TextView restaurantDialogTypeFeedback = (TextView) view.findViewById(R.id.restaurant_dialog_type_feedback);
                Button restaurant_add_button = (Button) view.findViewById(R.id.restaurant_dialog_add_button);

                OnTextChangedListener nameOnTextChangedListener = new OnTextChangedListener(restaurantName, null, restaurantDialogNameFeedback, "^[A-Z][A-Za-z '-]+$", getString(R.string.error_res_name), null);
                restaurantName.addTextChangedListener(nameOnTextChangedListener);
                OnTextChangedListener addressOnTextChangedListener = new OnTextChangedListener(restaurantAddress, null, restaurantDialogAddressFeedback, "^[A-Z0-9][A-Za-z0-9 '-]+$", getString(R.string.error_res_address), null);
                restaurantAddress.addTextChangedListener(addressOnTextChangedListener);
                OnTextChangedListener phoneOnTextChangedListener = new OnTextChangedListener(restaurantPhone, null, restaurantDialogPhoneFeedback, "^[0-9]{8}$", getString(R.string.error_phone), null);
                restaurantPhone.addTextChangedListener(phoneOnTextChangedListener);
                OnTextChangedListener typeOnTextChangedListener = new OnTextChangedListener(restaurantType, null, restaurantDialogTypeFeedback, "^[A-Z][A-Za-z '-]+$", getString(R.string.error_res_type), null);
                restaurantType.addTextChangedListener(typeOnTextChangedListener);

                dialogBuilder.setView(view);
                final AlertDialog dialog = dialogBuilder.create();
                final String sql = getString(R.string.RESTAURANT_ID) + " DESC LIMIT 1";
                restaurant_add_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!restaurantName.getText().toString().isEmpty() && restaurantDialogNameFeedback.getText().length() == 0 &&
                                !restaurantAddress.getText().toString().isEmpty() && restaurantDialogAddressFeedback.getText().length() == 0 &&
                                !restaurantPhone.getText().toString().isEmpty() && restaurantDialogPhoneFeedback.getText().length() == 0 &&
                                !restaurantType.getText().toString().isEmpty() && restaurantDialogTypeFeedback.getText().length() == 0)
                        {
                            db.addRestaurant(ActivityManageRestaurants.this, restaurantName.getText().toString(), restaurantAddress.getText().toString(), restaurantPhone.getText().toString(), restaurantType.getText().toString());
                            restaurantListArray.add(Integer.parseInt(db.getInfo(DB.CONTENT_RESTAURANT_URI, new String[]{getString(R.string.RESTAURANT_ID)}, null, sql,ActivityManageRestaurants.this))+ " "
                                    + restaurantName.getText().toString() + " " + restaurantAddress.getText().toString() + " " + restaurantPhone.getText().toString() + " " + restaurantType.getText().toString());
                            listAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });

        buttonDelete = (Button) findViewById(R.id.restaurant_delete_button);
        buttonEdit = (Button) findViewById(R.id.restaurant_edit_button);
        restaurantList = (ListView)findViewById(R.id.restaurant_list);
        restaurantList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        restaurantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!restaurantList.isItemChecked(restaurantList.getPositionForView(view))) {
                    restaurantList.setItemChecked(restaurantList.getPositionForView(view), false);
                    helper.animateBackground(view, getColor(R.color.colorPrimaryFade), getColor(R.color.colorText));
                } else {
                    restaurantList.setItemChecked(restaurantList.getPositionForView(view), true);
                    helper.animateBackground(view, getColor(R.color.colorPrimaryDark), getColor(R.color.colorPrimaryFade));
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray checked = restaurantList.getCheckedItemPositions();
                ArrayList<String> temp = new ArrayList<>();
                for(String s : restaurantListArray) {
                    if(checked.get(restaurantListArray.indexOf(s))) {
                        db.deleteRestaurant(ActivityManageRestaurants.this, helper.stringParser(s));
                    } else {
                        temp.add(s);
                    }
                }
                restaurantListArray.clear();
                restaurantListArray.addAll(temp);
                listAdapter.notifyDataSetChanged();
                restaurantList.setAdapter(listAdapter);
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray checked = restaurantList.getCheckedItemPositions();
                int booleanCounter = 0;
                String id = "";
                String unparsed = "";
                for(int i = 0; i < restaurantListArray.size(); i++){
                    if(checked.get(i)){
                        booleanCounter++;
                    }
                }
                if(booleanCounter == 1){
                    for(String s : restaurantListArray) {
                        if(checked.get(restaurantListArray.indexOf(s))) {
                            id = helper.stringParser(s);
                            unparsed = s;
                        }
                    }
                    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ActivityManageRestaurants.this);
                    View view = getLayoutInflater().inflate(R.layout.dialog_edit_restaurant, null);
                    final EditText restaurantName = (EditText) view.findViewById(R.id.restaurant_dialog_edit_name);
                    final EditText restaurantAddress = (EditText) view.findViewById(R.id.restaurant_dialog_edit_address);
                    final EditText restaurantPhone = (EditText) view.findViewById(R.id.restaurant_dialog_edit_phone);
                    final EditText restaurantType = (EditText) view.findViewById(R.id.restaurant_dialog_edit_type);
                    final TextView restaurantDialogNameFeedback = (TextView) view.findViewById(R.id.restaurant_dialog_edit_name_feedback);
                    final TextView restaurantDialogAddressFeedback = (TextView) view.findViewById(R.id.restaurant_dialog_edit_address_feedback);
                    final TextView restaurantDialogPhoneFeedback = (TextView) view.findViewById(R.id.restaurant_dialog_edit_phone_feedback);
                    final TextView restaurantDialogTypeFeedback = (TextView) view.findViewById(R.id.restaurant_dialog_edit_type_feedback);
                    Button restaurant_dialog_edit_button = (Button) view.findViewById(R.id.restaurant_dialog_edit_button);

                    OnTextChangedListener firstNameOnTextChangedListener = new OnTextChangedListener(restaurantName, null, restaurantDialogNameFeedback, "^[A-Z][A-Za-z '-]+$", getString(R.string.error_res_name), null);
                    restaurantName.addTextChangedListener(firstNameOnTextChangedListener);
                    OnTextChangedListener lastNameOnTextChangedListener = new OnTextChangedListener(restaurantAddress, null, restaurantDialogAddressFeedback, "^[A-Z0-9][A-Za-z0-9 '-]+$", getString(R.string.error_res_address), null);
                    restaurantAddress.addTextChangedListener(lastNameOnTextChangedListener);
                    OnTextChangedListener phoneOnTextChangedListener = new OnTextChangedListener(restaurantPhone, null, restaurantDialogPhoneFeedback, "^[0-9]{8}$", getString(R.string.error_phone), null);
                    restaurantPhone.addTextChangedListener(phoneOnTextChangedListener);
                    OnTextChangedListener typeOnTextChangedListener = new OnTextChangedListener(restaurantType, null, restaurantDialogTypeFeedback, "^[A-Z][A-Za-z '-]+$", getString(R.string.error_res_type), null);
                    restaurantType.addTextChangedListener(typeOnTextChangedListener);

                    restaurantName.setText(db.getInfo(DB.CONTENT_RESTAURANT_URI, new String[]{getString(R.string.RESTAURANT_NAME)}, getString(R.string.RESTAURANT_ID)+"="+id,null,ActivityManageRestaurants.this));
                    restaurantAddress.setText(db.getInfo(DB.CONTENT_RESTAURANT_URI, new String[]{getString(R.string.RESTAURANT_ADDRESS)}, getString(R.string.RESTAURANT_ID)+"="+id,null,ActivityManageRestaurants.this));
                    restaurantPhone.setText(db.getInfo(DB.CONTENT_RESTAURANT_URI, new String[]{getString(R.string.RESTAURANT_PHONE)}, getString(R.string.RESTAURANT_ID)+"="+id,null,ActivityManageRestaurants.this));
                    restaurantType.setText(db.getInfo(DB.CONTENT_RESTAURANT_URI, new String[]{getString(R.string.RESTAURANT_TYPE)}, getString(R.string.RESTAURANT_ID)+"="+id,null,ActivityManageRestaurants.this));

                    dialogBuilder.setView(view);
                    final AlertDialog dialog = dialogBuilder.create();
                    final String finalId = id;
                    restaurant_dialog_edit_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!restaurantName.getText().toString().isEmpty() && restaurantDialogNameFeedback.getText().length() == 0 &&
                                    !restaurantAddress.getText().toString().isEmpty() && restaurantDialogAddressFeedback.getText().length() == 0 &&
                                    !restaurantPhone.getText().toString().isEmpty() && restaurantDialogPhoneFeedback.getText().length() == 0 &&
                                    !restaurantType.getText().toString().isEmpty() && restaurantDialogTypeFeedback.getText().length() == 0)
                            {
                                db.editRestaurant(ActivityManageRestaurants.this, finalId, restaurantName.getText().toString(), restaurantAddress.getText().toString(), restaurantPhone.getText().toString(), restaurantType.getText().toString());
                                restaurantListArray.clear();
                                listAdapter.notifyDataSetChanged();
                                populateRestaurantList();
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
        toolbar.setTitle(getString(R.string.manage_restaurants));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(ActivityManageRestaurants.this, ActivityPreferences.class);
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
                helper.quit(ActivityManageRestaurants.this);
                return true;
            }
        });
    }

}
