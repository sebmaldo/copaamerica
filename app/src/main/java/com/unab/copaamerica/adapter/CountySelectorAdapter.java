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

public class CountySelectorAdapter extends ArrayAdapter<Country> {
    public CountySelectorAdapter(@NonNull Context context,
                                 @NonNull Country[] data){
        super(context, R.layout.country_item, data);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View item = convertView;
        final CountryItemHolder holder;

        if(item == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            item = inflater.inflate(R.layout.country_item, null);

            holder = new CountryItemHolder();
            holder.codigoPais = item.findViewById(R.id.ci_codigo);
            holder.banderaPais = item.findViewById(R.id.ci_bandera);
            holder.nombrePais = item.findViewById(R.id.ci_nombre);

            item.setTag(holder);

        } else {
            holder = (CountryItemHolder) item.getTag();
        }

        Country pais = getItem(position);
        holder.codigoPais.setText(pais.getCodigo());
        holder.banderaPais.setText(pais.getBandera());
        holder.nombrePais.setText(pais.getNombre());

        return item;
    }

    private class CountryItemHolder {
        TextView codigoPais;
        TextView banderaPais;
        TextView nombrePais;
    }

}
