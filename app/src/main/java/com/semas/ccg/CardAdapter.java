package com.semas.ccg;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private List<Card> cardList;
    private OnCardClickListener onCardClickListener;


    public CardAdapter(List<Card> cardList, OnCardClickListener listener) {
        this.cardList = cardList;
        this.onCardClickListener = listener;
    }


    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new CardViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Card card = cardList.get(position);
        holder.cardNameTextView.setText(card.getName() + " (" + card.getCost() + ")");
        holder.cardAttackTextView.setText("Attack: " + card.getAttack());
        holder.cardHealthTextView.setText("Health: " + card.getHealth());


        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.width = 150;
        holder.itemView.setLayoutParams(layoutParams);


        holder.itemView.setOnClickListener(v -> onCardClickListener.onCardClick(card, position));
    }


    @Override
    public int getItemCount() {
        return cardList.size();
    }


    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public TextView cardNameTextView;
        public TextView cardAttackTextView;
        public TextView cardHealthTextView;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardNameTextView = itemView.findViewById(R.id.card_name);
            cardAttackTextView = itemView.findViewById(R.id.card_attack);
            cardHealthTextView = itemView.findViewById(R.id.card_health);
        }
    }


    public interface OnCardClickListener {
        void onCardClick(Card card, int position);
    }
}