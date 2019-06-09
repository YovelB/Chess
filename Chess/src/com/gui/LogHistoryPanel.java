package com.gui;

import com.engine.board.Board;
import com.engine.board.Move;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.gui.Controller.*;

public class LogHistoryPanel extends Pane {
    private final DateModel model;
    private ScrollPane scrollPane;

    public LogHistoryPanel() {
        this.model = new DateModel();
        TableView table = new TableView(); //model
        table.setPrefHeight(15);
        table.setEditable(false);
        this.scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPrefSize(100, 400);
        scrollPane.setContent(table);
    }

    void add(final Board board, final MoveLog moveLog) {
        int currentRow = 0;
        this.model.clear();
        for(final Move move : moveLog.getMoves()) {
            final String moveText = move.toString();
            if(move.getMovedPiece().getPieceAlliance().isWhite()) {
                //this.model.setValueAt(moveText, currentRow, 0);
            } else if(move.getMovedPiece().getPieceAlliance().isBlack()) {
                //this.model.setValueAt(moveText, currentRow, 1);
                currentRow++;
            }
        }

        if(moveLog.getMoves().size() > 0) {
            final Move lastMove = moveLog.getMoves().get(moveLog.size() - 1);
            final String moveText = lastMove.toString();

            if(lastMove.getMovedPiece().getPieceAlliance().isWhite()) {
                //this.model.setValueAt(moveText + calculateCheckAndCheckMateHash(board), currentRow - 1, 1);
            }

        }
        /*final ScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMax());*/
    }

    private String calculateCheckAndCheckMateHash(final Board board) {
        if(board.getCurrentPlayer().isInCheckMate()) {
            return "#";
        } else if(board.getCurrentPlayer().isInCheck()) {
            return "+";
        }
        return "";
    }

    private static class DateModel {
        private final List<Row> values;
        private static final String[] NAMES = {"White", "Black"};

        DateModel() {
            this.values = new ArrayList<>();
        }

        public void clear() {
            this.values.clear();
            //SetRowConut(0);
        }

        /*@Override
        public int getRowCount() {
            if(this.values == null) {
                return 0;
            }
            return this.values.size();
        }

        @Override
        public int getColumnCount() {
            return NAMES.length;
        }

        @Override
        public Object getValueAt(final int row, final int column) {
            final Row currentRow = this.values.get(row);
            if(column == 0) {
                return currentRow.getWhiteMove();
            } else if(column == 1) {
                return currentRow.getBlackMove();
            }
            return null;
        }

        @Override
        public void setValueAt(final Object value, final int row, final int column) {
            final Row currentRow;
            if(this.values.size() <= row) {
                currentRow = new Row();
                this.values.add(currentRow);
            } else {
                currentRow = this.values.get(row);
            }
            if(column == 0) {
                currentRow.setWhiteMove((String)value);
            } else if(column == 1) {
                currentRow.setBlackMove((String)value);
                fireTableCellUpdated(row, column);
            }
        }

        @Override
        public Class<?> getColumnClass(final int column) {
            return Move.class;
        }

        @Override
        public String getColumnName(final int column) {
            return NAMES[column];
        }*/
    }

    private static class Row {
        private String whiteMove;
        private String blackMove;

        Row() {
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
