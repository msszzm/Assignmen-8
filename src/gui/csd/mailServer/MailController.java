package gui.csd.mailServer;

import eg.edu.alexu.csd.datastructure.mailServer.Mail;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class MailController {


    private Mail mail=Data.getSelectedMail();
    @FXML
    private Label to;
    @FXML
    private Label from;
    @FXML
    private Label subject;
    @FXML
    private TextArea body;
    @FXML
    private Button undo;;



    public void initialize(){
        to.setText(mail.getReceivers().dequeue().getEmail());
        from.setText(mail.getSender().getEmail());
        subject.setText(mail.getSubject());
        body.setText(mail.getBody());
        setFills(to);
        setFills(from);
        setFills(subject);
        body.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        undo.setDisable(true);
    }

    public void setFills(Label x){
        x.setTextFill(Color.BLUE);
        Font font=new Font("Bold",20);
        x.setFont(font);
    }

    public void handleOKAndUndo(){
            Data.getApp().save();
            Stage st=(Stage)undo.getScene().getWindow();
            st.close();
        }

}
