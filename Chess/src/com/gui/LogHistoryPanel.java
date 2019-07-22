package com.gui;

import com.engine.board.Board;
import com.engine.board.Move;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

class LogHistoryPanel extends Pane {
    private final TableView table;
    private String whiteMove;
    private int currentRowIndex;

    LogHistoryPanel() {
        table = new TableView();
        table.setPrefSize(175, 550);
        table.setEditable(false);
        TableColumn<String, Row> whiteCol = new TableColumn<>("White");
        whiteCol.setCellValueFactory(new PropertyValueFactory<>("whiteMove"));
        whiteCol.setResizable(false);
        TableColumn<String, Row> blackCol = new TableColumn<>("Black");
        blackCol.setCellValueFactory(new PropertyValueFactory<>("blackMove"));
        blackCol.setResizable(false);
        table.getColumns().addAll(whiteCol, blackCol);
        this.getChildren().add(table);
        whiteMove = "";
        currentRowIndex = 0;

    }

    void draw(final Board board, final Move move) {
        String moveText = move.toString() + calculateCheckAndCheckMateHash(board);
        if (move.getMovedPiece().getPieceAlliance().isWhite()) {
            whiteMove = moveText;
            table.getItems().add(new Row(moveText, ""));
        } else if (move.getMovedPiece().getPieceAlliance().isBlack()) {
            table.getItems().set(currentRowIndex, new Row(whiteMove, moveText));
            currentRowIndex++;
        }
    }

    private String calculateCheckAndCheckMateHash(final Board board) {
        if(board.getCurrentPlayer().isInCheckMate()) {
            return "#";
        } else if(board.getCurrentPlayer().isInCheck()) {
            return "+";
        }
        return "";
    }

    void clear() {
        whiteMove = "";
        this.currentRowIndex = 0;
        this.table.getItems().clear();
    }

    public static class Row {
        private String whiteMove;
        private String blackMove;

        Row(final String whiteMove, final String blackMove) {
            this.whiteMove = whiteMove;
            this.blackMove = blackMove;
        }

        public String getWhiteMove() {
            return whiteMove;
        }

        public void setWhiteMove(String whiteMove) {
            this.whiteMove = whiteMove;
        }

        public String getBlackMove() {
            return blackMove;
        }

        public void setBlackMove(String blackMove) {
            this.blackMove = blackMove;
        }
    }
}