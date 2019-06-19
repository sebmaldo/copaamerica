package com.unab.copaamerica;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

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

        if(paisesJson!=null && !paisesJson.equals("")) {
            loadMatchList();
            return;
        }
        if(isNetworkAvailable()){
            DatabaseReference paisesDB = FirebaseDatabase.getInstance().getReference().child(Cons.FB_COUNTRIES);

            paisesDB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot paises) {
                    for (DataSnapshot pais: paises.getChildren()) {
                        listaPaises.add((HashMap)pais.getValue());
                    }
                    loadMatchList();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    System.out.println(databaseError.getMessage());
                    Toast.makeText(getApplicationContext(),"Ha ocurrido un error",Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    public boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //desde la versión 15 de android facia arriba se piden todas las redes
            //y se everifica la info de cada red
            Network[] networks = connectivityManager.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network mNetwork : networks) {
                networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    return true;
                }
            }
        }else {
            //desde la versión 15 de android hacia atrás, se podía traer directamente toda la info de las redes
            //actualemente deprecado.
            if (connectivityManager != null) {
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        }
        Toast.makeText(getApplicationContext(),"No hay conexión a internet, conéctese e intente nuevamente.",Toast.LENGTH_LONG).show();
        return false;
    }

    public void loadMatchList(){
        if(isNetworkAvailable()){
            DatabaseReference partidosDB = FirebaseDatabase.getInstance().getReference().child(Cons.FB_MATCHS);

            partidosDB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot partidos) {
                    for (DataSnapshot partido: partidos.getChildren()) {
                        listaPartidos.add((HashMap)partido.getValue());
                    }
                    goToFirstPage();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    System.out.println(databaseError.getMessage());
                    Toast.makeText(getApplicationContext(),"Ha ocurrido un error",Toast.LENGTH_LONG).show();
                }
            });
        }

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
