package com.engine.pieces;

import com.engine.Alliance;
import com.engine.board.Board;
import com.engine.board.Move;

import java.util.Collection;

public abstract class Piece {
    private final PieceType pieceType;
    private final int piecePosition;
    private final Alliance pieceAlliance;
    private final boolean isFirstMove;

    Piece(final int piecePosition, final Alliance pieceAlliance, final PieceType pieceType) {
        this.pieceType = pieceType;
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        //TODO more work here!!!
        this.isFirstMove = false;
    }

    public int getPiecePosition() {
        return this.piecePosition;
    }

    public Alliance getPieceAlliance() {
        return this.pieceAlliance;
    }

    public PieceType getPieceType() {
        return this.pieceType;
    }

    public boolean isFirstMove() {
        return this.isFirstMove;
    }

    //Each Piece has this func so each piece has it's list of legal moves it can make
    public abstract Collection<Move> calculateLegalMoves(final Board board);
}