package ontime.app.okhttp;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceManagerFile {
    SharedPreferences moSharedPreferences;
    public static String FILE_NAME = "LANG";
    public static String SELECT_LANG = "LANG";
    public static String SESSION_GUID = "SessionGUID";
    public static String GEST_GUID = "GestUser";
    public static String API_SECRET_KEY = "ApiSecretKey";
    public static String REST_ADMIN_KEY = "RestAPIAdminAccessKey";

    public SharedPreferenceManagerFile(Context mContext) {
        try {
            moSharedPreferences = mContext.getApplicationContext().getSharedPreferences(FILE_NAME, 0 | Context.MODE_MULTI_PROCESS);
        } catch (Exception e) {
            moSharedPreferences = mContext.getApplicationContext().getSharedPreferences(FILE_NAME, 0);
        }
    }

    public static void setStringSharedPreference(Context context, String fsKey, String fsValue) {
        SharedPreferences.Editor loEditor  = context.getApplicationContext().getSharedPreferences(FILE_NAME, 0).edit();
        loEditor.putString(fsKey, fsValue);
        loEditor.commit();
        loEditor.apply();
    }


    public static String getFromStringSharedPreference(Context context, String fsKey) {
        SharedPreferences settings = context.getSharedPreferences(FILE_NAME, 0);
        if (settings != null) {
            return settings.getString(fsKey, "");
        }
        return null;
    }

    public void setBooleanSharedPreference(Context context,String fsKey, Boolean fsValue) {
        SharedPreferences.Editor loEditor  = context.getApplicationContext().getSharedPreferences(FILE_NAME, 0).edit();
        loEditor.putBoolean(fsKey, fsValue);
        loEditor.commit();
        loEditor.apply();
    }
    public Boolean getBooleanSharedPreference(Context context,String fsKey) {
        SharedPreferences settings = context.getSharedPreferences(FILE_NAME, 0);
        if (settings != null) {
            return settings.getBoolean(fsKey, false);
        }
        return null;
    }
}
