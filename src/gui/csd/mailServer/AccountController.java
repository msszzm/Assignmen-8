package gui.csd.mailServer;


import eg.edu.alexu.csd.datastructure.com.AQueue;
import eg.edu.alexu.csd.datastructure.com.DoubleList;
import eg.edu.alexu.csd.datastructure.com.SingleLinked;
import eg.edu.alexu.csd.datastructure.mailServer.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import java.io.IOException;

public class AccountController {
    // we get the contact from the other class as it will be a getter
    private Contact contact=Data.getApp().allInformations();
    private SingleLinked<Mail> mails=new SingleLinked<>();
    @FXML
    private Label user;
    @FXML
    private ListView<String> listView;
    @FXML
    private ListView<String> content;
    @FXML
    private Button moveButton;
    @FXML
    private Button delButton;
    @FXML
    private Button compose;
    @FXML
    private Button show;
    @FXML
    private Button restore;
    @FXML
    private Button edit;


    boolean inTrash=false;
    boolean showContact=false;








    public void initialize(){
        Data.addSound();
        listViewDesign();
    }
    private void listViewDesign(){
        user.setText(contact.getUserName());
        BackgroundFill bk=new BackgroundFill(Color.valueOf("A69C93"),CornerRadii.EMPTY,Insets.EMPTY);
        listView.setBackground(new Background(bk));
        listView.getItems().add("(+) Compose ");
        listView.getItems().add("Show Inbox ");
        listView.getItems().add("Show Sent Mails");
        listView.getItems().add("Show Draft Mails");
        listView.getItems().add("Show Trash Mails");
        listView.getItems().add("Show Contacts");
        listView.getItems().add("Log Out");
        listView.setFixedCellSize(50);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        listView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> stringListView) {
                return new ListCell<String>(){
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty) {
                            CornerRadii rad=new CornerRadii(10);
                            Insets inset=new Insets(2,2,2,2);
                            Color color=new Color(0.2,0.5,0.9,0.8);
                            BackgroundFill fill=new BackgroundFill(color,rad, inset);
                            Background back=new Background(fill);
                            setBackground(back);
                            setText(item);
                            Font font=new Font("Times new roman Bold",20);
                            setFont(font);
                            setTextFill(Color.BLACK);
                        }
                        else
                            setBackground(new Background(bk));
                    }
                };
            }
        });
    }
    public void listViewHandle() throws IOException {
        listView.setDisable(true);
        String item=listView.getSelectionModel().getSelectedItem();
        if(item==null) {
            listView.setDisable(false);
            return;
        }
        if(item.equals(listView.getItems().get(0))){
            ComposeController.isDraft=false;
            content.getItems().clear();
            Parent root= FXMLLoader.load(getClass().getResource("compose.fxml"));
            Stage composeStage=new Stage();
            composeStage.setScene(new Scene(root,1000,600));
            composeStage.setTitle("Create email");
            composeStage.showAndWait();
            ComposeController.isDraft=false;
        }
        else{
            show(item);
        }
        listView.setDisable(true);
        //Now we want to disable listView until actions are done.
        listView.setDisable(false);

    }
    private void show(String item) throws IOException {
        showContact=false;
        edit.setDisable(true);
        inTrash=false;
        content.getItems().clear();
        Folder folder;
        if(item.equals(listView.getItems().get(5))) {
            showContacts();
            contentDecor();
            return;
        }
        contentDecor();
        if(item.equals(listView.getItems().get(1)))//Inbox
        { folder=contact.getInbox(); }
        else if(item.equals(listView.getItems().get(2)))//Sent
        { folder=contact.getSent(); }
        else if(item.equals(listView.getItems().get(3))){//Draft
             folder=contact.getDraft();
             ComposeController.isDraft=true;
        }
        else if(item.equals(listView.getItems().get(6))) {
            handleLogOut();
            return;
        }
        else{//Trash
            folder=contact.getTrash();
        inTrash=true;
        }
        Stage stage=new Stage();
        Parent root=FXMLLoader.load(getClass().getResource("showEm.fxml"));
        stage.setTitle("Show options");
        stage.setScene(new Scene(root,500,500));
        stage.showAndWait();
        if(ShowEmController.getFilter()==null)
            return;
        mails.clear();
        Filter filter=new Filter();
        filter.setFilterList(ShowEmController.getFilter());
        Sort sort=new Sort();
        sort.setSelectedSort(ShowEmController.getSort());
        Data.getApp().setViewingOptions(folder,filter,sort);
        int pages=Data.getApp().noPages();
        for(int i=0;i<=pages;i++){
            IMail[] mails=Data.getApp().listEmails(i);
            if(mails!=null){
                for(int j=0;j<=mails.length;j++){
                    System.out.println("On page "+j);
                    System.out.println("Size of mails is "+mails.length);
                    if(mails[j]==null){
                        System.out.println("it's null");
                        break;
                    }
                    this.mails.add((Mail) mails[j]);
                    content.getItems().add(mails[j].toString());
                }
                }
            else{
                Alert alert=new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("No mails found");
                alert.show();
            }
            }
        content.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }
    public void handleContent() {
        //We need to save the content of mail.
        //We have the account contact.
        if(showContact){
            edit.setDisable(!(content.getSelectionModel().getSelectedItems().size()==1));
            return;
        }
        moveButton.setDisable(content.getSelectionModel().getSelectedItems().isEmpty());
        delButton.setDisable(content.getSelectionModel().getSelectedItems().isEmpty());
        restore.setDisable((!inTrash)||content.getSelectionModel().getSelectedItems().isEmpty());
        show.setDisable(content.getSelectionModel().getSelectedItems().size()!=1);

        if(ComposeController.isDraft&&content.getSelectionModel().getSelectedItems().size()==1)
            compose.setDisable(false);
        else
            compose.setDisable(true);

    }
    public void handleButtons(ActionEvent e) throws IOException {
        SingleLinked<Mail> selectedMails=new SingleLinked<>();
        for(int i=0;i<content.getSelectionModel().getSelectedItems().size();i++){
            Mail temp=getTheMail(content.getSelectionModel().getSelectedItems().get(i));
            selectedMails.add(temp);
        }
        //Now we have our linkedList.
        Parent root =FXMLLoader.load(getClass().getResource("choice.fxml"));
        Stage stage=new Stage();
        stage.setTitle("Check");
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                ChoiceController.choice=false;
            }
        });
        stage.setScene(new Scene(root,450,150));
        if(e.getSource().equals(moveButton)){
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Nothing to be done here.");
            alert.showAndWait();
        }
        else{//Delete Button
            ChoiceController.item="Are you sure you want to delete?";
            stage.showAndWait();
            if(ChoiceController.choice){
                Data.getApp().deleteEmails(selectedMails);
                content.getItems().clear();
                Data.getApp().deleteFolders();
                Data.getApp().save();
            }
        }


    }




    private Mail getMail(String item){
        for(int i=0;i<mails.size();i++){
            if(mails.get(i).toString().equals(item))
                return mails.get(i);
        }
        return null;
    }
    private Mail getTheMail(String item){
        Mail temp=getMail(item);
        if (temp.getSender() == null) {//then the account is the sender
            temp.setSender(contact);
        }
        else if(temp.getReceivers()==null){//then receiver is account
            AQueue<Contact> recs=new AQueue<>(1);
            recs.enqueue(contact);
            temp.setReceivers(recs);
        }
        return temp;
    }
    public void handleCompose() throws IOException {
        Mail temp=getTheMail(content.getSelectionModel().getSelectedItem());
        Data.setDraftMail(temp);
        Parent root= FXMLLoader.load(getClass().getResource("compose.fxml"));
        Stage composeStage=new Stage();
        composeStage.setScene(new Scene(root,1000,600));
        composeStage.setTitle("Create email");
        composeStage.show();
        content.getItems().clear();
    }
    public void handleShow() throws IOException {
        Mail temp=getTheMail(content.getSelectionModel().getSelectedItem());
        Data.setSelectedMail(temp);
        Parent root=FXMLLoader.load(getClass().getResource("mail.fxml"));
        Stage st=new Stage();
        st.setTitle("E-mail");
        st.setScene(new Scene(root,1200,700));
        st.showAndWait();
    }
    public void handleRestore() throws IOException {
        //We have mails that have the mails.

        SingleLinked temp=new SingleLinked();
        for(int i=0;i<content.getSelectionModel().getSelectedItems().size();i++){
            temp.add(getTrash(content.getSelectionModel().getSelectedItems().get(i)));
        }

        Parent root =FXMLLoader.load(getClass().getResource("choice.fxml"));
        Stage stage=new Stage();
        stage.setTitle("Check");
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                ChoiceController.choice=false;
            }
        });
        stage.setScene(new Scene(root,450,150));
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                ChoiceController.choice=false;
            }
        });
        stage.showAndWait();
        if(ChoiceController.choice){
            Data.getApp().restore(temp);
            Data.getApp().save();
        }
        content.getItems().clear();
    }
    public DeletedMail getTrash(String item){
        DoubleList<DeletedMail> x=contact.getTrash().getDeletedMails();
        for(int i=0;i<x.size();i++){
            if(item.equals(x.get(i).getDeletedMail().toString())){
                return x.get(i);
            }
        }
        return null;
    }
    private void showContacts(){
        showContact=true;
        content.getItems().clear();
        SingleLinked<UserContact> list=Data.getApp().getContacts();
        for(int i=0;i<list.size();i++){
            content.getItems().add(list.get(i).getNickname());
        }
    }
    public void handleEdit() throws IOException {
        String selected=content.getSelectionModel().getSelectedItem();
        Parent root=FXMLLoader.load(getClass().getResource("nickName.fxml"));
        Stage st=new Stage();
        st.setTitle("Set a nickname");
        st.setScene(new Scene(root,500,200));
        st.initOwner(delButton.getScene().getWindow());
        st.showAndWait();
        String name=NickNameController.getNickname();
        if(name==null||name.isEmpty()||name.trim().isEmpty())
            return;
       boolean worked= Data.getApp().changeName(name,selected);
       if(!worked){
           Alert alert=new Alert(Alert.AlertType.ERROR);
           alert.setContentText("Error process failed");
           alert.show();
       }
       else{
           content.getItems().clear();
           edit.setDisable(true);
       }
    }
    private void contentDecor(){
        if(showContact){
            BackgroundFill backgroundFill=new BackgroundFill(Color.DARKGRAY,CornerRadii.EMPTY,Insets.EMPTY);
            content.setBackground(new Background(backgroundFill));
            content.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
                @Override
                public ListCell<String> call(ListView<String> stringListView) {
                    return new ListCell<String>(){
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if(!empty){
                                setText(item);
                                Font font=new Font("Cambria Bold",30);
                                setFont(font);
                                setTextFill(Color.DARKBLUE);
                                Insets in=new Insets(5,5,5,5);
                                CornerRadii rad=new CornerRadii(5);
                                setBackground(new Background(new BackgroundFill(Color.GRAY,rad,in)));

                            }
                        }
                    };
                }
            });


        }
        else{
            content.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
                @Override
                public ListCell<String> call(ListView<String> stringListView) {
                    return new ListCell<String>(){
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if(!empty){
                                setText(item);
                                Font font=new Font("Andalus",20);
                                setFont(font);
                                setTextFill(Color.BLACK);
                                Insets in=new Insets(10,5,10,5);
                                CornerRadii rad=new CornerRadii(8);
                                BackgroundFill bk=new BackgroundFill(Color.DARKGRAY,rad,in);
                                setBackground(new Background(bk));
                            }
                        }
                    };
                }
            });

        }
    }
    public void handleLogOut() throws IOException {
            Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 1200, 500));
            stage.setTitle("aaa");
            stage.show();
        Stage st= (Stage) listView.getScene().getWindow();
        st.close();

    }
}
