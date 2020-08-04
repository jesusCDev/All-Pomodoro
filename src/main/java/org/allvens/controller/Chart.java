package org.allvens.controller;

import java.time.LocalDate;
import java.util.Calendar;

import org.allvens.assets.Constants;
import org.allvens.controller_ui.controller_managers.Chart_Manager;
import org.allvens.controller_ui.controller_methods.Common_ControllerMethods;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class Chart extends Common_ControllerMethods{

	@FXML
    BorderPane bpAll;
	@FXML
	Spinner<String> spProjects;
	@FXML
	PieChart pcDailyData;
	@FXML
	LineChart lcWeeklyData;
	@FXML
	Label lbProjectTotalMin;
	@FXML
	Label lbProjectTodayMin;
	@FXML
	Label lbProjectThousandHourProgress;
	@FXML
	DatePicker dpMinAddSubtract;
	@FXML
	TextField tfTimeManager;
	@FXML
	ProgressBar pbThousandHourProgress;
	@FXML
    StackPane spSnackbar;

	private Chart_Manager chartManager;

    public void initialize(){
        screen_SetSize(bpAll);
        chartManager = new Chart_Manager(pcDailyData, lcWeeklyData, lbProjectTotalMin, lbProjectTodayMin, lbProjectThousandHourProgress, pbThousandHourProgress, tfTimeManager);
		chartManager.setUp_Spinner(spProjects);
		dpMinAddSubtract.setValue(LocalDate.now());
		dpMinAddSubtract.getEditor().setDisable(true);
    }

    /**
     * Adds or Subtracts minutes passed through
     * @param time_seconds - time being added
     * @param adding - true = add, false = subtract
     */
    private void addSubtract_Time(int time_seconds, boolean adding){
        Calendar calUser = chartManager.getUser_DateCal(dpMinAddSubtract);
        int datePickerDayOfWeek = calUser.get(Calendar.DAY_OF_WEEK);

        if(calUser.get(Calendar.WEEK_OF_YEAR) <= Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)){
            int fixedTime;
            if(adding){
                if(datePickerDayOfWeek == Calendar.getInstance().get(Calendar.DAY_OF_WEEK)){
                    fixedTime = chartManager.check_EnoughTimeLeftToday(datePickerDayOfWeek, time_seconds);
                }else{
                    fixedTime = chartManager.check_EnoughTimeInDay(datePickerDayOfWeek, time_seconds);
                }
            }else{
                fixedTime = chartManager.check_UsedTimeLeft(datePickerDayOfWeek, time_seconds);
            }

            chartManager.add_TimeToDayInWeek(datePickerDayOfWeek, fixedTime);
            chartManager.add_TimeToTotal(fixedTime);
        }else{
            chartManager.add_TimeToTotal(time_seconds);
        }

        chartManager.update_PieChartAndLineChartAndThousandProgress(chartManager.getProject());
    }

    /****************************************
     /**** Button Actions
     ****************************************/
    /**
     * Adds Minutes to Selected Project
     */
    public void btn_addMinutes(ActionEvent e){
        addSubtract_Time(chartManager.convert_MinutesToSecs(chartManager.check_IfTimeIsInteger(tfTimeManager.getText())), true);
    }

    /**
     * Subtracts Minutes to Selected Project
     */
	public void btn_subtractMinutes(ActionEvent e){
        addSubtract_Time(chartManager.convert_MinutesToSecs(chartManager.check_IfTimeIsInteger(tfTimeManager.getText())), false);
	}

    /**
     * Main Menu
     */
	public void btn_ScreenChange_Home(ActionEvent e){
		screen_checkAlwaysOnTop(SETTINGS_OnTopOfWindow, e, Constants.FILE_SCREEN_Main, Constants.WINDOW_TITLE_Home, bpAll);
	}
}
