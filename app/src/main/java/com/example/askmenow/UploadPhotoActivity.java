package com.example.askmenow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.askmeknow.R;

import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UploadPhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_photo);

        // get the photo and show it in the ImageView
        ImageView photo = findViewById(R.id.preview_photo);
        Intent i = getIntent();
        Uri photoUri = Uri.parse(i.getStringExtra("photoURI"));
        Bitmap photoBitmap;
        try {
            // get the photo
            ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),
                    photoUri);
            photoBitmap = ImageDecoder.decodeBitmap(source);
            photo.setImageBitmap(photoBitmap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // get buttons and set up click listeners
        ImageButton returnButton = findViewById(R.id.photo_return);
        Button uploadButton = findViewById(R.id.confirm_upload);

        returnButton.setOnClickListener(view -> this.finish());

        uploadButton.setOnClickListener(view -> {
            this.finish();
        });

    }
}
