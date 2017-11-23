package com.ad_imagine.voropecom.fragment_coach;

/**
 * Created by Ling Qi on 2016/6/20.
 */
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ad_imagine.voropecom.R;
import com.ad_imagine.voropecom.fragment_coach.coach_accueil_Item;

import java.util.List;
public class coach_accueil_adapter extends ArrayAdapter<coach_accueil_Item> {
    private int resourceId;


    public coach_accueil_adapter(Context context, int textViewResourceId, List<coach_accueil_Item> lstItem) {
        super(context, textViewResourceId, lstItem);
        resourceId = textViewResourceId;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//  converView is view of item out
        coach_accueil_Item item = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view= LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.img = (ImageView) view.findViewById
                    (R.id.item_img);
            viewHolder.tv = (TextView) view.findViewById
                    (R.id.item_nom);
            viewHolder.tv2 = (TextView) view.findViewById
                    (R.id.item_comment);
            viewHolder.tv3 = (TextView) view.findViewById
                    (R.id.item_note);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
        viewHolder. img.setImageResource(item.getImgId());
        viewHolder.tv.setText(item.getTitle());
        viewHolder.tv2.setText(item.getTitle2());
        viewHolder.tv3.setText(item.getTitle3());


//        coach_accueil_Item item = getItem(position);
//
//        View v = LayoutInflater.from(getContext()).inflate(resourceId, null);
//
//        ImageView img = (ImageView) v.findViewById(R.id.item_img);
//        TextView tv = (TextView) v.findViewById(R.id.item_nom);
//        TextView tv2 = (TextView) v.findViewById(R.id.item_comment);
//        TextView tv3 = (TextView) v.findViewById(R.id.item_note);
//
//
//        img.setImageResource(item.getImgId());
//        tv.setText(item.getTitle());
//        tv2.setText(item.getTitle2());
//        tv3.setText(item.getTitle3());

        //change color of note
        if (Float.valueOf(viewHolder.tv3.getText().toString()) <= 3) {
            viewHolder.tv3.setBackgroundResource(R.drawable.greennumber);
        }
        if ((Float.valueOf(viewHolder.tv3.getText().toString()) <=6) && (Float.valueOf(viewHolder.tv3.getText().toString()) > 3)) {
            viewHolder.tv3.setBackgroundResource(R.drawable.orangenumber);
        }
        if (Float.valueOf(viewHolder.tv3.getText().toString()) > 6) {
            viewHolder.tv3.setBackgroundResource(R.drawable.rednumber);;
        }
        return view;

    }
    class ViewHolder {
        ImageView img;
        TextView tv ;
        TextView tv2;
        TextView tv3;
    }
}
