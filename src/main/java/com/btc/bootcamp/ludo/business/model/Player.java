package com.btc.bootcamp.ludo.business.model;

import java.util.HashSet;
import com.google.common.collect.ComparisonChain;
import java.util.Set;

public class Player implements Comparable<Player> {

	private Long id;
	private Color color;
	private String name;
	private Set<Piece> pieces;

	public Player(Long id, String name, Color color, Integer pieceCount) {
		this.id = id;
		this.color = color;
		this.name = name;
		this.pieces = new HashSet<>();
		for (int i = 1; i <= pieceCount; i++) {
			pieces.add(new Piece(i, 0));
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getShortName(int maxLength) {
		if (name.length() < maxLength) {
			return name;
		}
		int cutoff = name.length() - maxLength + 3;
		return name.substring(0, name.length() - cutoff) + "...";
	}

	public String getColoredName() {
		return color.getFg() + name + Color.reset();
	}

	public void setName(String name) {
		this.name = name;
	}

	public Color getColor() {
		return color;
	}

	public Set<Piece> getPieces() {
		return pieces;
	}

	public Integer getBasePieceCount() {
		return (int) pieces.stream().filter(piece -> piece.getPosition().equals(0)).count();
	}

	@Override
	public int compareTo(Player that) {
		return ComparisonChain.start()
				.compare(this.id, that.id)
				.result();
	}

}
