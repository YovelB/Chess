package com.gui;

import com.engine.Alliance;
import com.engine.board.Board;
import com.engine.board.BoardUtils;
import com.engine.board.Move;
import com.engine.board.Tile;
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

import javax.microedition.io.StreamConnection;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * GameController class that Controls the Board of the game (GUI Implantation)
 */

public class GameController implements Initializable {
    static final String RESOURCES_PATH = "Resources\\";
    private static final int BOARD_PANEL_WIDTH = 650;
    private static final int BOARD_PANEL_HEIGHT = 550;
    private static final double TILE_PANEL_WIDTH = 81.25;
    private static final double TILE_PANEL_HEIGHT = 68.75;

    private DataOutputStream dataOut;
    private StreamConnection connection;

    @FXML
    private BorderPane borderPane;

    private LogHistoryPanel logHistoryPanel;
    private TakenPiecesPanel takenPiecesPanel;
    private BoardPanel chessBoard;
    private MoveLog moveLog;

    private Board gameBoard;
    private Tile sourceTile;
    private Tile targetTile;
    private Piece movedPiece;
    private BoardDirection boardDirection;
    private boolean highlightTiles;
    private Alliance chosenAlliance;

    /**
     * Initialize method that will create all the objects on the board before starting the game
     *
     * The method iterates on all the GridPane nodes and creating new Panes
     * with different colors(BLACK, WHITE - DARK BROWN, WHITE BROWN)
     * On each Pane we create a ImageView control that has an Image of EmptyPiece(no Image, transparent) or a piece image
     * @param url draw text here
     * @param resourceBundle draw text here
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.connection = PrimaryController.getBluetoothConnection();
        this.dataOut = PrimaryController.getDataOutputStream();

        // Starts the listening service for incoming messages.
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(this::receivedMoveMessage);

        chosenAlliance = PrimaryController.getChosenPlayerAlliance();

        if(chosenAlliance == Alliance.WHITE) {
            this.boardDirection = BoardDirection.NORMAL;
        } else {
            this.boardDirection = BoardDirection.FLIPPED;
        }
        highlightTiles = true;

        this.gameBoard = Board.createStandardBoard();
        MenuBar menuBar = createTableMenuBar();
        this.moveLog = new MoveLog();
        this.logHistoryPanel = new LogHistoryPanel();
        this.takenPiecesPanel = new TakenPiecesPanel();
        this.chessBoard = new BoardPanel();
        this.chessBoard.drawBoard(gameBoard);

        this.borderPane.setTop(menuBar);
        this.borderPane.setRight(this.logHistoryPanel);
        this.borderPane.setLeft(this.takenPiecesPanel);
        this.borderPane.setCenter(this.chessBoard);
    }

    private void resetGame() {
        if(chosenAlliance == Alliance.WHITE) {
            this.boardDirection = BoardDirection.NORMAL;
        } else {
            this.boardDirection = BoardDirection.FLIPPED;
        }
        highlightTiles = true;

        this.gameBoard = Board.createStandardBoard();
        moveLog.clear();

        Platform.runLater(() -> {
            logHistoryPanel.clear();
            takenPiecesPanel.draw(moveLog);
            this.chessBoard.drawBoard(gameBoard);
        });
    }

    private void receivedMoveMessage() {
        InputStream inputStream;
        try {
            inputStream = connection.openInputStream();
        } catch (IOException e) {
            System.err.println("Listening service failed. Incoming messages won't be displayed.");
            e.printStackTrace();
            return;
        }
        DataInputStream dataInput = new DataInputStream(inputStream);
        String incomingMessage = "";
        while (true) {
            int currentCoordinate = -1;
            int destinationCoordinate = -1;
            try {
                incomingMessage = dataInput.readUTF();
            } catch (IOException e) {
                System.out.println("Error while reading the incoming message.");
                e.printStackTrace();
            }
            if(incomingMessage.length() >= 3) {
                if (incomingMessage.length() == 3) {
                    currentCoordinate = Integer.parseInt(incomingMessage.substring(0, 1));
                    destinationCoordinate = Integer.parseInt(incomingMessage.substring(2, 3));
                } else if (incomingMessage.length() == 4) {
                    currentCoordinate = Integer.parseInt(incomingMessage.substring(0, incomingMessage.indexOf(" ")));
                    destinationCoordinate = Integer.parseInt(incomingMessage.substring(incomingMessage.indexOf(" ") + 1, 4));
                } else if (incomingMessage.length() == 5) {
                    if (!incomingMessage.equals("Reset") && !incomingMessage.equals("Start")) {
                        currentCoordinate = Integer.parseInt(incomingMessage.substring(0, 2));
                        destinationCoordinate = Integer.parseInt(incomingMessage.substring(3, 5));
                    }
                }
                if (incomingMessage.equals("Reset")) {
                    resetGame();
                } else if (!incomingMessage.equals("Start")) {
                    final Move move = Move.MoveFactory.createMove(gameBoard,
                            currentCoordinate, destinationCoordinate);
                    final MoveTransition transition = gameBoard.getCurrentPlayer().makeMove(move);
                    if (transition.getMoveStatus().isDone()) {
                        gameBoard = transition.getTransitionBoard();
                        moveLog.addMove(move);
                        Platform.runLater(() -> {
                            logHistoryPanel.draw(gameBoard, move);
                            takenPiecesPanel.draw(moveLog);
                            chessBoard.drawBoard(gameBoard);
                        });
                    }
                }
            }
        }
    }

    private MenuBar createTableMenuBar() {
        final MenuBar tableMenuBar = new MenuBar();
        tableMenuBar.getMenus().add(createFileMenu());
        tableMenuBar.getMenus().add(createPreferencesMenu());
        return tableMenuBar;
    }

    private Menu createFileMenu() {
        final Menu fileMenu = new Menu("File");
        final MenuItem reset = createMenuItem("New Game", e -> {
            resetGame();
            try {
                dataOut.writeUTF("Reset");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        final MenuItem exitMenuItem = createMenuItem("Exit", e ->  {
            try {
                dataOut.close();
                PrimaryController.getBluetoothConnection().close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.exit(0);
        });
        fileMenu.getItems().add(reset);
        fileMenu.getItems().add(new SeparatorMenuItem());
        fileMenu.getItems().add(exitMenuItem);
        return fileMenu;
    }

    private Menu createPreferencesMenu() {
        final Menu preferencesMenu = new Menu("Preferences");
        MenuItem flipBoardMenuItem = createMenuItem("Flip Board", e -> {
            this.boardDirection = this.boardDirection.opposite();
            this.chessBoard.drawBoard(this.gameBoard);
        });
        CheckMenuItem highlightTilesMenuItem = new CheckMenuItem("Highlight Tiles");
        highlightTilesMenuItem.setOnAction(e -> this.highlightTiles = !this.highlightTiles);
        highlightTilesMenuItem.setSelected(true);
        preferencesMenu.getItems().add(flipBoardMenuItem);
        preferencesMenu.getItems().add(new SeparatorMenuItem());
        preferencesMenu.getItems().add(highlightTilesMenuItem);
        return preferencesMenu;
    }

    private MenuItem createMenuItem(final String itemTitle, final EventHandler<ActionEvent> eventHandler) {
        final MenuItem menuItem = new MenuItem(itemTitle);
        menuItem.setOnAction(eventHandler);
        return menuItem;
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

    public static class MoveLog {
        private final List<Move> moves;

        MoveLog() {
            this.moves = new ArrayList<>();
        }

        public List<Move> getMoves() {
            return this.moves;
        }

        void addMove(final Move move) {
            this.moves.add(move);
        }

        void clear() {
            this.moves.clear();
        }
    }

    class BoardPanel extends GridPane {

        final List<TilePanel> boardTiles;
        BoardPanel() {
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
        void drawBoard(final Board board) {
            this.getChildren().removeAll(this.getChildren());
            for(int i = 0; i < BoardUtils.NUM_TILES_PER_ROW; i++) {
                for(int j = 0, tileId; j < BoardUtils.NUM_TILES_PER_ROW; j++) {
                    tileId = i * BoardUtils.NUM_TILES_PER_ROW + j;
                    boardDirection.traverse(boardTiles).get(tileId).drawTile(board);
                    this.add(boardDirection.traverse(boardTiles).get(tileId), j, i);
                }
            }
        }
    }

    public class TilePanel extends Pane {
        private final int tileId;
        private boolean highlighted;

        private TilePanel(final BoardPanel boardPanel, final int tileId) {
            this.setPrefSize(TILE_PANEL_WIDTH, TILE_PANEL_HEIGHT);
            highlighted = false;
            this.tileId = tileId;
            assignTileColor();
            assignTilePieceIcon(gameBoard);

            EventHandler<MouseEvent> eventHandlerForMouseClick = mouseEvent -> {
                if(chosenAlliance == gameBoard.getCurrentPlayer().getAlliance()) {
                    if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                        sourceTile = null;
                        targetTile = null;
                        movedPiece = null;
                    } else if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                        if (sourceTile == null) {
                            //first click
                            sourceTile = gameBoard.getTile(tileId);
                            movedPiece = sourceTile.getPiece();
                            if (movedPiece == null) {
                                sourceTile = null;
                            }
                        } else {
                            // second click
                            targetTile = gameBoard.getTile(tileId);
                            final Move move = Move.MoveFactory.createMove(gameBoard, sourceTile.getTileCoordinate(),
                                    targetTile.getTileCoordinate());
                            final MoveTransition transition = gameBoard.getCurrentPlayer().makeMove(move);
                            if (transition.getMoveStatus().isDone()) {
                                gameBoard = transition.getTransitionBoard();
                                moveLog.addMove(move);
                                logHistoryPanel.draw(gameBoard, move);
                                try {
                                    dataOut.writeUTF(move.getCurrentCoordinate() + " " + move.getDestinationCoordinate());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            sourceTile = null;
                            targetTile = null;
                            movedPiece = null;
                        }
                        Platform.runLater(() -> {
                            takenPiecesPanel.draw(moveLog);
                            boardPanel.drawBoard(gameBoard);
                        });
                    }
                }
            };

            Pane HoveredPane = new Pane();
            this.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
                if(this.isHighlighted()) {
                    HoveredPane.setPrefSize(TILE_PANEL_WIDTH, TILE_PANEL_HEIGHT);
                    HoveredPane.setStyle("-fx-background-color: rgba(38, 127, 0, 0.5)");
                    this.getChildren().add(HoveredPane);
                }
            });

            this.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
                drawTile(gameBoard);
                this.getChildren().remove(HoveredPane);
            });

            this.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandlerForMouseClick);
        }

        boolean isHighlighted() {
            return highlighted;
        }

        void drawTile(final Board board) {
            this.getChildren().clear();
            assignTileColor();
            highlightSelection(board);
            assignTilePieceIcon(board);
        }

        private void assignTilePieceIcon(final Board gameBoard) {
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

        private void highlightSelection(final Board gameBoard) {
            this.getChildren().clear();
            this.highlighted = false;
            if (highlightTiles) {
                for (final Move move : pieceLegalMoves(gameBoard)) {
                    if (move.getCurrentCoordinate() == this.tileId) {
                        this.setStyle("-fx-background-color : rgb(38, 127, 0)");
                    }
                    if (move.getDestinationCoordinate() == this.tileId) {
                        String highlightedTile = "HighlightedTile.png";
                        this.highlighted = true;
                        ImageView highlightedDestinationImg = null;
                        if (move.isAttack()) {
                            highlightedTile = "HighlightedAttackTile.png";
                        }
                        try {
                            highlightedDestinationImg = new ImageView(new Image(new FileInputStream(
                                    RESOURCES_PATH + highlightedTile)));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        if (highlightedDestinationImg != null) {
                            highlightedDestinationImg.setFitWidth(TILE_PANEL_WIDTH);
                            highlightedDestinationImg.setFitHeight(TILE_PANEL_HEIGHT);
                            this.getChildren().add(highlightedDestinationImg);
                        }
                    }
                }
            }
        }

        private Collection<Move> pieceLegalMoves(final Board gameBoard) {
            if(movedPiece != null && movedPiece.getPieceAlliance() == gameBoard.getCurrentPlayer().getAlliance()) {
                return movedPiece.calculateLegalMoves(gameBoard);
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