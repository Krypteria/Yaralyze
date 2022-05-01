import yara
import os
import logging

from datetime import datetime

YARA_RULES_PATH = ".\\Analysis_tools\\YaraRules\\"
LOGS_PATH = ".\\Logs\\"

class Analyzer():
    def __init__(self) -> None:
        self.__setupLogConfig()
        self.__rules = self.__loadRules()
        self.__analysisOutcome = {}
        self.__matchedRules = []
        self.__malwareFound = 0
        self.__matchedRulesCount = 0

    def __setupLogConfig(self) -> None:
        logfilePath = LOGS_PATH + str(datetime.now().strftime("%d")) + "_analyzer.log"
        if(not os.path.exists(logfilePath)):
            open(logfilePath, 'a').close()

        handler = logging.FileHandler(logfilePath)        
        handler.setFormatter(logging.Formatter('%(asctime)s: %(levelname)s - %(message)s', "%H:%M:%S"))

        self.__logger = logging.getLogger("analyzerLogger")
        self.__logger.setLevel(logging.DEBUG)
        self.__logger.addHandler(handler)

    def __loadRules(self) -> yara.Rules: 
        try:
            if(not os.path.exists(YARA_RULES_PATH)):
                raise FileNotFoundError("La ruta especificada para las reglas Yara es incorrecta")

            yaraRulesDic = {}
            self.__logger.info("[!] - Cargando reglas Yara")
            for root, __, files in os.walk(YARA_RULES_PATH):
                for file in files:
                    filePath = os.path.join(root, file)                 
                    yaraRulesDic[file] = filePath
                    self.__logger.info("Regla " + file + " cargada")

            return yara.compile(filepaths=yaraRulesDic) 
        except FileNotFoundError as e:
            self.__logger.error("[!][!][!] - Error al cargar reglas Yara: " + str(e))
        except yara.YaraSyntaxError as e:
            self.__logger.error("[!][!][!] - Error al cargar reglas Yara: " + str(e))


    def __staticAnalysisCallback(self,data):
        if(data["matches"]):
            self.__logger.warning("[!][!] - El archivo coincide con la regla " + data["rule"])
            self.__matchedRules.append(data["rule"])
            self.__matchedRulesCount = self.__matchedRulesCount + 1
            self.__malwareFound = 1
    
    def cleanAnalysisOutcome(self):
        self.__analysisOutcome.clear()
        self.__matchedRules.clear()
        self.__malwareFound = 0
        self.__matchedRulesCount = 0

    def executeStaticAnalysis(self, clientSamplePath) -> dict:
        self.__logger.info("[!] - Ejecutando el análisis del archivo " + clientSamplePath)
        self.__rules.match(filepath=clientSamplePath, callback=self.__staticAnalysisCallback)
        if self.__malwareFound:
            self.__analysisOutcome["detected"] = True
            self.__analysisOutcome["numMatchedRules"] = self.__matchedRulesCount
            self.__analysisOutcome["matchedRules"] = self.__matchedRules
        else:
            self.__analysisOutcome["detected"] = False
        
        self.__logger.info("[!] - Análisis del archivo " + clientSamplePath + " terminado")
        return self.__analysisOutcome