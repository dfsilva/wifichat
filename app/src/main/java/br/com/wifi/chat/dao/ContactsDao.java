package br.com.wifi.chat.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.wifi.chat.db.DbConstants;
import br.com.wifi.chat.db.DbHelper;
import br.com.wifi.chat.model.Contact;

public class ContactsDao {

    private SQLiteDatabase db;
    private Context context;
    private DbHelper dbHelper;
    private List<Contact> allContacts;

    private ContentValues cv;

    private Cursor cr;
    private Contact contact;

    private long rowsEffected;
    private String whereConditions;
    private String[] whereArgs;

    public ContactsDao(Context context) {
        this.context = context;
        dbHelper = new DbHelper(this.context);
        cv = new ContentValues();
        allContacts = new ArrayList<>();
    }

    private String[] columns = new String[]{
            DbConstants.CONTACT.ID,
            DbConstants.CONTACT.NUMBER,
            DbConstants.CONTACT.NAME,
            DbConstants.CONTACT.LAST_MESSAGE,
            DbConstants.CONTACT.IMAGE
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


    public boolean create(Contact contact) {
        openForWrite();

        cv.put(DbConstants.CONTACT.NUMBER, contact.phoneNumber);
        cv.put(DbConstants.CONTACT.NAME, contact.name);
        cv.put(DbConstants.CONTACT.LAST_MESSAGE, contact.lastMessage);
        cv.put(DbConstants.CONTACT.IMAGE, contact.image);

        rowsEffected = db.insert(DbConstants.CONTACT.TABLE_NAME, null, cv);

        close();

        return rowsEffected > 0;
    }

    public boolean update(Contact contact) {

        openForWrite();
        whereConditions = DbConstants.CONTACT.NUMBER + "=?";
        whereArgs = new String[]{String.valueOf(contact.phoneNumber.trim())};

        cv.put(DbConstants.CONTACT.NUMBER, contact.phoneNumber);
        cv.put(DbConstants.CONTACT.NAME, contact.name);
        cv.put(DbConstants.CONTACT.LAST_MESSAGE, contact.lastMessage);
        cv.put(DbConstants.CONTACT.IMAGE, contact.image);

        rowsEffected = db.update(DbConstants.CONTACT.TABLE_NAME, cv, whereConditions, whereArgs);

        close();

        return rowsEffected > 0;
    }

    public boolean deleteContact(Contact contact) {
        openForWrite();
        whereConditions = DbConstants.CONTACT.NUMBER + "=?";
        whereArgs = new String[]{String.valueOf(contact.phoneNumber.trim())};

        rowsEffected = db.delete(DbConstants.CONTACT.TABLE_NAME, whereConditions, whereArgs);

        close();
        return rowsEffected > 0;
    }


    public Contact getContactByNumber(Contact contact) {
        openForRead();
        whereConditions = DbConstants.CONTACT.NUMBER + "=?";
        whereArgs = new String[]{contact.phoneNumber.trim()};

        cr = db.query(DbConstants.CONTACT.TABLE_NAME, columns, whereConditions, whereArgs, null, null, null);


        if (cr.getCount() < 1)
            return null;

        if (cr != null) {
            cr.moveToFirst();
            this.contact = this.cursorToContatc(cr);

        }

        close();
        return this.contact;
    }

    public List<Contact> getAllContacts() {
        openForRead();
        cr = db.query(DbConstants.CONTACT.TABLE_NAME, columns, null, null, null, null, null);
        allContacts = cursorToList(cr);
        close();
        return allContacts;
    }

    private Contact cursorToContatc(Cursor cursor) {
        return new Contact(
                cursor.getString(cursor.getColumnIndex(DbConstants.CONTACT.NAME)),
                cursor.getBlob(cursor.getColumnIndex(DbConstants.CONTACT.IMAGE)),
                cursor.getString(cursor.getColumnIndex(DbConstants.CONTACT.LAST_MESSAGE)),
                cursor.getString(cursor.getColumnIndex(DbConstants.CONTACT.NUMBER)));
    }

    private List<Contact> cursorToList(Cursor cursor) {
        List<Contact> retorno = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            retorno.add(cursorToContatc(cursor));
        }
        return retorno;
    }
}
