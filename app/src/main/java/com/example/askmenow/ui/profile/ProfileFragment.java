package com.example.askmenow.ui.profile;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.askmenow.R;
import com.example.askmenow.activities.MainActivity;
import com.example.askmenow.activities.SignInActivity;
import com.example.askmenow.databinding.FragmentProfileBinding;
import com.example.askmenow.utilities.Constants;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Base64;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private static String[]  data = new String[6];
    private static int visibility = -1;
    String[] dropdownMenu = {"Everyone", "Friends Only", "Only Me"};
    AutoCompleteTextView auto;
    ArrayAdapter<String> adapter;
    private AlertDialog.Builder dBuilder;
    private AlertDialog changePass;
    private EditText newcontactpopup_old, newcontactpopup_new;
    private Button newcontactpopup_cancel, newcontactpopup_submit;
    private ImageView img;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        pullProfile();
        ProfileViewModel homeViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //listener for updating the Visibility Settings
        auto.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getActivity().getApplicationContext(), item,Toast.LENGTH_SHORT).show();
                visibility = i;
            }
        });
        //preference manager

        //Listener for log out feature
        Button logout = root.findViewById(R.id.signout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        //Listener for change password
        Button changePassword = root.findViewById(R.id.changepassword);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //listener for pictures
        img = root.findViewById(R.id.pictures);

        img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                return false;
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public int push(String field){
        return 0;
    }

    public int pullProfile(){
        Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
        String user_username = intent.getStringExtra("username");
        String age = intent.getStringExtra("age");
        TextView fullName = getView().findViewById(R.id.usernameField);
        fullName.setText(user_username);
        TextView ageView = getView().findViewById(R.id.ageField);
        ageView.setText(age);
        visibility = intent.getIntExtra("visibility",1);

        return 0;
    }

    public int logout(){
        for(int i=0;i<data.length;i++){
            data[i] = null;
        }
        visibility = -1;
        return 0;
    }

    public void createNewContactDialog(){
        dBuilder = new AlertDialog.Builder(getActivity().getApplicationContext());
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup,null);
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

    private void storePictures(){
        int total = 0;
        while(total<=10){
             final ActivityResultLauncher<Intent> selectImage = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if(result.getResultCode() == RESULT_OK){
                            if(result.getData() != null){
                                Uri imageUri = result.getData().getData();
                                try{
                                    InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                    binding.pictures.setImageBitmap(bitmap);
                                    //binding.textAddImage.setVisibility(View.GONE);
                                    //encodedImage = encodeImage(bitmap);
                                } catch(FileNotFoundException e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
            );
        }
    }
}
