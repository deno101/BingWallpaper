package com.dnz.inc.bingwallpaper.fragments;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

        if (!dataStore.getBool()){
            holder.favorites.setImageResource(R.drawable.ic_heart_paths);
        }else{
            holder.favorites.setImageResource(R.drawable.ic_heart_red);
        }

    }

    @Override
    public int getItemCount() {
        return mainFragment.dataList.size();
    }

    public class CardVieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView bingImage, favorites;
        public TextView pictureDate, imageDescription, copyright;
        private LinearLayout menu;
        private int mHeight;
        private boolean isMenuUp;
        private ValueAnimator anim;

        public CardVieHolder(@NonNull View itemView) {
            super(itemView);

            bingImage = itemView.findViewById(R.id.image_view_for_main_fragment);
            favorites = itemView.findViewById(R.id.card_image_view_for_favorites);
            pictureDate = itemView.findViewById(R.id.card_text_view_for_date);
            imageDescription = itemView.findViewById(R.id.card_text_view_for_image_desc);
            copyright = itemView.findViewById(R.id.copyright_for_image);
            ImageView more = itemView.findViewById(R.id.card_more_vert);
            menu = itemView.findViewById(R.id.menu_for_card);

            bingImage.setOnClickListener(CardVieHolder.this);
            favorites.setOnClickListener(CardVieHolder.this);
            more.setOnClickListener(CardVieHolder.this);

            itemView.findViewById(R.id.save_to_external).setOnClickListener(CardVieHolder.this);
            itemView.findViewById(R.id.set_wallpaper).setOnClickListener(CardVieHolder.this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            DataStore data = mainFragment.dataList.get(position);

            switch (view.getId()) {
                case R.id.card_image_view_for_favorites:
                    if (data.getBool()) {
                        favorites.setImageResource(R.drawable.ic_heart_paths);
                        MainActivity.db_conn.updateFavorites("0", data.get_id());
                        data.updateBool(false);
                    } else {
                        favorites.setImageResource(R.drawable.ic_heart_red);
                        MainActivity.db_conn.updateFavorites("1", data.get_id());
                        data.updateBool(true);
                    }
                    break;
                case R.id.card_more_vert:
                    if (isMenuUp){
                        shrinkMenu();
                        isMenuUp = false;
                    }else {
                        expandMenu();
                        isMenuUp = true;
                    }
                    break;

                case R.id.save_to_external:
                    Log.d(TAG, "onClick: save to external");
                    // TODO: 5/19/20 save to external location
                    break;

                case R.id.set_wallpaper:
                    Log.d(TAG, "onClick: set as wallpaper");
                    // Todo: set wallpaer
                    break;

                case R.id.image_view_for_main_fragment:
                    String date = pictureDate.getText().toString();
                    String imageDesc = imageDescription.getText().toString();
                    String copyright_msg = copyright.getText().toString();
                    Bitmap bitmap = ((BitmapDrawable) bingImage.getDrawable()).getBitmap();

                    if (MainActivity.startFragment != null) {
                        MainActivity.startFragment.startImageFragment(bitmap, copyright_msg, imageDesc, date);
                    }
                    break;
            }

        }

        private void expandMenu(){
            if (anim == null){
                initAnimation();
            }
            anim.start();
        }

        private void shrinkMenu(){
            if (anim == null){
                initAnimation();
            }
            anim.reverse();
        }

        private void initAnimation() {
            menu.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            mHeight = menu.getMeasuredHeight();

            anim = ValueAnimator.ofInt(0, mHeight);
            anim.setDuration(300).setInterpolator(new AccelerateInterpolator());
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {

                    ViewGroup.LayoutParams params = menu.getLayoutParams();
                    params.height = (int) (mHeight * valueAnimator.getAnimatedFraction());
                    menu.setLayoutParams(params);
                }
            });
        }
    }


    public synchronized void insertItem(int position) {
        if (mainFragment.progressBar != null) {
            mainFragment.progressBar.setVisibility(View.INVISIBLE);
        }
        notifyItemInserted(position);
    }
}
