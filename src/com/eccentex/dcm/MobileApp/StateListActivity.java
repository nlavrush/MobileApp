package com.eccentex.dcm.MobileApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.eccentex.dcm.MobileApp.Data.State;

/**
 * Created by Nikolay on 10.08.2014.
 */
public class StateListActivity extends Activity {
	private final String TAG = this.getClass().getSimpleName();
	private State states[] = null;
	private String mToken;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_of_states);
		Intent intent = getIntent();
		if(intent != null) {

			Bundle data = intent.getExtras();
			if(data != null) {
//				State states[] = new State[data.getInt("size2",1)];//State.fromParcelable(data.getParcelableArray("states"));
//				for(int i=0;i<states.length;i++){
//					states[i] = data.getParcelable(Integer.toString(i));
//				}
                mToken = data.getString("token");
				states = State.fromParcelable(data.getParcelableArray("states"));
				if(states.length >0) {
					ArrayAdapter adapter = new ArrayAdapter<State>(this,
							android.R.layout.simple_list_item_1, states);
					ListView listView =  (ListView) findViewById(R.id.listView);
					listView.setAdapter(adapter);
					listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							Log.e(TAG,"Choses state # "+position);
							if(position>=0 && position < states.length){
							//	Log.e(TAG,"name: "+states[position].mName);
								Intent intent = new Intent(view.getContext(),StateDetailActivity.class);
								intent.putExtra("token",mToken);
								intent.putExtra("id",Integer.toString(states[position].mId));
								intent.putExtra("name",states[position].mName);
								intent.putExtra("abbr",states[position].mAbbr);
								startActivity(intent);
							}
						}
					});
				}
				else {
					Log.e(TAG, "states is empty");
				}
			}
			else {
				Log.e(TAG, "bundle = null");
			}
		}
		else {
			Log.e(TAG, "intent = null");
		}
	}
}