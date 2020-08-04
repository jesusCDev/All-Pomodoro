package org.allvens.controller_ui.home_managers;

import org.allvens.assets.Constants;
import org.allvens.assets.Constants_Prefs;
import org.allvens.controller_ui.Data_Manager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import java.awt.*;

public class UIEffects_Manager implements Constants {

    private BorderPane bpAll;
    private Label lbStatus;
    private Label lbTimer;
    private ProgressBar pbTimer;
    private Button btnPlayPause;

    private double timer_time;
    private boolean silentMode;

    private Toolkit toolKit;
    private Timeline blinkTimer;

    public UIEffects_Manager(BorderPane bpAll, Label lbStatus, Label lbTimer, ProgressBar pbTimer, Button btnPlayPause){
        this.bpAll = bpAll;
        this.lbStatus = lbStatus;
        this.lbTimer = lbTimer;
        this.pbTimer = pbTimer;
        this.btnPlayPause = btnPlayPause;

        Data_Manager data = new Data_Manager();
        silentMode = data.get_CheckBoxSettingValue(Constants_Prefs.SETTINGS_SilentMode);
        toolKit = Toolkit.getDefaultToolkit();
    }

    public int get_InitialTime(){
        return (int)timer_time;
    }

    public void set_initialTimerTime(double timer_time){
        this.timer_time = timer_time;
    }

    public void set_Screen(int status){
        int WORK_Screen = 1;
        int SHORT_Screen = 2;
        int LONG_Screen = 3;
        int BREAK_Screen = 4;

        if(status == WORK_Screen){
            set_PlayScreen();
        }else if(status == SHORT_Screen|| status == LONG_Screen){
            if(status == SHORT_Screen){
                set_BreakScreen(KEYWORD_ShortBreak);
            }else{
                set_BreakScreen(KEYWORD_LongBreak);
            }
        }else if(status == BREAK_Screen){
            set_PauseScreen();
        }else{
            set_EndingScreen();
        }
    }

    private void set_EndingScreen() {
        blinkTimer = new Timeline();
        blinkTimer.setCycleCount(Timeline.INDEFINITE);
        KeyFrame frame = new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            int timeIterator = 0;

            @Override
            public void handle(ActionEvent event) {
                timeIterator += 1;
                if ((timeIterator % 2) == 0) {
                    set_BasicUI(KEYWORD_Play, KEYWORD_Done, COLOR_Play);
                } else {
                    set_BasicUI(KEYWORD_Play, KEYWORD_Done, COLOR_Stop);
                }
                if ((timeIterator % 5) == 0) {
                    makeSound();
                }
            }
        });

        blinkTimer.getKeyFrames().add(frame);
        blinkTimer.playFromStart();
    }

    public void closeEndingScreen(){
        try{
            blinkTimer.stop();
        } catch (NullPointerException e){
        }
    }

    private void set_BreakScreen(String currentBreak){
        lbStatus.setText(currentBreak);
        set_BasicUI(KEYWORD_Pause, currentBreak, COLOR_Stop);
    }

    private void set_PauseScreen(){
        set_BasicUI(KEYWORD_Play,KEYWORD_Pause, COLOR_Pause);

    }

    private void set_PlayScreen(){
        lbStatus.setText(KEYWORD_Working);
        set_BasicUI(KEYWORD_Pause,KEYWORD_Working, COLOR_Play);
    }

    public void makeSound(){
        if(silentMode){
            toolKit.beep();
        }
    }

    private void set_BasicUI(String btnText, String status, String color){
        btnPlayPause.setText(btnText);
        lbStatus.setText(status);
        bpAll.setStyle(COLOR_CSS + color + KEYWORD_Semicolon);
    }

    public void add_5MinToTimer(){
        timer_time += 300;
    }

    public void update_ProgressBar(double time_seconds){
        double timeUsed;
        if(time_seconds != 0){
            timeUsed = (time_seconds / get_InitialTime());
        }else{
            timeUsed = 0;
        }
        pbTimer.setProgress(1 - timeUsed);
    }

    public void update_TimerValue(double time_seconds){
        lbTimer.setText(convert_ToTime(time_seconds));
    }

    private String convert_ToTime(double time_seconds){
        double time_minutes = Math.floor(time_seconds / 60.0);
        double time_seconds_left = Math.floor(time_seconds - (time_minutes * 60.0));
        return (int)time_minutes + KEYWORD_Colon + convert_SecondsIntToStr((int)time_seconds_left);
    }

    private String convert_SecondsIntToStr(int seconds){
        StringBuilder sb = new StringBuilder();
        if(seconds < 10){
            sb.append(KEYWORD_Zero);
        }
        sb.append(seconds);
        return sb.toString();
    }
}
