package application;

public class Cell {

	private String value;
	
	public Cell()
	{
		this.value="";
	
	}
	public Cell(String str)
	{
		this.value=str;
	}
	
	public String getValueOfCell()
	{
		return value;
	}
	public void setValueOfCell(String str)
	{
		this.value=str;
	}
	
	
}
