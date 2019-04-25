/*

 */

package org.gmu.chess.models;

import java.io.Serializable;
import java.util.List;

public class AvailableMove implements Serializable {
    private static final long serialVersionUID = 8612686569088965026L;

    private final String from;
    private final List<String> positions;

    public AvailableMove(String from, List<String> positions) {
        this.from = from;
        this.positions = positions;
    }

    public String getFrom() {
        return from;
    }

    public List<String> getPositions() {
        return positions;
    }
}
