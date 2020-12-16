package com.palebluedot.potion.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.palebluedot.clientsdk.model.Material;
import com.palebluedot.clientsdk.model.MaterialResultItem;
import com.palebluedot.potion.R;

import java.util.List;

public class SearchMaterialAdapter extends BaseAdapter {
    private final List<MaterialResultItem> mData;       //Material has List<MaterialResultItem>

    public SearchMaterialAdapter(Material response) {
        this.mData = response.getResult();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public MaterialResultItem getItem(int position){
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
                    .inflate(R.layout.item_search_material, parent, false);
            TextView productText = convertView.findViewById(R.id.item_product);
            TextView effectText = convertView.findViewById(R.id.item_effect);

            holder.productText = productText;
            holder.effectText = effectText;
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        MaterialResultItem material = mData.get(position);
        holder.productText.setText(material.getName());
        holder.effectText.setText(material.getEffect());
        return convertView;
    }

    static class ViewHolder{
        TextView effectText;
        TextView productText;
    }
}
