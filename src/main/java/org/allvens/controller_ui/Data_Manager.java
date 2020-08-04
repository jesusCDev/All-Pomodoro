package org.allvens.controller_ui;

import org.allvens.assets.Constants;
import org.allvens.assets.Constants_Prefs;

public class Data_Manager implements Constants_Prefs {

    /********** Project Cleaners **********/
    public void remove_Project(String project){
        PROJECT_Prefs.remove(get_TotalTimeSecs__Pref(project));

        for(int i = 0; i < 7; i++){
            PROJECT_Prefs.remove(get_TodayTimeSecs__Pref(project, i));
        }

        StringBuilder sb = new StringBuilder();
        for(String projects: get_Projects()){
            if(!project.equals(projects)){
                sb.append(projects);
                sb.append(Constants.KEYWORD_Comma);
            }
        }
        sb.replace(sb.length(), sb.length(), Constants.KEYWORD_Nothing);
        set_Projects(sb.toString());
    }

    public void reset_AllProjectsWeek(){
        String[] projects = get_Projects();
        for(String project: projects){
            reset_ProjectWeek(project);
        }
    }

    public void set_FirstTimeUsingApp_Value(boolean value){
        PROJECT_Prefs.putBoolean(PROJECT_FirstTime_UsingApp, value);
    }

    public boolean get_FirstTimeUsingApp_Value(){
        return PROJECT_Prefs.getBoolean(PROJECT_FirstTime_UsingApp, true);
    }

    /****************************************
     /**** Settings
     ****************************************/
    public void set_CheckBoxSettingValue(String prefID, boolean value){
        PROJECT_Prefs.putBoolean(prefID, value);
    }

    public boolean get_CheckBoxSettingValue(String prefID){
        return PROJECT_Prefs.getBoolean(prefID, false);
    }

    // PRESET AND TIME VALUES ARE HANDLED IN MINTUES

    public String get_SelectedPreset(){
        return PROJECT_Prefs.get(PRESET_Selected, PRESET_TimeDuration_One);
    }
    public void set_SelectedPreset(String value){
        PROJECT_Prefs.put(PRESET_Selected, value);
    }

    public void set_PresetTime_Duration(String prefID, String value){
        PROJECT_Prefs.put(prefID, value);
    }

    public String[] get_PresetTime_Duration(String prefID){
        return PROJECT_Prefs.get(prefID, Constants.TIMER_PRESETS_One).split(Constants.KEYWORD_Comma);
    }

    /****************************************
     /**** Project
     ****************************************/

    /********** Multiple Projects **********/
    // TIME DURATIONS ARE HANDLED IN MINUTES
    public void set_CurrentWorkingProject_PrefValue(String project){
        PROJECT_Prefs.put(PROJECT_ProjectInProgress, project);
    }

    public String get_CurrentWorkingProject_PrefValue(){
        return PROJECT_Prefs.get(PROJECT_ProjectInProgress, Constants.KEYWORD_AllPomodoro);
    }

    public String[] get_Projects(){
        return PROJECT_Prefs.get(PROJECT_ListOfProjects, Constants.KEYWORD_AllPomodoro).split(Constants.KEYWORD_Comma);
    }

    public void set_Projects(String projects){
        PROJECT_Prefs.put(PROJECT_ListOfProjects, projects);
    }

    /********** Total TimeSecs_ **********/

    public void add_TotalTimeSecs_ToProject(String project, int time_seconds){
        PROJECT_Prefs.putInt(get_TotalTimeSecs__Pref(project), (time_seconds + get_TotalTimeSecs_PrefValue(project)));
    }

    public void subtract_TotalTimeSecs_FromProject(String project, int time_seconds){
        PROJECT_Prefs.putInt(get_TotalTimeSecs__Pref(project), (get_TotalTimeSecs_PrefValue(project) - time_seconds));
    }

    public void set_TotalTimeSecs_PrefValue(String project, int time_seconds){
        PROJECT_Prefs.putInt(get_TotalTimeSecs__Pref(project), time_seconds);
    }

    public int get_TotalTimeSecs_PrefValue(String project){
        return PROJECT_Prefs.getInt(get_TotalTimeSecs__Pref(project), 0);
    }

    private String get_TotalTimeSecs__Pref(String project){
        return TIME_TotalTimeSecs + project;
    }

    /********** Today TimeSecs_ **********/
    public void add_TodayTimeSecs_ToDayData_PrefValue(String project, int day, int time_seconds){
        PROJECT_Prefs.putInt(get_TodayTimeSecs__Pref(project, day), (time_seconds + get_TodayTimeSecs_PrefValue(project, day)));
        System.out.println(project);
        System.out.println(PROJECT_Prefs.getInt(get_TodayTimeSecs__Pref(project, day), 0));
    }

    public void subtract_TodayTimeSecs_ToDayData_PrefValue(String project, int day, int time_seconds){
        PROJECT_Prefs.putInt(get_TodayTimeSecs__Pref(project, day), (get_TodayTimeSecs_PrefValue(project, day) - time_seconds));
    }

    public void set_TodayTimeSecs_PrefValue(String project, int dayOfWeek, int time_seconds){
        PROJECT_Prefs.putInt(get_TodayTimeSecs__Pref(project, dayOfWeek), time_seconds);
    }

    public int get_TodayTimeSecs_PrefValue(String project, int dayOfWeek){
        return PROJECT_Prefs.getInt(get_TodayTimeSecs__Pref(project, dayOfWeek), 0);
    }

    private String get_TodayTimeSecs__Pref(String project, int dayOfWeek){
        return TIME_TodayTimeSecs + project  + dayOfWeek;
    }

    public void reset_ProjectWeek(String project){
        set_TotalTimeSecs_PrefValue(project, 0);
        for(int i = 1; i < 8; i++){
            set_TodayTimeSecs_PrefValue(project, i, 0);
        }
    }

    /****************************************
     /**** Calendar
     ****************************************/
    /********** Week Values **********/
    public int get_LastWeekUsedApp_PrefValue(){
        return PROJECT_Prefs.getInt(DATE_LastWeekUsed, 0);
    }

    public void set_LastWeekUsedApp_PrefValue(int value){
        PROJECT_Prefs.getInt(DATE_LastWeekUsed, value);
    }

    /********** Time Duration Methods **********/
    public int get_TimeDuration(String status){
        return PROJECT_Prefs.getInt(status, 0);
    }

    public void set_TimeDuration(String status, int value){
        PROJECT_Prefs.putInt(status, value);
    }

    public void set_ShortBreak_Sets(int sets){
        PROJECT_Prefs.putInt(TIME_ShortBreak_Sets, sets);
    }

    public int get_ShortBreak_Sets(){
        return PROJECT_Prefs.getInt(TIME_ShortBreak_Sets, 0);
    }
}
