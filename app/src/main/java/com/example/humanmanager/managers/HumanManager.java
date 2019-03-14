package com.example.humanmanager.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.humanmanager.models.HumanModel;

import java.util.ArrayList;
import java.util.List;

public class HumanManager extends SQLiteOpenHelper {
    private static final String TAG = HumanManager.class.getSimpleName();

    private static final String DATABASE_NAME = "human_manager";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "human";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String BIRTH_DAY = "birth_day";
    private static final String IMAGE = "image";

    public HumanManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE " + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                 + NAME + " TEXT, " + BIRTH_DAY + " TEXT, " + IMAGE + " TEXT)";
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(HumanModel model) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME, model.getBuilder().getName());
        values.put(BIRTH_DAY, model.getBuilder().getBirthDay());
        values.put(IMAGE, model.getBuilder().getImage());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public HumanModel get(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] {ID, NAME, BIRTH_DAY, IMAGE},
                ID + " =?", new String[] {String.valueOf(id)}, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        return new HumanModel.Builder().setId(cursor.getInt(0))
                .setName(cursor.getString(1))
                .setBirthDay(cursor.getString(2))
                .setImage(cursor.getString(3)).build();
    }

    public List<HumanModel> gets() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<HumanModel> humanModels = new ArrayList<>();

        String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + ID + " DESC";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                HumanModel model = new HumanModel.Builder()
                        .setId(cursor.getInt(0))
                        .setName(cursor.getString(1))
                        .setBirthDay(cursor.getString(2))
                        .setImage(cursor.getString(3))
                        .build();

                humanModels.add(model);
            } while (cursor.moveToNext());
        }

        return humanModels;
    }
}
