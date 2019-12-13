package com.btc.bootcamp.ludo.business;

import com.btc.bootcamp.ludo.business.model.Color;
import com.btc.bootcamp.ludo.business.model.Piece;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.springframework.stereotype.Service;

@Service
public class PromptingService {

	private TextIO textIO = TextIoFactory.getTextIO();
	// Web page: https://text-io.beryx.org/releases/latest/

	public Boolean setBots(Integer id) {
		return textIO.newBooleanInputReader()
				.withDefaultValue(true)
				.read("Do you want player " + id + " to be an AI?");
	}

	public String getPlayerName(int playerId, String defaultName) {
		return textIO.newStringInputReader()
				.withDefaultValue(defaultName)
				.read(playerId + ". player name");
	}

	public Color getPlayerColor(int playerId, String playerName, List<Color> list) {
		return textIO.newEnumInputReader(Color.class)
				.withNumberedPossibleValues(list)
				.withDefaultValue(list.get(0))
				.read(playerId + ". " + playerName + "'s player color");
	}

	public Piece choosePiece(List<Piece> possiblePieces) {
		Map<Integer, Piece> map = new HashMap<>();
		List<Integer> possibleValues = new ArrayList<>();
		for (Piece possiblePiece : possiblePieces) {
			map.put(possiblePiece.getId(), possiblePiece);
			possibleValues.add(possiblePiece.getId());
		}
		int input = textIO.newIntInputReader()
				.withPossibleValues(possibleValues)
				.read("Select the Piece you want to move");
		return map.get(input);
	}

	public void pressEnterToContinue() {
		textIO.newBooleanInputReader()
				.withDefaultValue(true)
				.withPromptAdjustments(false)
				.read("Press enter to continue...");
	}

	public boolean closeGame() {
		return textIO.newBooleanInputReader()
				.withPropertiesPrefix("exit")
				.withDefaultValue(true)
				.read("play again?");
	}
}
