package com.example.humanmanager.activities.add;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.humanmanager.R;
import com.example.humanmanager.activities.main.MainActivity;
import com.example.humanmanager.managers.HumanManager;
import com.example.humanmanager.managers.ImageManager;
import com.example.humanmanager.models.HumanModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddActivity extends AppCompatActivity {
    private static final String TAG = AddActivity.class.getSimpleName();
    private static final int REQUEST_GET_IMAGE = 16;

    // ui
    private Toolbar mToolbar;
    private CircleImageView mImageView;
    private TextInputLayout mName, mBirthDay;
    private Button mSubmit;

    private HumanModel mHumanModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mHumanModel = new HumanModel.Builder().build();

        addControls();
    }

    private void addControls() {
        // toolbar
        mToolbar = findViewById(R.id.add_toolbar);
        mToolbar.setTitle("Add new person");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());

        // circle image
        mImageView = findViewById(R.id.add_img);
        mImageView.setOnClickListener(v -> checkGetImage());

        // input text layout name
        mName = findViewById(R.id.add_ed_name);

        // input text layout birth day
        mBirthDay = findViewById(R.id.add_ed_birth_day);

        // button submit
        mSubmit = findViewById(R.id.add_btn_submit);
        mSubmit.setOnClickListener(v -> save());
    }

    private void save() {
        mHumanModel.getBuilder().setName(mName.getEditText().getText().toString())
                .setBirthDay(mBirthDay.getEditText().getText().toString());

        HumanManager.getInstance(this).insert(mHumanModel, new HumanManager.HumanManagerListener() {
            @Override
            public void onFinished(long id) {
                Intent intent = new Intent();
                intent.putExtra("id", id);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }

            @Override
            public void onFail() {
                Toast.makeText(AddActivity.this, "Save data fail, please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getImage() {
        Intent iImage = new Intent();
        iImage.setType("image/*");
        iImage.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(iImage, REQUEST_GET_IMAGE);
    }

    private void checkGetImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    MainActivity.CONTEXT_INCLUDE_CODE);
        } else {
            getImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MainActivity.REQUEST_CODE_PERMISSION_READ_STORAGE && grantResults.length > 0) {
            getImage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_GET_IMAGE && data != null && resultCode == RESULT_OK) {
            Uri path = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                mImageView.setImageBitmap(bitmap);

                mHumanModel.getBuilder().setImage(ImageManager.getInstance().toBase64(bitmap));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
