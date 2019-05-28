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

    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentLegals) {
        final List<Move> kingCastles = new ArrayList<>();
        if(this.playerKing.isFirstMove() && !this.isInCheck()) {
            //black king side castle
            if(!this.board.getTile(5).isTileOccupied() && !this.board.getTile(6).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(7);
                if(rookTile.isTileOccupied() && rookTile.getPiece().getPieceType().isRook() && rookTile.getPiece().isFirstMove()) {
                    if(Player.calculateAttackOnTile(5, opponentLegals).isEmpty() &&
                            Player.calculateAttackOnTile(6, opponentLegals).isEmpty()) {
                        kingCastles.add(new KingSideCastleMove(this.board, this.playerKing,
                                6, (Rook) rookTile.getPiece(),5));
                    }
                }
            }
            //black queen side castle
            if(!this.board.getTile(3).isTileOccupied() && !this.board.getTile(2).isTileOccupied() &&
                    !this.board.getTile(1).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(0);
                if(rookTile.isTileOccupied() && rookTile.getPiece().getPieceType().isRook() && rookTile.getPiece().isFirstMove()) {
                    if(Player.calculateAttackOnTile(3, opponentLegals).isEmpty() &&
                            Player.calculateAttackOnTile(2, opponentLegals).isEmpty() &&
                            Player.calculateAttackOnTile(1, opponentLegals).isEmpty())
                        //TODO add queen side CASTLEMOVE!
                        kingCastles.add(new QueenSideCastleMove(this.board, this.playerKing,
                                2, (Rook) rookTile.getPiece(), 3));
                }
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}