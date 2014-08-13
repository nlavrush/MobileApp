package com.eccentex.dcm.MobileApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.eccentex.dcm.MobileApp.Authentication.CreateResourceTask;
import com.eccentex.dcm.MobileApp.Authentication.ProgressiveEntityListener;
import org.apache.http.entity.FileEntity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Nikolay on 02.08.2014.
 */
public class StateDetailActivity extends Activity
		implements ProgressiveEntityListener{

	private String mToken;
	private String mCurrentPhotoPath;
	private String mCurrentPhotoName;
	private String mCurrentPhotoName2;
	private static final int REQUEST_TAKE_PHOTO = 1;
	private static final int REQUEST_PICK_PHOTO = 2;
	private static final String PHOTO_FOLDER = "MobileApp";

	private final  String TAG  = "StateDetail";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.state_detail);
		Intent intent = getIntent();
		if(intent != null) {

			TextView txt = (TextView)findViewById(R.id.textViewId);
			txt.setText(intent.getStringExtra("id"));
			txt = (TextView)findViewById(R.id.textViewName);
			txt.setText(intent.getStringExtra("name"));
			txt = (TextView)findViewById(R.id.textViewAbr);
			txt.setText(intent.getStringExtra("abbr"));
			mToken = intent.getStringExtra("token");

		}

	}
	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "STATE_" + timeStamp;
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
		mCurrentPhotoName2 = image.getName();
		mCurrentPhotoName =  image.getAbsolutePath();
		Log.i(TAG,mCurrentPhotoPath);
		return image;
	}
	public  void attachFile(View view){
		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, REQUEST_PICK_PHOTO);
	}
	private void setPic() {

		/* There isn't enough memory to open up more than a couple camera photos */
        /* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
		ImageView mImageView = (ImageView)findViewById(R.id.imageView);
		int targetW = mImageView.getWidth();
		int targetH = mImageView.getHeight();

		/* Get the size of the image */
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(mCurrentPhotoName, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
		int scaleFactor = 1;
		if ((targetW > 0) || (targetH > 0)) {
			scaleFactor = Math.min(photoW / targetW, photoH / targetH);
		}

		/* Set bitmap options to scale the image decode target */
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
		Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoName, bmOptions);

		/* Associate the Bitmap to the ImageView */
		mImageView.setImageBitmap(bitmap);
		mImageView.setVisibility(View.VISIBLE);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
			//ImageView mImageView = (ImageView) findViewById(R.id.imageView);
			//mImageView.setImageURI(Uri.fromFile(new File(mCurrentPhotoName)));

			galleryAddPic();
			setPic();
		}
		else if(requestCode == REQUEST_PICK_PHOTO && resultCode == RESULT_OK){
			Uri selectedImageUri = data.getData();

			Log.e(TAG, selectedImageUri.getPath());
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

	@Override
	public void onFileUpload(String response, String fileName, FileEntity fileEntity, String responseId) {
		Log.d(TAG,"OK");
		AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
		dlgAlert.setTitle("Result");
		String message ="";
		if(response.contains("\"ErrorCode\":0"))
			message ="File was successfully uploaded!";
		else
			message = response;
		dlgAlert.setMessage(message);
		dlgAlert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				//finish();
			}
		});
		dlgAlert.setCancelable(false);
		dlgAlert.create().show();
	}
	public void uploadFile(View view){
		final String token = mToken;
		String cmsBaseUrl = Config.URL_PREFIX+Config.DEFAULT_HOST+":"+Config.DEFAULT_PORT+"/"+Config.DEFAULT_CSM_URL_BODY;
		final String domain = Config.DEFAULT_DOMAIN;
		final String responseId = "1";
		final String filePath = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+PHOTO_FOLDER;
		final String fileName = mCurrentPhotoName2;
		String url = cmsBaseUrl + "/" + domain;
		String query = String.format("%s=%s&%s=%s", "t", token, "u", "/" + fileName);
		url += "?" + query;
		Log.e(TAG,url);
		final File file = new File(filePath+"/"+fileName);
		final FileEntity fileEntity = new FileEntity(file, "image/jpg");
		CreateResourceTask putTask = new CreateResourceTask(this, fileEntity, fileName, url, responseId);
		putTask.execute();
	}
}
