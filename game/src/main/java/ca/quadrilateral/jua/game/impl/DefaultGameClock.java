package ca.quadrilateral.jua.game.impl;

import ca.quadrilateral.jua.game.IGameClock;
import ca.quadrilateral.jua.game.IGameContext;
import ca.quadrilateral.jua.game.exception.JUARuntimeException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.springframework.beans.factory.annotation.Autowired;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;


public class DefaultGameClock implements IGameClock {
    @Autowired
    private IGameContext gameContext;

    private int timeUnits = 0;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

    @Override
    public void decrementTimeOneUnit() {
        if (timeUnits > 0) {
            timeUnits--;
        }
    }

    @Override
    public String getCurrentTime() {
//        try {
//            final GroovyScriptEngine scriptEngine = gameContext.getGroovyScriptEngine();
//            Binding binding = new Binding();
//            binding.setVariable("timeUnits", new Integer(timeUnits));
//            scriptEngine.run("gameClockCurrentTime.groovy", binding);
//            return (String) binding.getVariable("result");
//        } catch (Exception e) {
//            throw new JUARuntimeException(e);
//        }

        calendar.set(0, 0, 0, 0, 0);
        calendar.add(Calendar.MINUTE, timeUnits);
        return formatter.format(calendar.getTime());
    }

    @Override
    public int getCurrentTimeUnits() {
        return this.timeUnits;
    }

    @Override
    public void incrementTimeOneUnit() {
        this.timeUnits++;
    }

    @Override
    public void setCurrentTimeUnits(int timeUnits) {
        this.timeUnits = timeUnits;
    }

}
