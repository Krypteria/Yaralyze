import yara
import os
import logging

from datetime import datetime

YARA_RULES_PATH = ".\\Analysis_tools\\YaraRules\\"
LOGS_PATH = ".\\Logs\\analyzerLogs.log"

class Analyzer():
    def __init__(self) -> None:
        self.__setupLogConfig()
        self.__rules = self.__loadRules()
        self.__analysisOutcome = {}
        self.__matchedRules = []
        self.__malwareFound = 0
        self.__matchedRulesCount = 0

    def __setupLogConfig(self) -> None:
        if(not os.path.exists(LOGS_PATH)):
            open(LOGS_PATH, 'a').close()

        file = open(LOGS_PATH, "a")
        file.write("--------------------------- \n")
        file.write(str(datetime.now().strftime("%d-%B-%Y \n")))
        file.write("--------------------------- \n")
        file.close()

        handler = logging.FileHandler(LOGS_PATH)        
        handler.setFormatter(logging.Formatter('%(asctime)s: %(levelname)s - %(message)s', "%H:%M:%S"))

        self.__logger = logging.getLogger("analyzerLogger")
        self.__logger.setLevel(logging.DEBUG)
        self.__logger.addHandler(handler)

    def __loadRules(self) -> yara.Rules: 
        try:
            if(not os.path.exists(YARA_RULES_PATH)):
                raise FileNotFoundError("La ruta especificada para las reglas Yara es incorrecta")

            yaraRulesDic = {}
            self.__logger.info("Cargando reglas Yara en Analyzer.py")
            for root, __, files in os.walk(YARA_RULES_PATH):
                for file in files:
                    filePath = os.path.join(root, file)                 
                    yaraRulesDic[file] = filePath
                    self.__logger.info("Regla " + file + " cargada con éxito")

            return yara.compile(filepaths=yaraRulesDic) 
        except FileNotFoundError as e:
            print("[!] - ", str(e))
            self.__logger.error(str(e))
        except yara.YaraSyntaxError as e:
            print("[!] - ", str(e))
            self.__logger.error(str(e))


    def __staticAnalysisCallback(self,data):
        if(data["matches"]):
            self.__logger.warning("El archivo coincide con la regla " + data["rule"])
            self.__matchedRules.append(data["rule"])
            self.__matchedRulesCount = self.__matchedRulesCount + 1
            self.__malwareFound = 1
    
    def cleanAnalysisOutcome(self):
        self.__analysisOutcome.clear()
        self.__matchedRules.clear()
        self.__malwareFound = 0
        self.__matchedRulesCount = 0

    def executeStaticAnalysis(self, clientSamplePath) -> dict:
        self.__logger.info("Ejecutando el análisis del archivo situado en " + clientSamplePath)
        self.__rules.match(filepath=clientSamplePath, callback=self.__staticAnalysisCallback)
        if self.__malwareFound:
            self.__analysisOutcome["detected"] = True
            self.__analysisOutcome["numMatchedRules"] = self.__matchedRulesCount
            self.__analysisOutcome["matchedRules"] = self.__matchedRules
        else:
            self.__analysisOutcome["detected"] = False

        self.__logger.info("Análisis finalizado")

        return self.__analysisOutcome