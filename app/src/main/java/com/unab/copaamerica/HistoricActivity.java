package com.unab.copaamerica;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.gson.Gson;
import com.unab.copaamerica.adapter.HistoricAdapter;
import com.unab.copaamerica.constants.Cons;
import com.unab.copaamerica.model.APIResponse;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HistoricActivity extends AppCompatActivity {
    String local;
    String visita;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic);
        context = this;

        Bundle bundle = this.getIntent().getExtras();

        local = bundle.getString(Cons.B_LOCAL_API);
        visita = bundle.getString(Cons.B_VISIT_API);

        new GetDataTask().execute(Cons.URL_API);

    }

    private String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            for(int i = is.read() ; i != -1 ; i = is.read()) {
                bo.write(i);
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }

    private class GetDataTask extends AsyncTask<String, Void, APIResponse[]> {
        protected APIResponse[] doInBackground(String... urlString) {
            try {
                APIResponse localAPIResponse = null;
                APIResponse visitaAPIResponse = null;

                Gson gson = new Gson();

                URL url = new URL(urlString[0] + local);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    String localResponse = readStream(in);
                    localAPIResponse = gson.fromJson(localResponse, APIResponse.class);
                } catch(Exception e){
                    System.out.println(e.getMessage());
                } finally{
                    urlConnection.disconnect();
                }

                url = new URL(urlString[0] + visita);
                urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    String visitaResponse = readStream(in);
                    visitaAPIResponse = gson.fromJson(visitaResponse, APIResponse.class);
                } catch(Exception e){
                    System.out.println(e.getMessage());
                } finally{
                    urlConnection.disconnect();
                }

                APIResponse[] response = new APIResponse[2];
                response[0] = localAPIResponse;
                response[1] = visitaAPIResponse;
                return response;
            } catch (Exception e){

            }
            return new APIResponse[2];
        }

        protected void onPostExecute(APIResponse[] result) {
            HistoricAdapter adapterLocal = new HistoricAdapter(context, result[0].getData().getOldMatches());
            HistoricAdapter adapterVisit = new HistoricAdapter(context, result[1].getData().getOldMatches());

            ListView localHistory = findViewById(R.id.old_local_matchs);
            ListView visitaHistory = findViewById(R.id.old_visit_matchs);

            localHistory.setAdapter(adapterLocal);
            visitaHistory.setAdapter(adapterVisit);
        }
    }
}
