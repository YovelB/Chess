package com.engine.board;

import com.engine.pieces.Piece;

import static com.engine.board.Board.*;

public abstract class Move {
    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;

    private Move(final Board board, final Piece movedPiece, final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    public abstract Board execute();

    public static final class MajorMove extends Move {
        public MajorMove(final Board board, final Piece movedPiece, final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public Board execute() {
            final Builder Builder = new Builder();
            for(final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
                //TODO hashcode and equals for pieces
                if(!this.movedPiece.equals(piece)) {
                    Builder.setPiece(piece);
                }
            }
            for(final Piece piece: this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
                Builder.setPiece(piece);
            }
            //move the moved piece
            Builder.setPiece(null);
            Builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return Builder.build();
        }
    }
    public static final class AttackMove extends Move {

        final Piece attackedPiece;

        public AttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public Board execute() {
            return null;
        }
    }
}
