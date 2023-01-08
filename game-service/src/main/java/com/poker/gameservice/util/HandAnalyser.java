package com.poker.gameservice.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.poker.gameservice.model.Card;
import com.poker.gameservice.model.Hand;
import com.poker.gameservice.model.entity.Game;
import com.poker.gameservice.model.entity.Player;

public class HandAnalyser {
    public HashMap<Long, Hand> getHands(Game game) {
        List<Player> players = game.getPlayers();
        HashMap<Long, Hand> hands = new HashMap<>();
        for (Player player : players) {
            hands.put(player.getId(), analyseHand(player, game));
        }
        return hands;
    }

    private Hand analyseHand(Player player, Game game) {
        List<Card> cards = game.getCardsOnTable();
        cards.addAll(player.getDeck());

        HashMap<Integer, Integer> cardValueCount = new HashMap<>();

        for (Card card : cards) {
            int rankValue = CardUtils.getRankValue(card);
            if (cardValueCount.containsKey(rankValue)) {
                cardValueCount.put(rankValue, cardValueCount.get(rankValue) + 1);
            } else {
                cardValueCount.put(rankValue, 1);
            }
        }

        cards.sort(new Comparator<Card>() {
            @Override
            public int compare(Card card1, Card card2) {
                int card1Value = CardUtils.getRankValue(card1);
                int card2Value = CardUtils.getRankValue(card2);

                return cardValueCount.get(card1Value) < cardValueCount.get(card2Value) ? 1
                        : cardValueCount.get(card1Value) > cardValueCount.get(card2Value) ? -1
                                : cardValueCount.get(card1Value) > cardValueCount.get(card2Value) ? -1
                                        : cardValueCount.get(card1Value) > cardValueCount.get(card2Value) ? -1
                                                : cardValueCount.get(card1Value) > cardValueCount.get(card2Value) ? -1
                                                        : cardValueCount.get(card1Value) > cardValueCount
                                                                .get(card2Value) ? -1
                                                                        : cardValueCount
                                                                                .get(card1Value) > cardValueCount
                                                                                        .get(card2Value) ? -1
                                                                                                : cardValueCount.get(
                                                                                                        card1Value) > cardValueCount
                                                                                                                .get(card2Value)
                                                                                                                        ? -1
                                                                                                                        : card2Value
                                                                                                                                - card1Value;
            }
        });

        Hand hand = rankHand(cards, cardValueCount);
        return hand;
    }

    private Hand rankHand(List<Card> cards, HashMap<Integer, Integer> cardValueCount) {
        Hand result = new Hand();
        result.setRank(getRankValue(cards, cardValueCount));
        result.setScore(getScore(cards));
        result.setName(getRankName(result.getRank()));
        return result;
    }

    private int getRankValue(List<Card> cards, HashMap<Integer, Integer> cardValueCount) {
        int rank = 0;
        if (isRoyalFlush(cards))
            rank = 9;
        else if (isStraightFlush(cards))
            rank = 8;
        else if (isFourOfAKind(cardValueCount))
            rank = 7;
        else if (isFullHouse(cardValueCount))
            rank = 6;
        else if (isFlush(cards))
            rank = 5;
        else if (isStraight(cards))
            rank = 4;
        else if (isThreeOfAKind(cardValueCount))
            rank = 3;
        else if (isTwoPair(cardValueCount))
            rank = 2;
        else if (isPair(cardValueCount))
            rank = 1;
        return rank;
    }

    private int getScore(List<Card> cards) {
        int score = 0;
        int shift = 24;
        for (Card card : cards) {
            score |= CardUtils.getRankValue(card) << shift;
            shift -= 4;
        }
        return score;

    }

    private String getRankName(int rank) {
        switch (rank) {
            case 9:
                return "Royal Flush";
            case 8:
                return "Straight Flush";
            case 7:
                return "Four of a Kind";
            case 6:
                return "Full House";
            case 5:
                return "Flush";
            case 4:
                return "Straight";
            case 3:
                return "Three of a Kind";
            case 2:
                return "Two Pair";
            case 1:
                return "Pair";
            default:
                return "High Card";
        }
    }

    private boolean isRoyalFlush(List<Card> cards) {
        List<Integer> cardValues = new ArrayList<>();
        for (Card card : cards) {
            cardValues.add(CardUtils.getRankValue(card) + CardUtils.getSuiteValue(card));
        }

        return (cardValues.contains(114) && cardValues.contains(113) && cardValues.contains(112)
                && cardValues.contains(111) && cardValues.contains(110))
                || (cardValues.contains(214) && cardValues.contains(213) && cardValues.contains(212)
                        && cardValues.contains(211) && cardValues.contains(210))
                || (cardValues.contains(314) && cardValues.contains(313) && cardValues.contains(312)
                        && cardValues.contains(311) && cardValues.contains(310))
                || (cardValues.contains(414) && cardValues.contains(413) && cardValues.contains(412)
                        && cardValues.contains(411) && cardValues.contains(410));
    }

    private boolean isStraightFlush(List<Card> cards) {
        return isStraight(cards) && isFlush(cards);
    }

    private boolean isFourOfAKind(HashMap<Integer, Integer> cardValueCount) {
        int count = 0;
        for (Integer value : cardValueCount.values()) {
            if (value == 4)
                count++;
        }
        return count == 1;
    }

    private boolean isFullHouse(HashMap<Integer, Integer> cardValueCount) {
        int count = 0;
        for (Integer value : cardValueCount.values()) {
            if (value == 3)
                count++;
        }

        return count == 2 || (count == 1 && (isPair(cardValueCount) || isTwoPair(cardValueCount)));
    }

    private boolean isFlush(List<Card> cards) {
        HashMap<Integer, Integer> suiteCount = new HashMap<>();
        for (Card card : cards) {
            int suitValue = CardUtils.getSuiteValue(card);
            if (suiteCount.containsKey(suitValue)) {
                suiteCount.put(suitValue, suiteCount.get(suitValue) + 1);
            } else {
                suiteCount.put(suitValue, 1);
            }
        }

        for (Integer value : suiteCount.values()) {
            if (value >= 5)
                return true;
        }

        return false;
    }

    private boolean isStraight(List<Card> cards) {
        Set<Integer> rankValuesSet = new TreeSet<>();
        for (Card card : cards) {
            rankValuesSet.add(CardUtils.getRankValue(card));
        }
        if (rankValuesSet.contains(14)) {
            rankValuesSet.add(1);
        }

        List<Integer> rankValues = new ArrayList<>(rankValuesSet);

        Collections.sort(rankValues);

        int count = 1;
        for (int i = 0; i < rankValues.size() - 1; i++) {
            if (rankValues.get(i + 1) - rankValues.get(i) == 1) {
                count++;
            } else if (count < 5) {
                count = 1;
            } else {
                return true;
            }
        }
        return count >= 5;
    }

    private boolean isThreeOfAKind(HashMap<Integer, Integer> cardValueCount) {
        int count = 0;
        for (Integer value : cardValueCount.values()) {
            if (value == 3)
                count++;
        }
        return count == 1;
    }

    private boolean isTwoPair(HashMap<Integer, Integer> cardValueCount) {
        int count = 0;
        for (Integer value : cardValueCount.values()) {
            if (value == 2)
                count++;
        }
        return count >= 2;
    }

    private boolean isPair(HashMap<Integer, Integer> cardValueCount) {
        int count = 0;
        for (Integer value : cardValueCount.values()) {
            if (value == 2)
                count++;
        }
        return count == 1;
    }
}
