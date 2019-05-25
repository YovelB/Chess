package com.engine.pieces;

import com.engine.Alliance;
import com.engine.board.Board;
import com.engine.board.BoardUtils;
import com.engine.board.Move;
import com.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.engine.board.Move.*;

public class Knight extends Piece {

    // offsets for knight with the perspective of its position
    private static final int[] CANDIDATE_MOVE_COORDINATE = {-17, -15, -10, -6, 6, 10, -15, -17};

    public Knight(final int piecePosition, final Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance, PieceType.KNIGHT);
    }

    /**
     * Calculates all the legal(available) moves of the knight and return it as a list
     *
     * Iterates over all the offsets compared to the current position and checks if the candidateDestinationCoordinate
     * is on the board and the exceptions are checks against the offset rules(because they do not always apply).
     * after that we check if the candidateDestinationCoordinate has a Piece, if not we add a legal MajorMove and else it
     * checks whether the Piece is an enemy, if it is we add a AttackMove.
     *
     * @param board is needed for access to the Tiles and Pieces on the board
     * @return the list of legalMoves that cannot be change hench it is "final" and return as "Immutable.copyOf(legalMoves)"
     */
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {
            final int candidateDestinationCoordinate = this.getPiecePosition() + currentCandidateOffset;
            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                if (isFirstColumnExclusion(this.getPiecePosition(), currentCandidateOffset) ||
                        isSecondColumnExclusion(this.getPiecePosition(), currentCandidateOffset) ||
                        isSeventhColumnExclusion(this.getPiecePosition(), currentCandidateOffset) ||
                        isEighthColumnExclusion(this.getPiecePosition(), currentCandidateOffset)) {
                    continue;
                }
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if (!candidateDestinationTile.isTileOccupied()) { // new Move if Tile is empty
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                } else {
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    if (this.getPieceAlliance() != pieceAtDestination.getPieceAlliance()) { // new Move if on the destination Tile there is an enemy
                        legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public String toString() {
        return PieceType.KNIGHT.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -17 || candidateOffset == -10 ||
                candidateOffset == 6 || candidateOffset == 15);
    }

    private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.SECOND_COLUMN[currentPosition] && (candidateOffset == -10 || candidateOffset == 6);
    }

    private static boolean isSeventhColumnExclusion(final int currentPortion, final int candidateOffset) {
        return BoardUtils.SEVENTH_COLUMN[currentPortion] && (candidateOffset == -6 || candidateOffset == -10);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -15 || candidateOffset == -6 ||
                candidateOffset == 10 || candidateOffset == 17);
    }
}