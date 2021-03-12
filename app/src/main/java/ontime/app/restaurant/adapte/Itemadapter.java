package ontime.app.restaurant.adapte;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;

import ontime.app.R;


public class Itemadapter extends PagerAdapter {
    Context contet;
    LayoutInflater mLayoutInflater;
    String[] mStrings;

    public Itemadapter(FragmentActivity mainFragment, String[] strings) {
        this.mStrings =strings;
        this.contet = mainFragment;
        mLayoutInflater = (LayoutInflater) contet.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mStrings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.r_status_item_layout, container, false);

        TextView txt_title = itemView.findViewById(R.id.txt_title);
        txt_title.setText(mStrings[position].toString());
//        final ImageView imageView = (ImageView) itemView.findViewById(R.id.category_imageee);
//        Glide.with(contet).load(silderlist.get(position).getImagePath() + silderlist.get(position).getSliderImage()).into(imageView);


        container.addView(itemView);


        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

}
