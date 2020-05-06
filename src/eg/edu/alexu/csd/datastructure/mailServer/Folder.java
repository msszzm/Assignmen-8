package eg.edu.alexu.csd.datastructure.mailServer;
import eg.edu.alexu.csd.datastructure.com.DoubleList;

public class Folder implements IFolder {
    private DoubleList<Mail> Mails;
    private String name;

    private DoubleList<DeletedMail> DeletedMails;

    public Folder(String name) {
        this.name = name;
    }

    public String getNameFolder(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addMail(Mail mail) {
        if(Mails==null)
            Mails=new DoubleList<>();
        Mails.add(mail);
    }

    public Mail getMail(int index) {
        if(index>Mails.size())
            return null;
        return Mails.get(index);
    }

    public void removeMail(Mail mail) {
        for(int i =0;i<Mails.size();i++)
            if(Mails.get(i)==mail) {
                Mails.remove(i);
                break;
            }
    }

    public int noofMails() {
        if(Mails==null)
            return 0;
        return Mails.size();
    }

    public DoubleList<Mail> getMails() {
        return Mails;
    }

    public void setMails(DoubleList<Mail> mails) {
        Mails = mails;
    }



///////////////////   functions for deleted Mails


    public void addDeletedMail(DeletedMail mail) {
        if(DeletedMails==null)
            DeletedMails=new DoubleList<>();
        DeletedMails.add(mail);
    }

    public DeletedMail getDeletedMail(int index) {
        if(index>DeletedMails.size())
            return null;
        return DeletedMails.get(index);
    }

    public void removeDeletedMail(Mail mail) {
        for(int i =0;i<DeletedMails.size();i++)
            if(DeletedMails.get(i).getDeletedMail()==mail) {
                DeletedMails.remove(i);
                break;
            }
    }

    public int noofDeletedMails() {
        if(DeletedMails==null)
            return 0;
        return DeletedMails.size();
    }

    public DoubleList<DeletedMail> getDeletedMails() {
        return DeletedMails;
    }

    public void setDeletedMails(DoubleList<DeletedMail> mails) {
        DeletedMails = mails;
    }

}