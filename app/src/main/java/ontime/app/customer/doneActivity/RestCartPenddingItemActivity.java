package ontime.app.customer.doneActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;

import ontime.app.R;
import ontime.app.customer.Adapter.RvRestaurantCartPenddingListAdapter;
import ontime.app.databinding.ActivityCartBinding;
import ontime.app.model.usermain.UserCartItem;
import ontime.app.model.usernewmain.ExampleUserItem;
import ontime.app.okhttp.APIcall;
import ontime.app.okhttp.AppConstant;
import ontime.app.rv_interface;
import ontime.app.utils.BaseActivity;
import ontime.app.utils.Common;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;

import static ontime.app.customer.doneActivity.RestaurantProfileActivity.isTakeAway;

public class RestCartPenddingItemActivity extends BaseActivity implements View.OnClickListener, APIcall.ApiCallListner, rv_interface {

    ActivityCartBinding binding;
    private ProgressDialog dialog;
    RvRestaurantCartPenddingListAdapter mAdapter;
    rv_interface anInterface;
    int position = 0;
    int editPosition;
    public ArrayList<UserCartItem> newCartItem = new ArrayList<>();

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        anInterface = RestCartPenddingItemActivity.this;
        setUpUI();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setUpUI() {
        binding.payButtonId.setVisibility(View.VISIBLE);

        binding.txtTitle.setText(getResources().getString(R.string.mycart_title_item));
        if (Common.MERCHANT_TYPE == 1) {
            Common.setSystemBarColor(this, R.color.colorAccent);
//            Common.setSystemBarLight(this);
            binding.txtTitle.setTextColor(getResources().getColor(R.color.colorAccent));
            binding.payButtonId.setBackground(getResources().getDrawable(R.drawable.r_btn_design));

        } else {
            Common.setSystemBarColor(this, R.color.super_mart);
//            Common.setSystemBarLight(this);
            binding.txtTitle.setTextColor(getResources().getColor(R.color.super_mart));
            binding.payButtonId.setBackground(getResources().getDrawable(R.drawable.super_btn_design));
        }
        LinearLayoutManager mLayoutManager1as = new LinearLayoutManager(getContext());
        mLayoutManager1as.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvList.setLayoutManager(mLayoutManager1as);
        binding.txtCount.setText("");
        GetAPICallRestaurantCartList();
    }

    private void GetAPICallRestaurantCartList() {
        if (Common.newCartItem.size() != 0) {
            binding.rvList.setVisibility(View.VISIBLE);
            binding.tvNodata.setVisibility(View.GONE);
            binding.txtCount.setText("(" + String.valueOf(Common.newCartItem.size()) + ")");
            newCartItem.clear();
            newCartItem.addAll(Common.newCartItem);
            mAdapter = new RvRestaurantCartPenddingListAdapter(getContext(), newCartItem, true);
            binding.rvList.setItemAnimator(new DefaultItemAnimator());
            binding.rvList.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(this);
        } else {
            binding.rvList.setVisibility(View.GONE);
            binding.tvNodata.setVisibility(View.VISIBLE);
        }
        mAdapter.setOnClick(new RvRestaurantCartPenddingListAdapter.OnClick() {
            @Override
            public void OnItemClick(View view, int position) {
                editPosition = position;
                Common.UpdateCart = newCartItem.get(position);
                Intent i2 = new Intent(getContext(), RestaurantDetailActivity.class);
                i2.putExtra("MENU_ID", newCartItem.get(position).getItemDetail().getMenuId());
                i2.putExtra("ITEM_ID", newCartItem.get(position).getItemDetail().getId());
                i2.putExtra("CART_ITEM_ID", newCartItem.get(position).getId());
                i2.putExtra("CART_ITEM_SIZE", newCartItem.size());
                i2.putExtra("restaurant_id", newCartItem.get(position).getItemDetail().getRestaurantId());
                i2.putExtra("UPDATE_ITEM", 0);
                startActivityForResult(i2, 34543);
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 34543 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                newCartItem.get(editPosition).setQuantity(Integer.valueOf(data.getStringExtra("Quantity")));
                newCartItem.get(editPosition).setTotalPrice((data.getStringExtra("total")));
                mAdapter.notifyDataSetChanged();
            }

        }
    }

    private void GetAPICallRestCartListItemDelete(int pos) {
        Common.hideKeyboard(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cart_item_id", pos);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_RESTAURANT_DELETE_CART;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_RESTAURANT_DELETE_CART, this);
    }

    protected void setListener() {
        super.setListener();
        binding.back.setOnClickListener(this);
        binding.payButtonId.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                isTakeAway = false;
                break;
            case R.id.payButtonId:
                startActivity(new Intent(this, OrderSummaryActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isTakeAway = false;
//        Intent i=new Intent(this,RestCartPenddingItemActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(i);
//        finish();
    }

    private void showDialog() {
        dialog = new ProgressDialog(RestCartPenddingItemActivity.this);
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
        if (operationCode == APIcall.OPERATION_SUPER_MENU_ITEM_UPDATE_DETAIL) {
            showDialog();
        }
        if (operationCode == APIcall.OPERATION_RESTAURANT_DELETE_CART) {
            showDialog();
        }
        if (operationCode == APIcall.OPERATION_MENU_ITEM_UPDATE_DETAIL) {
            showDialog();
        }

    }

    @Override
    public void OnItemClick(Integer id, int item_id) {
        position = id;
        GetAPICallRestCartListItemDelete(item_id);
    }

    @Override
    public void OnUpDateItemClick(UserCartItem item_id) {
        if(Common.MERCHANT_TYPE == 1){
            GetAPICallRestaurantItemUPDATEAddtoCart(item_id);
        }else {
            GetAPICallSuperMartItemUPDATEAddtoCart(item_id);
        }

    }

    private void GetAPICallRestaurantItemUPDATEAddtoCart(UserCartItem item_id) {
        Common.hideKeyboard(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cart_item_id", item_id.getId());
            if (item_id.getAdditionId() != null) {
                jsonObject.put("addition_id", item_id.getAdditionId().getId());
            } else {
                jsonObject.put("addition_id", 0);
            }

            if (item_id.getSizeId() != null) {
                jsonObject.put("size_id", item_id.getSizeId().getId());
            } else {
                jsonObject.put("size_id", 0);
            }

            if (item_id.getRemovalId() != null) {
                jsonObject.put("removal_id", item_id.getRemovalId().getId());
            } else {
                jsonObject.put("removal_id", 0);
            }
            jsonObject.put("quantity", item_id.getQuantity());

            jsonObject.put("note", item_id.getItemDetail().getNote());

            double total_price = item_id.getQuantity() * Double.parseDouble(item_id.getUnitPrice());

            item_id.setTotalPrice(String.valueOf(total_price));

            jsonObject.put("unit_price", item_id.getUnitPrice());
            jsonObject.put("total_price", String.valueOf(total_price));

            if (item_id.getAdditionId() != null) {
                double addition_price = item_id.getQuantity() * Double.parseDouble(item_id.getAdditionId().getPrice());
                jsonObject.put("addition_price", String.valueOf(addition_price));
            } else {
                jsonObject.put("addition_price", 0.00);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_RESTAURANT_MENU_ITEM_UPDATE_DETAIL;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_MENU_ITEM_UPDATE_DETAIL, this);
    }

    @Override
    public void onProgress(int operationCode, int progress) {

    }

    @Override
    public void onSuccess(int operationCode, String response, Object customData) {
        try {
            if (operationCode == APIcall.OPERATION_RESTAURANT_DELETE_CART) {
                hideDialog();
                try {
                    JSONObject json_data = new JSONObject(response);
                    if (json_data.getInt("status") == 200) {
                        Toast.makeText(this, json_data.getString("message"), Toast.LENGTH_SHORT).show();
                        newCartItem.remove(position);
                        Common.newCartItem.remove(position);
                        mAdapter.notifyDataSetChanged();
                        binding.txtCount.setText("(" + String.valueOf(newCartItem.size()) + ")");
                    } else {
                        Toast.makeText(this, json_data.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (operationCode == APIcall.OPERATION_MENU_ITEM_UPDATE_DETAIL) {
                hideDialog();
                JSONObject json_data = new JSONObject(response);
                if (json_data.getInt("status") == 200) {
                    Toast.makeText(this, json_data.getString("message"), Toast.LENGTH_SHORT).show();
                    mAdapter.notifyDataSetChanged();
                    GetAPICallRestaurantCartList();
                } else {
                    Toast.makeText(this, json_data.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }
            if (operationCode == APIcall.OPERATION_SUPER_MENU_ITEM_UPDATE_DETAIL) {
                hideDialog();
                Gson gson = new Gson();
                ExampleUserItem exampleUser = gson.fromJson(response, ExampleUserItem.class);
                if (exampleUser.getStatus() == 200) {
                    mAdapter.notifyDataSetChanged();
                    GetAPICallRestaurantCartList();
                    Toast.makeText(this, "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {

        }


    }

    @Override
    public void onFail(int operationCode, String response) {

    }
    private void GetAPICallSuperMartItemUPDATEAddtoCart(UserCartItem item_id) {
        Common.hideKeyboard(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cart_item_id", item_id.getId());
            jsonObject.put("quantity", item_id.getQuantity());
            jsonObject.put("unit_price", item_id.getUnitPrice());
            double total_price = item_id.getQuantity() * Double.parseDouble(item_id.getUnitPrice());
            item_id.setTotalPrice(String.valueOf(total_price));
            jsonObject.put("total_price", String.valueOf(total_price));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_SUPER_MENU_ITEM_UPDATE_DETAIL;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_SUPER_MENU_ITEM_UPDATE_DETAIL, this);

    }

}