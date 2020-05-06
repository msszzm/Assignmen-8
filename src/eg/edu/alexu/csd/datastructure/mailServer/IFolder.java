package eg.edu.alexu.csd.datastructure.mailServer;

public interface IFolder {
    public String getNameFolder();

    public void addMail(Mail mail);
    public int noofMails();
    public Mail getMail(int index);
    public void removeMail(Mail mail);
    public void addDeletedMail(DeletedMail mail);
    public int noofDeletedMails();
    public DeletedMail getDeletedMail(int index);
    public void removeDeletedMail(Mail mail) ;
}