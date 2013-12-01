package com.example.imagegeotag;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	int TAKE_PHOTO_CODE = 0;
	public static int count=0;
	String exportFileLocation = "SnapCam";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    
	    try {
			File exportDir = new File(Environment.getExternalStorageDirectory(), "/" + exportFileLocation);
			final File phoneDir = new File(Environment.getExternalStorageDirectory(), "/" + exportFileLocation + "/" + android.os.Build.MODEL);
			if (!exportDir.exists()) {
				exportDir.mkdirs();
				if (!phoneDir.exists()) {
					phoneDir.mkdirs();
				}
			} else if (!phoneDir.exists()) {
				phoneDir.mkdirs();
			}
			
			  Button capture = (Button) findViewById(R.id.btnCapture);
		        capture.setOnClickListener(new View.OnClickListener() {
		        public void onClick(View v) {
		        	
		        	//1.jpg, 2.jpg i td..
		            count++;
		            String file = count+".jpg";
		            File newfile = new File(phoneDir, file);
		            try {
		                newfile.createNewFile();
		            } catch (IOException e) {
		            	
		            }       
		
		            Uri outputFileUri = Uri.fromFile(newfile);
		
		            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
		            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		
		            startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);

		        }
		    });
		        
		} catch (NullPointerException ex) {
			Toast.makeText(getApplicationContext(), "Name cannot be empty.", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	      
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	
	    if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
	        Log.d("CameraDemo", "Pic saved");
	
	    }
	}

}
