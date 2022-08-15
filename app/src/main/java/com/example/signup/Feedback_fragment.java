package com.example.signup;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.signup.Utilities.Constants;
import com.example.signup.Utilities.PreferenceManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class Feedback_fragment extends Fragment {


    View view;
    private PreferenceManager preferenceManager;
    FirebaseFirestore db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        preferenceManager = new PreferenceManager(getActivity().getApplicationContext());
        view = inflater.inflate(R.layout.fragment_feedback_fragment, container, false);

        db = FirebaseFirestore.getInstance();
        ImageView feedback =  view.findViewById(R.id.send_feedback);
        EditText write_feedback = view.findViewById(R.id.writefeedback);

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){

                if(write_feedback.getText().toString().isEmpty())
                {
                    Toast.makeText(getActivity(),"Enter feedback",Toast.LENGTH_LONG).show();
                }
                else
                {
                    String written_feedback = write_feedback.getText().toString();
                    Map<String,Object> user = new HashMap<>();
                    user.put("Feedback",written_feedback);

                    db.collection("Feedbacks")
                            .add(user)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(getActivity(),"Feedback Submitted",Toast.LENGTH_LONG).show();
                                    write_feedback.setText("");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(),"Feedback Failed",Toast.LENGTH_LONG).show();

                        }
                    });

                }
            }
                else{
                    Toast.makeText(getActivity(), "Login First", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;

    }

}