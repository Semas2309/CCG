package com.semas.ccg;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Button;
import android.widget.Toast;
import android.os.Vibrator;



import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Game game;


    private boolean newgame = true;
    private int turnCount = 1;
    public int player1Coins;
    public int player2Coins;
    private CardAdapter player1Adapter;
    private CardAdapter player2Adapter;
    private List<Card> player1Cards;
    private List<Card> player2Cards;
    private List<Card> cardsOnField;
    private int player1CardCount = 0;
    private int player2CardCount = 0;
    private AlertDialog winnerDialog;
    private Vibrator vibrator;

    private boolean isVibrationEnabled;

    private RecyclerView player1RecyclerView;
    private RecyclerView player2RecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        SharedPreferences sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        isVibrationEnabled = sharedPreferences.getBoolean("vibration", false);



        game = new Game("Игрок 1", "Игрок 2");
        game.drawCards(game.getPlayer1());
        game.drawCards(game.getPlayer2());

        player1Coins = game.getPlayer1().getCoins();
        player2Coins = game.getPlayer2().getCoins();

        player1Cards = game.getPlayer1().getHand();
        player2Cards = game.getPlayer2().getHand();
        cardsOnField = new ArrayList<>();

        // Настройка адаптера для игрока 1---------------------------------------------------------------
        player1RecyclerView = findViewById(R.id.player1_hand);
        player1RecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        player1Adapter = new CardAdapter(player1Cards, this::onCardClick);
        player1RecyclerView.setAdapter(player1Adapter);


        DividerItemDecoration player1Divider = new DividerItemDecoration(0xFF000000, 2); // Черный цвет, ширина 2dp
        player1RecyclerView.addItemDecoration(player1Divider);

        // Настройка адаптера для игрока 2---------------------------------------------------------------
        player2RecyclerView = findViewById(R.id.player2_hand);
        player2RecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        player2Adapter = new CardAdapter(player2Cards, this::onCardClick);
        player2RecyclerView.setAdapter(player2Adapter);


        DividerItemDecoration player2Divider = new DividerItemDecoration(0xFF000000, 2);
        player2RecyclerView.addItemDecoration(player2Divider);

        Button endTurnButton = findViewById(R.id.end_turn_button);
        endTurnButton.setOnClickListener(v -> endTurn());

        Button exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExitConfirmationDialog();
            }
        });
    }

    //клик на карту ---------------------------------------------------------------
    private void onCardClick(Card card, int position) {
        if (!cardsOnField.contains(card)) {
            if (player1Cards.contains(card)) {
                if (player1Coins >= card.getCost()) {
                    player1Coins -= card.getCost();
                    game.player1.coins -= card.getCost();
                    updateCoinDisplay(1);
                    cardsOnField.add(card);
                    player1Cards.remove(card);
                    game.player1.addCardToField(card);
                    player1Adapter.notifyDataSetChanged();
                    moveCardToField(card, 1);
                }
            } else if (player2Cards.contains(card)) {
                if (player2Coins >= card.getCost()) {
                    player2Coins -= card.getCost();
                    game.player2.coins -= card.getCost();
                    updateCoinDisplay(2);
                    cardsOnField.add(card);
                    player2Cards.remove(card);
                    game.player2.addCardToField(card);
                    player2Adapter.notifyDataSetChanged();
                    moveCardToField(card, 2);
                }
            }
        }
    }








    private void moveCardToField(Card card, int playerNumber) {

        LinearLayout targetField = (playerNumber == 1) ? findViewById(R.id.player1_field) : findViewById(R.id.player2_field);


        View cardView = LayoutInflater.from(this).inflate(R.layout.card_item, targetField, false);


        TextView cardName = cardView.findViewById(R.id.card_name);
        TextView cardCost = cardView.findViewById(R.id.card_cost);
        cardName.setText(card.getName());



        TextView cardAttack = cardView.findViewById(R.id.card_attack);
        TextView cardHealth = cardView.findViewById(R.id.card_health);
        cardAttack.setText(String.valueOf(card.getAttack()));
        cardHealth.setText(String.valueOf(card.getHealth()));


        cardView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));


        targetField.addView(cardView);


        cardView.setTranslationY(0);


        float translationY = (playerNumber == 1) ? -200f : 200f; // Вверх для игрока 1, вниз для игрока 2

        cardView.animate()
                .translationY(translationY)
                .setDuration(500)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {

                        cardView.setTranslationY(0);
                    }
                })
                .start();


    }

    private void endTurn() {
        game.endTurn();
        turnCount++;
        updateHealthDisplays();
        updateFieldDisplay();
        updateCoinDisplay(1);
        updateCoinDisplay(2);
        updateTurnCounterDisplay();



        checkForWinner();
    }

    private void updateTurnCounterDisplay() {
        TextView turnCounterView = findViewById(R.id.turn_counter);

        turnCounterView.setText("Ход: " + turnCount);
    }

    private void updateHealthDisplays() {
        TextView player1HealthView = findViewById(R.id.player1_health);
        TextView player2HealthView = findViewById(R.id.player2_health);

        player1HealthView.setText("Health: " + game.getPlayer1().getHealth());
        player2HealthView.setText("Health: " + game.getPlayer2().getHealth());
    }

    private void checkForWinner() {
        if (game.getPlayer1().getHealth() < 1) {
            showWinnerDialog("Игрок 2 победил!");
        } else if (game.getPlayer2().getHealth() < 1) {
            showWinnerDialog("Игрок 1 победил!");
        }
    }

    private void showWinnerDialog(String message) {
        if (winnerDialog != null && winnerDialog.isShowing()) {
            winnerDialog.dismiss();
        }

        if (isVibrationEnabled && vibrator != null) {
            vibrator.vibrate(500);
        }

        winnerDialog = new AlertDialog.Builder(this)
                .setTitle("Игра окончена")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Начать заново", (dialog, which) -> {
                    restartGame();
                    winnerDialog.dismiss();
                })
                .setNegativeButton("Выход", (dialog, which) -> {
                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                    startActivity(intent);
                    finish();
                })
                .create();

        winnerDialog.show();
    }

    private void restartGame() {
        turnCount = 1;
        updateTurnCounterDisplay();
        game = new Game("Игрок 1", "Игрок 2");
        game.drawCards(game.getPlayer1());
        game.drawCards(game.getPlayer2());

        player1Coins = game.getPlayer1().getCoins();
        player2Coins = game.getPlayer2().getCoins();

        player1Cards = game.getPlayer1().getHand();
        player2Cards = game.getPlayer2().getHand();
        cardsOnField.clear();




        player1Adapter = new CardAdapter(player1Cards, this::onCardClick);
        player2Adapter = new CardAdapter(player2Cards, this::onCardClick);
        player1RecyclerView.setAdapter(player1Adapter);
        player2RecyclerView.setAdapter(player2Adapter);

        updateHealthDisplays();
        updateFieldDisplay();
        updateCoinDisplay(1);
        updateCoinDisplay(2);
    }
    private void updateCoinDisplay(int playerNumber) {
        if (playerNumber == 1) {
            player1Coins = game.getPlayer1().getCoins();
        } else {
            player2Coins = game.getPlayer2().getCoins();
        }

        TextView player1CoinsView = findViewById(R.id.player1_coins);
        TextView player2CoinsView = findViewById(R.id.player2_coins);

        player1CoinsView.setText("Money: " + player1Coins);
        player2CoinsView.setText("Money: " + player2Coins);
    }

    private void updateFieldDisplay() {
        LinearLayout player1Field = findViewById(R.id.player1_field);
        LinearLayout player2Field = findViewById(R.id.player2_field);

        player1Field.removeAllViews();
        player2Field.removeAllViews();


        for (Card card : game.getPlayer1().getField()) {
            moveCardToField(card, 1);
        }


        for (Card card : game.getPlayer2().getField()) {
            moveCardToField(card, 2);
        }
    }
    private void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Подтверждение выхода");
        builder.setMessage("Вы уверены, что хотите вернуться в главное меню?");

        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });

        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}