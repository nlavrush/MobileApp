package com.eccentex.dcm.MobileApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	public final static String EXTRA_MESSAGE = "com.eccentex.dcm.MESSAGE";
	public final static String CASE_TITLE = "com.eccentex.dcm.CASE_TITLE";
	public final static String CASE_TEXT = "com.eccentex.dcm.CASE_TEXT";
	/**
	 * Called when the activity is first created.
	 */
		@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}
	public void sendMessage(View view){
		Log.d(TAG, "Button clicked");
		Intent intent = new Intent(this, CaseDetailActivity.class);
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String title = editText.getText().toString();
		intent.putExtra(CASE_TITLE, title);
		EditText editText2 = (EditText) findViewById(R.id.edit_message2);
		String description = editText2.getText().toString();
		intent.putExtra(CASE_TEXT, description);
		startActivity(intent);
	}
}
