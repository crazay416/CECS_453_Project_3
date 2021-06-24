package com.example.project_3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    private Spinner spinner;
    private ProgressDialog progressDialog;

    static ArrayList<HashMap<String, String>> carMakeList;

    private static String urlCarMake = "https://thawing-beach-68207.herokuapp.com/carmakes";
    private static String urlCarModels = "https://thawing-beach-68207.herokuapp.com/carmodelmakes/";
    private static String urlCarMakeModel = "https://thawing-beach-68207.herokuapp.com/cars/10/20/92603";
    private static String urlCarDetail = "https://thawing-beach-68207.herokuapp.com/cars/3484";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        carMakeList = new ArrayList<>();

        spinner = findViewById(R.id.make_id);

        new GetCarMake().execute();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new GetCarModel().execute();
            }
        });


        for(HashMap<String, String> value : carMakeList){
                for(Map.Entry entry : value.entrySet()){
                    String key = (String) entry.getKey();
                    String val = (String) entry.getValue();
                    System.out.println(key + " : " + val);
                }
        }




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


                        HashMap<String, String> carMakeMap = new HashMap<>();

                        carMakeMap.put("vehicle_make", carmake);
                        carMakeMap.put("id", id);

                        carMakeList.add(carMakeMap);
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

            ArrayList<String> list_car_make = new ArrayList<String>();

            //Spinner carMakeSpinner = findViewById(R.id.make_id);
            //carMakeSpinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

            for(int i = 0; i < carMakeList.size(); i++){
                list_car_make.add(carMakeList.get(i).get("vehicle_make"));
            }

            for(int i = 0; i < list_car_make.size(); i++){
                System.out.println(list_car_make.get(i));
            }

            ArrayAdapter<String> aa = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, list_car_make);
            spinner.setAdapter(aa);


            /*
            SpinnerAdapter spinnerAdapter = new SimpleAdapter(MainActivity.this, carMakeList,
                    R.layout.carlist, new String[] {"vehicle_make"}, new int[]{R.id.make_id});


            spinner.setAdapter(spinnerAdapter);

             */

        }
    }
}