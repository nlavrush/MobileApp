package com.eccentex.dcm.MobileApp.Authentication;

import android.util.Log;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * Handles the comminication with AppBase
 *
 * User: udinic
 * Date: 3/27/13
 * Time: 3:30 AM
 */
public class ParseComServerAuthenticate implements ServerAuthenticate {
    @Override
    public String userSignUp(String name, String user, String pass, String authType) throws Exception {

		Log.d("udini", "userSignIn");

		DefaultHttpClient httpClient = new DefaultHttpClient();
		String url = "http://dcm2.eccentex.com:8086/Security.WebService/AuthenticationServiceRest.svc/login";


		String query = null;
		try {
			query = String.format("%s=%s&%s=%s", "u", URLEncoder.encode(user, "UTF-8"), "p", pass);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		url += "?" + query;
		Log.d("udini", "url: "+url);
		HttpGet httpGet = new HttpGet(url);

//        httpGet.addHeader("X-Parse-Application-Id", "XUafJTkPikD5XN5HxciweVuSe12gDgk2tzMltOhr");
//        httpGet.addHeader("X-Parse-REST-API-Key", "8L9yTQ3M86O4iiucwWb4JS7HkxoSKo7ssJqGChWx");
		httpGet.addHeader("Host", "dcm2.eccentex.com:8086");
		httpGet.addHeader("Content-Type", "application/json");
		HttpParams params = new BasicHttpParams();
		params.setParameter("username", user);
		params.setParameter("password", pass);
		Log.d("udini", "user: "+user+"pas: "+pass);
		httpGet.setParams(params);
//        httpGet.getParams().setParameter("username", user).setParameter("password", pass);

		String authtoken = null;
		try {
			HttpResponse response = httpClient.execute(httpGet);

			String responseString = EntityUtils.toString(response.getEntity());
			//String responseString2 = response.getFirstHeader("Cookie").getValue();
			Log.d("udini",responseString);
			if (response.getStatusLine().getStatusCode() != 200) {
				Log.d("udini", "bad!");
				ParseComError error = new Gson().fromJson(responseString, ParseComError.class);
				throw new Exception("Error signing-in ["+error.code+"] - " + error.error);
			}

			MAuthToken tok = new Gson().fromJson(responseString, MAuthToken.class);
			authtoken = tok.token();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return authtoken;
    }

    @Override
    public String userSignIn(String user, String pass, String authType) throws Exception {

        Log.d("udini", "userSignIn");

        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "http://dcm2.eccentex.com:8086/Security.WebService/AuthenticationServiceRest.svc/login";


        String query = null;
        try {
            query = String.format("%s=%s&%s=%s", "u", URLEncoder.encode(user, "UTF-8"), "p", pass);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        url += "?" + query;
		Log.d("udini", "url: "+url);
        HttpGet httpGet = new HttpGet(url);

//        httpGet.addHeader("X-Parse-Application-Id", "XUafJTkPikD5XN5HxciweVuSe12gDgk2tzMltOhr");
//        httpGet.addHeader("X-Parse-REST-API-Key", "8L9yTQ3M86O4iiucwWb4JS7HkxoSKo7ssJqGChWx");
		httpGet.addHeader("Host", "dcm2.eccentex.com:8086");
		httpGet.addHeader("Content-Type", "application/json");
        HttpParams params = new BasicHttpParams();
        params.setParameter("username", user);
        params.setParameter("password", pass);
		Log.d("udini", "user: "+user+"pas: "+pass);
        httpGet.setParams(params);
//        httpGet.getParams().setParameter("username", user).setParameter("password", pass);

        String authtoken = null;
        try {
            HttpResponse response = httpClient.execute(httpGet);

            String responseString = EntityUtils.toString(response.getEntity());
			//String responseString2 = response.getFirstHeader("Cookie").getValue();
			Log.d("udini",responseString);
            if (response.getStatusLine().getStatusCode() != 200) {
                Log.d("udini", "bad!");
				ParseComError error = new Gson().fromJson(responseString, ParseComError.class);
                throw new Exception("Error signing-in ["+error.code+"] - " + error.error);
            }

            responseString = responseString.substring(1);
			responseString = responseString.substring(0,responseString.length()-1);
			Log.i("udini",responseString);
            authtoken = responseString;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return authtoken;
    }


    private class ParseComError implements Serializable {
        int code;
        String error;
    }
	private class MAuthToken implements  Serializable {
		String mToken;
		public  String token() { return  mToken;}
		public void setToken(String token) {this.mToken = token;}

	}

    private class User implements Serializable {

        private String firstName;
        private String lastName;
        private String username;
        private String phone;
        private String objectId;
        public String sessionToken;
        private String gravatarId;
        private String avatarUrl;


        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public String getSessionToken() {
            return sessionToken;
        }

        public void setSessionToken(String sessionToken) {
            this.sessionToken = sessionToken;
        }

        public String getGravatarId() {
            return gravatarId;
        }

        public void setGravatarId(String gravatarId) {
            this.gravatarId = gravatarId;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }
    }
}
