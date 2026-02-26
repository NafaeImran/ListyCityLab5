package com.example.listycitylab5;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CityArrayAdapter extends ArrayAdapter<City> {
    private int selected_city=-1;
    public void setSelectedCity(int position) {
        selected_city = position;
        notifyDataSetChanged();
    }

    public void resetSelectedCity() {
        selected_city = -1;
        notifyDataSetChanged();
    }
    public CityArrayAdapter(Context context, ArrayList<City> cities) {
        super(context, 0, cities);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup
            parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.content,
                    parent, false);
        } else {
            view = convertView;
        }
        City city = getItem(position);
        TextView cityName = view.findViewById(R.id.city_text);
        TextView provinceName = view.findViewById(R.id.province_text);
        cityName.setText(city.getName());
        provinceName.setText(city.getProvince());
        if (position==selected_city)
        {
            view.setBackgroundColor(Color.RED);
        }
        else
        {
            view.setBackgroundColor(Color.TRANSPARENT);
        }
        return view;
    }
}