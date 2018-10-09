package vica.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class RestaurantProvider extends ContentProvider {

    // Database and Provider related stuff
    public final static String PROVIDER = "vica.contentprovider";
    private final static String DB_NAME = "vica.db";
    private final static int DB_VERSION = 1;
    private static final int BOK = 1;
    private static final int MBOK = 2;

    // Label table name
    private final static String TABLE_USER = "User";
    private final static String TABLE_FRIENDS = "Friends";
    private final static String TABLE_RESTAURANT = "Restaurant";
    private final static String TABLE_ORDER = "Order";
    private final static String TABLE_ORDERLINE = "Orderline";
    private final static String TABLE_MAIL = "Mail";

    // Label table user columns
    public final static String USER_ID = "idUser";
    public final static String USER_FIRSTNAME = "Firstname";
    public final static String USER_EMAIL = "Email";
    public final static String USER_LASTNAME = "Lastname";
    public final static String USER_ADDRESS = "Address";
    public final static String USER_PHONE = "Phonenumber";
    public final static String USER_PASSWORD = "Password";
    public final static String ZIP_CODE = "Zip_Code";
    public final static String AREA = "Zip_Area";

    RestaurantProvider.DatabaseHelper DBhelper;
    SQLiteDatabase db;

    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER + "/ User");
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER, "User", MBOK);
        uriMatcher.addURI(PROVIDER, "User/#", BOK);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = "CREATE TABLE " + TABLE_USER + "("
                    + USER_ID + " INTEGER PRIMARY KEY,"
                    + USER_FIRSTNAME + " TEXT,"
                    + USER_LASTNAME + " TEXT,"
                    + USER_EMAIL + " TEXT,"
                    + USER_ADDRESS + " TEXT,"
                    + USER_PHONE + " TEXT,"
                    + USER_PASSWORD + " TEXT,"
                    + ZIP_CODE + " TEXT,"
                    + AREA + " TEXT );";

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

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        switch(uriMatcher.match(uri)) {
            case MBOK:
                return"vnd.android.cursor.dir/vnd.example.bok";
            case BOK:
                return"vnd.android.cursor.item/vnd.example.bok";
            default:
                throw new IllegalArgumentException("Ugyldig URI" + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = DBhelper.getWritableDatabase();
        db.insert(TABLE_USER, null, values);
        Cursor c = db.query(TABLE_USER, null, null, null, null, null, null);
        c.moveToLast();
        long minid= c.getLong(0);getContext().getContentResolver().notifyChange(uri, null);
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
