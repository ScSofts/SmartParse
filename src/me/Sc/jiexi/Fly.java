package me.Sc.jiexi;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import me.Sc.Server.Message;
import me.Sc.Server.NetWorkUtils;
import me.Sc.Server.Server;

public class Fly extends Activity {
    public static Server server;
    private TextView textView;
    private EditText editText;
    private String sURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fly);
        server = new Server(8888);
        try {
            server.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block

        }
        textView = (TextView) findViewById(R.id.Server);
        editText = (EditText) findViewById(R.id.editText1);
        if (NetWorkUtils.checkEnable(getApplicationContext()))
            textView.setText("·þÎñµØÖ·:" + NetWorkUtils.getLocalIpAddress(getApplicationContext()) + ":8888");
        else {
            textView.setText("ÍøÂç´íÎó!");
        }
        editText.setText(MainActivity.web.getUrl());
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (true) {
                    String s = Message.getMessage();
                    if (s != "") {
                        sURL = s;
                    }
                }
            }
        });
        t.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            if (sURL != "") {
                MainActivity.web.loadUrl(sURL);
            }
            return super.onKeyDown(keyCode, event);
        }else if(keyCode==KeyEvent.KEYCODE_HOME){
        	return super.onKeyDown(keyCode, event);
        }
        else{
        	return true;
        }
    }
}
