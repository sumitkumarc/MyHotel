package ontime.app.customer.doneActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import ontime.app.R;
import ontime.app.databinding.ActivityLoginBinding;
import ontime.app.model.usermain.ExampleUser;
import ontime.app.okhttp.APIcall;
import ontime.app.okhttp.AppConstant;
import ontime.app.okhttp.SharedPreferenceManagerFile;
import ontime.app.restaurant.ui.Activity.RiderOrderDetails;
import ontime.app.restaurant.ui.Activity.SplashActivity;
import ontime.app.utils.BaseActivity;
import ontime.app.utils.Common;
import ontime.app.utils.LanguageManager;
import ontime.app.utils.SessionManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.phonenumberui.CountryCodeActivity;
import com.phonenumberui.countrycode.Country;
import com.phonenumberui.countrycode.CountryUtils;
import com.phonenumberui.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import okhttp3.RequestBody;


import static ontime.app.utils.Common.edvalidateName;


public class LoginActivity extends BaseActivity implements View.OnClickListener, APIcall.ApiCallListner {

    ActivityLoginBinding binding;
    private ProgressDialog dialog;
    Boolean aBoolean = false;
    private Country mSelectedCountry;
    private static final int COUNTRYCODE_ACTION = 1;
    SessionManager sessionManager;
    private SharedPreferenceManagerFile sharedPref;
    String country_code;
    String token;
    Locale locale;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sessionManager = new SessionManager(LoginActivity.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = getResources().getConfiguration().locale;
        }

        sharedPref = new SharedPreferenceManagerFile(this);
        Animation bottomUp = AnimationUtils.loadAnimation(this, R.anim.bottan_to_up);
        binding.loginBtn.startAnimation(bottomUp);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        token = task.getResult();
                        Log.d("TAG", token);
                    }
                });

        binding.edPassword.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint({"ClickableViewAccessibility", "UseCompatLoadingForDrawables"})
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                Boolean aBooleans = false;
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        if (locale.getLanguage().equals(LanguageManager.LANGUAGE_KEY_ARABIC)) {
//                          //  Boolean aBooleans = false;
//                            final int DRAWABLE_LIGHT = 3;
//                            if (event.getAction() == MotionEvent.ACTION_UP) {
//                                if (event.getRawX() >= (binding.edPassword.getLeft() - binding.edPassword.getCompoundDrawables()[DRAWABLE_LIGHT].getBounds().width())) {
//                                    if (binding.edPassword.getText().toString().equals("")) {
//                                        binding.edPassword.setText("");
//                                    } else {
//                                        if (aBoolean) {
//                                            binding.edPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                                            binding.edPassword.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(R.drawable.r_eyes_off), null, null, null);
//                                            aBoolean = false;
//                                        } else {
//                                            binding.edPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                                            binding.edPassword.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(R.drawable.r_eyes_on), null, null, null);
//                                            aBoolean = true;
//                                        }
//                                    }
//                                  //  aBooleans = true;
//                                }
//                            }
//                          //  aBooleans = false;
//                        } else {
////                            Boolean aBooleanss = false;
//                            final int DRAWABLE_RIGHT = 2;
//                            if (event.getAction() == MotionEvent.ACTION_UP) {
//                                if (event.getRawX() >= (binding.edPassword.getRight() - binding.edPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
//                                    if (binding.edPassword.getText().toString().equals("")) {
//                                        binding.edPassword.setText("");
//                                    } else {
//                                        if (aBoolean) {
//                                            binding.edPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                                            binding.edPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.drawable.r_eyes_off), null);
//                                            aBoolean = false;
//                                        } else {
//                                            binding.edPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                                            binding.edPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.drawable.r_eyes_on), null);
//                                            aBoolean = true;
//                                        }
//                                    }
//                                    //return true;
//                                }
//                            }
//                          //  return false;
//
//                        }
//                    }
//                });
               return aBoolean;
            }
        });
        getCountrycode();
        binding.etCountryCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.hideKeyBoardFromView(getActivity());
                binding.edPhoneNo.setError(null);
                Intent intent = new Intent(getActivity(), CountryCodeActivity.class);
                intent.putExtra("TITLE", getResources().getString(com.phonenumberui.R.string.app_name));
                startActivityForResult(intent, COUNTRYCODE_ACTION);
            }
        });
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
            binding.etCountryCode.setText("+" + country.getPhoneCode() + "");
            country_code = "+" + country.getPhoneCode();
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
    }

    private void showDialog() {
        dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage(getResources().getString(R.string.Please_wait));
        dialog.setCancelable(false);
        dialog.show();
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }


    @Override
    protected void setListener() {
        super.setListener();
        binding.loginBtn.setOnClickListener(this);
        binding.back.setOnClickListener(this);
        binding.tvForgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                if (isConnected()) {
                    if (
//                            !edvalidatePhoneName(binding.edPhoneNo.getText().toString().trim(), binding.edPhoneNo, getResources().getString(R.string.v_enter_mobile_no)) |
                            !edvalidateName(binding.edPassword.getText().toString().trim(), binding.edPassword, getResources().getString(R.string.v_enter_password))) {
                        return;
                    }
                    GetAPICallLoginUser(binding.edPhoneNo.getText().toString(), binding.edPassword.getText().toString());
                } else {
                    Toast.makeText(this, getResources().getString(R.string.v_check_internet_connnection), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.back:
                onBackPressed();
                break;
            case R.id.tv_forgot_password:
                startActivity(new Intent(LoginActivity.this, ForgotpasswordActivity.class));
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                break;
            default:
                break;
        }
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
//                            setPhoneNumberHint();
                            country_code = "+" + country.getPhoneCode();
                            binding.etCountryCode.setText("+" + country.getPhoneCode() + "");
                            binding.flagImv.setImageResource(CountryUtils.getFlagDrawableResId(country.getIso()));
                        }
                    }
                }
                break;
        }
    }

    private void GetAPICallLoginUser(String PhoneNo, String Password) {
        Common.hideKeyboard(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("contact_number", PhoneNo);
            jsonObject.put("password", Password);
            jsonObject.put("country_code", "+" + mSelectedCountry.getPhoneCode());
            jsonObject.put("device_type", "android");
            jsonObject.put("device_token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_LOGIN_NEW;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_LOGIN_NEW, this);
    }

    @Override
    public void onStartLoading(int operationCode) {
        showDialog();
    }

    @Override
    public void onProgress(int operationCode, int progress) {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onSuccess(int operationCode, String response, Object customData) {
        try {
            hideDialog();
            Gson gson = new Gson();
            ExampleUser exampleUser = gson.fromJson(response, ExampleUser.class);
            if (exampleUser.getStatus() == 200) {
                sessionManager.setBooleanData(SessionManager.LOGIN, true);
                String mainurl = "Bearer " + exampleUser.getResponceData().getUser().getToken();
                sharedPref.setStringSharedPreference(this, SharedPreferenceManagerFile.SESSION_GUID, mainurl);
                sessionManager.setUserDetails("", exampleUser.getResponceData().getUser());
                if (exampleUser.getResponceData().getUser().getUserType().equals("user")) {
                    LanguageManager.setNewLocale(LoginActivity.this, Locale.getDefault().getLanguage());
                    sharedPref.setStringSharedPreference(LoginActivity.this, LanguageManager.LANGUAGE_KEY, Locale.getDefault().getLanguage());
                    Intent intent = new Intent(LoginActivity.this, UserDashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                    finish();
                } else {
                    LanguageManager.setNewLocale(LoginActivity.this, Locale.getDefault().getLanguage());
                    sharedPref.setStringSharedPreference(LoginActivity.this, LanguageManager.LANGUAGE_KEY, Locale.getDefault().getLanguage());
                    Intent i = new Intent(LoginActivity.this, RiderOrderDetails.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    ActivityOptions transitionActivityOptions = null;

                    startActivity(i);
                    finish();
                }
            } else {
                sessionManager.setBooleanData(SessionManager.LOGIN, false);
                Toast.makeText(LoginActivity.this, "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.d("MAINERROR", ">>>>>>" + e.getMessage());
        }
    }

    @Override
    public void onFail(int operationCode, String response) {

    }
}