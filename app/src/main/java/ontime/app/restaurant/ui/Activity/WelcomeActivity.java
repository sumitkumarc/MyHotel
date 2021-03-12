package ontime.app.restaurant.ui.Activity;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import java.util.Locale;

import ontime.app.R;
import ontime.app.customer.doneActivity.LoginActivity;
import ontime.app.customer.doneActivity.RegistetActivity;
import ontime.app.databinding.RActivityWelcomeBinding;
import ontime.app.okhttp.APIcall;
import ontime.app.okhttp.SharedPreferenceManagerFile;
import ontime.app.utils.BaseActivity;
import ontime.app.utils.Common;
import ontime.app.utils.LanguageManager;

public class WelcomeActivity extends BaseActivity implements View.OnClickListener{
    RActivityWelcomeBinding binding;
 //   SharedPreferenceManagerFile sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
       // sharedPref = new SharedPreferenceManagerFile(WelcomeActivity.this);
        Common.checkAndRequestPermissions(this);


    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    Intent i = new Intent(WelcomeActivity.this, LoginActivity.class);
                    ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(WelcomeActivity.this);
                    startActivity(i,transitionActivityOptions.toBundle());
                }
                break;
            case R.id.bt_create_account:
                Intent i = new Intent(WelcomeActivity.this, RegistetActivity.class);
                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(WelcomeActivity.this);
                startActivity(i,transitionActivityOptions.toBundle());
                break;
            default:
                break;
        }
    }
    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.r_activity_welcome);
    }
    @Override
    protected void setListener() {
        super.setListener();
        binding.loginBtn.setOnClickListener(this);
        binding.btCreateAccount.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);
        builder.setMessage("Are you sure you want to Exit ?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}