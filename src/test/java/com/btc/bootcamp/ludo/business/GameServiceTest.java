package com.btc.bootcamp.ludo.business;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    @InjectMocks
    GameService gameService = new GameService();

    @Test
    void determineNewPiecePositionMoveOutOfStart() {
        assertEquals(Integer.valueOf(1), gameService.determineNewPiecePosition(0, 6,1, 40));
    }

    @Test
    void determineNewPiecePositionMove() {
        assertEquals(Integer.valueOf(5), gameService.determineNewPiecePosition(1, 4,1, 40));
        assertEquals(Integer.valueOf(3), gameService.determineNewPiecePosition(39, 4,11, 40));
    }

    @Test
    void determineNewPiecePositionMoveInsideGoal() {
        assertEquals(Integer.valueOf(-4), gameService.determineNewPiecePosition(-1, 3,1, 40));
    }

    @Test
    void determineNewPiecePositionMoveToGoal() {
        assertEquals(Integer.valueOf(-1), gameService.determineNewPiecePosition(40, 1,1, 40));
        assertEquals(Integer.valueOf(-2), gameService.determineNewPiecePosition(39, 3,1, 40));
        assertEquals(Integer.valueOf(-3), gameService.determineNewPiecePosition(9, 4,11, 40));
    }

}