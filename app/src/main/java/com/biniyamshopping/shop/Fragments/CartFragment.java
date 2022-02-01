package com.biniyamshopping.shop.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biniyamshopping.shop.CartAdapter;
import com.biniyamshopping.shop.CartMessage;
import com.biniyamshopping.shop.DbService.CartDbService.CartDbService;
import com.biniyamshopping.shop.Home;
import com.biniyamshopping.shop.ImageAdapter;
import com.biniyamshopping.shop.Messages;
import com.biniyamshopping.shop.R;
import com.biniyamshopping.shop.offline.ObjectBox;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


public class CartFragment extends Fragment {
    List<CartMessage> cartdata;
    private RecyclerView cartRecyclerView;
    private List<CartMessage> cartUploads;
    private CartViewModel cartviewmodel;
    private CartAdapter cartAdapter;

    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_cart, container, false);

        cartRecyclerView = view.findViewById(R.id.cartlist);

        cartviewmodel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);
        cartviewmodel.init();

        cartviewmodel.getCartLiveData().observe(requireActivity(), response -> {
            cartdata = response;
            List<CartMessage> revcartMessages=reverseList(response);
            populate(revcartMessages);

        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        cartviewmodel.getcartdata();
        if (cartdata != null) {
            List<CartMessage> revcartMessages=reverseList(cartdata);
            populate(revcartMessages);
        }
    }
    public static<CartMessage> List<CartMessage> reverseList(List<CartMessage> list)
    {
        List<CartMessage> reverse = new ArrayList<>(list.size());

        ListIterator<CartMessage> itr = list.listIterator(list.size());
        while (itr.hasPrevious()) {
            reverse.add(itr.previous());
        }

        return reverse;
    }
    public void populate(List<CartMessage> list) {
        System.out.println("cart size");
        System.out.println(list.size());
        cartAdapter = new CartAdapter(requireContext(), list);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

    }
}