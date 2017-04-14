package application;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.Preferences;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class TimeKeeper {

	Toolkit toolkit;
	
	//time of how long a long break will take
	int longBreak;
	//time of how long a short break will take
	int shortBreak;
	//time of how long a work session will take
	int workTime;
	
	//this will change depending on which break or period it is
	int timerTimeTracker;
	
	//switches to negative to positive depending whether there is a break or not
	int breakOrWork = -1;
	//this is just the current time at which we are in
	int currentTime = 0;
	//Which timer is playing
	int whichTimerIsPlaying = 0;

	//this keeps track of the time till a long break
	int amountOfCyclesTillLongBreak = 3;
	//this is keeping tack till long break
	int lengthTillLongBreakTracker = 3;

	String contiousMode;
	String project = "All Pomorodo";
	boolean playing = false;
	boolean paused = false;
	
	Label lbTimer;
	Button btnPlayAndPause;
	HBox hboxTop;
	HBox hboxCenter;
	HBox hboxBottom;
	Spinner spinnerProjects;

	Timeline longBreakTimeLine;
	Timeline shortBreakTimeLine;
	Timeline workTimeLIne;
	
	Preferences pref;
	//Preference names
	String longBreakDurationPrefString = "longBreakDuration";
	String shortBreakDurationPrefString = "shortBreakDuration";
	String workTimeDurationPrefString = "workTimeDuration";
	String currentProjectPrefString = "currentProject";
	String currentTimePrefString = "currentTime";
	

    String resumeTimeBooleanPrefString = "resumeTimeBoolean";
    String resumeTimePrefString = "resumeTime";
    String resumeWhichTimerIsPlayingPrefString = "resumeWhichTimerIsPlaying";
    String lengthTillLongBreakTrackerPrefString = "lengthTillLongBreakTracker";
	String resumebreakOrWorkPrefString = "resumebreakOrWork";
	String resumecurrentTimePrefString = "resumecurrentTime";
	String resumePrefString = "resume";
	String amountOfCyclesTillLongBreakPrefString = "amountOfCyclesTillLongBreak";
					
	//Common Keywords
	String yesKeyWord = "Yes";
	String spaceKeyWord = " ";
	String totalKeyWord = " Total";

	String play = "Play";
	String pauseColor = "-fx-background-color: #FFFF00";
	
	/**
	 * This is teh constructor that lets you pass in values that we will be using
	 * @param lbTimer the label we will be editing
	 * @param contiousMode whether continouse mode is on or not
	 */
	TimeKeeper(Label lbTimer, String contiousMode, HBox hboxTop, HBox hboxCenter, HBox hboxBottom, Spinner spinnerProjects, Button btnPlayAndPause){
		this.btnPlayAndPause = btnPlayAndPause;
		this.spinnerProjects = spinnerProjects;
		this.hboxTop = hboxTop;
		this.hboxCenter = hboxCenter;
		this.hboxBottom = hboxBottom;
		this.contiousMode = contiousMode;
		this.lbTimer = lbTimer;
		toolkit = Toolkit.getDefaultToolkit();
		setValues();
	}
	
	/**
	 * This method will update the label
	 * @param seconds
	 */
	public void updateValues(int seconds){
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
		pref.putInt(currentTimePrefString, currentTime);
	}
	
	/**
	 * This method sets the values incase the app is exited and come back
	 */
	public void setValues(){
		pref = Preferences.userRoot();
		longBreak = (pref.getInt(longBreakDurationPrefString, 10) * 60);
		shortBreak = (pref.getInt(shortBreakDurationPrefString, 5) * 60);
		workTime = (pref.getInt(workTimeDurationPrefString, 25) * 60);
		amountOfCyclesTillLongBreak = pref.getInt(amountOfCyclesTillLongBreakPrefString, 3);
		timerTimeTracker = workTime;
		lengthTillLongBreakTracker = amountOfCyclesTillLongBreak;
	}
	
	/**
	 * This method will handle the button presses, and will run the appropriate thread for the timer
	 * @param btnPlayAndPause
	 */
	public void playAndPause(){
		if(playing == false){
			if(breakOrWork == 1){
				if(lengthTillLongBreakTracker > 0){
					
					if(paused == false){
						if(pref.getBoolean(resumePrefString, false) == false){
							timerTimeTracker = shortBreak;
						}else{
							pref.putBoolean(resumePrefString, false);
						}
					}else{
						paused = false;
					}

					whichTimerIsPlaying = 1;
					playing = true;
					longBreakTimeLine = new Timeline();
					longBreakTimeLine.setCycleCount(Timeline.INDEFINITE);
					KeyFrame frame = new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
						
						@Override
						public void handle(ActionEvent event) {
							currentTime++;
							if(timerTimeTracker > 0){
								updateValues(timerTimeTracker);
								System.out.println(timerTimeTracker);
								timerTimeTracker--;
							}else{
								updateValues(timerTimeTracker);
			            		playing = false;
								breakOrWork *= -1;
								lengthTillLongBreakTracker--;
								toolkit.beep();
								longBreakTimeLine.stop();
								if(contiousMode.equals(yesKeyWord)){
									playAndPause();
								}else{
									spinnerProjects.setDisable(false);
									hboxBottom.setStyle(pauseColor);
									hboxCenter.setStyle(pauseColor);
									hboxTop.setStyle(pauseColor);
									btnPlayAndPause.setText(play);
								}
							}
							
						}
					});
					longBreakTimeLine.getKeyFrames().add(frame);
					longBreakTimeLine.playFromStart();
				}else{
					
					if(paused == false){

						if(pref.getBoolean(resumePrefString, false) == false){
							timerTimeTracker = longBreak;
						}else{
							pref.putBoolean(resumePrefString, false);
						}
					}else{
						paused = false;
					}

					lengthTillLongBreakTracker = amountOfCyclesTillLongBreak;
					whichTimerIsPlaying = 2;
					playing = true;
					shortBreakTimeLine = new Timeline();
					shortBreakTimeLine.setCycleCount(Timeline.INDEFINITE);
					KeyFrame frame = new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
						
						@Override
						public void handle(ActionEvent event) {
			            	currentTime++;
			            	if(timerTimeTracker > 0){
								updateValues(timerTimeTracker);
			            		System.out.println(timerTimeTracker);
			            		timerTimeTracker--;
			            	}else{
								updateValues(timerTimeTracker);
			            		playing = false;
			            		breakOrWork *= -1;
			            		toolkit.beep();
			            		shortBreakTimeLine.stop();
								if(contiousMode.equals(yesKeyWord)){
									playAndPause();
								}else{
									spinnerProjects.setDisable(false);
									hboxBottom.setStyle(pauseColor);
									hboxCenter.setStyle(pauseColor);
									hboxTop.setStyle(pauseColor);
									btnPlayAndPause.setText(play);
								}
			            	}
						}
					});
					shortBreakTimeLine.getKeyFrames().add(frame);
					shortBreakTimeLine.playFromStart();
				}
			}else{
				
				if(paused == false){

					if(pref.getBoolean(resumePrefString, false) == false){
						timerTimeTracker = workTime;
					}else{
						pref.putBoolean(resumePrefString, false);
					}
				}else{
					paused = true;
				}
				whichTimerIsPlaying = 3;
				playing = true;

				workTimeLIne = new Timeline();
				workTimeLIne.setCycleCount(Timeline.INDEFINITE);
				KeyFrame frame = new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
		            	currentTime++;
		            	if(timerTimeTracker > 0){
							updateValues(timerTimeTracker);
		            		System.out.println(timerTimeTracker);
		            		timerTimeTracker--;
		            	}else{
							updateValues(timerTimeTracker);
		            		playing = false;
		            		breakOrWork *= -1;
		            		toolkit.beep();
		            		workTimeLIne.stop();
							if(contiousMode.equals(yesKeyWord)){
								playAndPause();
							}else{
								spinnerProjects.setDisable(false);
								hboxBottom.setStyle(pauseColor);
								hboxCenter.setStyle(pauseColor);
								hboxTop.setStyle(pauseColor);
								btnPlayAndPause.setText(play);
							}
		            	}
					}
				});
				
				workTimeLIne.getKeyFrames().add(frame);
				workTimeLIne.playFromStart();
			}
		}else{
			if(whichTimerIsPlaying == 1){
				longBreakTimeLine.pause();
				paused = true;
			}else if(whichTimerIsPlaying == 2){
				shortBreakTimeLine.pause();
				paused = true;
			}else{
				workTimeLIne.pause();
				paused = true;
			}
			playing = false;
		}
	}
	
	/**
	 * This method will ahndle the skip button to skip the timer to the next period
	 */
	public void skip(){
		if(playing == true || paused == true){
			if(whichTimerIsPlaying == 1){
				longBreakTimeLine.stop();
				lengthTillLongBreakTracker--;
			}else if(whichTimerIsPlaying == 2){
				shortBreakTimeLine.stop();
				lengthTillLongBreakTracker = amountOfCyclesTillLongBreak;
			}else{
				workTimeLIne.stop();
			}
			playing = false;
			breakOrWork *= -1;
			paused = false;
			playAndPause();
		}else{
			System.out.println("Press Play First");
		}
	}
	
	/**
	 * this method resets the timer by canceling on and restarting it
	 */
	public void reset(){
		if(playing == true || paused == true){
			if(whichTimerIsPlaying == 1){
				currentTime = (currentTime - (longBreak - timerTimeTracker));
				longBreakTimeLine.stop();
			}else if(whichTimerIsPlaying == 2){
				currentTime = (currentTime - (shortBreak - timerTimeTracker));
				shortBreakTimeLine.stop();
			}else{
				currentTime = (currentTime - (workTime - timerTimeTracker));
				workTimeLIne.stop();
			}
			playing = false;
			paused = false;
			playAndPause();
		}else{
			System.out.println("Press Play First");
		}
	}
	
	public void save(){
		//pref.putInt(project, (pref.getInt(project, 0) + currentTime));
	}
	
	/**
	 * This method will only run if the projects are changed
	 * @param newVAlue
	 * @return
	 */
	public String hardReset(String newVAlue){
		Date date = new Date();
		pref.put(currentProjectPrefString, newVAlue);
		
		btnPlayAndPause.setText(play);
		hboxBottom.setStyle(pauseColor);
		hboxCenter.setStyle(pauseColor);
		hboxTop.setStyle(pauseColor);

		pref.putInt(project, (pref.getInt(project, 0) + currentTime));
		pref.putInt((project + totalKeyWord), (pref.getInt(project, 0) + currentTime));
		//TODO CHANGE THIS TO CALENDAR
		pref.putInt((project + spaceKeyWord + date.getDay()), (pref.getInt(project, 0) + currentTime));

		this.project = newVAlue;
		
		currentTime = 0;
		updateValues(workTime);
		
		if(playing == true || paused == true){
			if(whichTimerIsPlaying == 1){
				longBreakTimeLine.stop();
			}else if(whichTimerIsPlaying == 2){
				shortBreakTimeLine.stop();
			}else{
				workTimeLIne.stop();
			}
			playing = false;
			paused = false;
		}
		return newVAlue;
	}
	
	/**
	 * This class wills  stop the timer
	 * and save its current position
	 */
	public void stop(){
		pref.putBoolean(resumeTimeBooleanPrefString, true);
		pref.putInt(resumeTimePrefString, timerTimeTracker);
		pref.putInt(resumeWhichTimerIsPlayingPrefString, whichTimerIsPlaying);
		pref.putInt(lengthTillLongBreakTrackerPrefString, lengthTillLongBreakTracker);
		pref.putInt(resumebreakOrWorkPrefString, breakOrWork);
		pref.putInt(resumecurrentTimePrefString, currentTime);
		pref.putBoolean(resumePrefString, true);
		
		if(playing == true || paused == true){
			if(whichTimerIsPlaying == 1){
				longBreakTimeLine.stop();
			}else if(whichTimerIsPlaying == 2){
				shortBreakTimeLine.stop();
			}else{
				workTimeLIne.stop();
			}
			playing = false;
			paused = false;
		}else{
			System.out.println("Press Play First");
		}
	}
	
	/**
	 * this will keep the time
	 * this will keep which work or thing is
	 * and it will set all the values for them
	 */
	public void resume(){
		pref.putBoolean(resumeTimeBooleanPrefString, false);
		timerTimeTracker = pref.getInt(resumeTimePrefString, timerTimeTracker);
		whichTimerIsPlaying = pref.getInt(resumeWhichTimerIsPlayingPrefString, whichTimerIsPlaying);
		lengthTillLongBreakTracker = pref.getInt(lengthTillLongBreakTrackerPrefString, lengthTillLongBreakTracker);
		breakOrWork = pref.getInt(resumebreakOrWorkPrefString, breakOrWork);
		currentTime = pref.getInt(resumecurrentTimePrefString, currentTime);
	}
	
	/**
	 * This method runs only if resume is run when there is no time left to automatically start on the next 
	 * @param seconds
	 * @param workBreak
	 */
	public void resume(int seconds, int workBreak){
		pref.putBoolean(resumeTimeBooleanPrefString, false);
		timerTimeTracker = seconds;
		whichTimerIsPlaying = workBreak;
		lengthTillLongBreakTracker = pref.getInt(lengthTillLongBreakTrackerPrefString, lengthTillLongBreakTracker);
		breakOrWork = pref.getInt(resumebreakOrWorkPrefString, breakOrWork);
		currentTime = pref.getInt(resumecurrentTimePrefString, currentTime);
	}
}
