package ontime.app.customer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import ontime.app.R;
import ontime.app.customer.doneActivity.RestaurantProfileActivity;
import ontime.app.databinding.CRowRestaurantItemBinding;
import ontime.app.model.usermain.UserRestaurantsData;
import ontime.app.utils.Common;

import java.util.List;

public class RvRestaurantListAdapter extends RecyclerView.Adapter<RvRestaurantListAdapter.MyViewHolder> {
    Context mContext;
    List<UserRestaurantsData> mresponceDatumList;


    public RvRestaurantListAdapter(Context context, List<UserRestaurantsData> responceData) {
        this.mContext = context;
        this.mresponceDatumList = responceData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.c_row_restaurant_item, parent, false);
        return new MyViewHolder(itemView, CRowRestaurantItemBinding.bind(itemView));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        if (Common.MERCHANT_TYPE == 1) {
            holder.binding.txtName.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            Drawable drawable = holder.binding.rbStar.getProgressDrawable();
            drawable.setColorFilter(Color.parseColor("#FF0015"), PorterDuff.Mode.SRC_ATOP);
        } else {
            holder.binding.txtName.setTextColor(mContext.getResources().getColor(R.color.super_mart));
            Drawable drawable = holder.binding.rbStar.getProgressDrawable();
            drawable.setColorFilter(Color.parseColor("#FE8B00"), PorterDuff.Mode.SRC_IN);
//            DrawableCompat.setTint(drawable, ContextCompat.getColor(mContext, R.color.super_mart));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.RESTAURANT_ID = mresponceDatumList.get(position).getId();
                Common.DELIVER_TYPE ="";
                Intent i2 = new Intent(mContext, RestaurantProfileActivity.class);
                i2.putExtra("RE_NAME", mresponceDatumList.get(position).getName());
                i2.putExtra("RE_ID", mresponceDatumList.get(position).getId());
                i2.putExtra("RE_ADDRESS", mresponceDatumList.get(position).getBranchName());
                mContext.startActivity(i2);
            }
        });
        try {
//            float d= (float) ((Integer.parseInt(mresponceDatumList.get(position).getAvgRate())*5) /100);
            holder.binding.rbStar.setRating((float) Float.parseFloat(mresponceDatumList.get(position).getAvgRate()));
//            holder.binding.rbStar.setRating(Float.parseFloat(mresponceDatumList.get(position).getAvgRate()));
        } catch (Exception e) {
        }

        holder.binding.txtName.setText(Common.isStrempty(mresponceDatumList.get(position).getName()));
        holder.binding.txtAddress.setText(Common.isStrempty(mresponceDatumList.get(position).getBranchName()));
        Glide.with(mContext).load(mresponceDatumList.get(position).getImage()).centerCrop().placeholder(R.mipmap.image_placeholder_default).into(holder.binding.ivRestImg);
    }

    @Override
    public int getItemCount() {
        return mresponceDatumList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        CRowRestaurantItemBinding binding;

        MyViewHolder(View itemView, CRowRestaurantItemBinding itemBinding) {
            super(itemView);
            binding = itemBinding;

        }
    }
}
