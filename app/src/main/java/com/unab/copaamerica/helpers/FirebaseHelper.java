package com.unab.copaamerica.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.unab.copaamerica.constants.Cons;
import com.unab.copaamerica.model.Country;

import java.util.ArrayList;

public class FirebaseHelper {
    private static ArrayList<Country> inMemPosition = new ArrayList<>();

    public static void saveToFirebase(ArrayList<Country> toSave) {
        //TODO: Acá va el código de conexión a Firebase para salvar los lugares.
        //Aqui van las dos lineas para actualizar la base datos...
    }

    public static void saveToFirebase(int position, Country toSave) {
        //TODO: Acá va el código de conexión a Firebase para salvar los lugares.
        //Aqui van las dos lineas para actualizar la base datos...

        /*Sección de código para guardar en memoria por mientras en ram*/
        inMemPosition.remove(position);
        inMemPosition.add(position, toSave);
    }

    public static ArrayList<Country> getFirstPlaces(){
        //TODO: Acá va el código de conexión a Firebase para bajar los 3 primeros lugares.
        //Ojalá que utilice las preferencias para ir a buscar.

        /*Inicio de la sección de código para tener datos*/
        /*Esta sección de código es para tener datos, se debe eliminar al tener implementado
        * firebase, debe en retornar un array list de 3 posiciones.*/
        ArrayList<Country> salida = new ArrayList<>();
        salida.add(null);
        salida.add(null);
        salida.add(null);

        if(inMemPosition.size()==0){
            inMemPosition = salida;
        }
        /*Fin de la sección de código para tener datos*/

        return inMemPosition;
    }

}
