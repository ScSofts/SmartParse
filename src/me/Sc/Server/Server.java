package me.Sc.Server;

import java.io.IOException;
import java.util.HashMap;

import fi.iki.elonen.NanoHTTPD;

public class Server extends NanoHTTPD {
    public Server(int port) {
        super(port);
    }

    @Override
    public Response serve(IHTTPSession session) {

        try {
            // 这一句话必须要写，否则在获取数据时，获取不到数据
            session.parseBody(new HashMap<String, String>());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ResponseException e) {
            e.printStackTrace();
        }
        return Solve(session.getUri());
    }


    private Response Solve(String url) {
        StringBuilder builder = new StringBuilder();
        if (url.startsWith("/api")) {
            try {
                String u = url.split("xURL=", 2)[1];
                builder.append(url);
                Message.PushMessage(u);
                System.out.println("Message!!!!\n\n\n");

                builder.append("<html>" +
                        "<head>" +
                        "<meta charset=\"UTF-8\">" +
                        "<title飞鸽传书</title>" +
                        "</head>" +
                        "<body><h2><b>推送" + u + "成功！TV端返回即可</b></h1></body>" +
                        "</html>");
            } catch (Exception e) {
                builder.append("<html>" +
                        "<head>" +
                        "<meta charset=\"UTF-8\">" +
                        "<title飞鸽传书</title>" +
                        "</head>" +
                        "<body><h2><b>推送错误！请检查URL</b></h1></body>" +
                        "</html>");
            }
        } else {
            builder.append(Statics.html);
        }
        return newFixedLengthResponse(builder.toString());
    }
}