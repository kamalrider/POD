package com.kamalrider.pod.scan.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.kamalrider.pod.R;
import com.kamalrider.pod.utils.ImageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class UpdateCNActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAMERA = 0;
    private static final int REQUEST_IMAGE_GALLERY = 1;

    private String imageFilePath;
    private String tempFilePath;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_c_n);

        mImageView = findViewById(R.id.img);
        mImageView.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(UpdateCNActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                    requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAMERA);
                } else
                    openCameraIntent();
            }
        });

    }

    private void openCameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            File tempFile = createImageFile();
            tempFilePath = tempFile.getAbsolutePath();
            Uri photoURI = FileProvider.getUriForFile(UpdateCNActivity.this, UpdateCNActivity.this.getPackageName() + ".provider", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intent, REQUEST_IMAGE_CAMERA);

        }
    }

    private File createImageFile() {
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String imageFileName = "IC.jpg";

        if (!storageDir.exists())
            storageDir.mkdirs();

        File image = new File(storageDir + File.separator + imageFileName);
        return image;
    }

    private void loadImage(ImageView imageView, String imagePath) {
        File imgFile = new File(imagePath);
        if (imgFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView.setImageBitmap(bitmap);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
//            case REQUEST_IMAGE_GALLERY:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    openGalleryIntent();
//                } else {
//                    Toast.makeText(getActivity(), "Gallery access denied", Toast.LENGTH_SHORT).show();
//                }
//                break;
            case REQUEST_IMAGE_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCameraIntent();
                } else {
                    Toast.makeText(this, "Camera access denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                imageFilePath = tempFilePath;
                ImageUtils.normalizeImageForUri(this, Uri.fromFile(new File(imageFilePath)));
                ImageUtils.compressImage(this, imageFilePath, imageFilePath);
                loadImage(mImageView, imageFilePath);
            }
        }
//        else if (requestCode == REQUEST_IMAGE_GALLERY) {
//            if (resultCode == Activity.RESULT_OK) {
//                try {
//                    // Create empty file
//                    File photoFile = createImageFile();
//                    imageFilePath = photoFile.getAbsolutePath();
//
//                    // Copy galley file to empty file
//                    InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());
//                    FileOutputStream fileOutputStream = new FileOutputStream(photoFile);
//                    ImageUtils.copyStream(inputStream, fileOutputStream);
//                    fileOutputStream.close();
//                    inputStream.close();
//
//                    ImageUtils.compressImage(getActivity(), imageFilePath, imageFilePath);
//
//                    loadImage(mImageView, imageFilePath);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        } else if (requestCode == UCrop.REQUEST_CROP) {
//            if (resultCode == Activity.RESULT_OK) {
//                imageFilePath = tempFilePath;
//                loadImage(mImageView, imageFilePath);
//            }
//        } else if (resultCode == UCrop.RESULT_ERROR) {
//            final Throwable cropError = UCrop.getError(data);
//        }
    }
}