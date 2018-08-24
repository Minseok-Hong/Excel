package application;


import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.Scanner;

import javafx.css.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class  OpenController
{

	static int R;
	static int C;
	static String[][][] arr;
	static String fileName="";
	@FXML
	private TextField rText;
	@FXML
	private Label result;
	@FXML
	private TextField cText; 	
	@FXML
	private Button btn;
	@FXML
	private Button btnGetFile;
	@FXML
	private GridPane mainGp;
	@FXML
	private TextField path;
	


	@FXML
	private void ButtonPressed(ActionEvent event)
	{
		try
		{
			
			R= Integer.parseInt(rText.getText());
			
			C= Integer.parseInt(cText.getText());
			
			try{
			
			Stage stage = (Stage) btn.getScene().getWindow();
			stage.close();
			Stage sheet = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("excel.fxml"));
			Scene scene = new Scene(root,800,550);
			scene.getStylesheets().add(getClass().getResource("application.css").toString());
			sheet.setTitle("Excel-homemade");
			sheet.setScene(scene);

			sheet.show();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		catch (NumberFormatException ex)
		{
			rText.setText("Enter amount");
			cText.setText("Enter amount");
			
		}
	}
	
	@FXML
	private void openButtonPressed(ActionEvent event)
	{
	
		
			fileName=path.getText();
			ReadFile rf = new ReadFile();
			try
			{
				arr = rf.readFile(fileName);
				
			}
			catch(Exception e)
			{
				path.setText("wrong path name");
			}
			C = rf.getColumLength();
			R = rf.getRowLength();
			
			
			
			Stage stage = (Stage) btnGetFile.getScene().getWindow();
			stage.close();
			Stage sheet = new Stage();
			Parent root;
			try {
				root = FXMLLoader.load(getClass().getResource("excel.fxml"));
			
			Scene scene = new Scene(root,1000,550);
			scene.getStylesheets().add(getClass().getResource("application.css").toString());
			sheet.setTitle("Excel-homemade");
			sheet.setScene(scene);

			sheet.show();
			}
			catch (IOException e) {
				System.err.println("wrong excvel fxml");
			}
			
			
	}
	



}
