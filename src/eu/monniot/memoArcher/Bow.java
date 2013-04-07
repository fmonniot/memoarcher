package eu.monniot.memoArcher;

import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;

public class Bow {
	
	private List<Landmark> mLandmarks = new ArrayList<Landmark>();
	private long mId = 0;
	private String mName;
	private String mMarkUnit = "coutures";
	private String mDistanceUnit = "m";
	
	public Bow() {
		// Dummy instantiation, to remove after the implementation of LandmarkManager
//		Landmark l = new Landmark();
//		l.setDistance(15);
//		l.setMark(7);
//		mLandmarks.add(l);
//		mLandmarks.add(l);
//		mLandmarks.add(l);		
	}
	
	public void setId(long id) {
		if( mId != 0) {
			throw new IllegalAccessError("Can not modify an ID this way.");
		}
		mId = id;
	}
	
	public long getId() {
		return mId;
	}
	
	public void setName(String name) {
		if(!TextUtils.isEmpty(name)) {
			mName = name;
		} else
			throw new IllegalArgumentException("Bow name caanot be empty");
	}
	
	public String getName() {
		return mName;
	}

	public String getMarkUnit() {
		return mMarkUnit;
	}
	
	public void setMarkUnit(String markUnit) {
		mMarkUnit = markUnit;
	}

	public String getDistanceUnit() {
		return mDistanceUnit;
	}
	
	public void setDistanceUnit(String distanceUnit) {
		mDistanceUnit = distanceUnit;
	}
	
	
	/*
	 * These methods are mainly wrappers around the List<Landmark>
	 */
	public Landmark getLandmark(int at) {
		return mLandmarks.get(at);
	}

	public List<Landmark> getLandmarks() {
		return mLandmarks;
	}
	
	public int getLandmarkCount() {
		return mLandmarks.size();
	}
	
	public void addLandmark(Landmark landmark) {
		mLandmarks.add(landmark);
	}
	
	public boolean addLandmarks(List<Landmark> list) {
		if(list == null)
			return false;
		
		return mLandmarks.addAll(list);
	}
	
	public void removeLandmark(Landmark landmark) {
		mLandmarks.remove(landmark);
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
}
