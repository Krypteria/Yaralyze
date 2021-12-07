package com.example.yaralyze01;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

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
    }

    //Se llama la primera vez que se requiere la base de datos
    @Override
    public void onCreate(SQLiteDatabase db) {
        String malwareHashes = "CREATE TABLE " + MALWARE_HASHES +
                "(" + COLUMN_ID_MALWARE_HASHES + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_HASH_MALWARE_HASHES + " TEXT NOT NULL UNIQUE);";

        /*String analysisOutcomes = "CREATE TABLE " + ANALYSIS_OUTCOMES +
                "(" + COLUMN_ID_OUTCOME_ANALYSIS_OUTCOMES + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ID_APP_ANALYSIS_OUTCOMES + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ANALYSIS_TYPE_ANALYSIS_OUTCOMES + "TEXT NOT NULL, " +
                COLUMN_MALWARE_DETECTED_ANALYSIS_OUTCOMES + "INTEGER NOT NULL);";

        String coincidences = "CREATE TABLE " + COINCIDENCES +
                "(" + COLUMN_ID_COINCIDENCE_COINCIDENCES + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ID_OUTCOME_COINCIDENCES + "INTEGER PRIMARY KEY, " +
                COLUMN_MATCHED_RULE_COINCIDENCES + "TEXT NOT NULL," +
                "FOREIGN KEY (" + COLUMN_ID_OUTCOME_COINCIDENCES + ") REFERENCES " + ANALYSIS_OUTCOMES +
                "(" + COLUMN_ID_OUTCOME_ANALYSIS_OUTCOMES + "));";

        String lastOutcomesDate = "CREATE TABLE " + LAST_OUTCOMES_DATE +
                "(" + COLUMN_ID_OUTCOME_LAST_OUTCOMES_DATE + "INTEGER PRIMARY KEY, " +
                COLUMN_ANALYSIS_DATE_LAST_OUTCOMES_DATE + "DATETIME NOT NULL," +
                "FOREIGN KEY (" + COLUMN_ID_OUTCOME_LAST_OUTCOMES_DATE + ") REFERENCES " + ANALYSIS_OUTCOMES +
                "(" + COLUMN_ID_OUTCOME_ANALYSIS_OUTCOMES + "));";

        String lastAnalyzedApps = "CREATE TABLE " + LAST_ANALYZED_APPS +
                "(" + COLUMN_ID_APP_LAST_ANALYZED_APPS + "INTEGER PRIMARY KEY, " +
                COLUMN_APP_NAME_LAST_ANALYZED_APPS + "TEXT NOT NULL," +
                "FOREIGN KEY (" + COLUMN_ID_APP_LAST_ANALYZED_APPS + ") REFERENCES " + ANALYSIS_OUTCOMES +
                "(" + COLUMN_ID_APP_ANALYSIS_OUTCOMES + "));";*/

        db.execSQL(malwareHashes);
        /*db.execSQL(analysisOutcomes);
        db.execSQL(coincidences);
        db.execSQL(lastOutcomesDate);
        db.execSQL(lastAnalyzedApps);*/
    }

    //Se llama a este método cuando la versión de la base de datos cambia
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertHash(String hash){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_HASH_MALWARE_HASHES, hash);

        long insert = db.insert(MALWARE_HASHES, null, cv);
        if(insert == -1){
            return false;
        }

        return true;
    }
}
