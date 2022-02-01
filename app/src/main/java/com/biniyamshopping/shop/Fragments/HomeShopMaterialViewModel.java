package com.biniyamshopping.shop.Fragments;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.biniyamshopping.shop.CartMessage;
import com.biniyamshopping.shop.DbService.CartDbService.CartDbService;
import com.biniyamshopping.shop.DbService.HomeShopDbService.HomeDbService;
import com.biniyamshopping.shop.Messages;
import com.biniyamshopping.shop.offline.ObjectBox;

import java.util.List;

public class HomeShopMaterialViewModel extends AndroidViewModel {
    private final Context mcontext;
    private final HomeDbService messageDbService;
    private MutableLiveData<List<Messages>> listlivedata;

    public HomeShopMaterialViewModel(Application application) {
        super(application);
        mcontext = application.getApplicationContext();
        messageDbService = HomeDbService.getInstance(mcontext, ObjectBox.get());
    }

    public void init() {
        listlivedata = new MutableLiveData<>();

    }

    public void getdata() {
        List<Messages> messagelist = messageDbService.getAll();
        System.out.println("daa");
        System.out.println(messagelist.size());
        listlivedata.postValue(messagelist);
    }

    public MutableLiveData<List<Messages>> getlivedata() {
        return listlivedata;
    }


    public Messages filterdata(String id) {
        Messages messagelist = messageDbService.filtedData(id);
        return messagelist;

    }
    public List<Messages> filterbycatagory(String catagoryname){
        return  HomeDbService.getInstance(mcontext, ObjectBox.get()).filterbycatagory(catagoryname);
    }
    public List<Messages> filterbylocation(String locatiton){
        return  HomeDbService.getInstance(mcontext, ObjectBox.get()).filterbylocation(locatiton);
    }
    public List<Messages> filterbyname(String name){
        return  HomeDbService.getInstance(mcontext, ObjectBox.get()).filterbyname(name);
    }
}
