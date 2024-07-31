package org.gmele.android.pada.a04boundservice;

import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener
{
    final int TimeToPlay = 0;
    String[] SongTitles = {"Νοσταλγός του Rock N Roll", "Μάλιστα Κύριε", "Nothing Else Matters"};
    int[] SongIDs;
    int CurSong;
    MediaPlayer MP;
    Boolean Act;
    MusicInterface RegUpdates = null;

    public MusicService ()
    {

    }

    public void onCreate ()
    {
        ShowMessage ("On Create");
        SongIDs = new int[3];
        SongIDs[0] = R.raw.nostalgosrocknroll;
        SongIDs[1] = R.raw.malistakyrie;
        SongIDs[2] = R.raw.nothingelsematters;
        CurSong = 0;
        Act = false;                        //Μόνο για την περίπτωση που τα επόμεαν μεταφερθούν αλλού
        MP = MediaPlayer.create (this, SongIDs[CurSong]);
        MP.setOnCompletionListener (this);
        MP.start ();
        UpdateTitle ();
        if (TimeToPlay > 0)
            MP.seekTo (MP.getDuration () - TimeToPlay);
        Act = true;
    }

    public void onDestroy ()
    {
        ShowMessage ("on Destroy");
        MP.stop ();
        MP.release ();          //Χρειάζεται να εκτελεστούν;
        Act = false;
    }

    @Override
    public void onCompletion (MediaPlayer mp)
    {
        ShowMessage ("Song Completed");
        NextSong ();

    }

    public void NextSong ()
    {
        if (MP.isPlaying ())
        {
            MP.stop ();
            MP.release ();
        }
        if (++CurSong == 3)
            CurSong = 0;
        MP = MediaPlayer.create (this, SongIDs[CurSong]);
        MP.setOnCompletionListener (this);
        if (Act)
        {
            MP.start ();
            if (TimeToPlay > 0)
                MP.seekTo (MP.getDuration () - TimeToPlay);
            UpdateTitle ();
        }
        ShowMessage ("Next Song");
    }

    public void PreviousSong ()
    {
        if (MP.isPlaying ())
        {
            MP.stop ();
            MP.release ();
        }
        if (--CurSong == -1)
            CurSong = 2;
        MP = MediaPlayer.create (this, SongIDs[CurSong]);
        MP.setOnCompletionListener (this);
        if (Act)
        {
            MP.start ();
            if (TimeToPlay > 0)
                MP.seekTo (MP.getDuration () - TimeToPlay);
            UpdateTitle ();
        }
        ShowMessage ("Previous Song");
    }

    public void PlaySong ()
    {
        if (Act)
            return;
        MP.start ();
        if (TimeToPlay > 0)
            MP.seekTo (MP.getDuration () - TimeToPlay);
        UpdateTitle ();
        Act = true;
        ShowMessage ("Play Song");

    }

    public void PauseSong ()
    {
        if (!Act)
            return;
        MP.pause ();
        Act = false;
        ShowMessage ("Pause Song");
    }

    public String SongTitle ()
    {
        ShowMessage ("Song Title");
        return SongTitles [CurSong];
    }

    public void RegisterUpdates (MusicInterface Activ)
    {
        RegUpdates = Activ;
    }

    public void UpdateTitle ()
    {
        if (RegUpdates != null)
            RegUpdates.UpdateTitle (SongTitles[CurSong]);
    }

    private void ShowMessage (String Mess)
    {
        Toast Tst = Toast.makeText (getApplicationContext (), "Service: " + Mess, Toast.LENGTH_LONG);
        Tst.show ();
    }

    //===================================================================================
    // Needed for Binding
    private final IBinder Binder = new LocalBinder ();

    public class LocalBinder extends Binder
    {
        MusicService getService ()
        {
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind (Intent intent)
    {
        ShowMessage ("Someone binded");
        return Binder;
    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        ShowMessage ("Someone Unbinded");
        return false;              //Allow Rebind? For started services
    }


}