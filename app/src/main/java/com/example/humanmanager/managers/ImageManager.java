package com.example.humanmanager.managers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageManager {

    public ImageManager() {
        // Constructor
    }

    public String toBase64(Uri uri) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(uri));
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
