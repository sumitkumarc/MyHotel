package ontime.app.restaurant.ui.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
import ontime.app.R;
import ontime.app.customer.doneActivity.UserProfileActivity;
import ontime.app.databinding.ActivityNotificationBinding;
import ontime.app.model.restaurantlist.RestaurantExample;
import ontime.app.okhttp.APIcall;
import ontime.app.okhttp.AppConstant;
import ontime.app.restaurant.adapte.MutchantNotificationListAdapter;
import ontime.app.restaurant.model.NotificationExample;
import ontime.app.utils.BaseActivity;
import ontime.app.utils.Common;

import static ontime.app.utils.Common.setSystemBarColor;

public class MurchanNotification extends BaseActivity implements View.OnClickListener, APIcall.ApiCallListner {
    ActivityNotificationBinding binding;
    MutchantNotificationListAdapter madapter;
    private ProgressDialog dialog;

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Common.setSystemBarColor(this, R.color.colorAccent);
//        Common.setSystemBarLight(this);
        binding.ivLogo.setColorFilter(getResources().getColor(R.color.colorAccent));
        binding.ivBack.setColorFilter(getResources().getColor(R.color.colorAccent));
        binding.txtTitle.setTextColor(getResources().getColor(R.color.colorAccent));

//        if (Common.MERCHANT_TYPE == 1) {
//
//        } else {
//            setSystemBarColor(this, R.color.super_mart);
//            Common.setSystemBarLight(this);
//            binding.ivLogo.setColorFilter(getResources().getColor(R.color.super_mart));
//            binding.ivBack.setColorFilter(getResources().getColor(R.color.super_mart));
//            binding.txtTitle.setTextColor(getResources().getColor(R.color.super_mart));
//        }
        LinearLayoutManager mLayoutManager1as = new LinearLayoutManager(getContext());
        mLayoutManager1as.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvList.setLayoutManager(mLayoutManager1as);

        GetAPICallNotificastion();
    }

    @Override
    protected void setListener() {
        super.setListener();
        binding.back.setOnClickListener(this);
        binding.llDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.ll_delete:
                if (isConnected()) {
                    GetAPICallNotificastionDelete();
                }
                break;

        }
    }

    private void GetAPICallNotificastionDelete() {
        JSONObject jsonObject = new JSONObject();
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_READER_DELETE_NOTIFICATION;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_READER_DELETE_NOTIFICATION, this);
    }

    private void showDialog() {
        dialog = new ProgressDialog(MurchanNotification.this);
        dialog.setMessage(getResources().getString(R.string.Please_wait));
        dialog.setCancelable(false);
        dialog.show();
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    private void GetAPICallNotificastion() {
        Common.hideKeyboard(getActivity());
        JSONObject jsonObject = new JSONObject();
//        try {
////            jsonObject.put("name", "Customer");
////            jsonObject.put("contact_number", Phone_no);
//            jsonObject.put("message", message);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_MURCHANT_NOTI;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_NOTIFICATION, this);
    }

    @Override
    public void onStartLoading(int operationCode) {
        if (operationCode == APIcall.OPERATION_NOTIFICATION) {
            showDialog();
        }
        if (operationCode == APIcall.OPERATION_READER_DELETE_NOTIFICATION) {
            showDialog();
        }

    }

    @Override
    public void onProgress(int operationCode, int progress) {

    }

    @Override
    public void onSuccess(int operationCode, String response, Object customData) {
        try {
            if (operationCode == APIcall.OPERATION_NOTIFICATION) {
                hideDialog();
                Gson gson = new Gson();
                NotificationExample exampleUser = gson.fromJson(response, NotificationExample.class);
                if (exampleUser.getStatus() == 200) {
                    if (exampleUser.getResponceData().size() != 0) {
                        binding.llDelete.setVisibility(View.VISIBLE);
                        binding.rvList.setVisibility(View.VISIBLE);
                        binding.txtNoItem.setVisibility(View.GONE);
                        madapter = new MutchantNotificationListAdapter(getContext(), exampleUser.getResponceData());
                        binding.rvList.setItemAnimator(new DefaultItemAnimator());
                        binding.rvList.setAdapter(madapter);
                    } else {
                        binding.llDelete.setVisibility(View.GONE);
                        binding.rvList.setVisibility(View.GONE);
                        binding.txtNoItem.setVisibility(View.VISIBLE);
                    }

                } else {
                    binding.llDelete.setVisibility(View.GONE);
                    binding.rvList.setVisibility(View.GONE);
                    binding.txtNoItem.setVisibility(View.VISIBLE);
                    Toast.makeText(MurchanNotification.this, "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            if (operationCode == APIcall.OPERATION_READER_DELETE_NOTIFICATION) {
                hideDialog();
                JSONObject root = null;
                try {
                    root = new JSONObject(response);
                    Toast.makeText(MurchanNotification.this, "" + root.getString("message"), Toast.LENGTH_SHORT).show();
                    GetAPICallNotificastion();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        } catch (Exception e) {

        }

    }

    @Override
    public void onFail(int operationCode, String response) {

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MurchanNotification.this, RiderOrderDetails.class));
        finish();
    }
}