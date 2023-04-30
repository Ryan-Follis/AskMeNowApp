package com.example.askmenow.utilities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.askmenow.R;
import com.example.askmenow.firebase.DataAccess;
import com.example.askmenow.model.QA;
import com.example.askmenow.model.User;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    private final DataAccess da = new DataAccess();
    private final Activity context;
    private final List<User> users;
    private final User self;

    public ProfileAdapter(Activity activity, List<User> users, User self) {
        context = activity;
        this.users = users;
        this.self = self;
        da.setRoot(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        // configure sub-pageviewers

        holder.loadImage.setVisibility(View.VISIBLE);
        da.getUserPics(user.id, params -> {
            List<Bitmap> pics = (List<Bitmap>) params[0];
            PictureViewerAdapter picAdapter = new PictureViewerAdapter(pics);
            holder.picContainer.setAdapter(picAdapter);
            holder.loadImage.setVisibility(View.GONE);
        });
        da.getDisplayQuestions(user.id, params -> {
            List<QA> qaList= (List<QA>)params[0];
            ListViewerAdapter listAdapter = new ListViewerAdapter(qaList);
            holder.listContainer.setAdapter(listAdapter);
            holder.loadQA.setVisibility(View.GONE);
        });

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
        holder.name.setText(user.name);
        if (da.checkLocation(self.id, user.id))
            holder.nearby.setText("nearby");
        else
            holder.nearby.setText("");
    }

    @Override
    public int getItemCount() {
         return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final ViewPager2 picContainer;
        private final TextView name;
        private final TextView nearby;
        private final ViewPager2 listContainer;
        private final View loadImage;
        private final View loadQA;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            picContainer = itemView.findViewById(R.id.pic_container);
            name = itemView.findViewById(R.id.user_name);
            nearby = itemView.findViewById(R.id.nearby);
            listContainer = itemView.findViewById(R.id.list_container);
            loadImage = itemView.findViewById(R.id.load_image);
            loadQA = itemView.findViewById(R.id.load_qa);
        }
    }
}
