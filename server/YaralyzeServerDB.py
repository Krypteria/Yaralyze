import sqlite3
from xmlrpc.client import Boolean

from itsdangerous import json

DATABASE_NAME = ".\\Database\\Yaralyze_server.db"

MALWARE_HASHES = "malware_hashes";
COLUMN_ID_MALWARE_HASHES = "id_hash";
COLUMN_HASH_MALWARE_HASHES = "hash";

COMPLETE_MALWARE_HASHES_PATH = ".\\Analysis_tools\\MalwareHashes\\Complete_hashes.txt"

class YaralyzeServerDB:
    
    def __init__(self) -> None:
        self.__createDB()

    def __createDB(self) -> None:
        self.db = sqlite3.connect(DATABASE_NAME)
        cursor = self.db.cursor()

        malwareHashes = "CREATE TABLE IF NOT EXISTS " + MALWARE_HASHES + " (" + COLUMN_ID_MALWARE_HASHES + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_HASH_MALWARE_HASHES + " TEXT)"
        cursor.execute(malwareHashes)

        if(not self.__hasMalwareHashes(cursor)):
            self.__insertHashes(cursor)

        cursor.close()
        self.db.commit()

    def __hasMalwareHashes(self, cursor) -> Boolean:
        sql = "SELECT count(*) FROM " + MALWARE_HASHES
        cursor.execute(sql)

        return cursor.fetchone()[0] > 0 

    def __insertHashes(self, cursor):
        malwareHashesFile = open(COMPLETE_MALWARE_HASHES_PATH, "r")
        lines = malwareHashesFile.readlines()

        for line in lines:
            cursor.execute("INSERT INTO " + MALWARE_HASHES + " (" + COLUMN_HASH_MALWARE_HASHES + ") VALUES(?)", (line,))


    def getHashCoincidence(self, hash) -> Boolean:
        cursor = self.db.cursor()
        
        sql = "SELECT * FROM " + MALWARE_HASHES + " WHERE " + COLUMN_HASH_MALWARE_HASHES + " = " + "'" + hash + "'"
        cursor.execute(sql)
        
        coincidence = cursor.fetchone() != None 
        cursor.close()

        return coincidence
