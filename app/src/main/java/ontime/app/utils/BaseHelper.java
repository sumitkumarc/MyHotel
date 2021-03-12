package ontime.app.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import androidx.core.content.res.ResourcesCompat;

import ontime.app.R;
import ontime.app.retrofit.APIClient;
import ontime.app.retrofit.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;


import java.io.IOException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Response;


public class BaseHelper {
    private static final String TAG ="vegfarm4u" ;

//    public static void setLangPrefValue(String key, String value, Context context) {
//        SharedPreLangManagFile.setStringSharedPreference(context,key, value);
//    }
//    public static String getPrefValue(String key, Context context) {
//        return SharedPreLangManagFile.getFromStringSharedPreference(context, key);
//    }

//
//    public static void setUserPrefValue(String key, String value, Context context) {
//        SPUserManagerFile.setStringSharedPreference(context,key, value);
//    }
//    public static String getUserPrefValue(String key, Context context) {
//        return SPUserManagerFile.getFromStringSharedPreference(context, key);
//    }
//
//    public static void setIsValidPrefValue(String key, Boolean value, Context context) {
//        SPUserManagerFile.setBooleanSharedPreference(context,key, value);
//    }
//    public static Boolean getIsValidPrefValue(String key, Context context) {
//        return SPUserManagerFile.getBooleanSharedPreference(context,key);
//    }


//    public static Boolean getClearUser(Context context){
//        return SPUserManagerFile.clearPreference(context);
//    }
public static ApiInterface getApiInterface() {
    return APIClient.getClient().create(ApiInterface.class);
}
    public static String printRes(Response<?> response) {
        Log.e(TAG, "Url : " + response.raw().request().url());
        Log.e(TAG, "Response Code : " + response.code());
        Log.e(TAG, "Response : " +
                new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));
        Gson gson = new GsonBuilder().create();
        Error error;
        if (response.code() != 200) {
            try {
                assert response.errorBody() != null;
                error = gson.fromJson(response.errorBody().string(), Error.class);
                return error.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }
    public static void printError(Call<?> call, Throwable t) {
        Log.e(TAG, "Url : " + t.getMessage());
        if (t instanceof SocketTimeoutException) {
            Log.e(TAG, "Connection Timeout");
        } else if (t instanceof IOException) {
            Log.e(TAG, "Timeout");
        } else {
            if (call.isCanceled()) {
                Log.e(TAG, "Call was cancelled forcefully");
            } else {
                Log.e(TAG, "Network Error :: " + t.getLocalizedMessage());
            }
        }
    }
    public static void printParams(JsonObject params) {
        Log.e(TAG, "Params : " +
                new GsonBuilder().setPrettyPrinting().create().toJson(params));
    }
    public static SpannableString getMySpanColor(Context context,String text, int color) {
        SpannableString span = new SpannableString(text);
        span.setSpan(new ForegroundColorSpan(getMyColor(color,context))
                , 0,
                text.length(),
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return span;
    }


    public static int getMyColor(int color, Context context) {
        return ResourcesCompat.getColor(context.getResources(), color, null);
    }
}
