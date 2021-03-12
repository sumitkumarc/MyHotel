package ontime.app.restaurant.adapte;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import ontime.app.R;
import ontime.app.databinding.RowNotificationItemBinding;
import ontime.app.model.restaurantlist.ResponceDatum;
import ontime.app.restaurant.model.NotificationDataModel;
import ontime.app.utils.Common;

public class MutchantNotificationListAdapter extends RecyclerView.Adapter<MutchantNotificationListAdapter.MyViewHolder> {
    Context mContext;
    List<NotificationDataModel> mResponceData;

    public MutchantNotificationListAdapter(Context context, List<NotificationDataModel> responceData) {
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
//        if (Common.MERCHANT_TYPE == 1) {
//            holder.binding.txtTitle.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
//        } else {
//            holder.binding.txtTitle.setTextColor(mContext.getResources().getColor(R.color.super_mart));
//        }
        try {
            holder.binding.txtName.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            holder.binding.txtName.setText(Common.isStrempty(mResponceData.get(position).getUser().getFullName()));
            String data = mResponceData.get(position).getMessage();

            holder.binding.txtDetails1.setText("" + data);
            Glide.with(mContext).load(mResponceData.get(position).getUser().getImage()).centerCrop().placeholder(R.mipmap.image_placeholder_default).into(holder.binding.ivProfile);
//        holder.binding.txtTitle.setText(""+ mResponceData.get(position).getRestaurant().);

        }catch (Exception e){

        }
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
