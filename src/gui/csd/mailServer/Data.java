package gui.csd.mailServer;

import eg.edu.alexu.csd.datastructure.com.AQueue;
import eg.edu.alexu.csd.datastructure.mailServer.Application;
import eg.edu.alexu.csd.datastructure.mailServer.Contact;
import eg.edu.alexu.csd.datastructure.mailServer.Mail;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class Data {
    private static Application app=new Application();
    private static Mail draftMail;
    private static Mail selectedMail;

    public static Application getApp() {
        return app;
    }
    public static void setDraftMail(Mail draftMail){
        Data.draftMail=draftMail;
    }
    public static Mail getDraftMail(){
        return draftMail;
    }

    public static AQueue<String> copyQueue(AQueue<Contact> contacts){
        AQueue<String> temp=new AQueue<>(contacts.size());
        AQueue<Contact> copy=new AQueue<>(contacts.size());
        while(!contacts.isEmpty()){
            Contact contact=contacts.dequeue();
            temp.enqueue(contact.getEmail());
            copy.enqueue(contact);
        }
        contacts=copy;
        return temp;
    }

    public static AQueue<Contact> getCopy(AQueue<Contact> queu){
        if(queu==null)
            return null;
       AQueue<Contact> temp=new AQueue<>(queu.size());
        AQueue<Contact> copy=new AQueue<>(queu.size());
        while(!queu.isEmpty()){
            Contact qu=queu.dequeue();
            temp.enqueue(qu);
            copy.enqueue(qu);
        }
        System.out.println("Size of temp is "+temp.size());
        while(!temp.isEmpty()) {
            System.out.println("Size of queue is "+queu.size());
            queu.enqueue(temp.dequeue());
        }
        queu=temp;
        return copy ;
    }

    public static Mail getSelectedMail() {
        return selectedMail;
    }

    public static void setSelectedMail(Mail selectedMail) {
        Data.selectedMail = selectedMail;
    }

    public static void addSound(){
        try {
            String uriString = new File("src/gui/csd/mailServer/Blop-Mark_DiAngelo-79054334.wav").toURI().toString();
            MediaPlayer player = new MediaPlayer(new Media(uriString));
            player.play();
        }
        catch (Exception ignored){}
    }

}
