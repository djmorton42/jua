package ca.switchcase.commons.util;

import java.io.Serializable;
import java.text.MessageFormat;

/**
This class is a container that allows two strongly typed objects to be stored together
@author Daniel Morton
@version 1.0
@since 1.0
*/
public class Pair<F, S> implements Serializable {
	private static final long serialVersionUID = 1;

	public Pair() {}

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public F getFirst() {
        return this.first;
    }

    public S getSecond() {
        return this.second;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public void setSecond(S second) {
        this.second = second;
    }

    @Override
    public String toString() {
        return MessageFormat.format("Pair [First={0}, Second={1}",
            this.first == null ? "null" : this.first.toString(),
            this.second == null ? "null" : this.second.toString());
    }

    @Override
    @SuppressWarnings(value="unchecked")
    public boolean equals(Object o) {
        if (o instanceof Pair) {
            Pair p = (Pair)o;
            return p.getFirst().equals(this.getFirst()) &&
                   p.getSecond().equals(this.getSecond());
        }

        return false;
    }

    protected F first = null;
    protected S second = null;
}
