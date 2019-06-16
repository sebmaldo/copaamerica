package com.unab.copaamerica.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.unab.copaamerica.R;
import com.unab.copaamerica.model.OldMatch;

public class HistoricAdapter extends ArrayAdapter<OldMatch> {
    public HistoricAdapter(Context context, OldMatch[] data) {
        super(context, R.layout.old_match, data);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View item = convertView;
        final OldMatchHolder holder;

        if(item == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            item = inflater.inflate(R.layout.old_match, null);

            holder = new OldMatchHolder();
            holder.oldLocal = item.findViewById(R.id.old_local);
            holder.oldVisit = item.findViewById(R.id.old_visit);
            holder.oldResult = item.findViewById(R.id.old_result);
            holder.oldDate = item.findViewById(R.id.old_date);

            item.setTag(holder);

        } else {
            holder = (OldMatchHolder)item.getTag();
        }

        OldMatch currentOldMatch = getItem(position);
        holder.oldLocal.setText(currentOldMatch.getHomeName());
        holder.oldVisit.setText(currentOldMatch.getAwayName());
        holder.oldResult.setText(currentOldMatch.getScore());
        holder.oldDate.setText(currentOldMatch.getDate());

        return(item);
    }

    private class OldMatchHolder {
        TextView oldLocal;
        TextView oldVisit;
        TextView oldResult;
        TextView oldDate;
    }
}