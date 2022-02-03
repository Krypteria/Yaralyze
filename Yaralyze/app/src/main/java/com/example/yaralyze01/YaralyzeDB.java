package com.example.yaralyze01;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;
import android.util.Pair;

import androidx.annotation.Nullable;

import com.example.yaralyze01.ui.analysis.outcomes.AnalysisOutcome;
import com.example.yaralyze01.ui.common.AnalysisType;

import java.util.ArrayList;

public class YaralyzeDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Yaralyze.db";
    private static final int DATABASE_VERSION = 1;

    private static final String MALWARE_HASHES = "malware_hashes";
    private static final String COLUMN_ID_MALWARE_HASHES = "id_hash";
    private static final String COLUMN_HASH_MALWARE_HASHES = "hash";

    private static final String ANALYSIS_OUTCOME = "analysis_outcome";
    private static final String COLUMN_ID_OUTCOME_ANALYSIS_OUTCOME = "id_outcome";
    private static final String COLUMN_ID_APP_ANALYSIS_OUTCOME = "id_app";
    private static final String COLUMN_ANALYSIS_TYPE_ANALYSIS_OUTCOME = "analysis_type";
    private static final String COLUMN_MALWARE_DETECTED_ANALYSIS_OUTCOME = "malware_detected";
    private static final String COLUMN_DATE_ANALYSIS_OUTCOME = "analysis_date";
    
    private static final String COINCIDENCES = "coincidences";
    private static final String COLUMN_ID_OUTCOME_COINCIDENCES = "id_outcome";
    private static final String COLUMN_ID_COINCIDENCE_COINCIDENCES = "id_coincidence";
    private static final String COLUMN_MATCHED_RULE_COINCIDENCES = "matched_rule";

    private static final String ANALYZED_APPS = "analyzed_apps";
    private static final String COLUMN_ID_APP_ANALYZED_APPS = "id_app";
    private static final String COLUMN_APP_NAME_ANALYZED_APPS = "app_name";
    private static final String COLUMN_APP_PACKAGE_ANALYZED_APPS = "app_package";

    private static final String COMPLETE_ANALYSIS = "complete_analysis";
    private static final String COLUMN_ID_COMPLETE_ANALYSIS = "id_complete_analysis";
    private static final String COLUMN_ID_STATIC_ANALYSIS = "id_static_analysis";
    private static final String COLUMN_ID_HASH_ANALYSIS = "id_hash_analysis";

    private static YaralyzeDB dbInstance;
    private Context context;

    public static YaralyzeDB getInstance(Context context){
        if(dbInstance == null){
            dbInstance = new YaralyzeDB(context.getApplicationContext());
        }

        return dbInstance;
    }

    private YaralyzeDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String malwareHashes = "CREATE TABLE " + MALWARE_HASHES +
                "(" + COLUMN_ID_MALWARE_HASHES + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_HASH_MALWARE_HASHES + " TEXT UNIQUE NOT NULL);";

        String analyzedApps = "CREATE TABLE " + ANALYZED_APPS +
                "(" + COLUMN_ID_APP_ANALYZED_APPS + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_APP_NAME_ANALYZED_APPS + " TEXT NOT NULL," +
                COLUMN_APP_PACKAGE_ANALYZED_APPS + " TEXT NOT NULL);";


        String AnalysisOutcomes = "CREATE TABLE " + ANALYSIS_OUTCOME +
                "(" + COLUMN_ID_OUTCOME_ANALYSIS_OUTCOME + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                COLUMN_ID_APP_ANALYSIS_OUTCOME + " INTEGER NOT NULL, " +
                COLUMN_ANALYSIS_TYPE_ANALYSIS_OUTCOME + " TEXT NOT NULL, " +
                COLUMN_MALWARE_DETECTED_ANALYSIS_OUTCOME + " INTEGER NOT NULL," +
                COLUMN_DATE_ANALYSIS_OUTCOME + " DATETIME NOT NULL," +
                "FOREIGN KEY (" + COLUMN_ID_APP_ANALYSIS_OUTCOME + ") REFERENCES " + ANALYZED_APPS +
                "(" + COLUMN_ID_APP_ANALYZED_APPS + "));";

        String coincidences = "CREATE TABLE " + COINCIDENCES +
                "(" + COLUMN_ID_COINCIDENCE_COINCIDENCES + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ID_OUTCOME_COINCIDENCES + " INTEGER NOT NULL, " +
                COLUMN_MATCHED_RULE_COINCIDENCES + " TEXT NOT NULL," +
                "FOREIGN KEY (" + COLUMN_ID_OUTCOME_COINCIDENCES + ") REFERENCES " + ANALYSIS_OUTCOME +
                "(" + COLUMN_ID_OUTCOME_ANALYSIS_OUTCOME + "));";

        String completeAnalysis = "CREATE TABLE " + COMPLETE_ANALYSIS +
                "(" + COLUMN_ID_COMPLETE_ANALYSIS + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ID_STATIC_ANALYSIS + " INTEGER NOT NULL, " +
                COLUMN_ID_HASH_ANALYSIS + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + COLUMN_ID_STATIC_ANALYSIS + ") REFERENCES " + ANALYSIS_OUTCOME +
                "(" + COLUMN_ID_OUTCOME_ANALYSIS_OUTCOME + ")," +
                "FOREIGN KEY (" + COLUMN_ID_HASH_ANALYSIS + ") REFERENCES " + ANALYSIS_OUTCOME +
                "(" + COLUMN_ID_OUTCOME_ANALYSIS_OUTCOME + "));";


        db.execSQL(malwareHashes);
        db.execSQL(analyzedApps);
        db.execSQL(AnalysisOutcomes);
        db.execSQL(completeAnalysis);
        db.execSQL(coincidences);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MALWARE_HASHES);
        db.execSQL("DROP TABLE IF EXISTS " + COINCIDENCES);
        db.execSQL("DROP TABLE IF EXISTS " + ANALYSIS_OUTCOME);
        db.execSQL("DROP TABLE IF EXISTS " + ANALYZED_APPS);
        db.execSQL("DROP TABLE IF EXISTS " + COMPLETE_ANALYSIS);
        onCreate(db);
    }

    public void deleteDB(){
        this.context.deleteDatabase(DATABASE_NAME);
    }

    public boolean insertHash(String hash){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_HASH_MALWARE_HASHES, hash);

        long insert = db.replace(MALWARE_HASHES, null, cv);

        if(insert == -1){
            return false;
        }

        return true;
    }

    public AnalysisOutcome getCoincidence(String appName, String packageName, String hash){
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM " + MALWARE_HASHES + " WHERE " + COLUMN_HASH_MALWARE_HASHES +
                        " = " + "'" + hash + "'";
        Cursor cursor = db.rawQuery(sql, null);

        AnalysisOutcome AnalysisOutcome;

        if(cursor.moveToFirst()){
            cursor.close();
            AnalysisOutcome = new AnalysisOutcome(AnalysisType.HASH, null, appName, packageName,true, null, null);
        }
        else{
            cursor.close();
            AnalysisOutcome = new AnalysisOutcome(AnalysisType.HASH, null, appName, packageName,false, null, null);
        }

        return AnalysisOutcome;
    }

    public void insertAnalysisOutcome(AnalysisOutcome AnalysisOutcome){
        SQLiteDatabase db = this.getWritableDatabase();

        //Inserto la app analizada si no estuviese en la base de datos y obtengo su id
        int app_id = this.insertAnalyzedApp(AnalysisOutcome.getAnalyzedAppName(), AnalysisOutcome.getAnalyzedAppPackage());

        if(app_id != -1){
            //Inserto un nuevo analysis_outcome
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_ID_APP_ANALYSIS_OUTCOME, app_id);
            cv.put(COLUMN_ANALYSIS_TYPE_ANALYSIS_OUTCOME, AnalysisOutcome.getAnalysisType());
            cv.put(COLUMN_MALWARE_DETECTED_ANALYSIS_OUTCOME, AnalysisOutcome.isMalwareDetected());
            cv.put(COLUMN_DATE_ANALYSIS_OUTCOME, AnalysisOutcome.getAnalysisDate());

            long insert = db.insert(ANALYSIS_OUTCOME, null, cv);

            if(insert != -1){
                //Obtengo el id_outcome de la fila insertada e inserto su información asociada
                int id_outcome = this.getLastIndexFrom(ANALYSIS_OUTCOME, COLUMN_ID_OUTCOME_ANALYSIS_OUTCOME);

                if(AnalysisOutcome.getMatchedRules() != null){
                    for(String rule: AnalysisOutcome.getMatchedRules()){
                        cv.clear();
                        cv.put(COLUMN_ID_OUTCOME_COINCIDENCES, id_outcome);
                        cv.put(COLUMN_MATCHED_RULE_COINCIDENCES, rule);
                        db.insert(COINCIDENCES, null, cv);
                    }
                }
            }
        }
    }

    public void insertCompleteAnalysisOutcome(AnalysisOutcome staticAnalysisOutcome, AnalysisOutcome hashAnalysisOutcome){
        this.insertAnalysisOutcome(staticAnalysisOutcome);
        int idStaticAnalysis = this.getLastIndexFrom(ANALYSIS_OUTCOME, COLUMN_ID_OUTCOME_ANALYSIS_OUTCOME);
        this.insertAnalysisOutcome(hashAnalysisOutcome);
        int idHashAnalysis = this.getLastIndexFrom(ANALYSIS_OUTCOME, COLUMN_ID_OUTCOME_ANALYSIS_OUTCOME);

        SQLiteDatabase db = this.getWritableDatabase();

        //Inserto un nuevo analysis_outcome
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID_STATIC_ANALYSIS, idStaticAnalysis);
        cv.put(COLUMN_ID_HASH_ANALYSIS, idHashAnalysis);

        db.insert(COMPLETE_ANALYSIS, null, cv);
    }

    private int insertAnalyzedApp(String appName, String appPackage){
        Cursor cursor = this.getIndexFrom(ANALYZED_APPS, COLUMN_APP_PACKAGE_ANALYZED_APPS, appPackage);
        if(cursor == null){
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(COLUMN_APP_NAME_ANALYZED_APPS, appName);
            cv.put(COLUMN_APP_PACKAGE_ANALYZED_APPS, appPackage);

            long insert = db.insert(ANALYZED_APPS, null, cv);
            if(insert == -1){
                return -1;
            }
            return getLastIndexFrom(ANALYZED_APPS, COLUMN_ID_APP_ANALYZED_APPS);
        }
        else{
            int index = cursor.getInt(0);
            cursor.close();
            return index;
        }
    }

    public String getLastAnalysisDate(){
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT " + COLUMN_DATE_ANALYSIS_OUTCOME + " FROM " + ANALYSIS_OUTCOME + " WHERE " +
                        COLUMN_ID_APP_ANALYSIS_OUTCOME + " = " + this.getLastIndexFrom(ANALYSIS_OUTCOME, COLUMN_ID_APP_ANALYSIS_OUTCOME);

        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            return cursor.getString(0);
        }
        else{
            return null;
        }
    }

    private String getAppName(int appId){
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT " + COLUMN_APP_NAME_ANALYZED_APPS + " FROM " + ANALYZED_APPS + " WHERE " + COLUMN_ID_APP_ANALYZED_APPS + " = " + appId;
        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            return cursor.getString(0);
        }
        else{
            return null;
        }
    }

    private ArrayList<String> getMatchedRules(int idOutcome){
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT " + COLUMN_MATCHED_RULE_COINCIDENCES + " FROM " + COINCIDENCES + " WHERE " + COLUMN_ID_OUTCOME_COINCIDENCES + " = " + idOutcome;
        Cursor cursor = db.rawQuery(sql, null);

        ArrayList<String> matchedRules = new ArrayList<>();

        while(cursor.moveToNext()){
            matchedRules.add(cursor.getString(0));
        }


        return matchedRules;
    }

    public ArrayList<AnalysisOutcome> getReports(int reportType){
        ArrayList<AnalysisOutcome> analysisOutcomes = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM " + ANALYSIS_OUTCOME + " WHERE " + COLUMN_ANALYSIS_TYPE_ANALYSIS_OUTCOME + " = " + reportType +
                        " ORDER BY " + COLUMN_DATE_ANALYSIS_OUTCOME + " DESC";

        Cursor cursor = db.rawQuery(sql, null);

        while(cursor.moveToNext()){
            sql = "SELECT * FROM " + ANALYZED_APPS + " WHERE " + COLUMN_ID_APP_ANALYZED_APPS + " = " + cursor.getInt(1);
            Cursor cursorAnalyzedApps = db.rawQuery(sql, null);

            if(cursorAnalyzedApps.moveToFirst()){
                Drawable icon = null;
                try {
                    icon = this.context.getPackageManager().getApplicationIcon(cursorAnalyzedApps.getString(2));
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                ArrayList<String> matchedRules = null;
                switch (reportType){
                    case AnalysisType.STATIC:
                        matchedRules = this.getMatchedRules(cursor.getInt(0));
                        break;
                    case AnalysisType.COMPLETE:
                        break;
                    default:
                        break;
                }
                AnalysisOutcome analysisOutcome = new AnalysisOutcome(reportType, icon, getAppName(cursor.getInt(1)), cursorAnalyzedApps.getString(2),
                                                            cursor.getInt(3) == 1, cursor.getString(4), matchedRules);
                analysisOutcomes.add(analysisOutcome);
            }
        }

        return analysisOutcomes;
    }

    public ArrayList<Pair<String, Drawable>> getLastAnalyzedAppsIcons(){
        ArrayList<Pair<String, Drawable>> lastAnalyzedAppsIcons = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT DISTINCT * FROM " + ANALYZED_APPS + " ORDER BY " + COLUMN_ID_APP_ANALYZED_APPS + " DESC LIMIT 6";
        Cursor cursor = db.rawQuery(sql, null);

        while(cursor.moveToNext()){
            try {
                Drawable icon = this.context.getPackageManager().getApplicationIcon(cursor.getString(2));
                lastAnalyzedAppsIcons.add(new Pair<String, Drawable>(cursor.getString(1), icon));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        return lastAnalyzedAppsIcons;
    }

    public boolean hasMalwareHashes(){
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT COUNT(*) FROM " + MALWARE_HASHES;
        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.moveToFirst() && cursor.getInt(0) > 0){
            return true;
        }
        else{
            return false;
        }
    }

    private Cursor getIndexFrom(String tableName, String idColumnName, String item){
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM " + tableName + " WHERE " + idColumnName + " = " + "'" + item + "'";
        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            return cursor;
        }
        else{
            return null;
        }
    }

    private int getLastIndexFrom(String tableName, String idColumnName){
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT MAX(" + idColumnName + ") FROM " + tableName;
        Cursor cursor = db.rawQuery(sql, null);

        int index;
        if(cursor.moveToFirst()){
            index = cursor.getInt(0);
        }
        else{
            index = -1;
        }

        cursor.close();
        return index;
    }
}
