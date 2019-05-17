package com.engine.board;

import com.engine.board.BoardUtils.*;
import com.engine.pieces.Piece;

/**
 * Board class that represents the gameBoard
 */
public class Board {

    public Tile getTile (final int tileCoordinate) {
        return null;
    }
}
    /*Tile[][] gameBoard;

    *//**
     * Constructor that generates a two dimension array of EmptyTiles
     * rank is the current Row a - h
     * file is the current Column 1 - 8
     *//*
    public Board() {
        gameBoard = new Tile[8][8];
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 7; file >= 0; file--) {
                Piece piece = new Piece();
                piece.setType(EPType.EMPTY);
                if (file == 0 || file == 7) {
                    if (rank == 0 || rank == 7) {
                        piece.setType(EPType.ROOK);
                        if (file == 0)
                            piece.setColor(Color.WHITE);
                        else
                            piece.setColor(Color.BLACK);
                    }
                    else if (rank == 1 || rank == 6) {
                        piece.setType(EPType.KNIGHT);
                        if (file == 0)
                            piece.setColor(Color.WHITE);
                        else
                            piece.setColor(Color.BLACK);
                    }
                    else if (rank == 2 || rank == 5) {
                        piece.setType(EPType.BISHOP);
                        if (file == 0)
                            piece.setColor(Color.WHITE);
                        else
                            piece.setColor(Color.BLACK);
                    }
                    else {
                        if (rank == 3) {
                            piece.setType(EPType.QUEEN);
                        }
                        else {
                            piece.setType(EPType.KING);
                        }
                        if (file == 0) {

                            piece.setColor(Color.WHITE);
                        }
                        else {
                            piece.setColor(Color.BLACK);
                        }
                    }
                }
                else if (file == 1 || file == 6) {
                    piece.setType(EPType.PAWN);
                    if(file == 1)
                        piece.setColor(Color.WHITE);
                    else
                        piece.setColor(Color.BLACK);
                }

                if ((rank + file) % 2 == 0)
                    gameBoard[rank][file] = new Tile(piece ,Color.WHITE);
                else
                    gameBoard[rank][file] = new Tile(piece, Color.BLACK);

            }
        }
    }

    public Tile getTile(int rank, int file) {
        return gameBoard[rank][file];
    }

    public void setTile(Tile tile, int rank, int file) {
        gameBoard[rank][file] = tile;
    }
}*/
