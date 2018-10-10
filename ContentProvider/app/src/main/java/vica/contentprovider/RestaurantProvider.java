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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class RestaurantProvider extends ContentProvider {

    // Database and Provider related stuff
    public final static String PROVIDER = "vica.contentprovider";
    private final static String DB_NAME = "vica.db";
    private final static int DB_VERSION = 1;
    private static final int USER = 1;
    private static final int USER_WITH_ID = 2;

    // Label table name
    private final static String TABLE_USER = "User";
    private final static String TABLE_FRIENDS = "Friends";
    private final static String TABLE_RESTAURANT = "Restaurant";
    private final static String TABLE_ORDER = "Order";
    private final static String TABLE_ORDERLINE = "Orderline";

    // Label table user columns
    public final static String USER_ID = "Email";
    public final static String USER_FIRSTNAME = "Firstname";
    public final static String USER_LASTNAME = "Lastname";
    public final static String USER_PHONE = "Phonenumber";
    public final static String USER_PASSWORD = "Password";

    RestaurantProvider.DatabaseHelper DBhelper;
    SQLiteDatabase db;

    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER + "/ User");
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER, "User", USER);
        uriMatcher.addURI(PROVIDER, "User/#", USER_WITH_ID);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = "CREATE TABLE " + TABLE_USER + "("
                    + USER_ID + " TEXT PRIMARY KEY,"
                    + USER_FIRSTNAME + " TEXT,"
                    + USER_LASTNAME + " TEXT,"
                    + USER_PHONE + " TEXT,"
                    + USER_PASSWORD + " TEXT);";

            Log.d("DatabaseHelper", " oncreated sql" + sql);
            db.execSQL(sql);
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
        DBhelper = new RestaurantProvider.DatabaseHelper(getContext());
        db = DBhelper.getWritableDatabase();
        return true;
    }

//    @Override
//    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
//        Cursor cur = null;
//        if(uriMatcher.match(uri) == USER) {
//            cur = db.query(TABLE_USER, projection, USER_ID + "=" + uri.getPathSegments().get(1), selectionArgs, null, null, sortOrder);
//            return cur;
//        } else{
//            cur = db.query(TABLE_USER, null, null, null, null, null, null);
//            return cur;
//        }
//    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(TABLE_USER);

        int uriType = uriMatcher.match(uri);

        switch (uriType) {
            case USER_WITH_ID:
                queryBuilder.appendWhere(USER_ID + "="
                        + uri.getLastPathSegment());
                break;
            case USER:
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        Cursor cursor = queryBuilder.query(DBhelper.getReadableDatabase(),
                projection, selection, null, null, null,
                sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),
                uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch(uriMatcher.match(uri)) {
            case USER_WITH_ID:
                return"vnd.android.cursor.dir/vnd.example.User";
            case USER:
                return"vnd.android.cursor.item/vnd.example.User";
            default:
                throw new IllegalArgumentException("Unsupported URI" + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = DBhelper.getWritableDatabase();
        db.insert(TABLE_USER, null, values);
        Cursor c = db.query(TABLE_USER, null, null, null, null, null, null);
        c.moveToLast();
        long minid = c.getLong(0);
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, minid);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
