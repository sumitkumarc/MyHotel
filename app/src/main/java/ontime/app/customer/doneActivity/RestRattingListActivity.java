package ontime.app.customer.doneActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
import ontime.app.R;
import ontime.app.customer.Adapter.RvRestRatingListAdapter;
import ontime.app.databinding.ActivityRestRattingListBinding;
import ontime.app.model.usermain.ExampleUser;
import ontime.app.okhttp.APIcall;
import ontime.app.okhttp.AppConstant;
import ontime.app.utils.BaseActivity;
import ontime.app.utils.Common;

public class RestRattingListActivity extends BaseActivity implements View.OnClickListener, APIcall.ApiCallListner {
    ActivityRestRattingListBinding binding;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isConnected()) {
            LinearLayoutManager rv_proManager = new LinearLayoutManager(this);
            rv_proManager.setOrientation(LinearLayoutManager.VERTICAL);
            binding.rvFilterList.setLayoutManager(rv_proManager);
            binding.rvFilterList.setItemAnimator(new DefaultItemAnimator());
            binding.rvFilterList.setVisibility(View.GONE);
            binding.txtNoItem.setVisibility(View.GONE);

            if (Common.MERCHANT_TYPE == 1) {
                Common.setSystemBarColor(this, R.color.colorAccent);
                binding.txtResName.setTextColor(getResources().getColor(R.color.colorAccent));
                binding.ivBackArrow.setColorFilter(getResources().getColor(R.color.colorAccent));
                binding.ivRestProfileImg.setBorderColor(getResources().getColor(R.color.colorAccent));
                Drawable drawable = binding.rbRatingbar.getProgressDrawable();
                drawable.setColorFilter(Color.parseColor("#FF0015"), PorterDuff.Mode.SRC_ATOP);
            } else {
                Common.setSystemBarColor(this, R.color.super_mart);
                binding.txtResName.setTextColor(getResources().getColor(R.color.super_mart));
                binding.ivBackArrow.setColorFilter(getResources().getColor(R.color.super_mart));
                binding.ivRestProfileImg.setBorderColor(getResources().getColor(R.color.super_mart));
                Drawable drawable = binding.rbRatingbar.getProgressDrawable();
                drawable.setColorFilter(Color.parseColor("#FE8B00"), PorterDuff.Mode.SRC_IN);
            }
            try {
                Glide.with(getContext()).load(getIntent().getStringExtra("REST_IMAGE")).centerCrop().into(binding.ivRestProfileImg);
                binding.txtResName.setText(Common.isStrempty(getIntent().getStringExtra("REST_NAME")));
                binding.txtResBarnchname.setText(Common.isStrempty(getIntent().getStringExtra("REST_NAME_BARNCH")));
                binding.rbRatingbar.setRating((float) Float.parseFloat(Common.isStrempty(getIntent().getStringExtra("REST_RATING"))));
            } catch (Exception e) {
            }
            GetAPICallRestaurantReviewList();
        } else {

        }


    }

    @Override
    protected void setListener() {
        super.setListener();
        binding.llBack.setOnClickListener(this);
    }

    private void showDialog() {
        dialog = new ProgressDialog(RestRattingListActivity.this);
        dialog.setMessage(getResources().getString(R.string.Please_wait));
        dialog.setCancelable(false);
        dialog.show();
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    private void GetAPICallRestaurantReviewList() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("merchant_id", Common.RESTAURANT_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_USER_RESTAURNT_RATING;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_USER_RESTAURNT_RATING, this);
    }

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rest_ratting_list);
    }


    @Override
    public void onStartLoading(int operationCode) {
        if (operationCode == APIcall.OPERATION_USER_RESTAURNT_RATING) {
            showDialog();
        }
    }

    @Override
    public void onProgress(int operationCode, int progress) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSuccess(int operationCode, String response, Object customData) {
        try {
            hideDialog();
            if (operationCode == APIcall.OPERATION_USER_RESTAURNT_RATING) {
                Gson gson = new Gson();
                ExampleUser example = gson.fromJson(response, ExampleUser.class);
                if (example.getResponceData().getReview().getData().size() != 0) {
                    binding.rvFilterList.setVisibility(View.VISIBLE);
                    binding.txtNoItem.setVisibility(View.GONE);
                    binding.txtCount.setText("(" + String.valueOf(example.getResponceData().getReview().getData().size()) + ")");
                    RvRestRatingListAdapter adapter = new RvRestRatingListAdapter(RestRattingListActivity.this, example.getResponceData().getReview().getData());
                    binding.rvFilterList.setAdapter(adapter);

                } else {
                    binding.txtNoItem.setVisibility(View.VISIBLE);
                    binding.rvFilterList.setVisibility(View.GONE);
                }
            }
            hideDialog();
        } catch (Exception e) {
            binding.txtNoItem.setVisibility(View.VISIBLE);
            binding.rvFilterList.setVisibility(View.GONE);
            hideDialog();
        }
    }

    @Override
    public void onFail(int operationCode, String response) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                onBackPressed();
                break;

        }
    }
}