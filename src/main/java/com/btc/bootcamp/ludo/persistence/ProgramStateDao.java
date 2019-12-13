package com.btc.bootcamp.ludo.persistence;

import java.util.HashSet;
import java.util.Set;

import com.btc.bootcamp.ludo.business.model.Board;
import org.springframework.stereotype.Service;

@Service
public class ProgramStateDao {

    private final Set<Board> boards;

    // Hier könnt ihr euch Dinge für euer Spiel speichern bzw. den Zustand merken

    private ProgramStateDao() {
        this.boards = new HashSet<>();
    }

    public void addBoard(Board board) {
        boards.add(board);
    }

    public Set<Board> getBoards() {
        return boards;
    }
}
