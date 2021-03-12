package ontime.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import ontime.app.model.userlogin.UserData;
import ontime.app.model.usermain.Userdate;
import com.google.gson.Gson;

public class SessionManager {
    private final SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;
    public static String LOGIN = "login";
    public static String USER_DATA = "UserData";
    public static String USER_TYPE = "user_type";

    public SessionManager(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mPrefs.edit();
    }

    public void setStringData(String key, String val) {
        mEditor.putString(key, val);
        mEditor.commit();
    }
    public String getStringData(String key) {
        return mPrefs.getString(key, "");
    }

    public void setBooleanData(String key, Boolean val) {
        mEditor.putBoolean(key, val);
        mEditor.commit();
    }

    public boolean getBooleanData(String key) {
        return mPrefs.getBoolean(key, false);
    }

    public void setIntData(String key, int val) {
        mEditor.putInt(key, val);
        mEditor.commit();
    }
    public void setUserDetails(String key, Userdate val) {
        mEditor.putString(USER_DATA, new Gson().toJson(val));
        mEditor.commit();
    }

    public Userdate getUserDetails() {
        return new Gson().fromJson(mPrefs.getString(USER_DATA, ""), Userdate.class);
    }
    public int getIntData(String key) {
        return mPrefs.getInt(key, 0);
    }

    public void logoutUser() {
        mEditor.clear();
        mEditor.commit();
    }
}
