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

public class King extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATE = {-9, -8, -7, -1, 1, 7, 8, 9};

    public King(final int piecePosition, final Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance, PieceType.KING, true);
    }

    public King(final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove) {
        super(piecePosition, pieceAlliance, PieceType.KING, isFirstMove);
    }

    /**
     * Calculates all the legal(available) moves of the King and return it as a list
     * <p>
     * Iterates over all the offsets compared to the current position and compared to the knight it add the offsets again and
     * again until the candidateDestinationCoordinate is not on the Board and checks if the candidateDestinationCoordinate
     * is on the board and the exceptions are checks against the offset rules(because they do not always apply)
     * and breaks the loop if they don't apply.
     * after that we check if the candidateDestinationCoordinate has a Piece, if not we add a legal MajorMove and else it
     * checks whether the Piece is an enemy, if it is we add a AttackMove and because it has a Piece we don't want to
     * keep adding offsets because the pieceAtDestination is blocking the rest of the available Moves of the Bishop.
     *
     * @param board is needed for access to the Tiles and Pieces on the board
     * @return the list of legalMoves that cannot be change hench it is "final" and return as "Immutable.copyOf(legalMoves)"
     */
    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {
            final int candidateDestinationCoordinate = this.getPiecePosition() + currentCandidateOffset;
            if(isFirstColumnExclusion(this.getPiecePosition(), currentCandidateOffset) ||
                    isEighthColumnExclusion(this.getPiecePosition(), currentCandidateOffset)) {
                continue;
            }
            if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
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
    public King movePiece(final Move move) {
        return new King(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    @Override
    public String toString() {
        return PieceType.KING.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_FILE[currentPosition] && (candidateOffset == -9 || candidateOffset == -1 ||
                candidateOffset == 7);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHTH_FILE[currentPosition] && (candidateOffset == -7 || candidateOffset == 1 ||
                candidateOffset == 9);
    }
}