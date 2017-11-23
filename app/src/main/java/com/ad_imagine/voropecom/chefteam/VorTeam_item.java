package com.ad_imagine.voropecom.chefteam;


        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;

public class VorTeam_item{

    private String title;
    private String title2;


    public VorTeam_item(String title, String title2) {
        this.title = title;
        this.title2 = title2;

    }




    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title) {
        this.title2 = title2;
    }

}
