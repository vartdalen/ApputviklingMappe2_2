package vica.apputviklingmappe2;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;
import android.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class ActivityPreferences extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    Helper helper;
    SharedPreferences prefs;
    int MY_PERMISSIONS_REQUEST_SEND_SMS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        addPreferencesFromResource(R.xml.activity_preferences);
        setupToolbar();
        prefs = PreferenceManager.getDefaultSharedPreferences(ActivityPreferences.this);
        helper = new Helper();
        MY_PERMISSIONS_REQUEST_SEND_SMS = ActivityCompat.checkSelfPermission(ActivityPreferences.this, Manifest.permission.SEND_SMS);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(!prefs.getBoolean(getString(R.string.personal_reminder), false) && !prefs.getBoolean(getString(R.string.notify_friends), false)) {
            helper.stoppPeriodisk(ActivityPreferences.this);
            Toast.makeText(ActivityPreferences.this, getString(R.string.periodic_disabled), Toast.LENGTH_LONG).show();
        }
        if(prefs.getBoolean(getString(R.string.notify_friends), false) && MY_PERMISSIONS_REQUEST_SEND_SMS != PackageManager.PERMISSION_GRANTED) {
            helper.requestSMSPermission(ActivityPreferences.this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    private void setupToolbar() {
        LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar toolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.toolbar, root, false);
        root.addView(toolbar, 0);
        toolbar.setTitle(getString(R.string.preferences));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(ResultCodes.RESULT_FINISH);
                finish();
            }
        });
    }
}
