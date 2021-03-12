package ontime.app.restaurant.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ontime.app.retrofit.ApiInterface;
import ontime.app.utils.BaseHelper;
import ontime.app.utils.Common;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Response;

public abstract class BaseFragment extends Fragment {

    private Context context;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        initView(view);
        setListener();
        setLabel();
    }
    public static ApiInterface getApiInterface() {
        return Common.getApiInterface();
    }

    public static String printRes(Response<?> response) {
        return Common.printRes(response);
    }

    protected static void printParams(JsonObject params) {
        Common.printParams(params);
    }

    public static void printError(Call<?> call, Throwable t) {
        Common.printError(call, t);
    }
    protected abstract void initView(View view);

    protected void setLabel() {
    }

    protected void setListener() {
    }

    protected void setToolbar() {
    }




    //Network call helper
    protected boolean isConnected() {
        return Common.isInternetAvailable(getContext());
    }




}
