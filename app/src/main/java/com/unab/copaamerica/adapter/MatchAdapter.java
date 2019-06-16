package com.unab.copaamerica.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.unab.copaamerica.R;
import com.unab.copaamerica.model.Match;

public class MatchAdapter extends ArrayAdapter<Match> {
    public MatchAdapter(Context context, Match[] data) {
        super(context, R.layout.match, data);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View item = convertView;
        final MatchHolder holder;

        if(item == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            item = inflater.inflate(R.layout.match, null);

            holder = new MatchHolder();
            holder.codigoLocal = item.findViewById(R.id.codigo_local);
            holder.banderaLocal = item.findViewById(R.id.bandera_local);
            holder.codigoVisita = item.findViewById(R.id.codigo_visita);
            holder.banderaVisita = item.findViewById(R.id.bandera_visita);
            holder.hora = item.findViewById(R.id.hora);
            holder.fecha = item.findViewById(R.id.fecha);

            item.setTag(holder);

        } else {
            holder = (MatchHolder)item.getTag();
        }

        Match currentMatch = getItem(position);
        holder.codigoLocal.setText(currentMatch.getLocal().getCodigo());
        holder.banderaLocal.setText(currentMatch.getLocal().getBandera());
        holder.codigoVisita.setText(currentMatch.getVisita().getCodigo());
        holder.banderaVisita.setText(currentMatch.getVisita().getBandera());
        holder.hora.setText(currentMatch.getHora());
        holder.fecha.setText(currentMatch.getFecha());

        return(item);
    }

    private class MatchHolder {
        TextView codigoLocal;
        TextView banderaLocal;
        TextView codigoVisita;
        TextView banderaVisita;
        TextView hora;
        TextView fecha;
    }

}