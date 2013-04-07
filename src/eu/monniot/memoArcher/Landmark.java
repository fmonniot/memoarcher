package eu.monniot.memoArcher;

// TODO add some consistency to the Landmark class
public class Landmark {
	private long mId;
	private long mBowId;
	private double mDistance;
	private double mMark;
	
	public String toString() {
		return "id:"+String.valueOf(mId)+" | mark:"+String.valueOf(mMark)+" | distance:"+String.valueOf(mDistance);
	}
	
	public double getDistance() {
		return mDistance;
	}

	public void setDistance(double distance) {
		this.mDistance = distance;
	}

	public double getMark() {
		return mMark;
	}

	public void setMark(double mark) {
		this.mMark = mark;
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
	
	public void belong_to(Bow bow) {
		mBowId = bow.getId();
	}
	
	public long getBowId() {
		return mBowId;
	}
}