package com.example.project_3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
    private boolean mTwoPane = false;
    private Car_Data_Fragment car_data_fragment;


    static ArrayList<HashMap<String, String>> carMakeList;
    static ArrayList<HashMap<String, String>> carModelList;
    static ArrayList<HashMap<String, String>> car_specific_list;


    private static String urlCarMake = "https://thawing-beach-68207.herokuapp.com/carmakes";
    private static String urlCarModels = "https://thawing-beach-68207.herokuapp.com/carmodelmakes/";
    private static String urlCarMakeModel = "https://thawing-beach-68207.herokuapp.com/cars/";
    private static String urlCarDetail = "https://thawing-beach-68207.herokuapp.com/cars/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_car);


        carMakeList = new ArrayList<>();
        carModelList = new ArrayList<>();
        car_specific_list = new ArrayList<>();

        car_make_spinner = findViewById(R.id.make_id);
        car_model_spinner = findViewById(R.id.model_id);
        lv = findViewById(R.id.list);

        if (findViewById(R.id.car_details_fragment) != null){
            mTwoPane = true;
            //Toast.makeText(getApplicationContext(), "CAR DETAIL IS TRUE", Toast.LENGTH_LONG).show();
        }
        /*
        else{
            Toast.makeText(getApplicationContext(), "CAR DETAIL IS NOT TRUE", Toast.LENGTH_LONG).show();
        }
         */


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
                    String model_id = modelmap.get("id");
                    //Toast.makeText(getApplicationContext(), "ID: "+ car_model, Toast.LENGTH_SHORT).show();
                    new GetListCarModel(model_id, car_make_id).execute();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {



                }
            });

        }
    }

    private class GetListCarModel extends AsyncTask<Void, Void, Void>{
        String model_id;
        String make_id;
        String zipcode = "92603";

        public GetListCarModel(String model_id, String make_id){
            this.model_id = model_id;
            this.make_id = make_id;

        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(car_specific_list.size() != 0){
                car_specific_list.clear();
            }


            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall(urlCarMakeModel +  make_id + "/" + model_id + "/" + zipcode );

            if(jsonStr != null){
                try{

                    //JSONArray jsonArray = new JSONArray(jsonStr);
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONArray cars =jsonObject.getJSONArray("lists");


                    for(int i = 0; i < cars.length(); i++){
                        JSONObject d = cars.getJSONObject(i);
                        String carmodel = d.getString("model");
                        String car_detail = d.getString("id");
                        String car_color = d.getString("color");


                        HashMap<String, String> carModelMap = new HashMap<>();

                        carModelMap.put("model", carmodel);
                        carModelMap.put("id", car_detail);
                        carModelMap.put("color", car_color);

                        car_specific_list.add(carModelMap);




                    }

                } catch (JSONException e){
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }


            ListAdapter adapter = new SimpleAdapter(MainActivity.this, car_specific_list,
                    R.layout.carlist, new String[] {"model", "id", "color"}, new int[]{
                    R.id.car, R.id.color, R.id.detail});

            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(mTwoPane){
                        String frag_id = car_specific_list.get(position).get("id");
                        String frag_model = car_specific_list.get(position).get("model");
                        String frag_color = car_specific_list.get(position).get("color");
                        new GetCarFragment(frag_id, frag_model).execute();
                        //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        //car_data_fragment = Car_Data_Fragment.newInstance(frag_id, frag_model, frag_color);
                        //transaction.replace(R.id.car_details_fragment, car_data_fragment);
                        //transaction.addToBackStack(null);
                        //transaction.commit();

                    }
                    else {
                        Intent intent = new Intent(MainActivity.this, Car_Data.class);
                        String message = "abc";
                        Toast.makeText(getApplicationContext(), "Position: " + car_specific_list.get(position), Toast.LENGTH_LONG).show();
                        intent.putExtra("car", car_specific_list.get(position));
                        startActivity(intent);
                    }
                    
                }

            });

                }
        }
    private class GetCarFragment extends AsyncTask<Void, Void, Void>{
        String car_id;
        String currency;
        String model;
        String carDetails;
        String carUpdate;

        GetCarFragment(String car_id, String model){
            this.car_id = car_id;
            this.model = model;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall(urlCarDetail + car_id);

            if(jsonStr != null){
                try{

                    JSONObject jsonObject;

                    JSONArray jsonArray = new JSONArray(jsonStr);

                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject d = jsonArray.getJSONObject(i);
                        currency = d.getString("price");
                        carDetails = d.getString("veh_description");
                        carUpdate = d.getString("updated_at");
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            car_data_fragment = Car_Data_Fragment.newInstance(currency, model, carDetails, carUpdate);
            transaction.replace(R.id.car_details_fragment, car_data_fragment);
            transaction.addToBackStack(null);
            transaction.commit();

        }

    }
}