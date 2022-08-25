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


public class SlideAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public String slidetop = "KeyBlue";
    public SlideAdapter (Context context){
        this.context=context;
    }
    public int[] slide_top= {R.drawable.bg_top_first,R.drawable.bg_top_secound,R.drawable.bg_top_third};
    public int[] slide_images = {
            R.drawable.slider01,
            R.drawable.slider02,
            R.drawable.slider03
    };

    public String[] slide_headings = {
            "An assistant to guide you through Disasters.",
            "Knowing how safe your current location is.",
            "What to do when Disaster Strikes."
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
