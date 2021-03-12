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
import ontime.app.databinding.RowItemorderCancelledBinding;
import ontime.app.databinding.RowItemorderProssingBinding;
import ontime.app.databinding.RowNotificationItemBinding;
import ontime.app.model.usermain.OrderCancelled;
import ontime.app.utils.Common;

import java.util.List;

public class RvCancelledOrderListAdapter extends RecyclerView.Adapter<RvCancelledOrderListAdapter.MyViewHolder> {
    Context mContext;
    List<OrderCancelled> mCancelledList;
    public RvCancelledOrderListAdapter(Context context, List<OrderCancelled> objCancelled) {
        mContext = context;
        mCancelledList = objCancelled;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_itemorder_prossing, parent, false);
        return new MyViewHolder(itemView, RowItemorderProssingBinding.bind(itemView));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        if (mCancelledList.get(position).getRestaurant().getType() == 1) {
            holder.binding.txtTitle.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            holder.binding.txtTime.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            holder.binding.btCancelled.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.btn_golden));
        } else {
            holder.binding.txtTitle.setTextColor(mContext.getResources().getColor(R.color.super_mart));
            holder.binding.txtTime.setTextColor(mContext.getResources().getColor(R.color.super_mart));
            holder.binding.btCancelled.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.btn_goldenrectorange));
        }

        holder.binding.txtTitle.setText(Common.isStrempty(mCancelledList.get(position).getRestaurant().getName()));
        holder.binding.txtOrderId.setText(mContext.getResources().getString(R.string.order_no) + Common.isStrempty(mCancelledList.get(position).getOrderNumber()));
        holder.binding.txtTime.setText(mContext.getResources().getString(R.string.order_time)+ Common.parseDateToddMMyyyy(mCancelledList.get(position).getCreatedAt()));
        holder.binding.txtDeliverTime.setText(mContext.getResources().getString(R.string.delivery_time) + "-");
//        holder.binding.txtOrderPaymentStatus.setText(Common.isStrempty(mOrderFinisheds.get(position).getPaymentStatus() + " : SR "+Common.isStrempty(mOrderFinisheds.get(position).getGrandTotal())));
        Glide.with(mContext).load(mCancelledList.get(position).getRestaurant().getImage()).centerCrop().placeholder(R.mipmap.image_placeholder_default).into(holder.binding.ivRestProfileImg);

        if ((mCancelledList.get(position).getOrderStatus() == 0)) {
            holder.binding.txtOrderStatus.setText(mContext.getResources().getString(R.string.status_new));
        } else if ((mCancelledList.get(position).getOrderStatus() == 1)) {
            holder.binding.txtOrderStatus.setText(mContext.getResources().getString(R.string.status_processing));
        } else if ((mCancelledList.get(position).getOrderStatus() == 2)) {
            holder.binding.txtOrderStatus.setText(mContext.getResources().getString(R.string.status_cancelledByuser));
        } else if ((mCancelledList.get(position).getOrderStatus() == 3)) {
            holder.binding.txtOrderStatus.setText(mContext.getResources().getString(R.string.status_completed));
        } else if ((mCancelledList.get(position).getOrderStatus() == 4)) {
            holder.binding.txtOrderStatus.setText(mContext.getResources().getString(R.string.status_cancelled));
        }else if ((mCancelledList.get(position).getOrderStatus() == 99)) {
            holder.binding.txtOrderStatus.setText(mContext.getResources().getString(R.string.status_unknown));
        }

        if ((mCancelledList.get(position).getPaymentType().equals("1"))) {
            holder.binding.txtOrderPamentType.setText(mContext.getResources().getString(R.string.P_type) + " : " + mContext.getResources().getString(R.string.wallet));
        } else if ((mCancelledList.get(position).getPaymentType().equals("2"))) {
            holder.binding.txtOrderPamentType.setText(mContext.getResources().getString(R.string.P_type) + " : " +  mContext.getResources().getString(R.string.Card));
        } else if ((mCancelledList.get(position).getPaymentType().equals("3"))) {
            holder.binding.txtOrderPamentType.setText(mContext.getResources().getString(R.string.P_type) + " : " + mContext.getResources().getString(R.string.Apple_Pay));
        } else if ((mCancelledList.get(position).getPaymentType().equals("99"))) {
            holder.binding.txtOrderPamentType.setText(mContext.getResources().getString(R.string.P_type) + " : " + mContext.getResources().getString(R.string.Unknown));
        } else if ((mCancelledList.get(position).getPaymentType().equals("4"))) {
            holder.binding.txtOrderPamentType.setText(mContext.getResources().getString(R.string.P_type) + " : " + mContext.getResources().getString(R.string.Cod));
        }
        try {
            if (Common.isStrempty(mCancelledList.get(position).getPaymentStatus()).equals("success")) {
                holder.binding.txtOrderPaymentStatus.setTextColor(mContext.getResources().getColor(R.color.green));
                holder.binding.txtOrderPaymentStatus.setText(mContext.getResources().getString(R.string.Paid)+ " : SR " + Common.isStrempty(mCancelledList.get(position).getTotalPrice()));

            } else {
                holder.binding.txtOrderPaymentStatus.setText(mContext.getResources().getString(R.string.Pending) + " : SR " + Common.isStrempty(mCancelledList.get(position).getTotalPrice()));
                holder.binding.txtOrderPaymentStatus.setTextColor(mContext.getResources().getColor(R.color.red));
            }
        } catch (Exception e) {
            holder.binding.txtOrderPaymentStatus.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mCancelledList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        RowItemorderProssingBinding binding;

        MyViewHolder(View itemView, RowItemorderProssingBinding itemBinding) {
            super(itemView);
            binding = itemBinding;

        }
    }
}
