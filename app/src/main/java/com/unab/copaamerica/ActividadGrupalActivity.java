package com.unab.copaamerica;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.unab.copaamerica.adapter.CountySelectorAdapter;
import com.unab.copaamerica.adapter.PositionAdapter;
import com.unab.copaamerica.constants.Cons;
import com.unab.copaamerica.helpers.FirebaseHelper;
import com.unab.copaamerica.model.Country;

import java.util.ArrayList;

public class ActividadGrupalActivity extends AppCompatActivity {
    ArrayList<Country> Positions;
    ArrayList<Country> Paises;
    Context context;
    boolean dentro = false;
    int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        createPositionView();
    }

    private void createPositionView() {
        dentro = false;
        position = -1;
        setContentView(R.layout.country_positions_selected_list);
        Positions = FirebaseHelper.getFirstPlaces();
        PositionAdapter adapter = new PositionAdapter(context, Positions.toArray(new Country[Positions.size()]));
        ListView positionList = findViewById(R.id.cpsl_list);
        View header = getLayoutInflater().inflate(R.layout.country_position_selected_list_header, null);
        positionList.addHeaderView(header);
        positionList.setAdapter(adapter);

        positionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                createCountrySelectorView(i);
            }
        });
    }

    private void createCountrySelectorView(int i){
        dentro = true;
        position = i;
        setContentView(R.layout.country_list);
        SharedPreferences prefs =
                getSharedPreferences(Cons.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String paisesJson = prefs.getString(Cons.SP_COUNTRIES, "");
        ArrayList<LinkedTreeMap<String, Object>> listaPaises = gson.fromJson(paisesJson, ArrayList.class);
        System.out.println("Lista de paises a colocar " + listaPaises.size());
        Paises = FirebaseHelper.getCountries(listaPaises);
        System.out.println("Lista de paises a colocar " + Paises.size());
        CountySelectorAdapter adapter = new CountySelectorAdapter(context, Paises.toArray(new Country[Paises.size()]));
        final ListView countryList = findViewById(R.id.ci_list);
        View header = getLayoutInflater().inflate(R.layout.country_list_header, null);
        countryList.addHeaderView(header);
        countryList.setAdapter(adapter);

        countryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FirebaseHelper.saveToFirebase(position - 1, Paises.get(i - 1));
                createPositionView();

            }
        });
    }

    @Override
    public void onBackPressed(){
        if(dentro){
            createPositionView();
        }
        else {
            finish();
            super.onBackPressed();
        }
    }
}
