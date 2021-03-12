package ontime.app.customer.doneActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

import company.tap.gosellapi.open.delegate.SessionDelegate;
import okhttp3.RequestBody;
import ontime.app.R;
import ontime.app.customer.Adapter.RvCreditCardListAdapter;
import ontime.app.databinding.ActivityPaymentBinding;
import ontime.app.model.usermain.ExampleUser;
import ontime.app.okhttp.APIcall;
import ontime.app.okhttp.AppConstant;
import ontime.app.sqllight.CardDetails;
import ontime.app.sqllight.DBManager;
import ontime.app.utils.BaseActivity;
import ontime.app.utils.Common;

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

import java.util.ArrayList;

public class PaymentActivity extends BaseActivity implements View.OnClickListener, APIcall.ApiCallListner, SessionDelegate {
    public static ActivityPaymentBinding binding;
    public static DBManager dbManager;
    public static RvCreditCardListAdapter mAdapter;
    public static ArrayList<CardDetails> cardDetails;
    private ProgressDialog dialog;
    private SDKSession sdkSession;
    private PayButtonView payButtonView;
    private final int SDK_REQUEST_CODE = 1001;
    private double amountNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = new DBManager(this);
        dbManager.open();
        if (Common.MERCHANT_TYPE == 1) {
            Common.setSystemBarColor(this, R.color.colorAccent);
//            Common.setSystemBarLight(this);
            binding.llMainTop.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            binding.ivBack.setColorFilter(getResources().getColor(R.color.colorAccent));

        } else {
            Common.setSystemBarColor(this, R.color.super_mart);
//            Common.setSystemBarLight(this);
            binding.ivBack.setColorFilter(getResources().getColor(R.color.super_mart));
            binding.llMainTop.setBackgroundColor(getResources().getColor(R.color.super_mart));
        }
        Cursor cursor = dbManager.fetch();
        LinearLayoutManager mLayoutManager1as = new LinearLayoutManager(getContext());
        mLayoutManager1as.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvList.setLayoutManager(mLayoutManager1as);

        showlistdata(PaymentActivity.this);
        GetApiCallToWallets();

    }

    private void showDialog() {
        dialog = new ProgressDialog(PaymentActivity.this);
        dialog.setMessage(getResources().getString(R.string.Please_wait));
        dialog.setCancelable(false);
        dialog.show();
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    public static void showlistdata(Context context) {
        cardDetails = dbManager.listContacts();
        if (cardDetails.size() > 0) {
            binding.rvList.setVisibility(View.VISIBLE);
            mAdapter = new RvCreditCardListAdapter(context, cardDetails);
            binding.rvList.setItemAnimator(new DefaultItemAnimator());
            binding.rvList.setAdapter(mAdapter);
        } else {
            binding.rvList.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment);
    }

    @Override
    protected void setListener() {
        super.setListener();
        binding.back.setOnClickListener(this);
        binding.addNewCard.setOnClickListener(this);
        binding.llAddPamnet.setOnClickListener(this);

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
        TextView txt_credit_debit_card = dalSelectPayment.findViewById(R.id.txt_credit_debit_card);
        txt_apple_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dalSelectPayment.dismiss();
                if (isConnected()) {
                    //  ApiCallToAddPayment(amount);
                }
            }
        });
        txt_credit_debit_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amountNew = 0.0;
                dalSelectPayment.dismiss();
                if (isConnected()) {
                    showDialog();
                    amountNew = Double.parseDouble(amount);
                    initialiseSDK(Double.parseDouble(amount), "");
                    configureSDKMode();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.add_new_card:
                showAddtoCartItem();
                break;
            case R.id.ll_add_pamnet:
                if (isConnected()) {
                    showAddAmount();
                }
                break;

        }
    }

    private void ApiCallToAddPayment(String amount, String transaction_id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("amount", amount);
            jsonObject.put("transaction_id", transaction_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_WALLET_BALANCE;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_WALLET_BALANCE, this);
    }

    private void GetApiCallToWallets() {
        String url = AppConstant.GET_WALLET;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(false);
        apIcall.execute(url, APIcall.OPERATION_WALLET, this);
    }

    @Override
    public void onStartLoading(int operationCode) {
        if (operationCode == APIcall.OPERATION_WALLET_BALANCE) {
            showDialog();
        }
        if (operationCode == APIcall.OPERATION_WALLET) {
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
            if (operationCode == APIcall.OPERATION_WALLET_BALANCE) {
                hideDialog();
                Gson gson = new Gson();
                ExampleUser exampleUser = gson.fromJson(response, ExampleUser.class);
                if (exampleUser.getStatus() == 200) {
                    GetApiCallToWallets();
                    Toast.makeText(getContext(), "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            if (operationCode == APIcall.OPERATION_WALLET) {
                hideDialog();
                Gson gson = new Gson();
                ExampleUser exampleUser = gson.fromJson(response, ExampleUser.class);
                if (exampleUser.getStatus() == 200) {
                    Float main = Float.valueOf(exampleUser.getResponceData().getBalance());
                    binding.txtWallet.setText("SR " + Common.isStrempty(exampleUser.getResponceData().getBalance()));
//                    Toast.makeText(getContext(), "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "" + exampleUser.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void showAddAmount() {
        final Dialog dialogm = new Dialog(this);
        dialogm.setCancelable(true);
        dialogm.setContentView(R.layout.pop_add_amount);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogm.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        dialogm.getWindow().setAttributes(lp);
        dialogm.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialogm.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ImageView bt_close = dialogm.findViewById(R.id.bt_close);
        final EditText ed_card_number = dialogm.findViewById(R.id.ed_card_number);
        AppCompatButton bt_add_now = dialogm.findViewById(R.id.bt_add_now);
        TextView txt_title = dialogm.findViewById(R.id.txt_title);
        if (Common.MERCHANT_TYPE == 1) {
            txt_title.setTextColor(getResources().getColor(R.color.colorAccent));
            bt_close.setColorFilter(getResources().getColor(R.color.colorAccent));
            bt_add_now.setBackground(getResources().getDrawable(R.drawable.btn_goldenrect));

        } else {
            txt_title.setTextColor(getResources().getColor(R.color.super_mart));
            bt_close.setColorFilter(getResources().getColor(R.color.super_mart));
            bt_add_now.setBackground(getResources().getDrawable(R.drawable.btn_goldenrectorange));
        }
        bt_add_now.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if (!Common.edvalidateName(ed_card_number.getText().toString(), ed_card_number, getResources().getString(R.string.v_enter_card_no))) {
                    return;
                }
                if (isConnected()) {
                    showDialog();
                    amountNew = Double.parseDouble(ed_card_number.getText().toString());
                    initialiseSDK(Double.parseDouble(ed_card_number.getText().toString()), "");
                    configureSDKMode();
                }
                // showDialogSelectPayment(ed_card_number.getText().toString());

                dialogm.dismiss();
            }
        });
        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogm.dismiss();
            }
        });
        dialogm.show();
    }

    public void showAddtoCartItem() {
        final Dialog dialogm = new Dialog(this);
        dialogm.setCancelable(true);
        dialogm.setContentView(R.layout.pop_add_card);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogm.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        dialogm.getWindow().setAttributes(lp);
        dialogm.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialogm.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ImageView bt_close = dialogm.findViewById(R.id.bt_close);
        final EditText ed_card_number = dialogm.findViewById(R.id.ed_card_number);
        final EditText ed_expiry_date = dialogm.findViewById(R.id.ed_expiry_date);
        final EditText ed_name_on_card = dialogm.findViewById(R.id.ed_name_on_card);
        AppCompatButton bt_add_now = dialogm.findViewById(R.id.bt_add_now);
        TextView txt_title = dialogm.findViewById(R.id.txt_title);
        if (Common.MERCHANT_TYPE == 1) {
            txt_title.setTextColor(getResources().getColor(R.color.colorAccent));
            bt_close.setColorFilter(getResources().getColor(R.color.colorAccent));
            bt_add_now.setBackground(getResources().getDrawable(R.drawable.btn_goldenrect));

        } else {
            txt_title.setTextColor(getResources().getColor(R.color.super_mart));
            bt_close.setColorFilter(getResources().getColor(R.color.super_mart));
            bt_add_now.setBackground(getResources().getDrawable(R.drawable.btn_goldenrectorange));
        }
        bt_add_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Common.edvalidateName(ed_card_number.getText().toString(), ed_card_number, getResources().getString(R.string.v_enter_card_no)) |
                        !Common.edvalidateName(ed_expiry_date.getText().toString(), ed_expiry_date, getResources().getString(R.string.v_enter_card_date)) |
                        !Common.edvalidateName(ed_name_on_card.getText().toString(), ed_name_on_card, getResources().getString(R.string.v_enter_card_hoder))) {
                    return;
                }
                dbManager.insert(ed_name_on_card.getText().toString(), ed_card_number.getText().toString(), ed_expiry_date.getText().toString());
                dialogm.dismiss();
                showlistdata(PaymentActivity.this);
            }
        });
        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogm.dismiss();
            }
        });
        dialogm.show();
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
        hideDialog();
        ApiCallToAddPayment(String.valueOf(charge.getAmount()), Common.isStrempty(charge.getCustomer().getIdentifier()));
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


}