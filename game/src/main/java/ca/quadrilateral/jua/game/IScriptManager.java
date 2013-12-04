package ca.quadrilateral.jua.game;

import java.util.Map;

public interface IScriptManager {
    String processVariableReplacement(String inputText);
    String processVariableReplacement(String inputText, Map<String, String> eventBindings);
}
