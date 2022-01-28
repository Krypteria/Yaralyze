package com.example.yaralyze01;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.yaralyze01.ui.analysis.reports.AnalysisReport;

public class YaralyzeDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Yaralyze.db";
    private static final int DATABASE_VERSION = 1;

    private static final String MALWARE_HASHES = "malware_hashes";
    private static final String COLUMN_ID_MALWARE_HASHES = "id_hash";
    private static final String COLUMN_HASH_MALWARE_HASHES = "hash";

    private static final String ANALYSIS_REPORTS = "analysis_reports";
    private static final String COLUMN_ID_REPORT_ANALYSIS_REPORTS = "id_report";
    private static final String COLUMN_ID_APP_ANALYSIS_REPORTS = "id_app";
    private static final String COLUMN_ANALYSIS_TYPE_ANALYSIS_REPORTS = "analysis_type";
    private static final String COLUMN_MALWARE_DETECTED_ANALYSIS_REPORTS = "malware_detected";
    private static final String COLUMN_DATE_ANALYSIS_REPORTS = "analysis_date";
    
    private static final String COINCIDENCES = "coincidences";
    private static final String COLUMN_ID_REPORT_COINCIDENCES = "id_report";
    private static final String COLUMN_ID_COINCIDENCE_COINCIDENCES = "id_coincidence";
    private static final String COLUMN_MATCHED_RULE_COINCIDENCES = "matched_rule";

    private static final String ANALYZED_APPS = "analyzed_apps";
    private static final String COLUMN_ID_APP_ANALYZED_APPS = "id_app";
    private static final String COLUMN_APP_NAME_ANALYZED_APPS = "app_name";


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
                COLUMN_APP_NAME_ANALYZED_APPS + " TEXT NOT NULL);";


        String analysisReports = "CREATE TABLE " + ANALYSIS_REPORTS +
                "(" + COLUMN_ID_REPORT_ANALYSIS_REPORTS + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                COLUMN_ID_APP_ANALYSIS_REPORTS + " INTEGER NOT NULL, " +
                COLUMN_ANALYSIS_TYPE_ANALYSIS_REPORTS + " TEXT NOT NULL, " +
                COLUMN_MALWARE_DETECTED_ANALYSIS_REPORTS + " INTEGER NOT NULL," +
                COLUMN_DATE_ANALYSIS_REPORTS + " DATETIME NOT NULL," +
                "UNIQUE (" + COLUMN_ID_REPORT_ANALYSIS_REPORTS + "," + COLUMN_ID_APP_ANALYSIS_REPORTS + ")," +
                "FOREIGN KEY (" + COLUMN_ID_APP_ANALYSIS_REPORTS + ") REFERENCES " + ANALYZED_APPS +
                "(" + COLUMN_ID_APP_ANALYZED_APPS + "));";

        String coincidences = "CREATE TABLE " + COINCIDENCES +
                "(" + COLUMN_ID_COINCIDENCE_COINCIDENCES + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ID_REPORT_COINCIDENCES + " INTEGER NOT NULL, " +
                COLUMN_MATCHED_RULE_COINCIDENCES + " TEXT NOT NULL," +
                "UNIQUE (" + COLUMN_ID_COINCIDENCE_COINCIDENCES + "," + COLUMN_ID_REPORT_COINCIDENCES + ")," +
                "FOREIGN KEY (" + COLUMN_ID_REPORT_COINCIDENCES + ") REFERENCES " + ANALYSIS_REPORTS +
                "(" + COLUMN_ID_REPORT_ANALYSIS_REPORTS + "));";


        db.execSQL(malwareHashes);
        db.execSQL(analyzedApps);
        db.execSQL(analysisReports);
        db.execSQL(coincidences);
    }

    //Se llama a este método cuando la versión de la base de datos cambia
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MALWARE_HASHES);
        db.execSQL("DROP TABLE IF EXISTS " + COINCIDENCES);
        db.execSQL("DROP TABLE IF EXISTS " + ANALYSIS_REPORTS);
        db.execSQL("DROP TABLE IF EXISTS " + ANALYZED_APPS);
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

    public AnalysisReport getCoincidence(String appName, String hash){
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM " + MALWARE_HASHES + " WHERE " + COLUMN_HASH_MALWARE_HASHES +
                        " = " + "'" + hash + "'";
        Cursor cursor = db.rawQuery(sql, null);

        AnalysisReport AnalysisReport;

        if(cursor.moveToFirst()){
            cursor.close();
            AnalysisReport = new AnalysisReport(2, appName, true, null);
        }
        else{
            cursor.close();
            AnalysisReport = new AnalysisReport(2, appName, false, null);
        }

        return AnalysisReport;
    }

    public boolean insertAnalysisReport(AnalysisReport AnalysisReport){
        SQLiteDatabase db = this.getWritableDatabase();

        //Inserto la app analizada si no estuviese en la base de datos y obtengo su id
        int app_id = this.insertAnalyzedApp(AnalysisReport.getAnalyzedAppName());

        if(app_id != -1){
            //Inserto un nuevo analysis_report
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_ID_APP_ANALYSIS_REPORTS, app_id);
            cv.put(COLUMN_ANALYSIS_TYPE_ANALYSIS_REPORTS, AnalysisReport.getAnalysisType());
            cv.put(COLUMN_MALWARE_DETECTED_ANALYSIS_REPORTS, AnalysisReport.isMalwareDetected());
            cv.put(COLUMN_DATE_ANALYSIS_REPORTS, AnalysisReport.getAnalysisDate());

            long insert = db.insert(ANALYSIS_REPORTS, null, cv);

            if(insert != -1){
                //Obtengo el id_report de la fila insertada e inserto su información asociada
                int id_report = this.getLastIndexFrom(ANALYSIS_REPORTS, COLUMN_ID_REPORT_ANALYSIS_REPORTS);

                if(AnalysisReport.getMatchedRules() != null){
                    for(String rule: AnalysisReport.getMatchedRules()){
                        cv.clear();
                        cv.put(COLUMN_ID_REPORT_COINCIDENCES, id_report);
                        cv.put(COLUMN_MATCHED_RULE_COINCIDENCES, rule);
                        db.insert(COINCIDENCES, null, cv);
                    }
                }

                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    private int insertAnalyzedApp(String appName){
        Cursor cursor = this.getIndexFrom(ANALYZED_APPS, COLUMN_APP_NAME_ANALYZED_APPS, appName);
        if(cursor == null){
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(COLUMN_APP_NAME_ANALYZED_APPS, appName);

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

        String sql = "SELECT " + COLUMN_DATE_ANALYSIS_REPORTS + " FROM " + ANALYSIS_REPORTS + " WHERE " +
                        COLUMN_ID_APP_ANALYSIS_REPORTS + " = " + this.getLastIndexFrom(ANALYSIS_REPORTS, COLUMN_ID_APP_ANALYSIS_REPORTS);

        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            return cursor.getString(0);
        }
        else{
            return null;
        }
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
