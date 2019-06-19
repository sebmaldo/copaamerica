package com.unab.copaamerica;

import android.content.Context;
import android.content.Intent;
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

/**
 * Clase de la actividad grupal, que hereda de una actividad compacta.
 */
public class ActividadGrupalActivity extends AppCompatActivity {

    /**
     * Para manejar los estados de la actividad usamos el listado de posiciones
     * en paises el listado de los paises completos
     * como utilizamos más de una vista en una misma actividad usamos la variable dentro para decir
     * si estamos dentro de la segunda vista o no.
     * en position dejamos el puesto que estamos dejando seleccionando en este momento.
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
     * Metodo on create se sobre escribe y con eso se utiliza el metodo secundario createPositionView
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        createPositionView();
    }

    /**
     * Metodo auxiliar que recibe un listado de paises como hash y lo transforma a lista de countries.
     * @param listaPaises
     * @return
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
        PositionAdapter adapter = new PositionAdapter(context, positions.toArray(new Country[positions.size()]));
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

        Button sendMail = findViewById(R.id.cpsl_button);
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
        ArrayList<LinkedTreeMap<String, Object>> listaPaises = gson.fromJson(paisesJson, ArrayList.class);
        paises = getCountries(listaPaises);
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

    private void sendMail() {
        String subject = "Mis predicciones para la copa america";
        String text = "Hola:\n\n" +
                "Te comparto mis predicciones para la copa america 2019\n\n" +
                "1.- " + (positions.get(0) == null ? "Aún no lo sé" : positions.get(0).getNombre()) + "\n" +
                "2.- " + (positions.get(1) == null ? "Aún no lo sé" : positions.get(1).getNombre()) + "\n" +
                "3.- " + (positions.get(2) == null ? "Aún no lo sé" : positions.get(2).getNombre()) + "\n" +
                "\n\n ¿Qué opinas tú?";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(intent);
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
        return filtered.toArray(new Country[filtered.size()]);
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
