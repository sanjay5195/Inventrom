package com.sanju.developer.inventrom;

/**
 * Created by Sanju on 11-Aug-17.
 */
import android.content.Context;
import android.hardware.Camera ;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.io.IOException;




public class CameraView extends SurfaceView implements SurfaceHolder.Callback{         // imlements SurfaceHolder.callback
    private SurfaceHolder hold;
    private Camera cam;
    public CameraView(Context context, Camera camera){
        super(context);

        cam = camera;
        cam.setDisplayOrientation(90);
        hold = getHolder();                                                       // we can get camera data here
        hold.addCallback(this);
        hold.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try{
                                                                         //when the surface is created, set the camera to draw images in this surface
            cam.setPreviewDisplay(surfaceHolder);
            cam.startPreview();
        } catch (IOException e) {
            Log.d("ERROR", "Camera error on surfaceCreated " + e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i1, int i2, int i3) {
        if(hold.getSurface() == null)                                    // surface is ready to receive camera data
            return;

        try{
            cam.stopPreview();
        } catch (Exception e){
                                                                         // when camera  it's not running
        }

                                                                         //now, recreate the camera preview
        try{
            cam.setPreviewDisplay(hold);
            cam.startPreview();
        } catch (IOException e) {
            Log.d("ERROR", "Camera error on surfaceChanged " + e.getMessage());         //Testing
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        cam.stopPreview();
        cam.release();
    }
}


