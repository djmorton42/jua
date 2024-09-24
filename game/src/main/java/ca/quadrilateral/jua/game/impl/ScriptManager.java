package ca.quadrilateral.jua.game.impl;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.quadrilateral.jua.game.IGameContext;
import ca.quadrilateral.jua.game.IGameStateMachine;
import ca.quadrilateral.jua.game.ILevelContext;
import ca.quadrilateral.jua.game.IScriptManager;
import ca.quadrilateral.jua.game.exception.JUARuntimeException;

public class ScriptManager implements IScriptManager {
    private static final Logger logger = LoggerFactory.getLogger(ScriptManager.class);

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
        logger.debug("Original Output Text: {}", outputText);
        String modifiedOutputText = outputText.replace("<br/>", "\n").replace("<br />", "\n").replace("&#160;", " ");
        logger.debug("Processed Output Text: {}", modifiedOutputText);
        return modifiedOutputText;
    }

    private String cleanInputText(String inputText) {
        logger.debug("Original Input Text: ", inputText);
        String modifiedInputText = inputText.trim().replace("\n", " ").replaceAll("[\\s]{2,}", " ");
        logger.debug("Modified Input Text: ", modifiedInputText);
        return modifiedInputText;
    }
}
