package es.keensoft.bean;

public class TravelingVehicle {
	
	Integer time;
	Integer ride;
	Integer col;
	Integer row;
	Integer targetCol;
	Integer targetRow;
	
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
	public Integer getCol() {
		return col;
	}
	public void setCol(Integer col) {
		this.col = col;
	}
	public Integer getRow() {
		return row;
	}
	public void setRow(Integer row) {
		this.row = row;
	}
	public Integer getTargetCol() {
		return targetCol;
	}
	public void setTargetCol(Integer targetCol) {
		this.targetCol = targetCol;
	}
	public Integer getTargetRow() {
		return targetRow;
	}
	public void setTargetRow(Integer targetRow) {
		this.targetRow = targetRow;
	}
	@Override
	public String toString() {
		return "TravelingVehicle [time=" + time + ", ride=" + ride + ", col=" + col + ", row=" + row + ", targetCol=" + targetCol + ", targetRow=" + targetRow
				+ "]";
	}
}


