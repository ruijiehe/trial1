package edu.whitworth.sendandrecv.app;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Administrator on 14-4-19.
 */
public class MSGS {
        public static final String AUTHORITY = "edu.whitworth.sendandrecv.app.chattr";
        public static final class MSG implements BaseColumns {
            public static final String _FROM = "src";//where the msg is from
            public static final String _TO = "dest";//where the msg is to
            public static final String _TEXT = "text";//where the msg is to
            public static final String _SENT = "submit_time";//where the msg is sent
            public static final String _RECEIVED = "forward_time";//where the msg is forwarded/received

            public static final Uri MSGS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/msgs");
            public static final Uri MSG_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/msg");
    }
}
