package com.biniyamshopping.shop.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.biniyamshopping.shop.Fragments.CartFragment;
import com.biniyamshopping.shop.Fragments.HomsShopMaterialDisplay;

public class HomeTabAdapter extends FragmentStateAdapter {
    public HomeTabAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return HomsShopMaterialDisplay.newInstance();

            default:
                return CartFragment.newInstance();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }}
