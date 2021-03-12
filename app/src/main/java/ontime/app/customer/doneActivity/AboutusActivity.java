package ontime.app.customer.doneActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import ontime.app.R;
import ontime.app.databinding.ActivityAboutusBinding;
import ontime.app.model.usermain.ExampleUser;
import ontime.app.okhttp.APIcall;
import ontime.app.okhttp.AppConstant;
import ontime.app.utils.BaseActivity;
import ontime.app.utils.Common;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;

public class AboutusActivity extends BaseActivity implements View.OnClickListener, APIcall.ApiCallListner {

    ActivityAboutusBinding binding;
    private ProgressDialog dialog;

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_aboutus);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Common.MERCHANT_TYPE == 1) {
            Common.setSystemBarColor(this, R.color.colorAccent);
//            Common.setSystemBarLight(this);
            binding.ivLogo.setColorFilter(getResources().getColor(R.color.colorAccent));
            binding.txtTitle.setTextColor(getResources().getColor(R.color.colorAccent));

        } else {
            Common.setSystemBarColor(this, R.color.super_mart);
//            Common.setSystemBarLight(this);
            binding.ivLogo.setColorFilter(getResources().getColor(R.color.super_mart));
            binding.txtTitle.setTextColor(getResources().getColor(R.color.super_mart));
        }
        GetAPICallAboutUs();

    }

    @Override
    protected void setListener() {
        super.setListener();
        binding.back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;

        }
    }

    private void showDialog() {
        dialog = new ProgressDialog(AboutusActivity.this);
        dialog.setMessage(getResources().getString(R.string.Please_wait));
        dialog.setCancelable(false);
        dialog.show();
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    private void GetAPICallAboutUs() {
        Common.hideKeyboard(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_ABOUT_US;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_ABOUT_US, this);
    }

    @Override
    public void onStartLoading(int operationCode) {
        if (operationCode == APIcall.OPERATION_ABOUT_US) {
            showDialog();
        }

    }

    @Override
    public void onProgress(int operationCode, int progress) {

    }

    @Override
    public void onSuccess(int operationCode, String response, Object customData) {
        try {
            if (operationCode == APIcall.OPERATION_ABOUT_US) {
                hideDialog();
                Gson gson = new Gson();
                ExampleUser exampleUser = gson.fromJson(response, ExampleUser.class);
                if (exampleUser.getResponceData().getContent() != null) {
                    binding.txtTitle.setText((exampleUser.getResponceData().getContent().getTitle()));
                    binding.txtAboutUs.setText(exampleUser.getResponceData().getContent().getDescription());
                } else {
                    Toast.makeText(AboutusActivity.this, "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            hideDialog();
        } catch (Exception e) {

        }

    }

    @Override
    public void onFail(int operationCode, String response) {

    }

}