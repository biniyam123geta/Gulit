package com.gulitshopping.shop.Fragments;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.gulitshopping.shop.CartMessage;
import com.gulitshopping.shop.DbService.CartDbService.CartDbService;
import com.gulitshopping.shop.offline.ObjectBox;

import java.util.List;

public class CartViewModel  extends AndroidViewModel {
    private final Context mcontext;
    private MutableLiveData<List<CartMessage>> cartlivedata;
    private final CartDbService cartDbService;
    public CartViewModel(Application application) {
        super(application);
        mcontext = application.getApplicationContext();
        cartDbService = CartDbService.getInstance(mcontext, ObjectBox.get());
    }

    public void init() {
        cartlivedata = new MutableLiveData<>();
    }

    public void getcartdata() {
        List<CartMessage> cartMessages = cartDbService.getAll();
        cartlivedata.postValue(cartMessages);
    }
    public CartMessage getfiltereddata(String id) {
        CartMessage cartMessages = cartDbService.filtedCartData(id);
        return cartMessages;
    }

    public MutableLiveData<List<CartMessage>> getCartLiveData() {
        return cartlivedata;
    }

}
