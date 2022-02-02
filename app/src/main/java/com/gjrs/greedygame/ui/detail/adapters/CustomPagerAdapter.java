package com.gjrs.greedygame.ui.detail.adapters;

import static com.gjrs.greedygame.util.Constants.POSTER_BASE_URL;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;
import com.gjrs.greedygame.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomPagerAdapter extends PagerAdapter {

    private final Context context;
    private LayoutInflater mLayoutInflater;
    private String imgUrl;
    private List<String> bannerList;
    private static final String TAG = CustomPagerAdapter.class.getSimpleName();

    public CustomPagerAdapter(Context context, List<String> resources) {

        this.context = context;
        this.bannerList = resources;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return bannerList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        Log.d(TAG, "banner url : " + imgUrl + bannerList.get(position));

        Picasso.get()
                .load(bannerList.get(position))
                .error(R.drawable.test)
                .placeholder(R.drawable.test)
                .into(imageView);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((CardView) object);
    }
}
