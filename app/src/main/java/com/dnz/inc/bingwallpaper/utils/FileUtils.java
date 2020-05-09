package com.dnz.inc.bingwallpaper.utils;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    public static void saveDataToFile(Bitmap image, File dir, String fileName){
        File file = new File(dir, fileName);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            image.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            boolean bool = false;
            try {
                bool = file.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (bool){
                saveDataToFile(image, dir, fileName);
            }else {
                throw new IllegalStateException("Failed to create image file");
            }
        }
    }
}
