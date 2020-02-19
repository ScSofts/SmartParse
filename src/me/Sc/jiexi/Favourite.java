package me.Sc.jiexi;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Favourite extends Activity {
	private ListView listView;
	public String iniName = "";
	private IniFile iniFile;
	private ArrayAdapter<String> adapter;
	private boolean LongClick = false;
	private MouseView mouseView;
	private boolean show = false;

	private void ChangeMouse() {
		show = !show;
		mouseView.setVisibility((show)?View.VISIBLE:View.INVISIBLE);
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favourite);
		listView = (ListView) findViewById(R.id.Favourites);
		mouseView = (MouseView) findViewById(R.id.mouse_view_Fav);
		
		String cachePath = getApplicationContext().getExternalCacheDir()
				.getParentFile().getPath();
		iniName = cachePath + "/fav.ini";
		iniFile = new IniFile(new File(iniName));
		// adapter.
		String s[] = { "" };
		try{
		s = iniFile.get("Fav").getValues().keySet().toArray(s);
		}catch(Exception e){
			Toast.makeText(Favourite.this, "没有收藏项目", Toast.LENGTH_SHORT).show();
			this.finish();
		}
		if (s == null || s[0] == null) {
			Toast.makeText(Favourite.this, "没有收藏项目", Toast.LENGTH_SHORT).show();
			this.finish();
		}
		adapter = new ArrayAdapter<String>(Favourite.this,
				android.R.layout.simple_list_item_1, s);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (!LongClick) {
					// TODO Auto-generated method stub
					String s = (String) listView.getItemAtPosition(arg2);
					String cachePath = getApplicationContext()
							.getExternalCacheDir().getParentFile().getPath();
					String iniName = cachePath + "/fav.ini";
					IniFile iniFile = new IniFile(new File(iniName));
					MainActivity.forOpenString = (String) iniFile.get("Fav")
							.getValues().get(s);
					MainActivity.forOpenString = MainActivity.forOpenString
							.replace("\"", "");
					MainActivity.forOpenString = MainActivity.forOpenString
							.replace("·", "=");
					System.out.println(MainActivity.forOpenString);
					MainActivity.web.loadUrl(MainActivity.forOpenString);
					finish();
				}
			}
		});

		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				final int x = arg2;
				LongClick = true;
				new AlertDialog.Builder(Favourite.this)
						.setTitle("确认删除吗？")
						.setIcon(android.R.drawable.ic_dialog_info)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// 点击“确认”后
										String s = (String) listView
												.getItemAtPosition(x);
										String cachePath = getApplicationContext()
												.getExternalCacheDir()
												.getParentFile().getPath();
										String iniName = cachePath + "/fav.ini";
										IniFile iniFile = new IniFile(new File(
												iniName));
										iniFile.remove("Fav", s);
										iniFile.save();
										Intent intent = new Intent(
												Favourite.this, Favourite.class);

										startActivity(intent);
										finish();

									}
								})
						.setNegativeButton("返回",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// 点击“返回”后的操作,这里不设置没有任何操作

									}
								}).show();
				// super.onBackPressed();
				LongClick = false;
				return false;
			}
		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_MENU){
			openOptionsMenu();
			return true;
		}
		return super.onKeyDown(keyCode, event);
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
		if (id == R.id.Mouse){
			ChangeMouse();
		}
		return super.onOptionsItemSelected(item);
	}
}
