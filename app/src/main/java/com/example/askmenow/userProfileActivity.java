package com.example.askmenow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class userProfileActivity extends AppCompatActivity {
    private static String[]  data = new String[6];
    private static int visibility = -1;
    String[] dropdownMenu = {"Everyone", "Friends Only", "Only Me"};
    AutoCompleteTextView auto;
    ArrayAdapter<String> adapter;
    private AlertDialog.Builder dBuilder;
    private AlertDialog changePass;
    private EditText newcontactpopup_old, newcontactpopup_new;
    private Button newcontactpopup_cancel, newcontactpopup_submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        auto = findViewById(R.id.autodrop);
        adapter = new ArrayAdapter<String>(this,R.layout.list, dropdownMenu);
        auto.setAdapter(adapter);

        //listener for updating the Visibility Settings
        auto.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(userProfileActivity.this, item,Toast.LENGTH_SHORT).show();
                visibility = i;
            }
        });

        //Listener for log out feature
        Button logout = findViewById(R.id.signout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        //Listener for change password
        Button changePassword = findViewById(R.id.changepassword);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    public int push(String field){
        return 0;
    }

    public int pullProfile(String[] profileInfo){
        boolean check = true;
        for (int i = 0; i<data.length; i++){
            data[i] = profileInfo[i];
            if(data[i] == null){
                check = false;
            }
        }
        if(check){
            return 0;
        }
        else{
            return 1;
        }
    }

    public int logout(){
        for(int i=0;i<data.length;i++){
            data[i] = null;
        }
        visibility = -1;
        return 0;
    }

    public void createNewContactDialog(){
        dBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup,null);
    }
}