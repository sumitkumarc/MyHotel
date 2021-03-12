package ontime.app.customer.Adapter;

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
import ontime.app.databinding.CRowMyCartItemBinding;
import ontime.app.databinding.CRowOrderCartItemBinding;
import ontime.app.model.usermain.UserCartItem;
import ontime.app.rv_interface;
import ontime.app.utils.Common;

public class RvRestOrderCartListAdapter extends RecyclerView.Adapter<RvRestOrderCartListAdapter.MyViewHolder> {
    Context mContext;
    List<UserCartItem> mresponceDatumList;
    Boolean mABoolean;

    public RvRestOrderCartListAdapter(Context context, List<UserCartItem> responceData, boolean mBoolean) {
        this.mContext = context;
        this.mresponceDatumList = responceData;
        this.mABoolean = mBoolean;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.c_row_order_cart_item, parent, false);
        return new MyViewHolder(itemView, CRowOrderCartItemBinding.bind(itemView));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        UserCartItem userCartItem = mresponceDatumList.get(position);
        if (userCartItem.getItemDetail() != null) {
            if (Common.MERCHANT_TYPE == 1) {
                holder.binding.txtName.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                holder.binding.ivRestProfileImg.setBorderColor(mContext.getResources().getColor(R.color.colorAccent));
            } else {
                holder.binding.txtName.setTextColor(mContext.getResources().getColor(R.color.super_mart));
                holder.binding.ivRestProfileImg.setBorderColor(mContext.getResources().getColor(R.color.super_mart));
            }
            try {
                holder.binding.txtName.setText(Common.isStrempty(userCartItem.getItemDetail().getItemName()));
                Glide.with(mContext).load(userCartItem.getItemDetail().getImage()).centerCrop().placeholder(R.mipmap.image_placeholder_default).into(holder.binding.ivRestProfileImg);
                holder.binding.txtAddress.setText(mContext.getResources().getString(R.string.status_pending));
                holder.binding.txtPrice.setText("SR " + Common.isStrempty(userCartItem.getTotalPrice()));

                holder.binding.txtQty.setText(mContext.getResources().getString(R.string.Qty) +" : " +String.valueOf(userCartItem.getQuantity()));
            } catch (Exception e) {
            }
        }

    }

    @Override
    public int getItemCount() {
        return mresponceDatumList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CRowOrderCartItemBinding binding;

        MyViewHolder(View itemView, CRowOrderCartItemBinding itemBinding) {
            super(itemView);
            binding = itemBinding;

        }
    }
}
