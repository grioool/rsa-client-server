package by.grigorieva.olga.messages;

import java.util.List;

public class UserListUpdatedSystemMessage extends SystemMessage{
    private List<String> userList;

    public UserListUpdatedSystemMessage(String sender){
        super(sender);
    }

    public List<String> getUserList(){
        return userList;
    }

    public void setUserList(List<String> userList){

        this.userList = userList;
    }

    @Override
    public String toString(){
        return "UserListUpdatedSystemMessage{" +
                "userList=" + userList +
                ", sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' +
                '}';
    }
}
