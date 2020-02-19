package me.Sc.jiexi;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.PluginState;
import com.tencent.smtt.sdk.WebView;

public class MainActivity extends Activity {

    public static com.tencent.smtt.sdk.WebView web;
    public static String front = "javascript:if(typeof(a)==\"undefined\"&&typeof(xyplay)!=\"undefined\"){if(xyplay.video_front()==false){alert(\"已经第一集\");a=true}else{a=true;}}";//上集
    public static String next = "javascript:if(typeof(a)==\"undefined\"&&typeof(xyplay)!=\"undefined\"){if(xyplay.video_next()==false){alert(\"已经最后一集\");a=true}else{a=true;}}";//下集
    private MouseView mouseView;
    private boolean show = false;
    //public static String methodString="";//front
    private String lastUrlString = "";//上次打开

    private void ChangeMouse() {
        show = !show;
        mouseView.setVisibility((show) ? View.VISIBLE : View.INVISIBLE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        QbSdk.setDownloadWithoutWifi(true);
        QbSdk.initTbsSettings(map);
        // 启动服务
        Intent intent = new Intent(this, PreLoadX5Service.class);
        startService(intent);
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);// 防止视频闪烁
        getWindow().getDecorView().addOnLayoutChangeListener(
                new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top,
                                               int right, int bottom, int oldLeft, int oldTop,
                                               int oldRight, int oldBottom) {
                        ArrayList<View> outView = new ArrayList<View>();
                        getWindow().getDecorView().findViewsWithText(outView,
                                "QQ浏览器", View.FIND_VIEWS_WITH_TEXT);
                        if (outView != null && outView.size() > 0) {
                            outView.get(0).setVisibility(View.GONE);
                        }
                    }
                });// 去除 QQ浏览器推广
        setContentView(R.layout.activity_main);// 显示窗口内容
        String cachePath = getApplicationContext().getExternalCacheDir()
                .getParentFile().getPath();
        String iniName = cachePath + "/fav.ini";
        IniFile iniFile = new IniFile(new File(iniName));
        iniFile.set("Fav", "智能解析", "http://scvip.dowy.cn/so.php");
        iniFile.save();
        SystemClock.sleep(200);
        initWebView();
        mouseView = (MouseView) findViewById(R.id.mouse_view);
        mouseView.requestFocus();
        ChangeMouse();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.Like) {
            System.out.println("Like!");
            String cachePath = getApplicationContext().getExternalCacheDir()
                    .getParentFile().getPath();
            String iniName = cachePath + "/fav.ini";
            IniFile iniFile = new IniFile(new File(iniName));
            iniFile.set("Fav", "\"" + web.getTitle() + "\"", "\""
                    + web.getUrl().replace("=", "·") + "\" \n");
            System.out.println("\"" + web.getUrl().replace("=", "·") + "\"");
            iniFile.save();
            return true;
        } else if (id == R.id.open) {
            Intent i = new Intent(MainActivity.this, Favourite.class);
            startActivity(i);
            return true;
        } else if (id == R.id.Mouse) {
            ChangeMouse();
        }else if(id == R.id.Fly){
            Intent i = new Intent(MainActivity.this, Fly.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_MENU) {

            if ((event.getFlags() & KeyEvent.FLAG_LONG_PRESS) != 0) {
                ChangeMouse();
                return true;
            }
        }
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (web.canGoBack()) {
                web.goBack();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {//遥控器菜单键
            //传递URL 和 Title 为了收藏时使用
            TvMenu.title = web.getTitle();
            TvMenu.url = web.getUrl();
            //启动TV Menu窗口
            Intent intent = new Intent(MainActivity.this, TvMenu.class);
            startActivity(intent);
            lastUrlString = web.getUrl();
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        web = (com.tencent.smtt.sdk.WebView) findViewById(R.id.web);
        
        web.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        web.getSettings().setAppCacheEnabled(true);
        web.getSettings().setAllowFileAccess(true);
        web.getSettings().setUseWideViewPort(true);
        web.getSettings().setPluginState(PluginState.ON);
        web.getSettings().setAppCacheMaxSize(20 * 1024 * 1024);
        web.getSettings().setUserAgent("Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36");
        web.setWebViewClient(new com.tencent.smtt.sdk.WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView arg0, String arg1) {
                // TODO Auto-generated method stub
                arg0.loadUrl(arg1);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView arg0, SslErrorHandler arg1,
                                           SslError arg2) {
                // TODO Auto-generated method stub
                arg1.proceed();
               // Toast.makeText(MainActivity.this, "SSL证书错误!",
                 //       Toast.LENGTH_SHORT).show();
            }
           
        });
        
        web.setWebChromeClient(new WebChromeClient(){
        	@Override
        	public void onReceivedTitle(WebView arg0, String arg1) {
        		// TODO Auto-generated method stub
        		setTitle(web.getTitle());
        		super.onReceivedTitle(arg0, arg1);
        	}
        	
        });
        web.getSettings().setJavaScriptEnabled(true);
        if (lastUrlString == "") {
            web.loadUrl("http://scvip.dowy.cn/so.php");
            lastUrlString = web.getUrl();
        } else {
            web.loadUrl(lastUrlString);
//			new Thread(new Runnable() {
//
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					try {
//						Thread.sleep(3000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					web.loadUrl(methodString);
//					try {
//						Thread.sleep(3000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					web.loadUrl(methodString);
//					methodString = "";
//					web.loadUrl("javascript:a=undefined");
//				}
//			}).run();
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        mouseView.moveMouse(web, event);
        if (event.getKeyCode() == KeyEvent.KEYCODE_MENU ||
                event.getKeyCode() == KeyEvent.KEYCODE_BACK ||
                event.getKeyCode() == KeyEvent.KEYCODE_HOME) {
            return super.dispatchKeyEvent(event);
        }
        if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
        	return true;
        }
        return super.dispatchKeyEvent(event);
        //return true;
    }


}
