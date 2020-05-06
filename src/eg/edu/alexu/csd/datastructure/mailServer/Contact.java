package eg.edu.alexu.csd.datastructure.mailServer;
import eg.edu.alexu.csd.datastructure.com.AQueue;
import eg.edu.alexu.csd.datastructure.com.SingleLinked;


import java.util.Date;

public class Contact implements IContact {
    private String email;
    private String userName;
    private String password;
    private String phoneNumber;

    private Folder inbox = new Folder("inbox");
    private Folder sent = new Folder("sent");
    private Folder draft = new Folder("draft");
    private Folder trash = new Folder("trash");


    private SingleLinked<UserContact> userContacts=new SingleLinked<>();

    public Contact(String email, String password) {
        this.email=email;
        this.password = password;
    }

    public Contact(String email, String userName, String password) {
        this.email = email;
        this.userName = userName;
        this.password = password;
    }


    public void setUserContacts(SingleLinked<UserContact> userContacts) {
        this.userContacts = userContacts;
    }


    public Contact(){}
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }


    public void setInbox(Folder inbox){
        this.inbox= inbox;
    }
    public void setSent(Folder sent){
        this.sent = sent;
    }

    public void setDraft(Folder draft){
        this.draft = draft;
    }

    public void setTrash(Folder trash){
        this.trash =trash;
    }


    public Folder getInbox(){
        return inbox;
    }
    public Folder getSent(){
        return sent;
    }

    public Folder getDraft(){
        return draft;
    }

    public Folder getTrash(){
        return trash;
    }

    public void moveEmails(SingleLinked<Mail> Mails, IFolder des){
        switch (des.getNameFolder()) {
            case "inbox":
                for(int i =0;i<Mails.size();i++)
                    inbox.addMail(Mails.get(i));
                break;
            case "sent":
                for(int i =0;i<Mails.size();i++)
                    sent.addMail(Mails.get(i));
                break;
            case "draft":
                for(int i =0;i<Mails.size();i++)
                    draft.addMail(Mails.get(i));
                break;
            default:
                for(int i =0;i<Mails.size();i++)
                    trash.addMail(Mails.get(i));
                break;
        }

    }

    public Folder getFolder (String orginalfolder) {
        switch (orginalfolder) {
            case "inbox":
                return this.inbox;

            case "sent":
                return this.sent;

            case "draft":
                return this.draft;

            default:
                return this.trash;

        }
    }



    public Mail[] loadpage(Folder folder,int pageno) {
        Mail[] page = new Mail[10];
        for(int i = 0;i<10;i++)
            page[i] = folder.getMail(i+(pageno-1)*10);
        return page;
    }

    public void recieveMail(Mail mail) {
        inbox.addMail(mail);
        UserContact cont=getSavedContact(mail.getSender());
        if(cont==null)
            userContacts.add(new UserContact(mail.getSender()));
    }

    public void savetodraft(AQueue<Contact> Receivers,String Subject,String Body,SingleLinked<Object> Attachment,int Priority) {
        draft.addMail( new Mail(this,Receivers,Subject,Body,Attachment,Priority));
    }

    public void sendMail(AQueue<Contact> Receivers,String Subject,String Body,SingleLinked<Object> Attachment,int Priority) {
        Mail newMail = new Mail(this,Receivers,Subject,Body,Attachment,Priority);
        sent.addMail(newMail);
        AQueue<Contact> temp=new AQueue<>(Receivers.size());
        AQueue<Contact> copy=new AQueue<>(Receivers.size());
        while(!Receivers.isEmpty()){
            Contact con=Receivers.dequeue();
            temp.enqueue(con);
            copy.enqueue(con);
        }
        Receivers=temp;
        newMail.setReceivers(Receivers);
        while(!copy.isEmpty()) {
            Contact theContact=copy.dequeue();
            theContact.recieveMail(newMail);
            UserContact x=getSavedContact(theContact);
            if(x==null)
                userContacts.add(new UserContact(theContact));
        }
    }

    public void deleteMails(SingleLinked<Mail> selectedMails,Folder folder) {
        for(int i =0;i<selectedMails.size();i++)
            trash.addDeletedMail(new DeletedMail(selectedMails.get(i), new Date(),folder));
        for(int i =0;i<selectedMails.size();i++)
            folder.removeMail(selectedMails.get(i));
    }

    public void deletefromTrash(SingleLinked<DeletedMail> selectedMails) {
        for(int i =0;i<selectedMails.size();i++)
            trash.removeDeletedMail(selectedMails.get(i));
    }

    public void autodeletefromTrash() {
        for(int i =1;i<=trash.noofMails();i++)
            if((((DeletedMail) trash.getDeletedMail(i)).getDeletedDate()).getTime()-(new Date()).getTime()>=2629746*1000)
                trash.removeMail(trash.getDeletedMail(i));
    }

    public void restorefromtrash(SingleLinked<DeletedMail> selectedMails) {
        for(int i =0;i<selectedMails.size();i++)
            selectedMails.get(i).getOrginalFolder().addMail(selectedMails.get(i).getDeletedMail());
        for(int i =0;i<selectedMails.size();i++)
            trash.removeDeletedMail(selectedMails.get(i));
    }

    public void getadraftedmail(Mail mail) {

    }
    //////////////////////////////////////////////////////////////////
    @Override
    public boolean signin(String password) {
        if(!password.equals(this.password))
            throw new RuntimeException("Incorrect password");
        return true;
    }

    public UserContact getSavedContact(Contact contact){
        for(int i=0;i<userContacts.size();i++){
            if(userContacts.get(i).getContact().equals(contact))
                return userContacts.get(i);
        }
        return null;
    }

    public SingleLinked<UserContact> getUserContacts() {
        return userContacts;
    }
    public UserContact isNameThere(String nickname){
        for(int i=0;i<userContacts.size();i++){
            if(nickname.equals(userContacts.get(i).getNickname()))
                return userContacts.get(i);

        }
        return null;
    }

}