package com.unab.copaamerica;

import android.annotation.SuppressLint;
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
    //Se crean listas para almacenar partidos y países por cargar
    ArrayList<HashMap> listaPartidos = new ArrayList<>();
    ArrayList<HashMap> listaPaises = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Esta actividad hereda de  AppCompactActivity, implementa el método on create
        //lo primero que hace es llamar al mis método de su padre que
        //en resumen prepara la máquina para levantar las vistas.
        super.onCreate(savedInstanceState);
        //se setea la vista splash
        setContentView(R.layout.activity_splash);
        //se carga la lista de países
        loadCountryList();
    }

    public void loadCountryList(){
        //Se busca en los datos de la app, la lista de países
        SharedPreferences prefs = getSharedPreferences(Cons.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String paisesJson = prefs.getString(Cons.SP_COUNTRIES, "");
        //Si la lista de países existe, se continua a buscar las lista de partidos.
        if(paisesJson!=null && !paisesJson.equals("")) {
            loadMatchList();
            return;
        }
        //Si por el contrario, no está almacenada la lista de países
        if(isNetworkAvailable()){
            //se verifica que la red esté disponible y se solicita una referencia a la firebase por defecto,
            DatabaseReference paisesDB = FirebaseDatabase.getInstance().getReference().child(Cons.FB_COUNTRIES);
            //Se agregan a la referencia escucha de eventos para cuando los datos lleguen,
            paisesDB.addListenerForSingleValueEvent(new ValueEventListener() {
                //se estará escuchando por dos eventos
                //indatachange: cuando llegan los valores de sde la fb
                //oncancell: cuando ocurre un error a nivel de servidor en la fb
                @Override
                public void onDataChange(@NonNull DataSnapshot paises) {
                    //llegó la lista de países, por tanto se carga cada elemnto,
                    for (DataSnapshot pais: paises.getChildren()) {
                        listaPaises.add((HashMap)pais.getValue());
                    }
                    //y ahora si, se procede con la carga de partidos.
                    loadMatchList();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //Acá se agrega solo un breve control de errores.
                    System.out.println(databaseError.getMessage());
                    Toast.makeText(getApplicationContext(),"Ha ocurrido un error",Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    public boolean isNetworkAvailable(){
        //Se inicializa un conectivity manager, que permite acceder a los datos de conectividad del dispositivo.
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
        //Se muestra un pequeño mensaje de error al no exitir intertnet,
        Toast.makeText(getApplicationContext(),"No hay conexión a internet, conéctese e intente nuevamente.",Toast.LENGTH_LONG).show();
        return false;
    }

    public void loadMatchList(){
        if(isNetworkAvailable()){
            //Proceso análodo al anterior para la lista de partidos.
            DatabaseReference partidosDB = FirebaseDatabase.getInstance().getReference().child(Cons.FB_MATCHS);
            partidosDB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot partidos) {
                    for (DataSnapshot partido: partidos.getChildren()) {
                        listaPartidos.add((HashMap)partido.getValue());
                    }
                    //La diferencia radica en que una vez están cargados los partidos, se procede a cargar la primera página.
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

    @SuppressLint("ApplySharedPref") //Abotación para que ignore el warning por el commit.
    public void goToFirstPage() {
        //Se guardan en las preferencias compartidas los datos descargados
        //de esta manera estarán disponibles durante la ejecución.
        //y serán accesibles desde otras actividades.
        SharedPreferences prefs =getSharedPreferences(Cons.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        SharedPreferences.Editor editor = prefs.edit();
        String paisesJson = prefs.getString(Cons.SP_COUNTRIES, "");
        if(paisesJson!=null && paisesJson.equals("")) {
            editor.putString(Cons.SP_COUNTRIES, gson.toJson(listaPaises));
        }
        editor.putString(Cons.SP_MATCHS, gson.toJson(listaPartidos));
        editor.commit();
        //Ahora se intenta psar a la actividad principal.
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity ( intent );
        this.finish();
    }
}
