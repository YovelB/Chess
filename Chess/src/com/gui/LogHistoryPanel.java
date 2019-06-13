package com.gui;

import com.engine.board.Board;
import com.engine.board.Move;
import javafx.collections.ObservableList;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

import static com.gui.Controller.*;

public class LogHistoryPanel extends Pane {
    private TableView table;
    private String whiteMove = null;
    private int currentRow = 0;
    public LogHistoryPanel() {

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

    }

    void add(final Board board, final Move move) {
        final String moveText = move.toString() + calculateCheckAndCheckMateHash(board);
        if(move.getMovedPiece().getPieceAlliance().isWhite()) {
            whiteMove = moveText;
            table.getItems().add(new Row(moveText, ""));
        } else if(move.getMovedPiece().getPieceAlliance().isBlack())
        {
            table.getItems().set(currentRow, new Row(whiteMove, moveText));
            currentRow++;
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

    public static class Row {
        private String whiteMove;
        private String blackMove;

        public Row(final String whiteMove, final String blackMove) {
            this.whiteMove = whiteMove;
            this.blackMove = blackMove;
        }

        public String getWhiteMove() {
            return this.whiteMove;
        }

        public String getBlackMove() {
            return this.blackMove;
        }

        public void setWhiteMove(final String whiteMove) {
            this.whiteMove = whiteMove;
        }
        public void setBlackMove(final String blackMove) {
            this.blackMove = blackMove;
        }
    }
}