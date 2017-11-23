package com.ad_imagine.voropecom.fragment_coach;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ad_imagine.voropecom.R;
import com.ad_imagine.voropecom.fragment_coach.coache_historique_item;

import java.util.List;
public class coache_historique_adapter extends ArrayAdapter<coache_historique_item> {
    private int resourceId;


    public coache_historique_adapter(Context context, int textViewResourceId, List<coache_historique_item> lstItem) {
        super(context, textViewResourceId, lstItem);
        resourceId = textViewResourceId;// coache_historique_adapter adapter = new coache_historique_adapter(coache_historique.this, R.layout.coache_listview,listSliding);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

       coache_historique_item item = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view= LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.img = (ImageView) view.findViewById
                    (R.id.item_img2);
            viewHolder.tv = (TextView) view.findViewById
                    (R.id.coache_number);
            viewHolder.tv2 = (TextView) view.findViewById
                    (R.id.coache_note2);

            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
        viewHolder. img.setImageResource(item.getImgId());
        viewHolder.tv.setText(item.getTitle());
        viewHolder.tv2.setText(item.getTitle2());


//        View v = LayoutInflater.from(getContext()).inflate(resourceId, null);
//
//        ImageView img = (ImageView) v.findViewById(R.id.item_img2);
//        TextView tv = (TextView) v.findViewById(R.id.coache_number);
//        TextView tv2 = (TextView) v.findViewById(R.id.coache_note2);
//
//
//
//        img.setImageResource(item.getImgId());
//        tv.setText(item.getTitle());
//        tv2.setText(item.getTitle2());


        //change color of note
        if (Float.valueOf(viewHolder.tv2.getText().toString()) <=3) {
            viewHolder.tv.setBackgroundColor(Color.parseColor("#40b83d"));
        }
        if ((Float.valueOf(viewHolder.tv2.getText().toString()) <=6) && (Float.valueOf(viewHolder.tv2.getText().toString()) > 3)) {
            viewHolder.tv.setBackgroundColor(Color.parseColor("#f57f20"));
        }
        if (Float.valueOf(viewHolder.tv2.getText().toString()) > 6) {
            viewHolder.tv.setBackgroundColor(Color.parseColor("#ed282b"));
        }
        return view;

    }
    class ViewHolder {
        ImageView img;
        TextView tv ;
        TextView tv2;

    }
}
