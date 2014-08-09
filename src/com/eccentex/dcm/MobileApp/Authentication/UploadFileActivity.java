package com.eccentex.dcm.MobileApp.Authentication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import com.eccentex.dcm.MobileApp.R;
import org.apache.http.entity.FileEntity;

import java.io.File;

/**
 * Created by Nikolay on 09.08.2014.
 */
public class UploadFileActivity extends Activity
		implements ProgressiveEntityListener{
	private String mToken ="";
	public ProgressDialog mProgressDialog = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_file);
		Intent intent = getIntent();
		mToken = intent.getStringExtra("token");
		Log.d("udinic", "Got token: "+mToken);
	}

	@Override
	public void onFileUpload(String response, String fileName, FileEntity fileEntity, String responseId) {
		Log.d("udinic","onFileUpload");
	}

	public void startUploadFileActivity(View view){
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.setTitle("Upload");
		mProgressDialog.setMessage("Uploading");
		mProgressDialog.setMax(100);
		mProgressDialog.setProgress(0);

		final String token = mToken;
		String cmsBaseUrl = "http://dcm2.eccentex.com:8086";
		final String domain = "Eccentex_Example_Production.tenant41";
		final String responseId = "1";
		final String filePath = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES).getAbsolutePath();
		final String fileName = "IMG_1.jpg";
		String url = cmsBaseUrl + "/CMS.WebService/CMSServiceRest.svc/createResource/" + domain;
		String query = String.format("%s=%s&%s=%s", "t", token, "u", "/" + fileName);
		url += "?" + query;
		Log.e("udinic",url);
		final File file = new File(filePath+"/"+fileName);
		final FileEntity fileEntity = new FileEntity(file, "image/jpg");

		CreateResourceTask putTask = new CreateResourceTask(this, fileEntity, fileName, url, responseId);
		putTask.execute();
	}
}