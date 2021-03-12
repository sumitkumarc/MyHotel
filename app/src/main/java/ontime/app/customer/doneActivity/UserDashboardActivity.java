package ontime.app.customer.doneActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import okhttp3.RequestBody;
import ontime.app.R;
import ontime.app.databinding.ActivityHomeBinding;
import ontime.app.model.usermain.Userdate;
import ontime.app.okhttp.APIcall;
import ontime.app.okhttp.AppConstant;
import ontime.app.okhttp.SharedPreferenceManagerFile;
import ontime.app.utils.BaseActivity;
import ontime.app.utils.Common;
import ontime.app.utils.LanguageManager;
import ontime.app.utils.SessionManager;

public class UserDashboardActivity extends BaseActivity implements View.OnClickListener, APIcall.ApiCallListner {
    SessionManager sessionManager;
    Userdate userData;
    ActivityHomeBinding binding;
    Locale locale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(UserDashboardActivity.this);
        userData = sessionManager.getUserDetails();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = getResources().getConfiguration().locale;
        }
//        binding.menu.setVisibility(View.VISIBLE);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (locale.getLanguage().equals(LanguageManager.LANGUAGE_KEY_ARABIC)) {
                    binding.menu.setVisibility(View.GONE);
                    binding.menuNew.setVisibility(View.VISIBLE);
                    binding.closeNew.setVisibility(View.VISIBLE);
                    binding.close.setVisibility(View.GONE);
                } else {
                    binding.close.setVisibility(View.VISIBLE);
                    binding.closeNew.setVisibility(View.GONE);
                    binding.menuNew.setVisibility(View.GONE);
                    binding.menu.setVisibility(View.VISIBLE);

                }
            }
        });


        setUpUI();
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        GetGetUpdateToken(task.getResult());

                    }
                });

    }

    private void GetGetUpdateToken(String result) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_token", result);
            jsonObject.put("user_id", userData.getId());
            jsonObject.put("device_type", "android");
            jsonObject.put("user_type", userData.getUserType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_UPDATE_DEVICE_TOKEN;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_UPDATE_DEVICE_TOKEN, this);
    }

    private void setUpUI() {
        binding.AuthorUserName.setText(Common.isStrempty(userData.getFullName()));
        Glide.with(this).load(Common.isStrempty(userData.getImage())).centerCrop().placeholder(R.mipmap.image_placeholder_default).into(binding.AuthorAvatar);
    }

    @Override
    public void onResume() {
        super.onResume();
        userData = sessionManager.getUserDetails();
        setUpUI();
        //will be executed onResume
    }

    @Override
    protected void setListener() {
        super.setListener();
        binding.tvResturant.setOnClickListener(this);
        binding.tvSupermart.setOnClickListener(this);

        binding.close.setOnClickListener(this);
        binding.closeNew.setOnClickListener(this);
        binding.menu.setOnClickListener(this);
        binding.menuNew.setOnClickListener(this);
        binding.llhome.setOnClickListener(this);
        binding.llabout.setOnClickListener(this);
        binding.llangages.setOnClickListener(this);
        binding.llcart.setOnClickListener(this);
        binding.llmyorder.setOnClickListener(this);
        binding.llpaymet.setOnClickListener(this);
        binding.llprofile.setOnClickListener(this);
        binding.llcontact.setOnClickListener(this);
        binding.llnotifi.setOnClickListener(this);
        binding.llChangePws.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
    }

    @Override
    public void onBackPressed() {
        if (this.binding.drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            this.binding.drawerLayout.closeDrawers();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(UserDashboardActivity.this);
            builder.setMessage(getResources().getString(R.string.Are_you_sure_you_want_to_Exit)).setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int id) {
                            finishAffinity();
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

    }

    @SuppressLint({"WrongConstant", "NonConstantResourceId"})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.llhome:
                binding.drawerLayout.closeDrawers();
                break;
            case R.id.tv_resturant:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Intent i2 = new Intent(UserDashboardActivity.this, RestaurantlistActivity.class);
                    i2.putExtra("CAT_TYPE", 1);
                    Common.MERCHANT_TYPE = 1;
                    ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(UserDashboardActivity.this);
                    startActivity(i2, transitionActivityOptions.toBundle());
                    binding.drawerLayout.closeDrawers();
                }
                break;

            case R.id.tv_supermart:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Intent i2 = new Intent(UserDashboardActivity.this, RestaurantlistActivity.class);
                    i2.putExtra("CAT_TYPE", 2);
                    Common.MERCHANT_TYPE = 2;
                    ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(UserDashboardActivity.this);
                    startActivity(i2, transitionActivityOptions.toBundle());
                    binding.drawerLayout.closeDrawers();
                }
                break;
            case R.id.menu:
                binding.drawerLayout.openDrawer(Gravity.RIGHT);
                break;
            case R.id.menuNew:
                binding.drawerLayout.openDrawer(Gravity.RIGHT);
                break;
            case R.id.close:
                binding.drawerLayout.closeDrawers();
                break;
            case R.id.close_new:
                binding.drawerLayout.closeDrawers();
                break;
            case R.id.ll_change_pws:
                startActivity(new Intent(UserDashboardActivity.this, ChangePasswordActivity.class)); //done
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                binding.drawerLayout.closeDrawers();
                break;

            case R.id.llprofile:
                startActivity(new Intent(UserDashboardActivity.this, UserProfileActivity.class));  //done
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                binding.drawerLayout.closeDrawers();
                break;
            case R.id.llcart:
                startActivity(new Intent(UserDashboardActivity.this, RestCartItemActivity.class));  //done
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                binding.drawerLayout.closeDrawers();
                break;
            case R.id.llmyorder:
                startActivity(new Intent(UserDashboardActivity.this, MyOrdersListActivity.class)); //done
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                binding.drawerLayout.closeDrawers();
                break;
            case R.id.llpaymet:
                startActivity(new Intent(UserDashboardActivity.this, PaymentActivity.class));  //done
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                binding.drawerLayout.closeDrawers();
                break;
            case R.id.llnotifi:
                startActivity(new Intent(UserDashboardActivity.this, NotificationActivity.class)); //done
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                binding.drawerLayout.closeDrawers();
                break;
            case R.id.llcontact:
                startActivity(new Intent(UserDashboardActivity.this, ContactusActivity.class));  //done
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                binding.drawerLayout.closeDrawers();
                break;
            case R.id.llabout:
                startActivity(new Intent(UserDashboardActivity.this, AboutusActivity.class));  //done
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                binding.drawerLayout.closeDrawers();
                break;
            case R.id.llangages:
                startActivity(new Intent(UserDashboardActivity.this, LanguageActivity.class));  //done
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                binding.drawerLayout.closeDrawers();
                break;
            default:
                break;
        }
    }

    @Override
    public void onStartLoading(int operationCode) {

    }

    @Override
    public void onProgress(int operationCode, int progress) {

    }

    @Override
    public void onSuccess(int operationCode, String response, Object customData) {
        if (operationCode == APIcall.OPERATION_UPDATE_DEVICE_TOKEN) {
            Gson gson = new Gson();


        }
    }

    @Override
    public void onFail(int operationCode, String response) {

    }


}