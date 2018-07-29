package com.eusecom.demoad.datamodel.settrepository;

import android.content.SharedPreferences;
import android.graphics.Color;

public class SettingsController implements ISettingsController {

    SharedPreferences mPrefs;

    public SettingsController(SharedPreferences sharedPreferences) {
        mPrefs = sharedPreferences;
    }

    public SharedPreferences.Editor getEditor() {

        SharedPreferences.Editor editor = mPrefs.edit();
        return editor;

    }

    public void setGpsInterval(long interval) {

        SharedPreferences.Editor editor = getEditor();
        editor.putLong("gpsinterval", interval);
        editor.apply();
        editor.commit();

    }

    public long getGpsInterval() {

        return mPrefs.getLong("gpsinterval", 10000);
    }

    public void setLineSize(float size) {

        SharedPreferences.Editor editor = getEditor();
        editor.putFloat("linesize", size);
        editor.apply();
        editor.commit();

    }

    public float getLineSize() {

        return mPrefs.getFloat("linesize", 5.0f);
    }

    public void setLineColor(int color) {

        SharedPreferences.Editor editor = getEditor();
        editor.putInt("linecolor", color);
        editor.apply();
        editor.commit();

    }

    public int getLineColor() {

        return mPrefs.getInt("linecolor", Color.BLUE);
    }
}