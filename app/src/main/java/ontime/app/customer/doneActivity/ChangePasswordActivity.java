package ontime.app.customer.doneActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import ontime.app.R;
import ontime.app.customer.Adapter.RvRestaurantListAdapter;
import ontime.app.databinding.ActivityChangePasswordBinding;
import ontime.app.model.usermain.ExampleUser;
import ontime.app.model.usermain.UserRestaurantsData;
import ontime.app.okhttp.APIcall;
import ontime.app.okhttp.AppConstant;
import ontime.app.utils.BaseActivity;
import ontime.app.utils.Common;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.RequestBody;

import static ontime.app.utils.Common.edvalidateMatchingPassword;
import static ontime.app.utils.Common.edvalidateName;
import static ontime.app.utils.Common.edvalidatePhoneName;
import static ontime.app.utils.Common.edvalidatepassword;

public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener, APIcall.ApiCallListner {

    ActivityChangePasswordBinding binding;
    private ProgressDialog dialog;
    Boolean aBoolean = false;
    Boolean CoaBoolean = false;
    int FORGOT_TYPE = 0;
    String strPhoneNo = "";
    String strPhoneOTP = "";
    String strPhoneCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpUI();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpUI() {
        if (Common.MERCHANT_TYPE == 1) {
            Common.setSystemBarColor(this, R.color.colorAccent);
//            Common.setSystemBarLight(this);
            binding.registerBtn.setBackground(getResources().getDrawable(R.drawable.btn_design));
            binding.txtTitle.setTextColor(getResources().getColor(R.color.colorAccent));

        } else {
            Common.setSystemBarColor(this, R.color.super_mart);
//            Common.setSystemBarLight(this);
            binding.registerBtn.setBackground(getResources().getDrawable(R.drawable.super_btn_design));
            binding.txtTitle.setTextColor(getResources().getColor(R.color.super_mart));
        }


        if (FORGOT_TYPE != getIntent().getIntExtra("FORGOT_TYPE", 0)) {
            strPhoneNo = getIntent().getStringExtra("PHONE_NO");
            strPhoneOTP = getIntent().getStringExtra("PHONE_OTP");
            strPhoneCode = getIntent().getStringExtra("PhoneCode");
        }
        binding.edPassword.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint({"ClickableViewAccessibility", "UseCompatLoadingForDrawables"})
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (binding.edPassword.getRight() - binding.edPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (binding.edPassword.getText().toString().equals("")) {
                            binding.edPassword.setText("");
                        } else {
                            if (aBoolean) {
                                binding.edPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                binding.edPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.drawable.r_eyes_off), null);
                                aBoolean = false;
                            } else {
                                binding.edPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                binding.edPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.drawable.r_eyes_on), null);
                                aBoolean = true;
                            }
                        }
                        return true;
                    }
                }
                return false;
            }
        });
        binding.edCoPassword.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint({"ClickableViewAccessibility", "UseCompatLoadingForDrawables"})
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (binding.edCoPassword.getRight() - binding.edCoPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (binding.edCoPassword.getText().toString().equals("")) {
                            binding.edCoPassword.setText("");
                        } else {
                            if (CoaBoolean) {
                                binding.edCoPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                binding.edCoPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.drawable.r_eyes_off), null);
                                CoaBoolean = false;
                            } else {
                                binding.edCoPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                binding.edCoPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.drawable.r_eyes_on), null);
                                CoaBoolean = true;
                            }
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
    }

    @Override
    protected void setListener() {
        super.setListener();
        binding.back.setOnClickListener(this);
        binding.registerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.register_btn:
                if (isConnected()) {
                    if (!edvalidatepassword(binding.edPassword.getText().toString().trim(), binding.edPassword, getResources().getString(R.string.v_enter_password)) | !edvalidatepassword(binding.edCoPassword.getText().toString().trim(), binding.edCoPassword,  getResources().getString(R.string.v_co_enter_password))
                            | !edvalidateMatchingPassword(binding.edCoPassword.getText().toString().trim(), binding.edCoPassword, binding.edPassword.getText().toString().trim())) {

                        return;
                    }
                    if (FORGOT_TYPE == getIntent().getIntExtra("FORGOT_TYPE", 0)) {
                        GetAPICallChangePassword(binding.edCoPassword.getText().toString());
                    } else {
                        GetAPICallForgotPassword(strPhoneNo, strPhoneOTP, binding.edCoPassword.getText().toString());
                    }

                    break;
                }
        }
    }

    private void showDialog() {
        dialog = new ProgressDialog(ChangePasswordActivity.this);
        dialog.setMessage(getResources().getString(R.string.Please_wait));
        dialog.setCancelable(false);
        dialog.show();
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    private void GetAPICallChangePassword(String str_chan) {
        Common.hideKeyboard(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("password", str_chan);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_CHNAGE_PASSWORD;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_CHNAGE_PASSWORD, this);
    }

    @Override
    public void onStartLoading(int operationCode) {
        if (operationCode == APIcall.OPERATION_CHNAGE_PASSWORD) {
            showDialog();
        }
        if (operationCode == APIcall.OPERATION_FORGOT_PASSWORD) {
            showDialog();
        }

    }

    @Override
    public void onProgress(int operationCode, int progress) {

    }

    @Override
    public void onSuccess(int operationCode, String response, Object customData) {
        try {
            if (operationCode == APIcall.OPERATION_CHNAGE_PASSWORD) {
                hideDialog();
                JSONObject root = null;
                try {
                    root = new JSONObject(response);
                    Toast.makeText(ChangePasswordActivity.this, "" + root.getString("message"), Toast.LENGTH_SHORT).show();
                    binding.edCoPassword.setText("");
                    binding.edPassword.setText("");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (operationCode == APIcall.OPERATION_FORGOT_PASSWORD) {
                hideDialog();
                JSONObject root = null;
                try {
                    root = new JSONObject(response);
                    if (root.getString("status").equals("200")) {
                        Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        Toast.makeText(ChangePasswordActivity.this, "" + root.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, "" + root.getString("message"), Toast.LENGTH_SHORT).show();
                    }
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

    private void GetAPICallForgotPassword(String strPhoneNumber, String PhoneNO, String password) {
        Common.hideKeyboard(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("contact_number", strPhoneNumber);
            jsonObject.put("password", password);
            jsonObject.put("country_code", strPhoneCode);
            jsonObject.put("otp", PhoneNO);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_FORGOT_PASSWORD;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_FORGOT_PASSWORD, this);
    }
}