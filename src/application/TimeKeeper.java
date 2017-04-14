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
		pref.putInt("currentTime", currentTime);
	}
	
	/**
	 * This method sets the values incase the app is exited and come back
	 */
	public void setValues(){
		pref = Preferences.userRoot();
		longBreak = (pref.getInt("longBreakDuration", 10) * 60);
		shortBreak = (pref.getInt("shortBreakDuration", 5) * 60);
		workTime = (pref.getInt("workTimeDuration", 25) * 60);
		amountOfCyclesTillLongBreak = pref.getInt("amountOfCyclesTillLongBreak", 3);
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
						if(pref.getBoolean("resume", false) == false){
							timerTimeTracker = shortBreak;
						}else{
							pref.putBoolean("resume", false);
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
								if(contiousMode.equals("Yes")){
									playAndPause();
								}else{
									spinnerProjects.setDisable(false);
									hboxBottom.setStyle("-fx-background-color: #FFFF00");
									hboxCenter.setStyle("-fx-background-color: #FFFF00");
									hboxTop.setStyle("-fx-background-color: #FFFF00");
									btnPlayAndPause.setText("Play");
								}
							}
							
						}
					});
					longBreakTimeLine.getKeyFrames().add(frame);
					longBreakTimeLine.playFromStart();
				}else{
					
					if(paused == false){

						if(pref.getBoolean("resume", false) == false){
							timerTimeTracker = longBreak;
						}else{
							pref.putBoolean("resume", false);
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
			            		System.out.println("Done");
			            		shortBreakTimeLine.stop();
								if(contiousMode.equals("Yes")){
									playAndPause();
								}else{
									spinnerProjects.setDisable(false);
									hboxBottom.setStyle("-fx-background-color: #FFFF00");
									hboxCenter.setStyle("-fx-background-color: #FFFF00");
									hboxTop.setStyle("-fx-background-color: #FFFF00");
									btnPlayAndPause.setText("Play");
								}
			            	}
						}
					});
					shortBreakTimeLine.getKeyFrames().add(frame);
					shortBreakTimeLine.playFromStart();
				}
			}else{
				
				if(paused == false){

					if(pref.getBoolean("resume", false) == false){
						timerTimeTracker = workTime;
					}else{
						pref.putBoolean("resume", false);
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
		            		System.out.println("Done");
		            		workTimeLIne.stop();
							if(contiousMode.equals("Yes")){
								playAndPause();
							}else{
								spinnerProjects.setDisable(false);
								hboxBottom.setStyle("-fx-background-color: #FFFF00");
								hboxCenter.setStyle("-fx-background-color: #FFFF00");
								hboxTop.setStyle("-fx-background-color: #FFFF00");
								btnPlayAndPause.setText("Play");
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
		pref.putBoolean("Used", false);
		pref.put("CurrentProject", newVAlue);
		
		btnPlayAndPause.setText("Play");
		hboxBottom.setStyle("-fx-background-color: #FFFF00");
		hboxCenter.setStyle("-fx-background-color: #FFFF00");
		hboxTop.setStyle("-fx-background-color: #FFFF00");

		pref.putInt(project, (pref.getInt(project, 0) + currentTime));
		pref.putInt((project + " Total"), (pref.getInt(project, 0) + currentTime));
		pref.putInt((project + " " + date.getDay()), (pref.getInt(project, 0) + currentTime));

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
		pref.putBoolean("resumeTimeBoolean", true);
		pref.putInt("resumeTime", timerTimeTracker);
		pref.putInt("resumeWhichTimerIsPlaying", whichTimerIsPlaying);
		pref.putInt("lengthTillLongBreakTracker", lengthTillLongBreakTracker);
		pref.putInt("resumebreakOrWork", breakOrWork);
		pref.putInt("resumecurrentTime", currentTime);
		pref.putBoolean("resume", true);
		
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
		pref.putBoolean("resumeTimeBoolean", false);
		timerTimeTracker = pref.getInt("resumeTime", timerTimeTracker);
		whichTimerIsPlaying = pref.getInt("resumeWhichTimerIsPlaying", whichTimerIsPlaying);
		lengthTillLongBreakTracker = pref.getInt("lengthTillLongBreakTracker", lengthTillLongBreakTracker);
		breakOrWork = pref.getInt("resumebreakOrWork", breakOrWork);
		currentTime = pref.getInt("resumecurrentTime", currentTime);
	}
	
	/**
	 * This method runs only if resume is run when there is no time left to automatically start on the next 
	 * @param seconds
	 * @param workBreak
	 */
	public void resume(int seconds, int workBreak){
		pref.putBoolean("resumeTimeBoolean", false);
		timerTimeTracker = seconds;
		whichTimerIsPlaying = workBreak;
		lengthTillLongBreakTracker = pref.getInt("lengthTillLongBreakTracker", lengthTillLongBreakTracker);
		breakOrWork = pref.getInt("resumebreakOrWork", breakOrWork);
		currentTime = pref.getInt("resumecurrentTime", currentTime);
	}
}
