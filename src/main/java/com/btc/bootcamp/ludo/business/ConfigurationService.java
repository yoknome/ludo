package com.btc.bootcamp.ludo.business;

import org.springframework.stereotype.Service;

@Service
public class ConfigurationService {

	private final int pieceCountPerPlayer = 4; // min 1, max 26

	public int getPieceCountPerPlayer() {
		return Math.max(1, Math.min(pieceCountPerPlayer, 26));
	}

	public int getFieldsPerQuarterRound() {
		return getPieceCountPerPlayer() * 2 + 2;
	}

	public int getFieldsPerRound() {
		return getFieldsPerQuarterRound() * 4;
	}

	public int getBoardWidth() {
		return getFieldsPerQuarterRound() + 1;
	}

	private final int diceSideCount = Math.min(getPieceCountPerPlayer() + 2, 12);

	public int getDiceSideCount() {
		return diceSideCount;
	}

	private final String start = " A ";
	private final String base = " B ";
	private final String path = " O ";

	public String getStart() {
		return start;
	}

	public String getBase() {
		return base;
	}

	public String getPath() {
		return path;
	}

	private String[] defaultNames = {"Brian", "James", "Emma", "Tyler"};

	public String[] getDefaultNames() {
		return defaultNames;
	}
}
