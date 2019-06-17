package com.unab.copaamerica.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.unab.copaamerica.R;
import com.unab.copaamerica.model.Country;

public class PositionAdapter extends ArrayAdapter<Country> {
    public PositionAdapter(@NonNull Context context,
                           @NonNull Country[] data){
        super(context, R.layout.country_position_selected_item, data);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View item = convertView;
        final CountryHolder holder;

        if(item == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            item = inflater.inflate(R.layout.country_position_selected_item, null);

            holder = new CountryHolder();
            holder.posicionPais = item.findViewById(R.id.cpsi_posicion);
            holder.banderaPais = item.findViewById(R.id.cpsi_bandera);
            holder.probabilidadPais = item.findViewById(R.id.cpsi_porcentaje);

            item.setTag(holder);

        } else {
            holder = (CountryHolder) item.getTag();
        }

        Country pais = getItem(position);
        holder.posicionPais.setText(Integer.toString(position+1));
        holder.banderaPais.setText( pais == null ? "\uD83C\uDFF4\u200D☠️" : pais.getBandera());
        holder.probabilidadPais.setText(pais == null ? "Selecciona Pais" : pais.getPorcentajeProbabilidad() + " Probabilidad");

        return item;
    }

    private class CountryHolder{
        TextView posicionPais;
        TextView banderaPais;
        TextView probabilidadPais;
    }

}
