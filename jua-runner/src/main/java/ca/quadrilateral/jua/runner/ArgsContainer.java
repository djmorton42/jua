package ca.quadrilateral.jua.runner;

import java.util.ArrayList;
import java.util.List;

public class ArgsContainer {

    private static volatile ArgsContainer instance = null;
    public List<String> args = null;

    public static ArgsContainer getInstance() {
        if (instance == null) {
            instance = new ArgsContainer();
        }
        return instance;
    }

    public void setArgs(String[] commandLineArgs) {
        args = new ArrayList<String>();
        for (String s : commandLineArgs) {
            args.add(s);
        }

    }

    public List<String> getArgs() {
        return this.args;
    }
}
