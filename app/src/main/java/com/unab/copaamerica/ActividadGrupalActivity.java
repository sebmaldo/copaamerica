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

    private ArrayList<Country> getCountries(ArrayList<LinkedTreeMap<String, Object>> listaPaises){
        ArrayList<Country> paises = new ArrayList<>();
        for(LinkedTreeMap<String, Object> currentCountry: listaPaises) {
            paises.add(new Country(
                    (String) currentCountry.get(Cons.KEY_NAME),
                    (String) currentCountry.get(Cons.KEY_CODE),
                    (String) currentCountry.get(Cons.KEY_FLAG),
                    currentCountry.get(Cons.KEY_API_ID)+"",
                    "",
                    (String) currentCountry.get(Cons.KEY_WIN_PERCENTAGE)
            ));
        }

        return paises;
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
        Paises = getCountries(listaPaises);
        final Country[] countiesToSelect = getPosibleCountries();
        CountySelectorAdapter adapter = new CountySelectorAdapter(context, countiesToSelect);
        final ListView countryList = findViewById(R.id.ci_list);
        View header = getLayoutInflater().inflate(R.layout.country_list_header, null);
        countryList.addHeaderView(header);
        countryList.setAdapter(adapter);

        countryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FirebaseHelper.saveToFirebase(position - 1, countiesToSelect[i - 1]);
                createPositionView();

            }
        });
    }

    private Country[] getPosibleCountries(){
        ArrayList<Country> filtered = new ArrayList<>();
        for (Country item: Paises){
            boolean putItem = true;
            for(int i = 0; i < 3; i ++){
                Country toReview = Positions.get(i);
                if(toReview!=null && toReview.getCodigo().equalsIgnoreCase(item.getCodigo()) && i != position - 1){
                    putItem = false;
                }
            }
            if(putItem){
                filtered.add(item);
            }
        }
        return filtered.toArray(new Country[filtered.size()]);
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
