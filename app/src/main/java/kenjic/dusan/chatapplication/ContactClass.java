package kenjic.dusan.chatapplication;

/**
 * Created by Duuuuuc on 3/31/2018.
 */

public class ContactClass {
    //private String firstNameCon;
    //private String lastNameCon;
    private String userNameCon;
    //private String contactID;


    public ContactClass(/*String firstNameCon, String lastNameCon, */String userNameCon/*, String contactID*/) {
        //this.firstNameCon = firstNameCon;
        //this.lastNameCon = lastNameCon;
        this.userNameCon = userNameCon;
        //this.contactID = contactID;
    }


    /*public String getFirstNameCon() {
        return firstNameCon;
    }

    public void setFirstNameCon(String firstNameCon) {
        this.firstNameCon = firstNameCon;
    }

    public String getLastNameCon() {
        return lastNameCon;
    }

    public void setLastNameCon(String lastNameCon) {
        this.lastNameCon = lastNameCon;
    }*/

    public String getUserNameCon() {
        return userNameCon;
    }

    public void setUserNameCon(String userNameCon) {
        this.userNameCon = userNameCon;
    }

    /*public String getContactID() {
        return contactID;
    }

    public void setContactID(String contactID) {
        this.contactID = contactID;
    }*/
}
