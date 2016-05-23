package com.example.jaimejahuey.criminalintent;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import com.example.jaimejahuey.criminalintent.CrimeDbSchema.CrimeTable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by jaimejahuey on 5/18/16.
 */
//We wrap the regular cursor with our own personal wrapper.
    //Has the same functionalities but we can add our own.

public class CrimeCursorWrapper extends CursorWrapper {

    public CrimeCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Crime getCrime(){

        //Getting the values from the database.
        String uuidString = getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));
        String suspect = getString(getColumnIndex(CrimeTable.Cols.SUSPECT));

        //Creating a crime to return.
        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setmTitle(title);
        crime.setmDate(new Date(date));
        crime.setmSolved(isSolved!=0);
        crime.setmSuspect(suspect);

        return crime;
    }
}
