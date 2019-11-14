package de.hsrm.lback.myapplication.helpers.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import de.hsrm.lback.myapplication.R;
import de.hsrm.lback.myapplication.helpers.ResourcesHelper;

public class LocationLogoAdapter extends RecyclerView.Adapter<LocationLogoAdapter.ViewHolder> {
    private List<String> logoList;
    private LogoChosenCallback listener;

    public LocationLogoAdapter(List<String> logoList, LogoChosenCallback listener) {
        this.logoList = logoList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        ImageView itemView =
                (ImageView) inflater.inflate(R.layout.component_location_logo, parent, false);
        itemView.setOnClickListener(view -> listener.onLogoChosen((String) view.getTag()));

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String logoString = logoList.get(i);

        int resId = ResourcesHelper.getResId(logoString, R.drawable.class);

        viewHolder.itemView.setImageResource(resId);
        viewHolder.itemView.setTag(logoString);
    }

    @Override
    public int getItemCount() {
        return logoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView itemView;
        public ViewHolder(@NonNull ImageView itemView) {
            super(itemView);
            this.itemView = itemView;
        }

    }

    public interface LogoChosenCallback {
        void onLogoChosen(String logo);
    }
}
