package ontime.app.customer.doneActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import ontime.app.R;
import ontime.app.databinding.ActivityRegistetPageBinding;
import ontime.app.utils.BaseActivity;
import ontime.app.utils.Common;
import com.phonenumberui.CountryCodeActivity;
import com.phonenumberui.countrycode.Country;
import com.phonenumberui.countrycode.CountryUtils;
import com.phonenumberui.utility.Utility;

import java.io.File;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;
import okhttp3.MediaType;


import static ontime.app.utils.Common.edvalidateName;
import static ontime.app.utils.Common.edvalidatePhoneName;
import static ontime.app.utils.Common.edvalidatepassword;

public class RegistetActivity extends BaseActivity implements View.OnClickListener {
    private String mobileNumber = "";
    Button register_btn;
    ActivityRegistetPageBinding binding;
    private ProgressDialog dialog;
    private static final int COUNTRYCODE_ACTION = 1;
    private Country mSelectedCountry;
    Boolean aBoolean = false;
    private PhoneNumberUtil mPhoneUtil;
    public static final int SELECT_PICTURE = 101;
    String imageUploadpath = "";
    String imageUploadFileName = "";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.get("image/*");

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Animation bottomUp = AnimationUtils.loadAnimation(this, R.anim.bottan_to_up);
        binding.registerBtn.startAnimation(bottomUp);

        mPhoneUtil = PhoneNumberUtil.createInstance(getContext());
        getCountrycode();
        //  setPhoneNumberHint();

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
        binding.edEmailId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
//                if (s.length() == 0) {
//                    binding.prefix.setVisibility(View.VISIBLE);
//                } else {
//                    binding.prefix.setVisibility(View.GONE);
//                }
            }
        });
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
            binding.flagImv.setImageResource(CountryUtils.getFlagDrawableResId(country.getIso()));
            Utility.log(countryNumber);
        } else {
            Country country = new Country(getString(com.phonenumberui.R.string.country_united_states_code),
                    getString(com.phonenumberui.R.string.country_united_states_number),
                    getString(com.phonenumberui.R.string.country_united_states_name));
            this.mSelectedCountry = country;
            binding.etCountryCode.setText("+" + country.getPhoneCode() + "");
            binding.flagImv.setImageResource(CountryUtils.getFlagDrawableResId(country.getIso()));
            Utility.log(countryNumber);
        }
    }

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registet_page);
    }

    private void showDialog() {
        dialog = new ProgressDialog(RegistetActivity.this);
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
        binding.registerBtn.setOnClickListener(this);
        binding.cvUploadimage.setOnClickListener(this);
        binding.back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_btn:
                if (isConnected()) {
                    if (!edvalidateName(binding.edFullname.getText().toString().trim(), binding.edFullname, getResources().getString(R.string.v_enter_full_name))
                            | !edvalidatePhoneName(binding.edPhoneNo.getText().toString().trim(), binding.edPhoneNo, getResources().getString(R.string.v_enter_mobile_no))
//                            | !edvalidatePhoneName(binding.edAddress.getText().toString().trim(), binding.edAddress, "Enter address.")
                            | !edvalidatepassword(binding.edPassword.getText().toString().trim(), binding.edPassword, getResources().getString(R.string.v_enter_password))) {
                        return;
                    }
                    if (imageUploadpath.equals("")) {
                        Toast.makeText(this, getResources().getString(R.string.v_select_profile_image), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (validate()) {
                        Intent verificationIntent = new Intent(getContext(), VerificationCodeActivity.class);
                        verificationIntent.putExtra("PhoneNumber", binding.edPhoneNo.getText().toString().trim());
                        verificationIntent.putExtra("PhoneCode", mSelectedCountry.getPhoneCode());
                        verificationIntent.putExtra("EmailID", binding.edEmailId.getText().toString());
                        verificationIntent.putExtra("FullName", binding.edFullname.getText().toString());
                        verificationIntent.putExtra("FILENAME", imageUploadFileName);
                        verificationIntent.putExtra("FILEPARTH", imageUploadpath);
                        verificationIntent.putExtra("Password", binding.edPassword.getText().toString());
                        startActivity(verificationIntent);
                    }
                } else {
                    Toast.makeText(this, getResources().getString(R.string.v_check_internet_connnection), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.back:
                onBackPressed();
                break;
            case R.id.cv_uploadimage:
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, SELECT_PICTURE);
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
                            binding.etCountryCode.setText("+" + country.getPhoneCode() + "");
                            binding.flagImv.setImageResource(CountryUtils.getFlagDrawableResId(country.getIso()));
                        }
                    }
                }
                break;
            case SELECT_PICTURE:
                if (resultCode == RESULT_OK) {
                    try {
                        Uri selectedImageUri = data.getData();
                        imageUploadpath = getPath(selectedImageUri);
                        File N_file = Common.getCompressed(RegistetActivity.this, imageUploadpath);
                        imageUploadpath = N_file.getPath();
                        imageUploadFileName = new File(imageUploadpath).getName();
                        Glide.with(RegistetActivity.this).load(imageUploadpath).centerCrop().placeholder(R.mipmap.image_placeholder_default).into(binding.ivUserProfile);
                    } catch (Exception e) {
                        Log.d("SUMITPATEL", "EROOR" + e.toString());
                    }
                }
                break;
        }
    }


    private boolean validate() {
        if (TextUtils.isEmpty(binding.edPhoneNo.getText().toString().trim())) {
            binding.edPhoneNo.setError(getResources().getString(R.string.v_enter_mobile_no));
            binding.edPhoneNo.requestFocus();
            return false;
        } else if (!isValid()) {
            binding.edPhoneNo.setError(getResources().getString(R.string.v_valid_phone_no));
            binding.edPhoneNo.requestFocus();
            return false;
        }
        return true;
    }

    public boolean isValid() {
        Phonenumber.PhoneNumber phoneNumber = getPhoneNumber();
        return phoneNumber != null && mPhoneUtil.isValidNumber(phoneNumber);
    }

    public Phonenumber.PhoneNumber getPhoneNumber() {
        try {
            String iso = null;
            if (mSelectedCountry != null) {
                iso = mSelectedCountry.getIso().toUpperCase();
            }
            return mPhoneUtil.parse(binding.edPhoneNo.getText().toString().trim(), iso);
        } catch (NumberParseException ignored) {
            ignored.printStackTrace();
            return null;
        }
    }
    String res = "";
    public String getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.managedQuery(uri, projection, null, null, null);
        if (cursor.moveToFirst()) {
            res = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
        }
//        if (cursor == null) return null;
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        String s = cursor.getString(column_index);
//        if (cursor != null) {
//            cursor.close();
//        }
        return res;
    }

}