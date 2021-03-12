package ontime.app.restaurant.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import ontime.app.R;
import ontime.app.databinding.RFragmentCancellingOrderBinding;
import ontime.app.okhttp.APIcall;
import ontime.app.okhttp.AppConstant;
import ontime.app.restaurant.adapte.RvCancelledOrderAdapter;
import ontime.app.restaurant.adapte.RvCompletedOrderAdapter;
import ontime.app.restaurant.model.readerOrder.ReaderCancel;
import ontime.app.restaurant.model.readerOrder.ReaderCompleted;
import ontime.app.restaurant.model.readerOrder.ReaderExample;
import ontime.app.restaurant.ui.Activity.RiderOrderDetails;
import ontime.app.utils.Common;

import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class CompletedFragment extends BaseFragment  implements APIcall.ApiCallListner {

    RvCompletedOrderAdapter rvCompletedOrderAdapter;
    List<ReaderCompleted> readerCompleteds;
    RFragmentCancellingOrderBinding binding;
    static ProgressDialog dialog;

    private void GetAPICallRiderOrderDetails() {
        Common.hideKeyboard(getActivity());
        String url = AppConstant.GET_READER_ORDERDETAIL;
        APIcall apIcall = new APIcall(getContext());
        apIcall.isPost(false);
        apIcall.execute(url, APIcall.OPERATION_READER_ORDERLIST, this);
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (isConnected()) {
            GetAPICallRiderOrderDetails();

        }
    }
    @Override
    protected void initView(View view) {
        binding = RFragmentCancellingOrderBinding.bind(view);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.r_fragment_cancelling_order, container, false);
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
                    readerCompleteds = exampleUser.getResponceData().getOrders().getCompleted();
                    if (readerCompleteds.size() != 0) {
                        binding.rvDetails.setVisibility(View.VISIBLE);
                        binding.txtNoData.setVisibility(View.GONE);
                        LinearLayoutManager mLayoutManager1as = new LinearLayoutManager(getContext());
                        mLayoutManager1as.setOrientation(LinearLayoutManager.VERTICAL);
                        binding.rvDetails.setLayoutManager(mLayoutManager1as);
                        rvCompletedOrderAdapter = new RvCompletedOrderAdapter(getContext(), readerCompleteds);
                        binding.rvDetails.setItemAnimator(new DefaultItemAnimator());
                        binding.rvDetails.setAdapter(rvCompletedOrderAdapter);
                    } else {
                        binding.rvDetails.setVisibility(View.GONE);
                        binding.txtNoData.setVisibility(View.GONE);
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