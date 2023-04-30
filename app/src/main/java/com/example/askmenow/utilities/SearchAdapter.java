package com.example.askmenow.utilities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.askmenow.R;
import com.example.askmenow.activities.MainActivity;
import com.example.askmenow.model.User;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private final List<User> users;
    private final Activity context;

    public SearchAdapter(Activity context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        holder.rowText.setText(user.username);
        // decode image
        byte[] bytes = Base64.getDecoder().decode(user.image);
        Bitmap img = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        holder.userImage.setImageBitmap(img);

        holder.rowText.setOnClickListener((View view)->{
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("id", user.id);
            intent.putExtra("dest", "search result");
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView rowText;
        private final ImageView userImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rowText = itemView.findViewById(R.id.search_row);
            userImage = itemView.findViewById(R.id.search_user_image);
        }
    }
}
