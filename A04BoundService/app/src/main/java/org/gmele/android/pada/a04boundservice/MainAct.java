package org.gmele.android.pada.a04boundservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

public class MainAct extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener
{
    ConstraintLayout ClMain;
    ImageView Iv;
    Button BtMusic;


    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        ClMain = findViewById (R.id.ClMain);
        Iv = findViewById (R.id.IvVfV);
        Iv.setOnClickListener (this);
        Iv.setOnTouchListener (this);
        BtMusic = findViewById (R.id.BtMusic);
        BtMusic.setOnClickListener (this);


    }

    @Override
    public void onClick (View v)
    {
        if (v == Iv)
        {
            Toast Tst = Toast.makeText (this, "You did it....", Toast.LENGTH_LONG);
            Tst.show ();
        }
        if (v == BtMusic)
        {
            Intent InMus = new Intent (getApplicationContext (), MusicAct.class);
            startActivity (InMus);
        }
    }

    @Override
    public boolean onTouch (View v, MotionEvent event)
    {
        System.out.println ("*** in here");
        float Hbias = new Random ().nextFloat ();
        float Vbias = new Random ().nextFloat ();

        ConstraintSet ConSet = new ConstraintSet();
        ConSet.clone (ClMain);

        ConSet.setHorizontalBias (Iv.getId(), Hbias);
        ConSet.setVerticalBias (Iv.getId(), Vbias);
        ConSet.applyTo (ClMain);
        return false;
    }
}