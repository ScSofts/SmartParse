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
		// Log.e("haha","Ԥ������...");
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
			// Log.e("haha","Ԥ������...onCoreInitFinished");
			Toast.makeText(getApplicationContext(), "�����ں˼��سɹ�������APP��Ч",
					Toast.LENGTH_LONG).show();
		}
	};

	private void preinitX5WebCore() {

		if (!QbSdk.isTbsCoreInited()) {

			// preinitֻ��Ҫ����һ�Σ�����Ѿ�����˳�ʼ������ô��ֱ�ӹ���view
			// Log.e("haha","Ԥ������...preinitX5WebCore");
			QbSdk.preInit(getApplicationContext(), null);// ����X5��ʼ����ɵĻص��ӿ�

		}
	}
};
