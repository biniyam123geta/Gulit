package com.biniyamshopping.shop.DbService.HomeShopDbService;

import android.content.Context;

import com.biniyamshopping.shop.CartMessage;
import com.biniyamshopping.shop.DbService.CartDbService.CartDbService;
import com.biniyamshopping.shop.Messages;
import com.biniyamshopping.shop.Messages_;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.QueryBuilder;

public class HomeDbService {
    private static  HomeDbService  homedbservice;
    private final Context mContext;
    private final Box<Messages> messagesBox;


    private HomeDbService(Context context, BoxStore objectbox){
        this.mContext = context.getApplicationContext();
        this.messagesBox = objectbox.boxFor(Messages.class);
    }


    public static  HomeDbService getInstance(Context context,BoxStore objectbox){
        if (homedbservice == null) {
            homedbservice = new HomeDbService(context, objectbox);
        }
        return homedbservice;
    }


    public Messages filtedData(String id) {
        QueryBuilder<Messages> builder = messagesBox.query().equal(Messages_.mkey,
                id);
        Messages filtereddata = builder.build().findFirst();
        return filtereddata;
    }
    public List<Messages> getAll(){
        List<Messages> messages =messagesBox.getAll();
        return messages;
    }

    public boolean storeAll(List<Messages> payload) {
        removeAll();
        messagesBox.put(payload);
        return true;
    }

    public boolean removeAll() {
        messagesBox.removeAll();
        return true;
    }
    public List<Messages> filterbycatagory(String catagory){
        return messagesBox.query().equal(Messages_.cat, catagory).build().find();
    }
    public List<Messages> filterbylocation(String city){
        return messagesBox.query().equal(Messages_.cit, city).build().find();
    }
    public List<Messages> filterbyname(String name){
        return messagesBox.query().startsWith(Messages_.imageName, name).build().find();
    }
}
