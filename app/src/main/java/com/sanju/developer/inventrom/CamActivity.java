package com.sanju.developer.inventrom;

/**
 * Created by Sanju on 11-Aug-17.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CamActivity extends AppCompatActivity {


    private FrameLayout catching;
    private CameraView surfaceView;
    private Camera camera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camactivity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        catching = (FrameLayout) findViewById(R.id.catchimg);//connect with the Layout


        camera = checkDeviceCamera();

        surfaceView = new CameraView(CamActivity.this, camera); //received on the screen

        catching.addView(surfaceView);

            Button captureButton = (Button) findViewById(R.id.button);
            captureButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    camera.takePicture(null, null, pictureCallback);        //call pictureCallback
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
            final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);              //decoding Byte Array
            if (bitmap == null)
            {
                Toast.makeText(CamActivity.this, "NULL image ", Toast.LENGTH_LONG).show(); //when  image is NULL;
                return;
            }
            else
            {
                Toast.makeText(CamActivity.this, "Loading Image", Toast.LENGTH_LONG).show();//loading image
            }

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    CamActivity.this);

           // alertDialog.setTitle("Enter your Detail's ");
           // DialogViewDecorator.decorate(alertDialog, android.R.color.holo_red_light);
            alertDialog.setTitle(Html.fromHtml("<font color='#00ff37'>Enter your Detail's.</font>"));

            final EditText name = new EditText(CamActivity.this);
            final EditText age = new EditText(CamActivity.this);
            final EditText address = new EditText(CamActivity.this);
            final EditText gender = new EditText(CamActivity.this);
            name.setHint("NAME");
            age.setHint("Age");
            address.setHint("Address");
            gender.setHint("Gender");
            age.setInputType(InputType.TYPE_CLASS_NUMBER);
            gender.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);

            LinearLayout ll=new LinearLayout(getApplicationContext());          //saving the detail in Linear layout
            ll.setOrientation(LinearLayout.VERTICAL);
            ll.addView(name);
            ll.addView(age);
            ll.addView(address);
            ll.addView(gender);
            alertDialog.setView(ll);
            alertDialog.setIcon(R.drawable.bolt);

            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("Update",  new DialogInterface.OnClickListener() {        //click on update Button
                public void onClick(DialogInterface dialog, int id) {

                    File root=null;
                    try {
                        // <span id="IL_AD8" class="IL_AD">check for</span> SDcard
                        root = Environment.getExternalStorageDirectory();
                        Log.i("path","path.." +root.getAbsolutePath());         //testing for path

                        //check sdcard permission
                        if (root.canWrite()){
                            File fileDir = new File(root.getAbsolutePath()+"/Documents");   //find AbsolutePath
                            fileDir.mkdirs();

                            File file= new File(fileDir, name.getText().toString()+".txt");     // save file with .txt extention
                            FileWriter filewriter = new FileWriter(file);
                            BufferedWriter out = new BufferedWriter(filewriter);
                            out.write(name.getText().toString());               // saving name
                            out.newLine();
                            out.write(age.getText().toString());                // saving age
                            out.newLine();
                            out.write(address.getText().toString());            // saving address
                            out.newLine();
                            out.write(gender.getText().toString());             // saving gender
                            out.newLine();

                            out.close();
                            saveImage(bitmap,name.getText().toString());           //call function saveImage()
                                                                                    //pass argument bitmap and name
                        }
                    } catch (IOException e) {
                        Log.e("ERROR:---", "Could not write file to SDCard" + e.getMessage());

                    }

                    Intent intent=new Intent(CamActivity.this,Graph.class);      //In the end go back to Minactivity
                    startActivity(intent);
                    //ACTION
                }
            });
            alertDialog.setNegativeButton("Cancel",  new DialogInterface.OnClickListener() {    //onclick cancel
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent=new Intent(CamActivity.this,MainActivity.class);              //go back to Minactivity
                    startActivity(intent);
                    //ACTION
                }
            });

            AlertDialog alert = alertDialog.create();
            alert.show();                                                                   //dialog show









        }

    };

   // Resize of IMAGE
    private Bitmap scaleDownBitmapImage(Bitmap bitmap, int newWidth, int newHeight)
    {
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true); //Scaling according to the given newWidth and newHeight
        return resizedBitmap;
    }
    public void saveImage(Bitmap bitmap,String name)
    {
        File myDir = new File( Environment.getExternalStorageDirectory(),File.separator+"Inventrom");//Folder name to save.
        Log.v("File path",": "+myDir);
        myDir.mkdirs();

        String time=new SimpleDateFormat("yyyymmdd_HHmmss").format(new Date()); // Format for Saving The Image
        String fname =name+" "+time + ".jpeg";                                  //extend with ".jpeg"
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();


        try {
            FileOutputStream out = new FileOutputStream(file); //go to catch block for exception Handling
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
