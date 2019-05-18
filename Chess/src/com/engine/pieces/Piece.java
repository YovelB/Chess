package com.engine.pieces;

import com.engine.Alliance;
import com.engine.board.Board;
import com.engine.board.Move;

import java.util.Collection;

public abstract class Piece {
    private final pieceType pieceType;
    private final int piecePosition;
    private final Alliance pieceAlliance;
    private final boolean isFirstMove;

    Piece(final pieceType pieceType, final int piecePosition, final Alliance pieceAlliance) {
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

    public pieceType getPieceType() {
        return this.pieceType;
    }

    public boolean isFirstMove() {
        return this.isFirstMove;
    }

    //Each Piece has this func so it knows where it can Move
    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public enum pieceType {

        PAWN("P") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        KNIGHT("N") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        BISHOP("B") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        ROOK("R") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        QUEEN("Q") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        KING("K") {
            @Override
            public boolean isKing() {
                return true;
            }
        };

        private String pieceName;

        pieceType(final String pieceName) {
            this.pieceName = pieceName;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }

        public abstract boolean isKing();
    }
}
