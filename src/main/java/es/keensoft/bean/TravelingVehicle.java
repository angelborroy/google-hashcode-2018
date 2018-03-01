package es.keensoft.bean;

public class TravelingVehicle {
	
	Integer time;
	Integer ride;
	public Integer getTime() {
		return time;
	}
	public void setTime(Integer time) {
		this.time = time;
	}
	public Integer getRide() {
		return ride;
	}
	public void setRide(Integer ride) {
		this.ride = ride;
	}
	@Override
	public String toString() {
		return "TravellingVehicle [time=" + time + ", ride=" + ride + "]";
	}
}


