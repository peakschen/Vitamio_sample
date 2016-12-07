package android.com.vitamio_sample;

import java.util.HashMap;

import io.vov.vitamio.MediaPlayer;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.app.Activity;
import android.content.res.Configuration;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {
	
    private VideoPlayLayout mVideoPlayerLayout;
    private RelativeLayout advertvideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.liveshow);
        advertvideo = (RelativeLayout) findViewById(R.id.advertvideo);
        mVideoPlayerLayout = (VideoPlayLayout) findViewById(R.id.home_video);

//        String address = "http://220.181.23.202/s1/UvFuJjXR07s7t1nO/38694/v477/3/6/timdOsC5R7yf9FTPdtRqkw.mp4";
        String str = "http://120.25.126.95:8089/vod/DEFAULT_DEFAULT0000000000000000010000121E000000001";
//        String str1 = "http://gslb.miaopai.com/stream/oxX3t3Vm5XPHKUeTS-zbXA__.mp4";

        mVideoPlayerLayout.PlayStart(str, 0, new PlayerBackInterface() {
            @Override
            public void OnPreparedListener(io.vov.vitamio.MediaPlayer mp) {

            }

            @Override
            public void OnCompletionListener(io.vov.vitamio.MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setFullScreen();
            setFullScreenChange();
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setNormalScreen();
            cancelFullScreenChange();
        }
        super.onConfigurationChanged(newConfig);
    }
    public void setFullScreen() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mVideoPlayerLayout.FullView();
        mVideoPlayerLayout.setLayoutParams(layoutParams);
        advertvideo.setLayoutParams(layoutParams);
    }
    public void setNormalScreen() {
        int height = dip2px(this, 193f);
        Log.i("cgf","---setNormalScreen----"+getResources().getDisplayMetrics().widthPixels+"||"+height);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
        layoutParams.setMargins(0, dip2px(this, 45f), 0, 0);
        advertvideo.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
        layoutParams1.setMargins(0, 0, 0, 0);
        mVideoPlayerLayout.setLayoutParams(layoutParams1);
        mVideoPlayerLayout.FullView();
    }

    public static int dip2px(Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }

    public void setFullScreenChange(){
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);//全屏
    }
    public void cancelFullScreenChange(){
        getWindow().clearFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN);
    }
}
