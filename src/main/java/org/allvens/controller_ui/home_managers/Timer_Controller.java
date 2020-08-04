package org.allvens.controller_ui.home_managers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class Timer_Controller {

    private double timer_time;

    private int time_tracker = 0;
    private int total_time_tracker = 0;

    private Timeline timer;
    private UIEffects_Manager uiE;
    private boolean timerFinished = false;

    public void set_UIEffect(UIEffects_Manager uiE){
        this.uiE = uiE;
    }

    private void set_Time(double time){
        uiE.set_initialTimerTime(time);
        timer_time = time;
    }

    public boolean get_timerFinished(){
        return timerFinished;
    }

    public void create_timer(double time){
        timerFinished = false;
        set_Time(time);

        timer = new Timeline();
        timer.setCycleCount(Timeline.INDEFINITE);
        KeyFrame frame = new KeyFrame(Duration.seconds(1), event -> {

            uiE.update_TimerValue(timer_time);

            if(timer_time > 0){
                uiE.update_ProgressBar(timer_time);
                time_tracker();
                timer_time--;
            }else{
                uiE.update_ProgressBar(0);
                uiE.set_Screen(5);
                stop_timer();
                timerFinished = true;
            }
        });
        timer.getKeyFrames().add(frame);
    }

    public void restart_TimerValues(){
        time_tracker = 0;
        uiE.closeEndingScreen();
    }

    public void pause_timer(){
        timer.pause();
    }

    public void resume_timer(){
        timer.play();
    }

    public void stop_timer(){
        timer.stop();
    }

    public void add_5minToTimer(){
        timer.stop();
        timer_time += 300;
        uiE.add_5MinToTimer();
        create_timer(timer_time);
    }

    private void time_tracker(){
        time_tracker++;
        total_time_tracker++;
    }

    public int get_TrackedTime(){
        return time_tracker;
    }
    public int get_TotalTrackedTime(){
        return total_time_tracker;
    }
    public void set_Total_time_tracker(int total_time_tracker){
        this.total_time_tracker = total_time_tracker;
    }

    public void debug_Add10MinToTimer() {
        total_time_tracker += 10 * 60;
        System.out.println("Added 10 Mintues");
    }
}
