package com.engine.board;

import com.engine.pieces.Pawn;
import com.engine.pieces.Piece;
import com.engine.pieces.Rook;

import static com.engine.board.Board.*;

public abstract class Move {
    protected final Board board;
    protected final Piece movedPiece;
    protected final int destinationCoordinate;
    protected final boolean isFirstMove;

    public static final Move NULL_MOVE = new NullMove();

    private Move(final Board board, final Piece movedPiece, final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
        this.isFirstMove = movedPiece.isFirstMove();
    }

    private Move(final Board board, final int destinationCoordinate) {
        this.board = board;
        this.destinationCoordinate = destinationCoordinate;
        this.movedPiece = null;
        this.isFirstMove = false;
    }

    public int getCurrentCoordinate() {
        return this.getMovedPiece().getPiecePosition();
    }

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    public Piece getMovedPiece() {
        return this.movedPiece;
    }

    public boolean isAttack() {
        return false;
    }

    public boolean isCastlingMove() {
        return false;
    }

    public Piece getAttackedPiece() {
        return null;
    }

    @Override
    public boolean equals(Object other) {
        if(this == other) {
            return true;
        }
        if(!(other instanceof Move)) {
            return false;
        }
        final Move otherMove = (Move) other;
        return getCurrentCoordinate() == otherMove.getCurrentCoordinate() &&
                this.destinationCoordinate == otherMove.getDestinationCoordinate() &&
                this.movedPiece.equals(otherMove.getMovedPiece());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.destinationCoordinate;
        result = prime * result + this.getMovedPiece().hashCode();
        result = prime * result + this.movedPiece.getPiecePosition();
        //result = result + (isFirstMove ? 1 : 0);
        return result;
    }

    /**
     * Creates a new imgBoard(opposed to the default board) and will act as checking board for the default board
     * the players will makeMoves using makeMove func in Player class, on the imgBoard to check if it is valid
     * and if it is valid it will make them on the default board
     *
     * the function creates a builder(a helper class for board) and iterates on all currentPlayer active pieces(not dead pieces)
     * and on all the opponent active pieces and add them to the imgBoard. then it makes the testing move
     * and sets the move maker to the opposing team and returns the builder.build()(the board)
     * @return the imgBoard that is created for testing moves
     */
    public Board execute() {
        final Builder Builder = new Builder();
        for(final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
            if(!this.movedPiece.equals(piece)) {
                Builder.setPiece(piece);
            }
        }
        for(final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
            Builder.setPiece(piece);
        }
        //move the movedPiece the imgBoard
        Builder.setPiece(this.movedPiece.movePiece(this));
        Builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
        return Builder.build();
    }

    /*String disambiguationFile() {
        for(final Move move : this.board.getCurrentPlayer().getLegalMoves()) {
            if(move.getDestinationCoordinate() == this.destinationCoordinate && !this.equals(move) &&
                    this.movedPiece.getPieceType().equals(move.getMovedPiece().getPieceType())) {
                return BoardUtils.INSTANCE.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).substring(0, 1);
            }
        }
        return "";
    }*/

    public static final class MajorMove extends Move {
        public MajorMove(final Board board, final Piece movedPiece, final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof MajorMove && super.equals(other);
        }

        @Override
        public String toString() {
            return movedPiece.getPieceType().toString() + movedPiece.getPieceAlliance().toString();
        }
    }

    public static class AttackMove extends Move {
        final Piece attackedPiece;

        public AttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public int hashCode() {
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(Object other) {
            if(this == other) {
                return true;
            }
            if(!(other instanceof AttackMove)) {
                return false;
            }
            final AttackMove otherAttackMove = (AttackMove) other;
            return super.equals(otherAttackMove) &&
                    this.attackedPiece == otherAttackMove.getAttackedPiece();
        }

        @Override
        public Board execute() {
            return null;
        }

        @Override
        public boolean isAttack() {
            return true;
        }

        @Override
        public Piece getAttackedPiece() {
            return this.attackedPiece;
        }
    }

    public static final class PawnMove extends Move {
        public PawnMove(final Board board, final Piece movedPiece, final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }

    public static class PawnAttackMove extends AttackMove {
        public PawnAttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }
    }

    public static final class PawnEnPassantAttack extends PawnAttackMove {
        public PawnEnPassantAttack(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }
    }

    public static final class PawnJump extends Move {
        public PawnJump(final Board board, final Piece movedPiece, final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        /**
         * Same purpose as before, but now we might detect that when making a pawn jump the opponent can maybe make an enPassant Attack
         * @return the imgBoard
         */
        @Override
        public Board execute() {
            final Builder builder = new Builder();
            for(final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
                if(!this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            final Pawn movedPawn = (Pawn)this.movedPiece.movePiece(this);
            builder.setPiece(movedPiece);
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    static abstract class CastleMove extends Move {
        protected final Rook castleRook;
        protected final int CastleRookDestination;

        public CastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate,
                          Rook castleRook, int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate);
            this.castleRook = castleRook;
            CastleRookDestination = castleRookDestination;
        }

        public Rook getCastleRook() {
            return this.castleRook;
        }

        @Override
        public boolean isCastlingMove() {
            return true;
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder();
            for(final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
                if(!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setPiece(new Rook(this.CastleRookDestination, this.board.getCurrentPlayer().getAlliance()));
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    public static final class KingSideCastleMove extends CastleMove {
        public KingSideCastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate,
                                  Rook castleRook, int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate, castleRook, castleRookDestination);
        }

        @Override
        public String toString() {
            return "0-0";
        }
    }

    public static final class QueenSideCastleMove extends CastleMove {
        public QueenSideCastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate,
                                   Rook castleRook, int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate, castleRook, castleRookDestination);
        }

        @Override
        public String toString() {
            return "0-0-0";
        }
    }

    public static final class NullMove extends Move {
        public NullMove() {
            super(null, -1);
        }

        @Override
        public Board execute() {
            throw new RuntimeException("cannot execute the null move");
        }
    }

    public static class MoveFactory {
        private MoveFactory() {
            throw new RuntimeException("Not instantiable");
        }

        public static Move createMove(final Board board, final int currentCoordinate, final int destinationCoordinate) {
            for(final Move move : board.getAllLegalMoves()) {
                if(move.getCurrentCoordinate() == currentCoordinate && move.getDestinationCoordinate() == destinationCoordinate) {
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }
}