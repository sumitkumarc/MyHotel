package ontime.app.customer.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import ontime.app.R;
import ontime.app.customer.doneActivity.OrderSummaryActivity;
import ontime.app.databinding.RowItemPaymentMethordBinding;
import ontime.app.databinding.RowItemorderCancelledBinding;
import ontime.app.utils.Common;

public class RvPaymnetMethordListAdapter extends RecyclerView.Adapter<RvPaymnetMethordListAdapter.MyViewHolder> {
    Context mContext;
    int[] mints;
    int row_index = 0;
    int[] ints= new int[]{4,2,1,3};

    public RvPaymnetMethordListAdapter(Context context, int[] ints) {
        mContext = context;
        mints = ints;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_payment_methord, parent, false);
        return new MyViewHolder(itemView, RowItemPaymentMethordBinding.bind(itemView));
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Glide.with(mContext).load(mints[position]).centerCrop().placeholder(R.mipmap.image_placeholder_default).into(holder.binding.ivPamentMethord);

        holder.binding.llImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index=position;
                Common.PAYMENT_TYPE =ints[position];
                if(ints[position] == 1){
                    OrderSummaryActivity.binding.txtWallet.setVisibility(View.VISIBLE);
                }else {
                    OrderSummaryActivity.binding.txtWallet.setVisibility(View.GONE);
                }
                notifyDataSetChanged();;
            }
        });
        if (row_index == position) {
            if (Common.MERCHANT_TYPE == 1) {
                holder.binding.llImgBack.setBackground(mContext.getResources().getDrawable(R.drawable.btn_red_border));
            } else {
                holder.binding.llImgBack.setBackground(mContext.getResources().getDrawable(R.drawable.btn_orange_border));
            }
        }else {
            holder.binding.llImgBack.setBackground(null);
        }
    }

    @Override
    public int getItemCount() {
        return mints.length;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        RowItemPaymentMethordBinding binding;

        MyViewHolder(View itemView, RowItemPaymentMethordBinding itemBinding) {
            super(itemView);
            binding = itemBinding;

        }
    }
}
