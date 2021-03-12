package ontime.app.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ontime.app.retrofit.ApiInterface;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Response;

public abstract class BaseAdapter<V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {
    private static final String TAG = "BaseAdapter";
    private Context context;

    @NonNull
    @Override
    public V onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        return onCreateView(viewGroup, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull V v, int position) {
        bindRViewHolder(v, position);
    }

    @Override
    public int getItemCount() {
        return getListCounter();
    }

    protected abstract V onCreateView(ViewGroup viewGroup, int viewType);

    protected abstract void bindRViewHolder(V v, int position);

    protected abstract int getListCounter();

    Context getContext() {
        return context;
    }

    //Network call helper
    protected boolean isConnected() {
        return Common.isInternetAvailable(getContext());
    }





    public ApiInterface getApiInterface() {
        return BaseHelper.getApiInterface();
    }

    public String printRes(Response<?> response) {
        return BaseHelper.printRes(response);
    }

    protected void printParams(JsonObject params) {
        BaseHelper.printParams(params);
    }

    public void printError(Call<?> call, Throwable t) {
        BaseHelper.printError(call, t);
    }



}
