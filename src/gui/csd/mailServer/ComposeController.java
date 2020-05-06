package gui.csd.mailServer;


import eg.edu.alexu.csd.datastructure.com.AQueue;
import eg.edu.alexu.csd.datastructure.com.SingleLinked;
import eg.edu.alexu.csd.datastructure.mailServer.Application;
import eg.edu.alexu.csd.datastructure.mailServer.Contact;
import eg.edu.alexu.csd.datastructure.mailServer.Mail;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;


public class ComposeController {
    private String subject;
    private String emailText;

    public static boolean isDraft=false;//To check whether a draft message is called or not.



    SingleLinked<Object> attachments=new SingleLinked<>();

    @FXML
    ComboBox<String> to;
    @FXML
    TextField about;
    @FXML
    TextArea email;
    @FXML
    ComboBox<String> priority;
    @FXML
    private Button send;
    @FXML
    private Button add;
    @FXML
    private Button attach;


    public void initialize(){
        Data.addSound();
        if(isDraft){
            handleDrafted();
        }
        setComboBox();
        to.setEditable(true);
    }
    private void handleDrafted(){
        Mail mail=Data.getDraftMail();
        if(mail==null)
            return;
        email.setText(mail.getBody());
        about.setText(mail.getSubject());
        to.getItems().clear();
        AQueue<String> temp=Data.copyQueue(mail.getReceivers());
        while(!temp.isEmpty()){
            to.getItems().add(temp.dequeue());
        }
        attachments=(SingleLinked<Object>)mail.getAttachment();
        switch (mail.getPriority()){
            case 1:priority.setValue("Very Important");break;//Very
            case 2:priority.setValue("Important");break;//Impo
            case 3:priority.setValue("Medium");break;//med
            case 4:priority.setValue("Not Important");break;//not
            default:priority.setValue(null);
        }
        isDraft=false;
    }
    private void setComboBox(){
        priority.getItems().add("Very Important");
        priority.getItems().add("Important");
        priority.getItems().add("Medium");
        priority.getItems().add("Not Important");
    }
    public void handleTo(){
        String value=to.getValue();
        to.getEditor().setText(value);
    }
    public void handleAbout(){
        subject=about.getText();
    }

    public boolean handleTextArea(){
        emailText=email.getText();
        if(email.getText().isEmpty()||email.getText().trim().isEmpty())
            return true;
        if(emailText.length()>1000){
            //We will show error
            showAlert("Email has exceeded limit!");
            return false;
        }
        else{
            return true;
        }
    }
    public void handleButtons(ActionEvent e){
        Application app = Data.getApp();
        AQueue<String> receivers=getReceivers();
        if(e.getSource().equals(send)) {
            //Check fields are not empty
            //Check the (to) is correct.
            if (( receivers!= null && subject != null && emailText != null) && (receivers.size() != 0 && subject.length() != 0 && emailText.length() != 0)) {
                int prior = getPriority();
                System.out.println("Priority is "+prior);
                AQueue<Contact> contacts = app.compose(receivers);
                if (prior == -1) {
                    showAlert("You must choose a priority!");
                    return;
                } else if (contacts == null) {
                    //check userName
                    printErrors(app.getErrorContacts());
                    return;
                } else {
                    //send
                    Mail mail = new Mail(app.allInformations(),contacts, subject, emailText,attachments, getPriority());
                    app.compose(mail);
                }
            } else {
                showAlert("Field are empty");
                return;
            }
        }
        else{
            Mail mail = new Mail(app.allInformations(),app.compose(receivers), subject, emailText,attachments, getPriority());
            Data.getApp().saveDraft(mail.getReceivers(),mail.getSubject(),mail.getBody(), (SingleLinked<Object>) mail.getAttachment(),mail.getPriority());
            //Save to draft.
        }
        Data.getApp().save();
        Stage stage = (Stage) send.getScene().getWindow();
        stage.close();
    }
    public void handlePlusAndDel(ActionEvent e){
        String choice=to.getEditor().getText();
        to.getEditor().setText("");
        if(choice.isEmpty()||choice.trim().isEmpty())
            return;
        if(e.getSource().equals(add)){
           checkEqual(choice);
           to.getEditor().setText("");
        }
        else{
            for(int i=0;i<to.getItems().size();i++) {
                if (to.getItems().get(i).equals(choice)) {
                    to.getItems().remove(i);
                    return;
                }
            }
        }
    }

    private void checkEqual(String choice){
        for(int i=0;i<to.getItems().size();i++){
            if(to.getItems().get(0).equals(choice))
                return;
        }
        to.getItems().add(choice);
    }
    public void handleAttachments(){
        FileChooser chooser=new FileChooser();
        File file=chooser.showOpenDialog(attach.getScene().getWindow());
        if(file!=null){
            System.out.println(file.toString());
            attachments.add(file);
        }
    }
    private AQueue<String> getReceivers(){
        if(to.getItems().isEmpty())
            return null;
        AQueue<String> contacts=new AQueue<>(to.getItems().size());
        for(int i=0;i<to.getItems().size();i++){
            contacts.enqueue(to.getItems().get(i));
        }
        return  contacts;
    }
    private int getPriority(){
        String prior=priority.getValue();
        if(prior==null||prior.isEmpty())
            return -1;
        switch (prior){
            case "Very Important":return 1;
            case "Important":return 2;
            case "Medium":return 3;
            case "Not Important":return 4;
            default:return -1;
        }
    }
    private void printErrors(SingleLinked<String> errors){
        String error="";
        for(int i=0;i<errors.size();i++){
            error+=errors.get(i)+" is unknown!"+"\n";
        }
        showAlert(error);
    }

    private void showAlert(String message){
        Alert aler=new Alert(Alert.AlertType.ERROR);
        aler.setContentText(message);
        aler.showAndWait();
    }
}



