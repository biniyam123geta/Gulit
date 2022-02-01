package com.biniyamshopping.shop.DbService;

import android.content.Context;

import com.biniyamshopping.shop.DbService.HomeShopDbService.HomeDbService;
import com.biniyamshopping.shop.Messages;
import com.biniyamshopping.shop.Messages_;
import com.biniyamshopping.shop.SearchMessage;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;

public class SearchDbService {
    private static SearchDbService searchDbService;
    private final Context mContext;
    private final Box<SearchMessage> messagesBox;

    private SearchDbService(Context context, BoxStore objectbox){
        this.mContext = context.getApplicationContext();
        this.messagesBox = objectbox.boxFor(SearchMessage.class);
    }
    public static  SearchDbService getInstance(Context context,BoxStore objectbox){
        if (searchDbService == null) {
            searchDbService = new SearchDbService(context, objectbox);
        }
        return searchDbService;
    }

//    public List<Messages> filterbycatagory(String catagory){
//        return messagesBox.query().equal(Se, catagory).build().find();
//    }
}
