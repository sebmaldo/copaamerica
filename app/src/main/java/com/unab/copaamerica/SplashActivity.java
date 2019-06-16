package com.unab.copaamerica;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.unab.copaamerica.constants.Cons;

import java.util.ArrayList;
import java.util.HashMap;

public class SplashActivity extends AppCompatActivity {
    ArrayList<HashMap<String, Object>> listaPartidos = new ArrayList<>();
    ArrayList<HashMap<String, Object>> listaPaises = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loadCountryList();
    }

    public void loadCountryList(){
        SharedPreferences prefs =
                getSharedPreferences(Cons.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String paisesJson = prefs.getString(Cons.SP_COUNTRIES, "");

        if(!paisesJson.equals("")) {
            loadMatchList();
            return;
        }

        DatabaseReference paisesDB = FirebaseDatabase.getInstance().getReference().child(Cons.FB_COUNTRIES);

        paisesDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot paises) {
                for (DataSnapshot pais: paises.getChildren()) {
                    listaPaises.add((HashMap)pais.getValue());
                }
                loadMatchList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    public void loadMatchList(){
        DatabaseReference partidosDB = FirebaseDatabase.getInstance().getReference().child(Cons.FB_MATCHS);

        partidosDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot partidos) {
                for (DataSnapshot partido: partidos.getChildren()) {
                    listaPartidos.add((HashMap)partido.getValue());
                }
                goToFirstPage();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    public void goToFirstPage() {
        SharedPreferences prefs =
                getSharedPreferences(Cons.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        Gson gson = new Gson();

        SharedPreferences.Editor editor = prefs.edit();
        String paisesJson = prefs.getString(Cons.SP_COUNTRIES, "");
        if(paisesJson.equals("")) {
            editor.putString(Cons.SP_COUNTRIES, gson.toJson(listaPaises));
        }
        editor.putString(Cons.SP_MATCHS, gson.toJson(listaPartidos));
        editor.commit();

        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity ( intent );
        this.finish();
    }
}
