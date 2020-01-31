package com.btc.bootcamp.ludo.business;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    private final int FIELDS_PER_ROUND = 4;

    @InjectMocks
    ConfigurationService configService;

    @InjectMocks
    GameService gameService;

    @Test
    void determineNewPiecePositionMoveHomeToStartTile() {
        assertEquals(Integer.valueOf(1), gameService.determineNewPiecePosition(0, 6,1, FIELDS_PER_ROUND));
    }

    @Test
    void determineNewPiecePositionMove1to5() {
        assertEquals(Integer.valueOf(5), gameService.determineNewPiecePosition(1, 4,1, FIELDS_PER_ROUND));
    }

    @Test
    void determineNewPiecePositionMove39to3() {
        assertEquals(Integer.valueOf(3), gameService.determineNewPiecePosition(39, 4,11, FIELDS_PER_ROUND));
    }

    @Test
    void determineNewPiecePositionMoveMinus1toMinus4() {
        assertEquals(Integer.valueOf(-4), gameService.determineNewPiecePosition(-1, 3,1, FIELDS_PER_ROUND));
    }

    @Test
    void determineNewPiecePositionMoveToGoal() {
        assertEquals(Integer.valueOf(-1), gameService.determineNewPiecePosition(40, 1,1, FIELDS_PER_ROUND));
        assertEquals(Integer.valueOf(-1), gameService.determineNewPiecePosition(39, 2,1, FIELDS_PER_ROUND));
        assertEquals(Integer.valueOf(-2), gameService.determineNewPiecePosition(39, 3,1, FIELDS_PER_ROUND));
        assertEquals(Integer.valueOf(-3), gameService.determineNewPiecePosition(9, 4,11, FIELDS_PER_ROUND));
    }

}