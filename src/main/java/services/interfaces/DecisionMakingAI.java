package services.interfaces;

import exceptions.*;
import data.Suggestion;

import java.util.List;

public interface DecisionMakingAI {
    void initDecisionMakingAI() throws AIException;

    String getSuggestions(String prompt) throws BadPromptException;

    List<Suggestion> parseSuggest(String aiAnswer);
}
