package com.unab.copaamerica.helpers;

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
        inMemPosition.add(position, toSave);
    }

    public static ArrayList<Country> getFirstPlaces(){
        //TODO: Acá va el código de conexión a Firebase para bajar los 3 primeros lugares.
        //Ojalá que utilice las preferencias para ir a buscar.

        /*Inicio de la sección de código para tener datos*/
        /*Esta sección de código es para tener datos, se debe eliminar al tener implementado
        * firebase, debe en retornar un array list de 3 posiciones.*/
        ArrayList<Country> salida = new ArrayList<>();
        salida.add(new Country("Chile",
                "CHL",
                "\uD83C\uDDE8\uD83C\uDDF1",
                "1749",
                "",
                "5%"));
        salida.add(new Country("Argentina",
                "ARG",
                "\uD83C\uDDE6\uD83C\uDDF7",
                "1443",
                "",
                "9%"));
        salida.add(null);

        if(inMemPosition.size()==0){
            inMemPosition = salida;
        }
        /*Fin de la sección de código para tener datos*/

        return inMemPosition;
    }
}
