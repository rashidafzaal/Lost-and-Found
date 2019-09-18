package com.example.rashid.lostandfound.Start;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rashid.lostandfound.R;

/**
 * Created by rashid on 5/29/2017.
 */

public class CustomPagerAdapter extends PagerAdapter
{
    private Context mContext;
    LayoutInflater mLayoutInflater;
    private int[] mResources;

    public CustomPagerAdapter(Context context, int[] resources) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResources = resources;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View itemView = mLayoutInflater.inflate(R.layout.pager_item,container,false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        imageView.setImageResource(mResources[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        TextView txt = (TextView) itemView.findViewById(R.id.center_text);
        TextView txt2 = (TextView) itemView.findViewById(R.id.my_text);
        ImageView img = (ImageView) itemView.findViewById(R.id.icons_view);
        if (position ==1)
        {
            txt.setText("  Found any Item \n    somewhere?");
            txt2.setText("Login and Post on App");
            img.setImageResource(R.drawable.img2);
        }
        else if (position ==2)
        {
            txt.setText("Missing Children?");
            txt2.setText("Post here about them");
            img.setImageResource(R.drawable.img3);
        }

           /* LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(950, 950);
            imageView.setLayoutParams(layoutParams);*/
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }


    @Override
    public int getCount() {
        return mResources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
