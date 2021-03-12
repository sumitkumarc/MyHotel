package ontime.app.customer.doneActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import ontime.app.R;
import ontime.app.databinding.ActivityForgetPageBinding;
import ontime.app.model.usermain.ExampleUser;
import ontime.app.okhttp.APIcall;
import ontime.app.okhttp.AppConstant;
import ontime.app.okhttp.SharedPreferenceManagerFile;
import ontime.app.restaurant.ui.Activity.RiderOrderDetails;
import ontime.app.utils.BaseActivity;
import ontime.app.utils.Common;
import ontime.app.utils.SessionManager;
import com.google.gson.Gson;
import com.phonenumberui.CountryCodeActivity;
import com.phonenumberui.countrycode.Country;
import com.phonenumberui.countrycode.CountryUtils;
import com.phonenumberui.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;

import static ontime.app.utils.Common.edvalidateName;
import static ontime.app.utils.Common.edvalidatePhoneName;

public class ForgotpasswordActivity extends BaseActivity implements View.OnClickListener, APIcall.ApiCallListner {
    private ProgressDialog dialog;
    private static final int COUNTRYCODE_ACTION = 1;
    ActivityForgetPageBinding binding;
    private Country mSelectedCountry;
    String country_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getCountrycode();
    }

    @Override
    protected void setListener() {
        super.setListener();
        binding.loginBtn.setOnClickListener(this);
        binding.back.setOnClickListener(this);
        binding.etCountryCode.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case COUNTRYCODE_ACTION:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        if (data.hasExtra("COUNTRY")) {
                            Country country = (Country) data.getSerializableExtra("COUNTRY");
                            this.mSelectedCountry = country;
                            country_code = "+" + country.getPhoneCode();
                            binding.etCountryCode.setText("+" + country.getPhoneCode() + "");
                            binding.flagImv.setImageResource(CountryUtils.getFlagDrawableResId(country.getIso()));
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                if (isConnected()) {
                    if (!edvalidatePhoneName(binding.edPhoneNo.getText().toString().trim(), binding.edPhoneNo, getResources().getString(R.string.v_enter_mobile_no))) {
                        return;
                    }
                    GetAPICallCheckNumberIsExitst(binding.edPhoneNo.getText().toString());
                } else {
                    Toast.makeText(this, getResources().getString(R.string.v_check_internet_connnection), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.back:
                onBackPressed();
                break;
            case R.id.etCountryCode:
                Utility.hideKeyBoardFromView(getActivity());
                binding.edPhoneNo.setError(null);
                Intent intent = new Intent(getActivity(), CountryCodeActivity.class);
                intent.putExtra("TITLE", getResources().getString(com.phonenumberui.R.string.app_name));
                startActivityForResult(intent, COUNTRYCODE_ACTION);
                break;
            default:
                break;
        }
    }

    private void GetAPICallCheckNumberIsExitst(String PhoneNO) {
        Common.hideKeyboard(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("contact_number", PhoneNO);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_CHECK_NUMBER_ISEXIST;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_CHECK_NUMBER_ISEXIST, this);
    }


    private void getCountrycode() {
        TelephonyManager tm = (TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        String countryISO = tm.getNetworkCountryIso();
        String countryNumber = "";
        String countryName = "";
        Utility.log(countryISO);

        if (!TextUtils.isEmpty(countryISO)) {
            for (Country country : CountryUtils.getAllCountries(getContext())) {
                if (countryISO.toLowerCase().equalsIgnoreCase(country.getIso().toLowerCase())) {
                    countryNumber = country.getPhoneCode();
                    countryName = country.getName();
                    break;
                }
            }
            Country country = new Country(countryISO,
                    countryNumber,
                    countryName);
            this.mSelectedCountry = country;
            country_code = "+" + country.getPhoneCode();
            binding.etCountryCode.setText("+" + country.getPhoneCode() + "");
            binding.flagImv.setImageResource(CountryUtils.getFlagDrawableResId(country.getIso()));
            Utility.log(countryNumber);
        } else {
            Country country = new Country(getString(com.phonenumberui.R.string.country_united_states_code),
                    getString(com.phonenumberui.R.string.country_united_states_number),
                    getString(com.phonenumberui.R.string.country_united_states_name));
            this.mSelectedCountry = country;
            binding.etCountryCode.setText("+" + country.getPhoneCode() + "");
            country_code = "+" + country.getPhoneCode();
            binding.flagImv.setImageResource(CountryUtils.getFlagDrawableResId(country.getIso()));
            Utility.log(countryNumber);
        }
    }

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forget_page);
    }

    private void showDialog() {
        dialog = new ProgressDialog(ForgotpasswordActivity.this);
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
        if (operationCode == APIcall.OPERATION_CHECK_NUMBER_ISEXIST) {
            showDialog();
        }
    }

    @Override
    public void onProgress(int operationCode, int progress) {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onSuccess(int operationCode, String response, Object customData) {
        try {
            if (operationCode == APIcall.OPERATION_CHECK_NUMBER_ISEXIST) {
                hideDialog();
                JSONObject root = null;
                try {
                    root = new JSONObject(response);
                    if (root.getString("status").equals("400")) {
                        Intent verificationIntent = new Intent(getContext(), VerificationCodeActivity.class);
                        verificationIntent.putExtra("PhoneNumber", binding.edPhoneNo.getText().toString().trim());
                        verificationIntent.putExtra("PhoneCode", mSelectedCountry.getPhoneCode());
                        verificationIntent.putExtra("FORGOTPWS", 1);
                        startActivity(verificationIntent);
                    } else {
                        Toast.makeText(ForgotpasswordActivity.this, "" + root.getString("message"), Toast.LENGTH_SHORT).show();
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
}