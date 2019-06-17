package com.unab.copaamerica;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.unab.copaamerica.adapter.PositionAdapter;
import com.unab.copaamerica.helpers.FirebaseHelper;
import com.unab.copaamerica.model.Country;

import java.util.ArrayList;

public class ActividadGrupalActivity extends AppCompatActivity {
    ArrayList<Country> Positions;
    Context context;
    boolean dentro = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        createPositionView();
    }

    private void createPositionView() {
        setContentView(R.layout.country_positions_selected_list);
        Positions = FirebaseHelper.getFirstPlaces();
        PositionAdapter adapter = new PositionAdapter(context, Positions.toArray(new Country[Positions.size()]));
        ListView positionList = findViewById(R.id.cpsl_list);
        View header = getLayoutInflater().inflate(R.layout.country_position_selected_list_header, null);
        positionList.addHeaderView(header);
        positionList.setAdapter(adapter);

        positionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dentro = true;
                setContentView(R.layout.country_list);
            }
        });
    }

    @Override
    public void onBackPressed(){
        if(dentro){
            dentro = false;
            createPositionView();
        }
        else {
            finish();
            super.onBackPressed();
        }
    }
}
