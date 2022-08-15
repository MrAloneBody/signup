package com.example.signup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.signup.Utilities.Constants;
import com.example.signup.Utilities.PreferenceManager;
import com.example.signup.databinding.ActivitySignInBinding;
import com.example.signup.databinding.ActivitySignUpBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
    }

    private void setListeners(){
        binding.alreadyHaveAcc.setOnClickListener( v ->
                startActivity(new Intent(getApplicationContext(),SignInActivity.class)));

        binding.SignUpBtn.setOnClickListener(v -> {
            if(isValidSignUpDetails()){
                signUp();
            }
        });
/*        binding.SignUpBtn.setOnClickListener( v ->
                startActivity(new Intent(getApplicationContext(),MainActivity.class)));
 */
    }



    private void showToast(String message)
    {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }

    private void signUp() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String,Object> user = new HashMap<>();
        user.put(Constants.KEY_NAME,binding.UserName.getText().toString());
        user.put(Constants.KEY_EMAIL,binding.etMail.getText().toString());
        user.put(Constants.KEY_PASSWORD,binding.txtPassword.getText().toString());
        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    loading(false);
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
                    preferenceManager.putString(Constants.KEY_USER_ID,documentReference.getId());
                    preferenceManager.putString(Constants.KEY_NAME,binding.UserName.getText().toString());

                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(exception ->{
                    loading(false);
                    showToast(exception.getMessage());
                });
    }

    private Boolean isValidSignUpDetails(){
        if(binding.UserName.getText().toString().trim().isEmpty())
        {
            showToast("Enter Your Name");
            return false;
        }
        else if(binding.etMail.getText().toString().trim().isEmpty()){
            showToast("Enter Email");
            return false;
        }
 //       else if(!Pattern.EMAIL_ADDRESS.matcher(binding.etMail.getText().toString().matches())){
  //          showToast("Enter Valid Email");
   //         return false;
    //    }
        else if(binding.txtPassword.getText().toString().trim().isEmpty()){
            showToast("Enter Password");
            return false;
        }
        else if(binding.txtConfirmPassword.getText().toString().trim().isEmpty()){
            showToast("Enter Confirm Password");
            return false;
        }
        else if(!binding.txtPassword.getText().toString().equals(binding.txtConfirmPassword.getText().toString())){
            showToast("Password and Confirm Password must be same");
            return false;
        }
        else {
            return true;
        }
    }
    private void loading(Boolean isLoading){
        if(isLoading){
            binding.SignUpBtn.setVisibility(View.INVISIBLE);
            binding.progressbar.setVisibility(View.VISIBLE);
        }else{
            binding.progressbar.setVisibility(View.INVISIBLE);
            binding.SignUpBtn.setVisibility(View.VISIBLE);
        }
    }

}
