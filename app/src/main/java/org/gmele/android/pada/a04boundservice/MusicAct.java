package org.gmele.android.pada.a04boundservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MusicAct extends AppCompatActivity implements View.OnClickListener, MusicInterface
{
    Button BtStart;
    Button BtStop;
    Button BtPlay;
    Button BtPause;
    Button BtPrev;
    Button BtNext;
    TextView TvSongTitle;
    boolean Connected;
    MusicService MusicServ;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_music);
        BtStart = findViewById (R.id.BtStart);
        BtStop = findViewById (R.id.BtStop);
        BtPlay = findViewById (R.id.BtPlay);
        BtPause = findViewById (R.id.BtPause);
        BtPrev = findViewById (R.id.BtPrev);
        BtNext = findViewById (R.id.BtNext);
        TvSongTitle = findViewById (R.id.TvSongTitle);
        BtStart.setOnClickListener (this);
        BtStop.setOnClickListener (this);
        BtPlay.setOnClickListener (this);
        BtPause.setOnClickListener (this);
        BtPrev.setOnClickListener (this);
        BtNext.setOnClickListener (this);
        TvSongTitle.setOnClickListener (this);
        Connected = false;
        ShowMessage ("Created");
    }

    @Override
    protected void onDestroy ()
    {
        super.onDestroy ();
        ShowMessage ("On Destroy");
    }


    @Override
    public void onClick (View v)
    {
        if (v == BtStart)
            DoStart ();
        if (v == BtStop)
            DoStop ();
        if (v == BtPlay)
            DoPlay ();
        if (v == BtPause)
            DoPause ();
        if (v == BtPrev)
            DoPrev ();
        if (v == BtNext)
            DoNext ();
        if (v == TvSongTitle)
            DoShowTitle ();
    }



    public void onBackPressed ()
    {
        super.onBackPressed ();
        finish ();
        ShowMessage ("Pressed Back");
    }


    void DoStart ()
    {
        if (!Connected)
        {
            Intent MusicInt = new Intent (this, MusicService.class);
            bindService (MusicInt, ServCon, Context.BIND_AUTO_CREATE);
            System.out.println ("**** 1");
        }
        else
            System.out.println ("**** 2");

    }

    void DoStop ()
    {
        if (Connected)
        {
            Connected = false;              //Γιατί και εδώ;;;
            unbindService (ServCon);
        }
    }

    void DoPlay ()
    {
        if (!Connected)
            return;
        MusicServ.PlaySong ();
    }

    void DoPause ()
    {
        if (!Connected)
            return;
        MusicServ.PauseSong ();
    }

    void DoNext ()
    {
        if (!Connected)
            return;
        MusicServ.NextSong ();
    }

    void DoPrev ()
    {
        if (!Connected)
            return;
        MusicServ.PreviousSong ();
    }

    void DoShowTitle ()
    {
        if (!Connected)
            return;
        String Titl = MusicServ.SongTitle ();
        TvSongTitle.setText (Titl);
    }

    private void ShowMessage (String Mess)
    {
        Toast Tst = Toast.makeText (getApplicationContext (), "Activity: " + Mess, Toast.LENGTH_LONG);
        Tst.show ();
    }

    private ServiceConnection ServCon = new ServiceConnection ()
    {

        @Override
        public void onServiceConnected (ComponentName className, IBinder service)
        {
            System.out.println ("***3");
            ShowMessage ("Connected to Service");
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            MusicServ = binder.getService();
            Connected = true;
            MusicServ.RegisterUpdates (MusicAct.this);
        }

        @Override
        public void onServiceDisconnected (ComponentName CompNam)   //Πότε καλείται αυτή; Όταν κληθεί η unBoundService π.χ.;;;;
        {
            System.out.println ("***4");
            ShowMessage ("Disconnected from Service");
            Connected = false;

        }
    };

    @Override
    public void UpdateTitle (String Titl)
    {
        TvSongTitle.setText (Titl);
    }

    /*
    @Override
    public void onConfigurationChanged (Configuration UpdatedConfig)
    {
        super.onConfigurationChanged (UpdatedConfig);

        if (UpdatedConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
            ShowMessage ("Changed to Landscape");
        else
            ShowMessage ("Changed to Portrait");
    }
    */
}