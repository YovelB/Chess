package com.engine.pieces;

import com.engine.Alliance;
import com.engine.board.Board;
import com.engine.board.BoardUtils;
import com.engine.board.Move;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.engine.board.Move.*;

public class Pawn extends Piece{

    private final static int[] CANDIDATE_MOVE_COORDINATE = {7, 8, 9, 16};

    public Pawn(final int piecePosition, final Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance, PieceType.PAWN, true);
    }

    public Pawn(final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove) {
        super(piecePosition, pieceAlliance, PieceType.PAWN, isFirstMove);
    }

    /**
     * Calculates all the legal(available) moves of the Pawn and return it as a list
     *
     * Iterates over all the offsets compared to the current position and checks if the candidateDestinationCoordinate
     * is on the board and check if the Pawn can move 1 step else if the Pawn can make 2 steps when it is the first move.
     * else we check if there's a specific offset(7,9) of an attack move
     *
     * @param board is needed for access to the Tiles and Pieces on the board
     * @return the list of legalMoves that cannot be change hench it is "final" and return as "Immutable.copyOf(legalMoves)"
     */
    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMove = new ArrayList<>();
        for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {
            final int candidateDestinationCoordinate = this.getPiecePosition() + (this.getPieceAlliance().getDirection() * currentCandidateOffset);
            if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                continue;
            }
            if(currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                //TODO more work to do here(deal with promotions)!!
                legalMove.add(new MajorMove(board, this, candidateDestinationCoordinate));
            } else if(currentCandidateOffset == 16 && this.isFirstMove() &&
                    (BoardUtils.SEVENTH_RANK[this.getPiecePosition()] && this.getPieceAlliance().isBlack() ||
                    (BoardUtils.SECOND_RANK[this.getPiecePosition()] && this.getPieceAlliance().isWhite()))) {
                final int behindCandidateDestinationCoordinate = this.getPiecePosition() + (this.getPieceAlliance().getDirection() * 8);
                if(!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() &&
                    !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    legalMove.add(new PawnJump(board, this, candidateDestinationCoordinate));
                }
            } else if(currentCandidateOffset == 7 &&
                    !((BoardUtils.EIGHTH_FILE[this.getPiecePosition()] && this.getPieceAlliance().isWhite() ||
                    (BoardUtils.FIRST_FILE[this.getPiecePosition()] && this.getPieceAlliance().isBlack())))) {
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    final Piece pieceAtDestination = board.getTile(candidateDestinationCoordinate).getPiece();
                    if(this.getPieceAlliance() != pieceAtDestination.getPieceAlliance()) {
                        //TODO more to do here!!!
                        legalMove.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            } else if(currentCandidateOffset == 9 &&
                    !((BoardUtils.FIRST_FILE[this.getPiecePosition()] && this.getPieceAlliance().isWhite() ||
                    (BoardUtils.EIGHTH_FILE[this.getPiecePosition()] && this.getPieceAlliance().isBlack())))) {
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    final Piece pieceAtDestination = board.getTile(candidateDestinationCoordinate).getPiece();
                    if(this.getPieceAlliance() != pieceAtDestination.getPieceAlliance()) {
                        //TODO more to do here!!!
                        legalMove.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMove);
    }

    @Override
    public Pawn movePiece(final Move move) {
        return new Pawn(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }
}