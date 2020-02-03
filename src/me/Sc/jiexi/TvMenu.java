package me.Sc.jiexi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class TvMenu extends Activity {
    public static String url = "", title = "";
    private Button add;
    private Button open;
    private Button fly;
    private Button next, front;//下级上集

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tv_menu);
        add = (Button) findViewById(R.id.button1);
        open = (Button) findViewById(R.id.button2);
        fly = (Button) findViewById(R.id.button3);
        next = (Button) findViewById(R.id.Next);
        front = (Button) findViewById(R.id.Front);

        if (url == "" || title == "") finish();

        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                System.out.println("Like!");
                String cachePath = getApplicationContext().getExternalCacheDir()
                        .getParentFile().getPath();
                String iniName = cachePath + "/fav.ini";
                IniFile iniFile = new IniFile(new File(iniName));
                iniFile.set("Fav", "\"" + title + "\"", "\""
                        + url.replace("=", "·") + "\" \n");
                System.out.println("\"" + url.replace("=", "·") + "\"");
                iniFile.save();
                finish();
            }
        });

        open.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent i = new Intent(TvMenu.this, Favourite.class);
                startActivity(i);
                finish();
            }
        });

        fly.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent i = new Intent(TvMenu.this, Fly.class);
                startActivity(i);
                finish();
            }
        });

        front.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                //MainActivity.methodString = MainActivity.front;
                MainActivity.web.loadUrl("javascript:a=undefined");
                MainActivity.web.loadUrl(MainActivity.front);
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
//				MainActivity.methodString = MainActivity.next;
//				Intent i =new Intent(TvMenu.this,MainActivity.class);
//				startActivity(i);
                MainActivity.web.loadUrl("javascript:a=undefined");
                MainActivity.web.loadUrl(MainActivity.next);
                finish();
            }
        });

        add.requestFocus();
    }
}
