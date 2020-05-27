package com.dnz.inc.bingwallpaper.fragments;

import android.animation.ValueAnimator;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dnz.inc.bingwallpaper.MainActivity;
import com.dnz.inc.bingwallpaper.R;
import com.dnz.inc.bingwallpaper.utils.DataStore;
import com.dnz.inc.bingwallpaper.utils.FileUtils;
import com.dnz.inc.bingwallpaper.utils.Permissions;
import com.dnz.inc.bingwallpaper.utils.TimeUtils;

import java.io.File;
import java.io.IOException;

import androidx.annotation.NonNull;
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
        holder.pictureDate.setText(TimeUtils.forDisplay(dataStore.getDate()));
        holder.copyright.setText(dataStore.getCopyright());

        if (!dataStore.getBool()) {
            holder.favorites.setImageResource(R.drawable.ic_heart_paths);
        } else {
            holder.favorites.setImageResource(R.drawable.ic_heart_red);
        }

    }

    @Override
    public int getItemCount() {
        return mainFragment.dataList.size();
    }

    public class CardVieHolder extends RecyclerView.ViewHolder implements View.OnClickListener, SaveCallBack {

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
            itemView.findViewById(R.id.delete_card).setOnClickListener(CardVieHolder.this);
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
                    if (isMenuUp) {
                        shrinkMenu();
                    } else {
                        expandMenu();
                    }
                    break;

                case R.id.save_to_external:
                    if (Permissions.checkStoragePermission(mainFragment.getActivity())) {
                        saveFile();
                    } else {
                        Permissions.getStoragePermissions(mainFragment.getActivity());
                        MainActivity.saveCallBack = CardVieHolder.this;
                    }
                    break;

                case R.id.set_wallpaper:
                    setWallpaper();
                    break;
                case R.id.delete_card:
                    shrinkMenu();
                    MainActivity.db_conn.deleteEntry_byID(data.get_id());
                    mainFragment.dataList.remove(position);

                    notifyItemRemoved(position);
                    break;

                case R.id.image_view_for_main_fragment:

                    ImageFragment imageFragment = new ImageFragment(mainFragment.dataList.get(position));
                    mainFragment.getFragmentManager().
                            beginTransaction().
                            setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter, R.anim.exit).
                            replace(R.id.fragment_container, imageFragment).
                            addToBackStack(null).
                            commit();

                    break;
            }

        }

        private void setWallpaper() {
            shrinkMenu();
            new ChangeWallPaper().execute(((BitmapDrawable) bingImage.getDrawable()).getBitmap());

        }

        public void saveFile() {
            shrinkMenu();
            File root = Environment.getExternalStoragePublicDirectory
                    (Environment.DIRECTORY_PICTURES);
            File file = new File(root, "Bing Wallpapers");
            file.mkdir();

            Bitmap bitmap = ((BitmapDrawable) bingImage.getDrawable()).getBitmap();
            FileUtils.saveImageToFile(bitmap, file, imageDescription.getText().toString() + ".jpg");

            MediaScannerConnection.scanFile(mainFragment.getActivity(), new String[]{file.toString()},
                    null, null);

            Toast.makeText(mainFragment.getContext(), "Image successfully changed",
                    Toast.LENGTH_SHORT).show();
        }

        private void expandMenu() {
            if (anim == null) {
                initAnimation();
            }
            anim.start();
            isMenuUp = true;
        }

        private void shrinkMenu() {
            if (anim == null) {
                initAnimation();
            }
            anim.reverse();
            isMenuUp = false;
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

        @Override
        public void save() {
            saveFile();
        }

        private class ChangeWallPaper extends AsyncTask<Bitmap, Void, Boolean> {

            @Override
            protected Boolean doInBackground(Bitmap... voids) {
                WallpaperManager manager = WallpaperManager.getInstance(mainFragment.getContext());

                Bitmap mBitmap = voids[0];

                Display display = mainFragment.getActivity().getWindowManager().getDefaultDisplay();

                int displayHeight = display.getHeight();
                int displayWidth = display.getWidth();


                int imageHeight = mBitmap.getHeight();
                int imageWidth = mBitmap.getWidth();


                int xStart = 0, yStart = 0, xWidth = displayWidth, yHeight = displayHeight;

                if (displayWidth <= imageWidth && displayHeight <= imageHeight) {

                    xStart = ((imageWidth / 2) - (displayWidth / 2));
                    yStart = ((imageHeight / 2) - (displayHeight / 2));

                } else if (displayWidth <= imageWidth && displayHeight >= imageHeight) {
                    int scaledWidth = (int) (imageHeight * (displayWidth / (float) displayHeight));
                    xStart = ((imageWidth / 2) - (scaledWidth / 2));

                    xWidth = scaledWidth;
                    yHeight = imageHeight;
                } else if (displayWidth >= imageWidth && displayHeight <= imageHeight) {
                    int scaledHeight = (int) (imageWidth * (displayHeight / (float) displayWidth));
                    xStart = ((imageWidth / 2) - (scaledHeight / 2));

                    yHeight = scaledHeight;
                    xWidth = imageWidth;
                } else if (displayWidth >= imageWidth && displayHeight >= imageHeight) {
                    if (displayWidth >= displayHeight) {
                        int scaledHeight = (int) (imageWidth * (displayHeight / (float) displayWidth));
                        xStart = ((imageWidth / 2) - (scaledHeight / 2));

                        yHeight = scaledHeight;
                        xWidth = imageWidth;
                    } else {
                        int scaledWidth = (int) (imageHeight * (displayWidth / (float) displayHeight));
                        xStart = ((imageWidth / 2) - (scaledWidth / 2));

                        xWidth = scaledWidth;
                        yHeight = imageHeight;
                    }

                }

                mBitmap = Bitmap.createBitmap(mBitmap, xStart, yStart, xWidth, yHeight);

                try {
                    manager.setBitmap(mBitmap);
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if (aBoolean) {
                    Toast.makeText(mainFragment.getContext(),
                            "Wallpaper successfully changed", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mainFragment.getContext(),
                            "Unable to update wallpaper", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    public synchronized void insertItem(int position) {
        if (mainFragment.progressBar != null) {
            mainFragment.progressBar.setVisibility(View.INVISIBLE);
        }
        notifyItemInserted(position);
    }

    public interface SaveCallBack {
        void save();
    }

}


