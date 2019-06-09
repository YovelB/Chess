package com.engine.pieces;

public enum PieceType {
    PAWN("p", 1) {
        @Override
        public boolean isKing() {
            return false;
        }

        @Override
        public boolean isRook() {
            return false;
        }
    },
    KNIGHT("n", 3) {
        @Override
        public boolean isKing() {
            return false;
        }

        @Override
        public boolean isRook() {
            return false;
        }
    },
    BISHOP("b", 3) {
        @Override
        public boolean isKing() {
            return false;
        }

        @Override
        public boolean isRook() {
            return false;
        }
    },
    ROOK("r", 5) {
        @Override
        public boolean isKing() {
            return false;
        }

        @Override
        public boolean isRook() {
            return true;
        }
    },
    QUEEN("q", 9) {
        @Override
        public boolean isKing() {
            return false;
        }

        @Override
        public boolean isRook() {
            return false;
        }
    },
    KING("k", 20) {
        @Override
        public boolean isKing() {
            return true;
        }

        @Override
        public boolean isRook() {
            return false;
        }
    };

    private String pieceName;
    private int pieceValue;

    PieceType(final String pieceName, final int pieceValue) {
        this.pieceName = pieceName;
        this.pieceValue = pieceValue;
    }

    @Override
    public String toString() {
        return this.pieceName;
    }
    public int getPieceValue() {
        return this.pieceValue;
    }
    public abstract boolean isKing();
    public abstract boolean isRook();
}