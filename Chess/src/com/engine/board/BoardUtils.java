package com.engine.board;

/**
 * BoardUtils class that represents utilities for the board
 */
public class BoardUtils {

    // Properties of the board
    public static int boardWidth = 600, boardHeight = 600, tileWidth = 75, tileHeight = 75;

    // color of the Piece and Tile
    public enum Color {
        WHITE,
        BLACK;

    }
    // type of the Piece
    public enum EPType {
        EMPTY,
        PAWN,
        KNIGHT,
        BISHOP,
        ROOK,
        QUEEN,
        KING
        }
}
