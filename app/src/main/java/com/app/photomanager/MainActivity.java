package com.app.photomanager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.net.URI;
import java.util.ArrayList;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE=100;
    private static final int PERMISSION_CAMERA_REQUEST_CODE=200;
    private ArrayList<String> imgPaths=new ArrayList<>();
    private RecyclerView recyclerview;
    private FloatingActionButton floatingActionButton;
    private final String CAMERA_STRING_CODE="com.app.photomanager.CameraPreview";

    private ProgrammingAdapterClass recyclerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions();
        floatingActionButton=findViewById(R.id.fabimage);

        recyclerview = findViewById(R.id.RecycleView);
        prepareRecyclerView();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(),CAMERA)==PackageManager.PERMISSION_GRANTED){
                  startCameraPreview();
                }
                else
                    cameraPermissionRequest();

            }
        });

        //recyclerView.setAdapter(new ProgrammingAdapterClass(data));
    }

    private void startCameraPreview() {
        Intent intent=new Intent(getApplicationContext(),CameraActivity.class);
        startActivity(intent);
    }

    private void cameraPermissionRequest() {
        ActivityCompat.requestPermissions(this,new String[]{CAMERA},PERMISSION_CAMERA_REQUEST_CODE);
    }

    //check if the permissions are already granted at runtime
    private boolean checkPermissions(){
        int result= ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);

        return result==PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {

        //To check if permissions are granted and if the function returns true
        if(checkPermissions()){
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            getImagePath();
        }
        else{
            //if permissions are not granted we are calling a methd to request permissions
            requestUserPermission();
        }
    }

    private void requestUserPermission() {
        ActivityCompat.requestPermissions(this,new String[]{READ_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);

    }


//    @RequiresApi(api = Build.VERSION_CODES.Q)

    private void getImagePath() {

        //in this method we add the location data in strings of our image paths in the String list created
            Uri uri;
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
                uri=MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
            }
            else{
            uri=MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            }

            final String[] columns={MediaStore.Images.Media.DATA,MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
            final String order=MediaStore.Images.Media.DATE_TAKEN;

            Cursor cursor=getApplicationContext().getContentResolver().query(uri,columns,null,null,order+"  DESC");

            //int count=cursor.getCount();

        int dataColumnIndex=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        while (cursor.moveToNext()){

            String absolutePath=cursor.getString(dataColumnIndex);

            //URI contenturi= ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,)
            imgPaths.add(absolutePath);
        }




            cursor.close();


    }

    private void prepareRecyclerView() {
        recyclerViewAdapter=new ProgrammingAdapterClass(this,imgPaths);
        recyclerview.setLayoutManager(new GridLayoutManager(this,4));
        recyclerview.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_REQUEST_CODE:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    getImagePath();
                }
                else
                    Toast.makeText(this, "Permission is required to run the app", Toast.LENGTH_SHORT).show();

                break;

            case PERMISSION_CAMERA_REQUEST_CODE:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    startCameraPreview();
                }
                else
                    Toast.makeText(this, "Permission is required to run the camera", Toast.LENGTH_SHORT).show();

                break;

        }
    }


}