package com.ad_imagine.voropecom.fragment_coach;

public class coach_accueil_Item {

    private int imgId;
    private String title;
    private String title2;
    private String title3;

    public coach_accueil_Item(int imgId, String title, String title2, String title3) {
        this.imgId = imgId;
        this.title = title;
        this.title2 = title2;
        this.title3 = title3;
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

    public void setTitle2(String title) {
        this.title2 = title2;
    }
    public String getTitle3() {
        return title3;
    }

    public void setTitle3(String title) {
        this.title3 = title3;
    }
}
