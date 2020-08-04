package org.allvens.controller;

import org.allvens.controller_ui.controller_managers.Home_Manager;
import org.allvens.controller_ui.controller_methods.Common_ControllerMethods;
import org.allvens.assets.Constants;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.WindowEvent;

public class Home extends Common_ControllerMethods{

	@FXML
	BorderPane bpAll;
	@FXML
	Label lbTimer;
	@FXML
	Label lbTimerPlaying;
	@FXML
	Button btnPlayAndPause;
	@FXML
	Spinner<String> spProjectContainer;
	@FXML
	ProgressBar pbTimerTracker;

	private Home_Manager hm;

    /**
	 * set values of timer
	 * saves values and resets if things change
	 */
	public void initialize() {
        screen_SetSize(bpAll);
        hm = new Home_Manager(bpAll, lbTimerPlaying, lbTimer, pbTimerTracker, btnPlayAndPause);
        hm.setSpinner(spProjectContainer);
	}

    @FXML
	public void btn_ScreenChange_Settings(ActionEvent e){
        hm.saveCurrentData();
        changeScreen(Common_ControllerMethods.CHANGE_SCREEN_DYNAMIC_ALWAYS_ON_TOP, Constants.FILE_SCREEN_Settings, e, Constants.WINDOW_TITLE_Settings, bpAll, false);
	}

	@FXML
	public void btn_ScreenChange_Graph(ActionEvent e){
        hm.saveCurrentData();
        changeScreen(Common_ControllerMethods.CHANGE_SCREEN_DYNAMIC_ALWAYS_ON_TOP, Constants.FILE_SCREEN_Chart, e, Constants.WINDOW_TITLE_Charts, bpAll, false);
	}

    /**
     * resumes current created timer
     * @param e
     */
	@FXML
	public void btn_ResumePauseTimer(ActionEvent e){
	    hm.playPause_Timer();
	    setUpButtonAndKeyboardBindings(e);
	}

    /**
     * Skips current timer
     * @param e
     */
	@FXML
	public void btn_SkipCurrentTimer(ActionEvent e){
        hm.skip_Timer();
	}

    /**
     * Resets Timer
     * @param e
     */
	@FXML
	public void btn_ResetTimer(ActionEvent e){
        hm.reset_Timer();
	}

    /**
     * Add Five Minutes To Timer
     * @param e
     */
	@FXML
	public void btn_AddFiveMin(ActionEvent e){
        hm.add5Min_Timer();
	}

    private void setUpButtonAndKeyboardBindings(ActionEvent e){

        (((Button)e.getSource()).getScene()).setOnKeyPressed(event -> {
//            System.out.println("Pressed: " + event.getCharacter());
            if(event.getCode().equals(KeyCode.PLUS) | event.getCode().equals(KeyCode.NUMPAD5)){
                hm.add5Min_Timer();
            }
            if(event.getCode().equals(KeyCode.SPACE)){
                hm.playPause_Timer();
            }
            if(event.getCode().equals(KeyCode.R) | event.getCode().equals(KeyCode.LEFT)){
                hm.reset_Timer();
            }
            if(event.getCode().equals(KeyCode.N) | event.getCode().equals(KeyCode.RIGHT)){
                hm.skip_Timer();
            }
            // todo REMOVE DEBUG METHODS
            if(event.getCode().equals(KeyCode.A)){
                hm.debug_add10MinToTimer();
            }
            // todo REMOVE DEBUG METHODS
            if(event.getCode().equals(KeyCode.D)){
                hm.debug_ResetAllValuesToDefault();
            }
        });

        ((((Button)e.getSource()).getScene().getWindow())).setOnCloseRequest(event -> hm.saveCurrentData());
    }
}
