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
    private final Activity root;
    private final List<User> users;
    private final User self;

    public ProfileAdapter(Activity activity, List<User> users, User self) {
        root = activity;
        this.users = users;
        this.self = self;
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
        PictureViewerAdapter picAdapter = new PictureViewerAdapter(getPics(user));
        holder.picContainer.setAdapter(picAdapter);
        ListViewerAdapter listAdapter = new ListViewerAdapter(getQAList(user));
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            picContainer = itemView.findViewById(R.id.pic_container);
            name = itemView.findViewById(R.id.user_name);
            nearby = itemView.findViewById(R.id.nearby);
            listContainer = itemView.findViewById(R.id.list_container);
        }
    }

    private List<Bitmap> getPics(User user) {
        List<Bitmap> pics = da.getUserPics(user.id);
        if (pics.size() == 0) {
            Toast.makeText(root, "this user didn't upload any picture", Toast.LENGTH_SHORT).show();
        }
        return pics;
    }

    private List<QA> getQAList(User user) {
        List<QA> qaList = da.getDisplayQuestions(user.id);
        if (qaList.size() == 0) {
            Toast.makeText(root, "this user do not want to display any question", Toast.LENGTH_SHORT).show();
        }
        return qaList;
    }
}
