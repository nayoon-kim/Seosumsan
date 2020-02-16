package com.bgp.seosumsan.DTO;

import android.graphics.Bitmap;

public class SightsData {
    private String member_name;
    private Bitmap member_image;

    public String getMember_name() { return member_name;}
    public void setMember_name(String member_name) {this.member_name = member_name;}

    public Bitmap getMember_image() {
        return member_image;
    }

    public void setMember_image(Bitmap member_image) {
        this.member_image = member_image;
    }
}
