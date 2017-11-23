package com.ad_imagine.voropecom.utilisateur;

/**
 * Created by chu on 2016/6/7.
 */
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.ad_imagine.voropecom.R;

public class coach extends Fragment {
    public coach() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.utilisateur_coach, container, false);
        Button button=(Button)rootView.findViewById(R.id.button10);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                String subject="coach";
//                String message="coach contact";
//                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
//                        "mailto", "email@email.com", null));
//                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
//                intent.putExtra(Intent.EXTRA_TEXT, message);
//                startActivity(intent);
            sendEmail();

            }
        });
        return rootView;
    }
    protected void sendEmail() {

        String[] TO = {"g.traglia@ad-imagine.com"};
       // String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        //emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("plain/text");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
       // emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[OPECOM]Test-Coach-Phrase1");
       // emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));


        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
