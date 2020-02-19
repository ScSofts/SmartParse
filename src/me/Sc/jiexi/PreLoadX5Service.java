package me.Sc.jiexi;

import com.tencent.smtt.sdk.QbSdk;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class PreLoadX5Service extends Service {
	@Nullable
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		initX5();
		preinitX5WebCore();
	}

	private void initX5() {
		QbSdk.initX5Environment(getApplicationContext(), cb);
		// Log.e("haha","预加载中...");
	}

	private QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

		@Override
		public void onViewInitFinished(boolean arg0) {
			// TODO Auto-generated method stub
			// Log.e("haha", " onViewInitFinished is " + arg0);
		}

		@Override
		public void onCoreInitFinished() {
			// TODO Auto-generated method stub
			// Log.e("haha","预加载中...onCoreInitFinished");
			Toast.makeText(getApplicationContext(), "极速内核加载成功，重启APP生效",
					Toast.LENGTH_LONG).show();
		}
	};

	private void preinitX5WebCore() {

		if (!QbSdk.isTbsCoreInited()) {

			// preinit只需要调用一次，如果已经完成了初始化，那么就直接构造view
			// Log.e("haha","预加载中...preinitX5WebCore");
			QbSdk.preInit(getApplicationContext(), null);// 设置X5初始化完成的回调接口

		}
	}
};
