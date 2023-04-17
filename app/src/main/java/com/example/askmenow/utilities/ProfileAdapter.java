package com.example.askmenow.utilities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.askmenow.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // configure sub-pageviewers
        PictureViewerAdapter picAdapter = new PictureViewerAdapter(0);
        holder.picContainer.setAdapter(picAdapter);
        ListViewerAdapter listAdapter = new ListViewerAdapter(0);
        holder.listContainer.setAdapter(listAdapter);

        holder.picContainer.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        holder.listContainer.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        // update basic info
        holder.name.setText("test name " + position);
        holder.age.setText(String.valueOf(position * 5));
        if (position % 3 == 0)
            holder.nearby.setText("nearby");
        else
            holder.nearby.setText("");
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final ViewPager2 picContainer;
        private final TextView name;
        private final TextView age;
        private final TextView nearby;
        private final ViewPager2 listContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            picContainer = itemView.findViewById(R.id.pic_container);
            name = itemView.findViewById(R.id.user_name);
            age = itemView.findViewById(R.id.user_age);
            nearby = itemView.findViewById(R.id.nearby);
            listContainer = itemView.findViewById(R.id.list_container);
        }
    }
}
