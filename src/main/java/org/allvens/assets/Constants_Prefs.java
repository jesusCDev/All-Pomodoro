package org.allvens.assets;

import java.util.prefs.Preferences;

public interface Constants_Prefs {

    /********** Project Values **********/
    Preferences PROJECT_Prefs = Preferences.userRoot();
    String PROJECT_KEY = "AllPomodoro_KEY-jDw387*&34d_";
    String PROJECT_FirstTime_UsingApp = "PROJECT_FirstTime_UsingApp" + PROJECT_KEY;
    String PROJECT_ProjectInProgress = "PROJECT_ProjectInProgress_" + PROJECT_KEY;
    String PROJECT_ListOfProjects = "PROJECT_ListOfProject_" + PROJECT_KEY;

    /********** Date Preferences **********/
    String DATE_LastWeekUsed = "DATE_LastWeekUsed" + PROJECT_KEY;

    /********** SubSubTitle **********/
    String TIME_TotalTimeSecs = "TIME_TotalTimeSecs_" + PROJECT_KEY;
    String TIME_TodayTimeSecs = "TIME_TodayTimeSecs_" + PROJECT_KEY;

    /********** Preset Values **********/
    String PRESET_Selected = "PRESET_Selected_" + PROJECT_KEY;
    String PRESET_TimeDuration_One = "PRESET_ONE_" + PROJECT_KEY;
    String PRESET_TimeDuration_TWO = "PRESET_TWO_" + PROJECT_KEY;
    String PRESET_TimeDuration_THREE = "PRESET_THREE_" + PROJECT_KEY;

    /********** Time Values **********/
    String TIME_Work_Duration = "TimeSecs__Work_Duration_" + PROJECT_KEY;
    String TIME_LongBreak_Duration = "TimeSecs__LongBreak_Duration_" + PROJECT_KEY;
    String TIME_ShortBreak_Duration = "TimeSecs__ShortBreak_Duration_" + PROJECT_KEY;

    String TIME_ShortBreak_Sets = "TimeSecs__ShortBreak_Sets_" + PROJECT_KEY;

    /********** Settings **********/
    String SETTINGS_SilentMode = "SETTINGS_SilentMode_" + PROJECT_KEY;
    String SETTINGS_OnTopOfWindow = "SETTINGS_OnTopOfWindow_" + PROJECT_KEY;
    String SETTINGS_CountBreakTime = "SETTINGS_CountBreakTime_" + PROJECT_KEY;

    /********** Window Sizes **********/
    String SCREEN_Max = "WINDOW_MAX_" + PROJECT_KEY;
    String SCREEN_Width = "WINDOW_WIDTH_" + PROJECT_KEY;
    String SCREEN_Height = "WINDOW_HEIGHT_" + PROJECT_KEY;
}
