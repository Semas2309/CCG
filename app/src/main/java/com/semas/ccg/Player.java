package com.semas.ccg;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    public int health;
    public int coins;
    private List<Card> hand;
    private List<Card> field;

    public Player(String name) {
        this.name = name;
        this.coins = 10;
        this.health = 40;
        this.hand = new ArrayList<>();
        this.field = new ArrayList<>();
    }

    public void addCardToHand(Card card) {
        if (hand.size() < 5) {
            hand.add(card);
        }
    }

    public List<Card> getHand() {
        return hand;
    }

    public List<Card> getField() {
        return field;
    }

    public int getCoins(){
        return coins;
    }

    public void addCardToField(Card card) {
        field.add(card);
    }

    public void removeCardFromHand(Card card) {
        hand.remove(card);
    }

    public int getHealth() {
        return health;
    }
}