package ontime.app.restaurant.ui.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import ontime.app.R;
import ontime.app.databinding.RFragmentNewOrderBinding;
import ontime.app.okhttp.APIcall;
import ontime.app.okhttp.AppConstant;
import ontime.app.restaurant.adapte.DItemadapter;
import ontime.app.restaurant.adapte.RvNewOrderAdapter;
import ontime.app.restaurant.model.readerOrder.ReaderExample;
import ontime.app.restaurant.model.readerOrder.ReaderNewOrder;
import ontime.app.restaurant.ui.Activity.RiderOrderDetails;
import ontime.app.utils.Common;

import java.util.List;

public class NewOrderFragment extends BaseFragment implements View.OnClickListener, APIcall.ApiCallListner {

    static RFragmentNewOrderBinding binding;
    RvNewOrderAdapter rvNewOrderAdapter;
    static List<ReaderNewOrder> readerNewOrders;
    ProgressDialog dialog;
    static int Order_id = 0;
    static int mPosition = 0;


    @Override
    protected void initView(View view) {
        binding = RFragmentNewOrderBinding.bind(view);
    }

    private void GetAPICallRiderOrderDetails() {
        Common.hideKeyboard(getActivity());
        String url = AppConstant.GET_READER_ORDERDETAIL;
        APIcall apIcall = new APIcall(getContext());
        apIcall.isPost(false);
        apIcall.execute(url, APIcall.OPERATION_READER_ORDERLIST, this);
    }

    @Override
    protected void setListener() {
        super.setListener();
    }
    @Override
    public void setUserVisibleHint(boolean visible)
    {
        super.setUserVisibleHint(visible);
        if (visible && isResumed())
        {
            if (isConnected()) {
                GetAPICallRiderOrderDetails();
            }
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_timer_confirm:
                RiderOrderDetails.binding.picker.scrollToPosition(1);
                break;
            default:
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isConnected()) {
            GetAPICallRiderOrderDetails();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.r_fragment_new_order, container, false);
        return root;
    }

    private void showDialog() {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage(getContext().getResources().getString(R.string.Please_wait));
        dialog.setCancelable(false);
        dialog.show();
    }

    public void hideDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    public void onStartLoading(int operationCode) {
        if (operationCode == APIcall.OPERATION_READER_ORDERLIST) {
            showDialog();
        }
    }

    @Override
    public void onProgress(int operationCode, int progress) {

    }

    @Override
    public void onSuccess(int operationCode, String response, Object customData) {
        try {
            if (operationCode == APIcall.OPERATION_READER_ORDERLIST) {
                hideDialog();
                Gson gson = new Gson();
                ReaderExample exampleUser = gson.fromJson(response, ReaderExample.class);
                if (exampleUser.getResponceData() != null) {
                    readerNewOrders = exampleUser.getResponceData().getOrders().getNewOrder();
                    if (isConnected()) {
                        if (readerNewOrders.size() != 0) {
                            LinearLayoutManager mLayoutManager1as = new LinearLayoutManager(getContext());
                            mLayoutManager1as.setOrientation(LinearLayoutManager.VERTICAL);
                            binding.rvDetails.setLayoutManager(mLayoutManager1as);
                            rvNewOrderAdapter = new RvNewOrderAdapter(getContext(), readerNewOrders);
                            binding.rvDetails.setItemAnimator(new DefaultItemAnimator());
                            binding.rvDetails.setAdapter(rvNewOrderAdapter);
                            binding.rvDetails.setVisibility(View.VISIBLE);
                            binding.txtNoData.setVisibility(View.GONE);
                            binding.flDetails.setVisibility(View.GONE);
                            binding.flTime.setVisibility(View.GONE);
                        } else {
                            binding.rvDetails.setVisibility(View.GONE);
                            binding.txtNoData.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            hideDialog();
        } catch (Exception e) {

        }
    }

    @Override
    public void onFail(int operationCode, String response) {

    }
}