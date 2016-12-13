package android.com.vitamio_sample;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnErrorListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;


public class VideoPlayLayout extends RelativeLayout {
	private PlayerBackInterface mPlayerBackInterface;
	private MediaController mMediaController;
	private VideoView mVideoView;
	private ProgressBar pb;
	private TextView downloadRateView, loadRateView;
	private Context mContext;
	private View view;
	private View loadView;
	private MediaPlayer.OnPreparedListener OnPreparedListener = new OnPreparedListener() {

		@Override
		public void onPrepared(MediaPlayer mp) {
			mp.setPlaybackSpeed(1.0f);
			mp.setBufferSize(512 * 1024);
			mPlayerBackInterface.OnPreparedListener(mp);
		}
	};
	private MediaPlayer.OnInfoListener OnInfoListener = new OnInfoListener() {

		@Override
		public boolean onInfo(MediaPlayer mp, int what, int extra) {
			switch (what) {
			case MediaPlayer.MEDIA_INFO_BUFFERING_START:
				if (mVideoView.isPlaying()) {
					mVideoView.pause();

					loadView.setVisibility(View.VISIBLE);
					downloadRateView.setText("");
					loadRateView.setText("");
					//pb.setVisibility(View.VISIBLE);
					//downloadRateView.setVisibility(View.VISIBLE);
					//loadRateView.setVisibility(View.VISIBLE);

				}
				break;
			case MediaPlayer.MEDIA_INFO_BUFFERING_END:
				mVideoView.start();
				if(mVideoView.getNeedToSeek()){
					mVideoView.setNeedToSeek(false);
					mVideoView.setmSeekWhenPrepared(0);
				}
				//pb.setVisibility(View.GONE);
//				loadView.setBackgroundResource(R.drawable.touming);
				loadView.setVisibility(View.GONE);

				//downloadRateView.setVisibility(View.GONE);
				//loadRateView.setVisibility(View.GONE);
				break;
			case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
				downloadRateView.setText("速率:" + extra + "KB/S" + "  ");
				break;
			}
			return true;
		}
	};
	private OnBufferingUpdateListener onBufferUpdateListener = new OnBufferingUpdateListener() {

		@Override
		public void onBufferingUpdate(MediaPlayer mp, int percent) {
			loadRateView.setText("缓冲:"+percent + "%");

		}
	};
	private OnErrorListener onErrorListener = new OnErrorListener() {

		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			if(mPlayerBackInterface!=null){
				// CommonsUtil.showToast(mContext, "播放错误", 1);

				 mPlayerBackInterface.OnCompletionListener(mp);
		     }
			return false;
		}
	};
	private MediaPlayer.OnCompletionListener OnCompletionListener = new OnCompletionListener() {

		@Override
		public void onCompletion(MediaPlayer mp) {
		     if(mPlayerBackInterface!=null){
		    	 mPlayerBackInterface.OnCompletionListener(mp);
		     }

		}
	};
	public VideoPlayLayout(Context paramContext) {
		super(paramContext);
		this.mContext = paramContext;
		if (!isInEditMode())
			InitView();
	}
	public VideoPlayLayout(Context paramContext,
						   AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		this.mContext = paramContext;
		if (!isInEditMode())
			InitView();
	}
	public VideoPlayLayout(Context paramContext,
						   AttributeSet paramAttributeSet, int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
		this.mContext = paramContext;
		if (!isInEditMode())
			InitView();
	}

	private void InitView() {
		this.view = LayoutInflater.from(this.mContext).inflate(
				getResources().getIdentifier("mediaplayer", "layout",
						mContext.getPackageName()), this, true);
		this.mVideoView = ((VideoView) this.view.findViewById(getResources().getIdentifier("video_id", "id",
				mContext.getPackageName())));
		this.loadView = this.view.findViewById(getResources().getIdentifier("play_layout_initload", "id",
				mContext.getPackageName()));

		pb = (ProgressBar) findViewById(getResources().getIdentifier("probar", "id",
				mContext.getPackageName()));
		loadRateView = (TextView) findViewById(getResources().getIdentifier("load_rate", "id",
				mContext.getPackageName()));
		downloadRateView = (TextView) findViewById(getResources().getIdentifier("download_rate", "id",
				mContext.getPackageName()));
		mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH); //高品质
		mVideoView.setVideoChroma(MediaPlayer.VIDEOCHROMA_RGB565);
		mVideoView.setOnPreparedListener(OnPreparedListener);
		mVideoView.setOnInfoListener(OnInfoListener);
		mVideoView.setOnBufferingUpdateListener(onBufferUpdateListener);
		mVideoView.setOnErrorListener(onErrorListener);
		mVideoView.setOnCompletionListener(OnCompletionListener);
	}

	public long getSeek(){
		return mVideoView==null ? -1 : mVideoView.getCurrentPosition();
	}

    public void setSeek(long seek){
    	mVideoView.seekTo(seek);
    }
	
	public void PlayStart(String playUrl,long seek,PlayerBackInterface l){
		if(TextUtils.isEmpty(playUrl)) return;
		mPlayerBackInterface = l;
		//mVideoView.setPauseIcon(pauseImg);
		mMediaController = new MediaController(mContext);
		mVideoView.setMediaController(mMediaController);
		mVideoView.setVideoURI(Uri.parse(playUrl));
		if(seek>0){
			mVideoView.seekTo(seek);
		}
	}
	public void FullView(){
		mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
	}

	public void PlayPause() {
		mVideoView.pause();
	}
	public void PlaySuspend(){
		this.mVideoView.suspend();
	}
	public void PlayResume(){
	   this.mVideoView.resume();	
	}
	public void PlayStop() {
		mVideoView.stopPlayback();
	}
	public void dismis(){
		if(pb!=null){
			pb.setVisibility(View.GONE);
		}
	}
	/**播放完后调到开始位置*/
	public void endJumpToStart(){
		mVideoView.endJumpToStart();
	}
	public void setEnableShow(boolean isEnable){
		Log.i("cgf","----Videoplay--setEnableShow---"+isEnable);
		if(null != mMediaController){
			mMediaController.setVisibility(isEnable?View.VISIBLE:View.GONE);
			mMediaController.setHasDismiss(!isEnable);
			if(mVideoView != null){
				if(isEnable){
					mVideoView.start();
				}else{
					if(mVideoView.isPlaying()){
						mVideoView.pause();
					}
				}
			}
		}
	}
}
