package application;

import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.prefs.Preferences;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class StartPomorodoController {

	@FXML
	Label lbTimer;
	@FXML
	Button btnPlayAndPause;
	@FXML
	HBox hboxBottom;
	@FXML
	HBox hboxCenter;
	@FXML
	HBox hboxTop;
	@FXML
	Spinner spinnerProjects;
	
	int playPauseTracker = 1;
	TimeKeeper timer;
	Toolkit toolKit;
	
	String project = "All Pomorodo";
	/**
	 * This button action will let you see your settings and lets you affect them
	 * you can change period amounts
	 * @param event
	 */
	public void changeToSettings(ActionEvent event){
		timer.stop();
		Parent loader = null;
		try {
			loader = FXMLLoader.load(getClass().getResource("/fxml/Settings.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene scene = new Scene(loader);
		//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
		
	}
	
	/**
	 * This button will allow the user to see a visiual for how much time they have been working
	 * @param event
	 */
	public void btnSeeGraph(ActionEvent event){
		timer.save();
		timer.stop();
		Parent loader = null;
		try {
			loader = FXMLLoader.load(getClass().getResource("/fxml/Chart.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene scene = new Scene(loader);
		//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}
	
	/**
	 * This will set the values for the timer and will set the right time for the timer
	 */
	public void initialize(){
		Preferences pref = Preferences.userRoot();
		Calendar cal = Calendar.getInstance();

		String today = cal.get(Calendar.MONTH) + " " + cal.get(Calendar.DAY_OF_MONTH);
		String firstTimeUsing = "FirstTimeUsing";
		String todaysDate = "todaysDate";
		String lastTimeUsedDayOfYear = "lastTimeUsedDayOfYear";
		String lastTimeUsedDayOfWeek = "lastTimeUsedDayOfWeek";
		String lastTimeUsedWeekOfYear = "lastTimeUsedWeekOfYear";
		String lastTimeUsedYear = "lastTimeUsedYear";
				
		//this will only run the first time you run the program
		if(pref.getBoolean(firstTimeUsing, true) == true){
			pref.putBoolean(firstTimeUsing, false);
			pref.put(todaysDate, today);
			pref.putInt(lastTimeUsedDayOfWeek, cal.get(Calendar.DAY_OF_WEEK));
			pref.putInt(lastTimeUsedWeekOfYear, cal.get(Calendar.WEEK_OF_YEAR));
			pref.putInt(lastTimeUsedDayOfYear, cal.get(Calendar.DAY_OF_YEAR));
			pref.putInt(lastTimeUsedYear, cal.get(Calendar.YEAR));
		}
		
		//will resety the information when you first start the app on a different dzy
		if(!today.equals(pref.get(todaysDate, "Nope"))){
			
			String[] projectsList = pref.get("projects", "All Pomorodo").split(",");
			
			//TODO DO WE NEED TO SAVE THIS CONSIDERING WE ALREADY SAVED THAT OTHER TIME
			//this saves the values before we delete them
			for(int i = 0; i < projectsList.length; i++){
				pref.putInt((projectsList[i] + " Total"), pref.getInt(projectsList[i], 0));
				pref.putInt((projectsList[i] + " " + pref.getInt(lastTimeUsedDayOfWeek, 0)), pref.getInt(projectsList[i], 0));	
			}

		//WILL CHECK IF YOU MISSED A DAY AND SET THE VALUES TO ZERO FOR THE DAYS MISSED IN THE WEEK
			if(((cal.get(Calendar.DAY_OF_YEAR) - pref.getInt(lastTimeUsedDayOfYear, 0)) < 7) && (cal.get(Calendar.YEAR) == pref.getInt(lastTimeUsedYear, 0))){
				for(int j = 0; j < projectsList.length; j++){
					for(int i = ((cal.get(Calendar.DAY_OF_YEAR) - pref.getInt(lastTimeUsedDayOfYear, 0)) - 1); i > 0; i--){
						pref.putInt((projectsList[j] + " " + (Calendar.DAY_OF_WEEK - i)), 0);
					}
				}
			}
			if(cal.get(Calendar.YEAR) != pref.getInt(lastTimeUsedYear, 0)){
				if((365 - cal.get(Calendar.YEAR)) < 7){
					for(int j = 0; j < projectsList.length; j++){
						for(int i = ((cal.get(Calendar.DAY_OF_WEEK) - pref.getInt(lastTimeUsedDayOfWeek, 0)) - 1); i > 0; i--){
							pref.putInt((projectsList[j] + " " + (Calendar.DAY_OF_WEEK - i)), 0);
						}
					}
				}
			}
		//WILL CHECK IF YOU MISSED A DAY AND SET THE VALUES TO ZERO FOR THE DAYS MISSED IN THE WEEK
			
		//section will focus on reseting the values incase you are in a new week
			if((cal.get(Calendar.WEEK_OF_YEAR) != pref.getInt(lastTimeUsedWeekOfYear, 0)) && (cal.get(Calendar.DAY_OF_YEAR) > 6)){
				for(int j = 0; j < projectsList.length; j++){
					for(int i = 0; i < 7; i++){
						pref.putInt((projectsList[j] + " " + i), 0);
					}
				}
				pref.putInt(lastTimeUsedWeekOfYear, cal.get(Calendar.WEEK_OF_YEAR));
			}
		//section will focus on reseting the values incase you are in a new week
			
			//Will set all the values for today to zero so a fresh start
			for(int i = 0; i < projectsList.length; i++){
				pref.putInt(projectsList[i], 0);
			}
			
			pref.put(todaysDate, today);
			pref.putInt(lastTimeUsedDayOfWeek, cal.get(Calendar.DAY_OF_WEEK));
			pref.putInt(lastTimeUsedWeekOfYear, cal.get(Calendar.WEEK_OF_YEAR));
			pref.putInt(lastTimeUsedDayOfYear, cal.get(Calendar.DAY_OF_YEAR));
			pref.putInt(lastTimeUsedYear, cal.get(Calendar.YEAR));
		}
		
		toolKit = Toolkit.getDefaultToolkit();
		timer = new TimeKeeper(lbTimer, pref.get("continousMode", "Yes"), hboxTop, hboxCenter, hboxBottom, spinnerProjects, btnPlayAndPause);
		
		//this will handle the spinner
		String[] projectsList = pref.get("projects", "All Pomorodo").split(",");
		ObservableList<String> projects = FXCollections.observableArrayList(projectsList);
		SpinnerValueFactory<String> valueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<String>(projects);
		valueFactory.setValue(pref.get("CurrentProject", "All Pomorodo"));
		spinnerProjects.setValueFactory(valueFactory);
	    spinnerProjects.getStyleClass().add(spinnerProjects.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);
		
	    spinnerProjects.valueProperty().addListener((obs, oldValue, newValue) -> 
	    
	    project = timer.hardReset(newValue.toString())
	    
	    );
	    
	    //this will take care of the timer and set the time for which it is when resumed
		int seconds = (pref.getInt("workTimeDuration", 25) * 60);
		if(pref.getBoolean("resumeTimeBoolean", false) == true){
			seconds = (pref.getInt("resumeTime", 0));
			int workBreak = pref.getInt("resumeWhichTimerIsPlaying", 0);
			if(pref.getInt("resumeTime", 0) == 0){ 
				if(workBreak == 1){
					seconds = (pref.getInt("shortBreakDuration", 25) * 60);
				}else if(workBreak == 2){
					seconds = (pref.getInt("longBreakDuration", 25) * 60);
				}else{
					seconds = (pref.getInt("workTimeDuration", 25) * 60);
				}
			}
			if(pref.getInt("resumeTime", 0) == 0){
				timer.resume(seconds, workBreak);
			}else{
				timer.resume();
			}
			pref.putInt("resumeTime", 0);
		}
		
		//this puts the time in the correct format
		int minLeft = (seconds/60);
		int secondsLeft = (seconds - (minLeft * 60));
		if((seconds%60) == 0){
			lbTimer.setText(minLeft + ":" + "00");
		}else if(seconds == 0){
			lbTimer.setText("00:00");
		}else if(secondsLeft < 10){
			lbTimer.setText(minLeft + ":0" + secondsLeft);
		}else{
			lbTimer.setText(minLeft + ":" + secondsLeft);
		}
	}
	
	/**
	 * This is an action taht will be performed when the play button is pressed
	 * This wlll click the play and pause button
	 * @param e
	 */
	@FXML
	public void playPauseBtn(ActionEvent e){
		toolKit.beep();
		SetWindowSize();
		
		if(playPauseTracker == 1 || btnPlayAndPause.getText().equals("Play")){
			System.out.println("Play");
			
			spinnerProjects.setDisable(true);
			String color = "-fx-background-color: #00E676";
			hboxBottom.setStyle(color);
			hboxCenter.setStyle(color);
			hboxTop.setStyle(color);
			
			timer.playAndPause();
			btnPlayAndPause.setText("Pause");
			playPauseTracker *= -1;
		}else{
			System.out.println("Pause");
			
			spinnerProjects.setDisable(false);
			String color = "-fx-background-color: #FFFF00";
			hboxBottom.setStyle(color);
			hboxCenter.setStyle(color);
			hboxTop.setStyle(color);
			
			timer.playAndPause();
			btnPlayAndPause.setText("Play");
			playPauseTracker *= -1;
		}
	}
	
	/**
	 * This will handle the skip button actions
	 * skips to next period
	 * @param e
	 */
	@FXML
	public void skipBtn(ActionEvent e){
		toolKit.beep();
		System.out.println("Skip");
		SetWindowSize();
		timer.skip();
	}
	
	/**
	 * This button is the reset button
	 * this will reset the timer that is currently playing
	 * @param e
	 */
	@FXML
	public void resetBtn(ActionEvent e){
		toolKit.beep();
		System.out.println("Reset");
		timer.reset();
	}
	
	/**
	 * This class will handle the window resizing
	 */
	public void SetWindowSize(){
		//Scene scene = ((Node) e.getSource()).getScene();
		Scene scene = btnPlayAndPause.getScene();
		scene.widthProperty().addListener(new ChangeListener<Number>() {
		    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
		        System.out.println("Width: " + newSceneWidth);
		    }
		});
		scene.heightProperty().addListener(new ChangeListener<Number>() {
		    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
		        System.out.println("Height: " + newSceneHeight);
		        if(newSceneHeight.intValue() > 200.0 && newSceneHeight.intValue() < 350){
			        lbTimer.setFont(new Font("Cambria", (double) newSceneHeight - 150));
		        }
		    }
		});
	}
	
}
