package ca.quadrilateral.jua.game.impl;

import ca.quadrilateral.jua.game.IGameContext;
import ca.quadrilateral.jua.game.IGameStateMachine;
import ca.quadrilateral.jua.game.ILevelContext;
import ca.quadrilateral.jua.game.IScriptManager;
import ca.quadrilateral.jua.game.exception.JUARuntimeException;
import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class ScriptManager implements IScriptManager {

    private static final Logger log = Logger.getLogger(ScriptManager.class);
    @Autowired
    private IGameContext gameContext;
    @Autowired
    private ILevelContext levelContext;
    @Autowired
    private IGameStateMachine gameStateMachine;

    @Override
    public String processVariableReplacement(String inputText) {
        try {
            final GroovyScriptEngine scriptEngine = gameContext.getGroovyScriptEngine();
            Binding binding = new Binding();
            binding.setVariable("levelContext", levelContext);
            binding.setVariable("stateMachine", gameStateMachine);
            binding.setVariable("text", cleanInputText(inputText));

            scriptEngine.run("processString.groovy", binding);
            return processOutputText((String) binding.getVariable("result"));
        } catch (Exception e) {
            throw new JUARuntimeException(e);
        }
    }

    @Override
    public String processVariableReplacement(String inputText, Map<String, String> eventBindings) {
                try {
            final GroovyScriptEngine scriptEngine = gameContext.getGroovyScriptEngine();
            Binding binding = new Binding();
            binding.setVariable("levelContext", levelContext);
            binding.setVariable("stateMachine", gameStateMachine);
            binding.setVariable("text", cleanInputText(inputText));
            binding.setVariable("event", eventBindings);

            scriptEngine.run("processString.groovy", binding);
            return processOutputText((String) binding.getVariable("result"));
        } catch (Exception e) {
            throw new JUARuntimeException(e);
        }
    }

    private String processOutputText(String outputText) {
        System.out.println("Original Output Text: " + outputText);
        String modifiedOutputText = outputText.replace("<br/>", "\n").replace("<br />", "\n").replace("&#160;", " ");
        System.out.println("Processed Output Text: " + modifiedOutputText);
        return modifiedOutputText;
    }

    private String cleanInputText(String inputText) {
        System.out.println("Original Input Text: " + inputText);
        String modifiedInputText = inputText.trim().replace("\n", " ").replaceAll("[\\s]{2,}", " ");
        System.out.println("Modified Input Text: " + modifiedInputText);
        return modifiedInputText;
    }
}
