package ontime.app.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ontime.app.R;
import ontime.app.model.usermain.OrderProccessing;
import ontime.app.model.usermain.UserCart;
import ontime.app.model.usermain.UserCartItem;
import ontime.app.model.usernewmain.UserItem;
import ontime.app.restaurant.model.readerOrder.OrderDetail;
import ontime.app.restaurant.model.readerOrder.ReaderNewOrder;
import ontime.app.retrofit.APIClient;
import ontime.app.retrofit.ApiInterface;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Response;

public class Common {
    public static Toast toast;
    public static int OrderBy = 0;
    public static int MERCHANT_TYPE = 1;
    public static int MinPrice = 0;
    public static int MaxPrice = 0;
    public static List<Integer> integers = new ArrayList<>();
    public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
    private static final String TAG = "xcellenceit";
    public static List<UserCartItem> newCartItem;
    public static UserCartItem UpdateCart;
    public static UserCart MUserCart;
    public static ReaderNewOrder READERNEWORDER;
    public static OrderProccessing ORDERPROCCESSING_ORDER;
    public static List<UserItem> newUserCart;
    public static String DELIVER_TYPE = "";
    public static int PAYMENT_TYPE = 4;
    public static String NOTE = "type note here...";
    public static int FILTER_TYPE = 0;
    public static int RESTAURANT_ID = 0;
    public static String REST_NAME = "";
    public static String REST_LAT = "";
    public static String REST_LOG = "";

    public static void setSystemBarColor(Activity act, @ColorRes int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = act.getWindow();
            window.addFlags(Integer.MIN_VALUE);
            window.clearFlags(67108864);
            window.setStatusBarColor(act.getResources().getColor(color));
        }
    }

    public static void setSystemBarLight(Activity act) {
        if (Build.VERSION.SDK_INT >= 23) {
            View view = act.findViewById(16908290);
            view.setSystemUiVisibility(view.getSystemUiVisibility() | 8192);
        }
    }

    public static String parseDateToddMMyyyy(String time) {
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

    public static String parseDateToddMMyyyyTime(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "hh:mm aa";
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

    static Date date_cancels = null;

    public static String parseDateToddMMssyyyyTime(String time) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar cal = Calendar.getInstance();
        try {
            date_cancels = timeFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        timeFormat.format(date_cancels.getTime());
        Date current_dateas = new Date();
        String str = null;
       if(isYesterday(cal.getTime())){
           SimpleDateFormat print = new SimpleDateFormat("dd.MM.yyyy hh:mm aa");
           str = print.format(date_cancels.getTime());
       }else {
           SimpleDateFormat print = new SimpleDateFormat("hh:mm aa");
           str = print.format(date_cancels.getTime());
       }
        return str;
    }
    public static boolean isYesterday(Date d) {
        return DateUtils.isToday(d.getTime() + DateUtils.DAY_IN_MILLIS);
    }
    static String IMEI = "";

    public static String getIMEI(Context context) {

        String unique_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.e("unique_id", "-->" + unique_id);
        return unique_id;
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static Boolean hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        return imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean isEmpty(String value) {
        boolean isEmpty = TextUtils.isEmpty(value);
        if (!isEmpty) {
            isEmpty = value.toLowerCase().equals("null");
        }
        return isEmpty;
    }

    @NotNull
    public static String isStrempty(String s) {
        if (s == null) {
            return "-";
        } else {
            return s.trim();
        }
    }

    @NotNull
    public static int isIntempty(int s) {
        if (s == 0) {
            return 0;
        } else {
            return s;
        }
    }

    @NotNull
    public static int isIntSum(Integer quantity, int s) {
        if (quantity == 0) {
            return 0;
        } else {
            s = quantity;
            s++;
            return s;
        }
    }

    @NotNull
    public static int isIntSub(Integer quantity, int s) {
        if (quantity == 0) {
            return 0;
        } else {
            s = quantity;
            s--;
            return s;
        }
    }

    @NotNull
    public static String isStrBuildempty(String s) {
        if (s == null || s.equals(" ")) {
            return "";
        } else {
            String s1 = "[" + s + "]";
            return s1.trim();
        }
    }

    @NotNull
    public static String isRequired(Boolean s) {
        if (s) {
            return "<font color='#e4434b'> * </font>";
        } else {
            return "";
        }
    }

    public static boolean isInternetAvailable(Context foContext) {
        NetworkInfo loNetInfo = ((ConnectivityManager) foContext
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();

        if (loNetInfo != null)
            if (loNetInfo.isAvailable())
                if (loNetInfo.isConnected())
                    return true;

        return false;
    }

    public static void displayToastMessageShort(Context context, String msg, boolean isLong) {
        if (toast == null)
            toast = Toast.makeText(context, msg, isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        else {
            toast.setText(msg);
        }
        toast.show();
    }

    public static String removeDuplicates(String input) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            if (!result.toString().contains(String.valueOf(input.charAt(i)))) {
                result.append(String.valueOf(input.charAt(i)));
            }
        }
        return result.toString();
    }

    public static RecyclerView.LayoutManager isRvLayoutManger(boolean i, Activity activity, int i11, int i1) {
        if (i) {
            LinearLayoutManager rv_dynamicView = new LinearLayoutManager(activity);
            rv_dynamicView.setOrientation(i11);
            return rv_dynamicView;
        } else {
            GridLayoutManager rv_proManager = new GridLayoutManager(activity, i1);
            rv_proManager.setOrientation(i11);
            return rv_proManager;
        }
    }

    public static String getParenthesesContent1(String str) {
        String result = str.substring(str.indexOf("(\"") + 0, str.indexOf("\")"));

        return result;
    }

    public static int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }

    public static String addZeroBeforeDate(int n) {
        return String.valueOf((n < 10) ? ("0" + n) : n);
    }

    public static int getIntFullmonth(String month) {
        int m = 0;
        try {

            Date date = new SimpleDateFormat("MMMM", Locale.ENGLISH).parse(month);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            m = cal.get(Calendar.MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return m;
    }

    public static String valueOf(double d) {
        return Double.toString(d);
    }

    public static String getFullMonth(String month) {
        String finalMonth = null;
        SimpleDateFormat monthParse = new SimpleDateFormat("MM");
        SimpleDateFormat monthDisplay = new SimpleDateFormat("MMMM");
        try {
            finalMonth = monthDisplay.format(monthParse.parse(month));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return finalMonth;
    }

    public static String getDateString(int year, int month, int mDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, mDay);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(calendar.getTime());
    }

    public static String setDateTime(String currentString) {
        try {
            // 2019-01-14 T 17:36:08.8238945 (2019-01-29T09:55:33.7948683)
            String[] separated = currentString.split("T");
            String date = separated[0];
            String[] ltime = separated[1].split(":");
            String h = ltime[0];
            String m = ltime[1];
            double s = Double.parseDouble(ltime[2]);
            int i = (int) s;
            String time = h + ":" + m + ":" + i;
            String datetime = date + " " + time;
            String fdateTime = formateDateFromstring("yyyy-MM-dd hh:mm:ss", "MM/dd/yyyy hh:mm:ss a", datetime);
            return fdateTime;
        } catch (NumberFormatException e) {
            return "-";
        }
    }

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            Log.e("DateError : ", "ParseException - dateFormat");
        }

        return outputDate;

    }

    public static boolean edvalidateName(String name, EditText ti_name, String msg) {
        if (name.isEmpty()) {
            ti_name.requestFocus();
            ti_name.setError(msg);
            return false;
        } else {
            ti_name.setError(null);
            return true;
        }
    }

    public static boolean edvalidatePhoneName(String name, EditText ti_name, String msg) {
        if (name.isEmpty()) {
            ti_name.requestFocus();
            ti_name.setError(msg);
            return false;
        } else if (name.length() <= 5) {
            ti_name.requestFocus();
            ti_name.setError(msg);
            return false;
        } else {
            ti_name.setError(null);
            return true;
        }
    }

    public static boolean edvalidatepassword(String name, EditText ti_name, String msg) {
        if (name.isEmpty()) {
            ti_name.requestFocus();
            ti_name.setError(msg);
            return false;
        } else if (name.length() <= 5) {
            ti_name.requestFocus();
            ti_name.setError("Password between 6 and 20 alphanumeric characters");
            return false;
        } else {
            ti_name.setError(null);
            return true;
        }
    }

    public static boolean edvalidateemailid(String name, EditText ti_name, String msg) {
        if (!isValidEmail(name)) {
            ti_name.requestFocus();
            ti_name.setError("Enter valid Email id");
            return false;
        } else {
            ti_name.setError(null);
            return true;
        }
    }

    public static boolean edvalidateMatchingPassword(String co_password, EditText ti_con_password, String password) {
        if (co_password.isEmpty()) {
            ti_con_password.setError("Field can't be empty");
            return false;
        } else if (!co_password.equals(password)) {
            ti_con_password.setError("Password Not matching");
            return false;
        } else {
            ti_con_password.setError(null);
            return true;
        }
    }

    public static File getCompressed(Context context, String path) throws IOException {
        if (context == null)
            throw new NullPointerException();
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir == null)
            cacheDir = context.getCacheDir();
        String rootDir = cacheDir.getAbsolutePath() + "/ImageCompressor";
        File root = new File(rootDir);
        if (!root.exists())
            root.mkdirs();
        Bitmap bitmap = decodeImageFromFiles(path, 300, 300);
        File compressed = new File(root, SDF.format(new Date()) + ".jpg");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        FileOutputStream fileOutputStream = new FileOutputStream(compressed);
        fileOutputStream.write(byteArrayOutputStream.toByteArray());
        fileOutputStream.flush();
        fileOutputStream.close();
        return compressed;
    }

    public static Bitmap decodeImageFromFiles(String path, int width, int height) {
        BitmapFactory.Options scaleOptions = new BitmapFactory.Options();
        scaleOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, scaleOptions);
        int scale = 1;
        while (scaleOptions.outWidth / scale / 2 >= width
                && scaleOptions.outHeight / scale / 2 >= height) {
            scale *= 2;
        }
        BitmapFactory.Options outOptions = new BitmapFactory.Options();
        outOptions.inSampleSize = scale;
        return BitmapFactory.decodeFile(path, outOptions);
    }

    public static boolean checkAndRequestPermissions(Context context) {

        int locationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (readPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 2);
            return false;
        }
        return true;
    }

    public static ApiInterface getApiInterface() {
        return APIClient.getClient().create(ApiInterface.class);
    }

    public static String printRes(Response<?> response) {
        Log.e(TAG, "Url : " + response.raw().request().url());
        Log.e(TAG, "Response Code : " + response.code());
        Log.e(TAG, "Response : " +
                new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));
        Gson gson = new GsonBuilder().create();
        Error error;
        if (response.code() != 200) {
            try {
                assert response.errorBody() != null;
                error = gson.fromJson(response.errorBody().string(), Error.class);
                return error.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static void printError(Call<?> call, Throwable t) {
        Log.e(TAG, "Url : " + t.getMessage());
        if (t instanceof SocketTimeoutException) {
            Log.e(TAG, "Connection Timeout");
        } else if (t instanceof IOException) {
            Log.e(TAG, "Timeout");
        } else {
            if (call.isCanceled()) {
                Log.e(TAG, "Call was cancelled forcefully");
            } else {
                Log.e(TAG, "Network Error :: " + t.getLocalizedMessage());
            }
        }
    }

    public static void printParams(JsonObject params) {
        Log.e(TAG, "Params : " +
                new GsonBuilder().setPrettyPrinting().create().toJson(params));
    }

}
