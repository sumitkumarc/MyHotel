package ontime.app.customer.doneActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;

import ontime.app.R;
import ontime.app.customer.Adapter.RvRestaurantMenuFilterItemListAdapter;
import ontime.app.customer.Adapter.RvRestaurantMenuFilterListAdapter;
import ontime.app.customer.Adapter.RvRestaurantMenuListAdapter;
import ontime.app.databinding.ActivityRestprofileBinding;
import ontime.app.model.usermain.ExampleUser;
import ontime.app.model.usermain.UserCart;
import ontime.app.model.usermain.UserCartItem;
import ontime.app.model.usernewmain.ExampleUserItem;
import ontime.app.model.usernewmain.UserItem;
import ontime.app.model.usermain.UserRestData;
import ontime.app.model.usermain.UserRestaurantProCategory;
import ontime.app.model.usermain.Userdate;
import ontime.app.okhttp.APIcall;
import ontime.app.okhttp.AppConstant;
import ontime.app.rv_interface;
import ontime.app.utils.BaseActivity;
import ontime.app.utils.Common;
import ontime.app.utils.SessionManager;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;

import static ontime.app.utils.Common.DELIVER_TYPE;

public class RestaurantProfileActivity extends BaseActivity implements View.OnClickListener, APIcall.ApiCallListner, rv_interface {

    ActivityRestprofileBinding binding;
    RvRestaurantMenuListAdapter menuListAdapter;
    RvRestaurantMenuFilterItemListAdapter menuFilterItemListAdapter;
    RvRestaurantMenuFilterListAdapter menuFilterListAdapter;
    private ProgressDialog dialog;
    Userdate userData;
    SessionManager sessionManager;
    rv_interface anInterface;

    public static boolean isTakeAway = false;
    int RE_ID = 0;
    int CartItems = 0;
    int RestaurantId = 0;
    String REST_IMAGE = "";
    String REST_NAME = "";
    String REST_LAT = "";
    String REST_LOG = "";
    String REST_NAME_BARNCH = "";
    String REST_RATING = "";
    Dialog dialogsDoyouwant;

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_restprofile);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(RestaurantProfileActivity.this);
        userData = sessionManager.getUserDetails();
        isTakeAway = true;
        anInterface = RestaurantProfileActivity.this;
        RE_ID = getIntent().getIntExtra("RE_ID", 1);
        setUpUI();

    }

    private void setUpUI() {
        binding.llMain.setVisibility(View.GONE);

        if (Common.MERCHANT_TYPE == 1) {
            Common.setSystemBarColor(this, R.color.colorAccent);
//            Common.setSystemBarLight(this);

            binding.rlMain.setBackground(getResources().getDrawable(R.drawable.profilebg));
            binding.llBar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            binding.ivBackArrow.setColorFilter(getResources().getColor(R.color.colorAccent));
            binding.txtCount.setTextColor(getResources().getColor(R.color.colorAccent));

        } else {
            Common.setSystemBarColor(this, R.color.super_mart);
//            Common.setSystemBarLight(this);
            binding.ivAddtocart.setImageDrawable(getResources().getDrawable(R.drawable.oragecartwthcircle));
            binding.ivAddtocart.setColorFilter(getResources().getColor(R.color.white));
            binding.rlMain.setBackground(getResources().getDrawable(R.drawable.marketprofilebg));
            binding.llBar.setBackgroundColor(getResources().getColor(R.color.super_mart));
            binding.ivBackArrow.setColorFilter(getResources().getColor(R.color.super_mart));
            binding.txtCount.setTextColor(getResources().getColor(R.color.super_mart));
        }

//        binding.rbRatingbar.setEnabled(false);
        LinearLayoutManager mLayoutManager1as = new LinearLayoutManager(getContext());
        mLayoutManager1as.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvList.setLayoutManager(mLayoutManager1as);


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.rvFilterList.setLayoutManager(mLayoutManager);

        GetAPICallRestaurantProfile(Common.RESTAURANT_ID);
//        GetAPICallRestaurantMenuitems(getIntent().getIntExtra("RE_ID", 1));
//        GetAPICallRestaurantCartList();


    }


    @Override
    protected void setListener() {
        super.setListener();
        binding.ivAddtocart.setOnClickListener(this);
        binding.llRating.setOnClickListener(this);
        binding.llBack.setOnClickListener(this);
        binding.ivDirection.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.iv_direction:
                if(REST_LAT != null){
                    Log.d(">>>>>>>",">>>>>" + REST_LAT + " " + REST_LOG);
                    Intent intents = new Intent(this, MapActivity.class);
                    intents.putExtra("LAT", REST_LAT);
                    intents.putExtra("LONG", REST_LOG);
                    intents.putExtra("REST_NAME_BARNCH", REST_NAME_BARNCH);
                    intents.putExtra("REST_NAME", REST_RATING);
                    startActivity(intents);
                }

                break;
            case R.id.ll_rating:

                Intent intent = new Intent(this, RestRattingListActivity.class);
                intent.putExtra("REST_IMAGE", REST_IMAGE);
                intent.putExtra("REST_NAME", REST_NAME);
                intent.putExtra("REST_NAME_BARNCH", REST_NAME_BARNCH);
                intent.putExtra("REST_RATING", REST_RATING);
                startActivity(intent);
                break;
            case R.id.iv_addtocart:

                if (CartItems == 0) {
                    Toast.makeText(this, getResources().getString(R.string.no_item_cart), Toast.LENGTH_SHORT).show();
                } else {
                    Intent i2 = new Intent(RestaurantProfileActivity.this, RestCartPenddingItemActivity.class);
                    startActivity(i2);
                    finish();
                }
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            default:
                break;
        }
    }


    private void showDialog() {
        dialog = new ProgressDialog(RestaurantProfileActivity.this);
        dialog.setMessage(getResources().getString(R.string.Please_wait));
        dialog.setCancelable(false);
        dialog.show();
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    private void GetAPICallRestaurantMenuitems(int menu_id) {
        Common.hideKeyboard(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("restaurant_id", Common.RESTAURANT_ID);
            jsonObject.put("menu_id", menu_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_RESTAURANT_MENU_ITEM;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_RESTAURANT_MENU_ITEM, this);
    }

    private void GetAPICallRestaurantProfile(int rest_id) {
        Common.hideKeyboard(getActivity());
        // showDialog();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("restaurant_id", rest_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_RESTAURANT_PROFILE;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_RESTAURANT_PROFILE, this);
    }


    @Override
    public void onStartLoading(int operationCode) {
        if (operationCode == APIcall.OPERATION_RESTAURANT_MENU_ITEM) {
            showDialog();
        }
        if (operationCode == APIcall.OPERATION_ITEM_BY_CATEGORY) {
            showDialog();
        }
//        if (operationCode == APIcall.OPERATION_ITEM_BY_CATEGORY) {
//            showDialog();
//        }

    }

    @Override
    public void onProgress(int operationCode, int progress) {

    }

    @Override
    public void onSuccess(int operationCode, String response, Object customData) {
//        try {
        if (operationCode == APIcall.OPERATION_RESTAURANT_MENU_ITEM) {
            hideDialog();
            binding.llMain.setVisibility(View.VISIBLE);
            Gson gson = new Gson();
            ExampleUser exampleUser = gson.fromJson(response, ExampleUser.class);
            if (exampleUser.getStatus() == 200) {
                List<UserRestData> userRestData = exampleUser.getResponceData().getItems().getData();
                if (userRestData.size() == 0) {
                    binding.rvList.setVisibility(View.GONE);
                    binding.txtNoItem.setVisibility(View.VISIBLE);
                } else {
                    binding.rvList.setVisibility(View.VISIBLE);
                    binding.txtNoItem.setVisibility(View.GONE);
                    menuListAdapter = new RvRestaurantMenuListAdapter(getContext(), userRestData);
                    binding.rvList.setItemAnimator(new DefaultItemAnimator());
                    binding.rvList.setAdapter(menuListAdapter);
                }


            } else {
                Toast.makeText(RestaurantProfileActivity.this, "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
                binding.rvList.setVisibility(View.GONE);
                binding.txtNoItem.setVisibility(View.VISIBLE);
            }
        }
        if (operationCode == APIcall.OPERATION_RESTAURANT_PROFILE) {
            hideDialog();
            binding.llMain.setVisibility(View.VISIBLE);
            Gson gson = new Gson();
            ExampleUser exampleUser = gson.fromJson(response, ExampleUser.class);
            if (exampleUser.getStatus() == 200) {
                List<UserRestaurantProCategory> userRestaurantProCategories = exampleUser.getResponceData().getRestaurant().getCategories();
                RestaurantId = exampleUser.getResponceData().getRestaurant().getId();
                REST_RATING = exampleUser.getResponceData().getRestaurant().getAvgRate();
                REST_NAME = Common.isStrempty(exampleUser.getResponceData().getRestaurant().getName());
                REST_NAME_BARNCH = Common.isStrempty(exampleUser.getResponceData().getRestaurant().getBranchName());
                REST_IMAGE = exampleUser.getResponceData().getRestaurant().getImage();
                REST_LAT = exampleUser.getResponceData().getRestaurant().getLatitude();
                REST_LOG = exampleUser.getResponceData().getRestaurant().getLongitude();
                Common.REST_LAT = exampleUser.getResponceData().getRestaurant().getLatitude();
                Common.REST_LOG = exampleUser.getResponceData().getRestaurant().getLongitude();
                Common.REST_NAME = Common.isStrempty(exampleUser.getResponceData().getRestaurant().getName());
                binding.txtResName.setText(Common.isStrempty(exampleUser.getResponceData().getRestaurant().getName()));
                binding.txtResBarnchname.setText(Common.isStrempty(exampleUser.getResponceData().getRestaurant().getBranchName()));
//                    binding.rbRatingbar.setRating(exampleUser.getResponceData().getRestaurant().getAvag_rating());
                binding.rbRatingbar.setRating((float) Float.parseFloat(exampleUser.getResponceData().getRestaurant().getAvgRate()));
                Glide.with(getContext()).load(exampleUser.getResponceData().getRestaurant().getImage()).centerCrop().into(binding.ivRestProfileImg);
                menuFilterListAdapter = new RvRestaurantMenuFilterListAdapter(getContext(), userRestaurantProCategories);
                binding.rvFilterList.setItemAnimator(new DefaultItemAnimator());
                binding.rvFilterList.setAdapter(menuFilterListAdapter);
                menuFilterListAdapter.setOnItemClickListener(anInterface);
            } else {
                Toast.makeText(RestaurantProfileActivity.this, "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        if (operationCode == APIcall.OPERATION_ITEM_BY_CATEGORY) {
            hideDialog();
            binding.rvList.setVisibility(View.VISIBLE);
            Gson gson = new Gson();
            ExampleUserItem exampleUser = gson.fromJson(response, ExampleUserItem.class);
            if (exampleUser.getStatus() == 200) {
                List<UserItem> userItems = exampleUser.getResponceData().getUserItem();
                if (userItems.size() != 0) {
                    menuFilterItemListAdapter = new RvRestaurantMenuFilterItemListAdapter(getContext(), userItems);
                    binding.rvList.setItemAnimator(new DefaultItemAnimator());
                    binding.txtNoItem.setVisibility(View.GONE);
                    binding.rvList.setVisibility(View.VISIBLE);
                    binding.rvList.setAdapter(menuFilterItemListAdapter);
                } else {
                    binding.rvList.setVisibility(View.GONE);
                    binding.txtNoItem.setVisibility(View.VISIBLE);
                }

            }
        }
        if (operationCode == APIcall.OPERATION_RESTAURANT_MY_CART) {
            hideDialog();
            Gson gson = new Gson();
            ExampleUser exampleUser = gson.fromJson(response, ExampleUser.class);
            if (exampleUser.getStatus() == 200) {
                List<UserCart> cartList = exampleUser.getResponceData().getCart();
                if (cartList.size() != 0) {

                    Common.newCartItem = new ArrayList<>();
                    for (int i = 0; i < cartList.size(); i++) {
                        if (cartList.get(i).getRestaurantId() == RestaurantId) {
                            DELIVER_TYPE = cartList.get(i).getDeliveryType();
                            Common.newCartItem = cartList.get(i).getItems();
                        }
                    }

                    binding.txtCount.setVisibility(View.VISIBLE);
//                        List<UserCart> newuserCarts = new ArrayList<>();
//                        Common.newCartItem = new ArrayList<>();

//                        for (int i = 0; i < cartList.size(); i++) {
//                            UserCart userCart = new UserCart();
//                            userCart.setRestaurant(cartList.get(i).getRestaurant());
//                            userCart.setItems(cartList.get(i).getItems());
//                            newuserCarts.add(userCart);
//                            Common.newCartItem.addAll(userCart.getItems());
//                        }

                    Log.d("MAINERROR", ">>>>>" + Common.newCartItem.size());
                    CartItems = Common.newCartItem.size();
                    binding.txtCount.setText(String.valueOf(CartItems));


                } else {
                    binding.txtCount.setVisibility(View.GONE);
                }
                if (Common.MERCHANT_TYPE == 1) {
                    if (CartItems == 0) {
                        if (dialogsDoyouwant != null && dialogsDoyouwant.isShowing()) {
                            dialogsDoyouwant.dismiss();
                            dialogsDoyouwant = null;
                        }
                        if (Common.DELIVER_TYPE.equals("")) {
                            showDialogDoyouwant();
                        }
                    }

                }
            } else {
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
    public void OnItemClick(Integer id, int item_id) {
        binding.rvList.setVisibility(View.GONE);
//        GetAPICallRest(item_id);
        GetAPICallRestaurantMenuitems(item_id);
    }

    @Override
    public void OnUpDateItemClick(UserCartItem item_id) {

    }

    private void GetAPICallRest(int menu_id) {
        Common.hideKeyboard(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("menu_id", menu_id);
            jsonObject.put("restaurant_id", RE_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_ITEM_BY_CATEGORY;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_ITEM_BY_CATEGORY, this);
    }

    private void GetAPICallRestaurantCartList() {
        Common.hideKeyboard(getActivity());
        String url = AppConstant.GET_RESTAURANT_MY_CART;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(false);
        apIcall.execute(url, APIcall.OPERATION_RESTAURANT_MY_CART, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isConnected()) {
            GetAPICallRestaurantCartList();
        }
    }

    public void showDialogDoyouwant() {
        dialogsDoyouwant = new Dialog(this);
        dialogsDoyouwant.setCancelable(false);
        dialogsDoyouwant.setContentView(R.layout.popup_doyouwant);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogsDoyouwant.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        dialogsDoyouwant.getWindow().setAttributes(lp);
        dialogsDoyouwant.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialogsDoyouwant.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView tvtake = dialogsDoyouwant.findViewById(R.id.tvtake);

        TextView tvdine = dialogsDoyouwant.findViewById(R.id.tvdine);

        if (Common.MERCHANT_TYPE == 1) {
            tvtake.setBackground(this.getResources().getDrawable(R.drawable.btn_rest_pop));
            tvdine.setBackground(this.getResources().getDrawable(R.drawable.btn_rest_pop));
        } else {
            tvtake.setBackground(this.getResources().getDrawable(R.drawable.btn_super_pop));
            tvdine.setBackground(this.getResources().getDrawable(R.drawable.btn_super_pop));
        }
        tvtake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTakeAway = false;
                DELIVER_TYPE = "take away";
//                Intent i2 = new Intent(this, RestaurantDetailActivity.class);
//                i2.putExtra("MENU_ID" , mresponceDatumList.get(position).getMenuId());
//                i2.putExtra("ITEM_ID" , mresponceDatumList.get(position).getId());
//                i2.putExtra("UPDATE_ITEM", 1);
//                mContext.startActivity(i2);
                dialogsDoyouwant.dismiss();
            }
        });
        tvdine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTakeAway = false;
                DELIVER_TYPE = "dine in";
//                Intent i2 = new Intent(mContext, RestaurantDetailActivity.class);
//                i2.putExtra("MENU_ID" , mresponceDatumList.get(position).getMenuId());
//                i2.putExtra("ITEM_ID" , mresponceDatumList.get(position).getId());
//                i2.putExtra("UPDATE_ITEM", 1);
//                mContext.startActivity(i2);
                dialogsDoyouwant.dismiss();
            }
        });
        dialogsDoyouwant.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, RestaurantlistActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}