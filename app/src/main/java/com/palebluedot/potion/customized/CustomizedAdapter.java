package com.palebluedot.potion.customized;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.palebluedot.clientsdk.model.CustomizedResultItem;
import com.palebluedot.potion.R;
import java.util.List;

public class CustomizedAdapter extends BaseAdapter {
    private final List<CustomizedResultItem> mData;

    public CustomizedAdapter(List<CustomizedResultItem> data) {
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    public Object getItem(int position){
        return mData.get(position);

    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_customized, parent, false);
            TextView nameText = convertView.findViewById(R.id.customized_name_text);
            TextView effectText = convertView.findViewById(R.id.customized_effect_text);

            holder.nameText = nameText;
            holder.effectText = effectText;
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        CustomizedResultItem customized = mData.get(position);
        holder.nameText.setText(customized.getName());
        holder.effectText.setText(customized.getEffect());
        return convertView;
    }

    static class ViewHolder{
        TextView nameText;
        TextView effectText;
    }
}
