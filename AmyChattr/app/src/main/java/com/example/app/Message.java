package com.example.app;

/**
 * Created by ahannon14 on 5/13/14.
 */
public class Message {
    public static String from = "src";//where the msg is from
    public static String to = "dest";//where the msg is to
    public static String content = "text";//where the msg is to
    public static String sent_time = "submit_time";//where the msg is sent
    public static String received_time = "forward_time";//where the msg is forwarded/received

    public Message(String src, String dest, String text, String sent, String received){
        this.from = src;
        this.to = dest;
        this.content = text;
        this.sent_time = sent;
        this.received_time = received;
    }

}
