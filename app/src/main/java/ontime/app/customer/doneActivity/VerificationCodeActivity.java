package ontime.app.customer.doneActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import ontime.app.R;
import ontime.app.databinding.ActivityVerifyPageBinding;
import ontime.app.model.usermain.ExampleUser;
import ontime.app.okhttp.APIcall;
import ontime.app.okhttp.AppConstant;
import ontime.app.okhttp.SharedPreferenceManagerFile;
import ontime.app.restaurant.ui.Activity.RiderOrderDetails;
import ontime.app.utils.BaseActivity;
import ontime.app.utils.Common;
import ontime.app.utils.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.phonenumberui.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class VerificationCodeActivity extends BaseActivity implements View.OnClickListener, APIcall.ApiCallListner {

    ActivityVerifyPageBinding binding;
    private String strPhoneCode = "";
    private String strPhoneNumber = "";
    private String strEmailID;
    private String strFullName = "";
    private String strPassword = "";
    private String strFilename = "";
    private String strFileParth = "";
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private CountDownTimer countDownTimer;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private ProgressDialog dialog;
    SessionManager sessionManager;
    private SharedPreferenceManagerFile sharedPref;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.get("image/*");
    int FORGOTPWS = 0;
    String VERIFIY_CODE = "";
    String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sessionManager = new SessionManager(VerificationCodeActivity.this);
        sharedPref = new SharedPreferenceManagerFile(this);
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

        setUpUI();
        if (getIntent().getIntExtra("FORGOTPWS", 0) != FORGOTPWS) {
            if (getIntent().hasExtra("PhoneNumber")) {
                strPhoneNumber = getIntent().getStringExtra("PhoneNumber");
            }
            if (getIntent().hasExtra("PhoneCode")) {
                strPhoneCode = getIntent().getStringExtra("PhoneCode");
            }
        } else {
            if (getIntent().getExtras() != null) {
                if (getIntent().hasExtra("PhoneCode")) {
                    strPhoneCode = getIntent().getStringExtra("PhoneCode");
                }
                if (getIntent().hasExtra("PhoneNumber")) {
                    strPhoneNumber = getIntent().getStringExtra("PhoneNumber");
                }
                if (getIntent().hasExtra("EmailID")) {
                    strEmailID = getIntent().getStringExtra("EmailID");
                }
                if (getIntent().hasExtra("FullName")) {
                    strFullName = getIntent().getStringExtra("FullName");
                }
                if (getIntent().hasExtra("Password")) {
                    strPassword = getIntent().getStringExtra("Password");
                }
                if (getIntent().hasExtra("FILENAME")) {
                    strFilename = getIntent().getStringExtra("FILENAME");
                }
                if (getIntent().hasExtra("FILEPARTH")) {
                    strFileParth = getIntent().getStringExtra("FILEPARTH");
                }

            }

        }

        binding.txtPhoneNo.setText("+" + strPhoneCode + " " + strPhoneNumber + "");
        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Utility.log("onVerificationCompleted: " + credential);
                signInWithPhoneAuthCredential(credential);
                binding.pbVerify.setVisibility(View.GONE);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Utility.log("onVerificationFailed" + e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                } else if (e instanceof FirebaseTooManyRequestsException) {
                }
                binding.pbVerify.setVisibility(View.GONE);
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                Utility.log("onCodeSent: " + verificationId);
                Utility.log("token: " + token);
                binding.pbVerify.setVisibility(View.GONE);
                mVerificationId = verificationId;
                mResendToken = token;

            }
        };
        startPhoneNumberVerification("+ " + strPhoneCode + strPhoneNumber + "");
    }

    private boolean validate() {
        if (TextUtils.isEmpty(binding.etDigit1.getText().toString().trim())) {
            return false;
        } else if (TextUtils.isEmpty(binding.etDigit2.getText().toString().trim())) {
            return false;
        } else if (TextUtils.isEmpty(binding.etDigit3.getText().toString().trim())) {
            return false;
        } else if (TextUtils.isEmpty(binding.etDigit4.getText().toString().trim())) {
            return false;
        } else if (TextUtils.isEmpty(binding.etDigit5.getText().toString().trim())) {
            return false;
        } else if (TextUtils.isEmpty(binding.etDigit6.getText().toString().trim())) {
            return false;
        }
        return true;
    }

    private void setButtonContinueClickbleOrNot() {
        if (!validate()) {
            binding.llContinue.setAlpha(.5f);
            binding.btnContinue.setClickable(false);
        } else {
            binding.llContinue.setAlpha(1.0f);
            binding.btnContinue.setClickable(true);
        }
    }

    private void signOut() {
        mAuth.signOut();
    }

    private void startPhoneNumberVerification(String phoneNumber) {
//        // [START start_phone_auth]
//         PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                phoneNumber,        // Phone number to verify
//                30,                 // Timeout duration
//                TimeUnit.SECONDS,   // Unit of timeout
//                this,               // Activity (for callback binding)
//                mCallbacks);       // OnVerificationStateChangedCallbacks
//        // [END start_phone_auth]


        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

        startCounter();
    }

    private void startCounter() {
        if (countDownTimer != null)
            countDownTimer.cancel();

        countDownTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                binding.tvCountDownTimer.setText("" + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                binding.tvCountDownTimer.setText("");
                binding.btnResendCode.setEnabled(true);
                setResendButtonEnableDisable();
            }

        };
        countDownTimer.start();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                phoneNumber,        // Phone number to verify
//                30,                 // Timeout duration
//                TimeUnit.SECONDS,   // Unit of timeout
//                this,               // Activity (for callback binding)
//                mCallbacks,         // OnVerificationStateChangedCallbacks
//                token);

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);// ForceResendingToken from callbacks
        startCounter();
        binding.btnResendCode.setEnabled(false);
        setResendButtonEnableDisable();
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        binding.pbVerify.setVisibility(View.VISIBLE);
        VERIFIY_CODE = code;
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }


    private void setResendButtonEnableDisable() {
        if (binding.btnResendCode.isEnabled()) {
            binding.rlResend.setBackgroundResource(R.drawable.border_red_dark);
            binding.btnResendCode.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        } else {
            binding.rlResend.setBackgroundResource(R.drawable.border_red_light);
            binding.btnResendCode.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        }
    }

    private void setUpUI() {
        binding.llContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.btnContinue.isClickable())
                    binding.btnContinue.performClick();
            }
        });

        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.hideKeyBoardFromView(getActivity());
                if (validate()) {
                    if (!TextUtils.isEmpty(mVerificationId)) {
                        verifyPhoneNumberWithCode(mVerificationId,
                                binding.etDigit1.getText().toString().trim() +
                                        binding.etDigit2.getText().toString().trim() +
                                        binding.etDigit3.getText().toString().trim() +
                                        binding.etDigit4.getText().toString().trim() +
                                        binding.etDigit5.getText().toString().trim() +
                                        binding.etDigit6.getText().toString().trim());
                    } else {
                        Utility.showToast(VerificationCodeActivity.this, getResources().getString(R.string.Verification_not_received));
                    }
                }
            }
        });


        binding.btnResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.hideKeyBoardFromView(getActivity());
                if (mResendToken != null)
                    resendVerificationCode("+ " + strPhoneCode + strPhoneNumber, mResendToken);
                else {
                    Utility.showToast(VerificationCodeActivity.this, getResources().getString(R.string.Resend_code));
                    onBackPressed();
                }
            }
        });


        setButtonContinueClickbleOrNot();
        binding.etDigit1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setButtonContinueClickbleOrNot();
                if (editable.toString().length() == 1) {
                    binding.etDigit2.requestFocus();
                }
            }
        });
        binding.etDigit2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setButtonContinueClickbleOrNot();
                if (editable.toString().length() == 1) {
                    binding.etDigit3.requestFocus();
                } else {
                    binding.etDigit1.requestFocus();
                }
            }
        });
        binding.etDigit3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setButtonContinueClickbleOrNot();
                if (editable.toString().length() == 1) {
                    binding.etDigit4.requestFocus();
                } else {
                    binding.etDigit2.requestFocus();
                }
            }
        });
        binding.etDigit4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setButtonContinueClickbleOrNot();
                if (editable.toString().length() == 1) {
                    binding.etDigit5.requestFocus();
                } else {
                    binding.etDigit3.requestFocus();
                }
            }
        });
        binding.etDigit5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setButtonContinueClickbleOrNot();
                if (editable.toString().length() == 1) {
                    binding.etDigit6.requestFocus();
                } else {
                    binding.etDigit4.requestFocus();
                }
            }
        });
        binding.etDigit6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setButtonContinueClickbleOrNot();
                if (editable.toString().length() == 1) {
                } else {
                    binding.etDigit5.requestFocus();
                }
            }
        });

        binding.etDigit1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                } else {
                    if (binding.etDigit1.getText().toString().trim().length() == 1) {
                        binding.etDigit2.requestFocus();
                    }
                }
                return false;
            }
        });
        binding.etDigit2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (binding.etDigit2.getText().toString().trim().length() == 0)
                        binding.etDigit1.requestFocus();
                } else {
                    if (binding.etDigit2.getText().toString().trim().length() == 1) {
                        binding.etDigit3.requestFocus();
                    }
                }
                return false;
            }
        });
        binding.etDigit3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (binding.etDigit3.getText().toString().trim().length() == 0)
                        binding.etDigit2.requestFocus();
                } else {
                    if (binding.etDigit3.getText().toString().trim().length() == 1) {
                        binding.etDigit4.requestFocus();
                    }
                }
                return false;
            }
        });
        binding.etDigit4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (binding.etDigit4.getText().toString().trim().length() == 0)
                        binding.etDigit3.requestFocus();
                } else {
                    if (binding.etDigit4.getText().toString().trim().length() == 1) {
                        binding.etDigit5.requestFocus();
                    }
                }
                return false;
            }
        });
        binding.etDigit5.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (binding.etDigit5.getText().toString().trim().length() == 0)
                        binding.etDigit4.requestFocus();
                } else {
                    if (binding.etDigit5.getText().toString().trim().length() == 1) {
                        binding.etDigit6.requestFocus();
                    }
                }
                return false;
            }
        });
        binding.etDigit6.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (binding.etDigit6.getText().toString().trim().length() == 0)
                        binding.etDigit5.requestFocus();
                }
                return false;
            }
        });

    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success_s, update UI with the signed-in user's information
                            Utility.log("signInWithCredential:success_s");
                            binding.pbVerify.setVisibility(View.GONE);
                            final FirebaseUser user = task.getResult().getUser();
                            //    Utility.showToast(VerificationCodeActivity.this, user.getPhoneNumber() + " verified successfully");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    if (getIntent().getIntExtra("FORGOTPWS", 0)  == 0) {
                                        GetAPICallRegistrationUser();
                                    } else {
                                        GetAPICallUpdateOtpUser();

                                    }
                                }
                            }, 500);
                        } else {
                            // Sign in failed, display a message and update the UI
                            binding.pbVerify.setVisibility(View.GONE);
                            Utility.log("signInWithCredential:failure " + task.getException());
                            Utility.showToast(VerificationCodeActivity.this, getResources().getString(R.string.Verification_failed));
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                            onBackPressed();
                            finish();
                        }
                    }
                });
    }

    private void GetAPICallUpdateOtpUser() {
        Common.hideKeyboard(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("contact_number", strPhoneNumber);
            jsonObject.put("country_code", "+" + strPhoneCode);
            jsonObject.put("otp", VERIFIY_CODE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_LOGIN_UPDATE_OTP;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_LOGIN_UPDATE_OTP, this);
    }


    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verify_page);
    }

    @Override
    protected void setListener() {
        super.setListener();
        binding.back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_btn:
                if (isConnected()) {
                } else {
                    Toast.makeText(this, getResources().getString(R.string.v_check_internet_connnection), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.back:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    private void showDialog() {
        dialog = new ProgressDialog(VerificationCodeActivity.this);
        dialog.setMessage(getResources().getString(R.string.Please_wait));
        dialog.setCancelable(false);
        dialog.show();
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }


    private void GetAPICallLoginUser() {
        Common.hideKeyboard(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("contact_number", strPhoneNumber);
            jsonObject.put("password", strPassword);
            jsonObject.put("country_code", "+" + strPhoneCode);
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

    private void GetAPICallRegistrationUser() {
        Common.hideKeyboard(getActivity());
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("full_name", strFullName)
                .addFormDataPart("address", "dummydata")
                .addFormDataPart("country_code", "+" + strPhoneCode)
                .addFormDataPart("email", Common.isStrempty(strEmailID))
                .addFormDataPart("contact_number", strPhoneNumber)
                .addFormDataPart("password", strPassword)
                .addFormDataPart("device_type", "android")
                .addFormDataPart("device_token", token)
                .addFormDataPart("image", strFilename,
                        RequestBody.create(new File(strFileParth), MEDIA_TYPE_PNG))
                .build();
        String url = AppConstant.GET_USER_SIGNUP;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_REGISTET, this);

    }


    @Override
    public void onStartLoading(int operationCode) {
        if (operationCode == APIcall.OPERATION_LOGIN_NEW) {
            showDialog();
        }
        if (operationCode == APIcall.OPERATION_REGISTET) {
            showDialog();
        }
		  if (operationCode == APIcall.OPERATION_LOGIN_UPDATE_OTP) {
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
            if (operationCode == APIcall.OPERATION_LOGIN_NEW) {
                hideDialog();
                Gson gson = new Gson();
                ExampleUser exampleUser = gson.fromJson(response, ExampleUser.class);
                if (exampleUser.getStatus() == 200) {
                    sessionManager.setBooleanData(SessionManager.LOGIN, true);
                    String mainurl = "Bearer " + exampleUser.getResponceData().getUser().getToken();
                    sharedPref.setStringSharedPreference(this, SharedPreferenceManagerFile.SESSION_GUID, mainurl);
                    sessionManager.setUserDetails("", exampleUser.getResponceData().getUser());
                    if (exampleUser.getResponceData().getUser().getUserType().equals("user")) {
                        Intent intent = new Intent(VerificationCodeActivity.this, UserDashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                        finish();
                    } else {
                        Intent i = new Intent(VerificationCodeActivity.this, RiderOrderDetails.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        ActivityOptions transitionActivityOptions = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(VerificationCodeActivity.this);
                        }
                        startActivity(i, transitionActivityOptions.toBundle());
                        finish();
                    }
                } else {
                    sessionManager.setBooleanData(SessionManager.LOGIN, false);
                    Toast.makeText(VerificationCodeActivity.this, "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    finish();
                }
            }
            if (operationCode == APIcall.OPERATION_REGISTET) {
                hideDialog();
                JSONObject root = null;
                try {
                    root = new JSONObject(response);
                    if (root.getString("status").equals("200")) {
                        Toast.makeText(VerificationCodeActivity.this, "" + root.getString("message"), Toast.LENGTH_SHORT).show();
                        GetAPICallLoginUser();
                    } else {
                        Toast.makeText(VerificationCodeActivity.this, "" + root.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(operationCode == APIcall.OPERATION_LOGIN_UPDATE_OTP){
                hideDialog();
                JSONObject root = null;
                try {
                    root = new JSONObject(response);
                    if (root.getString("status").equals("200")) {
                   //     Toast.makeText(VerificationCodeActivity.this, "" + root.getString("message"), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(VerificationCodeActivity.this, ChangePasswordActivity.class);
                        intent.putExtra("PHONE_NO", strPhoneNumber);
                        intent.putExtra("PHONE_OTP", VERIFIY_CODE);
                        intent.putExtra("PhoneCode", "+" + strPhoneCode);
                        intent.putExtra("FORGOT_TYPE", 1);
                        startActivity(intent);
                    } else {
                        Toast.makeText(VerificationCodeActivity.this, "" + root.getString("message"), Toast.LENGTH_SHORT).show();
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