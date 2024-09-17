package com.example.projet_gouv.ui.home;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projet_gouv.R;
import com.example.projet_gouv.data.Rappel;
import com.example.projet_gouv.ui.dashboard.DashboardRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;
public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder> {
    private List<Rappel> rappelList;
    private List<Boolean> favorisStateList; // liste d'état des favoris
    private OnItemClickListener onItemClickListener;
    public HomeRecyclerAdapter() {
        rappelList = new ArrayList<>();
        favorisStateList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new HomeRecyclerAdapter.ViewHolder(view);
    }
    public interface OnItemClickListener {
        void onItemClick(Rappel rappel);
        void onFavoriteButtonClick(Rappel rappel);

    }
    public void setOnItemClickListener(HomeRecyclerAdapter.OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRecyclerAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Rappel rappel = rappelList.get(position);
        holder.titleTextView.setText(rappel.getNomProduit());
        holder.dateTextView.setText(rappel.getDateRappel());
        Glide.with(holder.itemView.getContext())
                .load(rappel.getLienImage())
                .into(holder.imageView);

        // Mise à jour de l'image du bouton favori en fonction de l'état du rappel
        if (favorisStateList.get(position)) {
            holder.favoriteButton.setImageResource(R.drawable.ic_favorite_full);
        } else {
            holder.favoriteButton.setImageResource(R.drawable.ic_favorite_empty);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(rappel);
                }
            }
        });

        // OnClickListener pour le bouton Favoris
        holder.favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onFavoriteButtonClick(rappel);
                    // Mettre à jour l'état du favori après le clic sur le bouton
                    favorisStateList.set(position, !favorisStateList.get(position));
                    notifyDataSetChanged(); // Notifie l'adaptateur des changements
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (rappelList == null) {
            return 0;
        } else {
            return rappelList.size();
        }
    }

    public void setRappelList(List<Rappel> rappels) {
        if (rappels != null) {
            this.rappelList = rappels;
            // Mettre à jour la liste d'état des favoris avec la même taille que la liste de rappels
            favorisStateList.clear();
            for (int i = 0; i < rappels.size(); i++) {
                favorisStateList.add(false); // Par défaut, aucun rappel n'est favorisé
            }
            notifyDataSetChanged();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView dateTextView;
        ImageView imageView;
        ImageButton favoriteButton;


        ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.item_title);
            dateTextView = itemView.findViewById(R.id.item_detail);
            imageView = itemView.findViewById(R.id.item_image);
            favoriteButton = itemView.findViewById(R.id.buttonFavorite);
        }
    }
    //Mets à jour les favoris pour l'affichage des coeurs dans card_layout
    public void updateFavorites(List<Boolean> favorisStateList) {
        this.favorisStateList = favorisStateList;
        notifyDataSetChanged();
    }
}