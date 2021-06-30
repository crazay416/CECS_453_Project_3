package com.example.project_3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class Car_Data extends AppCompatActivity {

    private static String urlCarDetail = "https://thawing-beach-68207.herokuapp.com/cars/";

    static ArrayList<HashMap<String, String>> carDetails;

    TextView price;
    TextView car_model;
    TextView description;
    TextView lastUpdate;
    ImageView carPicture;
    ImageView view;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_data);

        carDetails = new ArrayList<>();

        /*
        Recieve the information that was passed from the MainActivity class
         */
        Intent intent = getIntent();
        HashMap<String, String> carInfo = (HashMap<String, String>)intent.getSerializableExtra("car");



        new GetCarDetails(carInfo).execute();

    }


    /*
        GetCarDetails will retrieve the informaiton that it needs from the API to create a
        description of the specific vehicle that was selected
    */
    private class GetCarDetails extends AsyncTask<Void, Void, Void>{


        private HashMap<String, String> carInfo = new HashMap<String, String>();
        private String currency;
        private String model;
        private String carDetails;
        private String carUpdate;

        public GetCarDetails(HashMap carinfo){
            this.carInfo = carinfo;
        }


        /*
            Creates an http Handler to grab the information that is needed from the restfulk API
            It uses the Car Id alongisde the urlCarDetail to get the car's specific informaiton
            It gets the price, model, description and when it was last updated
        */
        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall(urlCarDetail + carInfo.get("id"));

            if(jsonStr != null){
                try{

                    JSONArray jsonArray = new JSONArray(jsonStr);

                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject d = jsonArray.getJSONObject(i);
                        currency = d.getString("price");
                        model = carInfo.get("model");
                        carDetails = d.getString("veh_description");
                        carUpdate = d.getString("updated_at");

                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }

            return null;
        }

        /*
            In the onPostExecute, it declares the EditViews and TextViews to the given id in
            the xml file. We then initialize the them by giving it the values from the doInBackground

        */
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            carPicture = findViewById(R.id.carPic);


            price = findViewById(R.id.price);
            price.setText(currency);

            car_model = findViewById(R.id.makeModel);
            car_model.setText(model);

            description = findViewById(R.id.carSpecs);
            description.setText(carDetails);

            lastUpdate = findViewById(R.id.lastUpdate);
            lastUpdate.setText(carUpdate);

            view = findViewById(R.id.carPic);
            view.setImageResource(R.drawable.car_image);



        }


        }

}