package ontime.app.customer.doneActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import ontime.app.R;
import ontime.app.databinding.ActivityLanguageBinding;
import ontime.app.model.usermain.Userdate;
import ontime.app.okhttp.SharedPreferenceManagerFile;
import ontime.app.restaurant.ui.Activity.RiderOrderDetails;
import ontime.app.restaurant.ui.Activity.SplashActivity;
import ontime.app.utils.BaseActivity;
import ontime.app.utils.Common;
import ontime.app.utils.LanguageManager;
import ontime.app.utils.SessionManager;

import java.util.Locale;

public class LanguageActivity extends BaseActivity implements View.OnClickListener {

    ActivityLanguageBinding binding;
    private ProgressDialog dialog;
    private SharedPreferenceManagerFile sharedPref;
    Userdate userData;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new SharedPreferenceManagerFile(LanguageActivity.this);

        sessionManager = new SessionManager(LanguageActivity.this);
        userData = sessionManager.getUserDetails();

        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = getResources().getConfiguration().locale;
        }
        if (Common.MERCHANT_TYPE == 1) {
            Common.setSystemBarColor(this, R.color.colorAccent);
//            Common.setSystemBarLight(this);
            binding.txtArbic.setTextColor(getResources().getColor(R.color.colorAccent));

            if (locale.getLanguage().equals(LanguageManager.LANGUAGE_KEY_ARABIC)) {
                binding.txtArbic.setTextColor(getResources().getColor(R.color.colorAccent));
                binding.txtEnglish.setTextColor(getResources().getColor(R.color.black));
            } else {
                binding.txtArbic.setTextColor(getResources().getColor(R.color.black));
                binding.txtEnglish.setTextColor(getResources().getColor(R.color.colorAccent));
            }

        } else {
            Common.setSystemBarColor(this, R.color.super_mart);
//            Common.setSystemBarLight(this);
            binding.txtArbic.setTextColor(getResources().getColor(R.color.super_mart));
            binding.txtEnglish.setTextColor(getResources().getColor(R.color.super_mart));

            if (locale.getLanguage().equals(LanguageManager.LANGUAGE_KEY_ARABIC)) {
                binding.txtArbic.setTextColor(getResources().getColor(R.color.super_mart));
                binding.txtEnglish.setTextColor(getResources().getColor(R.color.black));
            } else {
                binding.txtArbic.setTextColor(getResources().getColor(R.color.black));
                binding.txtEnglish.setTextColor(getResources().getColor(R.color.super_mart));
            }
        }


    }

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_language);
    }

    @Override
    protected void setListener() {
        super.setListener();
        binding.back.setOnClickListener(this);
        binding.txtArbic.setOnClickListener(this);
        binding.txtEnglish.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.txt_arbic:
                sharedPref.setStringSharedPreference(this, LanguageManager.LANGUAGE_KEY, LanguageManager.LANGUAGE_KEY_ARABIC);
                sharedPref.setBooleanSharedPreference(this, LanguageManager.LANGUAGE_CHANGE,true);
                LanguageManager.setNewLocale(this, LanguageManager.LANGUAGE_KEY_ARABIC);
                if (userData.getUserType().equals("user")) {
                    Intent i = new Intent(LanguageActivity.this, UserDashboardActivity.class);
                    ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(LanguageActivity.this);
                    startActivity(i, transitionActivityOptions.toBundle());
                    finish();
                } else {
                    Intent i = new Intent(LanguageActivity.this, RiderOrderDetails.class);
                    ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(LanguageActivity.this);
                    startActivity(i, transitionActivityOptions.toBundle());
                    finish();
                }
                onBackPressed();
                //finish();
                break;
            case R.id.txt_english:
                sharedPref.setStringSharedPreference(this, LanguageManager.LANGUAGE_KEY, LanguageManager.LANGUAGE_KEY_ENGLISH);
                sharedPref.setBooleanSharedPreference(this, LanguageManager.LANGUAGE_CHANGE,true);
                LanguageManager.setNewLocale(this, LanguageManager.LANGUAGE_KEY_ENGLISH);
                onBackPressed();
                if (userData.getUserType().equals("user")) {
                    Intent i = new Intent(LanguageActivity.this, UserDashboardActivity.class);
                    ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(LanguageActivity.this);
                    startActivity(i, transitionActivityOptions.toBundle());
                    finish();
                } else {
                    Intent i = new Intent(LanguageActivity.this, RiderOrderDetails.class);
                    ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(LanguageActivity.this);
                    startActivity(i, transitionActivityOptions.toBundle());
                    finish();
                }
                // finish();
                break;

        }
    }

}