package ontime.app.customer.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ontime.app.utils.Common;

public abstract class BaseFragment extends Fragment {

    private Context context;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        initView(view);
        setListener();
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
