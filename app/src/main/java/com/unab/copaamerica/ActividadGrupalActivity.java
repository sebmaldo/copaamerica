package com.unab.copaamerica;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.unab.copaamerica.adapter.CountySelectorAdapter;
import com.unab.copaamerica.adapter.PositionAdapter;
import com.unab.copaamerica.constants.Cons;
import com.unab.copaamerica.helpers.FirebaseHelper;
import com.unab.copaamerica.model.Country;

import java.util.ArrayList;

/**
 * Clase de la actividad grupal, que hereda de una actividad compacta.
 */
public class ActividadGrupalActivity extends AppCompatActivity {

    /**
     * Para manejar los estados de la actividad usamos:
     * -> posiciones: que contendrá listado de posiciones
     * -> paises: el listado de los paises completos
     * -> position guarda el puesto que está seleccionando.
     *
     * Como se usa más de una vista esta actividad usamos la variable "dentro" como bandera
     * representando si estamos dentro de la segunda vista o no.
     *
     */
    ArrayList<Country> positions;
    ArrayList<Country> paises;
    boolean dentro = false;
    int position = -1;

    /**
     * Se usa el contexto para indicar a los adapters donde deben trabajar.
     */
    Context context;

    /**
     * Metodo on create se sobrescribe llamando al método padre, seteando el conetxto actual y creando la vista de posiciones.
     * @param savedInstanceState Estado de la instancia
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        createPositionView();
    }

    /**
     * Metodo auxiliar que recibe un listado de paises como hash y lo transforma a lista de countries.
     * @param listaPaises ArrayList que contiene los países
     * @return ArrayList
     */
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

    /**
     * Método auxiliar para la creación de la vista de posiciones.
     */
    private void createPositionView() {
        dentro = false;
        position = -1;
        //Se setea el layout de la lista
        setContentView(R.layout.country_positions_selected_list);
        //Se usa el firebase helper para poder traer el listado de paises.
        positions = FirebaseHelper.getFirstPlaces();
        //Por motivos de performance, entregar un arrglo con largo fijo es indeseable, se recomienda con
        //largo variable
        PositionAdapter adapter = new PositionAdapter(context, positions.toArray(new Country[/*positions.size()*/0]));
        ListView positionList = findViewById(R.id.cpsl_list);
        @SuppressLint("InflateParams")
        View header = getLayoutInflater().inflate(R.layout.country_position_selected_list_header, null);
        positionList.addHeaderView(header);
        positionList.setAdapter(adapter);

        positionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                createCountrySelectorView(i);
            }
        });

        Button sendMail = findViewById(R.id.cpsl_button);
        //lambda exp (Java 8)
        sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMail();
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
        @SuppressWarnings("unchecked")
        ArrayList<LinkedTreeMap<String, Object>> listaPaises = gson.fromJson(paisesJson, ArrayList.class);
        paises = getCountries(listaPaises);
        final Country[] countiesToSelect = getPosibleCountries();
        CountySelectorAdapter adapter = new CountySelectorAdapter(context, countiesToSelect);
        final ListView countryList = findViewById(R.id.ci_list);
        @SuppressLint("InflateParams")
        View header = getLayoutInflater().inflate(R.layout.country_list_header, null);
        //root es un ViewGroup que sirve vomo padre para definir kayouts en las view hijas. Ya que no se usa esa
        //característuca, se suprime el waring
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

    private void sendMail() {
        if(positions.get(0)!=null || positions.get(1)!=null || positions.get(2)!=null){
            String asunto = "Mis predicciones para la copa america";
            String contenido = "Hola:<br>" +
                    "Te comparto <b>mis predicciones</b> para la copa america 2019<br>" +
                    "<ol>" +
                    "   <li>[1] " + (positions.get(0) == null ? "Aún no lo sé" : positions.get(0).getNombre()) + "</li>" +
                    "   <li>[2] " + (positions.get(1) == null ? "Aún no lo sé" : positions.get(1).getNombre()) + "</li>" +
                    "   <li>[3] " + (positions.get(2) == null ? "Aún no lo sé" : positions.get(2).getNombre()) + "</li>" +
                    "</ol>" +
                    "<br><br> ¿Qué opinas tú?";
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/html");
            intent.putExtra(Intent.EXTRA_SUBJECT, asunto);
            intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(contenido));
            startActivity(intent);
        }
        else{
             Toast.makeText(getApplicationContext(),"Seleccione al menos un país",Toast.LENGTH_SHORT).show();
        }
    }

    private Country[] getPosibleCountries(){
        ArrayList<Country> filtered = new ArrayList<>();
        for (Country item: paises){
            boolean putItem = true;
            for(int i = 0; i < 3; i ++){
                Country toReview = positions.get(i);
                if(toReview!=null && toReview.getCodigo().equalsIgnoreCase(item.getCodigo()) && i != position - 1){
                    putItem = false;
                }
            }
            if(putItem){
                filtered.add(item);
            }
        }
        return filtered.toArray(new Country[0/*filtered.size()*/]);
    }

    @Override
    public void onBackPressed(){
        if(dentro){
            createPositionView();
        }
        else {
            super.onBackPressed();
        }
    }
}
