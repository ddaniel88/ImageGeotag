package com.example.imagegeotag;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.location.Location;
import android.media.ExifInterface;
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
		            
		            ExifInterface exif = ExtIf(newfile);
		
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
	
	public ExifInterface ExtIf(File imgFile) {
		try {
			ExifInterface exif = new ExifInterface(imgFile.getCanonicalPath());
			//String latitudeStr = "90/1,12/1,30/1";
			double lat = 43.2154;
			double alat = Math.abs(lat);
			String dms = Location.convert(alat, Location.FORMAT_SECONDS);
			String[] splits = dms.split(":");
			String[] secnds = (splits[2]).split("\\.");
			String seconds;
			if(secnds.length==0)
			{
			    seconds = splits[2];
			}
			else
			{
			    seconds = secnds[0];
			}

			String latitudeStr = splits[0] + "/1," + splits[1] + "/1," + seconds + "/1";
			exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, latitudeStr);

			exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, lat>0?"N":"S");

			double lon = 21.125;
			double alon = Math.abs(lon);


			dms = Location.convert(alon, Location.FORMAT_SECONDS);
			splits = dms.split(":");
			secnds = (splits[2]).split("\\.");

			if(secnds.length==0)
			{
			    seconds = splits[2];
			}
			else
			{
			    seconds = secnds[0];
			}
			String longitudeStr = splits[0] + "/1," + splits[1] + "/1," + seconds + "/1";


			exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, longitudeStr);
			exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, lon>0?"E":"W");

			exif.saveAttributes();
			return exif;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

}
