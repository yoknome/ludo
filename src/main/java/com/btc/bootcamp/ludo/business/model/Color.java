package com.btc.bootcamp.ludo.business.model;

import java.util.ArrayList;
import java.util.List;

public enum Color {
	RED(     "\u001B[41m", "\u001B[91m", "red"),
	GREEN(   "\u001B[42m", "\u001B[32m", "green"),
	BLUE(    "\u001B[44m", "\u001B[94m", "blue"),
	PURPLE(  "\u001B[45m", "\u001B[95m", "purple"),
	CYAN(    "\u001B[46m", "\u001B[96m", "cyan"),
	YELLOW(  "\u001B[103m", "\u001B[33m", "yellow"),
	PATH(    "\u001B[47m", "\u001B[30m", ""), // white & black
	FIELD(   "\u001B[43m", "\u001B[91m", ""); // yellow & bright red

	private String bg;
	private String fg;
	private String name;

	Color(String bg, String fg, String name) {
		this.bg = bg;
		this.fg = fg;
		this.name = name;
	}

	public String getBg() {
		return bg;
	}

	public String getFg() {
		return fg;
	}

	public String getName() {
		return name;
	}

	public String getBgFg() {
		return bg + fg;
	}

	public static String reset() {
		return "\u001B[0m";
	}

	public static List<Color> getColorList() {
		List<Color> list = new ArrayList<>();
		int i = 0;
		for (Color value : values()) {
			list.add(value);
			i++;
			if (i==6) {
				break;
			}
		}
		return list;
	}
}
