package ontime.app.customer.doneActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import com.bumptech.glide.Glide;

import ontime.app.R;
import ontime.app.databinding.ActivityProfileBinding;
import ontime.app.model.usermain.ExampleUser;
import ontime.app.model.usermain.Userdate;
import ontime.app.okhttp.APIcall;
import ontime.app.okhttp.AppConstant;
import ontime.app.okhttp.SharedPreferenceManagerFile;
import ontime.app.restaurant.ui.Activity.SplashActivity;
import ontime.app.restaurant.ui.Activity.WelcomeActivity;
import ontime.app.utils.BaseActivity;
import ontime.app.utils.Common;
import ontime.app.utils.LanguageManager;
import ontime.app.utils.SessionManager;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import static ontime.app.utils.Common.edvalidateName;
import static ontime.app.utils.Common.edvalidateemailid;


public class UserProfileActivity extends BaseActivity implements View.OnClickListener, APIcall.ApiCallListner {

    SessionManager sessionManager;
    Boolean isUpdate = false;
    ActivityProfileBinding binding;
    private ProgressDialog dialog;
    public static Integer SELECT_PICTURE = 101;
    String imageUploadpath = "";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.get("image/*");
    Cursor cursor;

    String user_name = "";
    String user_email = "";
    String user_address = "";
    String FileName = "";
    String Filepath = "";
    private SharedPreferenceManagerFile sharedPref;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(UserProfileActivity.this);
        isUpdate = false;
        setUpUI();
    }

    private void setUpUI() {
        if (Common.MERCHANT_TYPE == 1) {
            Common.setSystemBarColor(this, R.color.colorAccent);
//            Common.setSystemBarLight(this);
            binding.llToolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            binding.rlProfile.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            binding.llOrder.setBackground(getResources().getDrawable(R.drawable.rest_contecte_border));

            binding.llCancelled.setBackground(getResources().getDrawable(R.drawable.rest_contecte_border));
            binding.llTotalSpendes.setBackground(getResources().getDrawable(R.drawable.rest_contecte_border));
            binding.txtOrder.setTextColor(getResources().getColor(R.color.colorAccent));
            binding.txtCancelled.setTextColor(getResources().getColor(R.color.colorAccent));
            binding.txtTotalSpend.setTextColor(getResources().getColor(R.color.colorAccent));
            binding.ivBackArrow.setColorFilter(getResources().getColor(R.color.colorAccent));
        } else {
            Common.setSystemBarColor(this, R.color.super_mart);
//            Common.setSystemBarLight(this);
            binding.llToolbar.setBackgroundColor(getResources().getColor(R.color.super_mart));
            binding.rlProfile.setBackgroundColor(getResources().getColor(R.color.super_mart));
            binding.llOrder.setBackground(getResources().getDrawable(R.drawable.super_contecte_border));
            binding.llCancelled.setBackground(getResources().getDrawable(R.drawable.super_contecte_border));
            binding.llTotalSpendes.setBackground(getResources().getDrawable(R.drawable.super_contecte_border));
            binding.txtOrder.setTextColor(getResources().getColor(R.color.super_mart));
            binding.txtCancelled.setTextColor(getResources().getColor(R.color.super_mart));
            binding.txtTotalSpend.setTextColor(getResources().getColor(R.color.super_mart));
            binding.ivBackArrow.setColorFilter(getResources().getColor(R.color.super_mart));
        }

        binding.llMain.setVisibility(View.GONE);
        binding.edName.setEnabled(false);
        binding.edPhoneNo.setEnabled(false);
        binding.flProfile.setVisibility(View.GONE);

        binding.txtEdit.setText(getString(R.string.Edit));

        Glide.with(getContext()).load(sessionManager.getUserDetails().getImage()).centerCrop().placeholder(R.mipmap.image_placeholder_default).into(binding.ivUserProfile);
        GetAPICallUserProfile();
        GetAPICallUserProfileDashboard();
    }

    private void GetAPICallUserProfileDashboard() {
        Common.hideKeyboard(getActivity());
        String url = AppConstant.GET_USER_DASHBOARD;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(false);
        apIcall.execute(url, APIcall.OPERATION_USER_DASHBOARD, this);
    }

    private void GetAPICallUserLogout() {
        Common.hideKeyboard(getActivity());
        String url = AppConstant.GET_USER_LOGOUT;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(false);
        apIcall.execute(url, APIcall.OPERATION_USER_LOGOUT, this);
    }


    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.txt_edit:
                showDialogProfileUpdateEdit();
                break;
            case R.id.txt_save:
                showDialogProfileUpdateSave();
                break;
            case R.id.fl_update_profile:
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, SELECT_PICTURE);
                break;
            case R.id.txt_sign_out:
                if (isConnected()) {
                    GetAPICallUserProfileLogout();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void setListener() {
        super.setListener();
        binding.txtSignOut.setOnClickListener(this);
        binding.txtEdit.setOnClickListener(this);
        binding.txtSave.setOnClickListener(this);
        binding.flUpdateProfile.setOnClickListener(this);
        binding.back.setOnClickListener(this);
    }

    private void showDialog() {
        dialog = new ProgressDialog(UserProfileActivity.this);
        dialog.setMessage(getResources().getString(R.string.Please_wait));
        dialog.setCancelable(false);
        dialog.show();
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    private void GetAPICallUserProfile() {
        Common.hideKeyboard(getActivity());
        String url = AppConstant.GET_USER_PROFILE;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(false);
        apIcall.execute(url, APIcall.OPERATION_USER_PROFILE, this);
    }

    private void GetAPICallUserProfileLogout() {
        Common.hideKeyboard(getActivity());
        String url = AppConstant.GET_USER_LOGOUT;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(false);
        apIcall.execute(url, APIcall.OPERATION_USER_LOGOUT, this);
    }

    private void GetAPICallUploadUserImage(String FileName, String Filepath) {
        Common.hideKeyboard(getActivity());
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", FileName,
                        RequestBody.create(new File(Filepath), MEDIA_TYPE_PNG))
                .build();

        String url = AppConstant.GET_USER_UPLOAD_IMAGE;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_USER_UPLOAD_IMAGE, this);

    }

    @Override
    public void onStartLoading(int operationCode) {
        if (operationCode == APIcall.OPERATION_USER_PROFILE) {
            //  showDialog();
        }
        if (operationCode == APIcall.OPERATION_USER_LOGOUT) {
            showDialog();
        }
        if (operationCode == APIcall.OPERATION_USER_UPDATE_PROFILE) {
            showDialog();
        }
        if (operationCode == APIcall.OPERATION_USER_UPLOAD_IMAGE) {
            showDialog();
        }
        if (operationCode == APIcall.OPERATION_USER_DASHBOARD) {
            showDialog();
        }
    }

    @Override
    public void onProgress(int operationCode, int progress) {

    }

    @Override
    public void onSuccess(int operationCode, String response, Object customData) {
        try {
            if (operationCode == APIcall.OPERATION_USER_PROFILE) {
                hideDialog();
                binding.llMain.setVisibility(View.VISIBLE);
                Gson gson = new Gson();
                ExampleUser exampleUser = gson.fromJson(response, ExampleUser.class);
                Userdate user_date = exampleUser.getResponceData().getUser();
                if (user_date != null) {
                    imageUploadpath = user_date.getImage();
                    user_name = user_date.getFullName();
                    user_email = user_date.getEmail();
                    user_address = user_date.getAddress();

                    binding.edName.setText(user_date.getFullName());
                    binding.edPhoneNo.setText(user_date.getContactNumber());
                    sessionManager.setUserDetails("", exampleUser.getResponceData().getUser());

                } else {
                    Toast.makeText(this, exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            if (operationCode == APIcall.OPERATION_USER_UPLOAD_IMAGE) {
                hideDialog();
                JSONObject root = null;
                try {
                    root = new JSONObject(response);
                    if (root.getString("status").equals("200")) {
                        Toast.makeText(UserProfileActivity.this, "" + root.getString("message"), Toast.LENGTH_SHORT).show();
                        GetAPICallUserProfile();
                    } else {
                        Toast.makeText(UserProfileActivity.this, "" + root.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (operationCode == APIcall.OPERATION_USER_UPDATE_PROFILE) {
                hideDialog();
                JSONObject root = null;
                try {
                    root = new JSONObject(response);
                    Toast.makeText(UserProfileActivity.this, "" + root.getString("message"), Toast.LENGTH_SHORT).show();
                    GetAPICallUserProfile();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (operationCode == APIcall.OPERATION_USER_DASHBOARD) {
                hideDialog();
                Gson gson = new Gson();
                ExampleUser exampleUser = gson.fromJson(response, ExampleUser.class);
                if (exampleUser.getStatus() == 200) {
                    binding.txtCancelled.setText(String.valueOf(exampleUser.getResponceData().getCancelled()));
                    binding.txtOrder.setText(String.valueOf(exampleUser.getResponceData().getTotalOrder()));
                    binding.txtTotalSpend.setText(String.valueOf(exampleUser.getResponceData().getTotalSpend()));
                } else {
                    Toast.makeText(this, exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            if (operationCode == APIcall.OPERATION_USER_LOGOUT) {
                hideDialog();
                JSONObject root = null;
                try {
                    root = new JSONObject(response);
                    Toast.makeText(UserProfileActivity.this, "" + root.getString("message"), Toast.LENGTH_SHORT).show();
                    sessionManager.logoutUser();
                    sharedPref = new SharedPreferenceManagerFile(UserProfileActivity.this);
                    sharedPref.setBooleanSharedPreference(UserProfileActivity.this, LanguageManager.LANGUAGE_CHANGE, true);
                    if (sharedPref.getFromStringSharedPreference(UserProfileActivity.this, LanguageManager.LANGUAGE_KEY).equals("en")) {
                        LanguageManager.setNewLocale(UserProfileActivity.this, LanguageManager.LANGUAGE_KEY_ENGLISH);
                        sharedPref.setStringSharedPreference(UserProfileActivity.this, LanguageManager.LANGUAGE_KEY, LanguageManager.LANGUAGE_KEY_ENGLISH);
                    } else {
                        LanguageManager.setNewLocale(UserProfileActivity.this, LanguageManager.LANGUAGE_KEY_ARABIC);
                        sharedPref.setStringSharedPreference(UserProfileActivity.this, LanguageManager.LANGUAGE_KEY, LanguageManager.LANGUAGE_KEY_ARABIC);
                    }
                    Intent intent = new Intent(UserProfileActivity.this, WelcomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        } catch (Exception e) {
            hideDialog();
        }
        hideDialog();

    }

    @Override
    public void onFail(int operationCode, String response) {

    }

    public void showDialogProfileUpdate() {
        final Dialog dialogsPro = new Dialog(this);
        dialogsPro.setCancelable(true);
        dialogsPro.setContentView(R.layout.pop_edit_profile);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogsPro.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        dialogsPro.getWindow().setAttributes(lp);
        dialogsPro.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        final EditText ed_fullname = dialogsPro.findViewById(R.id.ed_fullname);
        final EditText ed_email_id = dialogsPro.findViewById(R.id.ed_email_id);
        final EditText ed_address = dialogsPro.findViewById(R.id.ed_address);

        ed_fullname.setText(user_name);
        ed_email_id.setText(user_email);
        ed_address.setText(user_address);

        dialogsPro.findViewById(R.id.bt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogsPro.dismiss();
            }
        });

        dialogsPro.findViewById(R.id.register_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected()) {
                    if (!edvalidateName(ed_fullname.getText().toString().trim(), ed_fullname, getResources().getString(R.string.v_enter_full_name))
                            | !edvalidateemailid(ed_email_id.getText().toString().trim(), ed_email_id, getResources().getString(R.string.v_valid_email_id))
                            | !edvalidateName(ed_address.getText().toString().trim(), ed_address, getResources().getString(R.string.v_enter_password))) {
                        return;
                    }

                    if (imageUploadpath.equals("")) {
                        Toast.makeText(UserProfileActivity.this, getResources().getString(R.string.v_select_profile_image), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    GetAPICallUpdateProfile(ed_fullname.getText().toString(), ed_email_id.getText().toString(), ed_address.getText().toString());
                    dialogsPro.dismiss();
                }
            }
        });
        dialogsPro.show();
    }

    public void showDialogProfileUpdateEdit() {
        binding.edName.setEnabled(true);
        binding.edName.requestFocus();
        binding.flProfile.setVisibility(View.VISIBLE);
        binding.txtSave.setVisibility(View.VISIBLE);
        binding.txtEdit.setVisibility(View.GONE);
    }

    public void showDialogProfileUpdateSave() {

        if (isConnected()) {
            if (!edvalidateName(binding.edName.getText().toString().trim(), binding.edName, getResources().getString(R.string.v_enter_full_name))) {
                return;
            }

            if (imageUploadpath.equals("")) {
                Toast.makeText(UserProfileActivity.this, getResources().getString(R.string.v_select_profile_image), Toast.LENGTH_SHORT).show();
                return;
            }
            GetAPICallUpdateProfile(binding.edName.getText().toString(), user_email, user_address);
            binding.edName.setEnabled(false);
            binding.flProfile.setVisibility(View.GONE);
            binding.txtSave.setVisibility(View.GONE);
            binding.txtEdit.setVisibility(View.VISIBLE);
        }
    }

    private void GetAPICallUpdateProfile(String fullname, String email_id, String address) {
        JSONObject jsonObject = new JSONObject();
        try {
            RequestBody body;
            if (!FileName.equals("")) {
                 body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("image", FileName,
                                RequestBody.create(new File(Filepath), MEDIA_TYPE_PNG))
                        .addFormDataPart("full_name", fullname)
                        .addFormDataPart("address", address)
                        .addFormDataPart("email", email_id)
                        .build();
            } else {
                jsonObject.put("full_name", fullname);
                jsonObject.put("address", address);
                jsonObject.put("email", email_id);
                 body = RequestBody.create(APIcall.JSON, jsonObject + "");
            }
            String url = AppConstant.GET_USER_UPDATE_PROFILE;
            APIcall apIcall = new APIcall(getApplicationContext());
            apIcall.isPost(true);
            apIcall.setBody(body);
            apIcall.execute(url, APIcall.OPERATION_USER_UPDATE_PROFILE, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                try {
                    Uri selectedImageUri = data.getData();
                    imageUploadpath = getPath(selectedImageUri);
                    File N_file = Common.getCompressed(UserProfileActivity.this, imageUploadpath);
                    imageUploadpath = N_file.getPath();
                    String imageUploadFileName = new File(imageUploadpath).getName();
                    FileName = imageUploadFileName;
                    Filepath = imageUploadpath;
                    Glide.with(UserProfileActivity.this).load(imageUploadpath).centerCrop().placeholder(R.mipmap.image_placeholder_default).into(binding.ivUserProfile);
//                    GetAPICallUploadUserImage(imageUploadFileName, imageUploadpath);
                } catch (Exception e) {
                    Log.d("SUMITPATEL", "EROOR" + e.toString());
                }

            }
        }
    }

    public String getPath(Uri uri) {
        String res = "";
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.managedQuery(uri, projection, null, null, null);
        if (cursor.moveToFirst()) {
            res = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
        }
        return res;
    }

    public void onDestroy() {
        super.onDestroy();
        if (cursor != null) {
            cursor.close();
        }
    }

}