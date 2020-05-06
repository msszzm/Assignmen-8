package gui.csd.mailServer;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;


public class ExperimentController {
    @FXML
    private Label w;
    @FXML
    private Label e;
    @FXML
    private Label l;
    @FXML
    private Label c;
    @FXML
    private Label o;
    @FXML
    private Label m;
    @FXML
    private Label ee;
    @FXML
     private Line line;
    @FXML
    private AnchorPane all;


    public void initialize()  {
        BackgroundFill fill=new BackgroundFill(Color.valueOf("1A1696"), CornerRadii.EMPTY, Insets.EMPTY);
        all.setBackground(new Background(fill));
        FadeTransition fdw = new FadeTransition(Duration.seconds(1));
        fdw.setNode(w);
        w.setTextFill(Color.valueOf("FF2553"));
        FadeTransition fde = new FadeTransition(Duration.seconds(2));
        fdw.setNode(e);
        e.setTextFill(Color.valueOf("FF2553"));
        FadeTransition fdl = new FadeTransition(Duration.seconds(3));
        fdw.setNode(l);
        l.setTextFill(Color.valueOf("FF2553"));
        FadeTransition fdc = new FadeTransition(Duration.seconds(4));
        fdw.setNode(c);
        c.setTextFill(Color.valueOf("FF2553"));
        FadeTransition fdo = new FadeTransition(Duration.seconds(5));
        fdw.setNode(o);
        o.setTextFill(Color.valueOf("FF2553"));
        FadeTransition fdm = new FadeTransition(Duration.seconds(6));
        fdw.setNode(m);
        m.setTextFill(Color.valueOf("FF2553"));
        FadeTransition fdee = new FadeTransition(Duration.seconds(7));
        fdw.setNode(ee);
        ee.setTextFill(Color.valueOf("FF2553"));
        fade(fdw);
        fade(fde);
        fade(fdl);
        fade(fdc);
        fade(fdo);
        fade(fdm);
        fade(fdee);

        TranslateTransition tr = new TranslateTransition();
        tr.setNode(line);
        tr.setToX(1000);
        tr.setDuration(Duration.seconds(5));
        tr.setAutoReverse(true);

        ParallelTransition comp = new ParallelTransition();
        comp.getChildren().add(fde);
        comp.getChildren().add(fdw);
        comp.getChildren().add(fdl);
        comp.getChildren().add(fdc);
        comp.getChildren().add(fdo);
        comp.getChildren().add(fdm);
        comp.getChildren().add(fdee);
        comp.getChildren().add(tr);
        comp.setNode(all);
        comp.play();
        comp.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Parent root;
                try {
                    root = FXMLLoader.load(getClass().getResource("sample.fxml"));

                    Stage stage = new Stage();
                    assert root != null;
                    stage.setScene(new Scene(root, 1200, 500));
                    stage.setTitle("aaa");
                    stage.show();
                } catch (Exception ex) {
                    Alert alert=new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Couldn't load program!");
                    alert.show();
                }

                Stage st =(Stage)all.getScene().getWindow();
                st.close();
            }
        });

    }
    public void fade(FadeTransition fd){
        fd.setFromValue(0);
        fd.setToValue(4);
    }

}
