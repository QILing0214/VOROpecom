package com.ad_imagine.voropecom.utilisateur;

/**
 * Created by chu on 2016/6/7.
 */

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.ad_imagine.voropecom.R;

public class process extends Fragment implements OnSeekBarChangeListener {
    public process() {
    }
    SeekBar seekbar1,seekbar2,seekbar3,seekbar11,seekbar12,seekbar13;

    TextView result01,result02,result03,result11,result12,result13;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.process, container, false);
        seekbar1=(SeekBar)rootView.findViewById(R.id.seekBar1);
        seekbar2=(SeekBar)rootView.findViewById(R.id.seekBar2);
        seekbar3=(SeekBar)rootView.findViewById(R.id.seekBar3);
        seekbar11=(SeekBar)rootView.findViewById(R.id.seekBar11);
        seekbar12=(SeekBar)rootView.findViewById(R.id.seekBar12);
        seekbar13=(SeekBar)rootView.findViewById(R.id.seekBar13);
        seekbar1.setOnSeekBarChangeListener(this);
        seekbar2.setOnSeekBarChangeListener(this);
        seekbar3.setOnSeekBarChangeListener(this);
        seekbar11.setOnSeekBarChangeListener(this);
        seekbar12.setOnSeekBarChangeListener(this);
        seekbar13.setOnSeekBarChangeListener(this);
        result01=(TextView)rootView.findViewById(R.id.note01);
        result02=(TextView)rootView.findViewById(R.id.note02);
        result03=(TextView)rootView.findViewById(R.id.note03);
        result11=(TextView)rootView.findViewById(R.id.note11);
        result12=(TextView)rootView.findViewById(R.id.note12);
        result13=(TextView)rootView.findViewById(R.id.note13);
        // set images of thunb and progress bar
        seekbar1.setThumb(getResources().getDrawable(R.drawable.greenthumb));
        seekbar2.setThumb(getResources().getDrawable(R.drawable.greenthumb));
        seekbar3.setThumb(getResources().getDrawable(R.drawable.greenthumb));
        seekbar11.setThumb(getResources().getDrawable(R.drawable.greenthumb));
        seekbar12.setThumb(getResources().getDrawable(R.drawable.greenthumb));
        seekbar13.setThumb(getResources().getDrawable(R.drawable.greenthumb));

        seekbar1.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar2));
        seekbar2.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar2));
        seekbar3.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar2));
        seekbar11.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar2));
        seekbar12.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar2));
        seekbar13.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar2));



        //start activity  Note.class
        Button button=(Button)rootView.findViewById(R.id.button8);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Note.class);
                intent.putExtra("extra_data_note", "3");
                startActivity(intent);
            }
        });
        return rootView;
    }
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        switch (seekBar.getId()) {

            case R.id.seekBar1:
                progress++;
                // change progress text label with current see_kbar value
                result01.setText("" + progress);


                if (progress == 1 || progress == 2 || progress == 3) {
                    seekbar1.setThumb(getResources().getDrawable(R.drawable.greenthumb));
                    result01.setBackgroundResource(R.drawable.greennumber);

                }
                if (progress == 4 || progress == 5 || progress == 6) {
                    seekbar1.setThumb(getResources().getDrawable(R.drawable.orangethumb));
                    result01.setBackgroundResource(R.drawable.orangenumber);
                }
                if (progress == 7 || progress == 8 || progress == 9) {
                    seekbar1.setThumb(getResources().getDrawable(R.drawable.redthumb));
                    result01.setBackgroundResource(R.drawable.rednumber);
                }
                break;

            case R.id.seekBar2:
                progress++;
                // change progress text label with current see_kbar value
                result02.setText("" + progress);

                if (progress == 1 || progress == 2 || progress == 3) {
                    seekbar2.setThumb(getResources().getDrawable(R.drawable.greenthumb));
                    result02.setBackgroundResource(R.drawable.greennumber);
                }
                if (progress == 4 || progress == 5 || progress == 6) {
                    seekbar2.setThumb(getResources().getDrawable(R.drawable.orangethumb));
                    result02.setBackgroundResource(R.drawable.orangenumber);
                }
                if (progress == 7 || progress == 8 || progress == 9) {
                    seekbar2.setThumb(getResources().getDrawable(R.drawable.redthumb));
                    result02.setBackgroundResource(R.drawable.rednumber);
                }
                break;

            case R.id.seekBar3:
                progress++;
                // change progress text label with current see_kbar value
                result03.setText("" + progress);

                if (progress == 1 || progress == 2 || progress == 3) {
                    seekbar3.setThumb(getResources().getDrawable(R.drawable.greenthumb));
                    result03.setBackgroundResource(R.drawable.greennumber);
                }
                if (progress == 4 || progress == 5 || progress == 6) {
                    seekbar3.setThumb(getResources().getDrawable(R.drawable.orangethumb));
                    result03.setBackgroundResource(R.drawable.orangenumber);
                }
                if (progress == 7 || progress == 8 || progress == 9) {
                    seekbar3.setThumb(getResources().getDrawable(R.drawable.redthumb));
                    result03.setBackgroundResource(R.drawable.rednumber);
                }
                break;

            case R.id.seekBar11:
                progress++;
                // change progress text label with current see_kbar value
                result11.setText("" + progress);

                if (progress == 1 || progress == 2 || progress == 3) {
                    seekbar11.setThumb(getResources().getDrawable(R.drawable.greenthumb));
                    result11.setBackgroundResource(R.drawable.greennumber);
                }
                if (progress == 4 || progress == 5 || progress == 6) {
                    seekbar11.setThumb(getResources().getDrawable(R.drawable.orangethumb));
                    result11.setBackgroundResource(R.drawable.orangenumber);
                }
                if (progress == 7 || progress == 8 || progress == 9) {
                    seekbar11.setThumb(getResources().getDrawable(R.drawable.redthumb));
                    result11.setBackgroundResource(R.drawable.rednumber);
                }
                break;

            case R.id.seekBar12:
                progress++;
                // change progress text label with current see_kbar value
                result12.setText("" + progress);

                if (progress == 1 || progress == 2 || progress == 3) {
                    seekbar12.setThumb(getResources().getDrawable(R.drawable.greenthumb));
                    result12.setBackgroundResource(R.drawable.greennumber);
                }
                if (progress == 4 || progress == 5 || progress == 6) {
                    seekbar12.setThumb(getResources().getDrawable(R.drawable.orangethumb));
                    result12.setBackgroundResource(R.drawable.orangenumber);
                }
                if (progress == 7 || progress == 8 || progress == 9) {
                    seekbar12.setThumb(getResources().getDrawable(R.drawable.redthumb));
                    result12.setBackgroundResource(R.drawable.rednumber);
                }
                break;

            case R.id.seekBar13:
                progress++;
                // change progress text label with current see_kbar value
                result13.setText("" + progress);

                if (progress == 1 || progress == 2 || progress == 3) {
                    seekbar13.setThumb(getResources().getDrawable(R.drawable.greenthumb));
                    result13.setBackgroundResource(R.drawable.greennumber);
                }
                if (progress == 4 || progress == 5 || progress == 6) {
                    seekbar13.setThumb(getResources().getDrawable(R.drawable.orangethumb));
                    result13.setBackgroundResource(R.drawable.orangenumber);
                }
                if (progress == 7 || progress == 8 || progress == 9) {
                    seekbar13.setThumb(getResources().getDrawable(R.drawable.redthumb));
                    result13.setBackgroundResource(R.drawable.rednumber);
                }
                break;


        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

        //("starting to track touch");

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        seekBar.setSecondaryProgress(seekBar.getProgress());
        // ("ended tracking touch");
    }
}
