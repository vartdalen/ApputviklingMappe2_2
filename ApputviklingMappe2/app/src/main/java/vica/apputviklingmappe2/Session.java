package vica.apputviklingmappe2;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {
    private SharedPreferences prefs;
    private Context context;

    public Session(Context context) {
        this.context = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void destroy() {
        prefs.edit().clear().apply();
    }

    public void setUserLevel(int userLevel) {
        prefs.edit().putInt(context.getString(R.string.user_level), userLevel).apply();
    }

    public int getUserLevel() {
        return prefs.getInt(context.getString(R.string.user_level),0);
    }
}
