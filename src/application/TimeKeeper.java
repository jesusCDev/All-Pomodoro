package application;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class TimeKeeper {

	Toolkit toolkit;
	//time of how long a long break will take
	int longBreak = 10;
	//time of how long a short break will take
	int shortBreak = 5;
	//time of how long a work session will take
	int workTime = 25;
	
	//this will change depending on which break or period it is
	int timerTimeTracker = workTime;
	
	//Amount of sessions you have done already
	int workSession= 0;
	//switches to negative to positive depending whether there is a break or not
	int breakOrWork = -1;
	//this is just the current time at which we are in
	int currentTime = 0;
	//Which timer is playing
	int whichTimerIsPlaying = 0;

	//this keeps track of the time till a long break
	int lengthTillLongBreak = 3;
	//this is keeping tack till long break
	int lengthTillLongBreakTracker = 3;

	boolean playing = false;
	boolean paused = false;
	
	Label lbTimer;

	Timeline longBreakTimeLine;
	Timeline shortBreakTimeLine;
	Timeline workTimeLIne;
	
	TimeKeeper(Label lbTimer){
		this.lbTimer = lbTimer;
		toolkit = Toolkit.getDefaultToolkit();
		setValues();
	}
	
	public void updateValues(int time){
		lbTimer.setText(Integer.toString(time));
	}
	
	public void setValues(){
		try {
			BufferedReader br = new BufferedReader(new FileReader("info.txt"));
			longBreak = Integer.parseInt(br.readLine());
			shortBreak = Integer.parseInt(br.readLine());
			workTime = Integer.parseInt(br.readLine());
			lengthTillLongBreak = Integer.parseInt(br.readLine());
			timerTimeTracker = workTime;
			lengthTillLongBreakTracker = lengthTillLongBreak;
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void playAndPause(){
		if(playing == false){
			if(breakOrWork == 1){
				if(lengthTillLongBreakTracker > 0){
					
					if(paused == false){
						timerTimeTracker = shortBreak;
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
								timerTimeTracker--;
								updateValues(timerTimeTracker);
								System.out.println(timerTimeTracker);
							}else{
			            		playing = false;
								breakOrWork *= -1;
								lengthTillLongBreakTracker--;
								toolkit.beep();
								longBreakTimeLine.stop();
								playAndPause();
							}
							
						}
					});
					longBreakTimeLine.getKeyFrames().add(frame);
					longBreakTimeLine.playFromStart();
				}else{
					
					if(paused == false){
						timerTimeTracker = longBreak;
					}else{
						paused = false;
					}

					lengthTillLongBreakTracker = lengthTillLongBreak;
					whichTimerIsPlaying = 2;
					playing = true;
					shortBreakTimeLine = new Timeline();
					shortBreakTimeLine.setCycleCount(Timeline.INDEFINITE);
					KeyFrame frame = new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
						
						@Override
						public void handle(ActionEvent event) {
			            	currentTime++;
			            	if(timerTimeTracker > 0){
			            		timerTimeTracker--;
								updateValues(timerTimeTracker);
			            		System.out.println(timerTimeTracker);
			            	}else{
			            		playing = false;
			            		breakOrWork *= -1;
			            		toolkit.beep();
			            		System.out.println("Done");
			            		shortBreakTimeLine.stop();
			            		playAndPause();
			            	}
						}
					});
					shortBreakTimeLine.getKeyFrames().add(frame);
					shortBreakTimeLine.playFromStart();
				}
			}else{
				
				if(paused == false){
					timerTimeTracker = workTime;
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
		            		timerTimeTracker--;
							updateValues(timerTimeTracker);
		            		System.out.println(timerTimeTracker);
		            	}else{
		            		playing = false;
		            		breakOrWork *= -1;
		            		toolkit.beep();
		            		System.out.println("Done");
		            		workTimeLIne.stop();
		            		playAndPause();
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
	
	public void skip(){
		if(playing == true || paused == true){
			if(whichTimerIsPlaying == 1){
				longBreakTimeLine.stop();
			}else if(whichTimerIsPlaying == 2){
				shortBreakTimeLine.stop();
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
	
	public void reset(){
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
			playAndPause();
		}else{
			System.out.println("Press Play First");
		}
	}
}
