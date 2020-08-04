package org.allvens.controller_ui.controller_managers;

import org.allvens.assets.Constants;
import org.allvens.assets.Constants_Prefs;
import org.allvens.controller_ui.Data_Manager;
import org.allvens.controller_ui.ui_feedback.Toast;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class Settings_Manager implements Constants_Prefs {

    private Data_Manager data = new Data_Manager();
    private Toast toast = new Toast();

    public void setUp_ListView(ListView lv, Button btnDeleteProject, Button btnResetProject){
        ObservableList<String> items = FXCollections.observableArrayList (data.get_Projects());
        lv.setItems(items);
        lv.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try{
                    if(lv.getSelectionModel().getSelectedIndex() != 0 &&
                            lv.getSelectionModel().getSelectedItem() != null){
                        btnDeleteProject.setDisable(false);
                        btnResetProject.setDisable(false);
                    }else{
                        btnDeleteProject.setDisable(true);
                        btnResetProject.setDisable(true);
                    }
                }catch (NullPointerException e){

                }
            }
        });
    }

    /****************************************
     /**** TIME DURATION
     ****************************************/
    /********** TEXT FIELDS **********/
    public void setUp_tfTimeDuration(TextField tf_TimeDuration, String prefID){
        tf_TimeDuration.setText(Integer.toString(data.get_TimeDuration(prefID)));
    }

    public void save_Values(String prefID, TextField tf, String value){
        String number = checkIfNumber(value);
        data.set_TimeDuration(prefID, Integer.parseInt(number));
        tf.setText(number);
    }

    public void setUp_PresetButtons(Button btnPre1, Button btnPre2, Button btnPre3){
        switch (data.get_SelectedPreset()){
            case PRESET_TimeDuration_One:
                btnPre1.setDisable(true);
                break;
            case PRESET_TimeDuration_TWO:
                btnPre2.setDisable(true);
                break;
            default:
                btnPre3.setDisable(true);
                break;
        }
    }

    public String checkIfNumber(String value){
        try{
            Integer.parseInt(value);
            return value;
        }catch (NumberFormatException e){
            toast.showMessage("Preset Value Not An Integer", "Error");
            return "0";
        }
    }

    public void set_PresetValues(String workTime, String longBreakTime, String shortBreakTime, String shortBreakSets){
        String preset = (checkIfNumber(workTime) + Constants.KEYWORD_Comma + checkIfNumber(longBreakTime) +
                Constants.KEYWORD_Comma + checkIfNumber(shortBreakTime) + Constants.KEYWORD_Comma + checkIfNumber(shortBreakSets));

        data.set_PresetTime_Duration(data.get_SelectedPreset(), preset);
    }

    public void save_SelectedPreset(String prefID){
        data.set_SelectedPreset(prefID);
    }

    public String[] get_PresetValues(){
        return data.get_PresetTime_Duration(data.get_SelectedPreset());
    }

    /****************************************
     /**** ListView SETTINGS
     ****************************************/

    public boolean containsProject(String project){
        for(String projects: data.get_Projects()){
            if(project.equals(projects)){
                return true;
            }
        }
        return false;
    }

    public void resetProject(String project){
        data.reset_ProjectWeek(project);
    }

    public String[] getProjects(){
        return data.get_Projects();
    }

    public void addProject(String project){
        StringBuilder sb = new StringBuilder();

        for(String projects: data.get_Projects()){
            sb.append(projects);
            sb.append(Constants.KEYWORD_Comma);
        }
        sb.append(project);

        data.set_Projects(sb.toString());
    }

    public void removeProject(String project){
        data.remove_Project(project);
    }

    /****************************************
     /**** BOTTOM SETTINGS
     ****************************************/
    /********** CHECKBOX **********/
    /**
     * Will only run inthe begining to update the values to what they are checked originally
     */
    public void setUp_CheckBottomSettings(CheckBox cb_SilentMode, CheckBox cb_WindowAlwaysOnTop){
        setUp_CheckBox(cb_SilentMode, SETTINGS_SilentMode);
        setUp_CheckBox(cb_WindowAlwaysOnTop, SETTINGS_OnTopOfWindow);
    }

    private void setUp_CheckBox(CheckBox cb, String prefID){
        cb.setSelected(data.get_CheckBoxSettingValue(prefID));
    }

    public void change_CheckBox_Value(CheckBox cb, String prefID){
        data.set_CheckBoxSettingValue(prefID, (cb.isSelected()));
    }
}
