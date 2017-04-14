package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import com.sun.javafx.scene.control.skin.LabeledText;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
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
	@FXML
	Spinner spinnerProject2;
	@FXML
	PieChart dailyActivities;
	
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
		
		//Preference names
		String projectsPrefString = "projects";
		String allPomorodoPrefString = "All Pomorodo";
		String currentProjectPrefString = "currentProject";
						
		String overAllTimePrefString = "Overall Time: ";
		String currentTimePrefString = "currentTime";
		String programmingPrefString = "Programming";
		String homeworkPrefString = "Homework";
		String workoutPrefString = "Workout";
				
				
		Preferences pref = Preferences.userRoot();
		labelOne.setText(overAllTimePrefString + (((pref.getInt(currentTimePrefString, 0)) + (pref.getInt(allPomorodoPrefString, 0)) + (pref.getInt(programmingPrefString, 0)) + (pref.getInt(homeworkPrefString, 0)) + (pref.getInt(workoutPrefString, 0)))/60));
		
		labelTwo.setText("Programming time: " + ((pref.getInt(programmingPrefString, 0))/60));
		labelThree.setText("Homework time: " + ((pref.getInt(homeworkPrefString, 0))/60));
		labelFour.setText("Workout time: " + ((pref.getInt(workoutPrefString, 0))/60));
		
		if(pref.get(currentProjectPrefString, allPomorodoPrefString).equals(programmingPrefString)){
			labelTwo.setText("Programming time: " + (((pref.getInt(programmingPrefString, 0))/60) + ((pref.getInt(currentTimePrefString, 0))/60)));			
		}else if(pref.get(currentProjectPrefString, allPomorodoPrefString).equals(homeworkPrefString)){
			labelThree.setText("Homework time: " + (((pref.getInt(homeworkPrefString, 0))/60) + ((pref.getInt(currentTimePrefString, 0))/60)));			
		}else if(pref.get(currentProjectPrefString, allPomorodoPrefString).equals(workoutPrefString)){
			labelFour.setText("Workout time: " + (((pref.getInt(workoutPrefString, 0))/60) + ((pref.getInt(currentTimePrefString, 0))/60)));			
		}
		
		ArrayList<PieChart.Data> stuff = new ArrayList<>();
		String[] projectsList = pref.get(projectsPrefString, allPomorodoPrefString).split(",");
		
		for(int i = 1; i < projectsList.length; i++){
			PieChart.Data data = null;
			if(pref.get(currentProjectPrefString, allPomorodoPrefString).equals(projectsList[i])){
				data = new PieChart.Data(projectsList[i], ((pref.getInt(projectsList[i], 0) + (pref.getInt(currentTimePrefString, 0)))/60));	
			}else{
				data = new PieChart.Data(projectsList[i], (pref.getInt(projectsList[i], 0)/60));				
			}
			stuff.add(data);
		}
				
		int today = 24 * 60;
		int used = ((((pref.getInt(currentTimePrefString, 0)) + (pref.getInt(allPomorodoPrefString, 0)) + (pref.getInt(programmingPrefString, 0)) + (pref.getInt(homeworkPrefString, 0)) + (pref.getInt(workoutPrefString, 0)))));
		int hoursNotUsing = today - used;
		
		PieChart.Data data = new PieChart.Data("Mintues Not In Use", hoursNotUsing);
		stuff.add(data);
		
		//pie chart
		ObservableList<PieChart.Data> details = FXCollections.observableArrayList(stuff);
		
		dailyActivities.setData(details);
		dailyActivities.setTitle("Daily Progress");
		dailyActivities.setLegendSide(Side.BOTTOM);
		
		
		//spinner
		ObservableList<String> projects = FXCollections.observableArrayList(projectsList);
		SpinnerValueFactory<String> valueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<String>(projects);
		valueFactory.setValue(pref.get(currentProjectPrefString, allPomorodoPrefString));
		spinnerProject2.setValueFactory(valueFactory);
		spinnerProject2.getStyleClass().add(spinnerProject2.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);
		
		spinnerProject2.valueProperty().addListener((obs, oldValue, newValue) -> 
	    
	    	makeVisible(newValue.toString())
		
	    );
	}
	
	public void makeVisible(String newValue){
		//if(!newValue.equals("All Pomorodo")){
		    labelTwo.setVisible(false);
		    labelThree.setVisible(false);
		    labelFour.setVisible(false);

		    String two = "Programming";
		    String three = "Workout";
		    String four = "Homework";
		    
		    if(two.equals(newValue)){
		    	labelTwo.setVisible(true);
		    }else if(three.equals(newValue)){
			    labelThree.setVisible(true);
		    }else if(four.equals(newValue)){
			    labelFour.setVisible(true);
		    }else{
		    	labelTwo.setVisible(true);	
			    labelThree.setVisible(true);
			    labelFour.setVisible(true);
		    }
		//}
	}
}
