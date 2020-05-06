package gui.csd.mailServer;

import eg.edu.alexu.csd.datastructure.mailServer.*;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;


public class SignUpController {

    @FXML
    private Label phoneNumberLabel;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private TextField username;
    @FXML
    private CheckBox checkPhone;
    @FXML
    private  TextField phoneNumber;
    @FXML
    private Button signUpButt;
    @FXML
    private  Label ExitLabel;

    boolean hasPhone=false;

    @FXML
    public void initialize()
    {
        Data.addSound();

        phoneNumber.setDisable(true);

//        FileInputStream input=new FileInputStream("src/gui/csd/mailServer/bmw-335i-performance-dkg.jpg");
//        Image image=new Image(input);
//        BackgroundSize size=new BackgroundSize(image.getWidth(),border.getHeight(),true,true,true,false);
//        BackgroundImage image1=new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,size);
//        // create a background fill
////        BackgroundFill background_fill = new BackgroundFill(Color.PINK,
////                CornerRadii.EMPTY, Insets.EMPTY);
//
//        // create Background
//        Background background = new Background(image1);
//        border.setBackground(background);
    }
    @FXML
    public void handleSignUpButton() {
        boolean check = checkEmail() && checkPassWord() && checkUserName();
        if (hasPhone) {
            check = check && checkPhone();
        }
        if (check) {
            //Now we do the sign up.
//            Contact contact=new Contact(email.getText(),password.getText());
            Contact contact = new Contact(email.getText(), username.getText(), password.getText());
            if (hasPhone)
                contact.setPhoneNumber(phoneNumber.getText());
            if (!Data.getApp().signup(contact)) {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Choose another email or password,please.");
                alert.show();
                return;
            }
            FadeTransition fd = new FadeTransition();
            ExitLabel.setText("Loading signing up.This may take few seconds");
            ExitLabel.setTextFill(Color.BLACK);
            ExitLabel.setFont(Font.font(30));
            fd.setFromValue(0);
            fd.setToValue(1);
            fd.setNode(ExitLabel);
            fd.setDuration(Duration.seconds(2));
            fd.setCycleCount(4);
            fd.setAutoReverse(true);
            fd.play();
            fd.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Data.getApp().save();

                    Stage stage = (Stage) signUpButt.getScene().getWindow();
                    stage.close();
                }
            });

        }

    }
    @FXML
    public void isPhoneNumber(){
        if(checkPhone.isSelected()){
            phoneNumber.setDisable(false);
            hasPhone=true;
        }
        else{
            phoneNumber.setDisable(true);
            hasPhone=false;
            phoneNumberLabel.setText("");
        }
    }
    public boolean checkPhone(){
        phoneNumberLabel.setText("");
        String phoneNumber=this.phoneNumber.getText();
        if(phoneNumber.length()<5) {
            phoneNumberLabel.setText("Short number!");
            return false;
        }
        for(int i=0;i<phoneNumber.length();i++){
            char character=phoneNumber.charAt(i);
            if(character<'0'||character>'9'){
                phoneNumberLabel.setText("Invalid Input in phone number");
                return false;
            }
        }
        phoneNumberLabel.setText("");
        return true;
    }
    public boolean checkEmail(){
        String email=this.email.getText();
        if(email.length()<5){
            emailLabel.setText("Invalid Email!");
            return false;
        }
        for(int i=0;i<email.length();i++){
            char character=email.charAt(i);
            if(character>='a'&&character<='z')
                continue;
            if(character>='A'&&character<='Z')
                continue;
            if(character=='-'||character=='.') {
            }
            else{
                emailLabel.setText("Invalid Email");
                return false;
            }
        }
        emailLabel.setText("");
        return true;
    }
    public boolean checkUserName(){
        boolean user=username.getText().isEmpty()||username.getText().trim().isEmpty();
        if(user)
            userNameLabel.setText("Invalid username");
        else{
            userNameLabel.setText("");
        }
        return (!user);
    }
    public boolean checkPassWord(){
        boolean letter=false;
        boolean number=false;
        String pass=password.getText();
        if(pass.length()<5){
            passwordLabel.setText("Very short Password.");
            return false;
        }
        for(int i=0;i<pass.length();i++){
            char character=pass.charAt(i);
            if((character>='a'&&character<='z')||(character>='A'&&character<='Z'))
                letter=true;
             else if(character>='0'&&character<='9')
                 number=true;
             if(number&&letter)
                 break;
        }
        if(!number){
            passwordLabel.setText("Password must contain at least one number!");
            return false;
        }
        if(!letter){
            passwordLabel.setText("Password must contain at least one letter!");
            return false;
        }
        passwordLabel.setText("");
        return true;
    }
}
