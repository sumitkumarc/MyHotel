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
import ontime.app.databinding.RFragmentNewOrderBinding;
import ontime.app.databinding.RFragmentProcessingOrderBinding;
import ontime.app.okhttp.APIcall;
import ontime.app.okhttp.AppConstant;
import ontime.app.restaurant.adapte.RvNewOrderAdapter;
import ontime.app.restaurant.adapte.RvProcessingOrderAdapter;
import ontime.app.restaurant.model.readerOrder.ReaderExample;
import ontime.app.restaurant.model.readerOrder.ReaderProccessing;
import ontime.app.restaurant.ui.Activity.RiderOrderDetails;
import ontime.app.utils.Common;

import java.util.ArrayList;
import java.util.List;

public class ProcessingFragment extends BaseFragment implements APIcall.ApiCallListner {
    RFragmentProcessingOrderBinding binding;
    RvProcessingOrderAdapter rvProcessingOrderAdapter;
    ArrayList<ReaderProccessing> readerProccessings = new ArrayList<>();
    ProgressDialog dialog;

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
    protected void initView(View view) {
        binding = RFragmentProcessingOrderBinding.bind(view);
    }

    private void GetAPICallRiderOrderDetails() {
        Common.hideKeyboard(getActivity());
        String url = AppConstant.GET_READER_ORDERDETAIL;
        APIcall apIcall = new APIcall(getContext());
        apIcall.isPost(false);
        apIcall.execute(url, APIcall.OPERATION_READER_ORDERLIST, this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isConnected()) {
            LinearLayoutManager mLayoutManager1as = new LinearLayoutManager(getContext());
            mLayoutManager1as.setOrientation(LinearLayoutManager.VERTICAL);
            binding.rvDetails.setLayoutManager(mLayoutManager1as);

            rvProcessingOrderAdapter = new RvProcessingOrderAdapter(getContext(), readerProccessings);
            binding.rvDetails.setItemAnimator(new DefaultItemAnimator());
            binding.rvDetails.setAdapter(rvProcessingOrderAdapter);


            GetAPICallRiderOrderDetails();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.r_fragment_processing_order, container, false);
        return root;
    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (visible && isResumed()) {
//            if (isConnected()) {
//                GetAPICallRiderOrderDetails();
//            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onStop() {
        super.onStop();
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
                    readerProccessings.clear();
                    readerProccessings.addAll(exampleUser.getResponceData().getOrders().getProccessing());
                    if (readerProccessings.size() != 0) {
                        binding.rvDetails.setVisibility(View.VISIBLE);
                        binding.txtNoData.setVisibility(View.GONE);

                    } else {
                        binding.rvDetails.setVisibility(View.GONE);
                        binding.txtNoData.setVisibility(View.GONE);
                    }
                    rvProcessingOrderAdapter.notifyDataSetChanged();
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