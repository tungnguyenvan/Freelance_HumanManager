package com.example.humanmanager.managers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ImageManager {
    private static final String TAG = ImageManager.class.getSimpleName();
    private static ImageManager instance;

    private ImageManager() {
        // Constructor
    }

    public static ImageManager getInstance() {
        if (instance == null) {
            instance = new ImageManager();
        }

        return instance;
    }

    public String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();

        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public Bitmap toImage(String imageString) {
        byte[] imageByte = Base64.decode(imageString, Base64.DEFAULT);
        Bitmap decodeImage = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);

        return decodeImage;
    }
}
