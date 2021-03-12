package ontime.app.restaurant.adapte;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ontime.app.R;

import java.util.ArrayList;
import java.util.List;


public class DItemadapter extends RecyclerView.Adapter<DItemadapter.ViewHolder> {
    ArrayList<String>  mStrings;
    private int itemHeight;
    private RecyclerView parentRecycler;
    Context mcontext;
    List<Integer> mintegers;

    public DItemadapter(Context context, ArrayList<String> strings, List<Integer> integers) {
        this.mStrings = strings;
        this.mcontext = context;
        this.mintegers = integers;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.r_status_item_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        parentRecycler = recyclerView;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txt_title.setText(mStrings.get(position).toString());
        holder.txt_order_count.setText(mintegers.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return mStrings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView overlay;
        private TextView txt_title;
        private TextView txt_order_count;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_title = itemView.findViewById(R.id.txt_title);
            txt_order_count = itemView.findViewById(R.id.txt_order_count);
            overlay = itemView.findViewById(R.id.overlay);
            itemView.findViewById(R.id.container).setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            parentRecycler.smoothScrollToPosition(getAdapterPosition());
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        public void setOverlayColor(Drawable color) {
            overlay.setBackground(color);
        }
    }


}
