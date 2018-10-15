package vica.apputviklingmappe2;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;

public class ListCursorAdapter extends CursorAdapter {
    public ListCursorAdapter(Context context, Cursor cursor){
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        return LayoutInflater.from(context).inflate(R.layout.activity_add_friend, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor){
        ListView friendList = (ListView)view.findViewById(R.id.friend_listview);
        String body = cursor.getString(cursor.getColumnIndexOrThrow("Firstname"));
    }
}
