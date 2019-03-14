package com.example.humanmanager.activities.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.example.humanmanager.R;
import com.example.humanmanager.activities.add.AddActivity;
import com.example.humanmanager.activities.edit.EditActivity;
import com.example.humanmanager.managers.HumanManager;
import com.example.humanmanager.models.HumanModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainActivityIMP {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int REQUEST_CODE_PERMISSION_READ_STORAGE = 11;
    private static final int REQUEST_CODE_ADD = 12;
    private static final int REQUEST_CODE_EDIT = 13;

    // ui
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mBtnAdd;

    // data
    private List<HumanModel> mHumanModels;
    private MainAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHumanModels = new ArrayList<>();

        setPermission();
        getData();
        addControls();
    }

    private void setPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_READ_STORAGE);
        }
    }

    private void getData() {
        mHumanModels.clear();
        HumanManager.getInstance(this).gets(new HumanManager.HumanManagerGets() {
            @Override
            public void onFinished(List<HumanModel> humanModels) {
                mHumanModels = humanModels;

                if (mAdapter != null) {
                    mAdapter = new MainAdapter(MainActivity.this, MainActivity.this, mHumanModels);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onFail() {
                Toast.makeText(MainActivity.this, "null data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addControls() {
        // toolbar
        mToolbar = findViewById(R.id.main_toolbar);
        mToolbar.setTitle("Home");
        setSupportActionBar(mToolbar);

        mBtnAdd = findViewById(R.id.main_add);
        mBtnAdd.setOnClickListener(v -> {
            nextToAdd();
        });

        // recycler view
        mRecyclerView = findViewById(R.id.main_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new MainAdapter(this, this, mHumanModels);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void nextToAdd() {
        Intent iAdd = new Intent(this, AddActivity.class);
        startActivityForResult(iAdd, REQUEST_CODE_ADD);
    }

    private void nextToEdit(int id) {
        Intent iEdit = new Intent(this, EditActivity.class);
        iEdit.putExtra("id", id);
        startActivityForResult(iEdit, REQUEST_CODE_EDIT);
    }

    @Override
    public void edit(int id, int position) {
        nextToEdit(id);
    }

    @Override
    public void delete(int id, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete");
        builder.setMessage("Do you want delete " + mHumanModels.get(position).getBuilder().getName());
        builder.setPositiveButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.setNegativeButton("Ok", (dialog, which) -> {
            if (HumanManager.getInstance(this).delete(id) > 0) {
                Toast.makeText(this, "Delete success", Toast.LENGTH_SHORT).show();
                getData();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD && data != null && resultCode == RESULT_OK) {
            long id = data.getLongExtra("id", 0);
            if (id > 0) {
                getData();
            }
        }

        if (requestCode == REQUEST_CODE_EDIT && data != null && resultCode == RESULT_OK) {
            getData();
        }
    }
}
