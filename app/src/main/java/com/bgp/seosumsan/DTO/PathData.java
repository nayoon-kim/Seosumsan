package com.bgp.seosumsan.DTO;

import android.graphics.Bitmap;

public class PathData {
    private String member_id;
    private String member_name;
    private Bitmap member_image;
    private String member_start;
    private String member_end;

    public String getMember_id(){return member_id;}
    public void setMember_id(String member_id){this.member_id = member_id; }
    public String getMember_name(){return member_name;}
    public void setMember_name(String member_name){this.member_name = member_name; }
    public Bitmap getMember_image(){return member_image;}
    public void setMember_image(Bitmap member_image){this.member_image = member_image;}
    public String getMember_start(){return member_start;}
    public void setMember_start(String member_start){this.member_start=member_start;}
    public String getMember_end(){return member_end;}
    public void setMember_end(String member_end){this.member_end=member_end;}
}
