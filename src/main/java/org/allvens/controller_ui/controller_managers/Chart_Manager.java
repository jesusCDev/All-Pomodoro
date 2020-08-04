package org.allvens.controller_ui.controller_managers;

import org.allvens.assets.Constants;
import org.allvens.controller_ui.Data_Manager;
import org.allvens.controller_ui.ui_feedback.Toast;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import java.util.ArrayList;
import java.util.Calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.scene.chart.LineChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

public class Chart_Manager {

    private PieChart pcDailyData;
    private LineChart lcWeeklyData;
    private Label lbProjectTotalMin;
    private Label lbProjectTodayMin;
    private Label lbProjectThousandHourProgress;
    private ProgressBar pbThousandHourProgress;
    private TextField tfTimeManager;

    private Data_Manager data;
    private Toast toast;
    private String project;

    public Chart_Manager(PieChart pcDailyData, LineChart lcWeeklyData, Label lbProjectTotalMin, Label lbProjectTodayMin,
                         Label lbProjectThousandHourProgress, ProgressBar pbThousandHourProgress, TextField tfTimeManager){
        data = new Data_Manager();
        toast = new Toast();

        this.pcDailyData = pcDailyData;
        this.lcWeeklyData = lcWeeklyData;
        this.lbProjectTotalMin = lbProjectTotalMin;
        this.lbProjectTodayMin = lbProjectTodayMin;
        this.lbProjectThousandHourProgress = lbProjectThousandHourProgress;
        this.pbThousandHourProgress = pbThousandHourProgress;
        this.tfTimeManager = tfTimeManager;

        update_PieChartAndLineChartAndThousandProgress(Constants.KEYWORD_AllPomodoro);
    }

    public void setUp_Spinner(Spinner sp){
        SpinnerValueFactory<String> valueFactory =
                new SpinnerValueFactory.ListSpinnerValueFactory<>(FXCollections.observableArrayList(data.get_Projects()));
        valueFactory.setValue(Constants.KEYWORD_AllPomodoro);
        sp.setValueFactory(valueFactory);
        sp.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);
        sp.valueProperty().addListener((obs, oldProject, newProject) -> update_LineChartAndThousandProgress(newProject.toString()));
    }

    public String getProject(){
        return project;
    }

    public void update_PieChartAndLineChartAndThousandProgress(String project){
        this.project = project;
        tfTimeManager.setText(Constants.KEYWORD_Nothing);
        setUp_PieChart();
        update_WeeklyLineChart();
        update_TenThousandHourSection();
    }

    private void update_LineChartAndThousandProgress(String project){
        this.project = project;
        tfTimeManager.setText(Constants.KEYWORD_Nothing);
        update_WeeklyLineChart();
        update_TenThousandHourSection();
    }

    /****************************************
     /**** Today's Pie Chart
     ****************************************/
    // todo this is called twice for some reason
    public void setUp_PieChart(){
        ArrayList<PieChart.Data> pcDailyDataDataNamesAndValues = new ArrayList<>();
        int time_minutesUsedFromProjects = 0;

        for(String project: data.get_Projects()){
            PieChart.Data pieData = new PieChart.Data(project, convert_SecsToMinutes(data.get_TodayTimeSecs_PrefValue(project, Calendar.getInstance().get(Calendar.DAY_OF_WEEK))));
            time_minutesUsedFromProjects += convert_SecsToMinutes(data.get_TodayTimeSecs_PrefValue(project, Calendar.getInstance().get(Calendar.DAY_OF_WEEK)));
            pcDailyDataDataNamesAndValues.add(pieData);
        }

        int time_minutesOfToday = 24 * 60;

        PieChart.Data data = new PieChart.Data("Unused Time: ", (time_minutesOfToday - time_minutesUsedFromProjects));
        pcDailyDataDataNamesAndValues.add(data);

        ObservableList<PieChart.Data> details = FXCollections.observableArrayList(pcDailyDataDataNamesAndValues);
        pcDailyData.setData(details);
        pcDailyData.setTitle("Today's Progress: ");
        pcDailyData.setLegendSide(Side.BOTTOM);
    }

    /****************************************
     /**** Weekly Line Graph
     ****************************************/
    private void update_WeeklyLineChart(){
        lcWeeklyData.getData().clear();
        lcWeeklyData.layout();

        String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        XYChart.Series series = new XYChart.Series();
        series.setName(project);

        for(int i = 1; i < 8; i++){
            series.getData().add(new XYChart.Data(daysOfWeek[(i - 1)], (convert_SecsToMinutes(data.get_TodayTimeSecs_PrefValue(project, i)))));
        }

        lcWeeklyData.getData().add(series);
    }

    /****************************************
     /**** One thousand Hours
     ****************************************/

    private String convert_SecondsToBasicTimeFormat(int time_seconds){
        double time_hours = get_HoursFromSeconds(time_seconds);
        double time_minutes = Math.floor(get_MinutesFromSeconds(time_seconds, time_hours)/60);
        return (int)time_hours + Constants.KEYWORD_Colon + convertSecondsIntToStr((int)time_minutes);
    }

    private String convertSecondsIntToStr(int time_minutes){
        StringBuilder sb = new StringBuilder();
        if(time_minutes < 10){
            sb.append(Constants.KEYWORD_Zero);
        }
        sb.append(time_minutes);
        return sb.toString();
    }

    private void update_TenThousandHourSection(){

        int minPerTenThouHours = (10000 * 60);
        lbProjectTodayMin.setText("Today's Total Time: " + convert_SecondsToBasicTimeFormat(data.get_TodayTimeSecs_PrefValue(project, Calendar.getInstance().get(Calendar.DAY_OF_WEEK))));
        lbProjectTotalMin.setText("Total Total Time: " + convert_SecondsToBasicTimeFormat(data.get_TotalTimeSecs_PrefValue(project)));
        lbProjectThousandHourProgress.setText("Ten Thousand Hours Complete: " + ((convert_SecsToMinutes(data.get_TotalTimeSecs_PrefValue(project)/minPerTenThouHours) * 100)) + Constants.KEYWORD_PercentageSign);

        double progressValue = ((((double)(convert_SecsToMinutes(data.get_TotalTimeSecs_PrefValue(project))))/minPerTenThouHours));
        if(progressValue > 1){
            progressValue = 1.0;
        }
        pbThousandHourProgress.setProgress(progressValue);
    }

    /****************************************
     /**** Add/Subtract Time
     ****************************************/
    public int check_IfTimeIsInteger(String value){
        try{
            return Integer.parseInt(value);
        }catch (NumberFormatException e){
            toast.showMessage("Adding or subtracting time needs a number value.", Constants.TOAST_WindowTitle_Error);
            return 0;
        }
    }

    public void add_TimeToTotal(int time_seconds){
        data.add_TotalTimeSecs_ToProject(project, time_seconds);
    }

    public void add_TimeToDayInWeek(int day, int time_seconds){
        data.add_TodayTimeSecs_ToDayData_PrefValue(project, day, time_seconds);
    }

    public Calendar getUser_DateCal(DatePicker dp){
        //Getting date from the day picker
        String dateValue = dp.getValue().toString();
        String dateValueFormat = "yyyy-MM-dd";
        SimpleDateFormat df = new SimpleDateFormat(dateValueFormat);
        Date date = null;
        try {
            date = df.parse(dateValue);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        // SET DATE USER PICKED
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public int check_UsedTimeLeft(int dayOfWeek, int time_seconds){
        int total_TimeUsedThatDay = 0;
        for(String project: data.get_Projects()){
            total_TimeUsedThatDay += data.get_TodayTimeSecs_PrefValue(project, dayOfWeek);
        }

        if(time_seconds <= total_TimeUsedThatDay){
            return (-1 * time_seconds);
        }else{
            toast.showMessage("Can't subtract more time then you have recorded.", Constants.TOAST_WindowTitle_Error);
            return 0;
        }
    }

    public int check_EnoughTimeInDay(int dayOfWeek, int time_seconds){
        int total_TimeUsedThatDay = 0;
        for(String project: data.get_Projects()){
            total_TimeUsedThatDay += data.get_TodayTimeSecs_PrefValue(project, dayOfWeek);
        }

        int total_TimeInDay = (60 * 60 * 24);
        if((total_TimeInDay - total_TimeUsedThatDay) >= time_seconds){
            return time_seconds;
        }else{
            toast.showMessage("Can't add that much time to that day since there's only 24 hours a day.", Constants.TOAST_WindowTitle_Error);
            return 0;
        }
    }

    public int check_EnoughTimeLeftToday(int today, int time_seconds){
        int total_TimeUsedThatDay = 0;
        for(String project: data.get_Projects()){
            total_TimeUsedThatDay += data.get_TodayTimeSecs_PrefValue(project, today);
        }

        Calendar c = Calendar.getInstance();
        int now = (int)(c.getTimeInMillis()/1000.0);


        if ((now - total_TimeUsedThatDay >= time_seconds)) {
            return time_seconds;
        }else{
            toast.showMessage("Can't add that much time to today or else it'll surpass a day's time count.", Constants.TOAST_WindowTitle_Error);
            return 0;
        }
    }

    /****************************************
     /**** Time Converters
     ****************************************/
    // CONVERT ALL VALUES TO SECONDS WHEN THROWING OUT


    public int convert_SecsToMinutes(int time_seconds){
        return (time_seconds/60);
    }

    public int convert_MinutesToSecs(int time_min){
        return (time_min * 60);
    }

    public double get_HoursFromSeconds(int time_seconds){
        return Math.floor((time_seconds/60.0)/60);
    }

    public double get_MinutesFromSeconds(int time_seconds, double time_hours){
        return (time_seconds - (time_hours * 60 * 60));
    }
}
