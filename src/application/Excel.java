package application;

public class Excel {
	
	
	private Sheet sheet;
	
	public Excel( int column,int row)
	{
		this.sheet = new Sheet(column,row);
	}

	public void setValueOfCell(int c, int r, String str)
	{
		if(str.contains("="))
		sheet.setFormulaofCell(c, r, str);
		else
		sheet.setValueofCell(c, r, str);
	}
	public void resetFormulaOfCell(int c, int r)
	{
		sheet.setFormulaofCell(c, r, "");
	}
	
	public String getValueOfCell(int c, int r )
	{
		return sheet.getValueOfCell(c, r);
	}
	public String getFormulaOfCell(int c, int r )
	{
		return sheet.getFormulaOfCell(c, r);
	}
	public void save()
	{
		
	}
	public String Calculate(int c,int r)
	{
		return sheet.decode(c,r);
	}

}
