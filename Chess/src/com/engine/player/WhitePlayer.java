package com.engine.player;

import com.engine.Alliance;
import com.engine.board.Board;
import com.engine.board.Move;
import com.engine.pieces.Piece;

import java.util.Collection;

public class WhitePlayer extends Player {

    public WhitePlayer(final Board board, final Collection<Move> whiteStandardLegalMoves, final Collection<Move> blackStandardLegalMoves) {
        super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return null;
        //return this.board.getBlackPlayer();
    }
}