package com.eccentex.dcm.MobileApp.Data;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONResponseParser{
	private static final String TAG = "JSONParser";
	public  static  State[] getStatesJsonResponse(String responseString) throws Exception{
		Log.d(TAG, responseString);
		String pattern = "(null)";
		String result = responseString.replaceAll(pattern, "\"" + "$1" + "\"");
		JSONObject parser = null;
		Log.d(TAG, responseString);
		try {
			parser = new JSONObject(result);
			JSONObject jsonDataRoot = parser.getJSONObject("DATA");
			JSONObject jsonRootMobile = jsonDataRoot.getJSONObject("root_getListOfStates");
			JSONArray items = jsonRootMobile.getJSONArray("ITEMS");
			int itemsLength = items.length();
			Log.d(TAG, "length = " + itemsLength);
			State data[] = new State[itemsLength];
			Log.d(TAG, "data length = " + data.length);
			for(int i=0; i < itemsLength; i++) {
				JSONObject item = items.getJSONObject(i);
				data[i] = new State();
				data[i].mName = item.getString("STATENAME");
				data[i].mId = Integer.parseInt(item.getString("ID"));
				data[i].mAbbr = item.getString("STATEABBR");
//				Log.e(TAG, "number= " + item.getString("ID") + " Name = " + item.getString("STATENAME") + " Nickname= "
//						+ item.getString("STATEABBR"));
			}
			return data;
		} catch (JSONException e) {
			//e.printStackTrace();
			try {
				throw  new Exception("Error code: "+parser.getString("ErrorCode")+"\n"+parser.getString("ErrorMessage"));
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}


		return null;
	}
}
