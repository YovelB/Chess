package com.gui;

import com.engine.board.Move;
import com.engine.board.Tile;
import com.engine.pieces.Piece;
import com.google.common.primitives.Ints;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static com.gui.Controller.MoveLog;
import static com.gui.Controller.RESOURCES_PATH;

class TakenPiecesPanel extends VBox {
    private final TilePane upperPanel;
    private final TilePane lowerPanel;

    private static final String BACKGROUND_COLOR_PANEL = "-fx-background-color : rgb(255, 222, 173)";

    TakenPiecesPanel() {
        this.setPrefSize(80, 550);
        this.setStyle(BACKGROUND_COLOR_PANEL);
        upperPanel = new TilePane();
        lowerPanel = new TilePane();
        upperPanel.setPrefSize(80, 275);
        lowerPanel.setPrefSize(80, 275);
        //TODO check about the properties Hgap and Vgap and prefColumns of TilePane
        upperPanel.setHgap(2.5);
        upperPanel.setVgap(2.5);
        lowerPanel.setHgap(2.5);
        lowerPanel.setVgap(2.5);
        lowerPanel.setStyle(BACKGROUND_COLOR_PANEL);
        lowerPanel.setStyle(BACKGROUND_COLOR_PANEL);
        this.getChildren().addAll(upperPanel, lowerPanel);
    }

    void addTakenPiece(final MoveLog moveLog) {
        this.upperPanel.getChildren().clear();
        this.lowerPanel.getChildren().clear();
        final List<Piece> whiteTakenPieces = new ArrayList<>();
        final List<Piece> blackTakenPieces = new ArrayList<>();
        for(final Move move : moveLog.getMoves()) {
            if(move.isAttack()) {
                final Piece takenPiece = move.getAttackedPiece();
                if(takenPiece.getPieceAlliance().isWhite()) {
                    whiteTakenPieces.add(takenPiece);
                } else if(takenPiece.getPieceAlliance().isBlack()) {
                    blackTakenPieces.add(takenPiece);
                } else {
                    throw new RuntimeException("Should not reach here");
                }
            }
        }
        //TODO fix the add functions
        whiteTakenPieces.sort((p1, p2) -> Ints.compare(p1.getPieceValue(), p2.getPieceValue()));
        blackTakenPieces.sort((p1, p2) -> Ints.compare(p1.getPieceValue(), p2.getPieceValue()));
        for(final Piece takenPiece : whiteTakenPieces) {
            try {
                ImageView logPieceIcon = new ImageView(new Image(new FileInputStream(
                        RESOURCES_PATH +
                                takenPiece.getPieceAlliance().toString() +
                                takenPiece.toString() + ".png")));
                logPieceIcon.setFitWidth(35);
                logPieceIcon.setFitHeight(29.375);
                this.upperPanel.getChildren().add(logPieceIcon);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        for(final Piece takenPiece : blackTakenPieces) {
            try {
                ImageView logPieceIcon = new ImageView(new Image(new FileInputStream(
                        RESOURCES_PATH +
                                takenPiece.getPieceAlliance().toString() +
                                takenPiece.toString() + ".png")));
                logPieceIcon.setFitWidth(35);
                logPieceIcon.setFitHeight(29.375);
                this.lowerPanel.getChildren().add(logPieceIcon);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}