package com.example.askmenow.utilities;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.askmenow.R;
import com.example.askmenow.firebase.DataAccess;
import com.example.askmenow.model.QA;
import com.example.askmenow.model.User;
import com.google.android.flexbox.FlexboxLayout;

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
        holder.name.setText(user.username);
        if (user.age > 0)
            holder.age.setText(String.valueOf(user.age));
        if (da.checkLocation(self.id, user.id))
            holder.nearby.setText("nearby");
        else
            holder.nearby.setText("");
        holder.interests.setOnClickListener(view -> {
            Dialog interestPopup = new Dialog(context);
            interestPopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
            interestPopup.setContentView(R.layout.interest_popup);
            interestPopup.setCanceledOnTouchOutside(true);
            FlexboxLayout layout = interestPopup.findViewById(R.id.interest_popup);
            if (user.interests != null) {
                for (String interest : user.interests) {
                    Button button = new Button(context);
                    button.setLayoutParams(new FlexboxLayout.LayoutParams(
                            FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT));
                    button.setText(interest);
                    button.setTextColor(R.color.black);
                    button.setBackgroundResource(R.drawable.interest_button);
                    button.setOnClickListener(v -> {

                    });
                    layout.addView(button);
                }
            }
            interestPopup.show();
        });
    }

    @Override
    public int getItemCount() {
         return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final ViewPager2 picContainer;
        private final TextView name;
        private final TextView age;
        private final TextView nearby;
        private final ImageButton interests;
        private final ViewPager2 listContainer;
        private final View loadImage;
        private final View loadQA;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            picContainer = itemView.findViewById(R.id.pic_container);
            name = itemView.findViewById(R.id.user_name);
            age = itemView.findViewById(R.id.user_age);
            nearby = itemView.findViewById(R.id.nearby);
            listContainer = itemView.findViewById(R.id.list_container);
            loadImage = itemView.findViewById(R.id.load_image);
            loadQA = itemView.findViewById(R.id.load_qa);
            interests = itemView.findViewById(R.id.show_interest);
        }
    }
}
