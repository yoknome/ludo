package com.btc.bootcamp.ludo.business;

import com.btc.bootcamp.ludo.business.model.Board;
import com.btc.bootcamp.ludo.business.model.Dice;
import com.btc.bootcamp.ludo.business.model.Piece;
import com.btc.bootcamp.ludo.business.model.Player;
import com.btc.bootcamp.ludo.presentation.PresentationService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {

	private static Logger LOG = LoggerFactory.getLogger(GameService.class);

	@Autowired
	private ConfigurationService configService;
	@Autowired
	private BotService botService;
	@Autowired
	private PieceService pieceService;
	@Autowired
	private PresentationService presentationService;
	@Autowired
	private PromptingService promptingService;

	public void play(Board board) {
		presentationService.print(board.getPlayers());
		presentationService.print(board);
		boolean won = false;
		do {
			for (Map.Entry<Integer, Player> playerEntry : board.getPlayers().entrySet()) {
				if (board.getFinishedPlayers().size() == 4) {
					won = true;
					continue;
				} else if (board.getFinishedPlayers().contains(playerEntry.getValue())) {
					continue;
				}
				playerTurn(board, playerEntry.getKey());
//				promptingService.pressEnterToContinue();
			}
		} while (!won);
	}

	private void playerTurn(Board board, final int playerKey) {
		Player player = board.getPlayers().get(playerKey);
		Dice dice = board.getDice();
		int startTile = (playerKey - 1) * configService.getFieldsPerQuarterRound() + 1;
		do {
			roll(player, board.getDice());
			if (!dice.getResult().equals(dice.getSideCount()) && player.getBasePieceCount() == player.getPieces().size()) {
				LOG.info(player.getColoredName() + " didn't make it out of the base");
				return;
			}
			List<Piece> movablePieces = determineMovablePieceList(player, board.getDice(), startTile);
			if (movablePieces.size() == 0) {
				LOG.info(player.getColoredName() + " has no valid moves.");
				return;
			}
			Piece piece;
			if (board.getIsBot().get(playerKey)) {
				piece = botService.determineBestMove(board, movablePieces, dice.getResult(), startTile);
				LOG.info(player.getColoredName() + " moves piece #" + piece.getId());
			} else {
				piece = promptingService.choosePiece(movablePieces);
			}
			int newPiecePosition = determineNewPiecePosition(piece.getPosition(), dice.getResult(), startTile);
			if (newPiecePosition > 0) {
				throwIfOccupied(board.getPlayers(), playerKey, newPiecePosition);
			}

			piece.setPosition(newPiecePosition);
			presentationService.print(board);
			if (pieceService.allOnGoals(player.getPieces())) {
				board.getFinishedPlayers().add(player);
				LOG.info(player.getColoredName() + " brought all pieces to the target area!");
				LOG.info(player.getColoredName() + " finished " + board.getFinishedPlayers().size() + ".!");
				promptingService.pressEnterToContinue();
			}
		} while (dice.getResult().equals(dice.getSideCount()));
	}

	private List<Piece> determineMovablePieceList(Player player, Dice dice, Integer startTile) {
		boolean hasHomePieces = player.getBasePieceCount() > 0, hasHighestResult = dice.getResult().equals(dice.getSideCount());
		return player.getPieces().stream()
				.filter(piece -> (!pieceService.getPieceOnPosition(player.getPieces(), determineNewPiecePosition(piece.getPosition(), dice.getResult(), startTile)).isPresent() // don't land on a your on pieces
						&& determineNewPiecePosition(piece.getPosition(), dice.getResult(), startTile) >= -configService.getPieceCountPerPlayer() // exclude illegal positions
						&& ((hasHighestResult && player.getBasePieceCount() == configService.getPieceCountPerPlayer())
								|| (hasHighestResult && hasHomePieces && piece.getPosition().equals(0))
								|| !piece.getPosition().equals(0))))
				.collect(Collectors.toList());
	}

	Integer determineNewPiecePosition(Integer oldPosition, int diceResult, int startTile) {
		if (oldPosition == 0) {
			return startTile;
		} else if (oldPosition < 0) {
			return oldPosition - diceResult;
		}
		int newPosition = oldPosition + diceResult;
		if (startTile == 1 && newPosition > configService.getFieldsPerRound()) {
			newPosition %= configService.getFieldsPerRound();
			return 0 - newPosition;
		} else if (oldPosition < startTile && newPosition >= startTile) {
			return 0 - (startTile - newPosition);
//			return oldPosition - newPosition;
		} else {
			return newPosition;
		}
	}

	private void throwIfOccupied(Map<Integer, Player> playerMap, int playerKey, int targetTile) {
		Map<Integer, Piece> opponentKeyAndPiece = playerMap.entrySet().stream()
				.filter(entry -> pieceService.getPieceOnPosition(entry.getValue().getPieces(), targetTile).isPresent())
				.collect(Collectors.toMap(Map.Entry::getKey, playerMapEntry -> pieceService.getPieceOnPosition(playerMapEntry.getValue().getPieces(), targetTile).get()));
		for (Integer opponentKey : opponentKeyAndPiece.keySet()) {
			opponentKeyAndPiece.get(opponentKey).setPosition(0);
			LOG.info(playerMap.get(playerKey).getColoredName() + " send " + playerMap.get(opponentKey).getColoredName() + "'s Piece #" + opponentKeyAndPiece.get(opponentKey).getId() + " back in its base.");
		}
	}

	private void roll(Player player, Dice dice) {
		int maxRolls = 1, rollCount = 1;
		if (player.getBasePieceCount().equals(player.getPieces().size()) && player.getPieces().size() != 0) {
			maxRolls = 3;
		}
		do {
			dice.roll();
			if (dice.getResult().equals(dice.getSideCount()) || maxRolls == 1) {
				LOG.info(player.getColoredName() + " rolled a " + dice.getResult() + "!");
			} else {
				LOG.info(player.getColoredName() + "'s " + rollCount + ". roll: " + dice.getResult() + ". ");
			}
			rollCount++;
		} while (rollCount <= maxRolls && !dice.getResult().equals(dice.getSideCount()));
	}

}