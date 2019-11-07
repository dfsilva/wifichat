package br.com.wifi.chat.model;

import java.io.Serializable;
import java.net.Inet4Address;
import java.util.Arrays;

/**
 * Created by KayO on 28/12/2016.
 */
public class Contact implements Serializable {
    public String name = null;
    public byte[] image;
    public boolean online = false;
    public String lastMessage;
    public boolean isSenderOfLastMessage;
    public String phoneNumber = null;
    public Inet4Address ipAddress;
    public int port;

    public Contact() {
    }

    public Contact(String name, byte[] image, String lastMessage, String phoneNumber) {
        this.name = name;
        this.image = image;
        this.lastMessage = lastMessage;
        this.phoneNumber = phoneNumber;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        if (online != contact.online) return false;
        if (isSenderOfLastMessage != contact.isSenderOfLastMessage) return false;
        if (name != null ? !name.equals(contact.name) : contact.name != null) return false;
        if (!Arrays.equals(image, contact.image)) return false;
        if (lastMessage != null ? !lastMessage.equals(contact.lastMessage) : contact.lastMessage != null)
            return false;
        return phoneNumber != null ? phoneNumber.equals(contact.phoneNumber) : contact.phoneNumber == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(image);
        result = 31 * result + (online ? 1 : 0);
        result = 31 * result + (lastMessage != null ? lastMessage.hashCode() : 0);
        result = 31 * result + (isSenderOfLastMessage ? 1 : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        return result;
    }

}
