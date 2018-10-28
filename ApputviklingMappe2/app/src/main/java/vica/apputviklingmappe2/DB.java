package vica.apputviklingmappe2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class DB extends Activity{

    public static String PROVIDER = "vica.contentprovider" ;
    public static final Uri CONTENT_USER_URI = Uri.parse("content://"+ PROVIDER + "/User");
    public static final Uri CONTENT_FRIEND_URI = Uri.parse("content://" + PROVIDER + "/Friend");
    public static final Uri CONTENT_RESTAURANT_URI = Uri.parse("content://" + PROVIDER + "/Restaurant");
    public static final Uri CONTENT_ORDER_URI = Uri.parse("content://" + PROVIDER + "/Ordertable");
    public static final Uri CONTENT_ORDERLINE_URI = Uri.parse("content://" + PROVIDER + "/Orderline");

    public String getInfo(Uri uri, String[] projection, String selection, String sortOrder, Context context ) {
        StringBuilder stringBuilderQueryResult = new StringBuilder("");
        try {
            Cursor cursor = context.getContentResolver().query(uri, projection, selection, null, sortOrder);

            if (cursor.moveToFirst()) {
                cursor.moveToFirst();
                stringBuilderQueryResult.append(cursor.getString(0));
                cursor.close();
            }
            return stringBuilderQueryResult.toString();
        }catch (Exception e){}
        return stringBuilderQueryResult.toString();
    }

    public Integer getId(Uri uri, String[] projection, String selection, String sortOrder, Context context ) {
        int id = 1;
        try {
            Cursor cursor = context.getContentResolver().query(uri, projection, selection, null, sortOrder);
            if (cursor.moveToFirst()) {
                cursor.moveToFirst();
                id = cursor.getInt(0);
                cursor.close();
            }
            return id;
        }catch (Exception e){}
        return id;
    }

    public String getEmail(String email, Context context) {
        StringBuilder stringBuilderQueryResult = new StringBuilder("");
        try {
            String[] projection = {context.getString(R.string.USER_ID)}; // table columns
            String selection = context.getString(R.string.USER_ID) + "=" + "'" + email + "'";

            Cursor cursor = context.getContentResolver().query(CONTENT_USER_URI, projection, selection, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                cursor.moveToFirst();
                stringBuilderQueryResult.append(cursor.getString(0));
                cursor.close();
            }
            return stringBuilderQueryResult.toString();
        }catch (Exception e){}
        return stringBuilderQueryResult.toString();
    }

    public boolean verifyUser(String email, String password, Context context) {
        try {
            if (getEmail(email, context).length() > 5) {
                if (getInfo(CONTENT_USER_URI, new String[]{context.getString(R.string.USER_PASSWORD)}, context.getString(R.string.USER_ID) + "=" + "'" + email + "'", null, context).equals(password)) {
                    return true;
                }
            }
            return false;
        }catch (Exception e){}
        return false;
    }

    // Friend db methods
    public void createFriend(Context context, String firstname, String lastname, String phone, String userEmail){
        try {
            ContentValues values = new ContentValues();
            values.put(context.getString(R.string.FRIEND_FIRSTNAME), firstname);
            values.put(context.getString(R.string.FRIEND_LASTNAME), lastname);
            values.put(context.getString(R.string.FRIEND_PHONE), phone);
            values.put(context.getString(R.string.FRIEND_FK), userEmail);
            context.getContentResolver().insert(CONTENT_FRIEND_URI, values);
        }catch (Exception e){}
    }

    public void deleteFriend(Context context, String id){
        try {
            String selection = context.getString(R.string.FRIEND_ID) + "=" + "'" + id + "'";
            context.getContentResolver().delete(CONTENT_FRIEND_URI, selection, null);
        }catch (Exception e){}
    }

    public void editFriend(Context context, String id, String firstname, String lastname, String phone){
        try {
            ContentValues values = new ContentValues();
            values.put(context.getString(R.string.FRIEND_FIRSTNAME), firstname);
            values.put(context.getString(R.string.FRIEND_LASTNAME), lastname);
            values.put(context.getString(R.string.FRIEND_PHONE), phone);
            context.getContentResolver().update(CONTENT_FRIEND_URI, values, context.getString(R.string.FRIEND_ID) + "=" + id, null);
        }catch (Exception e){}
    }

    // Restaurant db methods
    public void createRestaurant(Context context, String name, String address, String phone, String type){
        try {
            ContentValues values = new ContentValues();
            values.put(context.getString(R.string.RESTAURANT_NAME), name);
            values.put(context.getString(R.string.RESTAURANT_ADDRESS), address);
            values.put(context.getString(R.string.RESTAURANT_PHONE), phone);
            values.put(context.getString(R.string.RESTAURANT_TYPE), type);
            context.getContentResolver().insert(CONTENT_RESTAURANT_URI, values);
        }catch (Exception e){}
    }
    public void deleteRestaurant(Context context, String id){
        try {
            String selection = context.getString(R.string.RESTAURANT_ID) + "=" + "'" + id + "'";
            context.getContentResolver().delete(CONTENT_RESTAURANT_URI, selection, null);
        }catch (Exception e){}
    }

    public void editRestaurant(Context context, String id, String name, String address, String phone, String type){
        try{
            ContentValues values = new ContentValues();
            values.put(context.getString(R.string.RESTAURANT_NAME), name);
            values.put(context.getString(R.string.RESTAURANT_ADDRESS), address);
            values.put(context.getString(R.string.RESTAURANT_PHONE), phone);
            values.put(context.getString(R.string.RESTAURANT_TYPE), type);
            context.getContentResolver().update(CONTENT_RESTAURANT_URI, values, context.getString(R.string.RESTAURANT_ID)+"="+id, null);
        }catch (Exception e){}
    }

    // Order db methods
    public boolean createOrder(Context context, Integer resId, String userEmail, String date, String time){
        try {
            ContentValues values = new ContentValues();
            values.put(context.getString(R.string.ORDER_DATE), date);
            values.put(context.getString(R.string.ORDER_TIME), time);
            values.put(context.getString(R.string.ORDER_UserFK), userEmail);
            values.put(context.getString(R.string.ORDER_RestaurantFK), resId);
            context.getContentResolver().insert(CONTENT_ORDER_URI, values);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public void createOrderline(Context context, Integer friendId, Integer orderId){
        try {
            ContentValues values = new ContentValues();
            values.put(context.getString(R.string.ORDERLINE_FriendFK), friendId);
            values.put(context.getString(R.string.ORDERLINE_OrderFK), orderId);
            context.getContentResolver().insert(CONTENT_ORDERLINE_URI, values);
        }catch(Exception e) {}
    }

    public boolean compareOrderDate(Context context, String systemDate)
    {
        try{
            String[] projection = {context.getString(R.string.ORDER_DATE)};
            String selection = context.getString(R.string.ORDER_DATE) + " LIKE " +"'"+systemDate+"%"+"'";
            Cursor cur = context.getContentResolver().query(CONTENT_ORDER_URI, projection, selection, null, null);
            if(cur != null && cur.moveToFirst()){
                cur.moveToFirst();
                System.out.println(cur.getString(0));
                cur.close();
                return true;
            }
            return false;
        } catch (Exception e){
            return false;
        }
    }
}
