package lissabuy.com.Core;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import lissabuy.com.R;

public class ImagePagerAdapter extends PagerAdapter {

    private Context context;
    private List<String> images;

    public ImagePagerAdapter(Context context) {
        this.context = context;
        this.images = new ArrayList<>();
    }

    public void setImages(List<String> images) {
        for (int i = 0; i < images.size(); i++){
            this.images.add(images.get(i));
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        String image = images.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.item_image_pager, container, false);
        ImageView ivHeader = (ImageView) itemView.findViewById(R.id.iv_header);

        Glide.with(context).load(image).into(ivHeader);
       // Picasso.with(context)
         //       .load(image)
           //     .into(ivHeader);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}

