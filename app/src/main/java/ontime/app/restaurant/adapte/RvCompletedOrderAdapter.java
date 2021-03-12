package ontime.app.restaurant.adapte;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import ontime.app.R;
import ontime.app.databinding.RRowCompletedItemBinding;
import ontime.app.restaurant.model.readerOrder.ReaderCompleted;
import ontime.app.utils.Common;

import java.util.List;

public class RvCompletedOrderAdapter extends RecyclerView.Adapter<RvCompletedOrderAdapter.MyViewHolder> {
    Context mContext;
    List<ReaderCompleted> mNewOrders;

    public RvCompletedOrderAdapter(Context context, List<ReaderCompleted> newOrders) {
        this.mContext = context;
        this.mNewOrders = newOrders;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.r_row_completed_item, parent, false);
        return new MyViewHolder(itemView, RRowCompletedItemBinding.bind(itemView));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(mContext).load(mNewOrders.get(position).getUser().getImage()).centerCrop().placeholder(R.mipmap.image_placeholder_default).into(holder.binding.ivUserImg);
        Glide.with(mContext).load(mNewOrders.get(position).getOrderDetail().get(0).getItemDetail().getImage()).centerCrop().placeholder(R.mipmap.image_placeholder_default).into(holder.binding.ivRestImg);
        holder.binding.txtUserName.setText(mNewOrders.get(position).getUser().getFullName());

        holder.binding.txtRestName.setText(mNewOrders.get(position).getOrderDetail().get(0).getItemDetail().getItemName());
        holder.binding.txtRestQty.setText(mContext.getResources().getString(R.string.Qty)+" : " + mNewOrders.get(position).getOrderDetail().get(0).getQuantity());

        if (Common.isStrempty(mNewOrders.get(position).getPaymentStatus()).equals("success")) {
            holder.binding.txtOrderStatus.setText(mContext.getResources().getString(R.string.Paid)  + " : SR " + Common.isStrempty(mNewOrders.get(position).getTotalPrice()));
            holder.binding.txtOrderStatus.setTextColor(mContext.getResources().getColor(R.color.green));
        }else {
            holder.binding.txtOrderStatus.setText(mContext.getResources().getString(R.string.Pending)  + " : SR " + Common.isStrempty(mNewOrders.get(position).getTotalPrice()));
            holder.binding.txtOrderStatus.setTextColor(mContext.getResources().getColor(R.color.red));
        }
        holder.binding.txtDtype.setText(mContext.getResources().getString(R.string.D_type) + " : " + mNewOrders.get(position).getDeliveryType());


        if ((mNewOrders.get(position).getPaymentType().equals("1"))) {
            holder.binding.txtPtype.setText(mContext.getResources().getString(R.string.P_type) + " : " + mContext.getResources().getString(R.string.wallet));
        } else if ((mNewOrders.get(position).getPaymentType().equals("2"))) {
            holder.binding.txtPtype.setText(mContext.getResources().getString(R.string.P_type) + " : " + mContext.getResources().getString(R.string.Card));
        } else if ((mNewOrders.get(position).getPaymentType().equals("3"))) {
            holder.binding.txtPtype.setText(mContext.getResources().getString(R.string.P_type) + " : " +  mContext.getResources().getString(R.string.Apple_Pay));
        } else if ((mNewOrders.get(position).getPaymentType().equals("99"))) {
            holder.binding.txtPtype.setText(mContext.getResources().getString(R.string.P_type) + " : " +  mContext.getResources().getString(R.string.Unknown));
        } else if ((mNewOrders.get(position).getPaymentType().equals("4"))) {
            holder.binding.txtPtype.setText(mContext.getResources().getString(R.string.P_type) + " : " + mContext.getResources().getString(R.string.Cod));
        }
    }

    @Override
    public int getItemCount() {
        return mNewOrders.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        RRowCompletedItemBinding binding;

        MyViewHolder(View itemView, RRowCompletedItemBinding itemBinding) {
            super(itemView);
            binding = itemBinding;

        }
    }
}
