import yara
import os

YARA_RULES_PATH = ".\\AnalysisSamples\\YaraRules\\"

class Analyzer():
    def __init__(self) -> None:
        self.__rules = self.__loadRules()
        self.__analysisOutcome = {}
        self.__matchedRules = []
        self.__malwareFound = 0
        self.__matchedRulesCount = 0

    def __loadRules(self) -> yara.Rules:
        if(not os.path.exists(YARA_RULES_PATH)):
            raise Exception("No existe la carpeta reglas Yara")
        
        try:
            yaraRulesDic = {}
            for root, __, files in os.walk(YARA_RULES_PATH):
                for file in files:
                    filePath = os.path.join(root, file)                 
                    yaraRulesDic[file] = filePath
                    print(filePath)

            return yara.compile(filepaths=yaraRulesDic) 
        except Exception as e:
            print("[!] Error al cargar las reglas Yara")

    def __staticAnalysisCallback(self,data):
        if(data["matches"]):
            self.__matchedRules.append(data["rule"])
            self.__matchedRulesCount = self.__matchedRulesCount + 1
            self.__malwareFound = 1
    
    def cleanAnalysisOutcome(self):
        self.__analysisOutcome.clear()
        self.__matchedRules.clear()
        self.__malwareFound = 0
        self.__matchedRulesCount = 0

    def executeStaticAnalysis(self, clientSamplePath) -> dict:
        self.__rules.match(filepath=clientSamplePath, callback=self.__staticAnalysisCallback)
        if self.__malwareFound:
            self.__analysisOutcome["detected"] = True
            self.__analysisOutcome["numMatchedRules"] = self.__matchedRulesCount
            self.__analysisOutcome["matchedRules"] = self.__matchedRules
        else:
            self.__analysisOutcome["detected"] = False

        return self.__analysisOutcome