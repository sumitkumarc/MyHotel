package ontime.app.customer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import ontime.app.R;
import ontime.app.customer.doneActivity.RestCartPenddingItemActivity;
import ontime.app.databinding.CRowItemCartBinding;
import ontime.app.model.usermain.UserCart;
import ontime.app.model.usermain.UserCartItem;
import ontime.app.rv_interface;
import ontime.app.utils.Common;

import java.util.List;

public class RvRestaurantCartListAdapter extends RecyclerView.Adapter<RvRestaurantCartListAdapter.MyViewHolder> {
    Context mContext;
    List<UserCart> mcartList;
    rv_interface rvInterface;


    public RvRestaurantCartListAdapter(Context context, List<UserCart> cartList) {
        this.mContext = context;
        this.mcartList = cartList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.c_row_item_cart, parent, false);
        return new MyViewHolder(itemView, CRowItemCartBinding.bind(itemView));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.binding.llDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rvInterface.OnItemClick(position, mcartList.get(position).getId());
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.MERCHANT_TYPE = mcartList.get(position).getRestaurant().getType();
                Common.newCartItem = mcartList.get(position).getItems();
                Intent intent = new Intent(mContext, RestCartPenddingItemActivity.class);
                intent.putExtra("POSITION", position);
                mContext.startActivity(intent);
            }
        });
        if (mcartList.get(position).getRestaurant().getType() == 1) {
            holder.binding.txtName.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            holder.binding.llDeleteItem.setBackground(mContext.getResources().getDrawable(R.drawable.btn_golden));
            holder.binding.ivRestProfileImg.setBorderColor(mContext.getResources().getColor(R.color.colorAccent));
        } else {
            holder.binding.txtName.setTextColor(mContext.getResources().getColor(R.color.super_mart));
            holder.binding.llDeleteItem.setBackground(mContext.getResources().getDrawable(R.drawable.btn_red));
            holder.binding.ivRestProfileImg.setBorderColor(mContext.getResources().getColor(R.color.super_mart));
        }
        try {
            holder.binding.txtName.setText(Common.isStrempty(mcartList.get(position).getRestaurant().getName()));
            Glide.with(mContext).load(mcartList.get(position).getRestaurant().getImage()).centerCrop().placeholder(R.mipmap.image_placeholder_default).into(holder.binding.ivRestProfileImg);
            holder.binding.txtAddress.setText(Common.isStrempty(mcartList.get(position).getRestaurant().getBranchName()));
        } catch (Exception e) {
        }

    }

    @Override
    public int getItemCount() {
        return mcartList.size();
    }

    public void setOnItemClickListener(rv_interface rvsInterface) {
        rvInterface = rvsInterface;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CRowItemCartBinding binding;

        MyViewHolder(View itemView, CRowItemCartBinding itemBinding) {
            super(itemView);
            binding = itemBinding;

        }
    }
}
