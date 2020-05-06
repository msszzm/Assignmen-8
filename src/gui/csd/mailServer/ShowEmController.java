package gui.csd.mailServer;

import eg.edu.alexu.csd.datastructure.com.SingleLinked;
import eg.edu.alexu.csd.datastructure.mailServer.Filter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class ShowEmController {
        private static String sort;
        private static SingleLinked<String> filter=new SingleLinked<>();




        @FXML
        private RadioButton date;
        @FXML
        private RadioButton receiver;
        @FXML
        private RadioButton subject;
        @FXML
        private RadioButton body;
        @FXML
        private RadioButton prior;


        @FXML
        private TextField filsub;
        @FXML
        private TextField filbody;
        @FXML
        private TextField filrec;
        @FXML
        private ComboBox<String> combo;
        @FXML
        private Button ok;
        @FXML
        private Button can;

        public void initialize(){
                setCombo();
                sort="date";
        }
        public void handleButtons(ActionEvent e){
                if(e.getSource().equals(ok)){
                        handleFilter();
                }
                else{
                        filter=new SingleLinked<>();
                        sort=null;
                }
                Stage stage= (Stage) ok.getScene().getWindow();
                stage.close();
        }
        public void setCombo(){
                combo.getItems().add("Very Important");
                combo.getItems().add("Important");
                combo.getItems().add("Medium");
                combo.getItems().add("Not Important");
                combo.getItems().add("None");
        }
    public void handleSort(ActionEvent e){
        if(e.getSource().equals(date)){
                sort="date";
        }
        else if(e.getSource().equals(receiver)){
                sort="receiver";

        }
        else if(e.getSource().equals(subject)){
                sort="head";
        }
        else if(e.getSource().equals(body)){
                sort="body";
        }
        else{
                sort="priority";
            }

        }
        public void handleFilter(){
                filter.add("Sender");
                String temp=filrec.getText();
                if(!(temp.isEmpty()||temp.trim().isEmpty())){
                        filter.add(temp);
                }
                else
                        filter.add(null);
                filter.add("head");
                temp=filsub.getText();
                if(!(temp.isEmpty()||temp.trim().isEmpty())){
                        filter.add(temp);
                }
                else
                        filter.add(null);
                filter.add("body");
                temp=filbody.getText();
                if(!(temp.isEmpty()||temp.trim().isEmpty())){
                        filter.add(temp);
                }
                else
                        filter.add(null);
                filter.add("priority");
                temp=combo.getValue();
                if(temp!=null&& !temp.equals("None")){
                        filter.add(temp);
                }
                else
                        filter.add(null);

        }

        public static String getSort() {
                return sort;
        }

        public static SingleLinked<String> getFilter() {
                return filter;
        }
}
