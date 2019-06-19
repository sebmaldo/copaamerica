package com.unab.copaamerica.helpers;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.unab.copaamerica.model.Country;

import java.util.ArrayList;


public class FirebaseHelper {
    private static ArrayList<Country> inMemPosition = new ArrayList<>();
    private static DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    public static void initHelper(ArrayList<Country> toSave) {
        //TODO: Acá va el código de conexión a Firebase para salvar los lugares.
        //Aqui van las dos lineas para actualizar la base datos...

        inMemPosition = toSave;
    }

    public static void saveToFirebase(int position, Country toSave) {
        //TODO: Acá va el código de conexión a Firebase para salvar los lugares.
        //Aqui van las dos lineas para actualizar la base datos...

        /*Sección de código para guardar en memoria por mientras en ram*/
        inMemPosition.remove(position);
        inMemPosition.add(position, toSave);

        dbRef.child("favoritos").child("favorito_" + position).setValue(toSave);
    }

    public static ArrayList<Country> getFirstPlaces(){
        //TODO: Acá va el código de conexión a Firebase para bajar los 3 primeros lugares.
        //Ojalá que utilice las preferencias para ir a buscar.

        /*Inicio de la sección de código para tener datos*/
        /*Esta sección de código es para tener datos, se debe eliminar al tener implementado
        * firebase, debe en retornar un array list de 3 posiciones.*/

        if(inMemPosition.size()==0){
            ArrayList<Country> salida = new ArrayList<>();
            salida.add(null);
            salida.add(null);
            salida.add(null);
            inMemPosition = salida;
        }

        /*Fin de la sección de código para tener datos*/

        return inMemPosition;

    }

}
