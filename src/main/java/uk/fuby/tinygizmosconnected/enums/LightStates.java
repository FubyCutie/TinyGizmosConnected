package uk.fuby.tinygizmosconnected.enums;

import net.minecraft.util.StringIdentifiable;

public enum LightStates implements StringIdentifiable {

	GLOWING("glowing"),
	NORMAL("normal"),
	SHADED("shaded");

	private final String name;

	LightStates(String name) {
		this.name = name;
	}

	@Override
	public String asString() {
		return this.name;
	}

	public String toString() {
		return this.name;
	}

}
