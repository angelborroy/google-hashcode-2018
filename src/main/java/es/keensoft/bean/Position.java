package es.keensoft.bean;

public class Position {
	
	Integer vehicle;
	Integer row;
	Integer col;
	
	public Integer getVehicle() {
		return vehicle;
	}
	public void setVehicle(Integer vehicle) {
		this.vehicle = vehicle;
	}
	public Integer getRow() {
		return row;
	}
	public void setRow(Integer row) {
		this.row = row;
	}
	public Integer getCol() {
		return col;
	}
	public void setCol(Integer col) {
		this.col = col;
	}
	@Override
	public String toString() {
		return "Position [vehicle=" + vehicle + ", row=" + row + ", col=" + col + "]";
	}

}
