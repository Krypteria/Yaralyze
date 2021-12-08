package com.example.yaralyze01;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.yaralyze01.ui.analysis.outcomes.AnalysisOutcome;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class YaralyzeDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Yaralyze.db";
    private static final int DATABASE_VERSION = 1;

    private static final String MALWARE_HASHES = "malware_hashes";
    private static final String COLUMN_ID_MALWARE_HASHES = "id_hash";
    private static final String COLUMN_HASH_MALWARE_HASHES = "hash";

    private static final String ANALYSIS_OUTCOMES = "analysis_outcomes";
    private static final String COLUMN_ID_OUTCOME_ANALYSIS_OUTCOMES = "id_outcome";
    private static final String COLUMN_ID_APP_ANALYSIS_OUTCOMES = "id_app";
    private static final String COLUMN_ANALYSIS_TYPE_ANALYSIS_OUTCOMES = "analysis_type";
    private static final String COLUMN_MALWARE_DETECTED_ANALYSIS_OUTCOMES = "malware_detected";

    private static final String COINCIDENCES = "coincidences";
    private static final String COLUMN_ID_OUTCOME_COINCIDENCES = "id_outcome";
    private static final String COLUMN_ID_COINCIDENCE_COINCIDENCES = "id_coincidence";
    private static final String COLUMN_MATCHED_RULE_COINCIDENCES = "matched_rule";

    private static final String LAST_OUTCOMES_DATE = "last_outcomes_date";
    private static final String COLUMN_ID_OUTCOME_LAST_OUTCOMES_DATE = "id_analysis_outcome";
    private static final String COLUMN_ANALYSIS_DATE_LAST_OUTCOMES_DATE = "analysis_date";

    private static final String LAST_ANALYZED_APPS = "last_analyzed_apps";
    private static final String COLUMN_ID_APP_LAST_ANALYZED_APPS = "id_app";
    private static final String COLUMN_APP_NAME_LAST_ANALYZED_APPS = "app_name";

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

    //Se llama la primera vez que se requiere la base de datos
    @Override
    public void onCreate(SQLiteDatabase db) {
        String malwareHashes = "CREATE TABLE " + MALWARE_HASHES +
                "(" + COLUMN_ID_MALWARE_HASHES + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_HASH_MALWARE_HASHES + " TEXT UNIQUE NOT NULL);";

        String analysisOutcomes = "CREATE TABLE " + ANALYSIS_OUTCOMES +
                "(" + COLUMN_ID_OUTCOME_ANALYSIS_OUTCOMES + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ID_APP_ANALYSIS_OUTCOMES + " INTEGER NOT NULL, " +
                COLUMN_ANALYSIS_TYPE_ANALYSIS_OUTCOMES + " TEXT NOT NULL, " +
                COLUMN_MALWARE_DETECTED_ANALYSIS_OUTCOMES + " INTEGER NOT NULL," +
                "FOREIGN KEY (" + COLUMN_ID_APP_ANALYSIS_OUTCOMES + ") REFERENCES " + LAST_ANALYZED_APPS +
                "(" + COLUMN_ID_APP_LAST_ANALYZED_APPS + "));";

        String coincidences = "CREATE TABLE " + COINCIDENCES +
                "(" + COLUMN_ID_COINCIDENCE_COINCIDENCES + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ID_OUTCOME_COINCIDENCES + " INTEGER NOT NULL, " +
                COLUMN_MATCHED_RULE_COINCIDENCES + " TEXT NOT NULL," +
                "FOREIGN KEY (" + COLUMN_ID_OUTCOME_COINCIDENCES + ") REFERENCES " + ANALYSIS_OUTCOMES +
                "(" + COLUMN_ID_OUTCOME_ANALYSIS_OUTCOMES + "));";

        String lastOutcomesDate = "CREATE TABLE " + LAST_OUTCOMES_DATE +
                "(" + COLUMN_ID_OUTCOME_LAST_OUTCOMES_DATE + " INTEGER PRIMARY KEY, " +
                COLUMN_ANALYSIS_DATE_LAST_OUTCOMES_DATE + " DATETIME NOT NULL," +
                "FOREIGN KEY (" + COLUMN_ID_OUTCOME_LAST_OUTCOMES_DATE + ") REFERENCES " + ANALYSIS_OUTCOMES +
                "(" + COLUMN_ID_OUTCOME_ANALYSIS_OUTCOMES + "));";

        String lastAnalyzedApps = "CREATE TABLE " + LAST_ANALYZED_APPS +
                "(" + COLUMN_ID_APP_LAST_ANALYZED_APPS + " INTEGER PRIMARY KEY, " +
                COLUMN_APP_NAME_LAST_ANALYZED_APPS + " TEXT NOT NULL," +
                "FOREIGN KEY (" + COLUMN_ID_APP_LAST_ANALYZED_APPS + ") REFERENCES " + ANALYSIS_OUTCOMES +
                "(" + COLUMN_ID_APP_ANALYSIS_OUTCOMES + "));";

        System.out.println("CARGO LA BASE DE DATOS");
        db.execSQL(malwareHashes);
        db.execSQL(analysisOutcomes);
        db.execSQL(coincidences);
        db.execSQL(lastOutcomesDate);
        db.execSQL(lastAnalyzedApps);
        System.out.println("DB CARGADA");
    }

    //Se llama a este método cuando la versión de la base de datos cambia
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MALWARE_HASHES);
        db.execSQL("DROP TABLE IF EXISTS " + COINCIDENCES);
        db.execSQL("DROP TABLE IF EXISTS " + LAST_OUTCOMES_DATE);
        db.execSQL("DROP TABLE IF EXISTS " + ANALYSIS_OUTCOMES);
        db.execSQL("DROP TABLE IF EXISTS " + LAST_ANALYZED_APPS);
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

    public AnalysisOutcome getCoincidence(String appName, String hash){
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM " + MALWARE_HASHES + " WHERE " + COLUMN_HASH_MALWARE_HASHES +
                        " = " + "'" + hash + "'";
        Cursor cursor = db.rawQuery(sql, null);

        AnalysisOutcome analysisOutcome;
        if(cursor.moveToFirst()){
            cursor.close();
            analysisOutcome = new AnalysisOutcome(2, appName, true, null);
        }
        else{
            cursor.close();
            analysisOutcome = new AnalysisOutcome(2, appName, false, null);
        }

        return analysisOutcome;
    }

    public boolean insertAnalysisOutcome(AnalysisOutcome analysisOutcome){
        SQLiteDatabase db = this.getWritableDatabase();

        //Inserto la app analizada si no estuviese en la base de datos y obtengo su id
        int app_id = insertLastAnalyzedApp(analysisOutcome.getAnalyzedAppName());

        //Inserto un nuevo analysis_outcome
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID_APP_ANALYSIS_OUTCOMES, app_id);
        cv.put(COLUMN_ANALYSIS_TYPE_ANALYSIS_OUTCOMES, analysisOutcome.getAnalysisType());
        cv.put(COLUMN_MALWARE_DETECTED_ANALYSIS_OUTCOMES, analysisOutcome.isMalwareDetected());

        long insert = db.insert(ANALYSIS_OUTCOMES, null, cv);

        if(insert == -1){
            return false;
        }

        //Obtengo el id_outcome de la fila insertada e inserto su información asociada
        int id_outcome = this.getLastIndexFrom(ANALYSIS_OUTCOMES, COLUMN_ID_OUTCOME_ANALYSIS_OUTCOMES);

        if(analysisOutcome.getMatchedRules() != null){
            for(String rule: analysisOutcome.getMatchedRules()){
                cv.clear();
                cv.put(COLUMN_ID_OUTCOME_COINCIDENCES, id_outcome);
                cv.put(COLUMN_MATCHED_RULE_COINCIDENCES, rule);
                db.insert(COINCIDENCES, null, cv);
            }
        }

        //Inserto la fecha del analisis
        this.insertLastAnalysisDate(id_outcome, analysisOutcome.getAnalysisDate());

        return true;
    }

    private int insertLastAnalyzedApp(String appName){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_APP_NAME_LAST_ANALYZED_APPS, appName);

        long insert = db.insert(LAST_ANALYZED_APPS, null, cv);

        if(insert == -1){
            return -1;
        }

        return this.getLastIndexFrom(LAST_ANALYZED_APPS, COLUMN_ID_APP_LAST_ANALYZED_APPS);
    }

    private boolean insertLastAnalysisDate(int outcome_id, String dateTime){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(LAST_OUTCOMES_DATE, null, null);

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID_OUTCOME_LAST_OUTCOMES_DATE, outcome_id);
        cv.put(COLUMN_ANALYSIS_DATE_LAST_OUTCOMES_DATE, dateTime);

        long insert = db.insert(LAST_OUTCOMES_DATE, null, cv);

        if(insert == -1){
            return false;
        }

        return true;
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
