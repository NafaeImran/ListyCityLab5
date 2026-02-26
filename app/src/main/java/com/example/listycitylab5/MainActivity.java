package com.example.listycitylab5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        AddCityFragment.AddCityDialogListener {
    private ArrayList<City> dataList;
    private ListView cityList;
    private CityArrayAdapter cityAdapter;
    private City delete_city;
    private int delete_pos=-1;

    private FirebaseFirestore db;
    private CollectionReference citiesRef;
    private FloatingActionButton delete_button;
    @Override
    public void addCity(City city) {
        cityAdapter.add(city);
        cityAdapter.notifyDataSetChanged();
        DocumentReference docRef=citiesRef.document(city.getName());
        docRef.set(city);
    }

    @Override
    public void editCity(City city,String old_name) {
        String curr_name=city.getName();

        if(curr_name!=old_name)
        {
            citiesRef.document(old_name).delete();
            citiesRef.document(curr_name).set(city);
        }

        cityAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        String[] cities = { "Edmonton", "Vancouver", "Toronto" };
//        String[] provinces = { "AB", "BC", "ON" };
        dataList = new ArrayList<>();
//        for (int i = 0; i < cities.length; i++) {
//            dataList.add(new City(cities[i], provinces[i]));
//        }
        db=FirebaseFirestore.getInstance();
        cityList = findViewById(R.id.city_list);
        cityList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        cityAdapter = new CityArrayAdapter(this, dataList);
        cityList.setAdapter(cityAdapter);
        delete_button = findViewById(R.id.button_delete_city);
        citiesRef=db.collection("cities");
        citiesRef.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e("Firestore", error.toString());
            }

            if (value != null) {
                dataList.clear();

                for (QueryDocumentSnapshot snapshot : value) {
                    String name = snapshot.getString("name");
                    String province = snapshot.getString("province");

                    dataList.add(new City(name, province));
                }
                cityAdapter.notifyDataSetChanged();
            }
        });



        FloatingActionButton fab = findViewById(R.id.button_add_city);
        fab.setOnClickListener(v -> {
            new AddCityFragment().show(getSupportFragmentManager(), "Add City");
        });
        cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                City selected_city=dataList.get(pos);
                AddCityFragment.newInstance(selected_city).show(getSupportFragmentManager(),"Edit City");

            }
        });
        cityList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {

                delete_pos =pos;
                cityAdapter.setSelectedCity(pos);
                return true;
            }
        });
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( delete_pos!=-1)
                {
                    delete_city=dataList.get(delete_pos);
                    DocumentReference docRef=citiesRef.document(delete_city.getName());
                    docRef.delete();
                    delete_city=null;
                    delete_pos=-1;
                    cityAdapter.resetSelectedCity();

                }
            }
        });
    }
}