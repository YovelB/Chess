package com.engine.player;

import com.engine.Alliance;
import com.engine.board.Board;
import com.engine.board.Move;
import com.engine.board.Tile;
import com.engine.pieces.Piece;
import com.engine.pieces.Rook;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.engine.board.Move.*;

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

    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentLegals) {
        final List<Move> kingCastles = new ArrayList<>();
        if(this.playerKing.isFirstMove() && !this.isInCheck()) {
            //white king side castle
            if(!this.board.getTile(61).isTileOccupied() && !this.board.getTile(62).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(63);
                if(rookTile.isTileOccupied() && rookTile.getPiece().getPieceType().isRook() && rookTile.getPiece().isFirstMove()) {
                    if(Player.calculateAttackOnTile(61, opponentLegals).isEmpty() &&
                        Player.calculateAttackOnTile(62, opponentLegals).isEmpty()) {
                        kingCastles.add(new KingSideCastleMove(this.board, this.playerKing,
                                62, (Rook) rookTile.getPiece(),61));
                    }
                }
            }
            //white queen side castle
            if(!this.board.getTile(59).isTileOccupied() && !this.board.getTile(58).isTileOccupied() &&
                !this.board.getTile(57).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(56);
                if(rookTile.isTileOccupied() && rookTile.getPiece().getPieceType().isRook() && rookTile.getPiece().isFirstMove()) {
                    if(Player.calculateAttackOnTile(59, opponentLegals).isEmpty() &&
                        Player.calculateAttackOnTile(58, opponentLegals).isEmpty() &&
                        Player.calculateAttackOnTile(57, opponentLegals).isEmpty())
                    kingCastles.add(new QueenSideCastleMove(this.board, this.playerKing,
                            58, (Rook) rookTile.getPiece(), 59));
                }
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}