package ontime.app.customer.doneActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import ontime.app.R;
import ontime.app.databinding.ActivityContactusBinding;
import ontime.app.model.usermain.ExampleUser;
import ontime.app.okhttp.APIcall;
import ontime.app.okhttp.AppConstant;
import ontime.app.utils.BaseActivity;
import ontime.app.utils.Common;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;

import static ontime.app.utils.Common.edvalidateName;
import static ontime.app.utils.Common.edvalidatePhoneName;

public class ContactusActivity extends BaseActivity implements View.OnClickListener, APIcall.ApiCallListner {
    ActivityContactusBinding binding;
    private ProgressDialog dialog;

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contactus);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setUpUI() {
        if (Common.MERCHANT_TYPE == 1) {
            Common.setSystemBarColor(this, R.color.colorAccent);
//            Common.setSystemBarLight(this);
            binding.ivBack.setColorFilter(getResources().getColor(R.color.colorAccent));
            binding.llTop.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            binding.btSend.setBackground(getResources().getDrawable(R.drawable.btn_goldenrect));
            binding.llMsg.setBackground(getResources().getDrawable(R.drawable.rest_contecte_border));
            binding.llPhone.setBackground(getResources().getDrawable(R.drawable.rest_contecte_border));

        } else {
            Common.setSystemBarColor(this, R.color.super_mart);
//            Common.setSystemBarLight(this);
            binding.ivBack.setColorFilter(getResources().getColor(R.color.super_mart));
            binding.llTop.setBackgroundColor(getResources().getColor(R.color.super_mart));
            binding.llMsg.setBackground(getResources().getDrawable(R.drawable.super_contecte_border));
            binding.btSend.setBackground(getResources().getDrawable(R.drawable.btn_goldenrectorange));
            binding.llPhone.setBackground(getResources().getDrawable(R.drawable.super_contecte_border));
        }
    }

    @Override
    protected void setListener() {
        super.setListener();
        binding.back.setOnClickListener(this);
        binding.btSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.bt_send:
                if (!edvalidateName(binding.edMsg.getText().toString().trim(), binding.edMsg, getResources().getString(R.string.v_enter_message))
                        | !edvalidatePhoneName(binding.edPhoneNo.getText().toString().trim(), binding.edPhoneNo, getResources().getString(R.string.v_enter_mobile_no))) {
                    return;
                }
                GetAPICallContectUs(binding.edMsg.getText().toString(), binding.edPhoneNo.getText().toString());
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpUI();
    }

    private void showDialog() {
        dialog = new ProgressDialog(ContactusActivity.this);
        dialog.setMessage(getResources().getString(R.string.Please_wait));
        dialog.setCancelable(false);
        dialog.show();
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    private void GetAPICallContectUs(String Phone_no, String message) {
        Common.hideKeyboard(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", "Customer");
            jsonObject.put("contact_number", Phone_no);
            jsonObject.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_CONTECT_US;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_CONTECT_US, this);
    }

    @Override
    public void onStartLoading(int operationCode) {
        if (operationCode == APIcall.OPERATION_CONTECT_US) {
            showDialog();
        }

    }

    @Override
    public void onProgress(int operationCode, int progress) {

    }

    @Override
    public void onSuccess(int operationCode, String response, Object customData) {
        try {
            if (operationCode == APIcall.OPERATION_CONTECT_US) {
                hideDialog();
                Gson gson = new Gson();
                ExampleUser exampleUser = gson.fromJson(response, ExampleUser.class);
                if (exampleUser.getResponceData().getContact() != null) {
                    binding.edPhoneNo.setText("");
                    binding.edMsg.setText("");
                    Toast.makeText(ContactusActivity.this, "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ContactusActivity.this, "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {

        }

    }

    @Override
    public void onFail(int operationCode, String response) {

    }

}