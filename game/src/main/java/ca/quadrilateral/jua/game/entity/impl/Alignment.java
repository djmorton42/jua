package ca.quadrilateral.jua.game.entity.impl;

import java.util.Collection;

public class Alignment {
    private String name = null;
    private String abbreviation = null;

    public Alignment(String name, String abbreviation) {
        this.name = name;
        this.abbreviation = abbreviation;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAbbreviation() {
        return this.abbreviation;
    }
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public static Alignment getAlignmentByName(Collection<Alignment> alignments, String name) {
        for(Alignment alignment : alignments) {
            if (name.equals(alignment.getName())) {
                return alignment;
            }
        }
        return null;
    }
}
