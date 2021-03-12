package ontime.app.customer.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import ontime.app.R;
import ontime.app.customer.doneActivity.PaymentActivity;
import ontime.app.databinding.RowCardDetailsBinding;
import ontime.app.databinding.RowNotificationItemBinding;
import ontime.app.model.restaurantlist.ResponceDatum;
import ontime.app.sqllight.CardDetails;
import ontime.app.utils.Common;

import java.util.ArrayList;
import java.util.List;

public class RvNotificationListAdapter extends RecyclerView.Adapter<RvNotificationListAdapter.MyViewHolder> {
    Context mContext;
    List<ResponceDatum> mResponceData;

    public RvNotificationListAdapter(Context context, List<ResponceDatum> responceData) {
        mContext = context;
        mResponceData = responceData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notification_item, parent, false);
        return new MyViewHolder(itemView, RowNotificationItemBinding.bind(itemView));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        if (Common.MERCHANT_TYPE == 1) {
            holder.binding.txtName.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        } else {
            holder.binding.txtName.setTextColor(mContext.getResources().getColor(R.color.super_mart));
        }
        try {
            holder.binding.txtName.setText(Common.isStrempty(mResponceData.get(position).getRestaurant().getName()));
            holder.binding.txtDetails1.setText("" + Common.isStrempty(mResponceData.get(position).getMessage()));
            if (mResponceData.get(position).getRestaurant().getImage() != null) {
                Glide.with(mContext).load(mResponceData.get(position).getRestaurant().getImage()).centerCrop().placeholder(R.mipmap.image_placeholder_default).into(holder.binding.ivProfile);
            }else {
                holder.binding.ivProfile.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_launcher));
            }
        } catch (Exception e) {
            holder.binding.ivProfile.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_launcher));
        }


//        holder.binding.txtTitle.setText(""+ mResponceData.get(position).getRestaurant().);


    }

    @Override
    public int getItemCount() {
        return mResponceData.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        RowNotificationItemBinding binding;

        MyViewHolder(View itemView, RowNotificationItemBinding itemBinding) {
            super(itemView);
            binding = itemBinding;

        }
    }
}
