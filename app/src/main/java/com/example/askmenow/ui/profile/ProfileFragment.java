package com.example.askmenow.ui.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.askmenow.R;
import com.example.askmenow.activities.MainActivity;
import com.example.askmenow.databinding.FragmentProfileBinding;
import java.io.ByteArrayOutputStream;
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
                changePassword();
            }
        });

        //Listener for forgot password button
        Button forgotPassword = root.findViewById(R.id.forgot);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPassword();
            }
        });

        //listener for delete profile button
        Button deleteProfile = root.findViewById(R.id.delete);
        deleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProfile();
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

    public int forgotPassword(){
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

    public int changePassword(){
        dBuilder.setTitle("Change Password").setMessage("Change your password?");
        final EditText old = new EditText(getActivity().getApplicationContext());
        old.setHint("Old Password");
        final EditText newPassword = new EditText(getActivity().getApplicationContext());
        newPassword.setHint("New Password");
        dBuilder.setView(old);
        dBuilder.setView(newPassword);
        dBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        return 0;
    }

    public int logout(){
        for(int i=0;i<data.length;i++){
            data[i] = null;
        }
        visibility = -1;
        return 0;
    }

    public int deleteProfile(){
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



}
