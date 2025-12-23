package services;

import data.ActionType;
import data.ProductID;
import exceptions.BadPromptException;
import exceptions.AIException;

import data.Suggestion;
import exceptions.ProductIDException;
import exceptions.SuggestionException;
import medicalconsultation.FqUnit;
import medicalconsultation.dayMoment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class DecisionMakingAIMock {

    private boolean isInitialized = false;

    public void initDecisionMakingIA() throws AIException {

        //Simulate and error for the exception
        if (Math.random() < 0.05) {
            throw new AIException("Problems with the invocation of the IA.");
        }

        this.isInitialized = true;
    }


    public String getSuggestions(String prompt) throws BadPromptException {
        if (!isInitialized) {
            throw new RuntimeException("The IA is not initialized.");
        }

        if (prompt == null) {
            throw new BadPromptException("The prompt is not clear or right");
        }

        String[] PossibleAnswers = {
                "<I, 243516578917, BEFORELUNCH, 15, 1, 1, DAY, Tomar con abundante agua>",
                "<R, 640557143200>",
                "<M, 243516578917, , , 3, , , > "
        };

        Random rand = new Random();
        int RandomIndex = rand.nextInt(PossibleAnswers.length);

        return PossibleAnswers[RandomIndex];
    }

    public List<Suggestion> parseSuggest(String aiAnswer) throws ProductIDException, SuggestionException {
        List<Suggestion> list = new ArrayList<>();
        if (aiAnswer == null || aiAnswer.isEmpty()) return list;

        String clean = aiAnswer.replace("<", "").replace(">", "");

        String[] data = clean.split(",");

        for (int i = 0; i < data.length; i++) {
            data[i] = data[i].trim();
        }

        String letter = data[0];
        ActionType action = switch (letter) {
            case "I" -> ActionType.ADD;
            case "R" -> ActionType.DELETE;
            case "M" -> ActionType.MODIFY;
            default -> null;
        };

        ProductID id = new ProductID(data[1]);

        dayMoment mom = null;
        Double dur = null, dose = null, freq = null;
        FqUnit unit = null;
        String instr = null;

        if (action != ActionType.DELETE) {
            if (!data[2].isEmpty()) mom = dayMoment.valueOf(data[2]);
            if (!data[3].isEmpty()) dur = Double.valueOf(data[3]);
            if (!data[4].isEmpty()) dose = Double.valueOf(data[4]);
            if (!data[5].isEmpty()) freq = Double.valueOf(data[5]);
            if (!data[6].isEmpty()) unit = FqUnit.valueOf(data[6]);
            if (data.length > 7) instr = data[7];
        }

        list.add(new Suggestion(action, id, mom, dur, dose, freq, unit, instr));

        return list;
    }
}
