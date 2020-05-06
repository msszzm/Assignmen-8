package eg.edu.alexu.csd.datastructure.mailServer;

public class UserContact {
    private Contact contact;
    private String nickname;

    public UserContact() {
    }

    public UserContact(Contact contact) {
        this.contact = contact;
        this.nickname=contact.getUserName();
    }

    public UserContact(Contact contact, String nickname){
        this.contact=contact;
        this.nickname=nickname;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
