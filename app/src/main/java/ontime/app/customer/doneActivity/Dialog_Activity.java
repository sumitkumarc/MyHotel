package ontime.app.customer.doneActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
import ontime.app.R;
import ontime.app.model.userorder.UserOrderList;
import ontime.app.okhttp.APIcall;
import ontime.app.okhttp.AppConstant;
import ontime.app.utils.Common;
import ontime.app.utils.LanguageManager;

public class Dialog_Activity extends AppCompatActivity implements View.OnClickListener, APIcall.ApiCallListner {
    String str;
    private Dialog customdialog;
    private ProgressDialog dialog;
    int REST_ID = 0;
    int ORDER_ID = 0;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageManager.setLocale(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        MyOrdersListActivity.dialogm.dismiss();
        str = getIntent().getStringExtra("YES");

        if (str.equals("yes")) {

            setContentView(R.layout.order_receivepopup);
            CardView cv_card = findViewById(R.id.cv_card);
            if (Common.MERCHANT_TYPE == 1) {
                cv_card.setBackgroundColor(getResources().getColor(R.color.red));
            } else {
                cv_card.setBackgroundColor(getResources().getColor(R.color.yellow));
//            DrawableCompat.setTint(drawable, ContextCompat.getColor(mContext, R.color.super_mart));
            }
            REST_ID = getIntent().getIntExtra("REST_ID", 0);
            ORDER_ID = getIntent().getIntExtra("ORDER_ID", 0);
        } else {
            setContentView(R.layout.error_msg_dialog);
        }

        if (str.equals("yes")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    openratedialog(Dialog_Activity.this);
                }
            }, 2000);
        }
    }

    private void openratedialog(Context context) {
        customdialog = new Dialog(this);
        customdialog.setCancelable(false);
        customdialog.setContentView(R.layout.rate_app);
        WindowManager.LayoutParams layoutParams = customdialog.getWindow().getAttributes();
        TextView txt_submit = (TextView) customdialog.findViewById(R.id.txt_submit);
        TextView txt_msg = (TextView) customdialog.findViewById(R.id.txt_msg);
        RatingBar rb_rate_review = (RatingBar) customdialog.findViewById(R.id.rb_rate_review);
        RatingBar rb_rate_review1 = (RatingBar) customdialog.findViewById(R.id.rb_rate_review_1);
        EditText ed_review_msg = (EditText) customdialog.findViewById(R.id.ed_review_msg);
        EditText ed_app_comment = (EditText) customdialog.findViewById(R.id.ed_app_comment);
        LinearLayout ll_backImage = (LinearLayout) customdialog.findViewById(R.id.ll_backImage);
        TextView rateapp = (TextView) customdialog.findViewById(R.id.rateapp);
        if (Common.MERCHANT_TYPE == 1) {
            txt_msg.setTextColor(getResources().getColor(R.color.red));
            rateapp.setTextColor(getResources().getColor(R.color.red));
            txt_msg.setText(getResources().getString(R.string.rate_dialogue_msg) + getResources().getString(R.string.restaurants));
            txt_submit.setBackground(getResources().getDrawable(R.drawable.btn_golden));

            Drawable drawable = rb_rate_review.getProgressDrawable();
            drawable.setColorFilter(Color.parseColor("#FF0015"), PorterDuff.Mode.SRC_ATOP);
        } else {
            txt_msg.setTextColor(getResources().getColor(R.color.yellow));
            rateapp.setTextColor(getResources().getColor(R.color.yellow));
            txt_msg.setText(getResources().getString(R.string.rate_dialogue_msg) + getResources().getString(R.string.supermarkets));
            txt_submit.setBackground(getResources().getDrawable(R.drawable.btn_kesari));
        }
        ll_backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customdialog.dismiss();
                Intent intent = new Intent(Dialog_Activity.this, UserDashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String StrRateReview = String.valueOf(rb_rate_review.getRating());
                String StrRateReview1 = String.valueOf(rb_rate_review1.getRating());
                if (rb_rate_review.getRating() == 0) {
                    Toast.makeText(context, getResources().getString(R.string.please_enter_rating), Toast.LENGTH_SHORT).show();
                } else if (ed_review_msg.getText().toString().equals("")) {
                    Toast.makeText(context, getResources().getString(R.string.please_enter_review), Toast.LENGTH_SHORT).show();
                } else {
                    GetAPICallRateuser(StrRateReview, StrRateReview1, ed_review_msg.getText().toString(),ed_app_comment.getText().toString());
                }
            }
        });

        rateapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

//        rb_rate_review1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
//                try {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
//                } catch (android.content.ActivityNotFoundException anfe) {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
//                }
//            }
//        });

        customdialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        customdialog.getWindow().setAttributes(layoutParams);
        customdialog.show();
    }

    private void GetAPICallRateuser(String strRateReview, String strRateReview1, String msg,String msg1) {
        Common.hideKeyboard(this);
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("rate", strRateReview);
            jsonObject.put("app_rating", strRateReview1);
            jsonObject.put("app_comment", msg1);
            jsonObject.put("review", msg);
            jsonObject.put("merchant_id", REST_ID);
            jsonObject.put("rate_type", 1);
            jsonObject.put("order_id", ORDER_ID);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_USER_RATE;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_USER_RATE_US, Dialog_Activity.this);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(Dialog_Activity.this, MyOrdersListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }

    private void showDialog() {
        dialog = new ProgressDialog(Dialog_Activity.this);
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
        if (operationCode == APIcall.OPERATION_USER_RATE_US) {
            showDialog();
        }
    }

    @Override
    public void onProgress(int operationCode, int progress) {

    }

    @Override
    public void onSuccess(int operationCode, String response, Object customData) {
        if (operationCode == APIcall.OPERATION_USER_RATE_US) {
            hideDialog();
            try {
                hideDialog();
                JSONObject root = null;
                try {
                    root = new JSONObject(response);
                    Toast.makeText(Dialog_Activity.this, "" + root.getString("message"), Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                customdialog.dismiss();
                Intent intent = new Intent(Dialog_Activity.this, MyOrdersListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } catch (Exception e) {
                Log.d("SUMITPATEL", "MAINRL" + e.getMessage());
            }
        }
    }

    @Override
    public void onFail(int operationCode, String response) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
