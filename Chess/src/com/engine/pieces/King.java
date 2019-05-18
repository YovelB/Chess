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
        super(pieceType.KING, piecePosition, pieceAlliance);
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == -1 ||
                candidateOffset == 7);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 1 ||
                candidateOffset == 9);
    }

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
    public String toString() {
        return pieceType.KING.toString();
    }
}