package com.poker.gameservice.util;

import com.poker.gameservice.model.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CardUtils {
    public static List<String> suite = new ArrayList<>(List.of("Diamond", "Spade", "Club", "Heart"));
    public static List<String> rank = new ArrayList<>(
            List.of("Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"));

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

    public static Card getRandomCard(List<Card> availableCards) {
        Random rand = new Random();
        int randomIndex = rand.nextInt(availableCards.size());
        return availableCards.get(randomIndex);
    }

    // availableCards should be pre-shuffled
    public static List<Card> getHandCards(List<Card> availableCards) {
        return new ArrayList<Card>(List.of(availableCards.get(0), availableCards.get(1)));
    }


    public static int getSuiteValue (Card card){
        if(card.getSuite().equals("Spade")){
            return 400;
        } else if(card.getSuite().equals("Heart")){
            return 300;
        } else if(card.getSuite().equals("Diamond")){
            return 200;
        } else if(card.getSuite().equals("Club")){
            return 100;
        }
        return 0;
    }

    public static int getRankValue(Card card){
        if(card.getRank().equals("Ace")){
            return 14;
        } else if(card.getRank().equals("King")){
            return 13;
        } else if(card.getRank().equals("Queen")){
            return 12;
        } else if(card.getRank().equals("Jack")){
            return 11;
        } 
        return Integer.parseInt(card.getRank());
        
    }
}
