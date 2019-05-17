package com.engine.pieces;

import com.engine.Alliance;
import com.engine.board.Board;
import com.engine.board.Move;
import com.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

    // offsets for knight with the perspective  of its position
    private static final int[] CANDIDATE_MOVE_COORDINATE = { -17, -15, -10, -6, 6, 10, -15, -17};

    public Knight(final int piecePosition, final Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance);
    }

    @Override
    public List<Move> calculateLegalMove(Board board) {

        int candidateDestinationCoordinate;
        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidate : CANDIDATE_MOVE_COORDINATE) {
            candidateDestinationCoordinate = this.piecePosition + currentCandidate;
            if (true /* isValidTileCoordinate */) {
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                // new Move if Tile is empty
                if (!candidateDestinationTile.isTileOccupied()) {
                    legalMoves.add(new Move());
                } else {
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAllianceAtDestination = pieceAtDestination.getPieceAlliance();
                    // new Move if on the Tile there is an enemy
                    if (this.pieceAlliance != pieceAllianceAtDestination) {
                        legalMoves.add(new Move());
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }
}
