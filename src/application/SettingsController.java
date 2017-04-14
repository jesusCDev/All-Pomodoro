package application;

import java.io.IOException;
import java.util.Arrays;
import java.util.prefs.Preferences;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SettingsController {

	@FXML
	TextField tfLongBreakDuration;
	@FXML
	TextField tfShortBreakDuration;
	@FXML
	TextField tfWorkDuration;
	@FXML
	TextField tfAmountOfShotBreak;
	@FXML
	CheckBox cbContiouseMode;
	@FXML
	ListView listView;
	@FXML
	TextField efAddProject;
	
	Preferences pref;
	
	public void cbCheckContinousMode(){
		System.out.println("Changed");
		if(cbContiouseMode.isSelected()){
			System.out.println("Set");
			pref.put("continousMode", "Yes");
		}else{
			System.out.println("No");
			pref.put("continousMode", "No");
		}
	}
	
	/**
	 * This method will allow the user to returen to the previous screen
	 * by creating a new stage and scene
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
	
	/**
	 * This method will set all the values as soon as they are loaded
	 */
	public void initialize(){
		pref = Preferences.userRoot();
		tfLongBreakDuration.setPromptText(Integer.toString(pref.getInt("longBreakDuration", 10)));
		tfShortBreakDuration.setPromptText(Integer.toString(pref.getInt("shortBreakDuration", 5)));
		tfWorkDuration.setPromptText(Integer.toString(pref.getInt("workTimeDuration", 25)));
		tfAmountOfShotBreak.setPromptText(Integer.toString(pref.getInt("amountOfCyclesTillLongBreak", 3)));
		
		if(pref.get("continousMode", "Yes").equals("Yes")){
			cbContiouseMode.setSelected(true);
		}else{
			cbContiouseMode.setSelected(false);
		}
		String[] projectsList = pref.get("projects", "All Pomorodo").split(",");
		ObservableList<String> items =FXCollections.observableArrayList (projectsList);
		listView.setItems(items);
	}
	
	public void resetTask(){
		if(listView.getSelectionModel().getSelectedIndex() != 0){
			String resetProject = listView.getSelectionModel().getSelectedItem().toString();
			pref.putInt(resetProject, 0);
			pref.putInt((resetProject + " Total"), 0);
			for(int i = 0; i < 7; i++){
				pref.putInt((resetProject + " " + i), 0);
			}
		}
	}
	
	public void deleteTask(){

		
		if(listView.getSelectionModel().getSelectedIndex() != 0){
			int j = 0;
			String deletedWord = listView.getSelectionModel().getSelectedItem().toString();

			String[] projectsList = pref.get("projects", "All Pomorodo").split(",");
			String[] newProjectList = new String[projectsList.length - 1];

			
			int iterator = 0;
			for(int i = 0; i < projectsList.length; i++){
				if(!projectsList[i].equals(deletedWord)){
					newProjectList[iterator] = projectsList[i];
					iterator++;
				}
			}

			StringBuilder words = new StringBuilder();
			for(int i = 0; i < newProjectList.length; i++){
				if(i < (newProjectList.length - 1)){
					words.append(newProjectList[i] + ",");
				}else{
					words.append(newProjectList[i]);
				}
			}
			String wordsToString = words.toString();

			pref.put("projects", wordsToString);

			ObservableList<String> items =FXCollections.observableArrayList (newProjectList);
			listView.setItems(items);
		}else{
			System.out.println("Sorry, Can't delete that one.");
		}
		
	}
	public void addTask(){
		pref.put("projects", (pref.get("projects", "All Pomorodo") + "," + efAddProject.getText()));
		pref.putInt(efAddProject.getText(), 0);
		pref.putInt((efAddProject.getText() + " Total"), 0);
		for(int i = 0; i < 7; i++){
			pref.putInt((efAddProject.getText() + " " + i), 0);
		}
		String[] projectsList = pref.get("projects", "All Pomorodo").split(",");
		ObservableList<String> items =FXCollections.observableArrayList (projectsList);
		listView.setItems(items);
		efAddProject.setText("");
	}
	
	/**
	 * This method will effect the textview of the long break and allow you to edit it
	 * @param e
	 */
	public void changeLongBreakTime(ActionEvent e){
		try{
			pref.putInt("longBreakDuration", Integer.parseInt(tfLongBreakDuration.getText()));
		}catch(NumberFormatException e1){
			System.out.println("That is not a valid number");
		}
	}
	
	/**
	 * This method will affect the textview of the short break and alllow you to edt it
	 * @param e
	 */
	public void changeShortBreakTime(ActionEvent e){
		try{
			pref.putInt("shortBreakDuration", Integer.parseInt(tfShortBreakDuration.getText()));
		}catch(NumberFormatException e1){
			System.out.println("That is not a valid number");
		}
	}
	
	/**
	 * This method will affect the textview of the work time and allow you to edit it
	 * @param e
	 */
	public void changeWorkTime(ActionEvent e){
		try{
			pref.putInt("workTimeDuration", Integer.parseInt(tfWorkDuration.getText()));
		}catch(NumberFormatException e1){
			System.out.println("That is not a valid number");
		}
		
	}
	
	/**
	 * This method will afect the amount of short breaks until the long break
	 * @param e
	 */
	public void cahngeAmountOfTimesTillLongBreak(ActionEvent e){
		try{
			pref.putInt("amountOfCyclesTillLongBreak", Integer.parseInt(tfAmountOfShotBreak.getText()));
		}catch(NumberFormatException e1){
			System.out.println("That is not a valid number");
		}
	}
}
