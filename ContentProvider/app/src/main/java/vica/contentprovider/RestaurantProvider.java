package vica.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class RestaurantProvider extends ContentProvider {

    // Database and Provider related stuff
    public final static String PROVIDER = "vica.contentprovider";
    public final static String DB_NAME = "vica.db";
    private final static int DB_VERSION = 1;
    private static final int USER = 1;
    private static final int USER_WITH_ID = 2;
    private static final int FRIEND = 3;
    private static final int FRIEND_WITH_ID = 4;
    private static final int RESTAURANT = 5;

    // Table names
    private final static String TABLE_USER = "User";
    private final static String TABLE_FRIEND = "Friend";
    private final static String TABLE_RESTAURANT = "Restaurant";
    private final static String TABLE_ORDER = "Order";
    private final static String TABLE_ORDERLINE = "Orderline";

    // Table User columns
    public final static String USER_ID = "Email";
    public final static String USER_LEVEL = "Userlevel";
    public final static String USER_FIRSTNAME = "Firstname";
    public final static String USER_LASTNAME = "Lastname";
    public final static String USER_PHONE = "Phonenumber";
    public final static String USER_PASSWORD = "Password";

    // Table Friends columns
    public final static String FRIEND_ID = "_id";
    public final static String FRIEND_FIRSTNAME = "Firstname";
    public final static String FRIEND_LASTNAME = "Lastname";
    public final static String FRIEND_PHONE = "Phonenumber";
    public final static String FRIEND_FK = "UserEmail";

    // Table Restaurant columns
    public final static String RESTAURANT_ID = "_id";
    public final static String RESTAURANT_NAME = "Name";
    public final static String RESTAURANT_ADDRESS = "Address";
    public final static String RESTAURANT_PHONE = "Phonenumber";
    public final static String RESTAURANT_TYPE = "Type";

    // Table User create-statement
    private static final String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + USER_ID + " TEXT PRIMARY KEY,"
            + USER_LEVEL + " INTEGER NOT NULL,"
            + USER_FIRSTNAME + " TEXT,"
            + USER_LASTNAME + " TEXT,"
            + USER_PHONE + " TEXT,"
            + USER_PASSWORD + " TEXT);";

    // Table Friends create-statement
    private static final String CREATE_FRIEND_TABLE = "CREATE TABLE " + TABLE_FRIEND + "("
            + FRIEND_ID + " Integer PRIMARY KEY AUTOINCREMENT,"
            + FRIEND_FIRSTNAME + " TEXT,"
            + FRIEND_LASTNAME + " TEXT,"
            + FRIEND_PHONE + " TEXT,"
            + FRIEND_FK + " TEXT,"
            + "FOREIGN KEY ("+FRIEND_FK+") REFERENCES "+TABLE_USER+"("+USER_ID+"));";

    // Table Friends create-statement
    private static final String CREATE_RESTAURANT_TABLE = "CREATE TABLE " + TABLE_RESTAURANT + "("
            + RESTAURANT_ID + " Integer PRIMARY KEY AUTOINCREMENT,"
            + RESTAURANT_NAME + " TEXT,"
            + RESTAURANT_ADDRESS + " TEXT,"
            + RESTAURANT_PHONE + " TEXT,"
            + RESTAURANT_TYPE + " TEXT);";

    RestaurantProvider.DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public static final Uri CONTENT_USER_URI = Uri.parse("content://" + PROVIDER + "/User");
    public static final Uri CONTENT_FRIEND_URI = Uri.parse("content://" + PROVIDER + "/Friend");
    public static final Uri CONTENT_RESTAURANT_URI = Uri.parse("content://" + PROVIDER + "/Restaurant");
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER, "User", USER);
        uriMatcher.addURI(PROVIDER, "User/#", USER_WITH_ID);
        uriMatcher.addURI(PROVIDER, "Friend", FRIEND);
        uriMatcher.addURI(PROVIDER, "Friend/#", FRIEND_WITH_ID);
        uriMatcher.addURI(PROVIDER, "Restaurant", RESTAURANT);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_USER_TABLE);
            db.execSQL(CREATE_FRIEND_TABLE);
            db.execSQL(CREATE_RESTAURANT_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists " + TABLE_USER);
            Log.d("DatabaseHelper", "updated");
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        DBHelper = new RestaurantProvider.DatabaseHelper(getContext());
        db = DBHelper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        int uriType = uriMatcher.match(uri);

        switch (uriType) {
            case USER_WITH_ID:
                queryBuilder.setTables(TABLE_USER);
                queryBuilder.appendWhere(USER_ID + "=" + uri.getLastPathSegment());
                break;
            case USER:
                queryBuilder.setTables(TABLE_USER);
                break;
            case FRIEND_WITH_ID:
                queryBuilder.setTables(TABLE_FRIEND);
                queryBuilder.appendWhere(FRIEND_ID + "=" + uri.getLastPathSegment());
                break;
            case FRIEND:
                queryBuilder.setTables(TABLE_FRIEND);
                break;
            case RESTAURANT:
                queryBuilder.setTables(TABLE_RESTAURANT);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        Cursor cursor = queryBuilder.query(DBHelper.getReadableDatabase(), projection, selection, null, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = DBHelper.getWritableDatabase();
        int uriType = uriMatcher.match(uri);
        long minid = 0;
        Cursor c;

        switch (uriType) {
            case USER_WITH_ID:
                db.insert(TABLE_USER, null, values);
                c = db.query(TABLE_USER, null, null, null, null, null, null);
                break;
            case USER:
                db.insert(TABLE_USER, null, values);
                c = db.query(TABLE_USER, null, null, null, null, null, null);
                break;
            case FRIEND_WITH_ID:
                db.insert(TABLE_FRIEND, null, values);
                c = db.query(TABLE_FRIEND, null, null, null, null, null, null);
                break;
            case FRIEND:
                db.insert(TABLE_FRIEND, null, values);
                c = db.query(TABLE_FRIEND, null, null, null, null, null, null);
                break;
            case RESTAURANT:
                db.insert(TABLE_RESTAURANT, null, values);
                c = db.query(TABLE_RESTAURANT, null,null,null,null,null,null);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
        c.moveToLast();
        minid = c.getLong(0);
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, minid);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = DBHelper.getWritableDatabase();
        int uriType = uriMatcher.match(uri);
        int rowsDeleted = 0;

        switch (uriType) {
            case USER:
                rowsDeleted = db.delete(TABLE_USER, selection, selectionArgs);
                break;
            case USER_WITH_ID:
                rowsDeleted = db.delete(TABLE_USER, selection, selectionArgs);
                break;
            case FRIEND:
                rowsDeleted = db.delete(TABLE_FRIEND, selection, selectionArgs);
                break;
            case FRIEND_WITH_ID:
               rowsDeleted = db.delete(TABLE_FRIEND, selection, selectionArgs);
                break;
            case RESTAURANT:
                rowsDeleted = db.delete(TABLE_RESTAURANT, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = DBHelper.getWritableDatabase();
        int uriType = uriMatcher.match(uri);
        int rowsUpdated = 0;

        switch (uriType) {
            case USER:
                rowsUpdated = db.update(TABLE_USER, values, selection, selectionArgs);
                break;
            case USER_WITH_ID:
                rowsUpdated = db.update(TABLE_USER, values, USER_ID + " = " + uri.getPathSegments().get(1), selectionArgs);
                break;
            case FRIEND:
                rowsUpdated = db.update(TABLE_FRIEND, values, selection, selectionArgs);
                break;
            case FRIEND_WITH_ID:
                rowsUpdated = db.update(TABLE_FRIEND, values, FRIEND_ID + " = " + uri.getPathSegments().get(1), selectionArgs);
                break;
            case RESTAURANT:
                rowsUpdated = db.update(TABLE_RESTAURANT, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    @Override
    public String getType(Uri uri) {
        return "";
    }
}
