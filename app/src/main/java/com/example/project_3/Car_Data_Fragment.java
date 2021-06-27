package com.example.project_3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Car_Data_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Car_Data_Fragment extends Fragment {

    TextView currency;
    TextView carmodel;
    TextView carDetails;
    TextView carUpdate;
    ImageView carView;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;
    private String mParam4;

    public Car_Data_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Car_Data_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Car_Data_Fragment newInstance(String param1, String param2, String param3, String param4) {
        Car_Data_Fragment fragment = new Car_Data_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
            mParam4 = getArguments().getString(ARG_PARAM4);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_car_data, container, false);

        currency = root.findViewById(R.id.price);
        currency.setText(mParam1);


        carmodel = root.findViewById(R.id.makeModel);
        carmodel.setText(mParam2);

        carDetails = root.findViewById(R.id.carSpecs);
        carDetails.setText(mParam3);

        carDetails = root.findViewById(R.id.carSpecs);
        carDetails.setText(mParam3);

        carUpdate = root.findViewById(R.id.lastUpdate);
        carUpdate.setText(mParam4);

        carView = root.findViewById(R.id.carPic);
        carView.setImageResource(R.drawable.car_image);







        return root;
    }



}