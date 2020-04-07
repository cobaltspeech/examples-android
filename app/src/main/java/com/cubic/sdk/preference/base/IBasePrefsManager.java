package com.cubic.sdk.preference.base;

import java.util.HashSet;
import java.util.Set;

public interface IBasePrefsManager {

    boolean getParam(String key, boolean defValue);

    double getParam(String key, double defValue);

    String getParam(String key, String defValue);

    int getParam(String key, int defValue);

    long getParam(String key, long defValue);

    void setParam(String key, Object value);

    void setParamImmidiate(String key, Object value);

    Set getParam(String cookies, HashSet hashSet);

    void clear();
}