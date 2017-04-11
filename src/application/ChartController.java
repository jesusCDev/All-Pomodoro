package application;

import java.io.IOException;
import java.util.prefs.Preferences;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ChartController {

	@FXML
	Label labelOne;
	@FXML
	Label labelTwo;
	@FXML
	Label labelThree;
	@FXML
	Label labelFour;
	
	public void goBackToMainMenu(ActionEvent e){
		Parent loader = null;
		try {
			loader = FXMLLoader.load(getClass().getResource("/fxml/StartPomorodo.fxml"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Scene scene = new Scene(loader);
		scene.getStylesheets().add(getClass().getResource("/style/startPomorodo.css").toExternalForm());
		Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		stage.setAlwaysOnTop(true);
		stage.setMinWidth(250.0);
		stage.setMinHeight(200.0);
		stage.setScene(scene);
		stage.show();
	}
	
	public void initialize(){
		Preferences pref = Preferences.userRoot();
		
		labelOne.setText("OverAllTime time: " + ((pref.getInt("currentTime", 0))/60));
		labelTwo.setText("Programming time: " + ((pref.getInt("Programming", 0))/60));
		labelThree.setText("Homework time: " + ((pref.getInt("Homework", 0))/60));
		labelFour.setText("Workout time: " + ((pref.getInt("Workout", 0))/60));
		
		if(pref.get("CurrentProject", "All Pomorodo").equals("Programming")){
			labelTwo.setText("Programming time: " + (((pref.getInt("Programming", 0))/60) + ((pref.getInt("currentTime", 0))/60)));			
		}else if(pref.get("CurrentProject", "All Pomorodo").equals("Homework")){
			labelThree.setText("Homework time: " + (((pref.getInt("Homework", 0))/60) + ((pref.getInt("currentTime", 0))/60)));			
		}else if(pref.get("CurrentProject", "All Pomorodo").equals("Workout")){
			labelFour.setText("Workout time: " + (((pref.getInt("Workout", 0))/60) + ((pref.getInt("currentTime", 0))/60)));			
		}
	}
}
