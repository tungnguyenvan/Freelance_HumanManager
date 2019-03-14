package com.example.humanmanager.activities.edit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.widget.Button;
import android.widget.Toast;

import com.example.humanmanager.R;
import com.example.humanmanager.activities.main.MainActivity;
import com.example.humanmanager.managers.HumanManager;
import com.example.humanmanager.managers.ImageManager;
import com.example.humanmanager.models.HumanModel;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditActivity extends AppCompatActivity {
    private static final String TAG = EditActivity.class.getSimpleName();
    private static final int REQUEST_GET_IMAGE = 16;

    //ui
    private Toolbar mToolbar;
    private CircleImageView mImageView;
    private TextInputLayout mName, mBirthDay;
    private Button mSubmit;

    // data
    private int id;
    private HumanModel mHumanModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        getData();
        addControls();
    }

    private void getData() {
        id = getIntent().getIntExtra("id", id);
        mHumanModel = HumanManager.getInstance(this).get(id);
    }

    private void addControls() {
        // toolbar
        mToolbar = findViewById(R.id.edit_toolbar);
        mToolbar.setTitle("Edit");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Image view
        mImageView = findViewById(R.id.edit_img);
        mImageView.setImageBitmap(ImageManager.getInstance().toImage(mHumanModel.getBuilder().getImage()));
        mImageView.setOnClickListener(v -> checkGetImage());

        // name
        mName = findViewById(R.id.edit_ed_name);
        mName.getEditText().setText(mHumanModel.getBuilder().getName());

        // birth day
        mBirthDay = findViewById(R.id.edit_ed_birth_day);
        mBirthDay.getEditText().setText(mHumanModel.getBuilder().getBirthDay());

        // button submit
        mSubmit = findViewById(R.id.edit_submit);
        mSubmit.setOnClickListener(v -> save());

    }

    private void save() {
        mHumanModel.getBuilder().setName(mName.getEditText().getText().toString())
                .setBirthDay(mBirthDay.getEditText().getText().toString());

        if (HumanManager.getInstance(this).edit(mHumanModel) > 0) {
            Intent intent = new Intent();
            intent.putExtra("id", id);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, "Save fail, please try again", Toast.LENGTH_SHORT).show();
        }
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
