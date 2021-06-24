package com.example.project_3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ListView lv;
    private ProgressDialog progressDialog;

    static ArrayList<HashMap<String, String>> carmakeList;

    private static String urlCarMake = "https://thawing-beach-68207.herokuapp.com/carmakes";
    private static String urlCarModels = "https://thawing-beach-68207.herokuapp.com/carmodelmakes/";
    private static String urlCarMakeModel = "https://thawing-beach-68207.herokuapp.com/cars/10/20/92603";
    private static String urlCarDetail = "https://thawing-beach-68207.herokuapp.com/cars/3484";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        carmakeList = new ArrayList<>();

        lv = findViewById(R.id.list);

        new GetCarMake().execute();



    }

    private class GetCarMake extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }


        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall(urlCarMake);

            if(jsonStr != null){
                try{
                    //JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONObject jsonObject;

                    JSONArray jsonArray = new JSONArray(jsonStr);

                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject d = jsonArray.getJSONObject(i);
                        String carmake = d.getString("vehicle_make");
                        String id = d.getString("id");


                        HashMap<String, String> carmakemap = new HashMap<>();

                        carmakemap.put("vehicle_make", carmake);
                        carmakemap.put("id", id);

                        carmakeList.add(carmakemap);
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);

            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            Spinner carmakespinner = findViewById(R.id.make_id);
            ArrayAdapter<>



            for(HashMap<String, String> value : carmakeList){
                for(Map.Entry entry : value.entrySet()){
                    String key = (String) entry.getKey();
                    String val = (String) entry.getValue();
                    System.out.println(key + " : " + val);
                }
            }
        }
    }
}