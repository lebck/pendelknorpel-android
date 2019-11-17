package de.hsrm.lback.pendelknorpel.helpers.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import de.hsrm.lback.pendelknorpel.R;
import de.hsrm.lback.pendelknorpel.helpers.ResourcesHelper;

public class IconAdapter extends BaseAdapter {

    private List<String> iconNames;
    private View.OnClickListener listener;

    public IconAdapter(List<String> iconNames, View.OnClickListener listener) {
        this.iconNames = iconNames;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return iconNames.size();
    }

    @Override
    public Object getItem(int position) {
        return iconNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return iconNames.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String iconName = iconNames.get(position);
        convertView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.component_location_layout, null);

        ImageView view = convertView.findViewById(R.id.location_logo);

        view.setImageResource(ResourcesHelper.getResId(iconName, R.drawable.class));

        convertView = view;

        convertView.setOnClickListener(listener);

        convertView.setTag(iconName);

        return convertView;
    }
}
