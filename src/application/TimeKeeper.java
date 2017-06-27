package application;

import java.awt.Toolkit;
import java.util.Calendar;
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

	private Toolkit toolkit;
	
	private int longBreakTimeDuration;
	private int shortBreakTimeDuration;
	private int workTimeDuration;
	private int extraTimeDuration = 5 *60;
	
	private int timeCountDownTracker;
	private	int breakOrWorkTracker = 1;
	private int overAllTimeCountedInCurrentProject = 0;
	private int whichTimerPeriodIsPlaying = 3;
	private int amountOfShortBreaksLeftTillLongBreak = 3;
	private int amountOfShortBreaksLeftTillLongBreakTracker = 3;

	private String contiousMode;
	private String currentProject = "All Pomorodo";
	private boolean playing = false;
	private boolean paused = false;
	private boolean timerDone = false;
	
	private Label lbTimer;
	private Button btnPlayAndPause;
	private HBox hboxTop;
	private HBox hboxCenter;
	private HBox hboxBottom;
	private Spinner<String> spinnerProjects;

	private Timeline timer;
	private Timeline extraTimeLine;
	
	private Preferences pref;
	//Preference names
	private String longBreakDurationPrefString = "longBreakDuration";
	private String shortBreakDurationPrefString = "shortBreakDuration";
	private String workTimeDurationPrefString = "workTimeDuration";
	private String currentProjectPrefString = "currentProject";

	private String allPomorodoPrefString = "All Pomorodo";
	private String overAllTimeCountedInCurrentProjectPrefString = "overAllTimeCountedInCurrentProject";
	private String totalTimeWorkingPrefString = "totalTimeWorking";

	private String resumeTimeBooleanPrefString = "resumeTimeBoolean";
    private String resumeTimePrefString = "resumeTime";
    private String resumeWhichTimerIsPlayingPrefString = "resumeWhichTimerIsPlaying";
    private String lengthTillLongBreakTrackerPrefString = "lengthTillLongBreakTracker";
    private String resumebreakOrWorkPrefString = "resumebreakOrWork";
	private String resumecurrentTimePrefString = "resumecurrentTime";
	private String resumePrefString = "resume";
	private String amountOfCyclesTillLongBreakPrefString = "amountOfCyclesTillLongBreak";
					
	//Common Keywords
	private String yesKeyWord = "Yes";
	private String spaceKeyWord = " ";
	private String totalKeyWord = " Total";

	private String play = "Play";
	private String pause = "Pause";
	private String playColor = "-fx-background-color: #00E676";
	private String pauseColor = "-fx-background-color: #FFFF00";
	
	/**
	 * Initializes all the values when timer is created
	 * @param lbTimer
	 * @param contiousMode
	 * @param hboxTop
	 * @param hboxCenter
	 * @param hboxBottom
	 * @param spinnerProjects
	 * @param btnPlayAndPause
	 */
	TimeKeeper(Label lbTimer, String contiousMode, HBox hboxTop, HBox hboxCenter, HBox hboxBottom, Spinner<String> spinnerProjects, Button btnPlayAndPause){
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
	 * This method sets the values incase the app is exited and come back
	 */
	private void setValues(){
		pref = Preferences.userRoot();
		
		longBreakTimeDuration = (pref.getInt(longBreakDurationPrefString, 10) * 60);
		shortBreakTimeDuration = (pref.getInt(shortBreakDurationPrefString, 5) * 60);
		workTimeDuration = (pref.getInt(workTimeDurationPrefString, 25) * 60);
		amountOfShortBreaksLeftTillLongBreak = pref.getInt(amountOfCyclesTillLongBreakPrefString, 3);
		
		timeCountDownTracker = workTimeDuration;
		amountOfShortBreaksLeftTillLongBreakTracker = amountOfShortBreaksLeftTillLongBreak;
	}
	
	//TODO RECHECK THIS
	/**
	 * This method will update the label
	 * @param seconds
	 */
	private void updateValues(int seconds){
		int minLeft = (seconds/60);
		int secondsLeft = (seconds - (minLeft * 60));
		if((seconds%60) == 0){
			lbTimer.setText(minLeft + ":00");
		}else if(seconds == 0){
			lbTimer.setText("00:00");
		}else if(secondsLeft < 10){
			lbTimer.setText(minLeft + ":0" + secondsLeft);
		}else{
			lbTimer.setText(minLeft + ":" + secondsLeft);
		}
		pref.putInt(overAllTimeCountedInCurrentProjectPrefString, overAllTimeCountedInCurrentProject);
	}
	
	/**
	 * This method will handle the button presses, and will run the appropriate thread for the timer
	 * @param btnPlayAndPause
	 */
	public void playAndPause(){
		
		if(playing == false){
			
			if(paused == false){
				if(pref.getBoolean(resumePrefString, false) == false){
					
					int timeDuration;
					
					if(whichTimerPeriodIsPlaying == 1){
						timeDuration = 	longBreakTimeDuration;
					}else if(whichTimerPeriodIsPlaying == 2){
						timeDuration = 	shortBreakTimeDuration;
					}else {
						timeDuration = 	workTimeDuration;
					}
					
					timeCountDownTracker = timeDuration;
				}else{
					pref.putBoolean(resumePrefString, false);
				}
			}else{
				paused = false;
			}
			
			timerDone = false;
			playing = true;
			
			timer = new Timeline();
			timer.setCycleCount(Timeline.INDEFINITE);
			KeyFrame frame = new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
				
				//TODO CONTINOUS TIMER THREAD
				@Override
				public void handle(ActionEvent event) {
					overAllTimeCountedInCurrentProject++;
					if(timeCountDownTracker > 0){
						updateValues(timeCountDownTracker);
						timeCountDownTracker--;
						System.out.println(timeCountDownTracker);
					}else{
						updateValues(timeCountDownTracker);
	            		playing = false;
	            		
	            		//use these to figure it out
	            		breakOrWorkTracker *= -1;
	            		
	            		if(breakOrWorkTracker == -1){
	            			amountOfShortBreaksLeftTillLongBreakTracker--;
	            			whichTimerPeriodIsPlaying = 1;
	            			if(amountOfShortBreaksLeftTillLongBreakTracker < 0){
		            			amountOfShortBreaksLeftTillLongBreakTracker = amountOfShortBreaksLeftTillLongBreak;
		            			whichTimerPeriodIsPlaying = 2;
		            		}
	            		}else{
	            			whichTimerPeriodIsPlaying = 3;
	            		}
	            		
						toolkit.beep();
						timer.stop();
						
						if(contiousMode.equals(yesKeyWord)){
							playAndPause();
						}else{
							System.out.println("Timer Finished");
							timerDone = true;
							spinnerProjects.setDisable(false);
							hboxBottom.setStyle(pauseColor);
							hboxCenter.setStyle(pauseColor);
							hboxTop.setStyle(pauseColor);
							btnPlayAndPause.setText(play);
						}
					}
				}
			});
			timer.getKeyFrames().add(frame);
			timer.playFromStart();
			
		}else{
			timer.pause();
			paused = true;
			playing = false;
		}
	}
	
	/**
	 * This method will ahndle the skip button to skip the timer to the next period
	 */
	public void skip(){
		if(playing == true || paused == true){
			timer.stop();
			
			breakOrWorkTracker *= -1;
			
			if(breakOrWorkTracker == -1){
    			amountOfShortBreaksLeftTillLongBreakTracker--;
    			whichTimerPeriodIsPlaying = 1;

    			if(amountOfShortBreaksLeftTillLongBreakTracker < 0){
        			amountOfShortBreaksLeftTillLongBreakTracker = amountOfShortBreaksLeftTillLongBreak;
        			whichTimerPeriodIsPlaying = 2;
        		}
    		}else{
    			whichTimerPeriodIsPlaying = 3;
    		}
			
			playing = false;
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
			
			int timeDuration;
			if(whichTimerPeriodIsPlaying == 1){
				timeDuration = 	longBreakTimeDuration;
			}else if(whichTimerPeriodIsPlaying == 2){
				timeDuration = 	shortBreakTimeDuration;
			}else {
				timeDuration = 	workTimeDuration;
			}
			
			overAllTimeCountedInCurrentProject = (overAllTimeCountedInCurrentProject - (timeDuration - timeCountDownTracker));
			timer.stop();
			playing = false;
			paused = false;
			playAndPause();
		}else{
			System.out.println("Press Play First");
		}
	}
	
	/**
	 * This method will only run if the projects are changed
	 * @param newVAlue
	 * @return
	 */
	public void hardReset(String newProjectName){
		
		btnPlayAndPause.setText(play);
		hboxBottom.setStyle(pauseColor);
		hboxCenter.setStyle(pauseColor);
		hboxTop.setStyle(pauseColor);
		
		
		pref.put(currentProjectPrefString, newProjectName);

		pref.putInt(currentProject, ((pref.getInt(currentProject, 0) + overAllTimeCountedInCurrentProject)));
		pref.putInt((currentProject + totalKeyWord), (pref.getInt((currentProject + totalKeyWord), 0) + overAllTimeCountedInCurrentProject));
		Calendar cal = Calendar.getInstance();
		pref.putInt((currentProject + spaceKeyWord + cal.get(Calendar.DAY_OF_WEEK)), ((pref.getInt((currentProject + spaceKeyWord + cal.get(Calendar.DAY_OF_WEEK)), 0) + overAllTimeCountedInCurrentProject)));
		
		pref.putInt(totalTimeWorkingPrefString, ((pref.getInt(totalTimeWorkingPrefString, 0) + overAllTimeCountedInCurrentProject)));

		currentProject = newProjectName;
		
		whichTimerPeriodIsPlaying = 3;
		//timeCountDownTracker = workTimeDuration;
		breakOrWorkTracker = 1;
		
		overAllTimeCountedInCurrentProject = 0;
		updateValues(workTimeDuration);
		
		if(playing == true || paused == true){       
			timer.stop();
			playing = false;
			paused = false;
		}
		
	}
	
	/**
	 * This method will stop the timer
	 * and save its current position
	 */
	public void stop(){
		pref.putBoolean(resumeTimeBooleanPrefString, true);
		pref.putInt(resumeTimePrefString, timeCountDownTracker);
		pref.putInt(resumeWhichTimerIsPlayingPrefString, whichTimerPeriodIsPlaying);
		pref.putInt(lengthTillLongBreakTrackerPrefString, amountOfShortBreaksLeftTillLongBreakTracker);
		pref.putInt(resumebreakOrWorkPrefString, breakOrWorkTracker);
		pref.putInt(resumecurrentTimePrefString, overAllTimeCountedInCurrentProject);
		pref.putBoolean(resumePrefString, true);
		
		if(playing == true || paused == true){
			timer.stop();
			
			playing = false;
			paused = false;
		}else{
			System.out.println("Press Play First");
		}
	}
	
	/**
	 * This method adds five mintues to the current work or break period
	 * TODO we have to find a way to add the mintues to the work or break period it was just in
	 */
	public void addFiveMinutes(){

		if(playing == true){
			timeCountDownTracker += 5*60;
		}
		
		if(timerDone == true){
			
			System.out.println("Extra Start");
			spinnerProjects.setDisable(true);
			hboxBottom.setStyle(playColor);
			hboxCenter.setStyle(playColor);
			hboxTop.setStyle(playColor);
			btnPlayAndPause.setText(pause);
			
			if(paused == false){
				if(pref.getBoolean(resumePrefString, false) == false){
					timeCountDownTracker = extraTimeDuration;
				}else{
					pref.putBoolean(resumePrefString, false);
				}
			}else{
				paused = true;
			}
			whichTimerPeriodIsPlaying = 4;
			playing = true;
			extraTimeLine = new Timeline();
			extraTimeLine.setCycleCount(Timeline.INDEFINITE);
			KeyFrame frame = new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					overAllTimeCountedInCurrentProject++;
	            	if(timeCountDownTracker > 0){
						updateValues(timeCountDownTracker);
	            		System.out.println(timeCountDownTracker);
	            		timeCountDownTracker--;
	            	}else{
						updateValues(timeCountDownTracker);
	            		playing = false;
	            		toolkit.beep();
	            		extraTimeLine.stop();
						if(contiousMode.equals(yesKeyWord)){
							playAndPause();
						}else{
							System.out.println("Work Finished");
							spinnerProjects.setDisable(false);
							hboxBottom.setStyle(pauseColor);
							hboxCenter.setStyle(pauseColor);
							hboxTop.setStyle(pauseColor);
							btnPlayAndPause.setText(play);
						}
	            	}
				}
			});
			extraTimeLine.getKeyFrames().add(frame);
			extraTimeLine.playFromStart();
		}
		
	}
	/**
	 * this will keep the time
	 * this will keep which work or thing is
	 * and it will set all the values for them
	 */
	public void resume(){
		pref.putBoolean(resumeTimeBooleanPrefString, false);
		timeCountDownTracker = pref.getInt(resumeTimePrefString, timeCountDownTracker);
		whichTimerPeriodIsPlaying = pref.getInt(resumeWhichTimerIsPlayingPrefString, whichTimerPeriodIsPlaying);
		amountOfShortBreaksLeftTillLongBreakTracker = pref.getInt(lengthTillLongBreakTrackerPrefString, amountOfShortBreaksLeftTillLongBreakTracker);
		breakOrWorkTracker = pref.getInt(resumebreakOrWorkPrefString, breakOrWorkTracker);
		overAllTimeCountedInCurrentProject = pref.getInt(resumecurrentTimePrefString, overAllTimeCountedInCurrentProject);
	}
	
	/**
	 * This method runs only if resume is run when there is no time left to automatically start on the next 
	 * @param seconds
	 * @param workBreak
	 */
	public void resume(int currentSecondsForTimer, int whichTimerPeriodIsPlaying){
		pref.putBoolean(resumeTimeBooleanPrefString, false);
		timeCountDownTracker = currentSecondsForTimer;
		this.whichTimerPeriodIsPlaying = whichTimerPeriodIsPlaying;
		amountOfShortBreaksLeftTillLongBreakTracker = pref.getInt(lengthTillLongBreakTrackerPrefString, amountOfShortBreaksLeftTillLongBreakTracker);
		breakOrWorkTracker = pref.getInt(resumebreakOrWorkPrefString, breakOrWorkTracker);
		overAllTimeCountedInCurrentProject = pref.getInt(resumecurrentTimePrefString, overAllTimeCountedInCurrentProject);
	}
}
