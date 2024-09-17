package com.example.projet_gouv.ui.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projet_gouv.R;
import com.example.projet_gouv.data.Rappel;

import java.util.List;

public class DashboardRecyclerAdapter extends RecyclerView.Adapter<DashboardRecyclerAdapter.ViewHolder> {

    private List<Rappel> rappelList;

    public DashboardRecyclerAdapter() {}

    private OnItemClickListener onItemClickListener;


    public void setRappelList(List<Rappel> rappelList) {
        this.rappelList = rappelList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_favoris, parent, false);
        return new ViewHolder(view);
    }

    public interface OnItemClickListener {
        void onItemClick(Rappel rappel);

        void onFavoriteButtonClick(Rappel rappel);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Rappel rappel = rappelList.get(position);
        holder.titleTextView.setText(rappel.getNomProduit());
        holder.dateTextView.setText(rappel.getDateRappel());

        Glide.with(holder.itemView.getContext())
                .load(rappel.getLienImage())
                .into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(rappel);
                }
            }
        });

        // OnClickListener pour le bouton Retirer des Favoris
        holder.favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onFavoriteButtonClick(rappel);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (rappelList == null) {
            return 0;
        }
        return rappelList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView dateTextView;
        ImageView imageView;
        Button favoriteButton;

        ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.item_title);
            dateTextView = itemView.findViewById(R.id.item_detail);
            imageView = itemView.findViewById(R.id.item_image);
            favoriteButton = itemView.findViewById(R.id.buttonFavorite);
        }
    }

    public List<Rappel> getRappelList() {
        return rappelList;
    }

}
