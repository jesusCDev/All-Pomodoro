package application;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class TimeKeeper {

	Timer timer;
	Toolkit toolkit;
	//time of how long a long break will take
	int longBreak = 10;
	//time of how long a short break will take
	int shortBreak = 5;
	//time of how long a work session will take
	int workTime = 25;
	
	//Amount of sessions you have done already
	int workSession= 0;
	//switches to negative to positive depending whether there is a break or not
	int breakOrWork = 1;
	//this keeps track of the time till a long break
	int lengthTillLongBreak = 3;
	//
	int tillBreak = 3;
	//this is just the current time at which we are in
	int currentTime = 0;
	//this will change depending on which break or period it is
	int timerTime = longBreak;

	boolean playing = false;
	
	TimeKeeper(){
		timer = new Timer();
		toolkit = Toolkit.getDefaultToolkit();
		setValues();
	}
	
	public void setValues(){
		try {
			BufferedReader br = new BufferedReader(new FileReader("info.txt"));
			longBreak = Integer.parseInt(br.readLine());
			shortBreak = Integer.parseInt(br.readLine());
			workTime = Integer.parseInt(br.readLine());
			lengthTillLongBreak = Integer.parseInt(br.readLine());
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		timerTime = longBreak;
		tillBreak = lengthTillLongBreak;
	}
	
	public void playAndPause(){
		if(playing == false){
			if(breakOrWork == 1){
				if(tillBreak > 0){
					playing = true;
					timerTime = shortBreak;
					
					timer.schedule(new TimerTask() {
						@Override
						public void run() {
							currentTime++;
							if(timerTime > 0){
								timerTime--;
							}else{
			            		playing = false;
								breakOrWork *= -1;
								tillBreak--;
								toolkit.beep();
								timer.cancel();
							}
						}
					}, 0, 1000);
				}else{
					playing = true;
					tillBreak = lengthTillLongBreak;
					timerTime = longBreak;
					
					timer.schedule(new TimerTask(){
						@Override
						public void run(){
			            	currentTime++;
			            	if(timerTime > 0){
			            		timerTime--;
			            	}else{
			            		playing = false;
			            		breakOrWork *= -1;
			            		toolkit.beep();
			            		timer.cancel();
			            	}
							
						}
					}, 0, 1000);
				}
			}else{
				timerTime = workTime;
				playing = true;
				
				timer.schedule(new TimerTask(){
					@Override
					public void run(){
		            	currentTime++;
		            	if(timerTime > 0){
		            		timerTime--;
		            	}else{
		            		playing = false;
		            		breakOrWork *= -1;
		            		toolkit.beep();
		            		timer.cancel();
		            	}
					}
				}, 0, 1000);
			}
		}else{
			timer.cancel();
		}
	}
	
	public void skip(){
		if(playing == true){
			timer.cancel();
			breakOrWork *= -1;
			playAndPause();
		}else{
			System.out.println("Press Play First");
		}
	}
	
	public void reset(){
		if(playing == true){
			timer.cancel();
			playAndPause();
		}else{
			System.out.println("Press Play First");
		}
	}
}
