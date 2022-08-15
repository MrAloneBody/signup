package com.example.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceControl;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.signup.Utilities.Constants;
import com.example.signup.Utilities.PreferenceManager;
import com.example.signup.databinding.ActivityMainBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    TextView news_frag,feedback_frag,send_feedback;
    FrameLayout backlayout;
    ScrollView current_matches_layout;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());

        news_frag = findViewById(R.id.News_fragment);
        feedback_frag = findViewById(R.id.Feedback_fragment);

        backlayout = findViewById(R.id.frame_layout);
        current_matches_layout = findViewById(R.id.Current_matches_layout);


        news_frag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_matches_layout.setVisibility(View.GONE);

                backlayout.setVisibility(View.VISIBLE);
                replaceFragment(new NewsFragment());

            }
        });

        feedback_frag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_matches_layout.setVisibility(View.GONE);
                backlayout.setVisibility(View.VISIBLE);

                replaceFragment(new Feedback_fragment());

            }
        });

//        loadUserDetails();
 //       getToken();
  //      setListeners();
    }
/*
    private void setListeners(){
        ImageView sign_out;
        sign_out = findViewById(R.id.ImgSignOut);
        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        //binding.ImgSignOut.setOnClickListener();
    }


 */


    public void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        backlayout.setVisibility(View.GONE);
        current_matches_layout.setVisibility(View.VISIBLE);
    }

/*
    private void loadUserDetails(){
        Log.d("UsernameDataPref", preferenceManager.getString(Constants.KEY_NAME));

        TextView username;
        username = findViewById(R.id.Show_user_name);
        username.setText(preferenceManager.getString(Constants.KEY_NAME));
    //    binding.ShowUserName.setText(preferenceManager.getString(Constants.KEY_NAME));
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }
    private void updateToken(String token){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        documentReference.update(Constants.KEY_FCM_TOKEN,token)
                .addOnSuccessListener(unused -> showToast("Token Updated Successfully"))
                .addOnFailureListener(e -> showToast("Unable to update token"));

    }





    private void signOut(){
        showToast("Signing Out...");
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    preferenceManager.clear();
                    startActivity(new Intent(getApplicationContext(),SignInActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> showToast("Unable to SignOut"));
    }



 */
}