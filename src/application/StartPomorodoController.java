package application;

import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
		
		toolKit = Toolkit.getDefaultToolkit();

		timer = new TimeKeeper(lbTimer, pref.get("continousMode", "Yes"), hboxTop, hboxCenter, hboxBottom, spinnerProjects, btnPlayAndPause);
		
		String[] projectsList = pref.get("projects", "All Pomorodo,wordTwo,wordThree,wordFour,wordFive,wordSix").split(",");
		ObservableList<String> projects = FXCollections.observableArrayList(projectsList);
		SpinnerValueFactory<String> valueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<String>(projects);
		valueFactory.setValue(pref.get("CurrentProject", "All Pomorodo"));
		spinnerProjects.setValueFactory(valueFactory);
	    spinnerProjects.getStyleClass().add(spinnerProjects.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);
		
	    spinnerProjects.valueProperty().addListener((obs, oldValue, newValue) -> 
	    
	    project = timer.hardReset(newValue.toString())
	    
	    );   
	    
		int seconds;
		if(pref.getInt("resumeTime", 0) != 0){
			seconds = (pref.getInt("resumeTime", 0));
			timer.resume();
			pref.putInt("resumeTime", 0);
		}else{
			seconds = (pref.getInt("workTimeDuration", 25) * 60);
		}
		
		//displayTime
		
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
		System.out.println("Play/Pause");
		if(playPauseTracker == 1 || btnPlayAndPause.getText().equals("Pause")){
			spinnerProjects.setDisable(true);
			hboxBottom.setStyle("-fx-background-color: #00E676");
			hboxCenter.setStyle("-fx-background-color: #00E676");
			hboxTop.setStyle("-fx-background-color: #00E676");
			
			timer.playAndPause();
			btnPlayAndPause.setText("Play");
			playPauseTracker *= -1;
		}else{
			spinnerProjects.setDisable(false);
			hboxBottom.setStyle("-fx-background-color: #FFFF00");
			hboxCenter.setStyle("-fx-background-color: #FFFF00");
			hboxTop.setStyle("-fx-background-color: #FFFF00");
			
			timer.playAndPause();
			btnPlayAndPause.setText("Pause");
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
