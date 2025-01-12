package com.semas.ccg;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Game {
    public Player player1;
    public Player player2;
    public List<Card> deck;

    public Game(String player1Name, String player2Name) {
        player1 = new Player(player1Name);
        player2 = new Player(player2Name);
        deck = new ArrayList<>();

        initializeDeck();
        shuffleDeck();
    }

    private void initializeDeck() {

        deck.add(new Card("Minion", 1, 1, 1));
        deck.add(new Card("Mage", 2, 2, 2));
        deck.add(new Card("Warrior", 3, 3, 3));
        deck.add(new Card("Ogre", 4, 4, 4));
        deck.add(new Card("Knight", 2, 3, 1));
        deck.add(new Card("Boulder", 2, 1, 3));
        deck.add(new Card("Archer", 3, 5, 1));
        deck.add(new Card("Shield", 3, 1, 5));
        for (int i = 0; i < 3; i++) {
            deck.add(new Card("Minion", 1, 1, 1));
            deck.add(new Card("Mage", 2, 2, 2));
            deck.add(new Card("Warrior", 3, 3, 3));
            deck.add(new Card("Ogre", 4, 4, 4));
            deck.add(new Card("Knight", 2, 3, 1));
            deck.add(new Card("Boulder", 2, 1, 3));
            deck.add(new Card("Archer", 3, 5, 1));
            deck.add(new Card("Shield", 3, 1, 5));
        }



    }

    private void shuffleDeck() {
        Collections.shuffle(deck);
    }

    public void endTurn() {
        shuffleAndDrawCards();
        executeCardAttacks();
        restoreCoins();
        Log.d("GameState","Current deck size" + deck.size());
    }

    private void executeCardAttacks() {

        List<Card> field1 = player1.getField();
        List<Card> field2 = player2.getField();

        Log.d("GameState", "Field 1 Size: " + field1.size());
        Log.d("GameState", "Field 2 Size: " + field2.size());


        List<Card> cardsToRemove1 = new ArrayList<>();
        List<Card> cardsToRemove2 = new ArrayList<>();

        // Атака карт
        for (int i = 0; i < Math.min(field1.size(), field2.size()); i++) {
            Card card1 = field1.get(i);
            Card card2 = field2.get(i);


            card1.health -= card2.attack;
            card2.health -= card1.attack;


            if (card1.health <= 0) {
                cardsToRemove1.add(card1);
            }
            if (card2.health <= 0) {
                cardsToRemove2.add(card2);
            }
        }


        field1.removeAll(cardsToRemove1);
        field2.removeAll(cardsToRemove2);


        handleUnpairedCards(field1, field2);

        Log.d("GameState", "Player 1 Health: " + player1.getHealth());
        Log.d("GameState", "Player 2 Health: " + player2.getHealth());
    }

    private void handleUnpairedCards(List<Card> field1, List<Card> field2) {

        Log.d("GameState", "Started player attack");
        if (field1.size() < field2.size()) {
            for (int i = field1.size(); i < field2.size(); i++) {
                player1.health -= field2.get(i).attack;
                Log.d("GameState", "Player 1 takes damage: " + field2.get(i).attack);
            }
        } else if (field2.size() < field1.size()) {
            for (int i = field2.size(); i < field1.size(); i++) {
                player2.health -= field1.get(i).attack;
                Log.d("GameState", "Player 2 takes damage: " + field1.get(i).attack);
            }
        }
    }

    private void restoreCoins() {
        player1.coins = 10;
        player2.coins = 10;


    }

    private void shuffleAndDrawCards() {
        restoreDeck();
        shuffleDeck();
        drawCards(player1);
        drawCards(player2);
    }

    private void restoreDeck(){
        deck.add(new Card("Minion", 1, 1, 1));
        deck.add(new Card("Mage", 2, 2, 2));
        deck.add(new Card("Warrior", 3, 3, 3));
        deck.add(new Card("Ogre", 4, 4, 4));
        deck.add(new Card("Knight", 2, 3, 1));
        deck.add(new Card("Boulder", 2, 1, 3));
        deck.add(new Card("Archer", 3, 5, 1));
        deck.add(new Card("Shield", 3, 1, 5));
    }


    public void drawCards(Player player) {
        while (player.getHand().size() < 5 && !deck.isEmpty()) {
            player.addCardToHand(deck.remove(0));
        }
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

}