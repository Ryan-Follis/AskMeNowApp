package com.example.askmenow.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.askmenow.databinding.ActivitySignUpBinding;
import com.example.askmenow.firebase.DataAccess;
import com.example.askmenow.models.User;
import com.example.askmenow.utilities.Constants;
import com.example.askmenow.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private PreferenceManager preferenceManager;
    private String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
    }

    private void setListeners(){
        binding.textSignIn.setOnClickListener(v -> onBackPressed());
        binding.buttonSignUp.setOnClickListener(v -> {
            if(isValidSignUpDetails()){
                signUp();
            }
        });
        binding.layoutImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            selectImage.launch(intent);
        });
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void signUp(){
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> newUser = new HashMap<>();
        newUser.put(Constants.KEY_NAME, binding.inputName.getText().toString());
        newUser.put(Constants.KEY_EMAIL, binding.inputEmail.getText().toString());
        newUser.put(Constants.KEY_USERNAME, binding.inputUsername.getText().toString());
        newUser.put(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString());
        newUser.put(Constants.KEY_IMAGE, encodedImage);
        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(newUser)
                .addOnSuccessListener(documentReference -> {
                    loading(false);
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                    preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                    preferenceManager.putString(Constants.KEY_NAME, binding.inputName.getText().toString());
                    preferenceManager.putString(Constants.KEY_IMAGE, encodedImage);
                    User user = new User();
                    user.id = documentReference.getId();
                    user.name = binding.inputName.getText().toString();
                    DataAccess.setSelf(user);
                    DataAccess.saveSelf(preferenceManager);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(exception -> {
                    loading(false);
                    showToast(exception.getMessage());
                });
    }

    private String encodeImage(Bitmap bitmap){
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        byte[] bytes = outputStream.toByteArray();
        // not sure if I need .encodeToString(bytes, Base64.DEFAULT) in line below or not
        return Base64.getEncoder().encodeToString(bytes);
    }

    private final ActivityResultLauncher<Intent> selectImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == RESULT_OK){
                    if(result.getData() != null){
                        Uri imageUri = result.getData().getData();
                        try{
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.profileImage.setImageBitmap(bitmap);
                            binding.textAddImage.setVisibility(View.GONE);
                            encodedImage = encodeImage(bitmap);
                        } catch(FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private boolean isValidPassword(String password){
        String passwordCheck = "^" +
                "(?=.*[A-Za-z])" +      // contains at least 1 alphabetic character
                "(?=.*[0-9])" +         // contains at least 1 digit
                //"(?=.*[!@#$%^&+=.?])" + // contains at least 1 special character
                "(?=\\S+$)" +           // contains no whitespace characters
                ".{8,256}" +            // is between 8 and 256 characters long
                "$";
        Pattern pattern = Pattern.compile(passwordCheck, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private boolean isValidSignUpDetails(){
        // checks if the user adds a profile picture
        if(encodedImage == null) {
            showToast("Please select a profile image.");
            return false;
        }
        // checks if the user enters their name
        else if(binding.inputName.getText().toString().trim().isEmpty()){
            showToast("Please enter your name.");
            return false;
        }
        // checks if the user enters an email address
        else if(binding.inputEmail.getText().toString().trim().isEmpty()){
            showToast("Please enter your email address.");
            return false;
        }
        // Uses regular expressions to verify that email address user enters is valid
        else if(!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()){
            showToast("Enter valid email address.");
            return false;
        }
        // checks if the user enters a username
        else if(binding.inputUsername.getText().toString().trim().isEmpty()){
            showToast("Please enter a username.");
            return false;
        }
        // checks if the user enters a password
        else if(binding.inputPassword.getText().toString().trim().isEmpty()){
            showToast("Please enter a password.");
            return false;
        }
        // checks if the password entered is valid
        else if(!isValidPassword(binding.inputPassword.getText().toString())){
            showToast("Password chosen does not fit criteria.");
            return false;
        }
        // checks if the user confirmed their password
        else if(binding.inputConfirmPassword.getText().toString().trim().isEmpty()){
            showToast("Please confirm your password.");
            return false;
        }
        // checks if the password matches the confirmation
        else if(!binding.inputPassword.getText().toString().equals(
                binding.inputConfirmPassword.getText().toString())){
            showToast("Password does not match confirmation.");
            return false;
        }
        return true;
    }

    private void loading(boolean isLoading){
        if (isLoading){
            binding.buttonSignUp.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }
        else{
            binding.buttonSignUp.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }
}