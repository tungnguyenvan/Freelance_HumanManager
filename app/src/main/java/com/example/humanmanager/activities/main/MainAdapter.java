package com.example.humanmanager.activities.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.humanmanager.R;
import com.example.humanmanager.managers.ImageManager;
import com.example.humanmanager.models.HumanModel;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private static final String TAG = MainAdapter.class.getSimpleName();

    private Context mContext;
    private List<HumanModel> mHumanModels;
    private MainActivityIMP mMainActivityIMP;

    public MainAdapter(Context mContext, MainActivityIMP mMainActivityIMP, List<HumanModel> mHumanModels) {
        this.mContext = mContext;
        this.mHumanModels = mHumanModels;
        this.mMainActivityIMP = mMainActivityIMP;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_human, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.setId(mHumanModels.get(i).getBuilder().getId());
        viewHolder.setName(mHumanModels.get(i).getBuilder().getName());
        viewHolder.setBirthDay(mHumanModels.get(i).getBuilder().getBirthDay());
        viewHolder.setImage(mHumanModels.get(i).getBuilder().getImage());
    }

    @Override
    public int getItemCount() {
        return mHumanModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // ui
        private CircleImageView mImageView;
        private TextView mName, mBirthDay;
        private ImageView mEdit, mDelete;

        // data
        private int mId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            addControls();
            addEvents();
        }

        private void addControls() {
            mImageView = itemView.findViewById(R.id.item_img);
            mName = itemView.findViewById(R.id.item_txt_name);
            mBirthDay = itemView.findViewById(R.id.item_txt_birth_day);
            mEdit = itemView.findViewById(R.id.item_edit);
            mDelete = itemView.findViewById(R.id.item_delete);
        }

        private void addEvents() {
            mEdit.setOnClickListener(v -> {
                mMainActivityIMP.edit(mId, getAdapterPosition());
            });

            mDelete.setOnClickListener(v -> {
                mMainActivityIMP.delete(mId, getAdapterPosition());
            });
        }

        public void setImage(String imageBase64) {
            if (imageBase64 != null)
                mImageView.setImageBitmap(ImageManager.getInstance().toImage(imageBase64));
        }

        public void setName(String name) {
            this.mName.setText(name);
        }

        public void setBirthDay(String birthDay) {
            this.mBirthDay.setText(birthDay);
        }

        public void setId(int id) {
            this.mId = id;
        }
    }
}
