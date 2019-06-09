package com.engine;

import com.engine.player.BlackPlayer;
import com.engine.player.Player;
import com.engine.player.WhitePlayer;

public enum Alliance {
    WHITE("w") {
        @Override
        public int getDirection() {
            return -1;
        }

        @Override
        public boolean isBlack() {
            return false;
        }

        @Override
        public boolean isWhite() {
            return true;
        }

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer) {
            return whitePlayer;
        }
    },
    BLACK("b") {
        @Override
        public int getDirection() {
            return 1;
        }

        @Override
        public boolean isBlack() {
            return true;
        }

        @Override
        public boolean isWhite() {
            return false;
        }

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer) {
            return blackPlayer;
        }
    };

    private String color;
    Alliance(final String color) {
        this.color = color;
    }

    @Override
    public String toString(){
        return this.color;
    }

    public abstract int getDirection();
    public abstract boolean isBlack();
    public abstract boolean isWhite();

    public abstract Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer);
}