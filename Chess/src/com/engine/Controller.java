package com.engine;

import com.engine.board.*;
import com.engine.board.BoardUtils.*;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class that Controls the Board of the game (GUI Implantation)
 * @author yovel
 */

public class Controller implements Initializable
{
    @FXML GridPane gridPane;

    /**
     * Initialize method that will create all the objects on the board before starting the game
     * ranks = rows, files = columns
     *
     * The method iterates on all the GridPane nodes and creating new Panes with different colors (BLACK, WHITE - DARK BROWN, WHITE BROWN)
     * On each Pane we create a ImageView control that has an Image of EmptyPiece(no Image, transparent) or a piece image
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final String[] strImages = {"wp", "wr", "wn", "wb", "wq", "wk", "bp", "br", "bn", "bb", "bq", "bk"};

        Image[] image = new Image[12];

        for (int i = 0; i < 12; i++) {
            try {
                image[i] = new Image(new FileInputStream("C:\\Users\\yovel\\IdeaProjects\\Chess\\Chess\\Resources\\" + strImages[i] + ".png"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        Board gameBoard = new Board();
        ImageView[][] imageViewArray = new ImageView[8][8];
        loadPieceImages(image, gameBoard, imageViewArray);
        boolean flag = true;
        for (int i = 0; i < 8; i++) {
            for (int file = 7, j = 0; file >= 0; file-- ,j++) {
                Pane pane = new Pane();
                pane.getChildren().add(imageViewArray[i][file]);

                if (gameBoard.getTile(i, j).getColor() == Color.WHITE)
                    pane.setStyle("-fx-background-color : rgb(255, 222, 173)");
                else
                    pane.setStyle("-fx-background-color : rgb(244, 164, 96)");
                gridPane.add(pane, i, j);
                flag = !flag;
            }
            flag = !flag;
        }

    }

    private void loadPieceImages(Image[] image, Board gameBoard, ImageView[][] imageViewArray) {
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 7; file >= 0; file--) {
                switch (gameBoard.getTile(rank, file).getPiece().getType()) {
                    case PAWN:
                        switch (gameBoard.getTile(rank, file).getPiece().getColor()) {
                            case WHITE:
                                imageViewArray[rank][file] = new ImageView(image[0]);
                                break;
                            case BLACK:
                                imageViewArray[rank][file] = new ImageView(image[6]);
                                break;
                        }
                        break;
                    case ROOK:
                        switch (gameBoard.getTile(rank, file).getPiece().getColor()) {
                            case WHITE:
                                imageViewArray[rank][file] = new ImageView(image[1]);
                                break;
                            case BLACK:
                                imageViewArray[rank][file] = new ImageView(image[7]);
                                break;
                        }
                        break;
                    case KNIGHT:
                        switch (gameBoard.getTile(rank, file).getPiece().getColor()) {
                            case WHITE:
                                imageViewArray[rank][file] = new ImageView(image[2]);
                                break;
                            case BLACK:
                                imageViewArray[rank][file] = new ImageView(image[8]);
                                break;
                        }
                        break;
                    case BISHOP:
                        switch (gameBoard.getTile(rank, file).getPiece().getColor()) {
                            case WHITE:
                                imageViewArray[rank][file] = new ImageView(image[3]);
                                break;
                            case BLACK:
                                imageViewArray[rank][file] = new ImageView(image[9]);
                                break;
                        }
                        break;
                    case QUEEN:
                        switch (gameBoard.getTile(rank, file).getPiece().getColor()) {
                            case WHITE:
                                imageViewArray[rank][file] = new ImageView(image[4]);
                                break;
                            case BLACK:
                                imageViewArray[rank][file] = new ImageView(image[10]);
                                break;
                        }
                        break;
                    case KING:
                        switch (gameBoard.getTile(rank, file).getPiece().getColor()) {
                            case WHITE:
                                imageViewArray[rank][file] = new ImageView(image[5]);
                                break;
                            case BLACK:
                                imageViewArray[rank][file] = new ImageView(image[11]);
                                break;
                        }
                        break;
                    case EMPTY:
                        imageViewArray[rank][file] = new ImageView();
                        break;
                }
                //figure out if these lines do anything
                imageViewArray[rank][file].setFitWidth(BoardUtils.tileWidth);
                imageViewArray[rank][file].setFitHeight(BoardUtils.tileHeight);
            }
        }
    }
}
