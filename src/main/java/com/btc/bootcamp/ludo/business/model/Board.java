package com.btc.bootcamp.ludo.business.model;

import com.google.common.collect.BiMap;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.HashBiMap;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board implements Comparable<Board> {

	private final Long id;
	private final Dice dice;
	private Map<Integer, Player> players;
	private BiMap<Integer, Point> path;
	private Map<Point, Color> colorMap;
	private Map<Point, String> stringMap;
	private Map<Integer, Boolean> isBot = new HashMap<>();
	private List<Player> finishedPlayers = new ArrayList<>();

	public Board(Long id, int diceSideCount) {
		this.id = id;
		this.dice = new Dice(diceSideCount);
		this.players = new HashMap<>();
		this.path = HashBiMap.create();
		this.colorMap = new HashMap<>();
		this.stringMap = new HashMap<>();
	}

	public Long getId() {
		return id;
	}

	public Dice getDice() {
		return dice;
	}

	public Map<Integer, Player> getPlayers() {
		return players;
	}

	public BiMap<Integer, Point> getPath() {
		return path;
	}

	public Map<Point, Color> getColorMap() {
		return colorMap;
	}

	public Map<Point, String> getStringMap() {
		return stringMap;
	}

	public Map<Integer, Boolean> getIsBot() {
		return isBot;
	}

	public void setIsBot(Map<Integer, Boolean> isBot) {
		this.isBot = isBot;
	}

	public List<Player> getFinishedPlayers() {
		return finishedPlayers;
	}

	@Override
	public int compareTo(Board that) {
		return ComparisonChain.start()
				.compare(this.id, that.id)
				.result();
	}
}
