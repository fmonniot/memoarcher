package eu.monniot.memoArcher;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

public class Landmark extends Model{
	
	@Column(name = "bow")
	public Bow bow;
	
	@Column(name = "distance")
	public double distance;
	
	@Column(name = "mark")
	public double mark;
	
	public String toString() {
		return "id:"+String.valueOf(getId())+" | mark:"+String.valueOf(mark)+" | distance:"+String.valueOf(distance);
	}

}