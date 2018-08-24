package application;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Formatter;
import java.util.Scanner;

public class WriteExcel {
	
	private static Formatter output;
	
	public void saveFile(String str,int c ,int r , Excel excel) throws IOException {
		
		String fileName= str;
		
		output = new Formatter (fileName);
	
		
			for(int col=0;col<c;col++){
				
				for(int row=0;row<r;row++)
				{
						output.format("%s@@",excel.getValueOfCell(col, row));
						if(excel.getFormulaOfCell(col, row)=="")
						output.format("null##");
						else
						output.format("%s##",excel.getFormulaOfCell(col, row));
					if(row==r-1)
						output.format("%n");
				}
				
			
			}
		output.close();
	}

}
