package com.btc.bootcamp.ludo.presentation;

import com.btc.bootcamp.ludo.business.ConfigurationService;
import com.btc.bootcamp.ludo.business.PieceService;
import com.btc.bootcamp.ludo.business.model.Board;
import com.btc.bootcamp.ludo.business.model.Color;
import com.btc.bootcamp.ludo.business.model.Player;
import java.awt.Point;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PresentationService {

	private static Logger LOG = LoggerFactory.getLogger(PresentationService.class);

	@Autowired
	private ConfigurationService configService;
	@Autowired
	private PieceService pieceService;

	public void print(Map<Integer, Player> players) {
		LOG.info("Players: " + players.size());
		for (int i = 1; i <= 4; i++) {
			LOG.info(players.get(i).getColoredName());
		}
	}

	public void print(Board board) {
		LOG.info("Game board:\n" + buildBoardString(board));
	}

	private String buildBoardString(Board board) {
		StringJoiner rows = new StringJoiner("\n");
		String row, outlineRow = "";
		Point point = new Point();
		for (int y = 0; y < configService.getBoardWidth(); y++) {
			point.y = y;
			row = Color.FIELD.getBgFg() + " ║ ";
			outlineRow += "═══";
			for (int x = 0; x < configService.getBoardWidth(); x++) {
				point.x = x;
				row += board.getColorMap().getOrDefault(point, Color.FIELD).getBgFg();
				if (board.getStringMap().containsKey(point) && board.getStringMap().get(point).equals(configService.getBase())) {
					row += getBaseString(board, point);
				} else if (board.getStringMap().containsKey(point) && board.getPath().inverse().containsKey(point)) {
					row += getStartString(board, point);
				} else if (board.getStringMap().containsKey(point)) {
					row += getGoalString(board, point);
				} else if (board.getPath().inverse().containsKey(point)) {
					row += getPathString(board, point);
				} else {
					row += "   ";
				}
			}
			rows.add(row + Color.FIELD.getBgFg() + " ║ " + Color.reset());
		}
		String topRow = Color.FIELD.getBgFg() + " ╔ " + getNameRowString(board.getPlayers().get(1), board.getPlayers().get(2), outlineRow) + Color.FIELD.getBgFg() + " ╗ " + Color.reset();
		String bottomRow = Color.FIELD.getBgFg() + " ╚ " + getNameRowString(board.getPlayers().get(4), board.getPlayers().get(3), outlineRow) + Color.FIELD.getBgFg() + " ╝ " + Color.reset();
		return topRow + "\n" + rows.toString() + "\n" + bottomRow;
	}

	private String getStartString(Board board, Point point) {
		Integer position = board.getPath().inverse().get(point);
		Color fieldColor = board.getColorMap().get(point);
		Map<Integer, Color> pieceIdAndColor = board.getPlayers().values().stream()
				.filter(player -> pieceService.getPieceOnPosition(player.getPieces(), position).isPresent())
				.collect(Collectors.toMap(player -> pieceService.getPieceOnPosition(player.getPieces(), position).get().getId(), Player::getColor));
		for (Map.Entry<Integer, Color> piece : pieceIdAndColor.entrySet()) {
			String startFieldString = " ";
			if (pieceIdAndColor.containsValue(fieldColor)) {
				startFieldString += Color.PATH.getFg();
			} else {
				startFieldString += pieceIdAndColor.get(piece.getKey()).getFg();
			}
			startFieldString += piece.getKey();
			if (piece.getKey() < 10) {
				startFieldString += " ";
			}
			return startFieldString;
		}
		return Color.PATH.getFg() + configService.getStart();
	}

	private String getBaseString(Board board, Point point) {
		Color fieldColor = board.getColorMap().get(point);
		List<Integer> collectBasePieceCount = board.getPlayers().values().stream()
				.filter(player -> player.getColor().equals(fieldColor) && pieceService.getPieceOnPosition(player.getPieces(), 0).isPresent())
				.map(Player::getBasePieceCount)
				.collect(Collectors.toList());
		for (Integer basePieceCount : collectBasePieceCount) {
			if (basePieceCount > 0 && basePieceCount < 10) {
				return Color.PATH.getFg() + " " + basePieceCount + " ";
			} else if (basePieceCount > 9) {
				return Color.PATH.getFg() + " " + basePieceCount;
			}
		}
		return Color.PATH.getFg() + configService.getBase();
	}

	private String getGoalString(Board board, Point point) {
		Color fieldColor = board.getColorMap().get(point);
		int position = 0 - ((int) board.getStringMap().get(point).charAt(1) - 96);
		Set<Integer> pieceIds = board.getPlayers().values().stream()
				.filter(player -> player.getColor().equals(fieldColor) && pieceService.getPieceOnPosition(player.getPieces(), position).isPresent())
				.map(player -> pieceService.getPieceOnPosition(player.getPieces(), position).get().getId())
				.collect(Collectors.toSet());
		for (Integer pieceId : pieceIds) {
			if (pieceId < 10) {
				return Color.PATH.getFg() + " " + pieceId + " ";
			}
			return Color.PATH.getFg() + " " + pieceId;
		}

		return Color.PATH.getFg() + board.getStringMap().get(point);
	}

	private String getPathString(Board board, Point point) {
		Integer position = board.getPath().inverse().get(point);
		List<String> tileStrings = board.getPlayers().values().stream()
				.filter(player -> pieceService.getPieceOnPosition(player.getPieces(), position).isPresent())
				.map(player -> player.getColor().getFg() + pieceService.getPieceOnPosition(player.getPieces(), position).get().getId())
				.collect(Collectors.toList());
		for (String tileString : tileStrings) {
			if (tileString.length() < 10) {
				tileString = " " + tileString + " ";
			} else if (tileString.length() < 100) {
				tileString = " " + tileString;
			}
			return tileString;
		}
		return Color.PATH.getFg() + configService.getPath();
	}

	private String getNameRowString(Player left, Player right, String outlineRow) {
		int maxLength = outlineRow.length() / 2 - 2;
		String leftName = left.getShortName(maxLength), rightName = right.getShortName(maxLength);
		return leftName + " " + Color.FIELD.getBgFg() + outlineRow.substring(leftName.length() + rightName.length() + 2) + " " + rightName;
	}

}
