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

public class Landslides extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public Landslides(Context context){
        this.context=context;
    }
    public String slidetop = "Landslides";
    public int[] slide_top= {R.drawable.bg_top_first,R.drawable.bg_top_secound,R.drawable.bg_top_third};
    public int[] slide_images = {
            R.drawable.land1,
            R.drawable.land2,
            R.drawable.land3
    };
    public String[] slide_headings = {
            "Avoid going near slide area.",
            "Avoid the surrounding area, building or electric cable near the slide area",
            "Watch out for flooding and debris flow which come along with landslide."
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
