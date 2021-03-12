package ontime.app.restaurant.adapte;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ontime.app.R;
import ontime.app.databinding.RRowNeworderItemBinding;
import ontime.app.okhttp.APIcall;
import ontime.app.okhttp.AppConstant;
import ontime.app.restaurant.model.readerOrder.ReaderExample;
import ontime.app.restaurant.model.readerOrder.ReaderNewOrder;
import ontime.app.restaurant.ui.Activity.NewOrderItemListActivity;
import ontime.app.restaurant.ui.Activity.RiderOrderDetails;
import ontime.app.restaurant.ui.fragment.NewOrderFragment;
import ontime.app.utils.BaseAdapter;
import ontime.app.utils.Common;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RvNewOrderAdapter extends BaseAdapter<RvNewOrderAdapter.MyViewHolder> implements APIcall.ApiCallListner {
    Context mContext;
    List<ReaderNewOrder> mNewOrders;
    ProgressDialog dialog;
    private int mYear, mMonth, mDay, mHour, mMinute;

    public RvNewOrderAdapter(Context context, List<ReaderNewOrder> newOrders) {
        this.mContext = context;
        this.mNewOrders = newOrders;

    }

    @Override
    protected MyViewHolder onCreateView(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.r_row_neworder_item, viewGroup, false);
        return new MyViewHolder(itemView, RRowNeworderItemBinding.bind(itemView));
    }

    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    @Override
    protected void bindRViewHolder(MyViewHolder holder, final int position) {
        try {
            holder.binding.txtOrderId.setText(mContext.getResources().getString(R.string.OrderNumber) + " : " + mNewOrders.get(position).getOrderNumber());
            holder.binding.txtOrderPamnetType.setText(mContext.getResources().getString(R.string.Payment_Type) + " : " + mNewOrders.get(position).getPaymentType());


            holder.binding.txtTotal.setText(mContext.getResources().getString(R.string.Total) + " : SR " + Common.isStrempty(mNewOrders.get(position).getTotalPrice()));

//        mContext.getResources().getString(R.string.Total) +
            holder.binding.txtUserName.setText(Common.isStrempty(mNewOrders.get(position).getUser().getFullName()));

            holder.binding.txtRestName.setText(Common.isStrempty(mNewOrders.get(position).getOrderDetail().get(0).getItemDetail().getItemName()));
            holder.binding.txtRestQty.setText(mContext.getResources().getString(R.string.Qty)+" : " + mNewOrders.get(position).getOrderDetail().get(0).getQuantity());

            Glide.with(mContext).load(mNewOrders.get(position).getUser().getImage()).centerCrop().placeholder(R.mipmap.image_placeholder_default).into(holder.binding.ivUserImage);
            Glide.with(mContext).load(mNewOrders.get(position).getOrderDetail().get(0).getItemDetail().getImage()).centerCrop().placeholder(R.mipmap.image_placeholder_default).into(holder.binding.ivRestImg);


            holder.binding.txtOrderDate.setText(mContext.getResources().getString(R.string.Date) + " : " + Common.parseDateToddMMyyyy(mNewOrders.get(position).getCreatedAt()));
            holder.binding.txtOrderTime.setText(mContext.getResources().getString(R.string.order_time)+" : "  + Common.parseDateToddMMyyyyTime(mNewOrders.get(position).getCreatedAt()));
            holder.binding.txtOrderDeliverType.setText(mContext.getResources().getString(R.string.Delivery_Type) + " : " + Common.isStrempty(mNewOrders.get(position).getDeliveryType()));


            if ((mNewOrders.get(position).getPaymentStatus().equals("success"))) {
                holder.binding.txtOrderStatus.setText(mContext.getResources().getString(R.string.Paid));
                holder.binding.txtOrderStatus.setTextColor(mContext.getResources().getColor(R.color.green));
            } else {
                holder.binding.txtOrderStatus.setText(mContext.getResources().getString(R.string.Pending));
                holder.binding.txtOrderStatus.setTextColor(mContext.getResources().getColor(R.color.red));
            }


//        if ((mNewOrders.get(position).getDeliveryStatus() == 0)) {
//            holder.binding.txtOrderStatus.setText("Paid");
//        } else if ((mNewOrders.get(position).getDeliveryStatus() == 1)) {
//            holder.binding.txtOrderStatus.setText("Paid");
//        } else if ((mNewOrders.get(position).getDeliveryStatus() == 2)) {
//            holder.binding.txtOrderStatus.setText("Cancelled By User");
//        } else if ((mNewOrders.get(position).getDeliveryStatus() == 3)) {
//            holder.binding.txtOrderStatus.setText("Cancelled");
//        } else if ((mNewOrders.get(position).getDeliveryStatus() == 4)) {
//            holder.binding.txtOrderStatus.setText("Completed");
//        } else if ((mNewOrders.get(position).getDeliveryStatus() == 99)) {
//            holder.binding.txtOrderStatus.setText("Unknown");
//        }
            if ((mNewOrders.get(position).getPaymentType().equals("1"))) {
                holder.binding.txtOrderPamnetType.setText(mContext.getResources().getString(R.string.Payment_Type) + " : " + mContext.getResources().getString(R.string.wallet));
            } else if ((mNewOrders.get(position).getPaymentType().equals("2"))) {
                holder.binding.txtOrderPamnetType.setText(mContext.getResources().getString(R.string.Payment_Type) + " : " + mContext.getResources().getString(R.string.Card));
            } else if ((mNewOrders.get(position).getPaymentType().equals("3"))) {
                holder.binding.txtOrderPamnetType.setText(mContext.getResources().getString(R.string.Payment_Type) + " : " +  mContext.getResources().getString(R.string.Apple_Pay));
            } else if ((mNewOrders.get(position).getPaymentType().equals("99"))) {
                holder.binding.txtOrderPamnetType.setText(mContext.getResources().getString(R.string.Payment_Type) + " : " +  mContext.getResources().getString(R.string.Unknown));
            } else if ((mNewOrders.get(position).getPaymentType().equals("4"))) {
                holder.binding.txtOrderPamnetType.setText(mContext.getResources().getString(R.string.Payment_Type) + " : " + mContext.getResources().getString(R.string.Cod));
            }
            holder.binding.btConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isConnected()) {
                        // Get Current Time
                        final Calendar c = Calendar.getInstance();
                        mHour = 00;
                        mMinute = 30;
                        TimePickerDialog timePickerDialog = new TimePickerDialog(mContext,
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        String time_final = hourOfDay + ":" + minute + ":00";
                                        ApiCAllToAcceptOrder(mNewOrders.get(position).getId(), time_final);
//                                    txtTime.setText(hourOfDay + ":" + minute);
                                    }
                                }, mHour, mMinute, true);
                        timePickerDialog.show();
                    }

                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Common.READERNEWORDER = mNewOrders.get(position);
                    Intent intent = new Intent(mContext, NewOrderItemListActivity.class);
                    mContext.startActivity(intent);
                }
            });
            holder.binding.btCancelled.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isConnected()) {
                        ApiCAllToRejectOrder(mNewOrders.get(position).getId());
                    }

                }
            });
            holder.binding.txtExtraSauce.setText(Common.isStrempty(mNewOrders.get(position).getOrderDetail().get(0).getAdditionId().getAdidtionItem()) + " : SR " + Common.isStrempty(mNewOrders.get(position).getOrderDetail().get(0).getAdditionId().getPrice()));
        } catch (Exception e) {
            holder.binding.txtExtraSauce.setVisibility(View.GONE);
        }

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

    public void ApiCAllToRejectOrder(Integer Order_id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("order_id", Order_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_READER_ORDER_REJECT;
        APIcall apIcall = new APIcall(mContext);
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_READER_ORDER_REJECT, this);
    }

    public void ApiCAllToAcceptOrder(Integer Order_id, String timefinal) {
//        Common.hideKeyboard(mContext);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("order_id", Order_id);
            jsonObject.put("delivery_time", timefinal);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_READER_ORDER_ACCEPT;
        APIcall apIcall = new APIcall(mContext);
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_ORDER_ACCEPT, this);
    }

    @Override
    protected int getListCounter() {
        return (null != mNewOrders ? mNewOrders.size() : 0);
    }

    @Override
    public void onStartLoading(int operationCode) {
        if (operationCode == APIcall.OPERATION_ORDER_ACCEPT) {
            showDialog();
        }
        if (operationCode == APIcall.OPERATION_READER_ORDER_REJECT) {
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
            if (operationCode == APIcall.OPERATION_ORDER_ACCEPT) {
                hideDialog();
                if (obj.getString("status").equals("200")) {
                    RiderOrderDetails.binding.picker.scrollToPosition(1);
                    Toast.makeText(mContext, "" + obj.getString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "" + obj.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }
            if (operationCode == APIcall.OPERATION_READER_ORDER_REJECT) {
                hideDialog();
                if (obj.getString("status").equals("200")) {
                    RiderOrderDetails.binding.picker.scrollToPosition(2);
                    Toast.makeText(mContext, "" + obj.getString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "" + obj.getString("message"), Toast.LENGTH_SHORT).show();
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RRowNeworderItemBinding binding;

        MyViewHolder(View itemView, RRowNeworderItemBinding itemBinding) {
            super(itemView);
            binding = itemBinding;

        }
    }
}
