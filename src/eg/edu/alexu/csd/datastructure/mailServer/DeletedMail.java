package eg.edu.alexu.csd.datastructure.mailServer;

import java.util.Date;

public class DeletedMail extends Mail {

    private Mail deletedmail;
    private Date deletedate;
    private Folder orginalfolder;

    public DeletedMail(Mail mail,Date date,Folder folder) {
        deletedmail = mail;
        deletedate = date;
        orginalfolder = folder;
    }


    public Mail getDeletedMail() {
        return deletedmail;
    }

    public Date getDeletedDate() {
        return deletedate;
    }

    public Folder getOrginalFolder() {
        return orginalfolder;
    }

    public void setDeletedmail(Mail deletedmail) {
        this.deletedmail = deletedmail;
    }

    public void setDeletedate(Date deletedate) {
        this.deletedate = deletedate;
    }

    public void setOrginalfolder(String orginalfolder,Contact user) {
        this.orginalfolder = user.getFolder(orginalfolder);
    }

    // public void setname(String name) {
    //   orginalfolder.setName(name);
    //}
}