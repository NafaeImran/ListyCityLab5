package com.example.listycitylab5;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment {
    interface AddCityDialogListener {
        void addCity(com.example.listycitylab5.City city);
        void editCity(com.example.listycitylab5.City city,String old_name);
    }
    private AddCityDialogListener listener;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }
    public static AddCityFragment newInstance() {
        return new AddCityFragment();
    }
    public static AddCityFragment newInstance(com.example.listycitylab5.City city) {
        AddCityFragment fragment = new AddCityFragment();
        Bundle args = new Bundle();
        args.putSerializable("city", city);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view =LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);
        com.example.listycitylab5.City selected_city=null;
        boolean edit=false;
        Bundle arguments=getArguments();
        if(arguments!=null)
        {
            selected_city=(com.example.listycitylab5.City) arguments.getSerializable("city");

            edit=true;
            editCityName.setText(selected_city.getName());
            editProvinceName.setText(selected_city.getProvince());
        }
        String title;
        String pos_button;

        if(edit)
        {
            pos_button="Save";
            title="Edit City";
        }
        else
        {
            pos_button="Add";
            title="Add a city";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final boolean edit_fin=edit;
        final com.example.listycitylab5.City selected_city_fin=selected_city;

        return builder


                .setView(view)
                .setTitle(title)
                .setNegativeButton("Cancel", null)
                .setPositiveButton(pos_button, (dialog, which) -> {
                    String cityName = editCityName.getText().toString();
                    String provinceName = editProvinceName.getText().toString();
                    if(edit_fin)
                    {
                        String old_name=selected_city_fin.getName();
                        selected_city_fin.setName(cityName);
                        selected_city_fin.setProvince(provinceName);
                        listener.editCity(selected_city_fin,old_name);

                    }
                    else
                    {
                        listener.addCity(new com.example.listycitylab5.City(cityName, provinceName));

                    }
                })
                .create();
    }
}
