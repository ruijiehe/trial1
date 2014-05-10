package com.example.app;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Administrator on 14-4-20.
 */
public class MSGProvider extends ContentProvider{
    private static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int msgs = 1;
    private static final int msg = 2;
    private static final int users = 3;
    private static final int user = 4;
    private MyDatabaseHelper dbOpenHelper;
    static {
        matcher.addURI(MSGS.AUTHORITY,"msgs",msgs);
        matcher.addURI(MSGS.AUTHORITY,"msg",msg);
        matcher.addURI(MSGS.AUTHORITY,"users",users);
        matcher.addURI(MSGS.AUTHORITY,"user",user);
    }
    @Override
    public boolean onCreate()
    {
        dbOpenHelper = new MyDatabaseHelper(this.getContext());
        return true;
    }
    //method that insert data
    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        // get database
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        //insert row, return row ID
        assert db != null;
        switch (matcher.match(uri))
        {
            case msgs:
                long msg_rowId = db.insert("msgs", MSGS.MSG._FROM, values);
                // if successfully inserted, return uri
                if (msg_rowId > 0)
                {
                    // add new data after the exists uri
                    Uri msg_Uri = ContentUris.withAppendedId(uri, msg_rowId);
                    // notify change in data
                    getContext().getContentResolver().notifyChange(msg_Uri, null);
                    return msg_Uri;
                }
                return null;
            case users:
                //TODO here exists a problem ,left unfixed
                long user_rowId = db.insert("users", USERS.USER._NUM, values);
                // if successfully inserted, return uri
                if (user_rowId > 0)
                {
                    // add new data after the exists uri
                    Uri wordUri = ContentUris.withAppendedId(uri, user_rowId);
                    // notify change in data
                    getContext().getContentResolver().notifyChange(wordUri, null);
                    return wordUri;
                }
                return null;
            default:
                return null;
        }
    }
    // method that delete data
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        // record number of rows deleted
        int num = 0;
        // match uri
        switch (matcher.match(uri))
        {
            case msgs:
                assert db != null;
                num = db.delete("msgs", selection, selectionArgs);
                break;
            case msg:
                // get the target row id
                long id = ContentUris.parseId(uri);
                String where = MSGS.MSG._ID + "=" + id;
                // concat substring of where, if exists
                if (selection != null && !selection.equals(""))
                {
                    where = where + " and " + selection;
                }
                assert db != null;
                num = db.delete("msgs", where, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("InvalidateUri:" + uri);
        }
        // notify change in data
        getContext().getContentResolver().notifyChange(uri, null);
        return num;
    }
    // modify data
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs)
    {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        // record number of modified data
        int num = 0;
        switch (matcher.match(uri))
        {
            case msgs:
                assert db != null;
                num = db.update("msgs", values, selection, selectionArgs);
                break;
            case msg:
                // find the msg that will be modified
                long id = ContentUris.parseId(uri);
                String where = MSGS.MSG._ID + "=" + id;
                // concat where expression if previous 'where' has a son expression
                if (selection != null && !selection.equals(""))
                {
                    where = where + " and " + selection;
                }
                assert db != null;
                num = db.update("msgs", values, where, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Invalidate Uri:" + uri);
        }
        //notify change
        getContext().getContentResolver().notifyChange(uri, null);
        return num;
    }
    // method that query data
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder)
    {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        switch (matcher.match(uri))
        {
            case msgs:
                // execute query
                assert db != null;
                return db.query("msgs", projection, selection, selectionArgs,
                        null, null, sortOrder);
            case msg:
                // find the row of query
                long id = ContentUris.parseId(uri);
                String where = MSGS.MSG._ID + "=" + id;
                // concat where expression if previous 'where' has a son expression
                if (selection != null && !"".equals(selection))
                {
                    where = where + " and " + selection;
                }
                assert db != null;
                return db.query("msgs", projection, where, selectionArgs, null,
                        null, sortOrder);
            case users:
                // execute query
                assert db != null;
                return db.query("users", projection, selection, selectionArgs,
                        null, null, sortOrder);
            case user:
                // find the row of query
                long user_id = ContentUris.parseId(uri);
                String user_where = USERS.USER._ID + "=" + user_id;
                // concat where expression if previous 'where' has a son expression
                if (selection != null && !"".equals(selection))
                {
                    user_where = user_where + " and " + selection;
                }
                assert db != null;
                return db.query("users", projection, user_where, selectionArgs, null,
                        null, sortOrder);
            default:
                throw new IllegalArgumentException("Invalidate Uri:" + uri);
        }
    }
    //return MIME type of data associated with certain uri s
    @Override
    public String getType(Uri uri)
    {
        switch (matcher.match(uri))
        {
            // if multiple data to be operated
            case msgs:
                return "vnd.android.cursor.dir/com.example.app.chattr";
            // if single data to be operated
            case msg:
                return "vnd.android.cursor.item/com.example.app.chattr";
            case users:
                return "vnd.android.cursor.dir/com.example.app.chattr";
            // if single data to be operated
            case user:
                return "vnd.android.cursor.item/com.example.app.chattr";
            default:
                throw new IllegalArgumentException("Invalidate Uri:" + uri);
        }
    }
}