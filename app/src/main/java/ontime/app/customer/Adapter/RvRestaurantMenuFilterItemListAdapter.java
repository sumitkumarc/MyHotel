package ontime.app.customer.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import ontime.app.R;
import ontime.app.customer.doneActivity.RestaurantDetailActivity;
import ontime.app.databinding.CRowMenuDishesBinding;
import ontime.app.model.usermain.UserRestData;
import ontime.app.model.usernewmain.UserItem;
import ontime.app.utils.Common;
import ontime.app.utils.LanguageManager;

import java.util.List;
import java.util.Locale;

import static ontime.app.utils.Common.DELIVER_TYPE;

public class RvRestaurantMenuFilterItemListAdapter extends RecyclerView.Adapter<RvRestaurantMenuFilterItemListAdapter.MyViewHolder> {
    Context mContext;
    List<UserItem>  mresponceDatumList;
    Locale locale;

    public RvRestaurantMenuFilterItemListAdapter(Context context, List<UserItem>  responceData) {
        this.mContext = context;
        this.mresponceDatumList = responceData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.c_row_menu_dishes, parent, false);
        return new MyViewHolder(itemView, CRowMenuDishesBinding.bind(itemView));
    }
    public void showDialogDoyouwant(int position) {
        final Dialog dialogs = new Dialog(mContext);
        dialogs.setCancelable(false);
        dialogs.setContentView(R.layout.popup_doyouwant);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogs.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        dialogs.getWindow().setAttributes(lp);
        dialogs.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialogs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tvtake = dialogs.findViewById(R.id.tvtake);

        TextView tvdine = dialogs.findViewById(R.id.tvdine);


        if (Common.MERCHANT_TYPE == 1) {
            tvtake.setBackground(mContext.getResources().getDrawable(R.drawable.btn_rest_pop));
            tvdine.setBackground(mContext.getResources().getDrawable(R.drawable.btn_rest_pop));
        } else {
            tvtake.setBackground(mContext.getResources().getDrawable(R.drawable.btn_super_pop));
            tvdine.setBackground(mContext.getResources().getDrawable(R.drawable.btn_super_pop));
        }
        tvtake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DELIVER_TYPE = "take away";
                Intent i2 = new Intent(mContext, RestaurantDetailActivity.class);
                i2.putExtra("MENU_ID" , mresponceDatumList.get(position).getMenuId());
                i2.putExtra("ITEM_ID" , mresponceDatumList.get(position).getId());
                i2.putExtra("UPDATE_ITEM", 1);
                mContext.startActivity(i2);
                dialogs.dismiss();
            }
        });
        tvdine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DELIVER_TYPE = "dine in";
                Intent i2 = new Intent(mContext, RestaurantDetailActivity.class);
                i2.putExtra("MENU_ID" , mresponceDatumList.get(position).getMenuId());
                i2.putExtra("ITEM_ID" , mresponceDatumList.get(position).getId());
                i2.putExtra("UPDATE_ITEM", 1);
                mContext.startActivity(i2);
                dialogs.dismiss();
            }
        });
        dialogs.show();
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showDialogDoyouwant(position);
                Intent i2 = new Intent(mContext, RestaurantDetailActivity.class);
                i2.putExtra("MENU_ID" , mresponceDatumList.get(position).getMenuId());
                i2.putExtra("ITEM_ID" , mresponceDatumList.get(position).getId());
                i2.putExtra("UPDATE_ITEM" , 1);
                mContext.startActivity(i2);
            }
        });
        if (Common.MERCHANT_TYPE == 1) {
            holder.binding.txtArName.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            holder.binding.txtEnName.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        } else {
            holder.binding.txtArName.setTextColor(mContext.getResources().getColor(R.color.super_mart));
            holder.binding.txtEnName.setTextColor(mContext.getResources().getColor(R.color.super_mart));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = mContext.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = mContext.getResources().getConfiguration().locale;
        }
        new Runnable() {
            @Override
            public void run() {
                if (locale.getLanguage().equals(LanguageManager.LANGUAGE_KEY_ARABIC)) {
                    holder.binding.txtEnName.setVisibility(View.GONE);
                    holder.binding.txtArName.setVisibility(View.VISIBLE);
                } else {
                    holder.binding.txtEnName.setVisibility(View.VISIBLE);
                    holder.binding.txtArName.setVisibility(View.GONE);
                }
            }
        };
        holder.binding.txtArName.setText(Common.isStrempty(mresponceDatumList.get(position).getItemName()));
        holder.binding.txtEnName.setText(Common.isStrempty(mresponceDatumList.get(position).getItemName()));
        holder.binding.txtPrice.setText("SR " +Common.isStrempty(mresponceDatumList.get(position).getPrice()));
        Glide.with(mContext).load(mresponceDatumList.get(position).getImage()).centerCrop().placeholder(R.drawable.people).into(holder.binding.ivRestMenu);
    }

    @Override
    public int getItemCount() {
        return mresponceDatumList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        CRowMenuDishesBinding binding;

        MyViewHolder(View itemView, CRowMenuDishesBinding itemBinding) {
            super(itemView);
            binding = itemBinding;

        }
    }
}
