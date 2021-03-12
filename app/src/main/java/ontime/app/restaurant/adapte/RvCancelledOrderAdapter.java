package ontime.app.restaurant.adapte;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import ontime.app.R;
import ontime.app.databinding.RRowCancelItemBinding;
import ontime.app.okhttp.APIcall;
import ontime.app.okhttp.AppConstant;
import ontime.app.restaurant.model.readerOrder.ReaderCancel;
import ontime.app.restaurant.ui.Activity.RiderOrderDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.RequestBody;
import ontime.app.utils.Common;


public class RvCancelledOrderAdapter extends RecyclerView.Adapter<RvCancelledOrderAdapter.MyViewHolder> implements APIcall.ApiCallListner {
    Context mContext;
    List<ReaderCancel> mreaderCancels;
    ProgressDialog dialog;

    public RvCancelledOrderAdapter(Context context, List<ReaderCancel> readerCancels) {
        this.mContext = context;
        this.mreaderCancels = readerCancels;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.r_row_cancel_item, parent, false);
        return new MyViewHolder(itemView, RRowCancelItemBinding.bind(itemView));
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(mContext).load(mreaderCancels.get(position).getUser().getImage()).centerCrop().placeholder(R.mipmap.image_placeholder_default).into(holder.binding.ivUserImg);
        Glide.with(mContext).load(mreaderCancels.get(position).getOrderDetail().get(0).getItemDetail().getImage()).centerCrop().placeholder(R.mipmap.image_placeholder_default).into(holder.binding.ivRestImg);
        holder.binding.txtUserName.setText(mreaderCancels.get(position).getUser().getFullName());

        holder.binding.txtRestName.setText(mreaderCancels.get(position).getOrderDetail().get(0).getItemDetail().getItemName());
        holder.binding.txtRestQty.setText(mContext.getResources().getString(R.string.Qty) + " : " + mreaderCancels.get(position).getOrderDetail().get(0).getQuantity());
        try {
            if (Common.isStrempty(mreaderCancels.get(position).getPaymentStatus()).equals("success")) {
                holder.binding.txtOrderStatus.setTextColor(mContext.getResources().getColor(R.color.green));
                holder.binding.txtOrderStatus.setText(mContext.getResources().getString(R.string.Paid) + " : SR " + Common.isStrempty(mreaderCancels.get(position).getTotalPrice()));

            } else {
                holder.binding.txtOrderStatus.setText(mContext.getResources().getString(R.string.Pending)  + " : SR " + Common.isStrempty(mreaderCancels.get(position).getTotalPrice()));
                holder.binding.txtOrderStatus.setTextColor(mContext.getResources().getColor(R.color.red));
            }
        } catch (Exception e) {
            holder.binding.txtOrderStatus.setVisibility(View.GONE);
        }

        holder.binding.txtDtype.setText(mContext.getResources().getString(R.string.D_type) + " : " + mreaderCancels.get(position).getDeliveryType());


        if ((mreaderCancels.get(position).getPaymentType().equals("1"))) {
            holder.binding.txtPtype.setText(mContext.getResources().getString(R.string.P_type) + " : " + mContext.getResources().getString(R.string.wallet));
        } else if ((mreaderCancels.get(position).getPaymentType().equals("2"))) {
            holder.binding.txtPtype.setText(mContext.getResources().getString(R.string.P_type) + " : " + mContext.getResources().getString(R.string.Card));
        } else if ((mreaderCancels.get(position).getPaymentType().equals("3"))) {
            holder.binding.txtPtype.setText(mContext.getResources().getString(R.string.P_type) + " : " +  mContext.getResources().getString(R.string.Apple_Pay));
        } else if ((mreaderCancels.get(position).getPaymentType().equals("99"))) {
            holder.binding.txtPtype.setText(mContext.getResources().getString(R.string.P_type) + " : " +  mContext.getResources().getString(R.string.Unknown));
        } else if ((mreaderCancels.get(position).getPaymentType().equals("4"))) {
            holder.binding.txtPtype.setText(mContext.getResources().getString(R.string.P_type) + " : " + mContext.getResources().getString(R.string.Cod));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiCAllToRejectOrder(mreaderCancels.get(position).getId());
            }
        });
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

    @Override
    public void onStartLoading(int operationCode) {
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
            if (operationCode == APIcall.OPERATION_READER_ORDER_REJECT) {
                hideDialog();
                JSONObject obj = new JSONObject(response);
                if (obj.getString("status").equals("200")) {
                    RiderOrderDetails.binding.picker.scrollToPosition(2);
                    Toast.makeText(mContext, "" + obj.getString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "" + obj.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }
            hideDialog();
        } catch (Exception e) {

        }
    }

    @Override
    public void onFail(int operationCode, String response) {

    }

    @Override
    public int getItemCount() {
        return mreaderCancels.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        RRowCancelItemBinding binding;

        MyViewHolder(View itemView, RRowCancelItemBinding itemBinding) {
            super(itemView);
            binding = itemBinding;

        }
    }
}
