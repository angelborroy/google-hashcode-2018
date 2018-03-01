package es.keensoft.bean;

import java.util.List;

public class Input {
	
	Integer rows;
	Integer cols;
	Integer vehicles;
	Integer rides;
	Integer bonus;
	Integer steps;
	
	List<InputLine> ride;

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public Integer getCols() {
		return cols;
	}

	public void setCols(Integer cols) {
		this.cols = cols;
	}

	public Integer getVehicles() {
		return vehicles;
	}

	public void setVehicles(Integer vehicles) {
		this.vehicles = vehicles;
	}

	public Integer getRides() {
		return rides;
	}

	public void setRides(Integer rides) {
		this.rides = rides;
	}

	public Integer getBonus() {
		return bonus;
	}

	public void setBonus(Integer bonus) {
		this.bonus = bonus;
	}

	public Integer getSteps() {
		return steps;
	}

	public void setSteps(Integer steps) {
		this.steps = steps;
	}

	public List<InputLine> getRide() {
		return ride;
	}

	public void setRide(List<InputLine> ride) {
		this.ride = ride;
	}

	@Override
	public String toString() {
		return "Input [rows=" + rows + ", cols=" + cols + ", vehicles=" + vehicles + ", rides=" + rides + ", bonus="
				+ bonus + ", steps=" + steps + ", ride=" + ride + "]";
	}


}
