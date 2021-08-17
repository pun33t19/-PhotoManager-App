package com.app.photomanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {
private ImageButton imageButton;
private ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture;
private PreviewView camerapreview;

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

        preview.setSurfaceProvider(camerapreview.getSurfaceProvider());



        Camera camera=cameraProvider.bindToLifecycle(this,cameraSelector,preview);
        CameraControl cameraControl=camera.getCameraControl();
    }


    @Override
    public void onClick(View v) {

    }
}