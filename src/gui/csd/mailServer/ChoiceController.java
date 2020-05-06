package gui.csd.mailServer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ChoiceController {
    public static String item;

    @FXML
    private  Label text;
    @FXML
    private Button yes;

    public  static  boolean choice;
    public void initialize(){
        if(item==null){
            item="Are you sure";
        }
        text.setText(item);
        item=null;
    }
    public void handleButtons(ActionEvent e) {
        choice = e.getSource().equals(yes);
        Stage st = (Stage) yes.getScene().getWindow();
        st.close();
    }

}
