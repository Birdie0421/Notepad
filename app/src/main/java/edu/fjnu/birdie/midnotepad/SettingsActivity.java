package edu.fjnu.birdie.midnotepad;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefsFragement()).commit();
    }
    public static class PrefsFragement extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference_settings);



        }
    }
    

    public void setBackground(){
        int gray = Color.GRAY;
        int yellow = Color.YELLOW;
        int white = Color.WHITE;
        int blue =Color.BLUE;
        int color[] = {gray,yellow,white,blue};
        //SharedPreferences shp = PreferenceManager.getDefaultSharedPreferences(R.xml.preference_settings);
        SharedPreferences shp = PreferenceManager.getDefaultSharedPreferences(this);
        String background = shp.getString("Text_Color_ListKey","0");
        LinearLayout editLayout = (LinearLayout)findViewById(R.id.edit_activity);
        editLayout.setBackgroundColor(color[Integer.parseInt(background)]);
    }

}
