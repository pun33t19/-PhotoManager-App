package com.app.photomanager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {
private ImageButton imageButton;
private ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture;
private PreviewView camerapreview;
ImageCapture imageCapture;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        imageButton=findViewById(R.id.capturedimage);
        camerapreview=findViewById(R.id.camerapreview);
        imageButton.setOnClickListener(this);
        cameraProviderListenableFuture=ProcessCameraProvider.getInstance(this);
        cameraProviderListenableFuture.addListener(()->{
            try {
                ProcessCameraProvider cameraProvider=cameraProviderListenableFuture.get();
        bindPreview(cameraProvider);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }, ContextCompat.getMainExecutor(this));

    }

    private void bindPreview(ProcessCameraProvider cameraProvider) {
        Preview preview=new Preview.Builder()
                .build();
        CameraSelector cameraSelector=new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

       imageCapture=new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)

                .build();

        preview.setSurfaceProvider(camerapreview.getSurfaceProvider());



        Camera camera=cameraProvider.bindToLifecycle(this,cameraSelector,preview,imageCapture);
        CameraControl cameraControl=camera.getCameraControl();
    }



    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onClick(View v) {

        File photoDir=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/CameraX");
        if (!photoDir.exists()){
            photoDir.mkdir();
        }
        Date date=new Date();
        String timestamp=String.valueOf(date.getTime());
        String photoFilePath=photoDir.getAbsolutePath()+"/"+timestamp+".jpg";
        File photoFile=new File(photoFilePath);



        ImageCapture.OutputFileOptions outputFileOptions=new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        imageCapture.takePicture(outputFileOptions,getMainExecutor(),new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull @NotNull ImageCapture.OutputFileResults outputFileResults) {
                Toast.makeText(CameraActivity.this, "Photo saved", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@NonNull @NotNull ImageCaptureException exception) {
                Toast.makeText(CameraActivity.this, "Error saving the photo"+exception, Toast.LENGTH_SHORT).show();
            }
        });
    }
}