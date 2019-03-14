package com.example.humanmanager.models;

import java.io.Serializable;

public class HumanModel implements Serializable {
    private Builder mBuilder;

    private HumanModel(Builder builder) {
        this.mBuilder = builder;
    }

    public Builder getBuilder() {
        return mBuilder;
    }

    public static class Builder {
        private int mId;
        private String mName;
        private String mBirthDay;
        private String mImage;

        public Builder setId(int id) {
            this.mId = id;
            return this;
        }

        public Builder setName(String name) {
            this.mName = name;
            return this;
        }

        public Builder setBirthDay(String birthDay) {
            this.mBirthDay = birthDay;
            return this;
        }

        public Builder setImage(String image) {
            this.mImage = image;
            return this;
        }

        public int getId() {
            return mId;
        }

        public String getName() {
            return mName;
        }

        public String getBirthDay() {
            return mBirthDay;
        }

        public String getImage() {
            return mImage;
        }

        public HumanModel build() {
            return new HumanModel(this);
        }
    }
}
