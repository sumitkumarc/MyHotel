package ontime.app.customer.doneActivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

import okhttp3.RequestBody;
import ontime.app.R;
import ontime.app.databinding.ActivityRestdetailBinding;
import ontime.app.model.usermain.ExampleUser;
import ontime.app.model.usermain.RestMenuItemAdditionDetail;
import ontime.app.model.usermain.RestMenuItemRemovalDetail;
import ontime.app.model.usermain.RestMenuItemSizeDetail;
import ontime.app.model.usermain.UserCartItem;
import ontime.app.model.usernewmain.ExampleUserItem;
import ontime.app.okhttp.APIcall;
import ontime.app.okhttp.AppConstant;
import ontime.app.utils.BaseActivity;
import ontime.app.utils.Common;

import static ontime.app.okhttp.APIcall.OPERATION_ADD_TO_CART;
import static ontime.app.utils.Common.DELIVER_TYPE;

public class RestaurantDetailActivity extends BaseActivity implements View.OnClickListener, APIcall.ApiCallListner {

    ActivityRestdetailBinding binding;
    private ProgressDialog dialog;
    int addition_id = 0;
    double addition_price = 0;
    int size_id = 0;
    double size_price = 0;
    int removal_id = 0;
    double removal_price = 0;
    int UPDATE_ITEM = 0;
    double unit_price = 0;
    int restaurant_id = 0;
    int CART_ITEM_ID = 0;
    int CART_ITEM_SIZE = 0;
    int CART_ITEM_POSITION = 0;
    int Counter = 1;
    double total_int = 0;
    double finaltotal = 0;
    int MENU_ID = 1;
    int ITEM_ID = 1;
    UserCartItem userCartItem;


    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_restdetail);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // userCartItem = (UserCartItem) getIntent().getSerializableExtra("USER_CART_ITEM");
        MENU_ID = getIntent().getIntExtra("MENU_ID", 0);
        ITEM_ID = getIntent().getIntExtra("ITEM_ID", 0);
        CART_ITEM_ID = getIntent().getIntExtra("CART_ITEM_ID", 0);
        CART_ITEM_SIZE = getIntent().getIntExtra("CART_ITEM_SIZE", 0);
        CART_ITEM_POSITION = getIntent().getIntExtra("CART_ITEM_POSITION", 0);
        restaurant_id = getIntent().getIntExtra("restaurant_id", 0);
        UPDATE_ITEM = getIntent().getIntExtra("UPDATE_ITEM", 0);
        setUpUI();
    }

    private void setUpUI() {

       /* binding.edNotes.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                //do your stuff here..
                binding.edNotes.setText("");
                return false;
            }
        });*/

        if (Common.MERCHANT_TYPE == 1) {
            binding.llDetails.setVisibility(View.VISIBLE);
            Common.setSystemBarColor(this, R.color.colorAccent);
            binding.btAddtocart.setBackground(getResources().getDrawable(R.drawable.btn_golden));
            binding.txtProName.setTextColor(getResources().getColor(R.color.colorAccent));
            binding.txtProCalories.setTextColor(getResources().getColor(R.color.colorAccent));
            binding.txtProSize.setTextColor(getResources().getColor(R.color.colorAccent));
            binding.txtAddition.setTextColor(getResources().getColor(R.color.colorAccent));
            binding.txtProRemoval.setTextColor(getResources().getColor(R.color.colorAccent));
            binding.txtProPrice.setTextColor(getResources().getColor(R.color.colorAccent));
            binding.txtQty.setTextColor(getResources().getColor(R.color.colorAccent));
            binding.txtCount.setBackground(getResources().getDrawable(R.drawable.circle));
        } else {
            binding.llDetails.setVisibility(View.GONE);
            Common.setSystemBarColor(this, R.color.super_mart);
            binding.ivAddtocart.setImageDrawable(getResources().getDrawable(R.drawable.oragecartwthcircle));
            binding.btAddtocart.setBackground(getResources().getDrawable(R.drawable.btn_kesari));
            binding.txtProName.setTextColor(getResources().getColor(R.color.super_mart));
            binding.txtProCalories.setTextColor(getResources().getColor(R.color.super_mart));
            binding.txtProSize.setTextColor(getResources().getColor(R.color.super_mart));
            binding.txtAddition.setTextColor(getResources().getColor(R.color.super_mart));
            binding.txtProRemoval.setTextColor(getResources().getColor(R.color.super_mart));
            binding.txtProPrice.setTextColor(getResources().getColor(R.color.super_mart));
            binding.txtQty.setTextColor(getResources().getColor(R.color.super_mart));
            binding.txtCount.setBackground(getResources().getDrawable(R.drawable.circle_golden));

        }
        LinearLayoutManager mLayoutManager1as = new LinearLayoutManager(getContext());
        mLayoutManager1as.setOrientation(LinearLayoutManager.VERTICAL);
        binding.llMain.setVisibility(View.GONE);
        GetAPICallRestaurantItemDetails(MENU_ID, ITEM_ID);
        if (UPDATE_ITEM == 0) {
            binding.btAddtocart.setText(getResources().getString(R.string.update_Cart));
            Counter = Common.UpdateCart.getQuantity();
            CART_ITEM_POSITION = 0;
            unit_price = Double.parseDouble(Common.UpdateCart.getUnitPrice());
//            showTotal();
        } else {
            binding.btAddtocart.setText(getResources().getString(R.string.Add_to_Cart));
            Counter = 1;
        }

//        binding.edNotes.setEnabled(false);
        binding.txtQty.setText(String.valueOf(Counter));

        if (CART_ITEM_SIZE == 0) {
            binding.txtCount.setVisibility(View.GONE);
            binding.txtCount.setText(String.valueOf(CART_ITEM_SIZE));
        } else {
            binding.txtCount.setVisibility(View.VISIBLE);
            binding.txtCount.setText(String.valueOf(CART_ITEM_SIZE));
        }

        DecimalFormat form = new DecimalFormat("0.00");
        binding.txtProPrice.setText("SR " + String.valueOf(finaltotal));
    }

    private void showDialog() {
        dialog = new ProgressDialog(RestaurantDetailActivity.this);
        dialog.setMessage(getResources().getString(R.string.Please_wait));
        dialog.setCancelable(false);
        dialog.show();
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }


    @Override
    protected void setListener() {
        super.setListener();
        binding.flAddtocart.setOnClickListener(this);
        binding.back.setOnClickListener(this);
        binding.ivAdd.setOnClickListener(this);
        binding.ivSub.setOnClickListener(this);
        binding.btAddtocart.setOnClickListener(this);
        binding.edNotes.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ed_notes:
                // binding.edNotes.setEnabled(true);
                // binding.edNotes.setFocusable(false);
                 binding.edNotes.setText("");
                break;
            case R.id.bt_addtocart:
                if (Common.MERCHANT_TYPE == 1) {
                    if (size_id == 0) {
                        Toast.makeText(this, getResources().getString(R.string.size_is_required), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (UPDATE_ITEM == 0) {
                        GetAPICallRestaurantItemUPDATEAddtoCart(addition_id, size_id, removal_id, unit_price, restaurant_id, MENU_ID, ITEM_ID);
                    } else {
                        GetAPICallRestaurantItemAddtoCart(addition_id, size_id, removal_id, size_price, restaurant_id, MENU_ID, ITEM_ID);
                    }
                } else {
                    if (UPDATE_ITEM == 0) {
                        GetAPICallSuperMartItemUPDATEAddtoCart(unit_price);
                    } else {
                        GetAPICallSuperMartItemAddtoCart(unit_price, restaurant_id, MENU_ID, ITEM_ID);
                    }
                }
                break;
            case R.id.back:
                onBackPressed();
                break;
            case R.id.iv_add:
                Counter++;
                binding.txtQty.setText(String.valueOf(Counter));
                showTotal();
                break;
            case R.id.iv_sub:
                if (Counter > 1) {
                    Counter--;
                    binding.txtQty.setText(String.valueOf(Counter));
                    showTotal();

                }
                break;
            case R.id.fl_addtocart:
                if (CART_ITEM_SIZE == 0) {
                    Toast.makeText(this, getResources().getString(R.string.no_item_cart), Toast.LENGTH_SHORT).show();
                } else {
                    Intent i2s = new Intent(RestaurantDetailActivity.this, RestCartItemActivity.class);
                    startActivity(i2s);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }

                break;
        }
    }

    private void GetAPICallSuperMartItemAddtoCart(double unit_price, int restaurant_id, int menu_id, int item_id) {
        Common.hideKeyboard(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("menu_id", menu_id);
            jsonObject.put("item_id", item_id);
            jsonObject.put("restaurant_id", Common.RESTAURANT_ID);
            jsonObject.put("quantity", Counter);
            jsonObject.put("unit_price", unit_price);
            jsonObject.put("total_price", finaltotal);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_ADD_TO_CART_SUPER;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_ADD_TO_CART_SUPER, this);
    }

    private void GetAPICallSuperMartItemUPDATEAddtoCart(double unit_price) {
        Common.hideKeyboard(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cart_item_id", CART_ITEM_ID);
            jsonObject.put("quantity", Counter);
            jsonObject.put("unit_price", unit_price);
            jsonObject.put("total_price", finaltotal);
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

    public void showTotal() {
        if (Common.MERCHANT_TYPE == 2) {
            finaltotal = unit_price * Counter;
            DecimalFormat form = new DecimalFormat("0.00");
            binding.txtProPrice.setText("SR " + String.valueOf(finaltotal));
        } else {
            //  total_int = 0;
            //  unit_price = 0;
            // total_int = size_price;
            if (size_price == 0) {
                finaltotal = unit_price * Counter;
            } else {
                finaltotal = size_price * Counter;
            }
            DecimalFormat form = new DecimalFormat("0.00");
            binding.txtProPrice.setText("SR " + String.valueOf(finaltotal));

        }

    }

    private void GetAPICallRestaurantUpdateItemDetails(int MENU_ID, int ITEM_ID) {
        Common.hideKeyboard(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("menu_id", MENU_ID);
            jsonObject.put("item_id", ITEM_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_RESTAURANT_MENU_ITEM_DETAIL;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_RESTAURANT_MENU_ITEM_DETAIL, this);
    }

    private void GetAPICallRestaurantItemDetails(int MENU_ID, int ITEM_ID) {
        Common.hideKeyboard(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("menu_id", MENU_ID);
            jsonObject.put("item_id", ITEM_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_RESTAURANT_MENU_ITEM_DETAIL;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_RESTAURANT_MENU_ITEM_DETAIL, this);
    }

    private void GetAPICallRestaurantItemUPDATEAddtoCart(int addition_id, int size_id, int removal_id, double unit_price, int restaurant_id, int menu_id, int iitem_id) {
        Common.hideKeyboard(getActivity());


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("menu_id", menu_id);
            jsonObject.put("cart_item_id", CART_ITEM_ID);
            jsonObject.put("restaurant_id", restaurant_id);
            jsonObject.put("addition_id", addition_id);
            jsonObject.put("size_id", size_id);
            jsonObject.put("removal_id", removal_id);
            jsonObject.put("quantity", Counter);
            jsonObject.put("unit_price", unit_price);
            jsonObject.put("total_price", finaltotal);
            jsonObject.put("addition_price", addition_price * Counter);
            jsonObject.put("delivery_type", DELIVER_TYPE);
            jsonObject.put("note", binding.edNotes.getText().toString().trim());
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

    private void GetAPICallRestaurantItemAddtoCart(int addition_id, int size_id, int removal_id, double unit_price, int restaurant_id, int menu_id, int Item_id) {
        Common.hideKeyboard(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("menu_id", menu_id);
            jsonObject.put("item_id", Item_id);
            jsonObject.put("restaurant_id", restaurant_id);
            jsonObject.put("addition_id", addition_id);
            jsonObject.put("size_id", size_id);
            jsonObject.put("removal_id", removal_id);
            jsonObject.put("quantity", Counter);
            jsonObject.put("unit_price", unit_price);
            jsonObject.put("total_price", finaltotal);
            jsonObject.put("delivery_type", DELIVER_TYPE);
            jsonObject.put("note", binding.edNotes.getText().toString().trim());
            jsonObject.put("addition_price", addition_price * Counter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_RESTAURANT_ADD_TO_CART;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, OPERATION_ADD_TO_CART, this);
    }

    public void showAddtoCartItem() {
        final Dialog dialogm = new Dialog(this);
        dialogm.setCancelable(false);
        dialogm.setContentView(R.layout.pop_addto_cart_success);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogm.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        dialogm.getWindow().setAttributes(lp);
        dialogm.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialogm.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        AppCompatButton bt_addtocart = dialogm.findViewById(R.id.bt_addtocart);
        ImageView iv_check = dialogm.findViewById(R.id.iv_check);
        TextView txt_name = dialogm.findViewById(R.id.txt_name);
        if (Common.MERCHANT_TYPE == 1) {
            bt_addtocart.setBackground(getResources().getDrawable(R.drawable.btn_golden));
            iv_check.setColorFilter(getResources().getColor(R.color.colorAccent));
            txt_name.setTextColor(getResources().getColor(R.color.colorAccent));
        } else {
            bt_addtocart.setBackground(getResources().getDrawable(R.drawable.btn_kesari));
            iv_check.setColorFilter(getResources().getColor(R.color.super_mart));
            txt_name.setTextColor(getResources().getColor(R.color.super_mart));
        }
        bt_addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2s = new Intent(RestaurantDetailActivity.this, RestaurantProfileActivity.class);
                startActivity(i2s);
                finish();
                dialogm.dismiss();
            }
        });
        dialogm.show();
    }

    @Override
    public void onStartLoading(int operationCode) {
        if (operationCode == APIcall.OPERATION_ADD_TO_CART) {
            showDialog();
        }
        if (operationCode == APIcall.OPERATION_RESTAURANT_MENU_ITEM_DETAIL) {
            showDialog();
        }
        if (operationCode == APIcall.OPERATION_MENU_ITEM_UPDATE_DETAIL) {
            showDialog();
        }
        if (operationCode == APIcall.OPERATION_ADD_TO_CART_SUPER) {
            showDialog();
        }
    }

    @Override
    public void onProgress(int operationCode, int progress) {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onSuccess(int operationCode, String response, Object customData) {
//        try {
        if (operationCode == APIcall.OPERATION_MENU_ITEM_UPDATE_DETAIL) {
            hideDialog();
            binding.llMain.setVisibility(View.VISIBLE);
            Gson gson = new Gson();
            ExampleUserItem exampleUser = gson.fromJson(response, ExampleUserItem.class);
            if (exampleUser.getStatus() == 200) {
//                    Common.UpdateCart.setAdditionId(exampleUser.getResponceData().getCart().getAdditionId());
//                    Common.UpdateCart.setCartId(exampleUser.getResponceData().getCart().getCartId());
//                    Common.UpdateCart.setCreatedAt(exampleUser.getResponceData().getCart().getCreatedAt());
//                    Common.UpdateCart.setId(exampleUser.getResponceData().getCart().getId());
//                    Common.UpdateCart.setItemDetail(exampleUser.getResponceData().getCart().getItemDetail());
//                    Common.UpdateCart.setItemId(exampleUser.getResponceData().getCart().getItemId());
//                    Common.UpdateCart.setMenuId(exampleUser.getResponceData().getCart().getMenuId());
//                    Common.UpdateCart.setQuantity(exampleUser.getResponceData().getCart().getQuantity());
//                    Common.UpdateCart.setRemovalId(exampleUser.getResponceData().getCart().getRemovalId());
//                    Common.UpdateCart.setRestaurantId(exampleUser.getResponceData().getCart().getRestaurantId());
//                    Common.UpdateCart.setSizeId(exampleUser.getResponceData().getCart().getSizeId());
//                    Common.UpdateCart.setTotalPrice(exampleUser.getResponceData().getCart().getTotalPrice());
//                    Common.UpdateCart.setType(exampleUser.getResponceData().getCart().getType());
//                    Common.UpdateCart.setUnitPrice(exampleUser.getResponceData().getCart().getUnitPrice());
//                    Common.UpdateCart.setUserId(exampleUser.getResponceData().getCart().getUserId());
                   /* Intent intent = new Intent();
                    intent.putExtra("Quantity", exampleUser.getResponceData().getCart().getQuantity().toString());
                    intent.putExtra("total", exampleUser.getResponceData().getCart().getTotalPrice().toString());
                    setResult(Activity.RESULT_OK, intent);*/
                Intent i2s = new Intent(RestaurantDetailActivity.this, RestaurantProfileActivity.class);
                startActivity(i2s);
                finish();
                Toast.makeText(RestaurantDetailActivity.this, "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RestaurantDetailActivity.this, "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        if (operationCode == APIcall.OPERATION_SUPER_MENU_ITEM_UPDATE_DETAIL) {
            hideDialog();
            binding.llMain.setVisibility(View.VISIBLE);
            Gson gson = new Gson();
            ExampleUserItem exampleUser = gson.fromJson(response, ExampleUserItem.class);
            if (exampleUser.getStatus() == 200) {
                Intent i2s = new Intent(RestaurantDetailActivity.this, RestaurantProfileActivity.class);
                startActivity(i2s);
                finish();
                Toast.makeText(RestaurantDetailActivity.this, "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RestaurantDetailActivity.this, "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        if (operationCode == APIcall.OPERATION_RESTAURANT_MENU_ITEM_DETAIL) {
            hideDialog();
            binding.llMain.setVisibility(View.VISIBLE);
            Gson gson = new Gson();
            ExampleUser exampleUser = gson.fromJson(response, ExampleUser.class);
            if (exampleUser.getStatus() == 200) {
                Glide.with(getContext()).load(exampleUser.getResponceData().getRestaurant().getImage()).centerCrop().into(binding.ivProImage);
                binding.txtProDsc.setText(exampleUser.getResponceData().getRestaurant().getDescription());
                binding.edNotes.setText(exampleUser.getResponceData().getRestaurant().getNote());
                binding.txtProName.setText(exampleUser.getResponceData().getRestaurant().getItemName());
                if (Common.MERCHANT_TYPE == 2) {
                    binding.txtProPrice.setText("SR " + exampleUser.getResponceData().getRestaurant().getPrice());
                    unit_price = Double.parseDouble(exampleUser.getResponceData().getRestaurant().getPrice());
                    showTotal();
                }
//
                binding.txtCalories.setText(exampleUser.getResponceData().getRestaurant().getCalories());
//
                final List<RestMenuItemSizeDetail> sizeDetails = exampleUser.getResponceData().getRestaurant().getSizeDetail();
                final List<RestMenuItemAdditionDetail> additionDetail = exampleUser.getResponceData().getRestaurant().getAdditionDetail();
                final List<RestMenuItemRemovalDetail> removalDetail = exampleUser.getResponceData().getRestaurant().getRemovalDetail();
                ColorStateList colorStateList = new ColorStateList(
                        new int[][]{
                                new int[]{-android.R.attr.state_enabled}, //disabled
                                new int[]{android.R.attr.state_enabled} //enabled
                        },
                        new int[]{
                                Color.RED, //disabled
                                Color.RED //enabled
                        }
                );
                if (UPDATE_ITEM == 0) {
                    binding.edNotes.setText(Common.UpdateCart.getNote());
                }

                for (int i = 0; i < sizeDetails.size(); i++) {
                    RadioButton rdbtn = new RadioButton(getContext());
                    rdbtn.setId(i);
                    rdbtn.setText(sizeDetails.get(i).getSize() + " SR " + Common.isStrempty(sizeDetails.get(i).getPrice()));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        rdbtn.setButtonTintList(colorStateList);
                    }
                    if (UPDATE_ITEM == 0) {
                        Log.d("SIZE_ID", "MAIN_URL" + Common.UpdateCart.getSizeId().getId());
                        if (Common.UpdateCart.getSizeId() != null) {
                            if (sizeDetails.get(i).getId() == Common.UpdateCart.getSizeId().getId()) {
                                size_id = sizeDetails.get(i).getId();
//                                    try {
                                if (sizeDetails.get(i).getSize() == null) {
                                    size_price = 0;
                                } else {
                                    size_price = Double.parseDouble(sizeDetails.get(i).getPrice());
                                }
//                                    } catch (Exception e) {
//                                        size_price = 0;
//                                    }
                                rdbtn.setChecked(true);

                            }
                        } else {
                            size_price = 0;
                            size_id = 0;
                        }
                        showTotal();
                    }
                    binding.rgCD.addView(rdbtn);


                    final int finalII = i;
                    rdbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            size_id = sizeDetails.get(finalII).getId();
                            restaurant_id = sizeDetails.get(finalII).getRestaurantId();
//                                try {
                            if (sizeDetails.get(finalII).getPrice() == null) {
                                size_price = 0;
                            } else {
                                size_price = Double.parseDouble(sizeDetails.get(finalII).getPrice());
                            }
//                                } catch (Exception e) {
//                                    size_price = 0;
//                                }
                            showTotal();
                        }
                    });
                }
                for (int i = 0; i < additionDetail.size(); i++) {
                    RadioButton rdbtn = new RadioButton(getContext());
                    rdbtn.setId(i);
                    rdbtn.setText(additionDetail.get(i).getAdidtionItem() + " SR " + additionDetail.get(i).getPrice());

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        rdbtn.setButtonTintList(colorStateList);
                    }
                    if (UPDATE_ITEM == 0) {
                        Log.d("SIZE_ID", "MAIN_URL" + Common.UpdateCart.getSizeId().getId());
                        if (Common.UpdateCart.getAdditionId() != null) {
                            if (additionDetail.get(i).getId() == Common.UpdateCart.getAdditionId().getId()) {
                                addition_id = additionDetail.get(i).getId();
//                                    try {
                                if (additionDetail.get(i).getPrice() == null) {
                                    addition_price = 0;
                                } else {
                                    addition_price = Double.parseDouble(additionDetail.get(i).getPrice());
                                }
//                                    } catch (Exception e) {
//                                        addition_price = 0;
//                                    }
                                rdbtn.setChecked(true);
                            }
                        } else {
                            addition_price = 0;
                            addition_id = 0;
                        }
                        showTotal();
                    }
                    binding.rgCDs.addView(rdbtn);
                    final int finalI = i;
                    rdbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addition_id = additionDetail.get(finalI).getId();
//                                try {
                            if (additionDetail.get(finalI).getPrice() == null) {
                                addition_price = 0;
                            } else {
                                addition_price = Double.parseDouble(additionDetail.get(finalI).getPrice());
                            }
//                                } catch (Exception e) {
//                                    addition_price = 0;
//                                    addition_id = 0;
//
//                                }
//                                boolean checked = ((RadioButton) v).isChecked();
//                                if (!checked) {
//                                    rdbtn.setChecked(true);
//                                    rdbtn.setSelected(true);
//
//                                } else {
//                                    rdbtn.setChecked(false);
//                                    rdbtn.setSelected(false);
//                                    addition_id = 0;
//                                    addition_price = 0;
//                                }
                            showTotal();
                        }
                    });
                }
                for (int i = 0; i < removalDetail.size(); i++) {
                    RadioButton rdbtn = new RadioButton(getContext());
                    rdbtn.setId(i);
                    rdbtn.setText(removalDetail.get(i).getRemoval());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        rdbtn.setButtonTintList(colorStateList);
                    }
                    if (UPDATE_ITEM == 0) {
                        Log.d("SIZE_ID", "MAIN_URL" + Common.UpdateCart.getSizeId().getId());
                        if (Common.UpdateCart.getRemovalId() != null) {
                            if (removalDetail.get(i).getId() == Common.UpdateCart.getRemovalId().getId()) {
                                removal_id = removalDetail.get(i).getId();
                                rdbtn.setChecked(true);
                            }
                        } else {
                            removal_id = 0;
                        }

                    }

                    binding.rgCDss.addView(rdbtn);
                    final int finalI = i;
                    rdbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            removal_id = removalDetail.get(finalI).getId();
                        }
                    });
                }
            } else {
                Toast.makeText(RestaurantDetailActivity.this, "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        if (operationCode == OPERATION_ADD_TO_CART) {
            hideDialog();
            binding.llMain.setVisibility(View.VISIBLE);
            Gson gson = new Gson();
            ExampleUser exampleUser = gson.fromJson(response, ExampleUser.class);
            if (exampleUser.getStatus() == 200) {
                onBackPressed();
                Toast.makeText(RestaurantDetailActivity.this, "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RestaurantDetailActivity.this, "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        if (operationCode == APIcall.OPERATION_ADD_TO_CART_SUPER) {
            hideDialog();
            binding.llMain.setVisibility(View.VISIBLE);
            Gson gson = new Gson();
            ExampleUser exampleUser = gson.fromJson(response, ExampleUser.class);
            if (exampleUser.getStatus() == 200) {
                onBackPressed();
                Toast.makeText(RestaurantDetailActivity.this, "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RestaurantDetailActivity.this, "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        hideDialog();
//        } catch (Exception e) {
//
//        }
    }

    @Override
    public void onFail(int operationCode, String response) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(this,RestaurantProfileActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();

    }
}