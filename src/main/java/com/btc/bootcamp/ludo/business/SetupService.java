package com.btc.bootcamp.ludo.business;

import com.btc.bootcamp.ludo.business.model.Board;
import com.btc.bootcamp.ludo.business.model.Color;
import com.btc.bootcamp.ludo.business.model.Player;
import com.btc.bootcamp.ludo.persistence.ProgramStateDao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SetupService {

	@Autowired
	private ConfigurationService configService;
	@Autowired
	private ProgramStateDao programStateDao;
	@Autowired
	private CreationService creationService;
	@Autowired
	private PromptingService promptingService;
	@Autowired
	private GameService gameService;

	public void setup() {

		List<Color> possibleColors = Color.getColorList();

		Map<Integer, Boolean> botMap = new HashMap<>();
		Map<Integer, Player> players = new HashMap<>();
		for (int i = 1; i <= 4; i++) {
			if (promptingService.setBots(i)) {
				botMap.put(i, true);
				players.put(i, setupNewBot(configService.getDefaultNames()[i - 1], possibleColors));
			} else {
				botMap.put(i, false);
				players.put(i, setupNewPlayer(i, configService.getDefaultNames()[i - 1], possibleColors));
			}
			possibleColors.remove(players.get(i).getColor());
		}

		Board board = creationService.createNewBoard(players);
		board.setIsBot(botMap);

		programStateDao.addBoard(board);

		gameService.play(board);

	}

	private Player setupNewBot(String defaultName, List<Color> possibleColors) {
		String name = defaultName;
		Color color = possibleColors.get(0);
		return creationService.createNewPlayer(name, color);
	}

	private Player setupNewPlayer(int playerId, String defaultName, List<Color> possibleColors) {
		String name = promptingService.getPlayerName(playerId, defaultName);
		Color color = promptingService.getPlayerColor(playerId, name, possibleColors);
		return creationService.createNewPlayer(name, color);
	}

}
