package com.ad_imagine.voropecom.fragment_coach;

public class coache_historique_item {

    private int imgId;
    private String title;
    private String title2;


    public coache_historique_item(int imgId, String title, String title2) {
        this.imgId = imgId;
        this.title = title;
        this.title2 = title2;

    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
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


}
