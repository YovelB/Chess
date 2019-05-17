package com.engine.board;

import com.engine.pieces.Piece;

import java.util.Map;
import java.util.HashMap;
import com.google.common.collect.ImmutableMap;

/**
 *  Tile class that is every Tile in the chess board that contains a piece
 */

public abstract class Tile {

    /*private final Piece piece;
    private final Color color;

    public Tile(final Piece pieceOnTile, Color color) {
        this.piece = pieceOnTile;
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

    public Piece getPiece() {
        return this.piece;
    }
}*/

    protected final int tileCoordinate;

    private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();

    private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {
        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();

        for (int i = 0; i < 64; i++) {
            emptyTileMap.put(i, new EmptyTile(i));
        }
        return ImmutableMap.copyOf(emptyTileMap);
    }

    public static Tile createTile (final int tileCoordinate, final Piece piece) {
        return  piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES_CACHE.get(tileCoordinate);
    }

    private Tile (final int tileCoordinate) {
        this.tileCoordinate = tileCoordinate;
    }

    public abstract boolean isTileOccupied();

    public abstract Piece getPiece();

    public static final class EmptyTile extends Tile {
        private EmptyTile (final int tileCoordinate) {
            super(tileCoordinate);
        }

        @Override
        public boolean isTileOccupied() {
            return false;
        }

        @Override
        public Piece getPiece() {
            return null;
        }
    }

    public static final class OccupiedTile extends Tile {

        private final Piece pieceOnTile;

        private OccupiedTile (final int tileCoordinate, final Piece piece) {
            super(tileCoordinate);
            this.pieceOnTile = piece;
        }

        @Override
        public boolean isTileOccupied() {
            return true;
        }

        @Override
        public Piece getPiece() {
            return this.pieceOnTile;
        }
    }
}