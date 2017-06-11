package application;

import java.awt.Toolkit;
import java.io.IOException;
import java.util.Calendar;
import java.util.prefs.Preferences;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class StartPomorodoController {

	@FXML
	BorderPane borderPaneAll;
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
	Spinner<String> spinnerProjects;
	
	private int playPauseTracker = 1;
	private TimeKeeper timer;
	private Toolkit toolKit;
	
	/**
	 * set values of timer
	 * saves values and resets if things change
	 */
	public void initialize(){
		
		Preferences pref = Preferences.userRoot();
		Calendar cal = Calendar.getInstance();
		
		//Preference names
		String continouseModePrefString = "continousMode";
		String longBreakDurationPrefString = "longBreakDuration";
		String shortBreakDurationPrefString = "shortBreakDuration";
		String workTimeDurationPrefString = "workTimeDuration";
		String projectsPrefString = "projects";
		String allPomorodoPrefString = "All Pomorodo";
		String currentProjectPrefString = "currentProject";
		
		String firstTimeUsingPrefString = "FirstTimeUsing";
		String todaysDatePrefString = "todaysDate";
		
		String lastTimeUsedDayOfYearPrefString = "lastTimeUsedDayOfYear";
		String lastTimeUsedDayOfWeekPrefString = "lastTimeUsedDayOfWeek";
		String lastTimeUsedWeekOfYearPrefString = "lastTimeUsedWeekOfYear";
		String lastTimeUsedYearPrefString = "lastTimeUsedYear";
		
		String resumeTimeBooleanPrefString = "resumeTimeBoolean";
		String resumeTimePrefString = "resumeTime";
		String resumeWhichTimerIsPlayingPrefString = "resumeWhichTimerIsPlaying";
		
		//Common Keywords
		String yesKeyWord = "Yes";
		String commaKeyWord = ",";
		String spaceKeyWord = " ";
		
		String today = cal.get(Calendar.MONTH) + spaceKeyWord + cal.get(Calendar.DAY_OF_MONTH);
		String[] projectsList = pref.get(projectsPrefString, allPomorodoPrefString).split(commaKeyWord);
		
		
		//this will only run the first time you run the program
		if(pref.getBoolean(firstTimeUsingPrefString, true) == true){
			pref.putBoolean(firstTimeUsingPrefString, false);
			pref.put(todaysDatePrefString, today);
			pref.putInt(lastTimeUsedDayOfWeekPrefString, cal.get(Calendar.DAY_OF_WEEK));
			pref.putInt(lastTimeUsedWeekOfYearPrefString, cal.get(Calendar.WEEK_OF_YEAR));
			pref.putInt(lastTimeUsedDayOfYearPrefString, cal.get(Calendar.DAY_OF_YEAR));
			pref.putInt(lastTimeUsedYearPrefString, cal.get(Calendar.YEAR));
		}
		
		
		//will reset the information when you first start the application on a different day
		if(!today.equals(pref.get(todaysDatePrefString, "Nope"))){
			
			
		//SECTION WILL FOCUS ON RESTING THE VALUES INCASE YOU ARE IN A NEW WEEK
			if((cal.get(Calendar.WEEK_OF_YEAR) != pref.getInt(lastTimeUsedWeekOfYearPrefString, 0)) && (cal.get(Calendar.DAY_OF_YEAR) > 6)){
				for(int j = 0; j < projectsList.length; j++){
					for(int i = 0; i < 8; i++){
						pref.putInt((projectsList[j] + spaceKeyWord + i), 0);
					}
				}
				pref.putInt(lastTimeUsedWeekOfYearPrefString, cal.get(Calendar.WEEK_OF_YEAR));
			}
		//SECTION WILL FOCUS ON RESTING THE VALUES INCASE YOU ARE IN A NEW WEEK
			
			
			//Will set all the values for today to zero so a fresh start
			for(int i = 0; i < projectsList.length; i++){
				pref.putInt(projectsList[i], 0);
			}
			
			pref.put(todaysDatePrefString, today);
			pref.putInt(lastTimeUsedDayOfWeekPrefString, cal.get(Calendar.DAY_OF_WEEK));
			pref.putInt(lastTimeUsedWeekOfYearPrefString, cal.get(Calendar.WEEK_OF_YEAR));
			pref.putInt(lastTimeUsedDayOfYearPrefString, cal.get(Calendar.DAY_OF_YEAR));
			pref.putInt(lastTimeUsedYearPrefString, cal.get(Calendar.YEAR));
		}
		
		toolKit = Toolkit.getDefaultToolkit();
		timer = new TimeKeeper(lbTimer, pref.get(continouseModePrefString, yesKeyWord), hboxTop, hboxCenter, hboxBottom, spinnerProjects, btnPlayAndPause);
		
		
		//this will handle the spinner
		ObservableList<String> projects = FXCollections.observableArrayList(projectsList);
		SpinnerValueFactory<String> valueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<String>(projects);
		valueFactory.setValue(pref.get(currentProjectPrefString, allPomorodoPrefString));
		spinnerProjects.setValueFactory(valueFactory);
		spinnerProjects.getStyleClass().add(spinnerProjects.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);
		spinnerProjects.valueProperty().addListener((obs, oldValue, newValue) -> timer.hardReset(newValue.toString()));
		
		
		//this will take care of the timer and set the time for which it is when resumed
		int currentSecondsForTimer = (pref.getInt(workTimeDurationPrefString, 25) * 60);
		if(pref.getBoolean(resumeTimeBooleanPrefString, false) == true){
			currentSecondsForTimer = (pref.getInt(resumeTimePrefString, 0));
			int whichTimerPeriodIsPlaying = pref.getInt(resumeWhichTimerIsPlayingPrefString, 0);
			if(pref.getInt(resumeTimePrefString, 0) == 0){ 
				if(whichTimerPeriodIsPlaying == 1){
					currentSecondsForTimer = (pref.getInt(shortBreakDurationPrefString, 25) * 60);
				}else if(whichTimerPeriodIsPlaying == 2){
					currentSecondsForTimer = (pref.getInt(longBreakDurationPrefString, 25) * 60);
				}else{
					currentSecondsForTimer = (pref.getInt(workTimeDurationPrefString, 25) * 60);
				}
			}
			if(pref.getInt(resumeTimePrefString, 0) == 0){
				timer.resume(currentSecondsForTimer, whichTimerPeriodIsPlaying);
			}else{
				timer.resume();
			}
			pref.putInt(resumeTimePrefString, 0);
		}
		
		
		//Change the label for the time in the correct format
		int minLeft = (currentSecondsForTimer/60);
		int secondsLeft = (currentSecondsForTimer - (minLeft * 60));
		if((currentSecondsForTimer % 60) == 0){
			lbTimer.setText(minLeft + ":00");
		}else if(currentSecondsForTimer == 0){
			lbTimer.setText("00:00");
		}else if(secondsLeft < 10){
			lbTimer.setText(minLeft + ":0" + secondsLeft);
		}else{
			lbTimer.setText(minLeft + ":" + secondsLeft);
		}
	}
	
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
		timer.stop();
		Parent loader = null;
		try {
			loader = FXMLLoader.load(getClass().getResource("/fxml/Chart_One.fxml"));
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
	 * This is an method will be performed when the play button is pressed
	 * @param e
	 */
	@FXML
	public void playPauseBtn(ActionEvent e){
		String play = "Play";
		String pause = "Pause";
		String playColor = "-fx-background-color: #00E676";
		String pauseColor = "-fx-background-color: #FFFF00";
		
		toolKit.beep();
		SetWindowSize(e);
		
		if(playPauseTracker == 1 || btnPlayAndPause.getText().equals(play)){
			spinnerProjects.setDisable(true);
			hboxBottom.setStyle(playColor);
			hboxCenter.setStyle(playColor);
			hboxTop.setStyle(playColor);
			
			timer.playAndPause();
			btnPlayAndPause.setText(pause);
			playPauseTracker *= -1;
		}else{
			spinnerProjects.setDisable(false);
			hboxBottom.setStyle(pauseColor);
			hboxCenter.setStyle(pauseColor);
			hboxTop.setStyle(pauseColor);
			
			timer.playAndPause();
			btnPlayAndPause.setText(play);
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
		timer.reset();
	}
	
	/**
	 * This method will add five min to the current work or break period
	 * @param e
	 */
	@FXML
	public void addFiveMin(ActionEvent e){
		toolKit.beep();
		timer.addFiveMinutes();
	}
	/**
	 * This will reset the numbers and move the borderpane in order to keep the numbers in the middle
	 */
	public void SetWindowSize(ActionEvent e){
		//Scene scene = ((Node) e.getSource()).getScene();
		Scene scene = btnPlayAndPause.getScene();
		
		//TODO we should try to make an algerithm where instead of moving right away it does it into a slower transition
		//TODO also we should find a way to better center it (the numbers are in the middle so transitioning should be easier

		//Handles the height by changing the size of the font, and also changing the position of the hbox so it stays centered
		scene.widthProperty().addListener(new ChangeListener<Number>() {
		    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
		        System.out.println("Width: " + newSceneWidth);
		        if(newSceneWidth.intValue() < 500){
		        	borderPaneAll.setLayoutX((-1 * newSceneWidth.intValue())/2);
		        }else{
		        	borderPaneAll.setLayoutX(0);
		        }
		        
		        /**
		        if(newSceneWidth.intValue() > 200.0 && newSceneWidth.intValue() < 350){
			        lbTimer.setFont(new Font("Verdana", (double) newSceneWidth - 150));
		        }
		        **/
		    }
		});
		
		//Handles the height by changing the size of the font, and also changing the position of the hbox so it stays centered
		scene.heightProperty().addListener(new ChangeListener<Number>() {
		    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
		        System.out.println("Height: " + newSceneHeight);
		        if(newSceneHeight.intValue() < 400){
		        	borderPaneAll.setLayoutY(( -1 * newSceneHeight.intValue())/2);
		        }else{
		        	borderPaneAll.setLayoutY(0);
		        }
		        
		        if(newSceneHeight.intValue() > 200.0 && newSceneHeight.intValue() < 350){
			        lbTimer.setFont(new Font("Verdana", (double) newSceneHeight - 150));
		        }
		    }
		});
		

		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			
			/**
			 * Handles the key events
			 * TODO the plus sign doesnt do anything yet
			 */
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case DIGIT5:
                    	addFiveMin(e);
                    	break;
                    case NUMPAD5:
                    	addFiveMin(e);
                    	break;
                    case P:
                    	playPauseBtn(e);
                    	break;
                    case R:
                    	resetBtn(e);
                   		break;
                    case N:
                    	skipBtn(e);
                    	break;
                }
            }
        });
	}
	
}
