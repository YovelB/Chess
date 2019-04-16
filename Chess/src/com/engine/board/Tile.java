package com.engine.board;

import com.engine.board.BoardUtils.*;

/**
 *  Tile class that is every Tile in the chess board that contains a piece
 */

/*
    TODO check if you need an abstract Tile
 */

public  class Tile {

    private Piece piece;
    private EPType pType;
    private Color color;

    /**
     * Constructor of Tile class
     * @param piece is a piece of the game
     * @param color is the color of the tile
     */
    public Tile (Piece piece, Color color) {
        this.piece = piece;
        this.color = color;
    }

    public Piece getPiece() { return  this.piece; }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public EPType getEPType() { return  this.pType; }

    public void setEPtype(EPType pType) { this.pType = pType; }

    public Color getColor() {
        return  color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}