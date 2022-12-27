package com.poker.gameservice.util;

import com.poker.gameservice.model.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CardUtils {
    public static List<String> suite = new ArrayList<>(List.of("Diamond", "Spade", "Club", "Heart"));
    public static List<String> rank = new ArrayList<>(
            List.of("Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"));

    public static List<Card> generateDeck() {
        List<String> suite = CardUtils.suite;
        List<String> rank = CardUtils.rank;

        List<Card> deck = new ArrayList<>();

        for (String value : suite) {
            for (String s : rank) {
                deck.add(new Card(value, s));
            }
        }

        return deck;
    }

    public static List<Card> getStartingDeck(Integer numDecks) {
        List<Card> availableCards = new ArrayList<>();

        for (int i = 0; i < numDecks; ++i) {
            List<Card> deck = generateDeck();
            availableCards.addAll(deck);
        }

        return availableCards;
    }

    public static List<Card> getFlopCards(Integer numDecks) {
        List<Card> availableCards = getStartingDeck(numDecks);

        Random rand = new Random();
        List<Card> chosenCards = new ArrayList<>();

        for (int i = 0; i < 3; ++i) {
            int randomIndex = rand.nextInt(availableCards.size());

            while (chosenCards.contains(availableCards.get(randomIndex))) {
                randomIndex = rand.nextInt(availableCards.size());
            }

            chosenCards.add(availableCards.get(randomIndex));
        }

        return chosenCards;
    }

    // availableCards should be pre-shuffled
    public static List<Card> getHandCards(List<Card> availableCards) {
        return new ArrayList<Card>(List.of(availableCards.get(0), availableCards.get(1)));
    }
}
