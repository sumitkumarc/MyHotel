package ontime.app.customer.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import ontime.app.R;
import ontime.app.databinding.CRowCategoryListItemBinding;
import ontime.app.model.usermain.UserRestaurantProCategory;
import ontime.app.rv_interface;
import ontime.app.utils.Common;

import java.util.List;

public class RvRestaurantMenuFilterListAdapter extends RecyclerView.Adapter<RvRestaurantMenuFilterListAdapter.MyViewHolder> {
    Context mContext;
    int row_index;
    List<UserRestaurantProCategory> mCategories;
    rv_interface rvInterface;

    public RvRestaurantMenuFilterListAdapter(Context context, List<UserRestaurantProCategory> categories) {
        this.mContext = context;
        this.mCategories = categories;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.c_row_category_list_item, parent, false);
        return new MyViewHolder(itemView, CRowCategoryListItemBinding.bind(itemView));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = position;
                notifyDataSetChanged();
            }
        });
        if (row_index == position) {
            rvInterface.OnItemClick(position, mCategories.get(position).getId());
            if (Common.MERCHANT_TYPE == 1) {
                holder.binding.cdMainCard.setCardBackgroundColor(Color.parseColor("#FFFF0015"));
                holder.binding.txtCatName.setTextColor(Color.parseColor("#ffffff"));
                holder.binding.ivCatImag.setColorFilter(Color.parseColor("#ffffff"));
            } else {
                holder.binding.cdMainCard.setCardBackgroundColor(Color.parseColor("#FE8B00"));
                holder.binding.txtCatName.setTextColor(Color.parseColor("#ffffff"));
                //  holder.binding.ivCatImag.setColorFilter(Color.parseColor("#ffffff"));
            }

        } else {
            if (Common.MERCHANT_TYPE == 1) {
                holder.binding.cdMainCard.setCardBackgroundColor(Color.parseColor("#ffffff"));
                holder.binding.txtCatName.setTextColor(Color.parseColor("#000000"));
                holder.binding.ivCatImag.setColorFilter(Color.parseColor("#000000"));
            }else {
                holder.binding.cdMainCard.setCardBackgroundColor(Color.parseColor("#ffffff"));
                holder.binding.txtCatName.setTextColor(Color.parseColor("#000000"));
            }
        }
        holder.binding.txtCatName.setText(mCategories.get(position).getName());
        Glide.with(mContext).load(mCategories.get(position).getIcon()).centerCrop().placeholder(R.mipmap.image_placeholder_default).into(holder.binding.ivCatImag);
    }

    public void setOnItemClickListener(rv_interface rvsInterface) {
        rvInterface = rvsInterface;

    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        CRowCategoryListItemBinding binding;

        MyViewHolder(View itemView, CRowCategoryListItemBinding itemBinding) {
            super(itemView);
            binding = itemBinding;

        }
    }
}
