package ontime.app.customer.doneActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import ontime.app.R;
import ontime.app.customer.Adapter.RvRestaurantCartListAdapter;
import ontime.app.databinding.ActivityCartBinding;
import ontime.app.model.usermain.ExampleUser;
import ontime.app.model.usermain.UserCart;
import ontime.app.model.usermain.UserCartItem;
import ontime.app.okhttp.APIcall;
import ontime.app.okhttp.AppConstant;
import ontime.app.rv_interface;
import ontime.app.utils.BaseActivity;
import ontime.app.utils.Common;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

//import company.tap.gosellapi.GoSellSDK;
//import company.tap.gosellapi.internal.api.callbacks.GoSellError;
//import company.tap.gosellapi.internal.api.models.Authorize;
//import company.tap.gosellapi.internal.api.models.Charge;
//import company.tap.gosellapi.internal.api.models.PhoneNumber;
//import company.tap.gosellapi.internal.api.models.SaveCard;
//import company.tap.gosellapi.internal.api.models.SavedCard;
//import company.tap.gosellapi.internal.api.models.Token;
//import company.tap.gosellapi.open.buttons.PayButtonView;
//import company.tap.gosellapi.open.controllers.SDKSession;
//import company.tap.gosellapi.open.controllers.ThemeObject;
//import company.tap.gosellapi.open.delegate.SessionDelegate;
//import company.tap.gosellapi.open.enums.AppearanceMode;
//import company.tap.gosellapi.open.enums.TransactionMode;
//import company.tap.gosellapi.open.models.CardsList;
//import company.tap.gosellapi.open.models.Customer;
//import company.tap.gosellapi.open.models.TapCurrency;
//import company.tap.gosellapi.open.models.PaymentItem;
//import company.tap.gosellapi.open.models.Tax;
import okhttp3.RequestBody;

public class RestCartItemActivity extends BaseActivity implements View.OnClickListener, APIcall.ApiCallListner, rv_interface {

    ActivityCartBinding binding;
    private ProgressDialog dialog;
    RvRestaurantCartListAdapter mAdapter;
    rv_interface anInterface;
    private final int SDK_REQUEST_CODE = 1001;

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        anInterface = RestCartItemActivity.this;
        setUpUI();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setUpUI() {
        binding.payButtonId.setVisibility(View.GONE);

        if (Common.MERCHANT_TYPE == 1) {
            Common.setSystemBarColor(this, R.color.colorAccent);
//            Common.setSystemBarLight(this);
            binding.txtTitle.setTextColor(getResources().getColor(R.color.colorAccent));
//            binding.payButtonId.setBackground(getResources().getDrawable(R.drawable.r_btn_design));

        } else {
            Common.setSystemBarColor(this, R.color.super_mart);
//            Common.setSystemBarLight(this);
            binding.txtTitle.setTextColor(getResources().getColor(R.color.super_mart));
//            binding.payButtonId.setBackground(getResources().getDrawable(R.drawable.super_btn_design));
        }
        LinearLayoutManager mLayoutManager1as = new LinearLayoutManager(getContext());
        mLayoutManager1as.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvList.setLayoutManager(mLayoutManager1as);
        binding.txtCount.setText("");
        GetAPICallRestaurantCartList();
    }


    private void GetAPICallRestaurantCartList() {
        Common.hideKeyboard(getActivity());
        String url = AppConstant.GET_RESTAURANT_MY_CART;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(false);
        apIcall.execute(url, APIcall.OPERATION_RESTAURANT_MY_CART, this);
    }

    private void GetAPICallRestaurantsDelete(int pos) {
        Common.hideKeyboard(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cart_id", pos);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_RESTAURANT_DELETE;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_RESTAURANT_DELETE, this);
    }

    protected void setListener() {
        super.setListener();
        binding.back.setOnClickListener(this);
//        binding.payButtonId.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
//            case R.id.payButtonId:
//                initialiseSDK(40.0, "");
//                configureSDKMode();
//                break;
            default:
                break;
        }
    }

    private void showDialog() {
        dialog = new ProgressDialog(RestCartItemActivity.this);
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
        if (operationCode == APIcall.OPERATION_RESTAURANT_MY_CART) {
            showDialog();
        }
        if (operationCode == APIcall.OPERATION_RESTAURANT_DELETE) {
            showDialog();
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(this,UserDashboardActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

    @Override
    public void onProgress(int operationCode, int progress) {

    }

    @Override
    public void onSuccess(int operationCode, String response, Object customData) {
        try {
            if (operationCode == APIcall.OPERATION_RESTAURANT_MY_CART) {
                hideDialog();
                Gson gson = new Gson();
                ExampleUser exampleUser = gson.fromJson(response, ExampleUser.class);
                if (exampleUser.getStatus() == 200) {
                    List<UserCart> cartList = exampleUser.getResponceData().getCart();
                    if (cartList.size() != 0) {
                        binding.rvList.setVisibility(View.VISIBLE);
                        binding.txtCount.setText("(" + String.valueOf(cartList.size()) + ")");
                        binding.tvNodata.setVisibility(View.GONE);
                        mAdapter = new RvRestaurantCartListAdapter(getContext(), cartList);
                        binding.rvList.setItemAnimator(new DefaultItemAnimator());
                        binding.rvList.setAdapter(mAdapter);
                        mAdapter.setOnItemClickListener(this);
                    } else {
                        binding.rvList.setVisibility(View.GONE);
                        binding.txtCount.setVisibility(View.GONE);
                        binding.tvNodata.setVisibility(View.VISIBLE);
                    }

                } else {
                }
            }
            if (operationCode == APIcall.OPERATION_RESTAURANT_DELETE) {
                hideDialog();
                try {
                    JSONObject json_data = new JSONObject(response);
                    Toast.makeText(this, json_data.getString("message"), Toast.LENGTH_SHORT).show();
                    GetAPICallRestaurantCartList();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {

        }


    }

    @Override
    public void onFail(int operationCode, String response) {

    }

    @Override
    public void OnItemClick(Integer id, int item_id) {
        GetAPICallRestaurantsDelete(item_id);
    }

    @Override
    public void OnUpDateItemClick(UserCartItem item_id) {

    }

}