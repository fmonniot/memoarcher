package eu.monniot.memoArcher;

import java.util.ArrayList;
import java.util.List;

public class Bow {
	
	private List<Bow.Landmark> mLandmark = new ArrayList<Bow.Landmark>();
	private String mDistanceUnit = "m";
	private String mMarkUnit = "coutures";
	private int mId;
	private String mName;
	
	public static Bow instanciateFromId(int id) {
		Bow bow = new Bow(id);
		
		return bow;
	}
	
	public Bow(int id) {
		mId = id;
		//TODO Retrieve content from database
		Landmark l = new Landmark();
		l.distance = 15;
		l.mark = 7;
		mLandmark.add(l);
		mLandmark.add(l);
		mLandmark.add(l);		
	}
	
	public String getDistanceUnit() {
		return mDistanceUnit;
	}
	
	public String getMarkUnit() {
		return mMarkUnit;
	}
	
	public int getId() {
		return mId;
	}
	
	public String getName() {
		return mName;
	}
	
	public Bow.Landmark getLandmark(int at) {
		return mLandmark.get(at);
	}

	public int getLandmarkCount() {
		return mLandmark.size();
	}
	
	public class Landmark {
		int distance;
		int mark;
	}
}
