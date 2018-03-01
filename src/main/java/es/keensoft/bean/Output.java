package es.keensoft.bean;

import java.util.List;

public class Output {
	
	Integer vehicle;
	List<Integer> rides;
	
	public Integer getVehicle() {
		return vehicle;
	}
	public void setVehicle(Integer vehicle) {
		this.vehicle = vehicle;
	}
	public List<Integer> getRides() {
		return rides;
	}
	public void setRides(List<Integer> rides) {
		this.rides = rides;
	}
	@Override
	public String toString() {
		return "Output [vehicle=" + vehicle + ", rides=" + rides + "]";
	}
			
	
}
