package ontime.app.customer.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import ontime.app.R;
import ontime.app.customer.doneActivity.RestaurantDetailActivity;
import ontime.app.databinding.CRowMyCartItemBinding;
import ontime.app.databinding.CRowRequestpendingItemBinding;
import ontime.app.model.usermain.OrderProccessing;
import ontime.app.model.usermain.UserCartItem;
import ontime.app.model.usermain.UserCartItemDetail;
import ontime.app.rv_interface;
import ontime.app.utils.Common;

public class RvRestPenddingListAdapter extends RecyclerView.Adapter<RvRestPenddingListAdapter.MyViewHolder> {
    Context mContext;
    List<UserCartItemDetail> mresponceDatumList;
    OrderProccessing morderProccessing;

    public RvRestPenddingListAdapter(Context context, List<UserCartItemDetail> responceData, OrderProccessing orderproccessingOrder) {
        this.mContext = context;
        this.mresponceDatumList = responceData;
        this.morderProccessing = orderproccessingOrder;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.c_row_requestpending_item, parent, false);
        return new MyViewHolder(itemView, CRowRequestpendingItemBinding.bind(itemView));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        try {
            UserCartItemDetail userCartItem = mresponceDatumList.get(position);
            holder.binding.txtQty.setText(Common.isStrempty(String.valueOf(userCartItem.getQuantity())));
            holder.binding.txtTotal.setText("SR " + Common.isStrempty(String.valueOf(morderProccessing.getGrandTotal())));
            holder.binding.txtName.setText(Common.isStrempty(String.valueOf(morderProccessing.getRestaurant().getName())));
            holder.binding.txtOrderStatus.setText("Status : "+Common.isStrempty(String.valueOf(morderProccessing.getPaymentStatus())));
        } catch (Exception e) {
        }
    }

    @Override
    public int getItemCount() {
        return mresponceDatumList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        CRowRequestpendingItemBinding binding;

        MyViewHolder(View itemView, CRowRequestpendingItemBinding itemBinding) {
            super(itemView);
            binding = itemBinding;

        }
    }
}
