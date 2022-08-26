package com.sih.disaster;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class adapter  extends BaseAdapter {

    Context context;
    String number[];
    int images;
    LayoutInflater inflater;

    public adapter(Context ctx, String[] numbers, int images){
        this.context = ctx;
        this.number = numbers;
        this.images = images;
        inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return number.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.activity_custom_list_view,null);
        TextView txtView = (TextView) view.findViewById(R.id.textView);
        ImageView image = (ImageView) view.findViewById(R.id.iconer);
        txtView.setText(number[i]);
        image.setImageResource(images);
        return view;
    }
}
