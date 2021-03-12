package ontime.app.restaurant.ui.Activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Locale;

import ontime.app.R;
import ontime.app.customer.doneActivity.LanguageActivity;
import ontime.app.customer.doneActivity.UserDashboardActivity;
import ontime.app.databinding.RActivitySplashBinding;
import ontime.app.model.usermain.Userdate;
import ontime.app.okhttp.SharedPreferenceManagerFile;
import ontime.app.utils.BaseActivity;
import ontime.app.utils.LanguageManager;
import ontime.app.utils.SessionManager;

public class SplashActivity extends BaseActivity {
    RActivitySplashBinding binding;
    Userdate userData;
    SessionManager sessionManager;
    private SharedPreferenceManagerFile sharedPref;


    protected void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.r_activity_splash);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new SharedPreferenceManagerFile(SplashActivity.this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sessionManager = new SessionManager(SplashActivity.this);
        userData = sessionManager.getUserDetails();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    if (sessionManager.getBooleanData(SessionManager.LOGIN)){
                        if(userData.getUserType().equals("user")){
                            if(!sharedPref.getBooleanSharedPreference(SplashActivity.this,LanguageManager.LANGUAGE_CHANGE)){
                                LanguageManager.setNewLocale(SplashActivity.this, Locale.getDefault().getLanguage());
                                sharedPref.setStringSharedPreference(SplashActivity.this, LanguageManager.LANGUAGE_KEY, Locale.getDefault().getLanguage());
                            }
                            Intent i = new Intent(SplashActivity.this, UserDashboardActivity.class);
                            ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this);
                            startActivity(i,transitionActivityOptions.toBundle());
                            finish();

                        }else {
                            LanguageManager.setNewLocale(SplashActivity.this, Locale.getDefault().getLanguage());
                            sharedPref.setStringSharedPreference(SplashActivity.this, LanguageManager.LANGUAGE_KEY, Locale.getDefault().getLanguage());
                            Intent i = new Intent(SplashActivity.this, RiderOrderDetails.class);
                            ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this);
                            startActivity(i,transitionActivityOptions.toBundle());
                            finish();
                        }
                    }else {
                        Resources.getSystem().getConfiguration().locale.getLanguage();
                        LanguageManager.setNewLocale(SplashActivity.this, Resources.getSystem().getConfiguration().locale.getLanguage());
                        sharedPref.setStringSharedPreference(SplashActivity.this, LanguageManager.LANGUAGE_KEY, Resources.getSystem().getConfiguration().locale.getLanguage());
                        Intent i = new Intent(SplashActivity.this, WelcomeActivity.class);
                        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this);
                        startActivity(i,transitionActivityOptions.toBundle());
                        finish();
                    }

                }
                finish();
            }
        },2000);
    }
}