package com.btc.bootcamp.ludo.business;

import com.btc.bootcamp.ludo.business.model.Board;
import com.btc.bootcamp.ludo.business.model.Piece;
import com.btc.bootcamp.ludo.business.model.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BotService {

	@Autowired
	private GameService gameService;

	public Piece determineBestMove(Board board, List<Piece> movablePieces, int diceResult, int startTile) {
		List<Piece> collectPossibilities = new ArrayList<>();
		for (Piece piece : movablePieces) {
			int possiblePosition = gameService.determineNewPiecePosition(piece.getPosition(), diceResult, startTile);
			Optional<Map.Entry<Integer, Player>> any = board.getPlayers().entrySet().stream()
					.filter(e -> e.getValue().getPieces().contains(new Piece(0, possiblePosition)))
					.findAny();
			if (any.isPresent()) {
				collectPossibilities.add(piece);
			}
		}
		if (collectPossibilities.size() > 0) {
			int random = (int) (Math.random() * collectPossibilities.size());
			return collectPossibilities.get(random);
		}
		int random = (int) (Math.random() * movablePieces.size());
		return movablePieces.get(random);
	}

}
