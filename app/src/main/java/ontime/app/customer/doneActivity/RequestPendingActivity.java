package ontime.app.customer.doneActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import company.tap.gosellapi.GoSellSDK;
import company.tap.gosellapi.internal.api.callbacks.GoSellError;
import company.tap.gosellapi.internal.api.enums.ChargeStatus;
import company.tap.gosellapi.internal.api.models.Authorize;
import company.tap.gosellapi.internal.api.models.Card;
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
import ontime.app.R;
import ontime.app.customer.Adapter.RvRestOrderCartListAdapter;
import ontime.app.customer.Adapter.RvRestOrderPeCartListAdapter;
import ontime.app.customer.Adapter.RvRestPenddingListAdapter;
import ontime.app.databinding.ActivityRequestpendingBinding;
import ontime.app.model.restaurantlist.RestaurantExample;
import ontime.app.model.usermain.ExampleUser;
import ontime.app.model.usermain.UserCartItemDetail;
import ontime.app.okhttp.APIcall;
import ontime.app.okhttp.AppConstant;
import ontime.app.restaurant.model.readerOrder.OrderDetail;
import ontime.app.utils.BaseActivity;
import ontime.app.utils.Common;


public class RequestPendingActivity extends BaseActivity implements View.OnClickListener, APIcall.ApiCallListner, SessionDelegate {

    public static String timeLeftFormatted;
    ProgressDialog dialog;
    private final int SDK_REQUEST_CODE = 1001;
    ActivityRequestpendingBinding binding;
    private SDKSession sdkSession;
    private PayButtonView payButtonView;
    public Runnable runnable;
    public Handler handler = new Handler();
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private double amountNew;
    Date event_date;
    float WALLET_AMOUNT = 0;
    float AMOUNT = 0;
    int PAYMENT_TYPE = 0;
    Date c_cancle_date;
    Date current_dateas;

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_requestpending);
    }

    @Override
    protected void setListener() {
        super.setListener();
        binding.back.setOnClickListener(this);
        binding.btCancel.setOnClickListener(this);
        binding.btPayNow.setOnClickListener(this);
    }

    private void GetApiCallToWallets() {
        String url = AppConstant.GET_WALLET;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(false);
        apIcall.execute(url, APIcall.OPERATION_WALLET, this);
    }

    private void GetAPICallUserpaymentFail(String transaction_id, double amount, Card card, ChargeStatus payment_status) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("payment_type", PAYMENT_TYPE);
            jsonObject.put("transaction_amount", String.valueOf(amount));
            jsonObject.put("transaction_type", card);
            jsonObject.put("payment_status", payment_status);
            jsonObject.put("order_id", Common.ORDERPROCCESSING_ORDER.getId());
            jsonObject.put("transaction_id", transaction_id);
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

    private void GetAPICallUserpaymentPost(String transaction_id, String amount) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("payment_type", PAYMENT_TYPE);
            jsonObject.put("transaction_amount", amount);
            jsonObject.put("order_id", Common.ORDERPROCCESSING_ORDER.getId());
            jsonObject.put("transaction_id", transaction_id);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.bt_cancel:
                if (isConnected()) {
                    DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    Calendar cal = Calendar.getInstance();
                    Date date_cancels = null;
                    try {
                        date_cancels = timeFormat.parse(Common.ORDERPROCCESSING_ORDER.getCreatedAt());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    cal.setTime(date_cancels);
                    cal.add(Calendar.MINUTE, 2);
                    timeFormat.format(cal.getTime());
                    c_cancle_date = cal.getTime();
                    current_dateas = new Date();
                    if (!current_dateas.after(c_cancle_date)) {
                        if (Common.ORDERPROCCESSING_ORDER.getDeliveryStatus() == 0) {
                            APICallUserCancleOrder(Common.ORDERPROCCESSING_ORDER.getId());
                        }
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.time_out_for_order_cancel), Toast.LENGTH_SHORT).show();

                    }


                }
                break;
            case R.id.bt_pay_now:
                if (isConnected()) {
                    //  showDialogSelectPayment(Common.ORDERPROCCESSING_ORDER.getTotalPrice());
                    if (!Common.ORDERPROCCESSING_ORDER.getPaymentStatus().equals("success")) {
                        showDialogSelectPayment(Common.ORDERPROCCESSING_ORDER.getTotalPrice());
//                        amountNew = Double.parseDouble(Common.ORDERPROCCESSING_ORDER.getTotalPrice());
//                        initialiseSDK(Double.parseDouble(Common.ORDERPROCCESSING_ORDER.getTotalPrice()), "");
//                        configureSDKMode();
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.payment_pay), Toast.LENGTH_SHORT).show();
                    }

                }
                break;
        }
    }

    public void APICallUserCancleOrder(int order_id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("order_id", order_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_USER_CANCEL_ORDER;
        APIcall apIcall = new APIcall(this);
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_USER_CANCEL_ORDER, this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Common.ORDERPROCCESSING_ORDER.getRestaurant().getType() == 1) {
            Common.setSystemBarColor(this, R.color.colorAccent);
            binding.btPayNow.setBackgroundColor(getResources().getColor(R.color.red));
            binding.btCancel.setBackgroundColor(getResources().getColor(R.color.red));
            binding.txtName.setTextColor(getResources().getColor(R.color.red));
        } else {
            Common.setSystemBarColor(this, R.color.super_mart);
            binding.btPayNow.setBackgroundColor(getResources().getColor(R.color.yellow));
            binding.btCancel.setBackgroundColor(getResources().getColor(R.color.yellow));
            binding.txtName.setTextColor(getResources().getColor(R.color.yellow));
        }
        LinearLayoutManager mLayoutManager1as = new LinearLayoutManager(getContext());
        mLayoutManager1as.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvList.setLayoutManager(mLayoutManager1as);
        APICallUserOrderDetails(Common.ORDERPROCCESSING_ORDER.getId());

        try {
//            getResources().getString(R.string.Qty) + " : "+
            binding.txtQty.setText(Common.isStrempty(String.valueOf(Common.ORDERPROCCESSING_ORDER.getOrderDetail().get(0).getQuantity())));
            binding.txtTotal.setText("SR " + Common.isStrempty(Common.ORDERPROCCESSING_ORDER.getTotalPrice()));
            binding.txtName.setText(Common.isStrempty(Common.ORDERPROCCESSING_ORDER.getRestaurant().getName()));

            if ((Common.ORDERPROCCESSING_ORDER.getOrderStatus() == 0)) {
                binding.txtOrderStatus.setText(getResources().getString(R.string.status_new));
            } else if ((Common.ORDERPROCCESSING_ORDER.getOrderStatus() == 1)) {
                binding.txtOrderStatus.setText(getResources().getString(R.string.status_processing));
            } else if ((Common.ORDERPROCCESSING_ORDER.getOrderStatus() == 2)) {
                binding.txtOrderStatus.setText(getResources().getString(R.string.status_cancelledByuser));
            } else if ((Common.ORDERPROCCESSING_ORDER.getOrderStatus() == 3)) {
                binding.txtOrderStatus.setText(getResources().getString(R.string.status_cancelled));
            } else if ((Common.ORDERPROCCESSING_ORDER.getOrderStatus() == 4)) {
                binding.txtOrderStatus.setText(getResources().getString(R.string.status_completed));
            } else if ((Common.ORDERPROCCESSING_ORDER.getOrderStatus() == 99)) {
                binding.txtOrderStatus.setText(getResources().getString(R.string.status_unknown));
            }


//            if(Common.isStrempty(Common.ORDERPROCCESSING_ORDER.getPaymentStatus()).equals("pending")){
//                binding.txtOrderStatus.setTextColor(getResources().getColor(R.color.red));
//                binding.txtOrderStatus.setText(getResources().getString(R.string.status) + " : " + Common.isStrempty(Common.ORDERPROCCESSING_ORDER.getPaymentStatus()));
//            }else {
//                binding.txtOrderStatus.setTextColor(getResources().getColor(R.color.green));
//                binding.txtOrderStatus.setText(getResources().getString(R.string.status) + " : " + Common.isStrempty(Common.ORDERPROCCESSING_ORDER.getPaymentStatus()));
//            }

            Glide.with(this).load(Common.ORDERPROCCESSING_ORDER.getRestaurant().getImage()).centerCrop().placeholder(R.mipmap.image_placeholder_default).into(binding.ivRestMenu);


            AMOUNT = Float.parseFloat(Common.ORDERPROCCESSING_ORDER.getTotalPrice());
            GetApiCallToWallets();
            Calendar c = Calendar.getInstance();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date ss = new Date();
            try {
                ss = dateFormat.parse(Common.ORDERPROCCESSING_ORDER.getCountdownTime());
                c.setTime(ss);
                c.add(Calendar.SECOND, Integer.parseInt(String.valueOf(parseTimeStringToSeconds(Common.ORDERPROCCESSING_ORDER.getDeliveryTime()))));
                event_date = new Date();
                dateFormat.setTimeZone(TimeZone.getDefault());
                event_date = c.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            GetCountDownStart();

            DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Calendar cal = Calendar.getInstance();
            Date date_cancels = null;
            try {
                date_cancels = timeFormat.parse(Common.ORDERPROCCESSING_ORDER.getCreatedAt());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            cal.setTime(date_cancels);
            cal.add(Calendar.MINUTE, 2);
            timeFormat.format(cal.getTime());
            c_cancle_date = cal.getTime();
            current_dateas = new Date();
            if (!current_dateas.after(c_cancle_date)) {
                if (Common.ORDERPROCCESSING_ORDER.getOrderStatus() == 0) {
                    binding.btCancel.setEnabled(true);
                    binding.btCancel.setClickable(true);
                } else {
                    binding.btCancel.setEnabled(false);
                    binding.btCancel.setClickable(false);
                }

            } else {
                binding.btCancel.setEnabled(false);
                binding.btCancel.setClickable(false);
                //  Toast.makeText(this, getResources().getString(R.string.time_out_for_order_cancel), Toast.LENGTH_SHORT).show();

            }
            //    GetCountDownStartCancle();
        } catch (Exception e) {

        }


        if (!Common.ORDERPROCCESSING_ORDER.getPaymentStatus().equals("success")) {
           if ((Common.ORDERPROCCESSING_ORDER.getPaymentType().equals("4"))) {
               binding.btPayNow.setEnabled(false);
               binding.btPayNow.setClickable(false);
            }
            binding.btPayNow.setEnabled(true);
            binding.btPayNow.setClickable(true);
        } else {
            binding.btPayNow.setEnabled(false);
            binding.btPayNow.setClickable(false);

        }

    }

    private void APICallUserOrderDetails(Integer id) {
        Common.hideKeyboard(getActivity());
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("order_id", id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_USER_ORDERDETAIL;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_USER_ORDERDETAIL, this);
    }

    private void GetCountDownStartCancle() {
        runnable = new Runnable() {
            @Override
            public void run() {

            }
        };
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//            }
//        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    public static long parseTime(String str) throws NumberFormatException {
        if (str == null)
            throw new NumberFormatException("parseTimeString null str");
        if (str.isEmpty())
            throw new NumberFormatException("parseTimeString empty str");

        int h = 0;
        int m, s;
        String units[] = str.split(":");
        assert (units.length == 2 || units.length == 3);
        switch (units.length) {
            case 2:
                // mm:ss
                m = Integer.parseInt(units[0]);
                s = Integer.parseInt(units[1]);
                break;

            case 3:
                // hh:mm:ss
                h = Integer.parseInt(units[0]);
                m = Integer.parseInt(units[1]);
                s = Integer.parseInt(units[2]);
                break;

            default:
                throw new NumberFormatException("parseTimeString failed:" + str);
        }
        if (m < 0 || m > 60 || s < 0 || s > 60 || h < 0)
            throw new NumberFormatException("parseTimeString range error:" + str);
        return h * 3600 + m * 60 + s;
    }

    public static long parseTimeStringToSeconds(String str) {
        try {
            return parseTime(str);
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }

    private void GetCountDownStart() {
        runnable = new Runnable() {
            @Override
            public void run() {
                try {

                    handler.postDelayed(this, 500);
                    Date current_dateas = new Date();
                    if (!current_dateas.after(event_date)) {
                        long diff = event_date.getTime() - current_dateas.getTime();
                        long Days = diff / (24 * 60 * 60 * 1000);
                        long Hours = diff / (60 * 60 * 1000) % 24;
                        long Minutes = diff / (60 * 1000) % 60;
                        long Seconds = diff / 1000 % 60;
                        //
                        timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", Hours, Minutes, Seconds);

                        char ch1 = timeLeftFormatted.charAt(0);
                        char ch2 = timeLeftFormatted.charAt(1);

                        char ch3 = timeLeftFormatted.charAt(3);
                        char ch4 = timeLeftFormatted.charAt(4);

                        char ch5 = timeLeftFormatted.charAt(6);
                        char ch6 = timeLeftFormatted.charAt(7);

                        if (binding.txtHfirst.getText().hashCode() != ch1) {
                            binding.txtHfirst.setText(Character.toString(ch1));
                        }
                        if (binding.txtHfsecond.getText().hashCode() != ch2) {
                            binding.txtHfsecond.setText(Character.toString(ch2));
                        }
                        if (binding.txtMfirst.getText().hashCode() != ch3) {
                            binding.txtMfirst.setText(Character.toString(ch3));
                        }
                        if (binding.txtMSecond.getText().hashCode() != ch4) {
                            binding.txtMSecond.setText(Character.toString(ch4));
                        }
                        if (binding.txtSFirst.getText().hashCode() != ch5) {
                            binding.txtSFirst.setText(Character.toString(ch5));
                        }
                        if (binding.txtSSecond.getText().hashCode() != ch6) {
                            binding.txtSSecond.setText(Character.toString(ch6));
                        }
                    } else {

                        handler.removeCallbacks(runnable);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

    private void showDialog() {
        dialog = new ProgressDialog(RequestPendingActivity.this);
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
        if (operationCode == APIcall.OPERATION_USER_CANCEL_ORDER) {
            showDialog();
        }
        if (operationCode == APIcall.OPERATION_USER_PAYMENT_FAIL) {
            showDialog();
        }
        if (operationCode == APIcall.OPERATION_USER_PAYMENT_POST) {
            showDialog();
        }
        if (operationCode == APIcall.OPERATION_WALLET) {
            showDialog();
        }
//        if (operationCode == APIcall.OPERATION_USER_ORDERDETAIL) {
//            showDialog();
//        }
    }

    @Override
    public void onProgress(int operationCode, int progress) {

    }

    @Override
    public void onSuccess(int operationCode, String response, Object customData) {
        try {
            if (operationCode == APIcall.OPERATION_WALLET) {
                hideDialog();
                Gson gson = new Gson();
                ExampleUser exampleUser = gson.fromJson(response, ExampleUser.class);
                if (exampleUser.getStatus() == 200) {
                    Float main = Float.valueOf(exampleUser.getResponceData().getBalance());
                    WALLET_AMOUNT = Float.valueOf(exampleUser.getResponceData().getBalance());

                } else {
                    Toast.makeText(getContext(), "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            if (operationCode == APIcall.OPERATION_USER_CANCEL_ORDER) {
                hideDialog();
                Gson gson = new Gson();
                ExampleUser exampleUser = gson.fromJson(response, ExampleUser.class);
                if (exampleUser.getStatus() == 200) {
                    Toast.makeText(RequestPendingActivity.this, "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    Toast.makeText(RequestPendingActivity.this, "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            if (operationCode == APIcall.OPERATION_USER_PAYMENT_FAIL) {
                hideDialog();
                JSONObject root = null;
                try {
                    root = new JSONObject(response);
//                    Toast.makeText(OrderSummaryActivity.this, "" + root.getString("message"), Toast.LENGTH_SHORT).show();
                    if (root.getString("status").equals("200")) {
                        Common.ORDERPROCCESSING_ORDER.setPaymentStatus("pending");
                    }
                    onBackPressed();
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
                    if (root.getString("status").equals("200")) {
                        Common.ORDERPROCCESSING_ORDER.setPaymentStatus("success");
                    }

                    onBackPressed();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (operationCode == APIcall.OPERATION_USER_ORDERDETAIL) {
                hideDialog();
                try {
                    Gson gson = new Gson();
                    RestaurantExample exampleUser = gson.fromJson(response, RestaurantExample.class);

                    if (exampleUser.getStatus() == 200) {
                        List<OrderDetail> orderDetails = exampleUser.getResponceData().get(0).getOrderDetail();
                        RvRestOrderPeCartListAdapter mMAdapter = new RvRestOrderPeCartListAdapter(getContext(), orderDetails, Common.ORDERPROCCESSING_ORDER.getOrderStatus());
                        binding.rvList.setItemAnimator(new DefaultItemAnimator());
                        binding.rvList.setAdapter(mMAdapter);
                        //showcustomedialog(exampleUser.getResponceData().get(0).getId(), exampleUser.getResponceData().get(0).getRestaurant());
                    }
                } catch (Exception e) {

                }
            }
            hideDialog();
        } catch (Exception e) {
            hideDialog();
        }
    }

    @Override
    public void onFail(int operationCode, String response) {

    }

    private void configureApp() {
        GoSellSDK.init(this, "sk_test_fC4J0RzAobEkZuBaFVwOyQmL", "ontime.app");  // to be replaced by merchant
//        GoSellSDK.setLocale("en");//  language to be set by merchant
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
//        configureSDKMode();

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
        sdkSession.setAmount(new BigDecimal(amountNew));  //** Required **

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
//        sdkSession.start(this);
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
        GetAPICallUserpaymentPost(charge.getId(), String.valueOf(charge.getAmount()));
        hideDialog();
//        ApiCallToAddPayment(String.valueOf(charge.getAmount()));
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
        Toast.makeText(this, "" + getResources().getString(R.string.Payment_failed), Toast.LENGTH_SHORT).show();
        GetAPICallUserpaymentFail(charge.getId(), charge.getAmount(), charge.getCard(), charge.getStatus());
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
        hideDialog();
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
        hideDialog();
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
        hideDialog();
        if (goSellError != null) {
            System.out.println("SDK Process Error : " + goSellError.getErrorBody());
            System.out.println("SDK Process Error : " + goSellError.getErrorMessage());
            System.out.println("SDK Process Error : " + goSellError.getErrorCode());
            Log.e("TAP_Payment_sdkError", "GoSellError " + goSellError.getErrorMessage());
        }
    }

    @Override
    public void sessionIsStarting() {
        hideDialog();
        System.out.println(" Session Is Starting.....");
    }

    @Override
    public void sessionHasStarted() {
        hideDialog();
        System.out.println(" Session Has Started .......");
    }


    @Override
    public void sessionCancelled() {
        hideDialog();
        Log.d("MainActivity", "Session Cancelled.........");
    }

    @Override
    public void sessionFailedToStart() {
        hideDialog();
        Log.d("MainActivity", "Session Failed to start.........");
    }


    @Override
    public void invalidCardDetails() {
        hideDialog();
        System.out.println(" Card details are invalid....");
    }

    @Override
    public void backendUnknownError(String message) {
        hideDialog();
        System.out.println("Backend Un-Known error.... : " + message);
    }

    @Override
    public void invalidTransactionMode() {
        hideDialog();
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

    @SuppressLint("SetTextI18n")
    private void showDialogSelectPayment(String amount) {
        final Dialog dalSelectPayment = new Dialog(this);
        dalSelectPayment.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dalSelectPayment.setContentView(R.layout.pop_select_paymnet_methord);
        dalSelectPayment.setCancelable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dalSelectPayment.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        TextView txt_apple_pay = dalSelectPayment.findViewById(R.id.txt_apple_pay);
        AppBarLayout al_top = dalSelectPayment.findViewById(R.id.al_top);
        txt_apple_pay.setText(getResources().getString(R.string.wallet) + " ( " + String.valueOf(WALLET_AMOUNT) + " )");
        TextView txt_credit_debit_card = dalSelectPayment.findViewById(R.id.txt_credit_debit_card);
        Button bt_sub_cancel = dalSelectPayment.findViewById(R.id.bt_sub_cancel);
        if (Common.ORDERPROCCESSING_ORDER.getRestaurant().getType() == 1) {
            bt_sub_cancel.setBackgroundColor(getResources().getColor(R.color.red));
            al_top.setBackgroundColor(getResources().getColor(R.color.red));
        } else {
            bt_sub_cancel.setBackgroundColor(getResources().getColor(R.color.yellow));
            al_top.setBackgroundColor(getResources().getColor(R.color.yellow));
        }
        txt_apple_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected()) {
                    if (WALLET_AMOUNT < AMOUNT) {
                        Toast.makeText(RequestPendingActivity.this, getResources().getString(R.string.lowest), Toast.LENGTH_SHORT).show();
                        dalSelectPayment.dismiss();
                        return;
                    }
                    PAYMENT_TYPE = 2;
                    GetAPICallUserpaymentPost("0", amount);
                    dalSelectPayment.dismiss();
                    //  ApiCallToAddPayment(amount);
                }
            }
        });
        txt_credit_debit_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amountNew = 0.0;
                if (isConnected()) {
                    showDialog();
                    PAYMENT_TYPE = 2;
                    amountNew = Double.parseDouble(amount);
                    initialiseSDK(Double.parseDouble(amount), "");
                    configureSDKMode();
                    dalSelectPayment.dismiss();
                }

            }
        });
        dalSelectPayment.findViewById(R.id.bt_sub_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dalSelectPayment.dismiss();
            }
        });
        dalSelectPayment.show();
        dalSelectPayment.getWindow().setAttributes(lp);
    }

}
