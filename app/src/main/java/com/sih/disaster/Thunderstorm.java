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

public class Thunderstorm extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public Thunderstorm(Context context){
        this.context=context;
    }
    public String slidetop = "Thunderstorm";
    public int[] slide_top= {R.drawable.bg_top_first,R.drawable.bg_top_secound,R.drawable.bg_top_third,R.drawable.bg_top_secound,R.drawable.bg_top_third};
    public int[] slide_images = {
            R.drawable.thun_01,
            R.drawable.thund_02,
            R.drawable.thun_03,
            R.drawable.thund_04,
            R.drawable.thund_05
    };
    public String[] slide_headings = {
            "Move to a lower elevation. Lightning is much more likely to strike objects at higher elevations. Do what you can do get as low as possible.",
            "It is nor safe carrying an umbrella during a thunderstorm. ",
            "Never lie flat on the ground. Crouch down in a ball-like position with your head tucked and hands over your ears",
            "Seek shelter immediately, even if caught out in the open.",
            "Stay away from water bodies"
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
