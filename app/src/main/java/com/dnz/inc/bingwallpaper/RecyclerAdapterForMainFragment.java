package com.dnz.inc.bingwallpaper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

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
        holder.bingImage.setImageResource(R.drawable.two_male_lion);
        holder.imageDescription.setText("kjdvfiuvjkfevnieurvbhjefviuhjkavnljhdvb jd lhbjclauy awcbougvyb auydcgbaoweuvyb");
        holder.pictureDate.setText("10-Sep-2019");
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class CardVieHolder extends RecyclerView.ViewHolder{

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
