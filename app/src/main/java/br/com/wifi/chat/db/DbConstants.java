package br.com.wifi.chat.db;


public class DbConstants {
    public static final String DB_NAME = "wifi_chat_db.db";
    public static final Integer DB_VERSION = 1;

    public interface CONTACT {
        String TABLE_NAME = "contact_tbl";

        String CREATE_SCRIPT = "CREATE TABLE " + CONTACT.TABLE_NAME + " (" +
                CONTACT.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CONTACT.NUMBER + " TEXT, " +
                CONTACT.NAME + " TEXT, " +
                CONTACT.LAST_MESSAGE + " TEXT, " +
                CONTACT.IMAGE + " BLOB ) ";

        String ID = "_id";
        String NUMBER = "number";
        String NAME = "_name";
        String LAST_MESSAGE = "last_message";
        String IMAGE = "image";

    }


    public interface MESSAGE {
        String TABLE_NAME = "message_tbl";

        String CREATE_SCRIPT =
                "CREATE TABLE " + MESSAGE.TABLE_NAME + " (" +
                        MESSAGE.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MESSAGE.CONTENT + " TEXT, " +
                        MESSAGE.SENDER + " TEXT, " +//me or recipient
                        MESSAGE.RECIPIENT + " TEXT, " +//me or recipient
                        MESSAGE.TIME + " TEXT ) ";//will become redundant 09/04/2017

        String ID = "_id";
        String CONTENT = "_content";
        String SENDER = "_sender";
        String RECIPIENT = "_recipient";
        String TIME = "_time";
    }
}
