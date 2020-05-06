package eg.edu.alexu.csd.datastructure.mailServer;
import eg.edu.alexu.csd.datastructure.com.AQueue;
import eg.edu.alexu.csd.datastructure.com.SingleLinked;

public interface IContact {
    public String getEmail();
    public void setEmail(String email);

    public String getUserName();
    public void setUserName(String userName);

    public String getPassword() ;
    public void setPassword(String password);

    public void setPhoneNumber(String phoneNumber);
    public String getPhoneNumber();

    public Mail[] loadpage(Folder folder, int pageno);
    public void recieveMail(Mail mail);
    public void savetodraft(AQueue<Contact> Receivers,String Subject,String Body,SingleLinked<Object> Attachment,int Priority);
    public void sendMail(AQueue<Contact> Receivers,String Subject,String Body,SingleLinked<Object> Attachment,int Priority);
    public void deleteMails(SingleLinked<Mail> selectedMails,Folder folder);
    public void deletefromTrash(SingleLinked<DeletedMail> selectedMails);


    public boolean signin(String password);
}