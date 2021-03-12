package ontime.app.customer.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import ontime.app.R;
import ontime.app.databinding.CRowReviewItemBinding;
import ontime.app.model.usermain.Rest_Datum;
import ontime.app.utils.Common;

public class RvRestRatingListAdapter extends RecyclerView.Adapter<RvRestRatingListAdapter.MyViewHolder> {
    Context mcontext;
    List<Rest_Datum> mRest_data;

    public RvRestRatingListAdapter(Context context, List<Rest_Datum> rest_data) {
        mcontext = context;
        mRest_data = rest_data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.c_row_review_item, parent, false);
        return new MyViewHolder(itemView, CRowReviewItemBinding.bind(itemView));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        try {
            if (Common.MERCHANT_TYPE == 1) {
                Drawable drawable = holder.binding.rbHomePro.getProgressDrawable();
                drawable.setColorFilter(Color.parseColor("#FF0015"), PorterDuff.Mode.SRC_ATOP);
            } else {
                Drawable drawable = holder.binding.rbHomePro.getProgressDrawable();
                drawable.setColorFilter(Color.parseColor("#FE8B00"), PorterDuff.Mode.SRC_IN);
//            DrawableCompat.setTint(drawable, ContextCompat.getColor(mContext, R.color.super_mart));
            }
            holder.binding.tvUserName.setText(mRest_data.get(position).getUser().getFullName());
            holder.binding.tvTitle.setText(Common.isStrempty(mRest_data.get(position).getReview()));
            holder.binding.rbHomePro.setRating(Float.parseFloat(Common.isStrempty(mRest_data.get(position).getRate())));
            Glide.with(mcontext).load(Common.isStrempty(mRest_data.get(position).getUser().getImage())).centerCrop().placeholder(R.mipmap.image_placeholder_default).into(holder.binding.ivProfile);
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return mRest_data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CRowReviewItemBinding binding;

        MyViewHolder(View itemView, CRowReviewItemBinding itemBinding) {
            super(itemView);
            binding = itemBinding;

        }
    }
}
