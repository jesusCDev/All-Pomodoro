package org.allvens.controller;

import org.allvens.controller_ui.controller_managers.Settings_Manager;
import org.allvens.controller_ui.controller_methods.Common_ControllerMethods;
import org.allvens.controller_ui.ui_feedback.SnackBar;
import org.allvens.assets.Constants;
import org.allvens.assets.Constants_Prefs;
import org.allvens.controller_ui.ui_feedback.Toast;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class Settings extends Common_ControllerMethods implements Constants_Prefs{

	@FXML
	BorderPane bpAll;

	@FXML
	TextField tfLongBreakDuration;
	@FXML
	TextField tfShortBreakDuration;
	@FXML
	TextField tfWorkDuration;
	@FXML
	TextField tfAmountOfShotBreak;
	@FXML
	CheckBox cbHoverModeTrueOrFalse;
	@FXML
	CheckBox cbIsSilentModeOn;
	@FXML
	ListView<String> listView;
	@FXML
	TextField efAddProject;
	@FXML
	Button btn_Preset1;
    @FXML
    Button btn_Preset2;
    @FXML
    Button btn_Preset3;
    @FXML
    Button btnDeleteProject;
    @FXML
    Button btnResetProject;
    @FXML
    StackPane spSnackbar;

    private SnackBar snack;
    private Toast toast = new Toast();

	private Settings_Manager sm = new Settings_Manager();
	/**
	 * This method will set all the values as soon as they are loaded
	 */
	public void initialize(){
        snack = new SnackBar(spSnackbar);
        screen_SetSize(bpAll);

        sm.setUp_PresetButtons(btn_Preset1, btn_Preset2, btn_Preset3);

        sm.setUp_tfTimeDuration(tfWorkDuration, TIME_Work_Duration);
        sm.setUp_tfTimeDuration(tfShortBreakDuration, TIME_ShortBreak_Duration);
        sm.setUp_tfTimeDuration(tfLongBreakDuration, TIME_LongBreak_Duration);
        sm.setUp_tfTimeDuration(tfAmountOfShotBreak, TIME_ShortBreak_Sets);

        btnDeleteProject.setDisable(true);
        btnResetProject.setDisable(true);

		sm.setUp_ListView(listView, btnDeleteProject, btnResetProject);
		sm.setUp_CheckBottomSettings(cbIsSilentModeOn, cbHoverModeTrueOrFalse);
	}

    /****************************************
     /**** Preset Methods
     ****************************************/
    /********** Button Presses **********/
	public void btn_PresetOnePressed(ActionEvent e){
		btnPressed(PRESET_TimeDuration_One, e);
	}
	
	public void btn_PresetTwoPressed(ActionEvent e){
		btnPressed(PRESET_TimeDuration_TWO, e);
	}

	public void btn_PresetThreePressed(ActionEvent e){
		btnPressed(PRESET_TimeDuration_THREE, e);
	}
	
	private void btnPressed(String prefID, ActionEvent e){
        sm.save_SelectedPreset(prefID);

        btn_Preset1.setDisable(false);
        btn_Preset2.setDisable(false);
        btn_Preset3.setDisable(false);

        ((Button)e.getSource()).setDisable(true);

		String[] presetValues = (sm.get_PresetValues());

		tfWorkDuration.setText(presetValues[0]);
		tfLongBreakDuration.setText(presetValues[1]);
		tfShortBreakDuration.setText(presetValues[2]);
		tfAmountOfShotBreak.setText(presetValues[3]);

        saveValues();
        snack.createSnackBar("Time Durations Changed" ,SnackBar.SHORT);
	}

    /**
     * Saves Values in Preset Section
     */
	private void saveValues(){
        sm.save_Values(TIME_LongBreak_Duration, tfLongBreakDuration, tfLongBreakDuration.getText());
        sm.save_Values(TIME_ShortBreak_Duration, tfShortBreakDuration, tfShortBreakDuration.getText());
        sm.save_Values(TIME_Work_Duration, tfWorkDuration, tfWorkDuration.getText());
        sm.save_Values(TIME_ShortBreak_Sets, tfAmountOfShotBreak, tfAmountOfShotBreak.getText());
    }

    /**
     * Saves Values For Current Presets
     */
	public void btn_SaveCurrentValuesToPreset(){
        sm.set_PresetValues((tfWorkDuration.getText()), (tfLongBreakDuration.getText()), (tfShortBreakDuration.getText()), (tfAmountOfShotBreak.getText()));
		saveValues();

		snack.createSnackBar("Preset's Values Changed", SnackBar.SHORT);
	}

    /****************************************
     /**** Checkbox Settings
     ****************************************/
    @FXML
	public void cb_CheckIsSilentModeOn(){
		sm.change_CheckBox_Value(cbIsSilentModeOn, SETTINGS_SilentMode);
	}

    @FXML
	public void cb_CheckHoverModeTrueOrFalse(){
		sm.change_CheckBox_Value(cbHoverModeTrueOrFalse, SETTINGS_OnTopOfWindow);
	}

    /****************************************
     /**** Project Manager
     ****************************************/
    /**
     * Resets Task Values - Weekly, Total
     */
    @FXML
	public void btn_ResetTask(){
		sm.resetProject(listView.getSelectionModel().getSelectedItem());
        snack.createSnackBar("Task Values reset To Zero.", SnackBar.SHORT);
	}

    /**
     * Deletes selected task
     */
    @FXML
	public void btn_DeleteTask(){
		if(listView.getSelectionModel().getSelectedIndex() != 0){
			sm.removeProject(listView.getSelectionModel().getSelectedItem());

			ObservableList<String> items =FXCollections.observableArrayList (sm.getProjects());
			listView.setItems(items);

            btnDeleteProject.setDisable(true);
            btnResetProject.setDisable(true);
            snack.createSnackBar("Task Deleted", SnackBar.SHORT);
		}else{
		    snack.createSnackBar("All Pomodoro Cannot Be Deleted.", SnackBar.SHORT);
		}
		
	}

	@FXML
	public void btn_AddTask(){
		if((efAddProject.getText().trim().length() > 0) && !efAddProject.getText().isEmpty() &&
                !efAddProject.getText().contains(Constants.KEYWORD_Comma) && !sm.containsProject(efAddProject.getText())){
		    sm.addProject(efAddProject.getText());
			ObservableList<String> items =FXCollections.observableArrayList (sm.getProjects());
			listView.setItems(items);
			efAddProject.setText("");
			snack.createSnackBar("Task Has Been Created.", SnackBar.SHORT);
		}else{
            toast.showMessage("Field can't be empty, contain commas, or already be created.", Constants.TOAST_WindowTitle_Error);
		}
	}

    @FXML
	public void btn_ScreenChange_Home(ActionEvent e){
        screen_checkAlwaysOnTop(SETTINGS_OnTopOfWindow, e, Constants.FILE_SCREEN_Main, Constants.WINDOW_TITLE_Home, bpAll);
	}
}
