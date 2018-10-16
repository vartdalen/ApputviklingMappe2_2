package vica.apputviklingmappe2;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class ActivityPreferences extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        addPreferencesFromResource(R.xml.activity_preferences);
        setupToolbar();
    }

    private void setupToolbar() {
        LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar toolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.toolbar, root, false);
        root.addView(toolbar, 0);
        toolbar.setTitle(getString(R.string.preferences));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
