package org.allvens.controller_ui.controller_managers;

import org.allvens.controller_ui.Data_Manager;
import org.allvens.controller_ui.home_managers.Timer_Controller;
import org.allvens.controller_ui.home_managers.UIEffects_Manager;
import org.allvens.assets.Constants;
import org.allvens.assets.Constants_Prefs;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.util.Calendar;

public class Home_Manager implements Constants{

    private int workTime;
    private int longBreakTime;
    private int shortBreakTime;
    private int shortBreakSets;

    private static int WORK_TIME = 1;
    private static int SHORT_TIME = 2;
    private static int LONG_TIME = 3;
    private static int PAUSE_TIME = 4;

    private boolean pauseOnStatus = true;

    private int current_status = WORK_TIME;
    private int next_status = WORK_TIME;
    private int shortBreak_Tracker;

    private Timer_Controller timer;
    private UIEffects_Manager uiE;
    private Data_Manager data;

    private String project;

    private void setDefaultValues(){
        data.set_FirstTimeUsingApp_Value(false);

        // setting up presets
        data.set_PresetTime_Duration(Constants_Prefs.PRESET_TimeDuration_One, Constants.TIMER_PRESETS_One);
        data.set_PresetTime_Duration(Constants_Prefs.PRESET_TimeDuration_TWO, Constants.TIMER_PRESETS_Two);
        data.set_PresetTime_Duration(Constants_Prefs.PRESET_TimeDuration_THREE, Constants.TIMER_PRESETS_Three);

        // setting up working
        String[] presetValues = data.get_PresetTime_Duration(data.get_SelectedPreset());
        data.set_TimeDuration(Constants_Prefs.TIME_Work_Duration, Integer.parseInt(presetValues[0]));
        data.set_TimeDuration(Constants_Prefs.TIME_LongBreak_Duration, Integer.parseInt(presetValues[1]));
        data.set_TimeDuration(Constants_Prefs.TIME_ShortBreak_Duration, Integer.parseInt(presetValues[2]));

        // SETUP DEFAULT PRESET VALUES TOO
        data.set_ShortBreak_Sets(Integer.parseInt(presetValues[3]));

        data.set_CurrentWorkingProject_PrefValue(Constants.KEYWORD_AllPomodoro);
    }

    private void setDateValues(){
        Calendar cal = Calendar.getInstance();
        if(data.get_LastWeekUsedApp_PrefValue() != cal.get(Calendar.WEEK_OF_YEAR)){
            data.set_LastWeekUsedApp_PrefValue(cal.get(Calendar.WEEK_OF_YEAR));
            data.reset_AllProjectsWeek();
        }
    }

    private int convert_MinutesToSeconds(int time_minutes){
        return (time_minutes * 60);
    }

    public Home_Manager(BorderPane bpAll, Label lbStatus, Label lbTimer, ProgressBar pbTimer, Button btnPlayPause){
        data = new Data_Manager();

        if(data.get_FirstTimeUsingApp_Value()){
            setDefaultValues();
        }

        workTime = convert_MinutesToSeconds(data.get_TimeDuration(Constants_Prefs.TIME_Work_Duration));
        shortBreakTime = convert_MinutesToSeconds(data.get_TimeDuration(Constants_Prefs.TIME_ShortBreak_Duration));
        longBreakTime = convert_MinutesToSeconds(data.get_TimeDuration(Constants_Prefs.TIME_LongBreak_Duration));
        shortBreakSets = data.get_TimeDuration(Constants_Prefs.TIME_ShortBreak_Sets);

        project = data.get_CurrentWorkingProject_PrefValue();

        shortBreak_Tracker = shortBreakSets;


        timer = new Timer_Controller();
        uiE = new UIEffects_Manager(bpAll, lbStatus, lbTimer, pbTimer, btnPlayPause);
        uiE.update_TimerValue(workTime);
        timer.set_UIEffect(uiE);
        set_NewTimer();
        uiE.set_Screen(PAUSE_TIME);
    }

    public void setSpinner(Spinner<String> spProjectContainer){
        String[] projectsList = data.get_Projects();
        ObservableList<String> projects = FXCollections.observableArrayList(projectsList);
        SpinnerValueFactory<String> valueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<String>(projects);

        valueFactory.setValue(data.get_CurrentWorkingProject_PrefValue());

        spProjectContainer.setValueFactory(valueFactory);
        spProjectContainer.getStyleClass().add(spProjectContainer.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);
        spProjectContainer.valueProperty().addListener((obs, oldValue, newValue) -> hardReset(newValue));
    }

    public void saveCurrentData(){
        timer.stop_timer();

        data.set_CurrentWorkingProject_PrefValue(project);

        Calendar cal = Calendar.getInstance();
        data.set_TotalTimeSecs_PrefValue(project, (data.get_TotalTimeSecs_PrefValue(project) + timer.get_TotalTrackedTime()));
        data.set_TodayTimeSecs_PrefValue(project, cal.get(Calendar.DAY_OF_WEEK),
                (data.get_TodayTimeSecs_PrefValue(project, cal.get(Calendar.DAY_OF_WEEK)) + timer.get_TotalTrackedTime()));
    }

    private void set_NewTimer(){
        int time;
        if(next_status == WORK_TIME){
            current_status = WORK_TIME;
            time = workTime;
            uiE.set_Screen(WORK_TIME);
            if(shortBreak_Tracker != 0){
                next_status = SHORT_TIME;
                shortBreak_Tracker--;
            }else{
                next_status = LONG_TIME;
                shortBreak_Tracker = shortBreakSets;
            }
        }else{
            if(next_status == SHORT_TIME){
                current_status = SHORT_TIME;
                next_status = WORK_TIME;
                time = shortBreakTime;
                uiE.set_Screen(SHORT_TIME);
            }else{
                current_status = LONG_TIME;
                next_status = WORK_TIME;
                time = longBreakTime;
                uiE.set_Screen(LONG_TIME);
            }
        }
        timer.create_timer(time);
    }

    public void playPause_Timer(){
        if(!timer.get_timerFinished()){
            if(pauseOnStatus){
                resume_Timer();
            }else{
                pause_Timer();
            }
        }else{
            timer.restart_TimerValues();
            set_NewTimer();
            resume_Timer();
        }
    }

    private void pause_Timer() {
        pauseOnStatus = true;
        uiE.set_Screen(PAUSE_TIME);
        timer.pause_timer();
    }

    private void resume_Timer() {
        pauseOnStatus = false;
        uiE.set_Screen(current_status);
        timer.resume_timer();
    }

    public void reset_Timer() {
        uiE.closeEndingScreen();
        timer.stop_timer();
        timer.create_timer(uiE.get_InitialTime());
        pauseOnStatus = true;
        next_status = current_status;
        if(current_status == WORK_TIME){
            shortBreak_Tracker++;
        }

        updateIfPaused(uiE.get_InitialTime());
    }

    private void hardReset(String project){
        this.project = project;
        uiE.closeEndingScreen();
        timer.set_Total_time_tracker((timer.get_TotalTrackedTime() - timer.get_TrackedTime()));
        timer.stop_timer();

        next_status = WORK_TIME;
        shortBreak_Tracker = shortBreakSets;

        updateIfPaused(workTime);
    }

    private void updateIfPaused(int time){
        uiE.update_TimerValue(time);
        set_NewTimer();
        playPause_Timer();
        pause_Timer();
    }

    public void skip_Timer() {
        pauseOnStatus = true;
        uiE.closeEndingScreen();
        timer.stop_timer();
        set_NewTimer();
        playPause_Timer();
    }

    public void add5Min_Timer(){
        pauseOnStatus = true;
        uiE.closeEndingScreen();
        timer.add_5minToTimer();
        playPause_Timer();

    }

    public void debug_add10MinToTimer() {
        timer.debug_Add10MinToTimer();
    }

    public void debug_ResetAllValuesToDefault() {
        setDateValues();
    }
}
