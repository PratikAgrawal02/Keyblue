package com.sih.disaster;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class Earthquake extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public Earthquake(Context context){
        this.context=context;
    }
    public String slidetop = "Earthquake";
    public int[] slide_top= {R.drawable.bg_top_first,R.drawable.bg_top_secound,R.drawable.bg_top_third};
    public int[] slide_images = {
            R.drawable.drop,
            R.drawable.cover,
            R.drawable.hold
    };
    public String[] slide_headings = {
            "Drop under heavy furniture such as a table, desk, bed or any solid furniture",
            "Cover your head and torso to prevent being hit by falling objects",
            "Hold onto the object that you are under so that you remain covered"
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout,container, false);
        ImageView slideimageView = (ImageView) view.findViewById(R.id.slidecurrimage);
        TextView slideheading = (TextView) view.findViewById(R.id.slidecurrtext);
        TextView sildetop = (TextView) view.findViewById(R.id.headingtop);
        LinearLayout linearLayout= (LinearLayout) view.findViewById(R.id.ln1);
        slideimageView.setImageResource(slide_images[position]);
        slideheading.setText(slide_headings[position]);
        sildetop.setText(""+slidetop);
        linearLayout.setBackgroundResource(slide_top[position]);
        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
