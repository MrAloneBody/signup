package com.example.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaCodec;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.signup.Utilities.Constants;
import com.example.signup.Utilities.PreferenceManager;
import com.example.signup.databinding.ActivitySignInBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());

     //if already login dont show login page
           if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();

        }

        setListeners();
    }

    private void setListeners(){
        binding.newUser.setOnClickListener( v ->
                startActivity(new Intent(getApplicationContext(),SignUpActivity.class)));

        binding.LoginBtn.setOnClickListener(v -> {
            if(isValidSignInDetails()){
                signIn();
            }
        });
    }
    private void signIn(){

        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL,binding.etMail.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD,binding.txtPassword.getText().toString())
                .get()
                .addOnCompleteListener(task ->{
                    if(task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0){

                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
                        preferenceManager.putString(Constants.KEY_USER_ID,documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_NAME,documentSnapshot.getString(Constants.KEY_NAME));
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    else {
                        loading(false);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,false);
                        showToast("Unable to SignIn.");
                    }
                });

    }

    private void loading(Boolean isLoading){
        if(isLoading){
            binding.LoginBtn.setVisibility(View.INVISIBLE);
            binding.progressbar.setVisibility(View.VISIBLE);
        }else{
            binding.progressbar.setVisibility(View.INVISIBLE);
            binding.LoginBtn.setVisibility(View.VISIBLE);
        }
    }


    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private Boolean isValidSignInDetails(){
        if(binding.etMail.getText().toString().trim().isEmpty()){
            showToast("Enter Email");
            return false;
        }
//        else if(!Pattern.EMAIL_ADDRESS.matcher(binding.etMail.getText().toString().matches())){
 //           showToast("Enter Valid Email");
   //         return false;
    //    }
        else if(binding.txtPassword.getText().toString().trim().isEmpty()){
            showToast("Enter Password");
            return false;
        }else{
            return true;
        }


    }

}
