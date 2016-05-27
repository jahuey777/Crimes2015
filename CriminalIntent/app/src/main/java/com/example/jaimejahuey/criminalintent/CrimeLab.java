package com.example.jaimejahuey.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.example.jaimejahuey.criminalintent.CrimeDbSchema.CrimeTable;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Created by jaimejahuey on 4/15/16.
 */
public class CrimeLab {

    private  static CrimeLab sCrimeLab;
//    private List<Crime> mCrimes;

    //For database
    private Context mcontext;
    private SQLiteDatabase mDatabase;

    public static CrimeLab get(Context context){
        if(sCrimeLab ==null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context){
        mcontext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mcontext).getWritableDatabase();
//        mCrimes = new ArrayList<>();

//        for(int i=0; i< 100; i++){
//            Crime crime = new Crime();
//            crime.setmTitle("Crime # " + i);
//            crime.setmSolved(i%2==0);
//            mCrimes.add(crime);
//        }
    }
    public List<Crime> getCrimes(){
//        return mCrimes;

        //Making the list by grabbing the crimes for the sql database
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursorWrapper = queryCrimes(null, null);
        try{
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()){
                crimes.add(cursorWrapper.getCrime());
                cursorWrapper.moveToNext();
            }
        }finally {
            cursorWrapper.close();
        }
        return crimes;
    }

    public Crime getCrime(UUID id){
//        for(Crime crime: mCrimes){
//            if(crime.getmId().equals(id)){
//                return crime;
//            }
//        }

        Log.v("Called " , " second");
        CrimeCursorWrapper cursorWrapper = queryCrimes(CrimeTable.Cols.UUID + " = ?", new String[] {id.toString()});

        try{
            if(cursorWrapper.getCount()==0){
                return null;
            }
            cursorWrapper.moveToFirst();
            return cursorWrapper.getCrime();
        }finally {
            cursorWrapper.close();
        }
    }

    public int getPosition(UUID id){
        int positionNotFound = -1;

//        for(int i =0; i< mCrimes.size(); i++) {
//            if(mCrimes.get(i).getmId()== id) {
//                return i;
//            }
//        }
        return positionNotFound;
    }

    public void addCrime(Crime c){
//        mCrimes.add(c);
        ContentValues values = getContentValues(c);
        mDatabase.insert(CrimeTable.NAME, null, values);
    }

    public void deleteCrime(UUID id){

//        for(Iterator<Crime> i = mCrimes.listIterator(); i.hasNext(); ) {
//            Crime crime = i.next();
//            if(crime.getmId()==id) {
//                mCrimes.remove(crime);
//            }
//        }
        mDatabase.delete(CrimeTable.NAME, CrimeTable.Cols.UUID + " =?",new String[]{String.valueOf(id.toString())});
    }

    private static ContentValues getContentValues(Crime crime){
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getmId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getmTitle());
        values.put(CrimeTable.Cols.DATE, crime.getmDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.ismSolved());
        values.put(CrimeTable.Cols.SUSPECT, crime.getmSuspect());

        return values;
    }

    public void updateCrime(Crime crime){
        String uuidString = crime.getmId().toString();
        ContentValues values = getContentValues(crime);

        mDatabase.update(CrimeTable.NAME, values, CrimeTable.Cols.UUID + " = ?", new String[]{uuidString});

    }

//    private Cursor queryCrimes(String whereClause, String[] whereArgs)
    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null, //Columns- null selects all columns
                whereClause,
                whereArgs,
                null, //groupBy
                null, //having
                null //orderby
        );
        return new CrimeCursorWrapper(cursor);
    }

    //Creating location for photo file
    public File getPhotoFile(Crime crime){
        File externalFilesDir = mcontext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        //If no external storgage found
        if(externalFilesDir == null){
            return null;
        }

        return new File(externalFilesDir, crime.getPhotoFileName());
    }
}
