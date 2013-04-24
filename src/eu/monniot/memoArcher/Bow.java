package eu.monniot.memoArcher;

import java.util.ArrayList;
import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;


@Table(name = "bows")
public class Bow extends Model {
	
	private List<Landmark> mLandmarks;

	@Column(name = "name")
	public String name;
	
	@Column(name = "mark_unit")
	public String markUnit;
	
	@Column(name = "distance_unit")
	public String distanceUnit;
	
	public Bow() {
		super();
		mLandmarks = new ArrayList<Landmark>();
	}
	
	public Bow(String name, String markUnit, String distanceUnit) {
		super();
		
		this.name = name;
		this.markUnit = markUnit;
		this.distanceUnit = distanceUnit;
	}

	public List<Landmark> landmarks() {
		mLandmarks = getMany(Landmark.class, "Landmark");
		return mLandmarks;
	}
	
	public int landmarksCount() {
		return mLandmarks.size();
	}
	
	public Landmark landmark(int id) {
		
		for(Landmark lm : mLandmarks) {
			if( lm.getId().longValue() == id)
				return lm;
		}
		
		return null;
	}
	
	public static List<Bow> loadAll() {
		return new Select().from(Bow.class).execute();
	}
	
	@Override
	public String toString() {
		return name;
	}
}
