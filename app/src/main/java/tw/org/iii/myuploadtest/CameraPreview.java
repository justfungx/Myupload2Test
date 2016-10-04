package tw.org.iii.myuploadtest;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

import java.io.IOException;

/**
 * Created by user on 2016/10/4.
 */

public class CameraPreview extends SurfaceView implements  SurfaceHolder.Callback{
    private Camera mCamera;
    private SurfaceHolder holder;


    public CameraPreview(Context context, Camera camera) {
        super(context);

        mCamera = camera;

        holder = getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (holder.getSurface() == null) return;

        try {
            mCamera.stopPreview();
        }catch (Exception e){

        }
        //
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.release();
        mCamera = null;
    }
}