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

public class SaveController {
	
	private String fileName;
	@FXML
	private TextField nameText;
	@FXML
	private Button saveBtn;

	private WriteExcel write = new WriteExcel();
	
	@FXML
	private void saveButtonPressed(ActionEvent event)
	{
		fileName=nameText.getText();
		
		try {
			write.saveFile(fileName, OpenController.C, OpenController.R, ExcelController.excel1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Stage stage = (Stage) saveBtn.getScene().getWindow();
		stage.close();
		
		
	}
	
	public String getFileName()
	{
		return fileName;
	}
	
		
			
	

}
