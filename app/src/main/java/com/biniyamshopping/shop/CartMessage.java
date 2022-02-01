package com.biniyamshopping.shop;

import com.google.firebase.database.Exclude;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class CartMessage {
    String cat, cit, desc, cond, type, phone;
    String imageName;
    String imageURL;
    String price;
    String mkey;
    @Id
    private long _id;
    public CartMessage() {

    }

    public CartMessage(String price, String cat, String cit, String desc, String imageName, String imageURL, String cond, String type, String phone) {
        this.cat = cat;
        this.cit = cit;
        this.desc = desc;
        this.cond = cond;
        this.type = type;
        this.phone = phone;
        this.imageName = imageName;
        this.imageURL = imageURL;
        this.price = price;
    }



    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getCit() {
        return cit;
    }

    public void setCit(String cit) {
        this.cit = cit;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCond() {
        return cond;
    }

    public void setCond(String cond) {
        this.cond = cond;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Exclude
    public String getKey() {
        return mkey;
    }

    @Exclude
    public void setKey(String key) {
        mkey = key;
    }

}
