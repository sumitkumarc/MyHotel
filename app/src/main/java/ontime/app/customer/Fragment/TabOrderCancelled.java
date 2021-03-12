package ontime.app.customer.Fragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import ontime.app.R;
import ontime.app.customer.Adapter.RvCancelledOrderListAdapter;
import ontime.app.customer.Adapter.RvProcessingOrderListAdapter;
import ontime.app.customer.doneActivity.MyOrdersListActivity;
import ontime.app.databinding.TabOrderProcessingBinding;
import ontime.app.model.usermain.OrderCancelled;
import ontime.app.model.usermain.OrderProccessing;
import ontime.app.model.userorder.UserOrderList;
import ontime.app.okhttp.APIcall;
import ontime.app.okhttp.AppConstant;
import ontime.app.utils.Common;


public class TabOrderCancelled extends BaseFragment implements APIcall.ApiCallListner, SwipeRefreshLayout.OnRefreshListener  {

    TabOrderProcessingBinding binding;
    RvCancelledOrderListAdapter mAdapter;
    ProgressDialog dialog;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.tab_order_processing, viewGroup, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isConnected()) {
            inclineWeight();
        } else {
            Common.displayToastMessageShort(getContext(), AppConstant.CONNECTION_ERROR_MSG, false);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isResumed()) {
                if (isConnected()) {
                    inclineWeight();
                } else {
                    Common.displayToastMessageShort(getContext(), AppConstant.CONNECTION_ERROR_MSG, false);
                }
            }
        }
    }

    private void GetAPICallOrderList() {
        Common.hideKeyboard(getActivity());
        JSONObject jsonObject = new JSONObject();
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_USER_ORDER_LIST;
        APIcall apIcall = new APIcall(getActivity());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_ORDER_LIST, this);
    }

    private void inclineWeight() {
        GetAPICallOrderList();
        binding.swipeToRefresh.setOnRefreshListener(this);
        LinearLayoutManager mLayoutManager1as = new LinearLayoutManager(getContext());
        mLayoutManager1as.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvDetails.setLayoutManager(mLayoutManager1as);

    }
    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                GetAPICallOrderList();
                binding.swipeToRefresh.setRefreshing(false);
            }
        }, 2000);
    }

    @Override
    protected void initView(View view) {
        binding = TabOrderProcessingBinding.bind(view);
    }

    private void showDialog() {
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getResources().getString(R.string.Please_wait));
        dialog.setCancelable(false);
        dialog.show();
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    public void onStartLoading(int operationCode) {
        if (operationCode == APIcall.OPERATION_ORDER_LIST) {
            showDialog();
        }
    }

    @Override
    public void onProgress(int operationCode, int progress) {

    }

    @Override
    public void onSuccess(int operationCode, String response, Object customData) {
        try {
            if (operationCode == APIcall.OPERATION_ORDER_LIST) {
                hideDialog();
                try {
                    hideDialog();
                    Gson gson = new Gson();
                    UserOrderList exampleUser = gson.fromJson(response, UserOrderList.class);
                    if (exampleUser.getStatus() == 200) {
                        List<OrderCancelled> objCancelled = exampleUser.getResponceData().getOCancelled();
                        if (objCancelled.size() != 0) {
                            binding.rvDetails.setVisibility(View.VISIBLE);
                            binding.txtNoData.setVisibility(View.GONE);
                            mAdapter = new RvCancelledOrderListAdapter(getContext(), objCancelled);
                            binding.rvDetails.setItemAnimator(new DefaultItemAnimator());
                            binding.rvDetails.setAdapter(mAdapter);
                        } else {
                            binding.rvDetails.setVisibility(View.GONE);
                            binding.txtNoData.setVisibility(View.VISIBLE);
                        }
//                        objCancelled = exampleUser.getResponceData().getOCancelled();
//                        objFinished = exampleUser.getResponceData().getFinished();
                    } else {
                        Toast.makeText(getActivity(), "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.d("SUMITPATEL", "MAINRL" + e.getMessage());
                }
            }
        } catch (Exception e) {
            hideDialog();
        }

    }

    @Override
    public void onFail(int operationCode, String response) {

    }
}
