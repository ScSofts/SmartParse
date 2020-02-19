package me.Sc.Server;

public class Statics {
    public static String html = "<!DOCTYPE html><html lang=\"en\">" +
            "<head>" +
            "<meta charset=\"UTF-8\">" +
            "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
            "<meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">" +
            "<title>∑…∏Î¥´ È</title>" +
            "<style>" +
            "#submit {" +
            "background-color: #4CAF50; /* Green */" +
            "border: none;" +
            "color: white;" +
            "padding: 15px 32px;" +
            "text-align: center;" +
            "text-decoration: none;" +
            "display: inline-block;" +
            "font-size: 16px;" +
            "}" +
            "#URL{" +
            "background: transparent;" +
            "font-size: 16px;" +
            "padding: 12px;" +
            "width: 500px;" +
            "}" +
            "#URL:focus{" +
            "border: 1px solid rgb(0, 60, 255);" +
            "}" +
            "</style>" +
            "</head>" +
            "<body>" +
            " ‰»ÎÕ¯÷∑:<br>" +
            "<input type=\"text\" id=\"URL\">" +
            "<input type=\"submit\" id=\"submit\" placeholder=\" ‰»ÎÕ¯÷∑\">" +
            "<script>" +
            "document.getElementById(\"submit\").onclick=function(){" +
            "document.location.href=\"/apixURL=\"+document.getElementById(\"URL\").value;" +
            "};" +
            "</script>" +
            "</body>" +
            "</html>";
}
