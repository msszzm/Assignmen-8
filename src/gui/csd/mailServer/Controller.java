package gui.csd.mailServer;

import eg.edu.alexu.csd.datastructure.mailServer.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.*;

public class Controller{
    public Label signin;
    @FXML
    private GridPane center;
    @FXML
    private TextField signInEmail;
    @FXML
    private PasswordField signInPassword;
    @FXML
    private Button signInButt;
    @FXML
    private  Button signUp;
    @FXML
    private BorderPane border;


    public void initialize() {
        signin.setTextFill(Color.valueOf("4242E2"));
        Data.getApp().load();
        Data.addSound();
        signInButt.setDisable(true);
        Background background;
    try {
        FileInputStream input=new FileInputStream("src/gui/csd/mailServer/new_570xN.1426349688_vanilla.jpg");
        Image image=new Image(input);
        BackgroundSize size=new BackgroundSize(image.getWidth(),image.getHeight(),true,true,true,false);
        BackgroundImage image1=new BackgroundImage(image,BackgroundRepeat.ROUND,BackgroundRepeat.SPACE,BackgroundPosition.CENTER,size);
        background = new Background(image1);
    }
    catch (FileNotFoundException e){
        BackgroundFill back1=new BackgroundFill(Color.RED,CornerRadii.EMPTY,Insets.EMPTY);
        background=new Background(back1);
    }
        // create a background fill
//        BackgroundFill background_fill = new BackgroundFill(Color.PINK,
//                CornerRadii.EMPTY, Insets.EMPTY);

        // create Background
        border.setBackground(background);
        addImage();
    }
    public void addImage(){
        Background background;
        try{
            FileInputStream input=new FileInputStream("src/gui/csd/mailServer/blue-sunScreen-paper-background-layer-hd-575x400.png");
            Image image=new Image(input);
            BackgroundSize size=new BackgroundSize(center.getWidth(),border.getHeight(),true,false,true,true);
            BackgroundImage image1=new BackgroundImage(image,BackgroundRepeat.ROUND,BackgroundRepeat.SPACE,BackgroundPosition.CENTER,size);
            background = new Background(image1);        }
        catch (FileNotFoundException e) {
            Color x=Color.valueOf("1A1696");
            BackgroundFill fill=new BackgroundFill(x,CornerRadii.EMPTY,Insets.EMPTY);
            background=new Background(fill);
        }
        center.setBackground(background);
    }

    public void disableSignIn(){
        boolean emailNotFound=signInEmail.getText().isEmpty()||signInEmail.getText().trim().isEmpty();
        boolean passwordNotFound=signInPassword.getText().isEmpty()||signInPassword.getText().trim().isEmpty();
        signInButt.setDisable(emailNotFound||passwordNotFound);
    }
    @FXML
    public void signIn() throws IOException {
        Application app= Data.getApp();
        boolean signedIn=app.signin(signInEmail.getText(),signInPassword.getText());
        if(signedIn) {
            Parent root = FXMLLoader.load(getClass().getResource("account.fxml"));
            Stage signInStage = new Stage();
            signInStage.setScene(new Scene(root, 1000, 600));
            signInStage.setTitle(Data.getApp().allInformations().getEmail());
            signInStage.show();
            Stage st= (Stage) signInButt.getScene().getWindow();
            st.close();
        }
        else{
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Incorrect email or password!");
            alert.show();
        }
    }
    @FXML
    public void signUp(ActionEvent e) throws IOException {
        if(e.getSource().equals(signUp)){
            Parent root=FXMLLoader.load(getClass().getResource("signUP.fxml"));
            Stage stage=new Stage();
            stage.setTitle("Sign UP");
            stage.setScene(new Scene(root,700,550));
            stage.show();
        }
    }
}
