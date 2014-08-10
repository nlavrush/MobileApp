package com.eccentex.dcm.MobileApp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.eccentex.dcm.MobileApp.Authentication.AccountGeneral;
import com.eccentex.dcm.MobileApp.Data.JSONResponseParser;
import com.eccentex.dcm.MobileApp.Data.State;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class MainActivity extends Activity {
	private static final String STATE_DIALOG = "state_dialog";
	private static final String STATE_INVALIDATE = "state_invalidate";

	private String TAG = this.getClass().getSimpleName();
	private AccountManager mAccountManager;
	private AlertDialog mAlertDialog;
	private boolean mInvalidate;
	private  String mToken ="";

	private void showMessage(final String msg) {
		if (TextUtils.isEmpty(msg))
			return;

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
			}
		});
	}
	/**
	 * Called when the activity is first created.
	 */
		@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mAccountManager = AccountManager.get(this);
		findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getTokenForAccountCreateIfNeeded(AccountGeneral.ACCOUNT_TYPE, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);
			}
		});
			findViewById(R.id.pick_button).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					showAccountPicker(AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS,false);
				}
			});
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mAlertDialog != null && mAlertDialog.isShowing()) {
			outState.putBoolean(STATE_DIALOG, true);
			outState.putBoolean(STATE_INVALIDATE, mInvalidate);
		}
	}
	public void sendMessage(View view){
		Log.d(TAG, "Button clicked");

	}
	/**
	 * Add new account to the account manager
	 * @param accountType
	 * @param authTokenType
	 */
	private void addNewAccount(String accountType, String authTokenType) {
		final AccountManagerFuture<Bundle> future = mAccountManager.addAccount(accountType, authTokenType, null, null, this, new AccountManagerCallback<Bundle>() {
			@Override
			public void run(AccountManagerFuture<Bundle> future) {
				try {
					Bundle bnd = future.getResult();
					showMessage("Account was created");
					Log.d("udinic", "AddNewAccount Bundle is " + bnd);

				} catch (Exception e) {
					e.printStackTrace();
					showMessage(e.getMessage());
				}
			}
		}, null);
	}

	/**
	 * Show all the accounts registered on the account manager. Request an auth token upon user select.
	 * @param authTokenType
	 */
	private void showAccountPicker(final String authTokenType, final boolean invalidate) {
		mInvalidate = invalidate;
		final Account availableAccounts[] = mAccountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);

		if (availableAccounts.length == 0) {
			Toast.makeText(this, getString(R.string.no_account), Toast.LENGTH_SHORT).show();
		} else {
			String name[] = new String[availableAccounts.length];
			for (int i = 0; i < availableAccounts.length; i++) {
				name[i] = availableAccounts[i].name;
			}

			// Account picker
			mAlertDialog = new AlertDialog.Builder(this).setTitle(getString(R.string.pick_account)).setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, name), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(invalidate)
						invalidateAuthToken(availableAccounts[which], authTokenType);
					else
						getExistingAccountAuthToken(availableAccounts[which], authTokenType);
				}
			}).create();
			mAlertDialog.show();
		}
	}

	/**
	 * Get the auth token for an existing account on the AccountManager
	 * @param account
	 * @param authTokenType
	 */
	private void getExistingAccountAuthToken(Account account, final String authTokenType) {
		final AccountManagerFuture<Bundle> future = mAccountManager.getAuthToken(account, authTokenType, null, this, null, null);

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Bundle bnd = future.getResult();

					final String authtoken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
					mToken = authtoken;
					showMessage((authtoken != null) ? "SUCCESS!" : "FAIL");
					Log.d("udinic", "GetToken Bundle is " + bnd);
					getStatesList(mToken);
//					Intent intent = new Intent(this, UploadFileActivity.class);
//					intent.putExtra("token",authtoken);
//					startActivity(intent);
/**********************************************************************************************/
//					DefaultHttpClient httpClient = new DefaultHttpClient();
//					String server ="http://dcm2.eccentex.com";
//					String port = "8086";
//					String domain ="Eccentex_Example_Production.tenant41";
//					String rule ="root_getListOfStates";
//					String url =server+":"+port+"/BDS.WebService/DataServiceRest.svc/get.json/"+domain+"/"+rule;
//					url +="?t="+authtoken;
//					Log.d("udini", "url: "+url);
//					HttpGet httpGet = new HttpGet(url);
//
//					httpGet.addHeader("Host", "dcm2.eccentex.com:8086");
//					httpGet.addHeader("Content-Type", "application/json");
//					HttpParams params = new BasicHttpParams();
//
//					httpGet.setParams(params);
////        httpGet.getParams().setParameter("username", user).setParameter("password", pass);
//
//					try {
//						HttpResponse response = httpClient.execute(httpGet);
//
//						String responseString = EntityUtils.toString(response.getEntity());
//						//String responseString2 = response.getFirstHeader("Cookie").getValue();
//						//Log.d("udini",responseString);
//						if (response.getStatusLine().getStatusCode() != 200) {
//							Log.d("udini", "bad!");
//							ParseComError error = new Gson().fromJson(responseString, ParseComError.class);
//							throw new Exception("Error signing-in ["+error.code+"] - " + error.error);
//						}
//
//
//						String pattern = "(null)";
//						String result = responseString.replaceAll(pattern, "\"" + "$1" + "\"");
//						JSONObject parser = new JSONObject(result);
//						JSONObject jsonDataRoot = parser.getJSONObject("DATA");
//						JSONObject jsonRootMobile = jsonDataRoot.getJSONObject("root_getListOfStates");
//						JSONArray items = jsonRootMobile.getJSONArray("ITEMS");
//						int itemsLength = items.length();
//
//
//						//Log.i("udini", String.valueOf(itemsLength));
////						State[] states = new Gson().fromJson(items,State[].class);
//						for(int i=0; i < itemsLength; i++) {
//							JSONObject item = items.getJSONObject(i);
//							Log.e("udini", "number= " + item.getString("ID") + " Name = " + item.getString("STATENAME") + " Nickname= "
//									+ item.getString("STATEABBR"));
//						}
//
//					} catch (IOException e) {
//						e.printStackTrace();
//					}

//					return authtoken;








					/***************************************************************************************************/
				} catch (Exception e) {
					e.printStackTrace();
					showMessage(e.getMessage());
				}
			}
		}).start();
	}

	/**
	 * Invalidates the auth token for the account
	 * @param account
	 * @param authTokenType
	 */
	private void invalidateAuthToken(final Account account, String authTokenType) {
		final AccountManagerFuture<Bundle> future = mAccountManager.getAuthToken(account, authTokenType, null, this, null,null);

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Bundle bnd = future.getResult();

					final String authtoken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
					mAccountManager.invalidateAuthToken(account.type, authtoken);
					showMessage(account.name + " invalidated");
				} catch (Exception e) {
					e.printStackTrace();
					showMessage(e.getMessage());
				}
			}
		}).start();
	}

	/**
	 * Get an auth token for the account.
	 * If not exist - add it and then return its auth token.
	 * If one exist - return its auth token.
	 * If more than one exists - show a picker and return the select account's auth token.
	 * @param accountType
	 * @param authTokenType
	 */
	private void getTokenForAccountCreateIfNeeded(String accountType, String authTokenType) {
		final AccountManagerFuture<Bundle> future = mAccountManager.getAuthTokenByFeatures(accountType, authTokenType, null, this, null, null,
				new AccountManagerCallback<Bundle>() {
					@Override
					public void run(AccountManagerFuture<Bundle> future) {
						Bundle bnd = null;
						try {
							bnd = future.getResult();
							final String authtoken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
							showMessage(((authtoken != null) ? "SUCCESS!": "FAIL"));
							Log.d("udinic", "GetTokenForAccount Bundle is " + bnd);

						} catch (Exception e) {
							e.printStackTrace();
							showMessage(e.getMessage());
						}
					}
				}
				, null);
	}
	private class DownloadUrlTask extends AsyncTask<String, Void, String>
	{

		@Override
		protected String doInBackground(String... params) {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			String responseString = "";
			String url =Config.URL_PREFIX+Config.DEFAULT_HOST+":"+Config.DEFAULT_PORT+"/"+Config.DEFAULT_URL_BODY+"/"+
					Config.DEFAULT_DOMAIN+"/"+Config.MOBILE_GET_STATES_RULE;
			url +="?t="+params[0];
			Log.d(TAG, "url: "+url);
			HttpGet httpGet = new HttpGet(url);
			httpGet.addHeader("Host", Config.DEFAULT_HOST+":"+Config.DEFAULT_PORT);
			httpGet.addHeader("Content-Type", "application/json");
			HttpParams httpParams = new BasicHttpParams();
			httpGet.setParams(httpParams);
			try {
				HttpResponse response = httpClient.execute(httpGet);
				responseString = EntityUtils.toString(response.getEntity());
				if (response.getStatusLine().getStatusCode() != 200) {
					Log.d(TAG, "bad response!");
					throw new Exception("Error signing-in!");
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return responseString;
		}
		@Override
		protected void onPostExecute(String result){
			State states[] = JSONResponseParser.getStatesJsonResponse(result);
			Intent intent = new Intent(getApplicationContext(), StateListActivity.class);
//			intent.putExtra("size2",states.length);
//			Log.e(TAG,"size2= "+states.length);
//			for(int i=0;i<states.length;i++){
//				intent.putExtra(Integer.toString(i),states[i]);
//			}
			//intent.putExtra("states",(Parcelable[])states);
			intent.putExtra("states",State.toParcelable(states));
			startActivity(intent);
		}
	}
	private void getStatesList(final String authToken){

		DownloadUrlTask task = new DownloadUrlTask();
		task.execute(authToken);

	}
}
