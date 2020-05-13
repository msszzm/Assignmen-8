package eg.edu.alexu.csd.datastructure.mailServer;

import eg.edu.alexu.csd.datastructure.com.AQueue;
import eg.edu.alexu.csd.datastructure.com.ILinkedList;
import eg.edu.alexu.csd.datastructure.com.LStack;
import eg.edu.alexu.csd.datastructure.com.SingleLinked;

import java.io.IOException;
import java.util.Date;
import java.util.Stack;

public class Application implements IApp {
    private Contact contact = new Contact();
    private Contacts contacts = new Contacts();
    private Folder temp;
    private SingleLinked<Mail> shownMails = new SingleLinked<>();
    private int i =0;
    private SingleLinked<String> errorContacts;

    @Override
    public boolean signin(String email, String password) {
        boolean check;
        try {
            check = contacts.signIN(email, password);
            contact = contacts.getContact(email);
//            contact=new Contact(email,password);
        } catch (RuntimeException r) {
            return false;
        }
        return true;

    }

    @Override
    public boolean signup(IContact contact) {
        return contacts.signup(contact);

    }

    public Contact allInformations() {
        return contact;

    }

    @Override
    public void setViewingOptions(IFolder folder, IFilter filter, ISort sort) {
        System.out.println("////////////////////SetViewing");
        if(i==0)
        	i=1;
        else
        	i=0;
        
        switch (folder.getNameFolder()) {
            case "inbox":
                temp = contact.getInbox();
                break;
            case "sent":
                temp = contact.getSent();
                break;
            case "draft":
                temp = contact.getDraft();
                break;
            default:
                temp = contact.getTrash();
                break;
        }
        System.out.println(temp.noofMails() +"  "+ temp.noofDeletedMails());
        SingleLinked<String> f = filter.getFilterList();
        String str = sort.getSelectedSort();
        int i = 0;
        LStack<Mail> viewMailed = new LStack<Mail>();
        while (i < f.size())
            try {
                viewMailed = filter.filter(viewMailed, folder, f.get(i++), f.get(i++));
            } catch (Exception e) {
                System.out.println("error in filter");
            }
        try {
            viewMailed = sort.sort(viewMailed, folder, str);
        } catch (Exception e) {
            System.out.println("error in sort");
        }
        shownMails = new SingleLinked<>();
        while (viewMailed!=null&&!viewMailed.isEmpty())
            try {
                shownMails.add(viewMailed.pop());
            } catch (Exception e) {
                System.out.println("error in emptying");
            }
        /////////////////////////////////////////////////filter or sort this folder using temp/////////////////
        ////////////////////// //////////now temp is folder sorted or filtered/////////////////////////////
    }

    @Override
    public IMail[] listEmails(int page) {
        ///////////////////////////////////////////////////// //setViewingOptions called////////////////////////////////
        System.out.println("Number of emails is "+temp.noofMails());
        //if (temp.noofMails()==0||temp.noofMails()<page*10)
        //return null;
        Mail[] Page = new Mail[10];
        //for(int i = 0;i<10;i++)
        // Page[i] = temp.getMail(i+(page-1)*10);
        if (shownMails == null||shownMails.size()==0)
            return null;
        for (int i = 0; i < 10; i++)
            if(i+(page)*10<shownMails.size())
                Page[i] = shownMails.get(i + (page ) * 10);
        return Page;
    }

    public int noPages() {
        int no = shownMails.size() / 10;
        if (shownMails.size() % 10 != 0)
            no++;
        return no;
    }

    @Override
    public void deleteEmails(ILinkedList mails) {
        ///////////////////////////////////////////////////// //setViewingOptions called////////////////////////////////
        if(temp!=contact.getTrash()) {
            SingleLinked<eg.edu.alexu.csd.datastructure.mailServer.Mail> Mails = (SingleLinked<eg.edu.alexu.csd.datastructure.mailServer.Mail>) mails;
            Folder trash = contact.getTrash();
            for (int i = 0; i < Mails.size(); i++) {
                trash.addDeletedMail(new DeletedMail(Mails.get(i), new Date(), temp));
                temp.removeMail(Mails.get(i));
            }
            //contact.deleteMails(Mails,  temp);
        }
        else {
            for (int i = 0; i < mails.size(); i++)
                temp.removeDeletedMail((Mail) mails.get(i));
        }
    }

    @Override
    public void moveEmails(ILinkedList mails, IFolder des) {
        ///////////////////////////////////////////////////// //setViewingOptions called////////////////////////////////
        SingleLinked<eg.edu.alexu.csd.datastructure.mailServer.Mail> Mails = (SingleLinked<eg.edu.alexu.csd.datastructure.mailServer.Mail>) mails;
        contact.moveEmails(Mails, des);
    }

    @Override
    public boolean compose(IMail email) {
        System.out.println("//////////////////////////COmpose//////////////////////////");
        eg.edu.alexu.csd.datastructure.mailServer.Mail new_email = (eg.edu.alexu.csd.datastructure.mailServer.Mail) email;
        contact.sendMail(new_email.getReceivers(), new_email.getSubject(), new_email.getBody(), (SingleLinked<Object>) new_email.getAttachment(), new_email.getPriority());
        return true;
    }

    public AQueue<Contact> compose(AQueue<String> Receivers) {
        System.out.println("/////////////////////compose////////////////////////////");
        if(Receivers==null)
            return null;
        String receiverEmail;
        Contact check;
        Stack<Contact> temp = new Stack<>();
        errorContacts = new SingleLinked<>();
        boolean found = true;
        while (!Receivers.isEmpty()) {
            receiverEmail = Receivers.dequeue();
            System.out.println("Receiver is "+receiverEmail);
            check = contacts.getContact(receiverEmail);
            if (check == null) {
                found = false;
                errorContacts.add(receiverEmail);
            } else {
                temp.push(check);
            }
        }
        if (!found)
            return null;
        AQueue<Contact> contacts = new AQueue<>(temp.size());
        while (!temp.isEmpty()) {
            contacts.enqueue(temp.pop());
        }
        return contacts;
    }

    public SingleLinked<String> getErrorContacts() {
        SingleLinked<String> temp = errorContacts;
        errorContacts = null;
        return temp;
    }

    public void save() {
        System.out.println("/////////////////////////save/////////////////////////////////");
        try {
            contacts.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("//////////////////////////save end//////////////////////////");
    }
    public void load(){
        try {
            contacts.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveDraft(AQueue<Contact> Receivers,String Subject,String Body,SingleLinked<Object> Attachment,int Priority){
        contact.savetodraft( Receivers, Subject, Body, Attachment, Priority);
    }
    public void deleteFolders(){
        contacts.DeleteFolders();
    }

    public void restore(ILinkedList mails) {
        for(int i =0;i<mails.size();i++) {
            Folder f = ((DeletedMail) mails.get(i)).getOrginalFolder();
            f.addMail(((DeletedMail) mails.get(i)).getDeletedMail());
        }
        for (int i = 0; i < mails.size(); i++)
            temp.removeDeletedMail((Mail) ((DeletedMail) mails.get(i)).getDeletedMail());
    }

    public boolean changeName(String newName,String oldName){
        Contact user=contacts.getByUserName(oldName);
        if(user==null)
            return false;
        //Now we have the contact to be edited ,we need the userContact.
        UserContact contact=this.contact.getSavedContact(user);
        if(contact==null)//This contact isn't saved
            return false;
        contact.setNickname(newName);
        return true;
    }
    public SingleLinked<UserContact> getContacts(){
        return contact.getUserContacts();
    }
}