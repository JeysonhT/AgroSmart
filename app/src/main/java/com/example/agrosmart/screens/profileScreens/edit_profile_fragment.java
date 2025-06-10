package com.example.agrosmart.screens.profileScreens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.agrosmart.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class edit_profile_fragment extends Fragment {

    private String username;
    private String phoneNumber;
    private String municipality;
    private String[] soilTypes;

    private FirebaseFirestore db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile_fragment, container, false);




        return view;
    }

    private Map<String, Object> getUserDetails(FirebaseFirestore db, )
}