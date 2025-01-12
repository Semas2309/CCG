package com.semas.ccg;

public class Card {
    public String name;
    public int cost;
    public int attack;
    public int health;

    public Card(String name, int cost, int attack, int health) {
        this.name = name;
        this.cost = cost;
        this.attack = attack;
        this.health = health;


        if (attack + health > cost * 2) {
            throw new IllegalArgumentException("Сумма характеристик превышает допустимое значение.");
            // похуй потом убрать ---------------------------------------------------------------
        }
    }

    public int getCost() {
        return cost;
    }

    public int getAttack() {
        return attack;
    }

    public int getHealth() {
        return health;
    }

    public String getName() {
        return name;
    }
}