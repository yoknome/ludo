package com.btc.bootcamp.ludo.business.model;

public class Piece {

    private Integer id;
    private Integer position;

    public Piece(Integer id, Integer position) {
        this.id = id;
        this.position = position;
    }

    public Integer getId() {
        return id;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

}
