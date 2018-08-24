package application;

public class Sheet {	
		private char[] colIndex =   {'A','B','C','D','E','F','G','H','I','J','K',
				'L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'}; 
		
		private Cell[][][] cell; // column,row,value or formula
		
		//==================contructor=========================================================
		Sheet(int column,int row)
		{
			cell = new Cell[column][row][2]; // last index 0:value 1: formula
			for(int i=0 ; i < column ; i++)
			{
				for ( int t=0 ; t < row; t++)
					for(int z =0; z<2 ; z++)
					cell[i][t][z] = new Cell();
			}
		}
		
		public void setFormulaofCell(int c,int r, String str)//input formula at index 1
		{
			this.cell[c][r][1].setValueOfCell(str);
		}
		
		public void setValueofCell(int c,int r, String str)//input value at index 0
		{
			this.cell[c][r][0].setValueOfCell(str);
		}
		
		public String getFormulaOfCell(int c,int r)
		{
			return this.cell[c][r][1].getValueOfCell(); 
		}
		
		public String getValueOfCell(int c,int r)
		{
			return this.cell[c][r][0].getValueOfCell();
		}
		
		public String decode(int c, int r)
		{
			String[] num;
			String str=this.getFormulaOfCell(c, r);
			NormalFormula nf =new NormalFormula();
			
			 String[] opCode = {"+", "-", "*", "/"};
			 //===================check normal calculate==================================================================================
			try
			{
			 
				for(int op=0; op<4;op++)
				{
					 if(str.contains(opCode[op]))
					 {
						 str=remakeFormulaIndexToValue(str);
						 return nf.Calc(str.substring(1));
					 } 
				}
				
				
				//=================decode function=================================================================
				switch (str.substring(0, str.indexOf('(')+1))
				{
				
				case "=sum(" :
					if(str.contains(":"))
							num=rangeDecode(str,5);
					else
					num = decodeString( str , 5);
					double result=0;
					for(int i=0;i<num.length;i++)
					{
							if(num[i]=="")
								num[i]="0";
							result+=Double.parseDouble(num[i]);
					}
					return String.valueOf(result); 
					
				case "=average(" :
					if(str.contains(":"))	
							num=rangeDecode(str,5);
					else
					num = decodeString( str ,9 );
					result=0;
					for(int i=0;i<num.length;i++)
					{
						if(num[i]=="")
						num[i]="0";
						result+=Double.parseDouble(num[i]);

					}
					result/=num.length;
					return String.valueOf(result); 
					
				case  "=counta(" :
					return countA(str,8) ;
				
				default :
					return "wrong formula";
				}
			}
			catch (Exception inputerror)
			{
				return "wrong formual";
			}
		}
		// =================counta method  =============================================================
		private String countA (String str ,int formulaLength)
		{
			
			int count=0;
			if(str.length()<=formulaLength)
				return "formula error";
			if(str.contains(")"))
				str=str.substring(str.indexOf('(')+1, str.length()-1);
			else
				str=str.substring(str.indexOf('(')+1, str.length());
			
			if(str.contains(":"))
			{
				int[][] rangeColNRow =  new int [2][2]; // first index [] - index of cell of formula, second index [][] - column,row
				String temp[]=str.split(":");
				for(int q=0;q<2;q++)
				{
					for(int p=0; p< 26;p++)
					{
						if(temp[q].contains(String.valueOf(colIndex[p])))
						{	
							rangeColNRow[q][0]=p;
							rangeColNRow[q][1]=Integer.parseInt(temp[q].replace(String.valueOf(colIndex[p]),""))-1;
							
							break;
						}
					
					}
				}
				for(int col=rangeColNRow[0][0]; col<rangeColNRow[1][0]+1;col++)
				{
					for(int row=rangeColNRow[0][1];row<rangeColNRow[1][1]+1;row++)
					{
						
						if(this.getValueOfCell(col, row).length()!=0)
							count++;
					}
				}
			
				return String.valueOf(count);
				
				
			}
			else
			{
				return "wrong range value";
			}
				
			
		}
		// ================================================when formual input is range ===============
		private String[] rangeDecode (String str , int formulaLength) throws Exception
		{
	
			String[] num ;
			
			int count =0;
	
			if(str.length()<=formulaLength)
					throw new Exception();
			if(str.contains(")"))
				str=str.substring(str.indexOf('(')+1, str.length()-1);
			else
				str=str.substring(str.indexOf('(')+1, str.length());
			
			if(str.contains(":"))
			{
				int[][] rangeColNRow =  new int [2][2]; // first index [] - index of cell of formula, second index [][] - column,row
				String temp[]=str.split(":");
				for(int q=0;q<2;q++)
				{
					for(int p=0; p< 26;p++)
					{
						count ++;
						if(temp[q].contains(String.valueOf(colIndex[p])))
						{	
							rangeColNRow[q][0]=p;
							
							while(temp[q].contains(",")) //throw out garbage "," that came in when double click
								temp[q]=temp[q].replace(",", "");
							rangeColNRow[q][1]=Integer.parseInt(temp[q].replace(String.valueOf(colIndex[p]),""))-1;
							break;
						}
					
					}
				}
				count = (rangeColNRow[1][0]-rangeColNRow[0][0]+1)*(rangeColNRow[1][1]-rangeColNRow[0][1]+1);
				num= new String[count];
				count=0;
				for(int col=rangeColNRow[0][0]; col<rangeColNRow[1][0]+1;col++)
				{
					for(int row=rangeColNRow[0][1];row<rangeColNRow[1][1]+1;row++)
					{
						num[count]=this.getValueOfCell(col, row);
						
						count++;
					}
				}
			
				return num;
				
				
			}
			
			//error case 
			throw new Exception();
			
			
		}
		//===========================================make formula new formula for calculate=====================
		private String[] decodeString( String  str , int formulaLength)
		{
	
			String num[];
			if(str.length()<=formulaLength)
				str+="0";
			if(str.contains(")"))
				num=str.substring(str.indexOf('(')+1, str.length()-1).split(",");
			else
				num=str.substring(str.indexOf('(')+1, str.length()).split(",");
				
			
				for(int i =0 ; i < num.length;i++)
				{
					for(int p=0; p< 26;p++)
					{
						if(num[i].contains(String.valueOf(colIndex[p])))
						{
							if(num[i].length()!=1)
							{
							num[i]=this.getValueOfCell(p,Integer.parseInt(num[i].replace(String.valueOf(colIndex[p]),""))-1);
							if(num[i]=="")
								num[i]="0";
							
							break;
							}
							if(num[i].length()==1)
								num[i]="0";
						}
						
					}
					if(num[i].equals(""))
						num[i]="0";
				}
				if(num[num.length-1].contains(")"))
						num[num.length-1].replace(")","");
			
			return num;
		}
		
		
//================================================change cell name to value===============================
		private String remakeFormulaIndexToValue(String str)
		{
			for(int n=0; n< OpenController.C;n++)
			{
				for( int m =0; m< OpenController.R;m++)
				{
					while(str.contains(String.valueOf(colIndex[n])+String.valueOf(m)))
						{
								if(this.getValueOfCell(n, m-1)=="")
									str=str.replace(String.valueOf(colIndex[n])+String.valueOf(m),"0");	
								str=str.replace(String.valueOf(colIndex[n])+String.valueOf(m), this.getValueOfCell(n, m-1));
						}
				}
			}
			return str;
		}
	}
