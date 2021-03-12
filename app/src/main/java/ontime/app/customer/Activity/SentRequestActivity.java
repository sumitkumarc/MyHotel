package ontime.app.customer.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import ontime.app.R;
import ontime.app.customer.doneActivity.RestCartItemActivity;
import ontime.app.customer.doneActivity.UserDashboardActivity;
import ontime.app.databinding.ActivitySentrequestBinding;
import ontime.app.utils.BaseActivity;
import ontime.app.utils.Common;

public class SentRequestActivity extends BaseActivity implements View.OnClickListener {
    ActivitySentrequestBinding binding;

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sentrequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (Common.MERCHANT_TYPE == 1) {
            Common.setSystemBarColor(this, R.color.colorAccent);
//            Common.setSystemBarLight(this);
            binding.ivRight.setColorFilter(getResources().getColor(R.color.colorAccent));
            binding.txtMsg.setTextColor(getResources().getColor(R.color.colorAccent));
            binding.btGotoCart.setBackground(getResources().getDrawable(R.drawable.btn_goldenrect));
            binding.txtMsg.setText(getResources().getString(R.string.request_sent_rest));
        } else {
            Common.setSystemBarColor(this, R.color.super_mart);
//            Common.setSystemBarLight(this);
            binding.ivRight.setColorFilter(getResources().getColor(R.color.super_mart));
            binding.txtMsg.setTextColor(getResources().getColor(R.color.super_mart));
            binding.btGotoCart.setBackground(getResources().getDrawable(R.drawable.btn_goldenrectorange));
            binding.txtMsg.setText(getResources().getString(R.string.request_sent_supermart));
        }


    }

    @Override
    protected void setListener() {
        super.setListener();
        binding.back.setOnClickListener(this);
        binding.btGotoCart.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
            case R.id.bt_goto_cart:
                Intent intenta = new Intent(SentRequestActivity.this, RestCartItemActivity.class);
               // intenta.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intenta);
                finish();
                break;
        }
    }

}