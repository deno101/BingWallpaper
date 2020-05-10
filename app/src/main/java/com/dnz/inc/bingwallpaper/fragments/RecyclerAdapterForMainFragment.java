package com.dnz.inc.bingwallpaper.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dnz.inc.bingwallpaper.MainActivity;
import com.dnz.inc.bingwallpaper.R;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapterForMainFragment extends RecyclerView.Adapter<RecyclerAdapterForMainFragment.CardVieHolder> {


    public RecyclerAdapterForMainFragment() {

    }

    @NonNull
    @Override
    public CardVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CardVieHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.
                view_holder_for_main_recycler_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CardVieHolder holder, int position) {
        holder.bingImage.setImageBitmap(MainFragment.dataStore.getBitmap(position));
        holder.imageDescription.setText(MainFragment.dataStore.getTitle(position));
        holder.pictureDate.setText(MainFragment.dataStore.getDate(position));
    }

    @Override
    public int getItemCount() {
        return MainFragment.dataStore.length();
    }

    public class CardVieHolder extends RecyclerView.ViewHolder {

        public CardView cardContainer;
        public ImageView bingImage, favorites;
        public TextView pictureDate, imageDescription;

        public CardVieHolder(@NonNull View itemView) {
            super(itemView);

            cardContainer = itemView.findViewById(R.id.card_container);
            bingImage = itemView.findViewById(R.id.image_view_for_main_image);
            favorites = itemView.findViewById(R.id.card_image_view_for_favorites);
            pictureDate = itemView.findViewById(R.id.card_text_view_for_date);
            imageDescription = itemView.findViewById(R.id.card_text_view_for_image_desc);
        }
    }
}
