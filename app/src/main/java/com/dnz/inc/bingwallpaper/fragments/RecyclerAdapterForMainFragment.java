package com.dnz.inc.bingwallpaper.fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dnz.inc.bingwallpaper.MainActivity;
import com.dnz.inc.bingwallpaper.R;
import com.dnz.inc.bingwallpaper.utils.DataStore;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapterForMainFragment extends RecyclerView.Adapter<RecyclerAdapterForMainFragment.CardVieHolder> {
    private static final String TAG = "RecyclerAdapterForMainF";
    private MainFragment mainFragment;

    public RecyclerAdapterForMainFragment(MainFragment fragment) {
        this.mainFragment = fragment;
    }

    @NonNull
    @Override
    public CardVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CardVieHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.
                view_holder_for_main_recycler_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CardVieHolder holder, int position) {
        DataStore dataStore = mainFragment.dataList.get(position);

        holder.bingImage.setImageBitmap(dataStore.getBitmap());
        holder.imageDescription.setText(dataStore.getTitle());
        holder.pictureDate.setText(dataStore.getDate());
        holder.copyright.setText(dataStore.getCopyright());

    }

    @Override
    public int getItemCount() {
        return mainFragment.dataList.size();
    }

    public class CardVieHolder extends RecyclerView.ViewHolder {

        public CardView cardContainer;
        public ImageView bingImage, favorites;
        public TextView pictureDate, imageDescription, copyright;

        public CardVieHolder(@NonNull View itemView) {
            super(itemView);

            cardContainer = itemView.findViewById(R.id.card_container);
            bingImage = itemView.findViewById(R.id.image_view_for_main_image);
            favorites = itemView.findViewById(R.id.card_image_view_for_favorites);
            pictureDate = itemView.findViewById(R.id.card_text_view_for_date);
            imageDescription = itemView.findViewById(R.id.card_text_view_for_image_desc);
            copyright = itemView.findViewById(R.id.copyright_for_image);

            cardContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String date = pictureDate.getText().toString();
                    String imageDesc = imageDescription.getText().toString();
                    String copyright_msg = copyright.getText().toString();
                    Bitmap bitmap = ( (BitmapDrawable) bingImage.getDrawable()).getBitmap();

                    if (MainActivity.startFragment != null){
                        MainActivity.startFragment.startImageFragment(bitmap,copyright_msg,imageDesc, date);
                    }
                }
            });
        }
    }

    public  synchronized void insertItem(int position){
        if (mainFragment.progressBar != null){
            mainFragment.progressBar.setVisibility(View.INVISIBLE);
        }
        notifyItemInserted(position);
    }
}
