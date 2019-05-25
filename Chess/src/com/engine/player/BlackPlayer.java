package com.engine.player;

import com.engine.Alliance;
import com.engine.board.Board;
import com.engine.board.Move;
import com.engine.pieces.Piece;

import java.util.Collection;

public class BlackPlayer extends Player {

    public BlackPlayer(final Board board, final Collection<Move> whiteStandardLegalMoves, final Collection<Move> blackStandardLegalMoves) {
        super(board, blackStandardLegalMoves, whiteStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }

    @Override
    public Player getOpponent() {
        return null;
        //return this.board.getWhitePlayer();
    }
}