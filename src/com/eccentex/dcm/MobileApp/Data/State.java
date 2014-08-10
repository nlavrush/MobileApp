package com.eccentex.dcm.MobileApp.Data;

import android.os.Parcel;
import android.os.Parcelable;

public class State implements Parcelable {
	public int mId=0;
	public String mName = "";
	public String mAbbr = "";
	public  String toString(){
		return  mName;
	}
	public State(){}
	public State(int id, String name, String abbr){
		this.mId = id;
		this.mName = name;
		this.mAbbr = abbr;
	}

	public State(Parcel in){
		String[] data = new String[3];
		in.readStringArray(data);
		this.mId = Integer.parseInt(data[0]);
		this.mName = data[1];
		this.mAbbr = data[2];
	}
	public static State[] fromParcelable(Parcelable[] parcelables) {
		State[] objects = new State[parcelables.length];
		System.arraycopy(parcelables, 0, objects, 0, parcelables.length);
		return objects;
	}
	public static Parcelable[] toParcelable(State[] states) {
		Parcelable[] objects = new Parcelable[states.length];
		System.arraycopy(states, 0, objects, 0, states.length);
		return objects;
	}
	@Override
	public int describeContents(){
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[] {Integer.toString(this.mId),
				this.mName,
				this.mAbbr});
	}
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public State createFromParcel(Parcel in) {
			return new State(in);
		}

		public State[] newArray(int size) {
			return new State[size];
		}
	};
}
