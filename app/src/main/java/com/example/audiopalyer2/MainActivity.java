package com.example.audiopalyer2;

import android.annotation.*;
import android.app.*;
import android.content.*;
import android.media.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import java.util.concurrent.*;

public class MainActivity extends Activity {

    ImageView play, prev, next, imageView;
    TextView songTitle,tv_songTime,tv_startTime;
    SeekBar mSeekBarTime, mSeekBarVol;
    static MediaPlayer mMediaPlayer;
    Runnable runnable;
    Handler hldr=new Handler();
    static int endTime=0,startTime=0,onTime;
    private AudioManager mAudioManager;
    int currentIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // initializing views

        play = findViewById(R.id.play);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        songTitle = findViewById(R.id.songTitle);
        imageView = findViewById(R.id.imageView);
        mSeekBarTime = findViewById(R.id.seekBarTime);
        mSeekBarVol = findViewById(R.id.seekBarVol);
        tv_startTime=findViewById(R.id.tv_startTime);
        tv_songTime=findViewById(R.id.tv_songTime);

        // creating an ArrayList to store our songs

        final ArrayList<Integer> songs = new ArrayList<>();

        songs.add(0, R.raw.bedardeya);
        songs.add(1, R.raw.barsat);
        songs.add(2, R.raw.piya);
        songs.add(3, R.raw.aarambh);
        songs.add(4, R.raw.tuhaito);


        // intializing mediaplayer

        mMediaPlayer = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex));



        // seekbar volume

        int maxV = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curV = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mSeekBarVol.setMax(maxV);
        mSeekBarVol.setProgress(curV);

        mSeekBarVol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });


        //above seekbar volume
        //

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBarTime.setMax(mMediaPlayer.getDuration());

                if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    play.setImageResource(R.drawable.pause);
                    Toast.makeText(MainActivity.this,"Pause Audio",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mMediaPlayer.start();
                    play.setImageResource(R.drawable.play);
                    Toast.makeText(MainActivity.this,"Playing Audio",Toast.LENGTH_SHORT).show();

                }

                UpdateSeekbar updateSeekbar= new UpdateSeekbar();
                hldr.post(updateSeekbar);
                endTime=mMediaPlayer.getDuration();
                startTime=mMediaPlayer.getCurrentPosition();
                if (onTime == 0)
                {
                    mSeekBarTime.setMax(endTime);
                    onTime=1;
                }

                tv_songTime.setText(String.format("%d min,%d sec", TimeUnit.MILLISECONDS.toMinutes(endTime),
                        TimeUnit.MILLISECONDS.toSeconds(endTime)- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime))));

                tv_startTime.setText(String.format("%d min,%d sec", TimeUnit.MILLISECONDS.toMinutes(startTime),
                        TimeUnit.MILLISECONDS.toSeconds(startTime)- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(startTime))));
                mSeekBarTime.setProgress(startTime);
                hldr.postDelayed(UpdateSongTime,100);

                songNames();



            }


        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayer != null) {
                    play.setImageResource(R.drawable.play);
                    Toast.makeText(MainActivity.this,"forword Audio",Toast.LENGTH_SHORT).show();
                }

                if (currentIndex < songs.size() - 1) {
                    currentIndex++;
                }
                else
                {
                    currentIndex = 0;
                }

                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }

                mMediaPlayer = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex));

                mMediaPlayer.start();
                UpdateSeekbar updateSeekbar= new UpdateSeekbar();
                hldr.post(updateSeekbar);
                endTime=mMediaPlayer.getDuration();
                startTime=mMediaPlayer.getCurrentPosition();
                if (onTime == 0)
                {
                    mSeekBarTime.setMax(endTime);
                    onTime=1;
                }

                tv_songTime.setText(String.format("%d min,%d sec", TimeUnit.MILLISECONDS.toMinutes(endTime),
                        TimeUnit.MILLISECONDS.toSeconds(endTime)- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime))));

                tv_startTime.setText(String.format("%d min,%d sec", TimeUnit.MILLISECONDS.toMinutes(startTime),
                        TimeUnit.MILLISECONDS.toSeconds(startTime)- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(startTime))));
                mSeekBarTime.setProgress(startTime);
                hldr.postDelayed(UpdateSongTime,100);

                songNames();

            }
        });


        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mMediaPlayer != null) {
                    play.setImageResource(R.drawable.play);
                    Toast.makeText(MainActivity.this,"Backword Audio",Toast.LENGTH_SHORT).show();
                }

                if (currentIndex > 0) {
                    currentIndex--;
                } else {
                    currentIndex = songs.size() - 1;
                }
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }

                mMediaPlayer = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex));
                mMediaPlayer.start();

                UpdateSeekbar updateSeekbar= new UpdateSeekbar();
                hldr.post(updateSeekbar);
                endTime=mMediaPlayer.getDuration();
                startTime=mMediaPlayer.getCurrentPosition();
                if (onTime == 0)
                {
                    mSeekBarTime.setMax(endTime);
                    onTime=1;
                }

                tv_songTime.setText(String.format("%d min,%d sec", TimeUnit.MILLISECONDS.toMinutes(endTime),
                        TimeUnit.MILLISECONDS.toSeconds(endTime)- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime))));

                tv_startTime.setText(String.format("%d min,%d sec", TimeUnit.MILLISECONDS.toMinutes(startTime),
                        TimeUnit.MILLISECONDS.toSeconds(startTime)- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(startTime))));
                mSeekBarTime.setProgress(startTime);
                hldr.postDelayed(UpdateSongTime,100);
                songNames();

            }
        });

    }

    private void songNames() {


        if (currentIndex == 0) {
            songTitle.setText("O Bedardeya..♬♩♪♩ ♩♪♩♬");
            imageView.setImageResource(R.drawable.bedardeya);
        }

        if (currentIndex == 1) {
            songTitle.setText("Barsat Aa Gayi..♬♩♪♩ ♩♪♩♬");
            imageView.setImageResource(R.drawable.barsat);
        }
        if (currentIndex == 2) {
            songTitle.setText("Piya O Re Piya..♩♪♩♬");
            imageView.setImageResource(R.drawable.piya);
        }
        if (currentIndex == 3) {
            songTitle.setText("Aarambh Hai Prachand.༺༻");
            imageView.setImageResource(R.drawable.aarambh);
        }
        if (currentIndex == 4) {
            songTitle.setText("Phir Aur Kya Chahiye.♩♪♩♬");
            imageView.setImageResource(R.drawable.tuhaito);
        }


        // seekbar duration
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mSeekBarTime.setMax(mMediaPlayer.getDuration());
                mMediaPlayer.start();

            }
        });



        mSeekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mMediaPlayer.seekTo(progress);
                    mSeekBarTime.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mMediaPlayer != null) {
                    try {
                        if (mMediaPlayer.isPlaying()) {
                            Message message = new Message();
                            message.what = mMediaPlayer.getCurrentPosition();
                            handler.sendMessage(message);
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @SuppressLint("Handler Leak") Handler handler = new Handler () {
        @Override
        public void handleMessage  (Message msg) {
            mSeekBarTime.setProgress(msg.what);
        }
    };


    private Runnable UpdateSongTime = new Runnable() {

        @Override
        public void run() {
            startTime=mMediaPlayer.getCurrentPosition();
            tv_startTime.setText(String.format("%d min,%d sec", TimeUnit.MILLISECONDS.toMinutes(startTime),
                    TimeUnit.MILLISECONDS.toSeconds(startTime)- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(startTime))));
            mSeekBarTime.setProgress(startTime);
            hldr.postDelayed(this,100);
        }

    };

    public class UpdateSeekbar implements Runnable
    {

        @Override
        public void run()
        {
            mSeekBarTime.setProgress(mMediaPlayer.getCurrentPosition());
            hldr.postDelayed(this,100);
        }
    }


}







