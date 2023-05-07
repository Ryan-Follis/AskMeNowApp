package com.example.askmenow.ui.personal_profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.askmenow.R;
import com.example.askmenow.activities.MainActivity;
import com.example.askmenow.activities.SignInActivity;
import com.example.askmenow.adapters.ProfileAdapter;
import com.example.askmenow.databinding.FragmentPersonalProfileBinding;
import com.example.askmenow.firebase.DataAccess;
import com.example.askmenow.firebase.RememberListOperations;
import com.example.askmenow.listeners.DataAccessListener;
import com.example.askmenow.models.User;
import com.example.askmenow.utilities.Constants;
import com.example.askmenow.utilities.PreferenceManager;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PersonalProfileFragment extends Fragment {
    private FragmentPersonalProfileBinding binding;
    private PreferenceManager preferenceManager;
    private static String[] data = new String[6];
    private static int visibility = -1;
    String[] dropdownMenu = {"Everyone", "Friends Only", "Only Me"};
    AutoCompleteTextView auto;
    private final DataAccess da = new DataAccess();
    private AlertDialog.Builder dBuilder;
    private static List<String> rememberList;
    static int GET_FROM_GALLERY = 1;
    ImageView img;
    int[] images = new int[10];

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // pullProfile();
        binding = FragmentPersonalProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        preferenceManager = new PreferenceManager(getActivity().getApplicationContext());

        //listener for updating the Visibility Settings
         /*auto.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getActivity().getApplicationContext(), item,Toast.LENGTH_SHORT).show();
                visibility = i;
            }
        });*/

        //Listener for log out feature
        Button logout = root.findViewById(R.id.signout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dBuilder = new AlertDialog.Builder(view.getContext());
                logout();
            }
        });

        //Listener for change password
        Button changePassword = root.findViewById(R.id.changepassword);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dBuilder = new AlertDialog.Builder(view.getContext());
                changePassword();
            }
        });

        //Listener for forgot password button
        Button forgotPassword = root.findViewById(R.id.forgot);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dBuilder = new AlertDialog.Builder(view.getContext());
                forgotPassword();
            }
        });

        //listener for delete profile button
        Button deleteProfile = root.findViewById(R.id.delete);
        deleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dBuilder = new AlertDialog.Builder(view.getContext());
                deleteProfile();
            }
        });

        Button addPic = root.findViewById(R.id.addpic);
        addPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), GET_FROM_GALLERY);
                onActivityResult(GET_FROM_GALLERY,1,intent);
            }
        });

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        EditText nameIn = root.findViewById(R.id.usernameField);
        EditText ageIn = root.findViewById(R.id.ageField);
        //nameIn.setText(database.collection(Constants.KEY_NAME).toString());
        nameIn.setText(preferenceManager.getString(Constants.KEY_NAME));
        ageIn.setText(database.collection(Constants.KEY_AGE).toString());

        da.getAllUser(params -> {
            List<User> users = (List<User>) params[0];
            if (users.size() == 0) {
                Toast.makeText(getActivity(), "no user found", Toast.LENGTH_SHORT).show();
            }
//            ProfileAdapter profileAdapter = new ProfileAdapter(this.getActivity(), users, self);
            ProfileAdapter profileAdapter = new ProfileAdapter(this.getActivity(), users);


            // get remember list
            RememberListOperations.getRememberList(params1 -> {
                List<User> resultList = (List<User>) params1[0];
                rememberList = new ArrayList<>();
                for (User user : resultList)
                    rememberList.add(user.id);
            });
        });
        return root;

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public int push(String field) {
        return 0;
    }

    public int pullProfile() {
        Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
        String user_username = intent.getStringExtra("username");
        String age = intent.getStringExtra("age");
        TextView fullName = getView().findViewById(R.id.usernameField);
        fullName.setText(user_username);
        TextView ageView = getView().findViewById(R.id.ageField);
        ageView.setText(age);
        visibility = intent.getIntExtra("visibility", 1);
        return 0;
    }

    public int forgotPassword() {
        dBuilder.setTitle("Forgot Your Password?").setMessage("Do you want to go to the password recovery page?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://www.youtube.com/watch?v=o-YBDTqX_ZU"));
                        startActivity(browserIntent);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        dBuilder.show();
        return 0;
    }

    public int changePassword() {
        dBuilder.setTitle("Change Password").setMessage("Do you want to change your password?");
        LinearLayout lila1 = new LinearLayout(getActivity().getApplicationContext());
        lila1.setOrientation(LinearLayout.VERTICAL);
        final EditText old = new EditText(getActivity().getApplicationContext());
        old.setHint("Old Password");
        final EditText newPassword = new EditText(getActivity().getApplicationContext());
        newPassword.setHint("New Password");
        lila1.addView(old);
        lila1.addView(newPassword);
        dBuilder.setView(lila1);
        dBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                /*
                 * find out how to delete record of thing in firebase
                 * */
                FirebaseFirestore database = FirebaseFirestore.getInstance();
                database.collection(Constants.KEY_COLLECTION_USERS)
                        .whereEqualTo(Constants.KEY_PASSWORD, old.getText().toString())
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && task.getResult() != null
                                    && task.getResult().getDocuments().size() > 0) {
                                DataAccess da = new DataAccess();
                                if (isValidPassword(newPassword.getText().toString())) {
                                    DocumentReference documentReference =
                                            database.collection(Constants.KEY_COLLECTION_USERS).document(
                                                    preferenceManager.getString(Constants.KEY_USER_ID)
                                            );
                                    documentReference.update(Constants.KEY_PASSWORD, newPassword.getText().toString())
                                            .addOnFailureListener(e -> showToast("Unable to update password."));
                                    showToast("Password successfully changed.");
                                } else {
                                    showToast("Password chosen does not fit criteria.");
                                }
                            } else {
                                showToast("Username or password is incorrect.");
                            }
                        });
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        dBuilder.show();
        return 0;
    }

    public int logout() {
        dBuilder.setTitle("Logout?")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PreferenceManager preferenceManager = new PreferenceManager(getActivity().getApplicationContext());
                        SharedPreferences.Editor editor = preferenceManager.sharedPreferences.edit();
                        editor.remove(Constants.KEY_IS_SIGNED_IN);
                        editor.commit();
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
                                })
                                .addOnFailureListener(e -> showToast("Unable to sign out."));
                        Intent switchActivityIntent = new Intent(getActivity(), SignInActivity.class);
                        startActivity(switchActivityIntent);
                        getActivity().finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        dBuilder.show();
        return 0;
    }

    public int deleteProfile() {
        dBuilder.setTitle("Delete Profile?")
                .setMessage("Are you sure you want to delete your profile?\nYour data can not be recovered.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PreferenceManager preferenceManager = new PreferenceManager(getActivity().getApplicationContext());
                        SharedPreferences.Editor editor = preferenceManager.sharedPreferences.edit();
                        editor.remove(Constants.KEY_IS_SIGNED_IN);
                        editor.commit();
                        FirebaseFirestore database = FirebaseFirestore.getInstance();
                        database.collection(Constants.KEY_COLLECTION_USERS).document(
                                preferenceManager.getString(Constants.KEY_USER_ID)).delete();
                        Intent switchActivityIntent = new Intent(getActivity(), SignInActivity.class);
                        startActivity(switchActivityIntent);
                        getActivity().finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        dBuilder.show();
        return 0;
    }

    public void createNewContactDialog() {
        dBuilder = new AlertDialog.Builder(getActivity().getApplicationContext());
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup, null);
    }

    private String encodeImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        byte[] bytes = outputStream.toByteArray();
        // not sure if I need .encodeToString(bytes, Base64.DEFAULT) in line below or not
        return Base64.getEncoder().encodeToString(bytes);
    }

    private void showToast(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private boolean isValidPassword(String password) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                img.setImageBitmap(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}