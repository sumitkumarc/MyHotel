package ontime.app.restaurant.adapte;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.RequestBody;
import ontime.app.R;
import ontime.app.customer.doneActivity.RestaurantDetailActivity;
import ontime.app.databinding.CRowMyCartItemBinding;
import ontime.app.databinding.RRowItemDetailsBinding;
import ontime.app.model.usermain.UserCartItem;
import ontime.app.okhttp.APIcall;
import ontime.app.okhttp.AppConstant;
import ontime.app.restaurant.model.readerOrder.OrderDetail;
import ontime.app.restaurant.ui.Activity.RiderOrderDetails;
import ontime.app.rv_interface;
import ontime.app.utils.Common;

public class RvItemsDetailsListAdapter extends RecyclerView.Adapter<RvItemsDetailsListAdapter.MyViewHolder> {
    Context mContext;
    List<OrderDetail> mresponceDatumList;
    String mOrderStatus;


    public RvItemsDetailsListAdapter(Context context, List<OrderDetail> orderDetail, String orderStatus) {
        this.mContext = context;
        this.mOrderStatus = orderStatus;
        this.mresponceDatumList = orderDetail;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.r_row_item_details, parent, false);
        return new MyViewHolder(itemView, RRowItemDetailsBinding.bind(itemView));
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        OrderDetail userCartItem = mresponceDatumList.get(position);
        if (userCartItem.getItemDetail() != null) {
            if (Common.MERCHANT_TYPE == 1) {
                holder.binding.txtName.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                holder.binding.llDeleteItem.setBackground(mContext.getResources().getDrawable(R.drawable.btn_golden));
//                holder.binding.ivRestProfileImg.setBorderColor(mContext.getResources().getColor(R.color.colorAccent));
            } else {
                holder.binding.txtName.setTextColor(mContext.getResources().getColor(R.color.super_mart));
                holder.binding.llDeleteItem.setBackground(mContext.getResources().getDrawable(R.drawable.btn_red));
//                holder.binding.ivRestProfileImg.setBorderColor(mContext.getResources().getColor(R.color.super_mart));
            }
            try {
                holder.binding.txtName.setText(Common.isStrempty(userCartItem.getItemDetail().getItemName()));
                Glide.with(mContext).load(userCartItem.getItemDetail().getImage()).centerCrop().placeholder(R.mipmap.image_placeholder_default).into(holder.binding.ivRestProfileImg);
                holder.binding.txtPrice.setText("SR " + Common.isStrempty(userCartItem.getTotalPrice()));
                holder.binding.txtAddress.setText(mContext.getResources().getString(R.string.status) +" : " + Common.isStrempty(mOrderStatus));
                holder.binding.txtQty.setText(mContext.getResources().getString(R.string.Qty) + " : "+String.valueOf(userCartItem.getQuantity()));
                holder.binding.ivAdd.setClickable(false);
                holder.binding.ivSub.setClickable(false);
                holder.binding.ivAdd.setEnabled(false);
                holder.binding.ivSub.setEnabled(false);
                holder.binding.llEdit.setVisibility(View.GONE);
                holder.binding.llDeleteItem.setVisibility(View.GONE);
            } catch (Exception e) {
            }
        }

    }


    @Override
    public int getItemCount() {
        return mresponceDatumList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RRowItemDetailsBinding binding;

        MyViewHolder(View itemView, RRowItemDetailsBinding itemBinding) {
            super(itemView);
            binding = itemBinding;

        }
    }
}
