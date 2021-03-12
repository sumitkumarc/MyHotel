package ontime.app.restaurant.adapte;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ontime.app.R;
import ontime.app.databinding.RRowNeworderItemBinding;
import ontime.app.databinding.RRowProcessingOrderItemBinding;
import ontime.app.okhttp.APIcall;
import ontime.app.okhttp.AppConstant;
import ontime.app.restaurant.model.readerOrder.ReaderExample;
import ontime.app.restaurant.model.readerOrder.ReaderProccessing;
import ontime.app.restaurant.ui.Activity.RiderOrderDetails;
import ontime.app.utils.BaseAdapter;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.RequestBody;
import ontime.app.utils.Common;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RvProcessingOrderAdapter extends BaseAdapter<RvProcessingOrderAdapter.MyViewHolder> implements APIcall.ApiCallListner {
    Context mContext;
    public static String timeLeftFormatted;
    int temp = 0;
    List<ReaderProccessing> mreaderProccessings;
    ProgressDialog dialog;

    public Runnable runnable;
    public Handler handler = new Handler();
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    Date event_date;

    public RvProcessingOrderAdapter(Context context, List<ReaderProccessing> readerProccessings) {
        this.mContext = context;
        this.mreaderProccessings = readerProccessings;

    }

    @Override
    protected MyViewHolder onCreateView(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.r_row_processing_order_item, viewGroup, false);
        return new MyViewHolder(itemView, RRowProcessingOrderItemBinding.bind(itemView));
    }

    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    @Override
    protected void bindRViewHolder(MyViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        Glide.with(mContext).load(mreaderProccessings.get(position).getUser().getImage()).centerCrop().placeholder(R.mipmap.image_placeholder_default).into(holder.binding.ivUserImg);
        Glide.with(mContext).load(mreaderProccessings.get(position).getOrderDetail().get(0).getItemDetail().getImage()).centerCrop().placeholder(R.mipmap.image_placeholder_default).into(holder.binding.ivRestImg);
        holder.binding.txtUserName.setText(mreaderProccessings.get(position).getUser().getFullName());

        holder.binding.txtRestName.setText(mreaderProccessings.get(position).getOrderDetail().get(0).getItemDetail().getItemName());
        holder.binding.txtRestQty.setText(mContext.getResources().getString(R.string.Qty) +" : " + mreaderProccessings.get(position).getOrderDetail().get(0).getQuantity());

        if ((Common.isStrempty(mreaderProccessings.get(position).getPaymentStatus()).equals("success"))) {
            holder.binding.txtOrderStatus.setText(mContext.getResources().getString(R.string.Paid) + " : SR " + Common.isStrempty(mreaderProccessings.get(position).getTotalPrice()));
            holder.binding.txtOrderStatus.setTextColor(mContext.getResources().getColor(R.color.green));
        } else {
            holder.binding.txtOrderStatus.setText(mContext.getResources().getString(R.string.Pending) + " : SR " + Common.isStrempty(mreaderProccessings.get(position).getTotalPrice()));
            holder.binding.txtOrderStatus.setTextColor(mContext.getResources().getColor(R.color.red));
        }

        holder.binding.txtDtype.setText(mContext.getResources().getString(R.string.D_type) + " : " + mreaderProccessings.get(position).getDeliveryType());


        if ((mreaderProccessings.get(position).getPaymentType().equals("1"))) {
            holder.binding.txtPtype.setText(mContext.getResources().getString(R.string.P_type) + " : " + mContext.getResources().getString(R.string.wallet));
        } else if ((mreaderProccessings.get(position).getPaymentType().equals("2"))) {
            holder.binding.txtPtype.setText(mContext.getResources().getString(R.string.P_type) + " : " + mContext.getResources().getString(R.string.Card));
        } else if ((mreaderProccessings.get(position).getPaymentType().equals("3"))) {
            holder.binding.txtPtype.setText(mContext.getResources().getString(R.string.P_type) + " : " +  mContext.getResources().getString(R.string.Apple_Pay));
        } else if ((mreaderProccessings.get(position).getPaymentType().equals("99"))) {
            holder.binding.txtPtype.setText(mContext.getResources().getString(R.string.P_type) + " : " +  mContext.getResources().getString(R.string.Unknown));
        } else if ((mreaderProccessings.get(position).getPaymentType().equals("4"))) {
            holder.binding.txtPtype.setText(mContext.getResources().getString(R.string.P_type) + " : " + mContext.getResources().getString(R.string.Cod));
        }


        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date ss = new Date();
        try {
            ss = dateFormat.parse(mreaderProccessings.get(position).getCountdownTime());
            c.setTime(ss);
            c.add(Calendar.SECOND, Integer.parseInt(String.valueOf(parseTimeStringToSeconds(mreaderProccessings.get(position).getDeliveryTime()))));
            event_date = new Date();
            dateFormat.setTimeZone(TimeZone.getDefault());
            event_date = c.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Date current_dateas = new Date();
                if (!current_dateas.after(event_date)) {
                    GetCountDownStart(holder);
                }
            }
        });


        holder.binding.btDelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()) {
                    ApiCAllToDeliveryOrder(mreaderProccessings.get(position).getId());
                }

            }
        });
    }


    private void showDialog() {
        dialog = new ProgressDialog(mContext);
        dialog.setMessage(mContext.getResources().getString(R.string.Please_wait));
        dialog.setCancelable(false);
        dialog.show();
    }

    public void hideDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    public void ApiCAllToDeliveryOrder(Integer Order_id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("order_id", Order_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_READER_ORDER_COMPLETE;
        APIcall apIcall = new APIcall(mContext);
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_READER_ORDER_COMPLETE, this);
    }

    @Override
    protected int getListCounter() {
        return (null != mreaderProccessings ? mreaderProccessings.size() : 0);
    }

    @Override
    public void onStartLoading(int operationCode) {
        if (operationCode == APIcall.OPERATION_READER_ORDER_COMPLETE) {
            showDialog();
        }
    }

    @Override
    public void onProgress(int operationCode, int progress) {

    }

    @Override
    public void onSuccess(int operationCode, String response, Object customData) {
        try {
            JSONObject obj = new JSONObject(response);
            if (operationCode == APIcall.OPERATION_READER_ORDER_COMPLETE) {
                hideDialog();
                if (obj.getString("status").equals("200")) {
                    RiderOrderDetails.binding.picker.scrollToPosition(3);
                    Toast.makeText(mContext, "" + obj.getString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "" + obj.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            hideDialog();
        }

    }

    @Override
    public void onFail(int operationCode, String response) {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RRowProcessingOrderItemBinding binding;

        CountDownTimer countDownTimer;

        MyViewHolder(View itemView, RRowProcessingOrderItemBinding itemBinding) {
            super(itemView);
            binding = itemBinding;

        }
    }

    public static long parseTimeStringToSeconds(String str) {
        try {
            return parseTime(str);
        } catch (NumberFormatException nfe) {
            return 0;
        }
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

    private void GetCountDownStart(final MyViewHolder mholder) {

        if (mholder.countDownTimer != null) {
            mholder.countDownTimer.cancel();
        }
        Date current_dateas = new Date();
        if (!current_dateas.after(event_date)) {
            long diff = event_date.getTime() - current_dateas.getTime();

            mholder.countDownTimer = new CountDownTimer(diff, 500) {

                @Override
                public void onTick(long l) {
                    long Days = l / (24 * 60 * 60 * 1000);
                    long Hours = l / (60 * 60 * 1000) % 24;
                    long Minutes = l / (60 * 1000) % 60;
                    long Seconds = l / 1000 % 60;
                    //
                    timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", Hours, Minutes, Seconds);
                    char ch1 = timeLeftFormatted.charAt(0);
                    char ch2 = timeLeftFormatted.charAt(1);

                    char ch3 = timeLeftFormatted.charAt(3);
                    char ch4 = timeLeftFormatted.charAt(4);

                    char ch5 = timeLeftFormatted.charAt(6);
                    char ch6 = timeLeftFormatted.charAt(7);

                    if (mholder.binding.txtHfirst.getText().hashCode() != ch1) {
                        mholder.binding.txtHfirst.setText(Character.toString(ch1));
                    }
                    if (mholder.binding.txtHfsecond.getText().hashCode() != ch2) {
                        mholder.binding.txtHfsecond.setText(Character.toString(ch2));
                    }
                    if (mholder.binding.txtMfirst.getText().hashCode() != ch3) {
                        mholder.binding.txtMfirst.setText(Character.toString(ch3));
                    }
                    if (mholder.binding.txtMSecond.getText().hashCode() != ch4) {
                        mholder.binding.txtMSecond.setText(Character.toString(ch4));
                    }
                    if (mholder.binding.txtSFirst.getText().hashCode() != ch5) {
                        mholder.binding.txtSFirst.setText(Character.toString(ch5));
                    }
                    if (mholder.binding.txtSSecond.getText().hashCode() != ch6) {
                        mholder.binding.txtSSecond.setText(Character.toString(ch6));
                    }
                }

                @Override
                public void onFinish() {
                    mholder.countDownTimer.cancel();
                }
            }.start();
        }
        /*runnable = new Runnable() {
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

                        if (mholder.binding.txtHfirst.getText().hashCode() != ch1) {
                            mholder.binding.txtHfirst.setText(Character.toString(ch1));
                        }
                        if (mholder.binding.txtHfsecond.getText().hashCode() != ch2) {
                            mholder.binding.txtHfsecond.setText(Character.toString(ch2));
                        }
                        if (mholder.binding.txtMfirst.getText().hashCode() != ch3) {
                            mholder.binding.txtMfirst.setText(Character.toString(ch3));
                        }
                        if (mholder.binding.txtMSecond.getText().hashCode() != ch4) {
                            mholder.binding.txtMSecond.setText(Character.toString(ch4));
                        }
                        if (mholder.binding.txtSFirst.getText().hashCode() != ch5) {
                            mholder.binding.txtSFirst.setText(Character.toString(ch5));
                        }
                        if (mholder.binding.txtSSecond.getText().hashCode() != ch6) {
                            mholder.binding.txtSSecond.setText(Character.toString(ch6));
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
    }*/
    }
}
