import yara
from yara.rules import Rules
import os

YARA_RULES_PATH = ".\\AnalysisSamples\\YaraRules\\staticRules.yar"

class Analyzer():
    def __init__(self) -> None:
        self.__rules = self.__loadRules()

    def __loadRules(self) -> Rules:
        try:
            return yara.compile(filepath=YARA_RULES_PATH) 
        except Exception as e:
            print("[!] Error al cargar las reglas Yara")

    def executeStaticAnalysis(self, clientSamplePath) -> bool:
        matches = self.__rules.match(filepath=clientSamplePath)
        if matches != {}:
            return True
        else:
            return False