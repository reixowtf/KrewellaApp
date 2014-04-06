package com.example.krewellaapp;

import java.io.Serializable;

public class Imatge implements Serializable {
	private final String descripcio;
	private final int bmp;

	public Imatge(String descripcio, int bmp) {
		this.descripcio = descripcio;
		this.bmp = bmp;
	}

	public String getDescripcio() {
		return descripcio;
	}

	public int getBmp() {
		return bmp;
	}
}
