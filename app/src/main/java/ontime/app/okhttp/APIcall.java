package ontime.app.okhttp;


import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import okhttp3.logging.HttpLoggingInterceptor;
import ontime.app.utils.Common;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ontime.app.utils.LanguageManager;

public class APIcall {

    public static int OPERATION_LOGIN_NEW = 100001;
    public static int OPERATION_RESTAURANT_LIST = 100002;
    public static int OPERATION_RESTAURANT_PROFILE = 100003;
    public static int OPERATION_RESTAURANT_MENU_ITEM = 100004;
    public static int OPERATION_RESTAURANT_MENU_ITEM_DETAIL = 100005;
    public static int OPERATION_RESTAURANT_MY_CART = 100006;
    public static int OPERATION_ADD_TO_CART = 100007;
    public static int OPERATION_RESTAURANT_DELETE_CART = 100008;
    public static int OPERATION_USER_PROFILE = 100009;
    public static int OPERATION_USER_LOGOUT = 100010;
    public static int OPERATION_CONTECT_US = 100011;
    public static int OPERATION_ABOUT_US = 100012;
    public static int OPERATION_ITEM_BY_CATEGORY = 100013;
    public static int OPERATION_CHNAGE_PASSWORD = 100014;
    public static int OPERATION_USER_UPDATE_PROFILE = 100015;
    public static int OPERATION_CHECK_NUMBER_ISEXIST = 100016;
    public static int OPERATION_FORGOT_PASSWORD = 100017;
    public static int OPERATION_REGISTET = 100018;
    public static int OPERATION_USER_UPLOAD_IMAGE = 100019;
    public static int OPERATION_ORDER_LIST = 100020;
    public static int OPERATION_USER_DASHBOARD = 100021;
    public static int OPERATION_RESTAURANT_DELETE = 100022;
    public static int OPERATION_MENU_ITEM_UPDATE_DETAIL = 100023;



    public static int OPERATION_READER_ORDERLIST = 100024;
    public static int OPERATION_ADVERTISEMENTS = 100025;
    public static int OPERATION_PLACE_ORDER = 100026;
    public static int OPERATION_DISCOUNT = 100027;
    public static int OPERATION_WALLET = 100028;


    public static int OPERATION_USER_CANCEL_ORDER = 100029;
    public static int OPERATION_ORDER_ACCEPT = 100030;
    public static int OPERATION_READER_ORDER_REJECT = 100031;
    public static int OPERATION_READER_ORDER_COMPLETE = 100032;
    public static int OPERATION_WALLET_BALANCE = 100033;
    public static int OPERATION_NOTIFICATION = 100034;

    public static int OPERATION_UPDATE_DEVICE_TOKEN= 100035;
    public static int OPERATION_READER_LOGOUT= 100036;
	
    public static int OPERATION_READER_DELETE_NOTIFICATION= 100037;
    public static int OPERATION_USER_DELETE_NOTIFICATION= 100038;
    public static int OPERATION_USER_RESTAURNT_RATING= 100039;
    public static int OPERATION_USER_PAYMENT_FAIL= 100040;
    public static int OPERATION_USER_PAYMENT_POST= 100041;
    public static int OPERATION_USER_SERRCH_RESRAURANTS= 100042;

    public static int OPERATION_USER_MSG= 100043;
    public static int OPERATION_USER_RATE_US= 100044;

    public static int OPERATION_ADD_TO_CART_SUPER= 100045;
    public static int OPERATION_SUPER_MENU_ITEM_UPDATE_DETAIL = 100046;
    public static int OPERATION_USER_ORDERDETAIL = 100047;
    public static int  OPERATION_LOGIN_UPDATE_OTP = 100048;





    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private ApiCallListner apiCallListner;
    Context moContext;
    private FormBody.Builder postBuilder;

    public APIcall(Context context) {
        moContext = context;
    }

    SharedPreferenceManagerFile sharedPreferenceManagerFile;
    private boolean isPost;

    private RequestBody body;

    public void setBody(RequestBody body) {
        this.body = body;
    }

    public void isPost(boolean isPost) {
        this.isPost = isPost;
    }

    public void setPostBuilder(FormBody.Builder postBuilder) {
        this.postBuilder = postBuilder;
    }

    private String tempSessionGUID;

    public void setTempSessionId(String tempSessionGUID) {
        this.tempSessionGUID = tempSessionGUID;
    }

    public interface ApiCallListner {
        public void onStartLoading(int operationCode);

        public void onProgress(int operationCode, int progress);

        public void onSuccess(int operationCode, String response, Object customData);

        public void onFail(int operationCode, String response);
    }

    private Object customData;

    public void setCustomData(Object customData) {
        this.customData = customData;
    }

    private int operationCode;
    private String url;
    private CallApiExecute callApiExecute;

    public void execute(String url, int operationCode, ApiCallListner apiCallListner) {
        this.operationCode = operationCode;
        this.url = url;
        this.url = this.url.replaceAll(" ", "%20");
        this.apiCallListner = apiCallListner;
        this.apiCallListner.onStartLoading(operationCode);
        callApiExecute = new CallApiExecute();
        callApiExecute.execute();
    }

    private class CallApiExecute extends AsyncTask<String, String, String> {

        String errorMsg = "Offers not found";

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if (!url.contains("http")) {
                    url = AppConstant.BASE_URL_NEW + url;
                }
                sharedPreferenceManagerFile = new SharedPreferenceManagerFile(moContext);
                String userAgent = System.getProperty("http.agent");
                Log.i("userAgent", "userAgent:::" + userAgent);
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
                okHttpBuilder.connectTimeout(60000, TimeUnit.MILLISECONDS);
                okHttpBuilder.readTimeout(60000, TimeUnit.MILLISECONDS);
                OkHttpClient client = okHttpBuilder.build();
               // okHttpBuilder.addInterceptor(interceptor);
                FormBody formBody = null;
                Request request;
                if (isPost && postBuilder != null) {
                    Log.i("CallApiExecute", "API Url:" + url);
                }

                Request.Builder reqBuilder = new Request.Builder();
                String sessionGUID = sharedPreferenceManagerFile.getFromStringSharedPreference(moContext,SharedPreferenceManagerFile.SESSION_GUID);
//                String sessionGUID = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjRlNWEwYTg1MWE1NmFmNTJlOGNkZjk5NGJjNzgzNDA1YTlhYmE3MWZlNjdjMGUzZTc3MzE3YmZhMzhkZmU1ZGQwY2U5MjU1MDM3MTEwZjU4In0.eyJhdWQiOiI3IiwianRpIjoiNGU1YTBhODUxYTU2YWY1MmU4Y2RmOTk0YmM3ODM0MDVhOWFiYTcxZmU2N2MwZTNlNzczMTdiZmEzOGRmZTVkZDBjZTkyNTUwMzcxMTBmNTgiLCJpYXQiOjE2MDg4ODg0NTcsIm5iZiI6MTYwODg4ODQ1NywiZXhwIjoxNjQwNDI0NDU3LCJzdWIiOiIyMyIsInNjb3BlcyI6W119.p9z8nzj8BNw3kX75a_YtsyiNEwLjMrTGXUKW9kdXCLRfzaHVAm5y_ztFSEaJBjccqLDi-4cKhO33vR0b46bF92LCQu-MgntB-HFZYrdvd_yAdgGKlVs2OdibpvWMiHbp5xqpaHivXfD0YO7v7D5F-z4jFg0uM0pU2o0teKljVTkZ2CsfIlUdnbIwH2Nqmqs1PWduBaO5zvqZxaBCh0LP2yXCeNJjgRco6b-Fc0Tv1viRq7f5JOLy12EbqvFDGCZTCTQKo56b1dnxhKNMA4uCB-EVn5EQUnNUVGo1z5OJxgkF7XZg0Pfqn8SlqAgpwuwK7UJbIeqJYO_o-ucOrF8fgrmnnnMcNXfD2lgMZRQ9BTeuZsQY-ElhyHZFYpkstqfe0-v0Seuc1tmWwctPpaJHj1shzHQfqBsAuH2_WOrp0oq4S-9pyjqsdJBCoG6b03Zce5j2sMg73mY1D9fThSlrCNeP8_ms8AK-LxImpCVA7xW7XWuZ5e1tAQpeyldLM9BQ262_oC97bO7m-AJQnh5aIt4XLB1nPhqP4G8ixrnjdJxqK6pa4NujOlW0L6d5gAkrx-Ebcr4YT2GPBrFZSSnZZbjVS2dsAzpSi2AYyk7HR2OhqTpZ2c3w40CHFOlMq0Hq-nZxpjktIlgFQS5LNpgJDu31xN1NltNVnzEaRZt_Hm8";
                Log.e("sessionGUID", "" + sessionGUID);
				String sessionLanguage = sharedPreferenceManagerFile.getFromStringSharedPreference(moContext, LanguageManager.LANGUAGE_KEY);
                if (!Common.isEmpty(sessionGUID)) {
                    reqBuilder.addHeader(AppConstant.REQUEST_HEADER_TOKEN, sessionGUID);
                    reqBuilder.addHeader("Accept-Language", sessionLanguage);
                    reqBuilder.addHeader("X-localization", sessionLanguage);
//                    reqBuilder.addHeader("Accept", "application/json");
//                    reqBuilder.addHeader("Content-Type", "application/json");
                    reqBuilder.addHeader("app-key", "#%2$#12fd$%^fg5");
                } else

                if (!Common.isEmpty(tempSessionGUID)) {
                    reqBuilder.addHeader(AppConstant.REQUEST_HEADER_TOKEN, tempSessionGUID);
                    reqBuilder.addHeader("Accept-Language", sessionLanguage);
                    reqBuilder.addHeader("X-localization", sessionLanguage);
//                    reqBuilder.addHeader("Accept", "application/json");
//                    reqBuilder.addHeader("Content-Type", "application/json");
                    reqBuilder.addHeader("app-key", "#%2$#12fd$%^fg5");
                }
                if (formBody != null) {
                    request = reqBuilder.url(url).post(formBody).build();
                    if (false && !TextUtils.isEmpty(sessionGUID)) {
                    } else {

                    }
                } else if (body != null) {
                    request = reqBuilder.url(url).post(body).build();
                } else {
                    request = reqBuilder.url(url).build();
                }
                Response response = null;
                String responseStr = null;
                try {
                    response = client.newCall(request).execute();
                    responseStr = response.body().string();
                    Log.i("CallApiExecute", "CallApiExecute Response got:" + responseStr);
                    return responseStr;
                } catch (IOException e) {
                    Log.i("CallApiExecute", "Exception Message:" + e.getMessage());
                    e.printStackTrace();
                }
                return null;
            } catch (Exception e) {
                Log.i("SUMIT_CallApiExecute", "Exception Message:" + e.getMessage());
                return null;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            if (!TextUtils.isEmpty(result)) {
                apiCallListner.onSuccess(operationCode, result, customData);
            } else {
                apiCallListner.onFail(operationCode, null);
            }
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            if (apiCallListner != null) {
                apiCallListner.onProgress(operationCode, Integer.parseInt(progress[0]));
            }
            super.onProgressUpdate(progress);
        }
    }
}
