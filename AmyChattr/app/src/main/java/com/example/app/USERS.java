package com.example.app;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Administrator on 14-4-19.
 */
public class USERS {
    public static final String AUTHORITY = "com.example.app.chattr";
    public static final class USER implements BaseColumns {
        public static final String _NUM = "num";//the full string including +86 or +1
        public static final String _ZONE = "zone";//zone code, +1 or +86
        public static final String _SUBNUM = "subnum";//number without zone code
        public static final String _NAME = "name";//user alias
        public static final String _FLAG = "flag";//user alias

        public static final Uri USERS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/users");
        public static final Uri USER_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/user");

    }
}