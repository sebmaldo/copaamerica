package com.unab.copaamerica;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.unab.copaamerica.adapter.MatchAdapter;
import com.unab.copaamerica.constants.Cons;
import com.unab.copaamerica.model.Country;
import com.unab.copaamerica.model.Match;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Match> Matches;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.match_list);
        createMatchView();
    }

    public void loadMatchs() {
        SharedPreferences prefs =getSharedPreferences(Cons.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String partidosJson = prefs.getString(Cons.SP_MATCHS, "");
        String paisesJson = prefs.getString(Cons.SP_COUNTRIES, "");

        //noinspection unchecked
        ArrayList <LinkedTreeMap<String, Object>> listaPartidos = gson.fromJson(partidosJson, ArrayList.class);
        //noinspection unchecked
        ArrayList <LinkedTreeMap<String, Object>> listaPaises = gson.fromJson(paisesJson, ArrayList.class);

        Matches = new ArrayList<>();
        for(LinkedTreeMap<String, Object> currentMatch: listaPartidos) {
            @SuppressWarnings("ConstantConditions")
            int localKey = ((Double)currentMatch.get(Cons.KEY_LOCAL)).intValue();
            @SuppressWarnings("ConstantConditions")
            int visitaKey = ((Double)currentMatch.get(Cons.KEY_VISIT)).intValue();
            LinkedTreeMap<String, Object> localData = listaPaises.get(localKey);
            LinkedTreeMap<String, Object> visitaData = listaPaises.get(visitaKey);

            Country local = new Country(
                    (String)localData.get(Cons.KEY_NAME),
                    (String)localData.get(Cons.KEY_CODE),
                    (String)localData.get(Cons.KEY_FLAG),
                    localData.get(Cons.KEY_API_ID)+"",
                    Integer.toString(localKey),
                    (String)localData.get(Cons.KEY_WIN_PERCENTAGE));
            Country visita = new Country(
                    (String)visitaData.get(Cons.KEY_NAME),
                    (String)visitaData.get(Cons.KEY_CODE),
                    (String)visitaData.get(Cons.KEY_FLAG),
                    visitaData.get(Cons.KEY_API_ID)+"",
                    Integer.toString(visitaKey),
                    (String)visitaData.get(Cons.KEY_WIN_PERCENTAGE));
            Match MatchToAdd = new Match(
                    local,
                    visita,
                    (String)currentMatch.get(Cons.KEY_HOUR),
                    (String)currentMatch.get(Cons.KEY_DATE));
            Matches.add(MatchToAdd);
        }
    }

    public void createMatchView(){
        loadMatchs();

        MatchAdapter adapter = new MatchAdapter(context, Matches.toArray(new Match[0/*Matches.size()*/]));

        ListView matchList = findViewById(R.id.match_list);

        @SuppressLint("InflateParams")
        View header = getLayoutInflater().inflate(R.layout.match_list_header, null);
        matchList.addHeaderView(header);
        matchList.setAdapter(adapter);

        matchList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MainActivity.this, HistoricActivity.class);

                Bundle b = new Bundle();

                b.putString(Cons.B_LOCAL_API, Matches.get(position-1).getLocal().getCodigoApi());
                b.putString(Cons.B_VISIT_API, Matches.get(position-1).getVisita().getCodigoApi());

                intent.putExtras(b);

                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.actividad_grupal){
            Intent intent = new Intent(MainActivity.this, ActividadGrupalActivity.class);
            startActivity(intent);
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
    }
}
