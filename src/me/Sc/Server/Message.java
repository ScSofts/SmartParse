package me.Sc.Server;

public class Message {
    public static String messageString;

    public Message() {
        // TODO Auto-generated constructor stub
    }

    public static void RemoveMessage() {
        messageString = "";
    }

    public static void PushMessage(String Message) {
        messageString = Message;
    }

    public static String getMessage() {
        String tmp = messageString;
        RemoveMessage();
        return tmp;
    }
}
