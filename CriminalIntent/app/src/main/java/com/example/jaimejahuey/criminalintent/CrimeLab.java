package com.example.jaimejahuey.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Created by jaimejahuey on 4/15/16.
 */
public class CrimeLab {

    private  static CrimeLab sCrimeLab;
    private List<Crime> mCrimes;

    public static CrimeLab get(Context context){
        if(sCrimeLab ==null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context){
        mCrimes = new ArrayList<>();

//        for(int i=0; i< 100; i++){
//            Crime crime = new Crime();
//            crime.setmTitle("Crime # " + i);
//            crime.setmSolved(i%2==0);
//            mCrimes.add(crime);
//        }
    }
    public List<Crime> getCrimes(){
        return mCrimes;
    }

    public Crime getCrime(UUID id){
        for(Crime crime: mCrimes){
            if(crime.getmId().equals(id)){
                return crime;
            }
        }
        return null;
    }

    public int getPosition(UUID id){
        int positionNotFound = -1;

        for(int i =0; i< mCrimes.size(); i++) {
            if(mCrimes.get(i).getmId()== id) {
                return i;
            }
        }
        return positionNotFound;
    }

    public void addCrime(Crime c){
        mCrimes.add(c);
    }

    public void deleteCrime(UUID id){

        for(Iterator<Crime> i = mCrimes.listIterator(); i.hasNext(); ) {
            Crime crime = i.next();
            if(crime.getmId()==id) {
                mCrimes.remove(crime);
            }
        }
    }


}
