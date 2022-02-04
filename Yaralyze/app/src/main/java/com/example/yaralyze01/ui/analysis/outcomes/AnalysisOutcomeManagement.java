package com.example.yaralyze01.ui.analysis.outcomes;

public interface AnalysisOutcomeManagement {
    void showAnalysisOutcome(AnalysisOutcome analysisOutcome);
    void showAnalysisOutcome(AnalysisOutcome staticOutcome, AnalysisOutcome hashOutcome);
    void showAnalysisException(String exception);
}
