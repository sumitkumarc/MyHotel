package ontime.app.customer.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import ontime.app.R;
import ontime.app.customer.doneActivity.PaymentActivity;
import ontime.app.databinding.CRowMyCartItemBinding;
import ontime.app.databinding.CRowRestaurantItemBinding;
import ontime.app.databinding.RowCardDetailsBinding;
import ontime.app.sqllight.CardDetails;
import ontime.app.sqllight.DBManager;
import ontime.app.utils.Common;

import java.util.ArrayList;

public class RvCreditCardListAdapter extends RecyclerView.Adapter<RvCreditCardListAdapter.MyViewHolder> {
    Context mContext;
    ArrayList<CardDetails> mCursor;

    public RvCreditCardListAdapter(Context context,  ArrayList<CardDetails> cursor) {
        mContext = context;
        mCursor = cursor;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_card_details, parent, false);
        return new MyViewHolder(itemView, RowCardDetailsBinding.bind(itemView));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        if (Common.MERCHANT_TYPE == 1) {
            holder.binding.llDelete.setBackground(mContext.getResources().getDrawable(R.drawable.rest_delete_bg));
        } else {
            holder.binding.llDelete.setBackground(mContext.getResources().getDrawable(R.drawable.super_delete_bg));
        }
        holder.binding.txtCardno.setText(""+mCursor.get(position).getCard_number());
        holder.binding.txtCardname.setText(""+mCursor.get(position).getName());
        holder.binding.txtCarddate.setText(""+mCursor.get(position).getExpiry_Date());
        holder.binding.llDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentActivity.dbManager.deleteContact(mCursor.get(position).getId());
                PaymentActivity.showlistdata(mContext);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCursor.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        RowCardDetailsBinding binding;

        MyViewHolder(View itemView, RowCardDetailsBinding itemBinding) {
            super(itemView);
            binding = itemBinding;

        }
    }
}
