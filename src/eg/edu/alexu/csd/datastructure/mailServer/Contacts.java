package eg.edu.alexu.csd.datastructure.mailServer;

import eg.edu.alexu.csd.datastructure.com.AQueue;
import eg.edu.alexu.csd.datastructure.com.DoubleList;
import eg.edu.alexu.csd.datastructure.com.SingleLinked;
import gui.csd.mailServer.Data;

import java.io.*;
import java.util.Date;
import java.util.Objects;


public class Contacts {
    private DoubleList<Contact> contacts;


    public Contacts() {
        contacts=new DoubleList<>();
    }

    public boolean signIN(String email,String password){
        if(contacts.isEmpty())
            throw new RuntimeException("No Match Found!");
        boolean found;
        Contact temp=getContact(email);
        if(temp==null)
            throw new RuntimeException("Incorrect email!");
        found=temp.signin(password);
        //If found then the email must open.
        return found;
    }

    public boolean signup(IContact contact){
        //Username ,email,password,phonenumber must be located once
        for(int i=0;i<contacts.size();i++) {
            Contact temp = contacts.get(i);
            if (temp.getEmail().equals(contact.getEmail())) {
                return false;            }
            if (temp.getPassword().equals(contact.getPassword())) {
                return false;            }
            if (temp.getUserName().equals(contact.getUserName())) {
                return false;            }
            if (temp.getPhoneNumber()!=null&&temp.getPhoneNumber().equals(contact.getPhoneNumber())) {
                return false;
            }
        }
        contacts.add((Contact) contact);
        return true;
    }

    public Contact getContact(String email){
        for(int i=0;i<contacts.size();i++){
            if(contacts.get(i).getEmail().equals(email)){
                return contacts.get(i);
            }
        }
        //Not Found.
        return null;
    }
    public Contact getByUserName(String username){
        for(int i=0;i<contacts.size();i++){
            if(contacts.get(i).getEmail().equals(username)){
                return contacts.get(i);
            }
        }
        //Not Found.
        return null;
    }
    ////////////////////////////////////////////////save////////////////////////////////////////////////////////////////////
    public void save() throws IOException {
        File Dir = new File("src\\contacts");
        if (!Dir.exists()) {
            Dir.mkdir();
        }
        String DirPath = Dir.getPath();

        BufferedWriter NoofContacts = new BufferedWriter(new FileWriter("src\\contactsNumber.txt"));
        String number =String.valueOf(contacts.size());
        NoofContacts.write(number);
        NoofContacts.close();

        for(int i = 0; i < contacts.size(); ++i) {
            File subDir = new File(DirPath + "\\" + i);
            if (!subDir.exists()) {
                subDir.mkdir();
            }
            String SubDirpath = subDir.getPath();
            BufferedWriter username = new BufferedWriter(new FileWriter(SubDirpath + "\\" + "username.txt"));
            username.write(contacts.get(i).getUserName());
            username.close();

            BufferedWriter email = new BufferedWriter(new FileWriter(SubDirpath + "\\" + "email.txt"));
            email.write(contacts.get(i).getEmail());
            email.close();

            BufferedWriter password = new BufferedWriter(new FileWriter(SubDirpath + "\\" + "password.txt"));
            password.write(contacts.get(i).getPassword());
            password.close();

            if (contacts.get(i).getPhoneNumber() != null) {
                BufferedWriter phone = new BufferedWriter(new FileWriter(SubDirpath + "\\" + "phone.txt"));
                phone.write(contacts.get(i).getPhoneNumber());
                phone.close();
            }
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            if (!(contacts.get(i).getUserContacts().isEmpty())){
                int j = 0;
                BufferedWriter UserContacts = new BufferedWriter(new FileWriter(SubDirpath + "\\" + "UserContacts.txt"));
                while (j<contacts.get(i).getUserContacts().size()) {
                    String useremail = contacts.get(i).getUserContacts().get(j).getContact().getEmail();
                    String nickname = contacts.get(i).getUserContacts().get(j).getNickname();
                    UserContacts.write(useremail);
                    UserContacts.newLine();
                    UserContacts.write(nickname);
                    j++;
                }
                UserContacts.close();
            }
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////


            String inboxPath= makeFolders("inbox",SubDirpath);
            String sentPath= makeFolders("sent",SubDirpath);
            String draftPath=makeFolders("draft",SubDirpath);
            String trashPath= makeFolders("trash",SubDirpath);
            saveMails(inboxPath,"inbox",contacts.get(i),SubDirpath);
            saveMails(sentPath,"sent",contacts.get(i),SubDirpath);
            saveMails(draftPath,"draft",contacts.get(i),SubDirpath);
            saveMails(trashPath,"trash",contacts.get(i),SubDirpath);
        }



    }

    public String makeFolders(String Name, String path){
        File folder = new File(path+"\\"+Name);
        if (!folder.exists()) {
            folder.mkdir();
        }
        return folder.getPath();
    }

    public void saveMails(String path , String name,Contact contact, String subpath) throws IOException {
        Folder temp = new Folder("temp");
        switch (name) {
            case "inbox":
                temp = contact.getInbox();
                BufferedWriter NoofInboxFolders = new BufferedWriter(new FileWriter(subpath+"\\"+"NoofInboxFolders.txt"));
                NoofInboxFolders.write(String.valueOf(temp.noofMails()));
                NoofInboxFolders.close();
                break;
            case "sent":
                temp = contact.getSent();
                BufferedWriter NoofSentFolders = new BufferedWriter(new FileWriter(subpath+"\\"+"NoofSentFolders.txt"));
                NoofSentFolders.write(String.valueOf(temp.noofMails()));
                NoofSentFolders.close();

                break;
            case "draft":
                temp = contact.getDraft();
                BufferedWriter NoofDraftFolders = new BufferedWriter(new FileWriter(subpath+"\\"+"NoofDraftFolders.txt"));
                NoofDraftFolders.write(String.valueOf(temp.noofMails()));
                NoofDraftFolders.close();
                break;
            case "trash":
                BufferedWriter NooftrashFolders = new BufferedWriter(new FileWriter(subpath+"\\"+"NooftrashFolders.txt"));
                NooftrashFolders.write(String.valueOf(contact.getTrash().noofDeletedMails()));
                NooftrashFolders.close();
                break;
        }
        if (!name.equals("trash")) {
            for (int i = 0; i < temp.noofMails(); i++) {
                File mail = new File(path + "\\" + i);
                if (!mail.exists()) {
                    mail.mkdir();
                }
                String FolderPath = mail.getPath();
                saveMailsDetails(FolderPath, temp.getMail(i), name);
            }
        }

        else {
            for (int i = 0; i <contact.getTrash().noofDeletedMails(); i++) {
                File mail = new File(path + "\\" + i);
                if (!mail.exists()) {
                    mail.mkdir();
                }
                String FolderPath = mail.getPath();
                saveTrashDetails(FolderPath,contact.getTrash().getDeletedMail(i).getDeletedMail(),contact.getTrash().getDeletedMail(i).getDeletedDate(),contact.getTrash().getDeletedMail(i).getOrginalFolder());
            }
        }

    }

    public void saveMailsDetails (String path ,Mail mail,String name) throws IOException {
        if (!name.equals("inbox")) {////////////////////////////////////////////////////////////////////////////////////
            BufferedWriter getReceivers = new BufferedWriter(new FileWriter(path + "\\" + "Receivers.txt"));
            AQueue<Contact> temp= Data.getCopy(mail.getReceivers());
            while (temp!=null&&!temp.isEmpty()) {
                String rec = temp.dequeue().getEmail();
                if(rec!=null)
                    getReceivers.write(rec);
            }
            getReceivers.close();
        }
        if (name.equals("inbox")){
            BufferedWriter getsender = new BufferedWriter(new FileWriter(path+"\\"+"sender.txt"));
            getsender.write(mail.getSender().getEmail());
            getsender.close();
        }

        if (mail.getBody()!=null) {
            BufferedWriter getBody = new BufferedWriter(new FileWriter(path + "\\" + "Body.txt"));
            getBody.write(mail.getBody());
            getBody.close();
        }

        if (mail.getSubject()!=null) {
            BufferedWriter getSubject = new BufferedWriter(new FileWriter(path + "\\" + "Subject.txt"));
            getSubject.write(mail.getSubject());
            getSubject.close();
        }

        BufferedWriter getDate = new BufferedWriter(new FileWriter(path+"\\"+"Date.txt"));
        long time = mail.getDate().getTime();
        getDate.write(""+time);
        getDate.close();

        if (mail.getAttachment()!=null) {
            BufferedWriter getAttachment = new BufferedWriter(new FileWriter(path + "\\" + "Attachment.txt"));
            SingleLinked<File> attach = (SingleLinked<File>) mail.getAttachment();
            SingleLinked<String> paths = new SingleLinked<>();
            for (int i = 0; i < attach.size(); i++) {
                paths.add(attach.get(i).getPath());
            }
            for (int i = 0; i < paths.size(); i++) {
                getAttachment.write(paths.get(i));
            }
            getAttachment.close();
        }

        BufferedWriter getPriority = new BufferedWriter(new FileWriter(path+"\\"+"priority.txt"));
        getPriority.write(""+mail.getPriority());
        getPriority.close();


    }

    public void saveTrashDetails(String path,Mail mail,Date deleted_date , Folder folder) throws IOException {

        BufferedWriter getReceivers = new BufferedWriter(new FileWriter(path + "\\" + "Receivers.txt"));
        AQueue<Contact> temp= Data.getCopy(mail.getReceivers());
        while (temp!=null&&!temp.isEmpty()) {
            String rec = temp.dequeue().getEmail();
            getReceivers.write(rec);
        }
        getReceivers.close();

        BufferedWriter getsender = new BufferedWriter(new FileWriter(path+"\\"+"sender.txt"));
        getsender.write(mail.getSender().getEmail());
        getsender.close();

        if (mail.getBody()!=null) {
            BufferedWriter getBody = new BufferedWriter(new FileWriter(path + "\\" + "Body.txt"));
            getBody.write(mail.getBody());
            getBody.close();
        }

        if (mail.getSubject()!=null) {
            BufferedWriter getSubject = new BufferedWriter(new FileWriter(path + "\\" + "Subject.txt"));
            getSubject.write(mail.getSubject());
            getSubject.close();
        }

        BufferedWriter getDate = new BufferedWriter(new FileWriter(path+"\\"+"Date.txt"));
        long time = mail.getDate().getTime();
        getDate.write(""+time);
        getDate.close();

        if (mail.getAttachment()!=null) {
            BufferedWriter getAttachment = new BufferedWriter(new FileWriter(path + "\\" + "Attachment.txt"));
            SingleLinked<File> attach = (SingleLinked<File>) mail.getAttachment();
            SingleLinked<String> paths = new SingleLinked<>();
            for (int i = 0; i < attach.size(); i++) {
                paths.add(attach.get(i).getPath());
            }
            for (int i = 0; i < paths.size(); i++) {
                getAttachment.write(paths.get(i));
            }
            getAttachment.close();
        }

        BufferedWriter getPriority = new BufferedWriter(new FileWriter(path+"\\"+"priority.txt"));
        getPriority.write(""+mail.getPriority());
        getPriority.close();


        BufferedWriter getDeleted_Date = new BufferedWriter(new FileWriter(path+"\\"+"Deleted Date.txt"));
        long time1 = deleted_date.getTime();
        getDeleted_Date.write(""+time1);
        getDeleted_Date.close();

        BufferedWriter Foldername = new BufferedWriter(new FileWriter(path+"\\"+"Folder Name.txt"));
        Foldername.write(folder.getNameFolder());
        Foldername.close();


    }
    ////////////////////////////////////load///////////////////////////////////////////////////////////////////////////
    public void load() throws IOException {
        //know number of contacts we have
        BufferedReader contactsNumber = null;
        File read;
        read=new File("src\\contactsNumber.txt");
        if(!read.exists())
            return;
        try {
            contactsNumber = new BufferedReader(new FileReader("src\\contactsNumber.txt"));
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        int NoofContacts = 0;
        NoofContacts = Integer.parseInt(contactsNumber.readLine());
        if (NoofContacts==0)
            return;
        //
        for (int i =0;i<NoofContacts;i++){
            Contact temp = new Contact();
            temp.setUserName(MainDetails(i,"username","src\\contacts"));
            temp.setEmail(MainDetails(i,"email","src\\contacts"));
            temp.setPassword(MainDetails(i,"password","src\\contacts"));
            temp.setPhoneNumber(MainDetails(i,"phone","src\\contacts"));
            temp.setUserContacts(getUserContact(i,"src\\contacts","UserContacts"));/////////////////////////
            int NoofInboxFolders =  NumberMails(i,"NoofInboxFolders","src\\contacts");
            int NoofSentFolders  =  NumberMails(i,"NoofSentFolders","src\\contacts");
            int NoofDraftFolders =  NumberMails(i,"NoofDraftFolders","src\\contacts");
            int NooftrashFolders =  NumberMails(i,"NooftrashFolders","src\\contacts");
            temp.setInbox(loadMails(i,"src\\contacts","inbox",NoofInboxFolders,temp));
            temp.setSent(loadMails(i,"src\\contacts","sent",NoofSentFolders,temp));
            temp.setDraft(loadMails(i,"src\\contacts","draft",NoofDraftFolders,temp));
            temp.setTrash(loadMails(i,"src\\contacts","trash",NooftrashFolders,temp));
            contacts.add(temp);
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private SingleLinked<UserContact> getUserContact(int i , String path , String name){
        BufferedReader getUserContact = null;
        if(!(new File(path+"\\"+i+"\\"+name+".txt").exists())) {
            return null;
        }
        try {
            getUserContact = new BufferedReader(new FileReader(path+"\\"+i+"\\"+name+".txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        SingleLinked<UserContact> back = new SingleLinked<>();
        Contact temp = new Contact();
        String line;
        boolean x =true;
        boolean y =false;
        String username=null;
        String nickname = null;
        UserContact user = new UserContact();

        try {
            while ((line=getUserContact.readLine())!=null) {
                System.out.println("Line is "+line);
                if (x)
                    username = line;
                else {
                    nickname = line;
                    y=!y;
                }
                if (y) {
                    System.out.println("in Y");
                    temp.setEmail(username);
                    user.setContact(temp);
                    user.setNickname(nickname);
                }
                if (!x&&y) {
                    System.out.println("In and");
                    back.add(user);
                    user=new UserContact();
                    y=!y;
                }
                x=!x;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return back;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private String MainDetails (int i , String name , String path ) throws IOException {
        BufferedReader MainDetails = null;
        File file=new File(path+"\\"+i+"\\"+name+".txt");
        if(!file.exists()) {
            return null;
        }
        try {
            MainDetails = new BufferedReader(new FileReader(path+"\\"+i+"\\"+name+".txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String theData;
        if (MainDetails==null||(theData=MainDetails.readLine()) == null)
            return null;
        return theData;
    }

    private int NumberMails (int i , String name , String path ) throws IOException {
        BufferedReader NumberMails = null;
        if(!(new File(path+"\\"+i+"\\"+name+".txt").exists())) {
            return 0;
        }
        try {
            NumberMails = new BufferedReader(new FileReader(path+"\\"+i+"\\"+name+".txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String num;
        if ((num=NumberMails.readLine()) == null)
            return 0;
        return Integer.parseInt(num);
    }

    private Folder loadMails (int i , String path , String name , int NoofMails , Contact con) throws IOException {
        Folder myfolder = new Folder(name);
        if(NoofMails==0)
            return new Folder(name);
        for (int j=0;j<NoofMails;j++){
            Mail mymail = new Mail();
            String lastPath = path + "\\" + i + "\\" + name + "\\" + j;
            if (!name.equals("trash")) {
                mymail.setSubject(GetDetailsMails(lastPath, "Subject"));
                mymail.setBody(GetDetailsMails(lastPath, "Body"));
                Date Time = new Date();
                Time.setTime(Long.parseLong(GetDetailsMails(lastPath, "Date")));
                mymail.setDate(Time);
                if (!name.equals("inbox"))//////////////////////////////////////////////////////////////////////////////////
                    mymail.setReceivers(loadReceivers(lastPath, "Receivers"));
                if (name.equals("inbox")) {
                    mymail.setSender(getSender(lastPath, "sender"));
                }
                mymail.setAttachment(Getattchments(lastPath, "Attachment"));
                mymail.setPriority(Integer.parseInt(GetDetailsMails(lastPath, "priority")));
                myfolder.addMail(mymail);
            }
            else {
                mymail.setSubject(GetDetailsMails(lastPath, "Subject"));
                mymail.setBody(GetDetailsMails(lastPath, "Body"));
                Date Time = new Date();
                Time.setTime(Long.parseLong(GetDetailsMails(lastPath, "Date")));
                mymail.setDate(Time);
                mymail.setReceivers(loadReceivers(lastPath, "Receivers"));
                mymail.setSender(getSender(lastPath, "sender"));
                mymail.setAttachment(Getattchments(lastPath, "Attachment"));
                mymail.setPriority(Integer.parseInt(GetDetailsMails(lastPath, "priority")));
                DeletedMail deletedMail = new DeletedMail(null,null,null);
                deletedMail.setDeletedmail(mymail);
                deletedMail.setOrginalfolder(GetDetailsMails(lastPath, "Folder Name"),con);
                Date Time1 = new Date();
                Time1.setTime(Long.parseLong(GetDetailsMails(lastPath, "Deleted Date")));
                deletedMail.setDeletedate(Time1);
                myfolder.addDeletedMail(deletedMail);
            }
        }
        return myfolder;
    }

    private String GetDetailsMails(String path , String name){
        BufferedReader GetDetialsMials = null;
        if(!(new File(path+"\\"+name+".txt").exists())) {
            return null;
        }
        try {
            GetDetialsMials = new BufferedReader(new FileReader(path+"\\"+name+".txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String back="";
        String line;

        try {
            while ((line=GetDetialsMials.readLine())!=null)
                back+=line;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return back;
    }

    private AQueue<Contact> loadReceivers(String path , String name){
        BufferedReader loadReceivers = null;
        if(!(new File(path+"\\"+name+".txt").exists())) {
            return null;
        }
        try {
            loadReceivers = new BufferedReader(new FileReader(path+"\\"+name+".txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        AQueue<Contact> back = null;
        Contact temp = new Contact();
        String line;

        try {
            while ((line=loadReceivers.readLine())!=null) {
                temp.setEmail(line);
            }
            back=new AQueue<>(1);
            back.enqueue(temp);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return back;
    }

    private SingleLinked<Object> Getattchments (String path , String name){
        BufferedReader Getattchments = null;
        if(!(new File(path+"\\"+name).exists()))
            return null;
        try {
            Getattchments = new BufferedReader(new FileReader(path+"\\"+name));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        SingleLinked<Object> back=new SingleLinked<>();
        String line;

        try {
            while ((line=Getattchments.readLine())!=null)
                back.add(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return back;
    }

    private Contact getSender(String path , String name ) throws IOException {
        BufferedReader getSender = null;
        File file=new File(path+"\\"+name+".txt");
        if(!file.exists()) {
            return null;
        }
        try {
            getSender = new BufferedReader(new FileReader(path+"\\"+name+".txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Contact theData = new Contact();
        String data;
        if (getSender==null||(data=getSender.readLine()) == null)
            return null;
        theData.setEmail(data);
        return theData;
    }

    ////////////////////////////////////////////////delete//////////////////////////////////////////////////////////////////
    public void DeleteFolders (){
        String SRC_FOLDER = "src\\contacts";
        File directory = new File(SRC_FOLDER);

        //make sure directory exists
        if(!directory.exists()){

            System.exit(0);

        }else{

            try{

                delete(directory);

            }catch(IOException e){
                e.printStackTrace();
                System.exit(0);
            }
        }

    }
    public static void delete(File file)
            throws IOException{

        if(file.isDirectory()){

            //directory is empty, then delete it
            if(Objects.requireNonNull(file.list()).length==0){

                file.delete();


            }else{

                //list all the directory contents
                String[] files = file.list();

                if (files != null) {
                    for (String temp : files) {
                        //construct the file structure
                        File fileDelete = new File(file, temp);

                        //recursive delete
                        delete(fileDelete);
                    }
                }

                //check the directory again, if empty then delete it
                if(Objects.requireNonNull(file.list()).length==0){
                    file.delete();

                }
            }

        }else{
            //if file, then delete it
            file.delete();

        }
    }
}