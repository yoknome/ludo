package com.btc.bootcamp.ludo.business;

import com.btc.bootcamp.ludo.business.model.Board;
import com.btc.bootcamp.ludo.business.model.Color;
import com.btc.bootcamp.ludo.business.model.Player;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CreationService {

	@Autowired
	private ConfigurationService configService;
	@Autowired
	private IdentitiyGenerationService identitiyGenerationService;

	public Board createNewBoard(Map<Integer, Player> players) {
		long id = identitiyGenerationService.generateId();

		Board board = new Board(id, configService.getDiceSideCount());
		board.getPlayers().putAll(players);
		board.getPath().putAll(createPath());
		board.getColorMap().putAll(createColorMap(board.getPlayers(), board.getPath()));
		board.getStringMap().putAll(createStringMap(board));

		return board;
	}

	public Player createNewPlayer(String name, Color color) {
		long id = identitiyGenerationService.generateId();

		return new Player(id, name, color, configService.getPieceCountPerPlayer());
	}

	private BiMap<Integer, Point> createPath() {
		int pieceCount = configService.getPieceCountPerPlayer(), fieldsPerQuarter = configService.getFieldsPerQuarterRound(), fieldsPerRound = configService.getFieldsPerRound();
		BiMap<Integer, Point> path = HashBiMap.create();
		int x = 0, y = pieceCount;
		path.put(1, new Point(x, y));
		for (int i = 2; i <= fieldsPerRound; i++) {
			if (i <= fieldsPerRound / 2 + 1) {
				if (i > pieceCount + 1 && i <= pieceCount * 2 + 1) {
					y--;
				} else if ((i > fieldsPerQuarter + 1 && i <= fieldsPerQuarter + pieceCount + 1) || i > fieldsPerRound / 2 - 1) {
					y++;
				} else {
					x++;
				}
			} else {
				if (i > fieldsPerQuarter * 2 + 1 + pieceCount && i <= fieldsPerQuarter * 3 - 1) {
					y++;
				} else if ((i > fieldsPerQuarter * 3 + 1 && i <= fieldsPerQuarter * 3 + pieceCount + 1) || i > fieldsPerRound - 1) {
					y--;
				} else {
					x--;
				}
			}
			path.put(i, new Point(x, y));
		}
		return path;
	}

	private Map<Point, Color> createColorMap(Map<Integer, Player> players, BiMap<Integer, Point> path) {
		Map<Point, Color> colorMap = new HashMap<>();
		// homes
		colorMap.put(new Point(0, 0), players.get(1).getColor());
		colorMap.put(new Point(configService.getBoardWidth() - 1, 0), players.get(2).getColor());
		colorMap.put(new Point(configService.getBoardWidth() - 1, configService.getBoardWidth() - 1), players.get(3).getColor());
		colorMap.put(new Point(0, configService.getBoardWidth() - 1), players.get(4).getColor());
		// goals
		for (int i = 0; i < configService.getPieceCountPerPlayer(); i++) {
			colorMap.put(new Point(i + 1, configService.getPieceCountPerPlayer() + 1), players.get(1).getColor());
			colorMap.put(new Point(configService.getPieceCountPerPlayer() + 1, i + 1), players.get(2).getColor());
			colorMap.put(new Point(configService.getBoardWidth() - 2 - i, configService.getPieceCountPerPlayer() + 1), players.get(3).getColor());
			colorMap.put(new Point(configService.getPieceCountPerPlayer() + 1, configService.getBoardWidth() - 2 - i), players.get(4).getColor());
		}
		// path
		colorMap.put(path.get(1), players.get(1).getColor());
		for (int i = 2; i < configService.getFieldsPerRound() + 1; i++) {
			colorMap.put(path.get(i), Color.PATH);
		}
		colorMap.replace(path.get(configService.getFieldsPerQuarterRound() + 1), players.get(2).getColor());
		colorMap.replace(path.get(configService.getFieldsPerQuarterRound() * 2 + 1), players.get(3).getColor());
		colorMap.replace(path.get(configService.getFieldsPerQuarterRound() * 3 + 1), players.get(4).getColor());
		return colorMap;
	}

	private Map<Point, String> createStringMap(Board board) {
		Map<Point, String> stringMap = new HashMap<>();
		for (int i = 1; i <= 4; i++) {
			Color playerColor = board.getPlayers().get(i).getColor();
			List<Point> points = board.getColorMap().entrySet().stream()
					.filter(e -> e.getValue() == playerColor)
					.map(Map.Entry::getKey)
					.collect(Collectors.toList());
			for (Point point : points) {
				if (point.x == 0 ^ point.x == configService.getBoardWidth() - 1 ^ point.y == 0 ^ point.y == configService.getBoardWidth() - 1) {
					stringMap.put(point, configService.getStart());
				} else if (point.x == 0 || point.x == configService.getBoardWidth() - 1 || point.y == 0 || point.y == configService.getBoardWidth() - 1) {
					stringMap.put(point, configService.getBase());
				} else {
					int charValue = 96;
					if (point.x == configService.getPieceCountPerPlayer() + 1) {
						charValue += Math.min(point.y, configService.getBoardWidth() - 1 - point.y);
					} else {
						charValue += Math.min(point.x, configService.getBoardWidth() - 1 - point.x);
					}
					stringMap.put(point, " " + (char) charValue + " ");
				}
			}
		}
		return stringMap;
	}

}
