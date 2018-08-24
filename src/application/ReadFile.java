package application;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class ReadFile {


	private int rowLength ;
	private int colLength ; 
	private static String[][][] array ;
	

	public ReadFile()
	{
		array= new String[26][80][2];
		rowLength=0;
		colLength=0;
	}
	public String[][][] readFile(String str) throws Exception {
		
			int i = 0,j = 0; int u=0;
			Scanner input = new Scanner(Paths.get(str));
			
			while(input.hasNext()) 
			{
				j = 0;
				String obj = input.next();
				String temp[] = obj.split("\\#\\#");
				
				for(String s:temp){
					u=0;
					String temp2[] = s.split("\\@\\@");
					for(String t : temp2){
						array[i][j][u]=t;
						if(array[i][j][u]=="null")
							array[i][j][u]="";
						u++;
					}
					j++;
				}
				rowLength = j; 
				
				i++;	
			}
			colLength = i;
			if(input != null)
			{
				input.close();
			}
			
			return array;
			
		
	}

	public String[][][] getArray() {
		return array;
	}
	
	public int getRowLength() {
		return rowLength;
	}
	
	public int getColumLength() {
		return colLength;
	}



}
