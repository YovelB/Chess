package com.gui;

import com.engine.board.*;

import com.engine.pieces.Piece;
import com.engine.player.MoveTransition;
import com.google.common.collect.Lists;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;


/**
 * Controller class that Controls the Board of the game (GUI Implantation)
 */

public class Controller implements Initializable {

    private static final int BOARD_PANEL_WIDTH = 600;
    private static final int BOARD_PANEL_HEIGHT = 500;
    private static final int TILE_PANEL_WIDTH = 75;
    private static final int TILE_PANEL_HEIGHT = 65;
    static final String RESOURCES_PATH = "Resources\\";

    @FXML
    private BorderPane borderPane;


    Board gameBoard = Board.createStandardBoard();
    private Tile sourceTile;
    private Tile targetTile;
    private Piece movedPiece;
    private BoardPanel chessBoard;
    private BoardDirection boardDirection;

    private boolean highlightLegalMoves;

    /**
     * Initialize method that will create all the objects on the board before starting the game
     *
     * The method iterates on all the GridPane nodes and creating new Panes
     * with different colors(BLACK, WHITE - DARK BROWN, WHITE BROWN)
     * On each Pane we create a ImageView control that has an Image of EmptyPiece(no Image, transparent) or a piece image
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boardDirection = BoardDirection.NORMAL;
        highlightLegalMoves = false;

        MenuBar menuBar = createTableMenuBar();
        borderPane.setTop(menuBar);

        /*TakenPiecesPanel logPanel = new TakenPiecesPanel();
        borderPane.setLeft(logPanel);*/

        chessBoard = new BoardPanel();
        borderPane.setCenter(chessBoard);
    }

    public enum BoardDirection {
        NORMAL {
            @Override
            List<TilePanel> traverse(final List<TilePanel> boardTiles) {
                return boardTiles;
            }

            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }
        },
        FLIPPED {
            @Override
            List<TilePanel> traverse(List<TilePanel> boardTiles) {
                return Lists.reverse(boardTiles);
            }

            @Override
            BoardDirection opposite() {
                return NORMAL;
            }
        };

        abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);

        abstract BoardDirection opposite();
    }

    private MenuBar createTableMenuBar() {
        final MenuBar tableMenuBar = new MenuBar();
        tableMenuBar.getMenus().add(createFileMenu());
        tableMenuBar.getMenus().add(createPreferencesMenu());
        return tableMenuBar;
    }

    private Menu createFileMenu() {
        final Menu fileMenu = new Menu("File");
        MenuItem openPGNMenuItem = createMenuItem("Load PGN file",
                e -> System.out.println("open up that PGN file!"));
        fileMenu.getItems().add(openPGNMenuItem);
        final MenuItem exitMenuItem = createMenuItem("Exit",
                e -> System.exit(0));
        fileMenu.getItems().add(exitMenuItem);
        return fileMenu;
    }

    private Menu createPreferencesMenu() {
        final Menu preferencesMenu = new Menu("Preferences");
        MenuItem flipBoardMenuItem = createMenuItem("Flip Board", e -> {
            boardDirection = boardDirection.opposite();
            chessBoard.drawBoard(gameBoard);
        });
        preferencesMenu.getItems().add(flipBoardMenuItem);
        preferencesMenu.getItems().add(new SeparatorMenuItem());
        CheckMenuItem legalMoveHighlighterCheckbox = new CheckMenuItem("Highlight legal Moves");
        legalMoveHighlighterCheckbox.setSelected(false);
        legalMoveHighlighterCheckbox.setOnAction(e -> highlightLegalMoves = legalMoveHighlighterCheckbox.isSelected());
        preferencesMenu.getItems().add(legalMoveHighlighterCheckbox);
        return preferencesMenu;
    }

    private MenuItem createMenuItem(final String ItemTitle, final EventHandler<ActionEvent> eventHandler) {
        final MenuItem openPGNMenuItem = new MenuItem(ItemTitle);
        openPGNMenuItem.setOnAction(eventHandler);
        return openPGNMenuItem;
    }

    private class BoardPanel extends GridPane {
        final List<TilePanel> boardTiles;

        public BoardPanel() {
            this.setPrefSize(BOARD_PANEL_WIDTH, BOARD_PANEL_HEIGHT);
            this.boardTiles = new ArrayList<>(BoardUtils.NUM_TILES);
            for(int i = 0; i < BoardUtils.NUM_TILES_PER_ROW; i++) {
                for(int j = 0, tileId; j < BoardUtils.NUM_TILES_PER_ROW; j++) {
                    tileId = i * BoardUtils.NUM_TILES_PER_ROW + j;
                    final TilePanel tilePanel = new TilePanel(this, tileId);
                    this.boardTiles.add(tilePanel);
                    this.add(tilePanel, j, i);
                }
            }
        }

        public void drawBoard(final Board board) {
            this.getChildren().clear();
            for(int i = 0; i < BoardUtils.NUM_TILES_PER_ROW; i++) {
                for(int j = 0, tileId; j < BoardUtils.NUM_TILES_PER_ROW; j++) {
                    tileId = i * BoardUtils.NUM_TILES_PER_ROW + j;
                    boardDirection.traverse(boardTiles).get(tileId).drawTile(board);
                    this.add(boardDirection.traverse(boardTiles).get(tileId), j, i);
                }
            }
        }
    }

    public static class MoveLog {
        private final List<Move> moves;

        MoveLog() {
            this.moves = new ArrayList<>();
        }

        public List<Move> getMoves() {
            return this.moves;
        }

        public void addMove(final Move move) {
            this.moves.add(move);
        }

        public int size() {
            return this.moves.size();
        }

        public void clear() {
            this.moves.clear();
        }

        public Move removeMove(final int index) {
            return this.moves.remove(index);
        }

        public boolean removeMove(final Move move) {
            return this.moves.remove(move);
        }
    }

    private class TilePanel extends Pane {
        private final int tileId;

        private TilePanel(final BoardPanel boardPanel, final int tileId) {
            this.setPrefSize(TILE_PANEL_WIDTH, TILE_PANEL_HEIGHT);
            this.tileId = tileId;
            assignTileColor();
            assignTilePieceIcon(gameBoard);
            EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {

                @Override
                public void handle(final MouseEvent mouseEvent) {
                    if(mouseEvent.getButton() == MouseButton.SECONDARY) {
                        sourceTile = null;
                        targetTile = null;
                        movedPiece = null;
                    } else if(mouseEvent.getButton() == MouseButton.PRIMARY) {
                        if(sourceTile == null) {
                            //first click
                            sourceTile = gameBoard.getTile(tileId);
                            movedPiece = sourceTile.getPiece();
                            if(movedPiece == null) {
                                sourceTile = null;
                            }
                        } else {
                            // second click
                            targetTile = gameBoard.getTile(tileId);
                            final Move move = Move.MoveFactory.createMove(gameBoard, sourceTile.getTileCoordinate(),
                                    targetTile.getTileCoordinate());
                            final MoveTransition transition = gameBoard.getCurrentPlayer().makeMove(move);
                            if(transition.getMoveStatus().isDone()) {
                                gameBoard = transition.getTransitionBoard();
                                System.out.println("piece " + movedPiece + " Moved from " + sourceTile.getTileCoordinate()  +
                                        " to " + targetTile.getTileCoordinate());
                                //TODO add the move that was made to the move log
                            }
                            sourceTile = null;
                            targetTile = null;
                            movedPiece = null;
                        }
                        Platform.runLater(() -> boardPanel.drawBoard(gameBoard));
                    }
                    System.out.println("I clicked tileId =  " + (tileId+1));
                }
            };
            this.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        }

        public void drawTile(final Board board) {
            assignTileColor();
            assignTilePieceIcon(board);
            highlightLegals(board);
        }

        private void assignTilePieceIcon(final Board gameBoard) {
            this.getChildren().clear();
            ImageView tilePieceIcon = null;
            if(gameBoard.getTile(this.tileId).isTileOccupied()) {
                final Piece tilePiece = gameBoard.getTile(this.tileId).getPiece();
                try {
                    tilePieceIcon = new ImageView(new Image(new FileInputStream(
                            RESOURCES_PATH +
                                    tilePiece.getPieceAlliance().toString() +
                                    tilePiece.toString() + ".png")));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                tilePieceIcon = new ImageView();
            }
            if (tilePieceIcon != null) {
                tilePieceIcon.setFitWidth(TILE_PANEL_WIDTH);
                tilePieceIcon.setFitHeight(TILE_PANEL_HEIGHT);
                this.getChildren().add(tilePieceIcon);
            }
        }

        private void highlightLegals(final Board board) {
            if(highlightLegalMoves) {
                for(final Move move : pieceLegalMoves(board)) {
                    if(move.getDestinationCoordinate() == this.tileId) {
                        ImageView tilePieceIcon = null;
                        try {
                            tilePieceIcon = new ImageView(new Image(new FileInputStream(
                                    RESOURCES_PATH + "green_dot.png")));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        if (tilePieceIcon != null) {
                            tilePieceIcon.setFitWidth(20);
                            tilePieceIcon.setFitHeight(20);
                            this.getChildren().add(tilePieceIcon);
                        }
                    }
                }
            }
        }

        private Collection<Move> pieceLegalMoves(final Board board) {
            if(movedPiece != null && movedPiece.getPieceAlliance() == board.getCurrentPlayer().getAlliance()) {
                return movedPiece.calculateLegalMoves(board);
            }
            return Collections.emptyList();
        }

        private void assignTileColor() {
            if((this.tileId + this.tileId / 8) % 2 == 0) {
                this.setStyle("-fx-background-color : rgb(255, 222, 173)");
            } else {
                this.setStyle("-fx-background-color : rgb(244, 164, 96)");
            }
        }
    }
}