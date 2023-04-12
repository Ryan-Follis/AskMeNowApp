package com.example.askmenow;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.askmeknow.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private final String[] names;
    private final Activity context;

    public SearchAdapter(Activity context, String[] names) {
        this.context = context;
        this.names = names;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_row, parent, false);
        return new SearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.rowText.setText(names[position]);
        holder.rowText.setOnClickListener((View view)->{
            Intent intent = new Intent(context, SearchResultActivity.class);
            intent.putExtra("id", String.valueOf(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView rowText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rowText = itemView.findViewById(R.id.search_row);
        }
    }
}
