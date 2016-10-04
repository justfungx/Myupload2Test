package tw.org.iii.myuploadtest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import java.util.List;

public class MycameraActivity extends AppCompatActivity {
    private Camera camera;
    private FrameLayout frame;
    private CameraPreview preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycamra);

        checkPermission(Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int num = Camera.getNumberOfCameras();
        Log.v("brad", "camera:" + num);

        camera = Camera.open();

        Camera.Parameters params =  camera.getParameters();

        List<Camera.Size> sizes = params.getSupportedPictureSizes();
        for (Camera.Size size :sizes){
            Log.v("brad", size.width + "x" +size.height);
        }

        params.setPictureSize(1280,720);
        camera.setParameters(params);


        //camera.takePicture(new MyShutter(),null,new MyJpegCallback());

        //camera.release();

        frame = (FrameLayout)findViewById(R.id.activity_my_camera);
        frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(new MyShutter(),null,new MyJpegCallback());
            }
        });

        preview = new CameraPreview(this, camera);
        frame.addView(preview);




    }
    private class MyShutter implements Camera.ShutterCallback {
        @Override
        public void onShutter() {
            Log.v("DK", "onShutter");
        }
    }

    private class MyJpegCallback implements Camera.PictureCallback {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Intent it = new Intent();
            it.putExtra("pic", data);
            setResult(1,it);
            finish();
        }
    }

    private int checkCameraNumber(){
//        PackageManager packageManager = getPackageManager();
//        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)){
//
//        }
        return Camera.getNumberOfCameras();


    }

    private void checkPermission(String... permissions){
        for (String permission : permissions){
            if (ContextCompat.checkSelfPermission(this,
                    permission) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                        new String[]{permission},1);
            }
        }
    }

}