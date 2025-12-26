package services;

import data.ActionType;
import data.ProductID;
import exceptions.BadPromptException;
import exceptions.AIException;

import data.Suggestion;
import medicalconsultation.FqUnit;
import medicalconsultation.dayMoment;
import services.interfaces.DecisionMakingAI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class DecisionMakingAIMock implements DecisionMakingAI {

    private boolean failWithAIException = false;
    private boolean failWithsuggestions = false;

    @Override
    public void initDecisionMakingAI() throws AIException {
        if (failWithAIException) {
            throw new AIException("Problems with the invocation of the IA.");
        }
    }

    @Override
    public String getSuggestions(String prompt) throws BadPromptException {
        if (prompt == null) {
            throw new BadPromptException("The prompt is not clear or right");
        }

        String[] PossibleAnswers = {
                "<I, 243516578917, BEFORELUNCH, 15, 1, 1, DAY, Tomar con abundante agua>",
                "<R, 640557143200>",
                "<M, 243516578917, , , 3, , , > "
        };

        Random rand = new Random();
        int randomIndex = rand.nextInt(PossibleAnswers.length);

        return PossibleAnswers[randomIndex];
    }

    @Override
    public List<Suggestion> parseSuggest(String aiAnswer) {
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

        try {
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

        } catch (Exception e) {
            // Shouldn't happen
            return new ArrayList<>();
        }

        if (failWithsuggestions) {
            return new ArrayList<>();
        }

        return list;
    }

    // Setters

    public void setFailWithAIException(boolean failWithAIException) {
        this.failWithAIException = failWithAIException;
    }

    public void setFailWithsuggestions(boolean failWithsuggestions) {
        this.failWithsuggestions = failWithsuggestions;
    }
}
