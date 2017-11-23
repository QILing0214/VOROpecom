package com.ad_imagine.voropecom.chefteam;


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

        import java.util.List;
public class VorTeam_adapter extends ArrayAdapter<VorTeam_item> {
    private int resourceId;


    public VorTeam_adapter(Context context, int textViewResourceId, List<VorTeam_item> lstItem) {
        super(context, textViewResourceId, lstItem);
        resourceId = textViewResourceId;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//  converView is view of item out
        VorTeam_item item = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view= LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();

            viewHolder.tv = (TextView) view.findViewById
                    (R.id.tv_note);
            viewHolder.tv2 = (TextView) view.findViewById
                    (R.id.tv_nom);

            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }

        viewHolder.tv.setText(item.getTitle());
        viewHolder.tv2.setText(item.getTitle2());


        //change color of note
        if (Float.valueOf(viewHolder.tv.getText().toString()) <= 3) {
            viewHolder.tv.setBackgroundResource(R.drawable.coach_coache_raduis_green);
        }
        if ((Float.valueOf(viewHolder.tv.getText().toString()) <=6) && (Float.valueOf(viewHolder.tv.getText().toString()) > 3)) {
            viewHolder.tv.setBackgroundResource(R.drawable.coach_coache_raduis_orange);
        }
        if (Float.valueOf(viewHolder.tv.getText().toString()) > 6) {
            viewHolder.tv.setBackgroundResource(R.drawable.coach_coache_raduis_orange);;
        }
        return view;

    }
    class ViewHolder {

        TextView tv ;
        TextView tv2;

    }
}