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

import ontime.app.R;
import ontime.app.customer.doneActivity.RestaurantDetailActivity;
import ontime.app.databinding.CRowMyCartItemBinding;
import ontime.app.databinding.RowItemorderFinisedBinding;
import ontime.app.model.usermain.UserCartItem;
import ontime.app.rv_interface;
import ontime.app.utils.Common;

import java.util.List;

public class RvRestaurantCartPenddingListAdapter extends RecyclerView.Adapter<RvRestaurantCartPenddingListAdapter.MyViewHolder> {
    Context mContext;
    List<UserCartItem> mresponceDatumList;
    rv_interface rvInterface;
    int Counter = 1;
    Boolean mABoolean;
    OnClick onClick;
//    UserCartItem userCartItem= new UserCartItem();

    public RvRestaurantCartPenddingListAdapter(Context context, List<UserCartItem> responceData, boolean mBoolean) {
        this.mContext = context;
        this.mresponceDatumList = responceData;
        this.mABoolean = mBoolean;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.c_row_my_cart_item, parent, false);
        return new MyViewHolder(itemView, CRowMyCartItemBinding.bind(itemView));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        UserCartItem userCartItem = mresponceDatumList.get(position);
        holder.binding.llDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rvInterface.OnItemClick(position, userCartItem.getId());
                //  removeAt(position);
            }
        });


        if (userCartItem.getItemDetail() != null) {
            if (Common.MERCHANT_TYPE == 1) {
                holder.binding.txtName.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                holder.binding.llDeleteItem.setBackground(mContext.getResources().getDrawable(R.drawable.btn_golden));
                holder.binding.ivRestProfileImg.setBorderColor(mContext.getResources().getColor(R.color.colorAccent));
            } else {
                holder.binding.txtName.setTextColor(mContext.getResources().getColor(R.color.super_mart));
                holder.binding.llDeleteItem.setBackground(mContext.getResources().getDrawable(R.drawable.btn_red));
                holder.binding.ivRestProfileImg.setBorderColor(mContext.getResources().getColor(R.color.super_mart));
            }
            try {
                holder.binding.txtName.setText(Common.isStrempty(userCartItem.getItemDetail().getItemName()));
                Glide.with(mContext).load(userCartItem.getItemDetail().getImage()).centerCrop().placeholder(R.mipmap.image_placeholder_default).into(holder.binding.ivRestProfileImg);
//                holder.binding.txtAddress.setText(Common.isStrempty(userCartItem.getItemDetail().getDescription()));
                holder.binding.txtAddress.setText(mContext.getResources().getString(R.string.status_pending));
                holder.binding.txtPrice.setText("SR " + Common.isStrempty(userCartItem.getTotalPrice()));

                holder.binding.txtQty.setText(String.valueOf(userCartItem.getQuantity()));
                if (mABoolean) {
                    holder.binding.llEdit.setVisibility(View.VISIBLE);
                    holder.binding.ivAdd.setClickable(mABoolean);
                    holder.binding.ivSub.setClickable(mABoolean);
                    holder.binding.ivAdd.setEnabled(mABoolean);
                    holder.binding.ivSub.setEnabled(mABoolean);
                    holder.binding.llDeleteItem.setVisibility(View.VISIBLE);
                } else {
                    holder.binding.ivAdd.setClickable(mABoolean);
                    holder.binding.ivSub.setClickable(mABoolean);
                    holder.binding.ivAdd.setEnabled(mABoolean);
                    holder.binding.ivSub.setEnabled(mABoolean);
                    holder.binding.llEdit.setVisibility(View.GONE);
                    holder.binding.llDeleteItem.setVisibility(View.GONE);
                }
                holder.binding.ivAdd.setOnClickListener(new View.OnClickListener() {
                    //                @Override
                    public void onClick(View v) {
                        Counter = Integer.parseInt(holder.binding.txtQty.getText().toString());
                        Counter++;
                        holder.binding.txtQty.setText(String.valueOf(Counter));
                        mresponceDatumList.set(position, userCartItem).setQuantity(Counter);
                        rvInterface.OnUpDateItemClick(userCartItem);
                    }
                });
                holder.binding.ivSub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Counter = Integer.parseInt(holder.binding.txtQty.getText().toString());
                        if (Counter > 1) {
                            Counter--;
                            holder.binding.txtQty.setText(String.valueOf(Counter));
                            mresponceDatumList.set(position, userCartItem).setQuantity(Counter);
                            rvInterface.OnUpDateItemClick(userCartItem);
                        }
                    }
                });
                holder.binding.llEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onClick != null) {
                            onClick.OnItemClick(v, position);
                        }

                    }
                });
            } catch (Exception e) {
            }
        }

    }

    @Override
    public int getItemCount() {
        return mresponceDatumList.size();
    }

    public void setOnItemClickListener(rv_interface rvsInterface) {
        rvInterface = rvsInterface;

    }

    public interface OnClick {
        void OnItemClick(View view, int position);
    }

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;

    }

    public void removeAt(int position) {
        mresponceDatumList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mresponceDatumList.size());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CRowMyCartItemBinding binding;

        MyViewHolder(View itemView, CRowMyCartItemBinding itemBinding) {
            super(itemView);
            binding = itemBinding;

        }
    }
}
