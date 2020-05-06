package eg.edu.alexu.csd.datastructure.mailServer;
import java.util.Date;

import eg.edu.alexu.csd.datastructure.com.AQueue;
import eg.edu.alexu.csd.datastructure.com.SingleLinked;
import gui.csd.mailServer.Data;

public class Mail implements IMail {
    private Date date;
    private int priority;
    private String subject;
    private String body;
    private Contact sender;
    private AQueue<Contact> receivers;
    private SingleLinked<Object> attachment;

    public Mail(Contact Sender,AQueue<Contact> Receivers,String Subject,String Body,SingleLinked<Object> Attachment,int Priority) {
        sender = Sender;
        receivers = Receivers;
        body = Body;
        subject = Subject;
        attachment =Attachment;
        priority = Priority;
        date = new Date();;
    }
    public Mail() {}
    public Contact getSender() {
        return sender;
    }

    public AQueue<Contact> getReceivers() {
        return receivers;
    }

    public String getBody() {
        return body;
    }

    public String getSubject() {
        return subject;
    }

    public Object getAttachment() {
        return attachment;
    }

    public Date getDate(){
        return date;
    }

    public int getPriority(){
        return priority;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setSender(Contact sender) {
        this.sender = sender;
    }

    public void setReceivers(AQueue<Contact> receivers) {
        this.receivers = receivers;
    }

    public void setAttachment(SingleLinked<Object> attachment) {
        this.attachment = attachment;
    }

    public String toString() {
        String str="";
        //sender
        if(getSender()!=null) {
             str += "From:  " + this.getSender().getEmail() + "\r\n";
        }
        //receivers
        if(this.getReceivers()!=null) {
            if(this.getReceivers().isEmpty()){
                System.out.println("Emptyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
            }
            str += "To: ";
            AQueue<Contact> temp = Data.getCopy(this.getReceivers());
            while (!temp.isEmpty())
                str += temp.dequeue().getEmail() + " " + "\r\n";
        }
        //date
        str += "Date: "+ this.getDate().toString()+"\r\n";

        str += "-------------------------------------------------------------"+"\r\n";
        //subject
        str +="About : "+this.getSubject()+"\r\n";
        str += "-------------------------------------------------------------"+"\r\n";
        //body
        str+=this.getBody();
        return str;
    }
}