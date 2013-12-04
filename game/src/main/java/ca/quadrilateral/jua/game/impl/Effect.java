package ca.quadrilateral.jua.game.impl;

import ca.quadrilateral.jua.game.enums.EffectType;
import java.util.ArrayList;
import java.util.List;

public class Effect {
    private String effectName = null;
    private List<EffectDetails> effectDetailsList = null;

    public Effect() {
        effectDetailsList = new ArrayList<EffectDetails>(0xff);
    }

    public Effect(String effectName) {
        this();
        this.effectName = effectName;
    }

    public void addEffectDetails(EffectDetails effectDetails) {
        this.effectDetailsList.add(effectDetails);
    }

    public List<EffectDetails> getEffectDetailsForType(EffectType effectType) {
        final List<EffectDetails> resultList = new ArrayList<EffectDetails>();

        for(EffectDetails details : this.effectDetailsList) {
            if (details.getEffectType().equals(effectType)) {
                resultList.add(details);
            }
        }

        return resultList;
    }

    public List<EffectDetails> getEffectDetails() {
        return new ArrayList<EffectDetails>();
    }
    
    public String getEffectName() {
        return this.effectName;
    }
    
    public void setEffectName(String effectName) {
        this.effectName = effectName;
    }


    public static class EffectDetails {
        private EffectType effectType;
        private String scriptName;

        public EffectDetails(EffectType effectType, String scriptName) {
            this.effectType = effectType;
            this.scriptName = scriptName;
        }

        public EffectType getEffectType() {
            return effectType;
        }

        public void setEffectType(EffectType effectType) {
            this.effectType = effectType;
        }

        public String getScriptName() {
            return scriptName;
        }

        public void setScriptName(String scriptName) {
            this.scriptName = scriptName;
        }
    }
}
