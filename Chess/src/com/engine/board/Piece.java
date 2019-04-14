package com.engine.board;

import com.engine.board.BoardUtils.*;

/**
 *
 */

/*
    TODO check how to add subclasses with the EPType
 */
public class Piece {

    private EPType type;
    private Color color;

    public void Piece(EPType type, Color color) {
        this.type = type;
        this.color = color;
    }

    public EPType getType() {
        return type;
    }

    public void setType(EPType type) {
        this.type = type;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
