package gui.csd.mailServer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NickNameController {
    @FXML
    private TextField name;
    @FXML
    private Button ok;

    private static String nickname;

   public void handleButtons(ActionEvent e){
       if(e.getSource().equals(ok)){
           nickname=name.getText();
       }
       Stage st= (Stage) ok.getScene().getWindow();
       st.close();
   }

    public static String getNickname() {
       String name=nickname;
       nickname=null;
        return name;
    }
}
