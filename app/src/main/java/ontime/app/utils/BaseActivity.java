package ontime.app.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ontime.app.retrofit.ApiInterface;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Response;


public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        setListener();

    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageManager.setLocale(base));
    }

    protected abstract void initView();


    protected void setListener() {
    }



    public Context getContext() {
        return this;
    }


    public Activity getActivity() {
        return this;
    }

    protected boolean isConnected() {
        return Common.isInternetAvailable(getContext());
    }


    public static ApiInterface getApiInterface() {
        return BaseHelper.getApiInterface();
    }

    protected boolean isHideKeyboard() {
        return Common.hideKeyboard(getActivity());
    }

    public static String printRes(Response<?> response) {
        return BaseHelper.printRes(response);
    }

    protected static void printParams(JsonObject params) {
        BaseHelper.printParams(params);
    }

    public static void printError(Call<?> call, Throwable t) {
        BaseHelper.printError(call, t);
    }

}
