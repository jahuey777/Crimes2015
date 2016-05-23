package com.example.jaimejahuey.criminalintent;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Created by jaimejahuey on 4/14/16.
 */
public class Crime implements Serializable {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;

    private String mSuspect;

    public Crime(){
        //Generate unique identifier
//        mId = UUID.randomUUID();
//        mDate = new Date();
        //Calls the other constructior
        this(UUID.randomUUID());
    }
    public Crime(UUID id){
        mId = id;
        mDate = new Date();
    }
    public UUID getmId() {
        return mId;
    }

    public void setmId(UUID mId) {
        this.mId = mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String s) {
        this.mTitle = s;
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public boolean ismSolved() {
        return mSolved;
    }

    public void setmSolved(boolean mSolved) {
        this.mSolved = mSolved;
    }

    public String getmSuspect() {
        return mSuspect;
    }

    public void setmSuspect(String mSuspect) {
        this.mSuspect = mSuspect;
    }
}
