package kenjic.dusan.chatapplication;

/**
 * Created by Duuuuuc on 4/1/2018.
 */

public class MessageClass {
    //private String msgId;
    private String senderId;
    //private String receiverId;
    private String msg;


    public MessageClass(/*String msgId, */String senderId, /*String receiverId,*/ String msg) {
        //this.msgId = msgId;
        this.senderId = senderId;
        //this.receiverId = receiverId;
        this.msg = msg;
    }

    /*public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }*/

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
