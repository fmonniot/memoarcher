package eu.monniot.memoArcher;

import java.util.ArrayList;
import java.util.List;

public class Bow {
	
	private List<Bow.Landmark> mLandmark = new ArrayList<Bow.Landmark>();
	private long mId;
	private String mName;
	private String mMarkUnit = "coutures";
	private String mDistanceUnit = "m";
	
	public Bow() {

		Landmark l = new Landmark();
		l.distance = 15;
		l.mark = 7;
		mLandmark.add(l);
		mLandmark.add(l);
		mLandmark.add(l);		
	}
	
	public void setId(long id) {
		mId = id;
	}
	
	public long getId() {
		return mId;
	}
	
	public void setName(String name) {
		mName = name;
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

	public Bow.Landmark getLandmark(int at) {
		return mLandmark.get(at);
	}

	public int getLandmarkCount() {
		return mLandmark.size();
	}
	
	// TODO add some consistency to the Landmark class
	public class Landmark {
		public double distance;
		public double mark;
	}
	
}
