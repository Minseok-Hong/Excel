package application;

import java.io.IOException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.*;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;



	
public class ExcelController {

	

	static Excel excel1; 
	private TextField[][] tf;
	private MenuBar save = new MenuBar () ;
	private Menu m1 = new Menu ("File");
	private Menu m2 = new Menu ("edit");
	private Label lbFormula = new Label();
	private TextField formula = new TextField ();
	private MenuItem saveMenu =new MenuItem("save");
	private MenuItem clearMenu =new MenuItem("clear");
	private MenuItem closeMenu =new MenuItem("close");
	private int i; // for textfield loop
	private int t; // for textfield loop
	private long time1=0,time2=0; //for double click check
	private int[] beforeFcs = new int[2];//focus before focus move
	private int resultFlag=0; //flag to show formula textfield result
	private int functionMoveFlag=0; //flag to tell fucntion enter is end
	private int focusC,focusR; // indexes of  focused textfield
	@FXML
	private ScrollPane mainGp;
	
	private boolean doubleC; //flag for double click
	
	public void initialize(){
		GridPane gp = makeExcelGridPane(OpenController.C, OpenController.R);
		mainGp.setContent(gp);
		mainGp.setMinSize(300, 30);
		
	}
	
	//to make excel grid =============================================================================
	public GridPane makeExcelGridPane(int c, int r){
		

		char[] columnIndex =   {'/','A','B','C','D','E','F','G','H','I','J','K',
				'L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'}; 
		Label [] lbRowIndex;
		Label [] lbcolumnIndex;
		
		tf = new TextField[c][r];
		lbRowIndex = new Label[r];
		lbcolumnIndex =new Label[c+1];
	
		  	
		m1.getItems().addAll(saveMenu,closeMenu);
		m2.getItems().addAll(clearMenu);
		save.getMenus().addAll(m1,m2);
		
		excel1= new Excel (c,r);
		
		save.setPrefSize(80*(c+1), 30);

		lbFormula.setPrefSize(80, 30);	
		lbFormula.setText("f(x)");	
	
		save.setMinSize(80, 30);
		lbFormula.setMinSize(80, 30);
		formula.setStyle("-fx-border-color: skyblue;");
		
		GridPane gp = new GridPane();
		gp.add(save, 0, 0,c+1,1);
		gp.add(lbFormula, 0,1);
		if(c>2)
			gp.add(formula,1,1,c,1);
		else
			gp.add(formula,1,1,3,1);
			
		  
		  // make index of column and row =====================================
		  for(int z=0; z<c+1;z++){
			  lbcolumnIndex[z] = new Label();
			  lbcolumnIndex[z].setAlignment(Pos.CENTER);
			  lbcolumnIndex[z].setMinSize(80, 30);
			  lbcolumnIndex[z].setText(String.valueOf(columnIndex[z]));
			  gp.add(lbcolumnIndex[z], z, 2);
		  }
		  for(int z=0; z<r;z++){
			  lbRowIndex[z] = new Label();
			  lbRowIndex[z].setAlignment(Pos.CENTER);
			  lbRowIndex[z].setPrefSize(80, 30);
			  lbRowIndex[z].setText(String.valueOf(z+1));
			  gp.add(lbRowIndex[z], 0,z+3);
		  }
		  
		  
		  //formula event handler, enter value by formula textfield==========================================
		  formula.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() 
		  {
              @Override
	              
	              public void handle(KeyEvent event) 
              	  {
	            	  resultFlag=0;
	            	 
	            	  if(formula.isFocused())
	            	  {
	            		  tf[focusC][focusR].setText(formula.getText());
	            		  excel1.setValueOfCell(focusC,focusR,tf[focusC][focusR].getText());
	            		  enterFormula ( event, focusC, focusR);
	            		  
	            	  }
	            	  	textFlooding (gp);
	                 	formula.positionCaret(formula.getText().length());
	              }
	          });
			  
		  //===save menu active========================================
		  saveMenu.setOnAction(new EventHandler<ActionEvent>()
		  {
			    
			  @Override public void handle(ActionEvent e) 
			  {
			    	Stage sheet = new Stage();
					Parent root;
					try 
					{
						
						root = FXMLLoader.load(getClass().getResource("save.fxml"));
						Scene scene = new Scene(root,550,150);
						sheet.setTitle("save excelfile");
						sheet.setScene(scene);
						sheet.show();
					}
					
					catch (IOException e2) {
						
						System.err.println("wrong excvel fxml");
					}
					
			  }
			});
		//===clear menu active========================================
		  clearMenu.setOnAction(new EventHandler<ActionEvent>() 
		  {
			    @Override public void handle(ActionEvent e) 
			    {
			        for(int col=0; col<OpenController.C;col++)
			        {
			        	for(int row =0; row < OpenController.R;row++)
			        	{
			        		excel1.setValueOfCell(col, row, "");
			        		excel1.resetFormulaOfCell(col, row);
			        		tf[col][row].setText("");
			        	}
			        }
			    }
			});
		  
		//===close menu active========================================
		  closeMenu.setOnAction(new EventHandler<ActionEvent>() 
		  {
			    @Override public void handle(ActionEvent e) {
			    	System.exit(0);

			    }
		  });

		
		  //textField setting==============================================
		  for( i=0; i<c; i++)
		  {
			  for( t=0; t<r; t++)
			  {
				  tf[i][t] = new TextField();
				  tf[i][t].setPrefSize(80, 30);
				  tf[i][t].setStyle("-fx-border-color: skyblue;");
				  gp.add(tf[i][t], i+1, t+3);
				  
				  if(OpenController.fileName.length()>1)
				  {
					  tf[i][t].setText(OpenController.arr[i][t][0]);
					  excel1.setValueOfCell(i, t, OpenController.arr[i][t][0]);
					  
					  if(OpenController.arr[i][t][1].contains("="))
						  excel1.setValueOfCell(i, t, OpenController.arr[i][t][1]);
				  }
				  
				  //====reset textfield size after flooding ============================
				  tf[i][t].focusedProperty().addListener(new ChangeListener<Boolean>(){
					  @Override
					  public void changed(ObservableValue<? extends Boolean> observable , Boolean oldValue, Boolean newValue)
					  {
						  
						  for(int z =0;z<c;z++){
							  for(int y =0;y<r;y++){
								  gp.setColumnSpan(tf[z][y], 1);
								  tf[z][y].setVisible(true);
							  }
						  }
						  textFlooding(gp);
					  }});
				  
				  
				  //====set formula's value by cell ============================
				  tf[i][t].textProperty().addListener(new ChangeListener<String>(){

					public void changed(ObservableValue<? extends String> observable, String oldValue,
							String newValue) {
						if(resultFlag==0)
						{
							formula.setText(newValue); // set formula text, make cell equal with formula text.
						}
						for(int z =0;z<c;z++){
							  for(int y =0;y<r;y++){
								  if(tf[z][y].isFocused()) // when any textfield focused, set new focusC ,R
								  {
									  focusC=z;
									  focusR=y;
									  
								  }
							  }
						  }
						  	
					}
					  });
				  
				  //====when key pressed in textfield ============================
				  tf[i][t].addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                      @Override
                      public void handle(KeyEvent event) {	
                      }
				  }
				  );
				  
				  
				  tf[i][t].addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() 
				  {
                      @Override
                      
                      public void handle(KeyEvent event) {
                    	  
                    	 resultFlag=0;
                    	int c = findFocus()/1000;
                    	int r = findFocus()%1000;
                    	
                    	for(int col=0;col<OpenController.C;col++)
                    	{
                    		 for(int row=0; row<OpenController.R;row++)
                    		 {
                    			 if(tf[col][row].isFocused())
                      			 {
                      				excel1.setValueOfCell(col, row, tf[col][row].getText());
                      				
                      				if(excel1.getFormulaOfCell(col, row).contains("=")&&
                      						!excel1.getFormulaOfCell(col, row).contains(":"))
                      					excel1.setValueOfCell(col, row, excel1.Calculate(col, row));
                      				if(!(tf[col][row].getText().contains("=")))
                      						excel1.resetFormulaOfCell(col, row);
                      			 }
                    			 if(!tf[col][row].isFocused())
                    				 tf[col][row].setText(excel1.getValueOfCell(col, row));
                    		 }
                    	}
                    		enterFormula ( event,  c,  r);
                    	
                    	   textFlooding (gp);
                         	 
                      }
                  });
				 
				 


		            
				  //클릭으로  formula에값 입력====================================
				  tf[i][t].addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() 
				  {
                      @Override
                      
                      public void handle(MouseEvent mouseEvent) {
	                    	  TextField f = (TextField) mouseEvent.getSource();
	                    	  f.positionCaret(f.getText().length());
	                    	  resultFlag=0;
	                    	  if(!doubleC)
	                    	  {
		                    	  if((formula.getText().contains("=sum("))&&!(f.getText().contains("=sum")))
		                    	  {
		                    		  if(formula.getText().contains(")"))
		                    			  formula.setText(formula.getText().replace(")",""));
			                          formula.appendText(","+f.getText());
			                          if(formula.getText().contains("(,"))
			                        	  formula.setText(formula.getText().replace("(,","("));
			                        	  
		                    	  }
		                    	  else if((formula.getText().contains("=average("))&&!(f.getText().contains("=average(")))
		                    	  {
		                    		  if(formula.getText().contains(")"))
		                    			  formula.setText(formula.getText().replace(")",""));
		                    		  formula.appendText(","+f.getText());
			                          if(formula.getText().contains("(,"))
			                        	  formula.setText(formula.getText().replace("(,","("));
		                    	  }
		                    	  else if((formula.getText().contains("=counta("))&&!(f.getText().contains("=counta(")))
		                    	  {
		                    		  if(formula.getText().contains(")"))
		                    			  formula.setText(formula.getText().replace(")",""));
		                    		  formula.appendText(","+f.getText());
			                          if(formula.getText().contains("(,"))
			                        	  formula.setText(formula.getText().replace("(,","("));
		                    	  }
		                    	  
		                    	  else
		                    	  {
		                    		  focusC=findFocus()/1000;
		                    		  focusR=findFocus()%1000;
		                    		  formula.setText(f.getText());
		                    	  }
	                    	  }
                    	  }
                  });
				  
				  //==============double click check
				  tf[i][t].setOnMousePressed(new EventHandler<MouseEvent>() {
					     @Override
					     public void handle(MouseEvent event) {
					    	 
					          time2=System.currentTimeMillis();
					        	 
					          if((time2-time1)<200&&time2!=0&&time1!=0) // double click
					          {
					        	  doubleC=true;
					        	  time1=time2;
					        	  
					          }
					          
					          else
					          {
					        	  doubleC=false;	//not double click
					        	  time1=time2;
					          }
					          
					          
					          
					          if(doubleC)
					          {
						          TextField f = (TextField) event.getSource();					        
						          f.requestFocus();
						          hideFormula();
					          }        
					     }
					});
			//=======================enter value by click, exit formula by double click===========================================
				  tf[i][t].addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
					  @Override
					  
					  public void handle(MouseEvent mouse) {
						  resultFlag=0;
						  TextField f = (TextField) mouse.getSource();
						  
						 
		        		  for(int col=0;col<OpenController.C;col++)
		                  {
		                	  for(int row =0; row < OpenController.R;row++)
		                		  if(f!=tf[col][row]&&!doubleC)   // enter value by click,when user click unfocused cell and not double click
		                		  {
			                		  if(tf[col][row].getText().contains("=sum(")||tf[col][row].getText().contains("=average("))
			                		  {
			                			  beforeFcs[0]=col;
			                			  beforeFcs[1]=row;
			                			  functionMoveFlag=1;
			                			  
			                			  if(tf[col][row].getText().contains(")"))
			                				  tf[col][row].setText(tf[col][row].getText().replace(")",""));
			                			  
			                			  tf[col][row].appendText(","+f.getText());
			                			  
			                			  if(tf[col][row].getText().contains("(,"))
			                				  tf[col][row].setText(formula.getText().replace("(,","("));
			                			  
			                			  tf[col][row].requestFocus();
			                			  hideFormula();
			                			  tf[col][row].positionCaret(tf[col][row].getText().length());
			                			  excel1.setValueOfCell(col, row, tf[col][row].getText());
			                		  }
			                		  else if(tf[col][row].getText().contains("="))
			                		  {
			                			  beforeFcs[0]=col;
			                			  beforeFcs[1]=row;
			                			  functionMoveFlag=1;
			                			  tf[col][row].appendText(f.getText());
			                			  tf[col][row].requestFocus();
			                			  hideFormula();
			                			  tf[col][row].positionCaret(tf[col][row].getText().length());
			                			  excel1.setValueOfCell(col, row, tf[col][row].getText());
			                		  }
			                		  else
			                		  {	
			                			  //not enter formula, set cell that is clicked focused.
			                			  focusC=findFocus()/1000;
			                    		  focusR=findFocus()%1000;
			                    		  
			                		  }
		                		  }
		                	  
			                	  else if(f!=tf[col][row])
			                	  {		
			                		  int fcs=findFocus();
		                	  		  focusC=fcs/1000;
			                    	  focusR=fcs%1000;
			                    	  
			                    		if(functionMoveFlag==1)//더블클릭시 불필요하게 들어가는 ,값들 제거
			                    		{
			                    			if(excel1.getFormulaOfCell(beforeFcs[0], beforeFcs[1]).contains(","))
			                    			excel1.setValueOfCell(beforeFcs[0],beforeFcs[1],
			                    				excel1.getFormulaOfCell(beforeFcs[0],beforeFcs[1]).
			                    					substring(0,excel1.getFormulaOfCell(beforeFcs[0], beforeFcs[1]).lastIndexOf(",")));
			                    			
			                    			excel1.setValueOfCell(beforeFcs[0],beforeFcs[1],excel1.Calculate(beforeFcs[0], beforeFcs[1]));
			                    			functionMoveFlag=0;
			                    			
			                    		}
			                    		
			                    		if(excel1.getFormulaOfCell(fcs/1000, fcs%1000).contains("="))
			                    			tf[fcs/1000][fcs%1000].setText(excel1.getFormulaOfCell(fcs/1000, fcs%1000));
			                    		else
			                    			tf[fcs/1000][fcs%1000].setText(excel1.getValueOfCell(fcs/1000, fcs%1000));
			                    		formula.setText(tf[focusC][focusR].getText());
			                    		hideFormula();
			                	  }
		                	  
		                  }
		        		  
		        		  if(!(excel1.getFormulaOfCell(findFocus()/1000,findFocus()%1000).contains(f.getText()))&&
		        		  	excel1.getFormulaOfCell(findFocus()/1000,findFocus()%1000).contains("="))
		        			  
						  {
							  tf[findFocus()/1000][findFocus()%1000].setText
							  (excel1.getFormulaOfCell(findFocus()/1000,findFocus()%1000));
							  hideFormula();
						  }
		        		  
		        		  textFlooding(gp);
		              }

		          }); 
			  }//반복문 끝 ====================================================
		  }

		  return gp;
	  }
	//===========================================end make Gridpane==================
	
	
	//======================================method, when textfield overflow=======================
	public void textFlooding (GridPane gp)
	{
		for(int l =0;l<OpenController.C;l++)
		{
    		 for(int u =0; u<OpenController.R;u++)
    		 {	 
    			 if(tf[l][u].isFocused()||(l==focusC&&u==focusR))
    			 {
                    	 if(l+tf[l][u].getText().length()/8<OpenController.C)
                    	 {
                    		 for(int col=0; col<OpenController.C;col++){
                    			 for(int row=0; row<OpenController.R;row++){
                    				 tf[col][row].setVisible(true);
                    			 }
                    		 }
                    		 
	                    		 gp.setColumnSpan(tf[l][u], 1+tf[l][u].getText().length()/8);
	                    		 int colnum ; colnum=tf[l][u].getText().length()/8;
	                    		
	                    		 for(;colnum>0;colnum--)
	                    		 {
	                    			 if(tf[l+colnum][u].isVisible())
	                    			 tf[l+colnum][u].setVisible(false);
	                    		 
	                    		 }
                     }
    			 }
    		 }		 
    	 }
	}
	//=======================================find now focused========================================
	public int findFocus()
	{
		 for(int l =0;l<OpenController.C;l++){
    		 for(int u =0; u<OpenController.R;u++){
    			
    			if(tf[l][u].isFocused())
    				return l*1000+u;  // 1000으로 나누면 몫은 행 나머지는 열이나옴.
    			
    		 }
		 }
		 return 0;
	}
	//=======================================hide formula for unfocused textfield====================================
	public void hideFormula()
	{
	
		 for(int l =0;l<OpenController.C;l++){
    		 for(int u =0; u<OpenController.R;u++){
    			 if(tf[l][u].getText().contains("="))
    			 {
    				 excel1.setValueOfCell(l, u, tf[l][u].getText());
    				 excel1.setValueOfCell(l,u,excel1.Calculate(l, u));
    				
    			 }
    			 if(!tf[l][u].isFocused())
    			 {
    				
    				if(excel1.getFormulaOfCell(l, u).contains("="))
    					tf[l][u].setText(excel1.getValueOfCell(l, u));
    				else
    				tf[l][u].setText(excel1.getValueOfCell(l, u));
    			}
    		 }
		 }
		
		 
	}
		 
	//=======================================enter formula=====================================
	public void enterFormula (KeyEvent event, int c, int r)
	{
		String nowText = tf[c][r].getText();
		int nowL = tf[c][r].getText().length();
		
		if(nowText.contains("=sum(")||nowText.contains("=average(")||nowText.contains("=counta("))
		{
			if (event.getCode().equals(KeyCode.ENTER)||(((nowText.charAt(nowL-1)==')'))))
	        {
				if (event.getCode().equals(KeyCode.ENTER))
					{
						tf[c][r].setText(nowText.concat(")"));
						nowL++;
						nowText=nowText.concat(")");
						formula.requestFocus();
					}
	            excel1.setValueOfCell(c,r,nowText);
	            formula.setText(excel1.Calculate(c,r)); // )포함된 식이 보내짐
	            excel1.setValueOfCell(c,r,excel1.Calculate(c,r));
	            resultFlag=1;
	        }
				if(tf[c][r].getText().contains(")"))
				tf[c][r].setText(nowText.replace(")",""));
				
				
			
		}
		else if(nowText.contains("=")) //일반 수식
		{
			if (event.getCode().equals(KeyCode.ENTER))
	        {
				formula.setText(excel1.Calculate(c,r));
				tf[c][r].setText(excel1.Calculate(c,r));
				excel1.setValueOfCell(c,r,excel1.Calculate(c,r));
				resultFlag=1;
				formula.requestFocus();
				
	        }
		}
	}

	
}
