package ontime.app.restaurant.ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import okhttp3.RequestBody;
import ontime.app.R;
import ontime.app.customer.Adapter.RvRestaurantCartPenddingListAdapter;
import ontime.app.customer.doneActivity.OrderSummaryActivity;
import ontime.app.databinding.ActivityNewOrderItemListBinding;
import ontime.app.okhttp.APIcall;
import ontime.app.okhttp.AppConstant;
import ontime.app.restaurant.adapte.RvCancelledOrderAdapter;
import ontime.app.restaurant.adapte.RvItemsDetailsListAdapter;
import ontime.app.utils.BaseActivity;
import ontime.app.utils.Common;

public class NewOrderItemListActivity extends BaseActivity implements View.OnClickListener, APIcall.ApiCallListner {
    ActivityNewOrderItemListBinding binding;
    RvItemsDetailsListAdapter mMAdapter;
    ProgressDialog dialog;
    private int mYear, mMonth, mDay, mHour, mMinute;


    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_order_item_list);
    }

    protected void setListener() {
        super.setListener();
        binding.back.setOnClickListener(this);
        binding.btCancelled.setOnClickListener(this);
        binding.btConfirm.setOnClickListener(this);
//        binding.payButtonId.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.bt_confirm:
                final Calendar c = Calendar.getInstance();
                mHour = 00;
                mMinute = 30;
                TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String time_final = hourOfDay + ":" + minute + ":00";
                                ApiCAllToAcceptOrder(Common.READERNEWORDER.getId(), time_final);
//                                    txtTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();
                break;
            case R.id.bt_cancelled:
                if (isConnected()) {
                    ApiCAllToRejectOrder(Common.READERNEWORDER.getId());
                }
                break;
            default:
                break;
        }
    }

    public void ApiCAllToRejectOrder(Integer Order_id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("order_id", Order_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_READER_ORDER_REJECT;
        APIcall apIcall = new APIcall(this);
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_READER_ORDER_REJECT, this);
    }

    public void ApiCAllToAcceptOrder(Integer Order_id, String timefinal) {
//        Common.hideKeyboard(mContext);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("order_id", Order_id);
            jsonObject.put("delivery_time", timefinal);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_READER_ORDER_ACCEPT;
        APIcall apIcall = new APIcall(this);
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_ORDER_ACCEPT, this);
    }

    private void showDialog() {
        dialog = new ProgressDialog(this);
        dialog.setMessage(this.getResources().getString(R.string.Please_wait));
        dialog.setCancelable(false);
        dialog.show();
    }

    public void hideDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    public void onStartLoading(int operationCode) {
        if (operationCode == APIcall.OPERATION_ORDER_ACCEPT) {
            showDialog();
        }
        if (operationCode == APIcall.OPERATION_READER_ORDER_REJECT) {
            showDialog();
        }
    }

    @Override
    public void onProgress(int operationCode, int progress) {

    }

    @Override
    public void onSuccess(int operationCode, String response, Object customData) {
        try {
            JSONObject obj = new JSONObject(response);
            if (operationCode == APIcall.OPERATION_ORDER_ACCEPT) {
                hideDialog();
                if (obj.getString("status").equals("200")) {
                    finish();
                    RiderOrderDetails.binding.picker.scrollToPosition(1);
                    Toast.makeText(this, "" + obj.getString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "" + obj.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }
            if (operationCode == APIcall.OPERATION_READER_ORDER_REJECT) {
                hideDialog();
                if (obj.getString("status").equals("200")) {
                    finish();
                    RiderOrderDetails.binding.picker.scrollToPosition(2);
                    Toast.makeText(this, "" + obj.getString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "" + obj.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }
            hideDialog();
        } catch (Exception e) {
            hideDialog();
        }
    }

    @Override
    public void onFail(int operationCode, String response) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isConnected()) {
            if (Common.READERNEWORDER.getOrderDetail().size() != 0) {
                binding.txtCount.setText("(" + String.valueOf(Common.READERNEWORDER.getOrderDetail().size()) + ")");
                binding.rvDetails.setVisibility(View.VISIBLE);
                binding.txtNoData.setVisibility(View.GONE);
                LinearLayoutManager mLayoutManager1as = new LinearLayoutManager(getContext());
                mLayoutManager1as.setOrientation(LinearLayoutManager.VERTICAL);
                binding.rvDetails.setLayoutManager(mLayoutManager1as);
                mMAdapter = new RvItemsDetailsListAdapter(getContext(), Common.READERNEWORDER.getOrderDetail(),Common.READERNEWORDER.getPaymentStatus());
                binding.rvDetails.setItemAnimator(new DefaultItemAnimator());
                binding.rvDetails.setAdapter(mMAdapter);
            } else {
                binding.rvDetails.setVisibility(View.GONE);
                binding.txtNoData.setVisibility(View.GONE);
            }
        }

    }

}