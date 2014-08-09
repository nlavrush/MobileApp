package com.eccentex.dcm.MobileApp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Nikolay on 02.08.2014.
 */
public class CaseDetailActivity extends Activity {
	private String mCurrentPhotoPath;
	private String mCurrentPhotoName;
	private static final int REQUEST_TAKE_PHOTO = 1;
	private static final String TAG = "CaseDetail";
	private static final String PHOTO_FOLDER = "MobileApp";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.case_detail);
		Intent intent = getIntent();
		String title = "";//intent.getStringExtra(MainActivity.CASE_TITLE);
		String description = "";// intent.getStringExtra(MainActivity.CASE_TEXT);

		TextView titleView  = (TextView) findViewById(R.id.caseTitle);
		titleView.setText(title);

		TextView textView = (TextView)findViewById(R.id.caseText);
		textView.setTextSize(20);
		textView.setText(description);

	}
	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "CASE_" + timeStamp+"_dcm";
		File storageDir = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES);
		File myDir = new File(storageDir.getAbsolutePath()+"/"+PHOTO_FOLDER);
		myDir.mkdir();
		File image = File.createTempFile(
				imageFileName,  /* prefix */
				".jpg",         /* suffix */
				myDir      /* directory */
		);

		// Save a file: path for use with ACTION_VIEW intents
		mCurrentPhotoPath = "file:" + image.getAbsolutePath();
		mCurrentPhotoName =  image.getAbsolutePath();
		Log.i(TAG,mCurrentPhotoPath);
		return image;
	}
	public  void attachFile(View view){

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
			ImageView mImageView = (ImageView) findViewById(R.id.caseImageView);
			mImageView.setImageURI(Uri.fromFile(new File(mCurrentPhotoName)));
			galleryAddPic();
		}
		else{
			Toast.makeText(getApplicationContext(), "Couldn't take a picture!", Toast.LENGTH_LONG).show();
		}
	}
	private void galleryAddPic() {
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(mCurrentPhotoName);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		this.sendBroadcast(mediaScanIntent);
	}
	public void takePicture (View view){
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			// Create the File where the photo should go
			File photoFile = null;
			try {
				photoFile = createImageFile();
				Log.i(TAG,photoFile.getAbsolutePath());
			} catch (IOException ex) {
				// Error occurred while creating the File
				Log.e(TAG, "Error with picture!");
			}
			// Continue only if the File was successfully created
			if (photoFile != null) {
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(photoFile));
				startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
			}
		}
	}
}
