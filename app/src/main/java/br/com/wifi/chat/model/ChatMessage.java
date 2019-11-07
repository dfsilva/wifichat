package br.com.wifi.chat.model;

import java.io.Serializable;


public class ChatMessage implements Serializable {


    public String messageContent;
    public String sender = null;
    public String recipient = null;
    public long time;
    public long messageID;

    public ChatMessage(boolean received, String messageContent) {
        this.messageContent = messageContent;
    }

    public ChatMessage(long messageID, String messageContent, String sender, String recipient, long time) {
        this.messageContent = messageContent;
        this.sender = sender;
        this.recipient = recipient;
        this.time = time;
        this.messageID = messageID;
    }

    public ChatMessage() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatMessage that = (ChatMessage) o;

        if (time != that.time) return false;
        if (messageID != that.messageID) return false;
        if (messageContent != null ? !messageContent.equals(that.messageContent) : that.messageContent != null)
            return false;
        if (sender != null ? !sender.equals(that.sender) : that.sender != null) return false;
        return recipient != null ? recipient.equals(that.recipient) : that.recipient == null;

    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (messageContent != null ? messageContent.hashCode() : 0);
        result = 31 * result + (sender != null ? sender.hashCode() : 0);
        result = 31 * result + (recipient != null ? recipient.hashCode() : 0);
        result = 31 * result + (int) (time ^ (time >>> 32));
        result = 31 * result + (int) (messageID ^ (messageID >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "messageContent='" + messageContent + '\'' +
                ", sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' +
                ", time=" + time +
                ", messageID=" + messageID +
                '}';
    }
}
