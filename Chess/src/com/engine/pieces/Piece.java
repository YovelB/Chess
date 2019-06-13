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
    // a variable that store a Piece(class or subclass) hashCode and pre computes it in the constructor(at the start)
    private final int cachedHasCode;


    Piece(final int piecePosition, final Alliance pieceAlliance, final PieceType pieceType, final boolean isFirstMove) {
        this.pieceType = pieceType;
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.isFirstMove = isFirstMove;
        this.cachedHasCode = computeHashCode();
    }

    /**
     * function that is for computing the hash code of Piece
     * @return the Piece(class or sub class) hashCode
     */
    private int computeHashCode() {
        int resualt = this.piecePosition;
        resualt = 31 * resualt + this.pieceAlliance.hashCode();
        resualt = 31 * resualt + this.pieceType.hashCode();
        resualt = 31 * resualt + (this.isFirstMove ? 1 : 0);
        return resualt;
    }

    /**
     * needed for not only comparing two Piece objects via only reference(class it self) but comparing
     * them(classes and subclasses) with their parameters
     *
     * we first compare by reference of the objects then check if it is the class or it is a subclass then we check
     * if it has the same parameters
     * @param other the other Object we compare with
     * @return if the two objects are equal
     */
    @Override
    public boolean equals(final Object other) {
        if(this == other) {
            return true;
        }
        if(!(other instanceof Piece)) {
            return false;
        }
        final Piece otherPiece = (Piece) other;
        return this.piecePosition == otherPiece.getPiecePosition() &&
                this.pieceAlliance == otherPiece.getPieceAlliance() &&
                this.pieceType == otherPiece.getPieceType() &&
                this.isFirstMove == otherPiece.isFirstMove();
    }

    @Override
    public int hashCode() {
        return this.cachedHasCode;
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

    public int getPieceValue() {
        return this.pieceType.getPieceValue();
    }

    //Each Piece has this func so each piece has it's list of legal moves it can make
    public abstract Collection<Move> calculateLegalMoves(final Board board);
    public abstract Piece movePiece(final Move move);
}