package com.cubic.sdk.preference.base;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.util.HashSet;
import java.util.Set;

public abstract class BasePrefsManager implements IBasePrefsManager {

    protected Context mContext;
    protected SharedPreferences mSharedPreferences;

    public BasePrefsManager(@NonNull Context context, @NonNull String namePrefs) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(namePrefs, Context.MODE_PRIVATE);
    }

    @Override
    public void clear() {
        mSharedPreferences
                .edit()
                .clear().commit();
    }

    @Override
    public boolean getParam(String key, boolean defValue) {
        return mSharedPreferences.getBoolean(key, defValue);
    }

    @Override
    public double getParam(String key, double defValue) {
        return (double) mSharedPreferences.getFloat(key, (float) defValue);
    }

    @Override
    public String getParam(String key, String defValue) {
        return mSharedPreferences.getString(key, defValue);
    }

    @Override
    public int getParam(String key, int defValue) {
        return mSharedPreferences.getInt(key, defValue);
    }

    @Override
    public long getParam(String key, long defValue) {
        return mSharedPreferences.getLong(key, defValue);
    }

    @Override
    public Set getParam(String key, HashSet hashSet) {
        return mSharedPreferences.getStringSet(key, hashSet);
    }

    @Override
    public void setParam(String key, Object value) {
        if (key == null || value == null) {
            return;
        }
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        setValue(editor, key, value);
        editor.apply();
    }

    @Override
    public void setParamImmidiate(String key, Object value) {
        if (key == null || value == null) {
            return;
        }
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        setValue(editor, key, value);
        editor.commit();
    }

    private void setValue(SharedPreferences.Editor editor, String key, Object value){

        if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Double) {
            editor.putFloat(key, ((Double) value).floatValue());
        } else if (value instanceof Set) {
            editor.putStringSet(key, (Set) value);
        }

    }
}
