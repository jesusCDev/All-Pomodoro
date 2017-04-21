package application;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.prefs.Preferences;

import javax.management.ServiceNotFoundException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChartController {

	@FXML
	Spinner<String> spinnerProject2;
	@FXML
	PieChart dailyActivities;
	@FXML
	LineChart weeklyActivites;
	@FXML
	Label totalMin;
	@FXML
	Label todayMin;
	@FXML
	BorderPane bpChartAll;
	@FXML
	ScrollBar sbVBCenter;
	@FXML
	DatePicker dpMinAddSubtract;
	@FXML
	TextField TFMinAddSubtract;
	
	private Preferences pref;
	private String currentTimePrefString = "overAllTimeCountedInCurrentProject";
	private String currentProjectPrefString = "currentProject";
	private String allPomorodoPrefString = "All Pomorodo";
	private String totalKeyWord = " Total";
	private String commaKeyWord = ",";
	private String totalMinWord = "Total Minutes: ";
	private String todaysMinWord = "Today's Minutes: ";
	
	private String sundayWord = "Sunday";
	private String mondayWord = "Monday";
	private String tuesdayWord = "Tuesday";
	private String wednesdayWord = "Wednesday";
	private String thursdayWord = "Thursday";
	private String fridayWord = "Friday";
	private String saturdayWord = "Saturday";
	private String daysWord = "Days";

	private String oneWord = " 1";
	private String twoWord = " 2";
	private String threeWord = " 3";
	private String fourWord = " 4";
	private String fiveWord = " 5";
	private String sixWord = " 6";
	private String sevenWord = " 7";
	
	private int dayOfWeek;
	
	/**
	 * Method will set all the values for the pie chart and the other graphs
	 */
	public void initialize(){
		
		sbVBCenter.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov,
				Number old_val, Number new_val) {
				System.out.println(-new_val.doubleValue()*5);
				bpChartAll.setLayoutY(-new_val.doubleValue()*5);
			}
		});
		pref = Preferences.userRoot();
		
		Calendar cal = Calendar.getInstance();
		dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		
		String pieChartsTitle = "Today's Progress";
		String pieChartsWastedTimeTitle = "Mintues of the day not use.";
		
		//Preference names
		String projectsPrefString = "projects";
		String programmingPrefString = "Programming";
		String homeworkPrefString = "Homework";
		String workoutPrefString = "Workout";
		
		
		//Handles Pie Chart
		ArrayList<PieChart.Data> pieChartDataNamesAndValues = new ArrayList<>();
		String[] projectsList = pref.get(projectsPrefString, allPomorodoPrefString).split(commaKeyWord);
		
		for(int i = 0; i < projectsList.length; i++){
			PieChart.Data data = null;
			if(pref.get(currentProjectPrefString, allPomorodoPrefString).equals(projectsList[i])){
				data = new PieChart.Data(projectsList[i], (((pref.getInt(projectsList[i], 0)) + (pref.getInt(currentTimePrefString, 0)))/60));	
			}else{
				data = new PieChart.Data(projectsList[i], (pref.getInt(projectsList[i], 0)/60));				
			}
			pieChartDataNamesAndValues.add(data);
		}
				
		int minutesOfToday = 24 * 60;
		int minutesUsedFromProjects = ((((pref.getInt(currentTimePrefString, 0)) + (pref.getInt(allPomorodoPrefString, 0)) + (pref.getInt(programmingPrefString, 0)) + (pref.getInt(homeworkPrefString, 0)) + (pref.getInt(workoutPrefString, 0)))/60));
		int hoursNotUsing = minutesOfToday - minutesUsedFromProjects;
		
		PieChart.Data data = new PieChart.Data(pieChartsWastedTimeTitle, hoursNotUsing);
		pieChartDataNamesAndValues.add(data);
		
		ObservableList<PieChart.Data> details = FXCollections.observableArrayList(pieChartDataNamesAndValues);
		dailyActivities.setData(details);
		dailyActivities.setTitle(pieChartsTitle);
		dailyActivities.setLegendSide(Side.BOTTOM);

		
		//Handles the spinner
		ObservableList<String> projects = FXCollections.observableArrayList(projectsList);
		SpinnerValueFactory<String> valueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<String>(projects);
		valueFactory.setValue(allPomorodoPrefString);
		spinnerProject2.setValueFactory(valueFactory);
		spinnerProject2.getStyleClass().add(spinnerProject2.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);
		spinnerProject2.valueProperty().addListener((obs, oldValue, newValue) -> makeVisible(newValue.toString()));
		
		//Handles Line Chart
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel(daysWord);
						
		XYChart.Series series = new XYChart.Series();
		series.setName(allPomorodoPrefString);
		
		
		int sunday = 0;
		int monday = 0;
		int tuesday = 0;
		int wednesday = 0;
		int thursday = 0;
		int friday = 0;
		int saturday = 0;

		totalMin.setText(totalMinWord + ((pref.getInt(allPomorodoPrefString + totalKeyWord, 0))/60));
		todayMin.setText(todaysMinWord + ((pref.getInt(allPomorodoPrefString, 0))/60));
		
		if(allPomorodoPrefString.equals(pref.get(currentProjectPrefString, allPomorodoPrefString))){
			System.out.println("Ran");
			switch(dayOfWeek){
				case 1:
					sunday = ((pref.getInt(currentTimePrefString, 0)));
					totalMin.setText(totalMinWord + ((pref.getInt(allPomorodoPrefString + totalKeyWord, 0) + sunday)/60));
					todayMin.setText(todaysMinWord + ((pref.getInt(allPomorodoPrefString, 0) + sunday)/60));
					break;
				case 2:
					monday = ((pref.getInt(currentTimePrefString, 0)));
					totalMin.setText(totalMinWord + ((pref.getInt(allPomorodoPrefString + totalKeyWord, 0) + monday)/60));
					todayMin.setText(todaysMinWord + ((pref.getInt(allPomorodoPrefString, 0) + monday)/60));
					break;
				case 3:
					tuesday = ((pref.getInt(currentTimePrefString, 0)));
					totalMin.setText(totalMinWord + ((pref.getInt(allPomorodoPrefString + totalKeyWord, 0) + tuesday)/60));
					todayMin.setText(todaysMinWord + ((pref.getInt(allPomorodoPrefString, 0) + tuesday)/60));
					break;
				case 4:
					wednesday = ((pref.getInt(currentTimePrefString, 0)));
					totalMin.setText(totalMinWord + ((pref.getInt(allPomorodoPrefString + totalKeyWord, 0) + wednesday)/60));
					todayMin.setText(todaysMinWord + ((pref.getInt(allPomorodoPrefString, 0) + wednesday)/60));
					break;
				case 5:
					thursday = ((pref.getInt(currentTimePrefString, 0)));
					totalMin.setText(totalMinWord + ((pref.getInt(allPomorodoPrefString + totalKeyWord, 0) + thursday)/60));
					todayMin.setText(todaysMinWord + ((pref.getInt(allPomorodoPrefString, 0) + thursday)/60));
					break;
				case 6:
					friday = ((pref.getInt(currentTimePrefString, 0)));
					totalMin.setText(totalMinWord + ((pref.getInt(allPomorodoPrefString + totalKeyWord, 0) + friday)/60));
					todayMin.setText(todaysMinWord + ((pref.getInt(allPomorodoPrefString, 0) + friday)/60));
					break;
				case 7:
					saturday = ((pref.getInt(currentTimePrefString, 0)));
					totalMin.setText(totalMinWord + ((pref.getInt(allPomorodoPrefString + totalKeyWord, 0) + saturday)/60));
					todayMin.setText(todaysMinWord + ((pref.getInt(allPomorodoPrefString, 0) + saturday)/60));
					break;
			}
		}
		
		series.getData().add(new XYChart.Data(sundayWord, (((pref.getInt((allPomorodoPrefString + oneWord), 0)) + sunday)/60)));
		series.getData().add(new XYChart.Data(mondayWord, (((pref.getInt((allPomorodoPrefString + twoWord), 0)) + monday)/60)));
		series.getData().add(new XYChart.Data(tuesdayWord, (((pref.getInt((allPomorodoPrefString + threeWord), 0)) + tuesday)/60)));
		series.getData().add(new XYChart.Data(wednesdayWord, (((pref.getInt((allPomorodoPrefString + fourWord), 0)) + wednesday)/60)));
		series.getData().add(new XYChart.Data(thursdayWord, (((pref.getInt((allPomorodoPrefString + fiveWord), 0)) + thursday)/60)));
		series.getData().add(new XYChart.Data(fridayWord, (((pref.getInt((allPomorodoPrefString + sixWord), 0)) + friday)/60)));
		series.getData().add(new XYChart.Data(saturdayWord, (((pref.getInt((allPomorodoPrefString + sevenWord), 0)) + saturday)/60)));

		weeklyActivites.getData().add(series);

		dpMinAddSubtract.setValue(LocalDate.now());
		
	}

	/**
	 * Method runs whenever the spinner project is changed
	 * it presents the information in the  bottom graph of it
	 * @param projectName
	 */
	private void makeVisible(String projectName) {
		
		weeklyActivites.getData().clear();
		weeklyActivites.layout();
		
		//Handles Line Chart
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel(daysWord);
				
		XYChart.Series series = new XYChart.Series();
		series.setName(projectName);
		
		int sunday = 0;
		int monday = 0;
		int tuesday = 0;
		int wednesday = 0;
		int thursday = 0;
		int friday = 0;
		int saturday = 0;

		totalMin.setText(totalMinWord + ((pref.getInt(projectName + totalKeyWord, 0))/60));
		todayMin.setText(todaysMinWord + ((pref.getInt(projectName, 0))/60));
		if(projectName.equals(pref.get(currentProjectPrefString, allPomorodoPrefString))){
			switch(dayOfWeek){
				case 1:
					sunday = ((pref.getInt(currentTimePrefString, 0)));
					totalMin.setText(totalMinWord + ((pref.getInt(projectName + totalKeyWord, 0) + sunday)/60));
					todayMin.setText(todaysMinWord + ((pref.getInt(projectName, 0) + sunday)/60));
					break;
				case 2:
					monday = ((pref.getInt(currentTimePrefString, 0)));
					totalMin.setText(totalMinWord + ((pref.getInt(projectName + totalKeyWord, 0) + monday)/60));
					todayMin.setText(todaysMinWord + ((pref.getInt(projectName, 0) + monday)/60));
					break;
				case 3:
					tuesday = ((pref.getInt(currentTimePrefString, 0)));
					totalMin.setText(totalMinWord + ((pref.getInt(projectName + totalKeyWord, 0) + tuesday)/60));
					todayMin.setText(todaysMinWord + ((pref.getInt(projectName, 0) + tuesday)/60));
					break;
				case 4:
					wednesday = ((pref.getInt(currentTimePrefString, 0)));
					totalMin.setText(totalMinWord + ((pref.getInt(projectName + totalKeyWord, 0) + wednesday)/60));
					todayMin.setText(todaysMinWord + ((pref.getInt(projectName, 0) + wednesday)/60));
					break;
				case 5:
					thursday = ((pref.getInt(currentTimePrefString, 0)));
					totalMin.setText(totalMinWord + ((pref.getInt(projectName + totalKeyWord, 0) + thursday)/60));
					todayMin.setText(todaysMinWord + ((pref.getInt(projectName, 0) + thursday)/60));
					break;
				case 6:
					friday = ((pref.getInt(currentTimePrefString, 0)));
					totalMin.setText(totalMinWord + ((pref.getInt(projectName + totalKeyWord, 0) + friday)/60));
					todayMin.setText(todaysMinWord + ((pref.getInt(projectName, 0) + friday)/60));
					break;
				case 7:
					saturday = ((pref.getInt(currentTimePrefString, 0)));
					totalMin.setText(totalMinWord + ((pref.getInt(projectName + totalKeyWord, 0) + saturday)/60));
					todayMin.setText(todaysMinWord + ((pref.getInt(projectName, 0) + saturday)/60));
					break;
			}
		}

		String projectNameSunday = (projectName + oneWord);
		String projectNameMonday = (projectName + twoWord);
		String projectNameTuesday = (projectName + threeWord);
		String projectNameWednesday = (projectName + fourWord);
		String projectNameThursday = (projectName + fiveWord);
		String projectNameFriday = (projectName + sixWord);
		String projectNameSaturday = (projectName + sevenWord);
		
		series.getData().add(new XYChart.Data(sundayWord, (((pref.getInt(projectNameSunday, 0)) + sunday)/60)));
		series.getData().add(new XYChart.Data(mondayWord, (((pref.getInt(projectNameMonday, 0)) + monday)/60)));
		series.getData().add(new XYChart.Data(tuesdayWord, (((pref.getInt(projectNameTuesday, 0)) + tuesday)/60)));
		series.getData().add(new XYChart.Data(wednesdayWord, (((pref.getInt(projectNameWednesday, 0)) + wednesday)/60)));
		series.getData().add(new XYChart.Data(thursdayWord, (((pref.getInt(projectNameThursday, 0)) + thursday)/60)));
		series.getData().add(new XYChart.Data(fridayWord, (((pref.getInt(projectNameFriday, 0)) + friday)/60)));
		series.getData().add(new XYChart.Data(saturdayWord, (((pref.getInt(projectNameSaturday, 0)) + saturday)/60)));
		
		weeklyActivites.getData().add(series);
	}
	
	/**
	 * will add or subtract the amount of mintues in the current project
	 * will use the date picker in order to chose the day of it for which the amount of time you would 
	 * like to do or not
	 * @param e
	 */
	public void addSubtractMinutes(ActionEvent e){
		String date = dpMinAddSubtract.getValue().toString();
		String day = dpMinAddSubtract.getValue().get(Calendar.DAY_OF_YEAR);
		System.out.println(date);
	}

	/**
	 * returns it to the timer screen
	 * @param e
	 */
	public void goBackToMainMenu(ActionEvent e){
		Parent loader = null;
		try {
			loader = FXMLLoader.load(getClass().getResource("/fxml/StartPomorodo.fxml"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Scene scene = new Scene(loader);
		scene.getStylesheets().add(getClass().getResource("/style/startPomorodo.css").toExternalForm());
		Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		stage.setAlwaysOnTop(true);
		stage.setMinWidth(250.0);
		stage.setMinHeight(200.0);
		stage.setScene(scene);
		stage.show();
	}
}
