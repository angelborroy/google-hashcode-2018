package es.keensoft.bean;

public class InputLine {
	
	Integer startingRow;
	Integer startingCol;
	Integer endingRow;
	Integer endingCol;
	Integer startingTime;
	Integer finishingTime;
	
	public Integer getStartingRow() {
		return startingRow;
	}
	public void setStartingRow(Integer startingRow) {
		this.startingRow = startingRow;
	}
	public Integer getStartingCol() {
		return startingCol;
	}
	public void setStartingCol(Integer startingCol) {
		this.startingCol = startingCol;
	}
	public Integer getEndingRow() {
		return endingRow;
	}
	public void setEndingRow(Integer endingRow) {
		this.endingRow = endingRow;
	}
	public Integer getEndingCol() {
		return endingCol;
	}
	public void setEndingCol(Integer endingCol) {
		this.endingCol = endingCol;
	}
	public Integer getStartingTime() {
		return startingTime;
	}
	public void setStartingTime(Integer startingTime) {
		this.startingTime = startingTime;
	}
	public Integer getFinishingTime() {
		return finishingTime;
	}
	public void setFinishingTime(Integer finishingTime) {
		this.finishingTime = finishingTime;
	}
	@Override
	public String toString() {
		return "InputLine [startingRow=" + startingRow + ", startingCol=" + startingCol + ", endingRow=" + endingRow
				+ ", endingCol=" + endingCol + ", startingTime=" + startingTime + ", finishingTime=" + finishingTime
				+ "]";
	}


}
