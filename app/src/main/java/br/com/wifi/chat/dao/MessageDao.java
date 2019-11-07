package br.com.wifi.chat.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.wifi.chat.db.DbConstants;
import br.com.wifi.chat.db.DbHelper;
import br.com.wifi.chat.model.ChatMessage;
import br.com.wifi.chat.model.Contact;


public class MessageDao {

    private SQLiteDatabase db;
    private Context context;
    private DbHelper dbHelper;
    private List<ChatMessage> allChatMessages;
    private ContentValues cv;
    private Cursor cr;
    private ChatMessage chatMessage;

    private long rowsEffected;

    private String whereConditions;
    private String[] whereArgs;


    public MessageDao(Context context) {
        this.context = context;
        dbHelper = new DbHelper(this.context);//creates tables
        cv = new ContentValues();
        allChatMessages = new ArrayList<>();
    }

    private String[] columns = new String[]{
            DbConstants.MESSAGE.ID,
            DbConstants.MESSAGE.CONTENT,
            DbConstants.MESSAGE.SENDER,
            DbConstants.MESSAGE.RECIPIENT,
            DbConstants.MESSAGE.TIME
    };

    private void openForRead() {
        db = dbHelper.getReadableDatabase();
    }

    private void openForWrite() {
        db = dbHelper.getWritableDatabase();
    }

    private void close() {
        db.close();
        dbHelper.close();
    }


    public boolean createChatMessage(ChatMessage chatMessage) {
        openForWrite();

        cv.put(DbConstants.MESSAGE.CONTENT, chatMessage.messageContent);
        cv.put(DbConstants.MESSAGE.SENDER, chatMessage.sender);
        cv.put(DbConstants.MESSAGE.RECIPIENT, chatMessage.recipient);
        cv.put(DbConstants.MESSAGE.TIME, chatMessage.time);

        rowsEffected = db.insert(DbConstants.MESSAGE.TABLE_NAME, null, cv);

        close();

        return rowsEffected > 0;
    }

    public boolean updateChatMessage(ChatMessage chatMessage) {

        openForWrite();
        whereConditions = DbConstants.MESSAGE.ID + "=?";
        whereArgs = new String[]{String.valueOf(chatMessage.messageID)};

        cv.put(DbConstants.MESSAGE.CONTENT, chatMessage.messageContent);
        cv.put(DbConstants.MESSAGE.SENDER, chatMessage.sender);
        cv.put(DbConstants.MESSAGE.RECIPIENT, chatMessage.recipient);
        cv.put(DbConstants.MESSAGE.TIME, chatMessage.time);

        rowsEffected = db.update(DbConstants.MESSAGE.TABLE_NAME, cv, whereConditions, whereArgs);
        close();
        return rowsEffected > 0;
    }

    public boolean deleteChatMessage(ChatMessage chatMessage) {
        openForWrite();
        whereConditions = DbConstants.MESSAGE.ID + "=?";
        whereArgs = new String[]{String.valueOf(chatMessage.messageID)};

        rowsEffected = db.delete(DbConstants.MESSAGE.TABLE_NAME, whereConditions, whereArgs);

        close();
        return rowsEffected > 0;
    }

    public ChatMessage getChatMessageByNumber(ChatMessage chatMessage) {
        openForRead();

        whereConditions = DbConstants.MESSAGE.SENDER + "=? AND "
                + DbConstants.MESSAGE.RECIPIENT + "=? AND "
                + DbConstants.MESSAGE.TIME + "=?";

        whereArgs = new String[]{chatMessage.sender, chatMessage.recipient, String.valueOf(chatMessage.time)};

        cr = db.query(DbConstants.MESSAGE.TABLE_NAME, columns, whereConditions, whereArgs, null, null, null);

        if (cr.getCount() < 1)
            return null;

        if (cr != null) {
            cr.moveToFirst();
            this.chatMessage = cursorToMessage(cr);
        }

        close();
        return this.chatMessage;
    }

    public List<ChatMessage> getAllChatMessages() {
        openForRead();
        cr = db.query(DbConstants.MESSAGE.TABLE_NAME, columns, null, null, null, null, null);
        allChatMessages = cursorToMessages(cr);
        close();
        return allChatMessages;
    }

    public List<ChatMessage> getChatsWithThisUser(Contact thisContact) {
        openForRead();
        whereConditions = DbConstants.MESSAGE.SENDER + "=? OR " + DbConstants.MESSAGE.RECIPIENT + "=?";
        whereArgs = new String[]{thisContact.name, thisContact.name};
        cr = db.query(DbConstants.MESSAGE.TABLE_NAME, columns, whereConditions, whereArgs, null, null, DbConstants.MESSAGE.TIME + " ASC");
        allChatMessages = cursorToMessages(cr);
        close();
        return allChatMessages;
    }

    private ChatMessage cursorToMessage(Cursor cursor) {
        return new ChatMessage(
                cursor.getLong(cursor.getColumnIndex(DbConstants.MESSAGE.ID)),
                cursor.getString(cursor.getColumnIndex(DbConstants.MESSAGE.CONTENT)),
                cursor.getString(cursor.getColumnIndex(DbConstants.MESSAGE.SENDER)),
                cursor.getString(cursor.getColumnIndex(DbConstants.MESSAGE.RECIPIENT)),
                cursor.getLong(cursor.getColumnIndex(DbConstants.MESSAGE.TIME))
        );
    }

    private List<ChatMessage> cursorToMessages(Cursor cursor) {
        List<ChatMessage> messagesReturn = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            messagesReturn.add(cursorToMessage(cursor));
        }
        return messagesReturn;
    }

}
