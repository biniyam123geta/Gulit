package com.gulitshopping.shop.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.gulitshopping.shop.R;


public class LocationDialoge extends DialogFragment {
    String str = "";
    CatagoriesDialog catagoriesDialog;
    public static LocationDialoge newInstance() {
        return new LocationDialoge();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.where, null);
        RadioButton adis, adama, bahir, dire, hawasa, gondar, mekele;
        Button select;
        String city = "";

        adis = (RadioButton) view.findViewById(R.id.addis);
        select = (Button) view.findViewById(R.id.select);
        adama = (RadioButton) view.findViewById(R.id.adama);
        bahir = (RadioButton) view.findViewById(R.id.bahir);
        dire = (RadioButton) view.findViewById(R.id.dire);
        hawasa = (RadioButton) view.findViewById(R.id.hawasa);
        gondar = (RadioButton) view.findViewById(R.id.gondar);
        mekele = (RadioButton) view.findViewById(R.id.mekele);
        adis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adis.isChecked()) {
                    str = "Addis Ababa";
                }
            }
        });
        adama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adama.isChecked()) {
                    str = "Adama";
                }
            }
        });
        bahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bahir.isChecked()) {
                    str = "Bahir Dar";
                }
            }
        });
        dire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dire.isChecked()) {
                    str = "Dire Dawa";
                }
            }
        });
        hawasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hawasa.isChecked()) {
                    str = "Awassa";
                }
            }
        });
        gondar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gondar.isChecked()) {
                    str = "Gondar";
                }
            }
        });
        mekele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mekele.isChecked()) {
                    str = "Mekele";
                }
            }
        });
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                Intent intent = new Intent(getContext(), Catagories.class);
//                intent.putExtra("city", str);
//                startActivity(intent);
                    if (catagoriesDialog == null) {
                        catagoriesDialog = CatagoriesDialog.newInstance(str);
                    }
                catagoriesDialog.show(getChildFragmentManager(), "ddd");


            }
        });

        return new AlertDialog.Builder(requireActivity())
                .setView(view)
                .setCancelable(false)
                .create();
    }

}

