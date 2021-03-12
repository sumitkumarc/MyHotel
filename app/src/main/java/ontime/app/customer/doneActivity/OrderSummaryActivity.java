package ontime.app.customer.doneActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import ontime.app.R;
import ontime.app.customer.Activity.SentRequestActivity;
import ontime.app.customer.Adapter.RvPaymnetMethordListAdapter;
import ontime.app.customer.Adapter.RvRestOrderCartListAdapter;
import ontime.app.customer.Adapter.RvRestaurantCartPenddingListAdapter;
import ontime.app.databinding.ActivityOrdersummaryBinding;
import ontime.app.model.usermain.ExampleUser;
import ontime.app.model.usermain.UserCart;
import ontime.app.model.usermain.UserCartItem;
import ontime.app.okhttp.APIcall;
import ontime.app.okhttp.AppConstant;
import ontime.app.utils.BaseActivity;
import ontime.app.utils.Common;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import company.tap.gosellapi.GoSellSDK;
import company.tap.gosellapi.internal.api.callbacks.GoSellError;
import company.tap.gosellapi.internal.api.models.Authorize;
import company.tap.gosellapi.internal.api.models.Charge;
import company.tap.gosellapi.internal.api.models.PhoneNumber;
import company.tap.gosellapi.internal.api.models.SaveCard;
import company.tap.gosellapi.internal.api.models.SavedCard;
import company.tap.gosellapi.internal.api.models.Token;
import company.tap.gosellapi.open.buttons.PayButtonView;
import company.tap.gosellapi.open.controllers.SDKSession;
import company.tap.gosellapi.open.controllers.ThemeObject;
import company.tap.gosellapi.open.delegate.SessionDelegate;
import company.tap.gosellapi.open.enums.AppearanceMode;
import company.tap.gosellapi.open.enums.TransactionMode;
import company.tap.gosellapi.open.models.CardsList;
import company.tap.gosellapi.open.models.Customer;
import company.tap.gosellapi.open.models.PaymentItem;
import company.tap.gosellapi.open.models.TapCurrency;
import company.tap.gosellapi.open.models.Tax;
import okhttp3.RequestBody;

public class OrderSummaryActivity extends BaseActivity implements View.OnClickListener, APIcall.ApiCallListner, SessionDelegate {
    private int[] logos = {
            R.drawable.cod, R.drawable.visa, R.drawable.wallettype/*, R.drawable.mac*/
    };
    public static ActivityOrdersummaryBinding binding;
    RvPaymnetMethordListAdapter madapter;
    private final int SDK_REQUEST_CODE = 1001;
    public SDKSession sdkSession;
//    private PayButtonView payButtonView;
    private ProgressDialog dialog;
    RvRestOrderCartListAdapter mMAdapter;
    List<UserCartItem> userCartItemDetail;
    float atotal = 0;
    float totalQty = 0;
    float total = 0;
    float atotalQty = 0;
    List<UserCart> cartList;
    float price = 0;
    float dprice = 0;
    int percentage = 0;
    String GrandTotal = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.rvPaymnetMethordList.setLayoutManager(mLayoutManager);
        madapter = new RvPaymnetMethordListAdapter(getContext(), logos);

        LinearLayoutManager mLayoutManager1as = new LinearLayoutManager(getContext());
        mLayoutManager1as.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvList.setLayoutManager(mLayoutManager1as);

        binding.rvPaymnetMethordList.setAdapter(madapter);
        binding.llMain.setVisibility(View.GONE);

        if (Common.MERCHANT_TYPE == 1) {
            Common.setSystemBarColor(this, R.color.colorAccent);
//            Common.setSystemBarLight(this);
            binding.rly.setBackground(getResources().getDrawable(R.drawable.profilebg));
            binding.llToolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            binding.ivBackArrow.setColorFilter(getResources().getColor(R.color.colorAccent));
            binding.txtGrandTotal.setTextColor(getResources().getColor(R.color.colorAccent));
            binding.txtOrderTotal.setTextColor(getResources().getColor(R.color.colorAccent));
            binding.txtPayAmount.setTextColor(getResources().getColor(R.color.colorAccent));
            binding.btSendRequest.setBackground(getResources().getDrawable(R.drawable.btn_goldenrect));
            binding.btApply.setBackground(getResources().getDrawable(R.drawable.btn_goldenrect));
        } else {
            Common.setSystemBarColor(this, R.color.super_mart);
//            Common.setSystemBarLight(this);
            binding.rly.setBackground(getResources().getDrawable(R.drawable.summaryorange));
            binding.llToolbar.setBackgroundColor(getResources().getColor(R.color.super_mart));
            binding.ivBackArrow.setColorFilter(getResources().getColor(R.color.super_mart));
            binding.txtGrandTotal.setTextColor(getResources().getColor(R.color.super_mart));
            binding.txtOrderTotal.setTextColor(getResources().getColor(R.color.super_mart));
            binding.txtPayAmount.setTextColor(getResources().getColor(R.color.super_mart));
            binding.btSendRequest.setBackground(getResources().getDrawable(R.drawable.btn_goldenrectorange));
            binding.btApply.setBackground(getResources().getDrawable(R.drawable.btn_goldenrectorange));
        }
        binding.llMain.setVisibility(View.GONE);
        GetAPICallRestaurantNewCartList();
        GetApiCallToWallets();
        GetApiCallCouponCode(binding.edCouponCode.getText().toString(), Common.newCartItem.get(0).getCartId());
        binding.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showTotal(0);
                    Toast.makeText(getContext(), "You checked the checkbox!", Toast.LENGTH_SHORT).show();
                } else {
                    showTotal(0);
                }
            }
        });
    }

    private void GetAPICallUserpaymentFail() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("payment_type", 2);
            jsonObject.put("transaction_amount", 100);
            jsonObject.put("transaction_type", "transaction_fail");
            jsonObject.put("payment_status", "failure");
            jsonObject.put("order_id", "1");
            jsonObject.put("transaction_id", "#tw554545");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_USER_PAYMENT_FAIL;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_USER_PAYMENT_FAIL, this);
    }

    private void GetAPICallUserpaymentPost() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("payment_type", 2);
            jsonObject.put("transaction_amount", 100);
            jsonObject.put("order_id", "1");
            jsonObject.put("transaction_id", "#tw554545");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_USER_PAYMENT_POST;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_USER_PAYMENT_POST, this);
    }

    private void GetAPICallRestaurantNewCartList() {
        if (Common.newCartItem.size() != 0) {
            binding.llMain.setVisibility(View.VISIBLE);
            binding.rvList.setVisibility(View.VISIBLE);
            for (int i = 0; i < Common.newCartItem.size(); i++) {
                total = Float.parseFloat(Common.newCartItem.get(i).getTotalPrice());
                atotal = atotal + total;
            }
            for (int i = 0; i < Common.newCartItem.size(); i++) {
                totalQty = Common.newCartItem.get(i).getQuantity();
                atotalQty = atotalQty + totalQty;
            }
            binding.txtDate.setText(Common.isStrempty(parseDateToddMMyyyy(Common.newCartItem.get(0).getCreatedAt())));
            binding.txtGrandTotal.setText(getResources().getString(R.string.Total) + ": SR " + String.valueOf(atotal));
            mMAdapter = new RvRestOrderCartListAdapter(getContext(), Common.newCartItem, false);
            binding.rvList.setItemAnimator(new DefaultItemAnimator());
            binding.rvList.setAdapter(mMAdapter);
        } else {
            binding.rvList.setVisibility(View.GONE);
            binding.llMain.setVisibility(View.GONE);
        }

    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd.MM.yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    @Override
    protected void setListener() {
        super.setListener();
        binding.llBack.setOnClickListener(this);
        binding.btSendRequest.setOnClickListener(this);
        binding.btApply.setOnClickListener(this);
    }

    private void GetAPICallRestaurantCartList() {
        Common.hideKeyboard(getActivity());
        String url = AppConstant.GET_RESTAURANT_MY_CART;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(false);
        apIcall.execute(url, APIcall.OPERATION_RESTAURANT_MY_CART, this);
    }

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ordersummary);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.bt_apply:
                GetApiCallCouponCode(binding.edCouponCode.getText().toString(), Common.newCartItem.get(0).getCartId());
                break;
            case R.id.bt_send_request:
//                ApiCAllToRejectOrder();
                if (Common.PAYMENT_TYPE == 2) {
                    showDialog();
                    initialiseSDK(Double.parseDouble(GrandTotal), "");
                    configureSDKMode();
                } else {
                    GetApiCallToPlceOrder("");
                }

                break;
            default:
                break;
        }
    }

    private void GetApiCallToWallets() {
        String url = AppConstant.GET_WALLET;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(false);
        apIcall.execute(url, APIcall.OPERATION_WALLET, this);
    }

    private void GetApiCallToPlceOrder(String transaction_id) {
        JSONObject jsonObject = new JSONObject();
        JSONArray arrForA = new JSONArray();
        JSONObject itemA = new JSONObject();
        try {
            for (int i = 0; i < Common.newCartItem.size(); i++) {
                itemA.put("menu_id", Common.newCartItem.get(i).getMenuId());
                if (Common.newCartItem.get(i).getSizeId() != null) {
                    itemA.put("size_id", Common.newCartItem.get(i).getSizeId().getId());
                } else {
                    itemA.put("size_id", 0);
                }
                if (Common.newCartItem.get(i).getRemovalId() != null) {
                    itemA.put("removal_id", Common.newCartItem.get(i).getRemovalId().getId());
                } else {
                    itemA.put("removal_id", 0);
                }
                if (Common.newCartItem.get(i).getAdditionId() != null) {
                    itemA.put("addition_id", Common.newCartItem.get(i).getAdditionId().getId());
                } else {
                    itemA.put("addition_id", 0);
                }
                itemA.put("item_id", Common.newCartItem.get(i).getItemId());
                itemA.put("quantity", Common.newCartItem.get(i).getQuantity());
                itemA.put("unit_price", Common.newCartItem.get(i).getUnitPrice());
                itemA.put("total_price", Common.newCartItem.get(i).getTotalPrice());
                arrForA.put(itemA);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            jsonObject.put("total_quantity", atotalQty);
            jsonObject.put("restaurant_id", Common.newCartItem.get(0).getRestaurantId());
            jsonObject.put("cart_id", Common.newCartItem.get(0).getCartId());
            jsonObject.put("total_price", price);
            jsonObject.put("grand_total", GrandTotal);
            jsonObject.put("payment_type", Common.PAYMENT_TYPE);
            jsonObject.put("transaction_id", transaction_id);
            jsonObject.put("items", arrForA);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_PLACE_ORDER;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_PLACE_ORDER, this);
    }

    private void GetApiCallCouponCode(String coupon_code, Integer cartId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("coupon_code", coupon_code);
            jsonObject.put("cart_id", cartId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_DISCOUNT;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_DISCOUNT, this);
    }

    private void showDialog() {
        dialog = new ProgressDialog(OrderSummaryActivity.this);
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
        if (operationCode == APIcall.OPERATION_PLACE_ORDER) {
            showDialog();
        }
        if (operationCode == APIcall.OPERATION_DISCOUNT) {
            showDialog();
        }
        if (operationCode == APIcall.OPERATION_USER_PAYMENT_FAIL) {
            showDialog();
        }
        if (operationCode == APIcall.OPERATION_USER_PAYMENT_POST) {
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
            if (operationCode == APIcall.OPERATION_RESTAURANT_MY_CART) {
                hideDialog();
                binding.llMain.setVisibility(View.VISIBLE);
                Gson gson = new Gson();
                ExampleUser exampleUser = gson.fromJson(response, ExampleUser.class);
                if (exampleUser.getStatus() == 200) {
                    cartList = exampleUser.getResponceData().getCart();
                    if (cartList.size() != 0) {
                        binding.rvList.setVisibility(View.VISIBLE);
                        userCartItemDetail = cartList.get(0).getItems();
                        for (int i = 0; i < userCartItemDetail.size(); i++) {
                            total = Float.parseFloat(userCartItemDetail.get(i).getTotalPrice());
                            atotal = atotal + total;
                        }
                        binding.txtGrandTotal.setText(getResources().getString(R.string.Total) + ": SR " + String.valueOf(atotal));
                    } else {
                        binding.rvList.setVisibility(View.GONE);
                    }
                } else {
                }
            }
            if (operationCode == APIcall.OPERATION_PLACE_ORDER) {
                hideDialog();
                Gson gson = new Gson();
                ExampleUser exampleUser = gson.fromJson(response, ExampleUser.class);
                if (exampleUser.getStatus() == 200) {
                    Toast.makeText(this, "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
//
                    Intent intent = new Intent(OrderSummaryActivity.this, SentRequestActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            if (operationCode == APIcall.OPERATION_DISCOUNT) {
                hideDialog();
                Gson gson = new Gson();
                ExampleUser exampleUser = gson.fromJson(response, ExampleUser.class);
                if (exampleUser.getStatus() == 200) {
//                    Toast.makeText(this, "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
                    price = Float.parseFloat(exampleUser.getResponceData().getDiscountedAmount());
                    GrandTotal = exampleUser.getResponceData().getGrandTotal();
                    binding.txtAdditionalItem.setText(getResources().getString(R.string.additional_item) + " : SR " + Common.isStrempty(exampleUser.getResponceData().getAdditional()));
                    binding.txtTotalTax.setText(getResources().getString(R.string.total_tax) + ": SR " + Common.isStrempty(exampleUser.getResponceData().getTax()));
                    binding.txtServiceFees.setText(getResources().getString(R.string.service_fees) + ": SR " + Common.isStrempty(exampleUser.getResponceData().getServiceFee()));
                    binding.txtServiceTax.setText(getResources().getString(R.string.service_fees_tax) + ": SR " + Common.isStrempty(exampleUser.getResponceData().getServiceFeeTax()));
                    binding.txtOrderTotal.setText(getResources().getString(R.string.Total) + ": SR " + Common.isStrempty(exampleUser.getResponceData().getTotal()));
                    binding.txtPayAmount.setText(getResources().getString(R.string.TotalPrice) + ": SR " + Common.isStrempty(exampleUser.getResponceData().getGrandTotal()));
                    String nullres = Common.isStrempty(String.valueOf(exampleUser.getResponceData().getPercentage()));
                    if (nullres.equals("null")) {
                        binding.txtDiscount.setText(getResources().getString(R.string.disconnect) + " 0%");
                    } else {
                        binding.txtDiscount.setText(getResources().getString(R.string.disconnect) + Common.isStrempty(String.valueOf(exampleUser.getResponceData().getPercentage())) + "%");
                    }

                } else {
                    Toast.makeText(this, "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            if (operationCode == APIcall.OPERATION_WALLET) {
                Gson gson = new Gson();
                ExampleUser exampleUser = gson.fromJson(response, ExampleUser.class);
                if (exampleUser.getStatus() == 200) {
                    Float main = Float.valueOf(exampleUser.getResponceData().getBalance());
                    if (main < atotal) {
                        binding.txtWallet.setTextColor(getResources().getColor(R.color.colorAccent));
                    } else {
                        binding.txtWallet.setTextColor(getResources().getColor(R.color.green));
                    }
                    binding.txtWallet.setText(getResources().getString(R.string.wallet) + " : SR " + Common.isStrempty(exampleUser.getResponceData().getBalance()));
//                    Toast.makeText(getContext(), "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            if (operationCode == APIcall.OPERATION_USER_PAYMENT_FAIL) {

                hideDialog();
                JSONObject root = null;
                try {
                    root = new JSONObject(response);
//                    Toast.makeText(OrderSummaryActivity.this, "" + root.getString("message"), Toast.LENGTH_SHORT).show();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (operationCode == APIcall.OPERATION_USER_PAYMENT_POST) {

                hideDialog();
                JSONObject root = null;
                try {
                    root = new JSONObject(response);
//                    Toast.makeText(OrderSummaryActivity.this, "" + root.getString("message"), Toast.LENGTH_SHORT).show();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {

        }
    }

    private void showTotal(Integer mper) {
        dprice = 0;
        price = 0;
        dprice = (atotal * mper / 100);
        price = atotal - dprice;
        binding.txtGrandTotal.setText(getResources().getString(R.string.Total) + ": SR " + String.valueOf(price));
    }

    @Override
    public void onFail(int operationCode, String response) {

    }

    private void configureApp() {
        GoSellSDK.init(this, "sk_test_fC4J0RzAobEkZuBaFVwOyQmL", "ontime.app");  // to be replaced by merchant
        //GoSellSDK.setLocale("en");//  language to be set by merchant
    }

    /**
     * Integrating SDK.
     */
    private void startSDK() {
        /**
         * Required step.
         * Configure SDK with your Secret API key and App Bundle name registered with tap company.
         */
        configureApp();

        /**
         * Optional step
         * Here you can configure your app theme (Look and Feel).
         */
        configureSDKThemeObject();

        /**
         * Required step.
         * Configure SDK Session with all required data.
         */
        configureSDKSession();

        /**
         * Required step.
         * Choose between different SDK modes
         */
        configureSDKMode();

        /**
         * If you included Tap Pay Button then configure it first, if not then ignore this step.
         */
//        initPayButton();
    }

    public void initialiseSDK(double v, String s) {
        startSDK();
    }

    private void configureSDKThemeObject() {

        ThemeObject.getInstance()

                // set Appearance mode [Full Screen Mode - Windowed Mode]
                .setAppearanceMode(AppearanceMode.FULLSCREEN_MODE) // **Required**
//                .setSdkLanguage("ar") //if you dont pass locale then default locale EN will be used

                // Setup header font type face **Make sure that you already have asset folder with required fonts**
//                .setHeaderFont(Typeface.createFromAsset(getAssets(),"fonts/roboto_light.ttf"))//**Optional**

                //Setup header text color
                .setHeaderTextColor(getResources().getColor(R.color.black1))  // **Optional**

                // Setup header text size
                .setHeaderTextSize(17) // **Optional**

                // setup header background
                .setHeaderBackgroundColor(getResources().getColor(R.color.french_gray_new))//**Optional**

                // setup card form input font type
//                .setCardInputFont(Typeface.createFromAsset(getAssets(), "fonts/roboto_light.ttf"))//**Optional**

                // setup card input field text color
                .setCardInputTextColor(getResources().getColor(R.color.black))//**Optional**

                // setup card input field text color in case of invalid input
                .setCardInputInvalidTextColor(getResources().getColor(R.color.red))//**Optional**

                // setup card input hint text color
                .setCardInputPlaceholderTextColor(getResources().getColor(R.color.black))//**Optional**

                // setup Switch button Thumb Tint Color in case of Off State
                .setSaveCardSwitchOffThumbTint(getResources().getColor(R.color.gray)) // **Optional**

                // setup Switch button Thumb Tint Color in case of On State
                .setSaveCardSwitchOnThumbTint(getResources().getColor(R.color.vibrant_green)) // **Optional**

                // setup Switch button Track Tint Color in case of Off State
                .setSaveCardSwitchOffTrackTint(getResources().getColor(R.color.gray)) // **Optional**

                // setup Switch button Track Tint Color in case of On State
                .setSaveCardSwitchOnTrackTint(getResources().getColor(R.color.green)) // **Optional**

                // change scan icon
                .setScanIconDrawable(getResources().getDrawable(R.drawable.btn_card_scanner_normal)) // **Optional**

                // setup pay button selector [ background - round corner ]
                .setPayButtonResourceId(R.drawable.btn_pay_selector)

                // setup pay button font type face
//                .setPayButtonFont(Typeface.createFromAsset(getAssets(), "fonts/roboto_light.ttf")) // **Optional**

                // setup pay button disable title color
                .setPayButtonDisabledTitleColor(getResources().getColor(R.color.black)) // **Optional**

                // setup pay button enable title color
                .setPayButtonEnabledTitleColor(getResources().getColor(R.color.white)) // **Optional**

                //setup pay button text size
                .setPayButtonTextSize(14) // **Optional**
                // show/hide pay button loader
                .setPayButtonLoaderVisible(true) // **Optional**

                // show/hide pay button security icon
                .setPayButtonSecurityIconVisible(true) // **Optional**

                // set the text on pay button
                .setPayButtonText("PAY") // **Optional**


                // setup dialog textcolor and textsize
                .setDialogTextColor(getResources().getColor(R.color.black1))     // **Optional**
                .setDialogTextSize(17)                // **Optional**

        ;

    }

    private void configureSDKSession() {

        // Instantiate SDK Session
        if (sdkSession == null) sdkSession = new SDKSession();   //** Required **

        // pass your activity as a session delegate to listen to SDK internal payment process follow
        sdkSession.addSessionDelegate(this);    //** Required **

        // initiate PaymentDataSource
        sdkSession.instantiatePaymentDataSource();    //** Required **

        // set transaction currency associated to your account
        sdkSession.setTransactionCurrency(new TapCurrency("SAR"));    //** Required **

        // Using static CustomerBuilder method available inside TAP Customer Class you can populate TAP Customer object and pass it to SDK
        sdkSession.setCustomer(getCustomer());    //** Required **

        // Set Total Amount. The Total amount will be recalculated according to provided Taxes and Shipping
        sdkSession.setAmount(new BigDecimal(GrandTotal));  //** Required **

        // Set Payment Items array list
        sdkSession.setPaymentItems(new ArrayList<PaymentItem>());// ** Optional ** you can pass empty array list

        // Set Taxes array list
        sdkSession.setTaxes(new ArrayList<Tax>());// ** Optional ** you can pass empty array list

        // Set Shipping array list
        sdkSession.setShipping(new ArrayList());// ** Optional ** you can pass empty array list

        // Post URL
        sdkSession.setPostURL(""); // ** Optional **

        // Payment Description
        sdkSession.setPaymentDescription(""); //** Optional **

        // Payment Extra Info
        sdkSession.setPaymentMetadata(new HashMap());// ** Optional ** you can pass empty array hash map

        // Payment Reference
        sdkSession.setPaymentReference(null); // ** Optional ** you can pass null

        // Payment Statement Descriptor
        sdkSession.setPaymentStatementDescriptor(""); // ** Optional **

        // Enable or Disable Saving Card
        sdkSession.isUserAllowedToSaveCard(true); //  ** Required ** you can pass boolean

        // Enable or Disable 3DSecure
        sdkSession.isRequires3DSecure(true);

        //Set Receipt Settings [SMS - Email ]
        sdkSession.setReceiptSettings(null); // ** Optional ** you can pass Receipt object or null

        // Set Authorize Action
        sdkSession.setAuthorizeAction(null); // ** Optional ** you can pass AuthorizeAction object or null

        sdkSession.setDestination(null); // ** Optional ** you can pass Destinations object or null

        sdkSession.setMerchantID(null); // ** Optional ** you can pass merchant id or null

//        sdkSession.setPaymentType("WEB");   //** Merchant can customize payment options [WEB/CARD] for each transaction or it will show all payment options granted to him.

//        sdkSession.setCardType(CardType.CREDIT); // ** Optional ** you can pass which cardType[CREDIT/DEBIT] you want.By default it loads all available cards for Merchant.

//        sdkSession.setDefaultCardHolderName("TEST TAP"); // ** Optional ** you can pass default CardHolderName of the user .So you don't need to type it.

//        sdkSession.isUserAllowedToEnableCardHolderName(false); //** Optional ** you can enable/ disable  default CardHolderName .

        /**
         * Use this method where ever you want to show TAP SDK Main Screen.
         * This method must be called after you configured SDK as above
         * This method will be used in case of you are not using TAP PayButton in your activity.
         */
//       sdkSession.start(this);
    }

    private void configureSDKMode() {

        /**
         * You have to choose only one Mode of the following modes:
         * Note:-
         *      - In case of using PayButton, then don't call sdkSession.start(this); because the SDK will start when user clicks the tap pay button.
         */
        //////////////////////////////////////////////////////    SDK with UI //////////////////////
        /**
         * 1- Start using  SDK features through SDK main activity (With Tap CARD FORM)
         */
        startSDKWithUI();

    }

    String saveOrPurchaseCard = "";

    private void startSDKWithUI() {
        if (sdkSession != null) {
            if (saveOrPurchaseCard.equals("saveCard")) {
                TransactionMode trx_mode = TransactionMode.SAVE_CARD;
                // set transaction mode [TransactionMode.PURCHASE - TransactionMode.AUTHORIZE_CAPTURE - TransactionMode.SAVE_CARD - TransactionMode.TOKENIZE_CARD ]
                sdkSession.setTransactionMode(trx_mode);    //** Required **
            } else {
                TransactionMode trx_mode = TransactionMode.PURCHASE;
                // set transaction mode [TransactionMode.PURCHASE - TransactionMode.AUTHORIZE_CAPTURE - TransactionMode.SAVE_CARD - TransactionMode.TOKENIZE_CARD ]
                sdkSession.setTransactionMode(trx_mode);    //** Required **
            }

            // if you are not using tap button then start SDK using the following call
            sdkSession.start(this);
        }
    }

    private void initPayButton() {

       PayButtonView payButtonView;
        payButtonView = findViewById(R.id.payButtonId);

        payButtonView.setupFontTypeFace(ThemeObject.getInstance().getPayButtonFont());

        payButtonView.setupTextColor(ThemeObject.getInstance().getPayButtonEnabledTitleColor(),
                ThemeObject.getInstance().getPayButtonDisabledTitleColor());
        //
        payButtonView.getPayButton().setTextSize(ThemeObject.getInstance().getPayButtonTextSize());
        //
        payButtonView.getSecurityIconView().setVisibility(ThemeObject.getInstance().isPayButtSecurityIconVisible() ? View.VISIBLE : View.INVISIBLE);

        payButtonView.setBackgroundSelector(ThemeObject.getInstance().getPayButtonResourceId());

        if (sdkSession != null) {
            TransactionMode trx_mode = sdkSession.getTransactionMode();
            if (trx_mode != null) {

                if (TransactionMode.SAVE_CARD == trx_mode || TransactionMode.SAVE_CARD_NO_UI == trx_mode) {
                    payButtonView.getPayButton().setText(getString(R.string.save_card));
                } else if (TransactionMode.TOKENIZE_CARD == trx_mode || TransactionMode.TOKENIZE_CARD_NO_UI == trx_mode) {
                    payButtonView.getPayButton().setText(getString(R.string.tokenize));
                } else {
                    payButtonView.getPayButton().setText(getString(R.string.pay));
                }
            } else {
                configureSDKMode();
            }
            sdkSession.setButtonView(payButtonView, this, SDK_REQUEST_CODE);
        }


    }

    private void listSavedCards() {
        if (sdkSession != null)
            sdkSession.listAllCards("CUSTOMER_ID", this);
    }

    private Customer getCustomer() {
        return new Customer.CustomerBuilder(null).email("abc@abc.com").firstName("firstname")
                .lastName("lastname").metadata("").phone(new PhoneNumber("965", "69045932"))
                .middleName("middlename").build();
    }

    @Override
    public void paymentSucceed(@NonNull Charge charge) {
        hideDialog();
        GetApiCallToPlceOrder(Common.isStrempty(charge.getId()));
//        charge
        System.out.println("Payment Succeeded : charge status : " + charge.getStatus());
        System.out.println("Payment Succeeded : description : " + charge.getDescription());
        System.out.println("Payment Succeeded : message : " + charge.getResponse().getMessage());
        System.out.println("##############################################################################");
        if (charge.getCard() != null) {
            System.out.println("Payment Succeeded : first six : " + charge.getCard().getFirstSix());
            System.out.println("Payment Succeeded : last four: " + charge.getCard().getLastFour());
            System.out.println("Payment Succeeded : card object : " + charge.getCard().getObject());
            System.out.println("Payment Succeeded : brand : " + charge.getCard().getBrand());
            System.out.println("Payment Succeeded : exp mnth : " + charge.getCard().getExpiry().getMonth());
            System.out.println("Payment Succeeded : exp year : " + charge.getCard().getExpiry().getYear());
        }

        System.out.println("##############################################################################");
        if (charge.getAcquirer() != null) {
            System.out.println("Payment Succeeded : acquirer id : " + charge.getAcquirer().getId());
            System.out.println("Payment Succeeded : acquirer response code : " + charge.getAcquirer().getResponse().getCode());
            System.out.println("Payment Succeeded : acquirer response message: " + charge.getAcquirer().getResponse().getMessage());
        }
        System.out.println("##############################################################################");
        if (charge.getSource() != null) {
            System.out.println("Payment Succeeded : source id: " + charge.getSource().getId());
            System.out.println("Payment Succeeded : source channel: " + charge.getSource().getChannel());
            System.out.println("Payment Succeeded : source object: " + charge.getSource().getObject());
            System.out.println("Payment Succeeded : source payment method: " + charge.getSource().getPaymentMethodStringValue());
            System.out.println("Payment Succeeded : source payment type: " + charge.getSource().getPaymentType());
            System.out.println("Payment Succeeded : source type: " + charge.getSource().getType());
        }

        System.out.println("##############################################################################");
        if (charge.getExpiry() != null) {
            System.out.println("Payment Succeeded : expiry type :" + charge.getExpiry().getType());
            System.out.println("Payment Succeeded : expiry period :" + charge.getExpiry().getPeriod());
        }

//        saveCustomerRefInSession(charge);
        configureSDKSession();
    }

    @Override
    public void paymentFailed(@Nullable Charge charge) {
        hideDialog();
        Toast.makeText(this, ""+ getResources().getString(R.string.Payment_failed), Toast.LENGTH_SHORT).show();
        System.out.println("Payment Failed : " + charge.getStatus());
        System.out.println("Payment Failed : " + charge.getDescription());
        System.out.println("Payment Failed : " + charge.getResponse().getMessage());
    }

    @Override
    public void authorizationSucceed(@NonNull Authorize authorize) {
        System.out.println("Authorize Succeeded : " + authorize.getStatus());
        System.out.println("Authorize Succeeded : " + authorize.getResponse().getMessage());

        if (authorize.getCard() != null) {
            System.out.println("Payment Authorized Succeeded : first six : " + authorize.getCard().getFirstSix());
            System.out.println("Payment Authorized Succeeded : last four: " + authorize.getCard().getLast4());
            System.out.println("Payment Authorized Succeeded : card object : " + authorize.getCard().getObject());
        }

        System.out.println("##############################################################################");
        if (authorize.getAcquirer() != null) {
            System.out.println("Payment Authorized Succeeded : acquirer id : " + authorize.getAcquirer().getId());
            System.out.println("Payment Authorized Succeeded : acquirer response code : " + authorize.getAcquirer().getResponse().getCode());
            System.out.println("Payment Authorized Succeeded : acquirer response message: " + authorize.getAcquirer().getResponse().getMessage());
        }
        System.out.println("##############################################################################");
        if (authorize.getSource() != null) {
            System.out.println("Payment Authorized Succeeded : source id: " + authorize.getSource().getId());
            System.out.println("Payment Authorized Succeeded : source channel: " + authorize.getSource().getChannel());
            System.out.println("Payment Authorized Succeeded : source object: " + authorize.getSource().getObject());
            System.out.println("Payment Authorized Succeeded : source payment method: " + authorize.getSource().getPaymentMethodStringValue());
            System.out.println("Payment Authorized Succeeded : source payment type: " + authorize.getSource().getPaymentType());
            System.out.println("Payment Authorized Succeeded : source type: " + authorize.getSource().getType());
        }

        System.out.println("##############################################################################");
        if (authorize.getExpiry() != null) {
            System.out.println("Payment Authorized Succeeded : expiry type :" + authorize.getExpiry().getType());
            System.out.println("Payment Authorized Succeeded : expiry period :" + authorize.getExpiry().getPeriod());
        }


//        saveCustomerRefInSession(authorize);
        configureSDKSession();
//        showDialog(authorize.getId(),authorize.getResponse().getMessage(),company.tap.gosellapi.R.drawable.ic_checkmark_normal);
    }

    @Override
    public void authorizationFailed(Authorize authorize) {
        System.out.println("Authorize Failed : " + authorize.getStatus());
        System.out.println("Authorize Failed : " + authorize.getDescription());
        System.out.println("Authorize Failed : " + authorize.getResponse().getMessage());
    }

    @Override
    public void cardSaved(@NonNull Charge charge) {
// Cast charge object to SaveCard first to get all the Card info.
        if (charge instanceof SaveCard) {
            System.out.println("Card Saved Succeeded : first six digits : " + ((SaveCard) charge).getCard().getFirstSix() + "  last four :" + ((SaveCard) charge).getCard().getLast4());
        }
        System.out.println("Card Saved Succeeded : " + charge.getStatus());
        System.out.println("Card Saved Succeeded : " + charge.getCard().getBrand());
        System.out.println("Card Saved Succeeded : " + charge.getDescription());
        System.out.println("Card Saved Succeeded : " + charge.getResponse().getMessage());
    }

    @Override
    public void cardSavingFailed(@NonNull Charge charge) {
        System.out.println("Card Saved Failed : " + charge.getStatus());
        System.out.println("Card Saved Failed : " + charge.getDescription());
        System.out.println("Card Saved Failed : " + charge.getResponse().getMessage());
    }

    @Override
    public void cardTokenizedSuccessfully(@NonNull Token token) {
        System.out.println("Card Tokenized Succeeded : ");
        System.out.println("Token card : " + token.getCard().getFirstSix() + " **** " + token.getCard().getLastFour());
        System.out.println("Token card : " + token.getCard().getFingerprint() + " **** " + token.getCard().getFunding());
        System.out.println("Token card : " + token.getCard().getId() + " ****** " + token.getCard().getName());
        System.out.println("Token card : " + token.getCard().getAddress() + " ****** " + token.getCard().getObject());
        System.out.println("Token card : " + token.getCard().getExpirationMonth() + " ****** " + token.getCard().getExpirationYear());
    }

    @Override
    public void savedCardsList(@NonNull CardsList cardsList) {
        if (cardsList != null && cardsList.getCards() != null) {
            System.out.println(" Card List Response Code : " + cardsList.getResponseCode());
            System.out.println(" Card List Top 10 : " + cardsList.getCards().size());
            System.out.println(" Card List Has More : " + cardsList.isHas_more());
            System.out.println("----------------------------------------------");
            for (SavedCard sc : cardsList.getCards()) {
                System.out.println(sc.getBrandName());
            }
            System.out.println("----------------------------------------------");

//            showSavedCardsDialog(cardsList);
        }
    }

    @Override
    public void sdkError(@Nullable GoSellError goSellError) {
        if (goSellError != null) {
            System.out.println("SDK Process Error : " + goSellError.getErrorBody());
            System.out.println("SDK Process Error : " + goSellError.getErrorMessage());
            System.out.println("SDK Process Error : " + goSellError.getErrorCode());
            Log.e("TAP_Payment_sdkError", "GoSellError " + goSellError.getErrorMessage());
        }
    }

    @Override
    public void sessionIsStarting() {
        System.out.println(" Session Is Starting.....");
    }

    @Override
    public void sessionHasStarted() {
        System.out.println(" Session Has Started .......");
    }


    @Override
    public void sessionCancelled() {
        Log.d("MainActivity", "Session Cancelled.........");
    }

    @Override
    public void sessionFailedToStart() {
        Log.d("MainActivity", "Session Failed to start.........");
    }


    @Override
    public void invalidCardDetails() {
        System.out.println(" Card details are invalid....");
    }

    @Override
    public void backendUnknownError(String message) {
        System.out.println("Backend Un-Known error.... : " + message);
    }

    @Override
    public void invalidTransactionMode() {
        System.out.println(" invalidTransactionMode  ......");
    }


    @Override
    public void invalidCustomerID() {
        System.out.println("Invalid Customer ID .......");
    }

    @Override
    public void userEnabledSaveCardOption(boolean saveCardEnabled) {
        System.out.println("userEnabledSaveCardOption :  " + saveCardEnabled);
    }

}