package com.sanju.developer.inventrom;

/**
 * Created by Sanju on 11-Aug-17.
 */

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CamActivity extends AppCompatActivity {
    private CameraView mImageSurfaceView;
    private Camera camera;

    private FrameLayout cameraPreviewLayout;
    private ImageView capturedImageHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camactivity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        cameraPreviewLayout = (FrameLayout) findViewById(R.id.camera_preview);//connecting the FrameLayout
        capturedImageHolder = (ImageView) findViewById(R.id.captured_image); //connecting the ImageView

        camera = checkDeviceCamera();
        mImageSurfaceView = new CameraView(CamActivity.this, camera); //received on the screen
        cameraPreviewLayout.addView(mImageSurfaceView);

        Button captureButton = (Button) findViewById(R.id.button);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, pictureCallback);
            }
        });
    }

    private Camera checkDeviceCamera() {
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mCamera;
    }

    PictureCallback pictureCallback = new PictureCallback()
    {
        @Override
        public void onPictureTaken(byte[] data, Camera camera)
        {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            if (bitmap == null)
            {
                Toast.makeText(CamActivity.this, "Captured image is empty", Toast.LENGTH_LONG).show(); //Failed to capture image
                return;
            }
            else
            {
                Toast.makeText(CamActivity.this, "Casting Image", Toast.LENGTH_LONG).show();
            }
            //Casting the bitmap image captured to the imageview
            // Continue only if the File was successfully created
            saveImage(bitmap);
            capturedImageHolder.setImageBitmap(scaleDownBitmapImage(bitmap, 300, 200));
        }

    };

    //Resizing the b3itmap image
    private Bitmap scaleDownBitmapImage(Bitmap bitmap, int newWidth, int newHeight)
    {
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true); //Scaling according to the given width and height
        return resizedBitmap;
    }
    public void saveImage(Bitmap bitmap)
    {
        File myDir = new File( Environment.getExternalStorageDirectory(),File.separator+"Inventrom");//Folder name where I want to save.
        Log.v("File path",": "+myDir);
        myDir.mkdirs();

        String time=new SimpleDateFormat("yyyymmdd_HHmmss").format(new Date());
        String fname = time + "-.jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file); //from here it goes to catch block
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            String[] paths = {file.toString()};
            String[] mimeTypes = {"/image/jpeg"};
            MediaScannerConnection.scanFile(this, paths, mimeTypes, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
