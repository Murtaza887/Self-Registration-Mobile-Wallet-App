package com.murtaza.simplifiedselfregistrationonmobilewallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoritesRvAdapter extends RecyclerView.Adapter<FavoritesRvAdapter.FavoritesViewHolder> {

    List<Favorite> list;
    Context context;

    public FavoritesRvAdapter(List<Favorite> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.favorites, parent, false);
        return new FavoritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {
        holder.imageView.setImageResource(list.get(position).image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class FavoritesViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public FavoritesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
