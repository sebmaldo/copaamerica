package com.unab.copaamerica.helpers;

import com.unab.copaamerica.model.Country;

import java.util.ArrayList;
import java.util.List;

public class FirebaseHelper {
    public static void saveToFirebase(ArrayList<Country> toSave) {
        //TODO: Acá va el código de conexión a Firebase para salvar los lugares.
        //Aqui van las dos lineas para actualizar la base datos...
    }

    public static ArrayList<Country> getFirstPlaces(){
        //TODO: Acá va el código de conexión a Firebase para bajar los 3 primeros lugares.
        //Ojalá que utilice las preferencias para ir a buscar.
        ArrayList<Country> salida = new ArrayList<>();
        salida.add(new Country("Argentina",
                "ARG",
                "\uD83C\uDDE6\uD83C\uDDF7",
                "1443",
                "",
                "9%"));
        salida.add(new Country("Argentina",
                "ARG",
                "\uD83C\uDDE6\uD83C\uDDF7",
                "1443",
                "",
                "9%"));
        salida.add(null);
        return salida;
    }
}
