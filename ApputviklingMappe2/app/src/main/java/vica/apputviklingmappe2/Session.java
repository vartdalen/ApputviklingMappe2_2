package vica.apputviklingmappe2;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;
import java.util.Map;

public class Session {
    private SharedPreferences prefs;
    private Context context;

    public Session(Context context) {
        this.context = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void destroy() {
        Map<String,?> prefMap = prefs.getAll();
        for(Map.Entry<String,?> prefToReset : prefMap.entrySet()) {
            if (!prefToReset.getKey().equals(context.getString(R.string.personal_reminder))
            && !prefToReset.getKey().equals(context.getString(R.string.notify_friends))
            && !prefToReset.getKey().equals(context.getString(R.string.timing))
            && !prefToReset.getKey().equals(context.getString(R.string.notify_friends_message))) {
                prefs.edit().remove(prefToReset.getKey()).apply();
            }
        }
    }

    public void setUserLevel(int userLevel) {
        prefs.edit().putInt(context.getString(R.string.user_level), userLevel).apply();
    }
    public int getUserLevel() {
        return prefs.getInt(context.getString(R.string.user_level),0);
    }

    public void setFirstName(String firstName) {
        prefs.edit().putString(context.getString(R.string.first_name), firstName).apply();
    }
    public String getFirstName() {
        return prefs.getString(context.getString(R.string.first_name),"");
    }

    public void setLastName(String lastName) {
        prefs.edit().putString(context.getString(R.string.last_name), lastName).apply();
    }
    public String getLastName() {
        return prefs.getString(context.getString(R.string.last_name),"");
    }

    public void setEmail(String email) {
        prefs.edit().putString(context.getString(R.string.email), email).apply();
    }
    public String getEmail() {
        return prefs.getString(context.getString(R.string.email),"");
    }

    public void setPassword(String password) {
        prefs.edit().putString(context.getString(R.string.password), password).apply();
    }
    public String getPassword() {
        return prefs.getString(context.getString(R.string.password),"");
    }

    public void setPhone(String phone) {
        prefs.edit().putString(context.getString(R.string.phone), phone).apply();
    }
    public String getPhone() {
        return prefs.getString(context.getString(R.string.phone),"");
    }

    public Boolean getPrefPersonalReminder() {
        return prefs.getBoolean(context.getString(R.string.personal_reminder), false);
    }
    public Boolean getPrefNotifyFriends() {
        return prefs.getBoolean(context.getString(R.string.notify_friends), false);
    }
    public String getPrefNotifyFriendsMessage() {
        return prefs.getString(context.getString(R.string.notify_friends_message), context.getString(R.string.notify_friends_message_default));
    }
    public String getPrefTiming() {
        return prefs.getString(context.getString(R.string.timing), Calendar.getInstance().getTime().getHours() + ":" + Calendar.getInstance().getTime().getMinutes());
    }
}
