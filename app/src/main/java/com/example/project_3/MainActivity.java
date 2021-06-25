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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements OnTaskCompleted {

    private ListView lv;
    private Spinner car_make_spinner;
    private Spinner car_model_spinner;
    private ProgressDialog progressDialog;
    private OnTaskCompleted listener;
    private String value;

    static ArrayList<HashMap<String, String>> carMakeList;
    static ArrayList<HashMap<String, String>> carModelList;

    private static String urlCarMake = "https://thawing-beach-68207.herokuapp.com/carmakes";
    private static String urlCarModels = "https://thawing-beach-68207.herokuapp.com/carmodelmakes/";
    private static String urlCarMakeModel = "https://thawing-beach-68207.herokuapp.com/cars/10/20/92603";
    private static String urlCarDetail = "https://thawing-beach-68207.herokuapp.com/cars/3484";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        carMakeList = new ArrayList<>();
        carModelList = new ArrayList<>();

        car_make_spinner = findViewById(R.id.make_id);
        car_model_spinner = findViewById(R.id.model_id);

        //GetCarMake  task = new GetCarMake(this);
        new GetCarMake(this).execute();









        /*
        System.out.println("Hello");
        for(HashMap<String, String> value : carMakeList){
                for(Map.Entry entry : value.entrySet()){
                    String key = (String) entry.getKey();
                    String val = (String) entry.getValue();
                    System.out.println(key + " : " + val);
                }
        }*/




    }

    @Override
    public void onTaskCompleted(String value) {
        System.out.println("THIS IS VALUEEE" + value);

    }

    private class GetCarMake extends AsyncTask<Void, Void, Void> {
        public OnTaskCompleted listener;

        public GetCarMake(OnTaskCompleted listener){
            this.listener = listener;
        }

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



            for(int i = 0; i < carMakeList.size(); i++){
                list_car_make.add(carMakeList.get(i).get("vehicle_make"));
            }

            for(int i = 0; i < list_car_make.size(); i++){
                System.out.println(list_car_make.get(i));
            }

            ArrayAdapter<String> aa = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, list_car_make);
            car_make_spinner.setAdapter(aa);

            car_make_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    HashMap<String, String> makemap = carMakeList.get(position);
                    String car_make = makemap.get("id");
                    new GetCarModel(car_make).execute();


                    //Toast.makeText(getApplicationContext(), "ID: "+ car_make, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            //listener.onTaskCompleted(list_car_make.get(0));




        }
    }

    private class GetCarModel extends AsyncTask<Void, Void, Void>{
        String car_make_id;


        public GetCarModel(String car_make_id){
            this.car_make_id = car_make_id;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (carModelList.size() != 0){
                carModelList.clear();
            }

            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall(urlCarModels +  car_make_id );

            if(jsonStr != null){
                try{

                    JSONArray jsonArray = new JSONArray(jsonStr);

                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject d = jsonArray.getJSONObject(i);
                        String carmodel = d.getString("model");
                        String id = d.getString("id");
                        String vehicle_make_id = d.getString("vehicle_make_id");


                        HashMap<String, String> carModelMap = new HashMap<>();

                        carModelMap.put("model", carmodel);
                        carModelMap.put("id", id);
                        carModelMap.put("vehicle_make_id", vehicle_make_id);

                        carModelList.add(carModelMap);
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

            ArrayList<String> list_car_model = new ArrayList<String>();



            for(int i = 0; i < carModelList.size(); i++){
                list_car_model.add(carModelList.get(i).get("model"));
            }

            for(int i = 0; i < list_car_model.size(); i++){
                System.out.println(list_car_model.get(i));
            }

            ArrayAdapter<String> aa = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, list_car_model);
            car_model_spinner.setAdapter(aa);

            car_model_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    HashMap<String, String> modelmap = carModelList.get(position);
                    String car_model = modelmap.get("vehicle_make_id");
                    //new GetCarModel(car_make).execute();


                    Toast.makeText(getApplicationContext(), "ID: "+ car_model, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
    }

}