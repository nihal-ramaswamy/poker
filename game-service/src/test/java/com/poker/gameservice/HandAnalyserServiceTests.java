package com.poker.gameservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.poker.gameservice.model.Card;
import com.poker.gameservice.model.Hand;
import com.poker.gameservice.model.entity.Game;
import com.poker.gameservice.model.entity.Player;
import com.poker.gameservice.util.HandAnalyser;

public class HandAnalyserServiceTests {
    private HandAnalyser handAnalyser;

    @BeforeEach
    public void setup() {
        handAnalyser = new HandAnalyser();
    }

    @Test
    public void analyseHandTestHighCard() {
        List<Card> deck = new ArrayList<Card>(List.of(
                new Card("Club", "Queen"),
                new Card("Spade", "King")));

        Player player1 = new Player().toBuilder().id(1L).deck(deck).build();
        List<Player> players = List.of(player1);

        Game game = new Game();
        game.setPlayers(players);
        List<Card> cardsOnTable = new ArrayList<>(List.of(
                new Card("Club", "2"),
                new Card("Spade", "3"),
                new Card("Diamond", "4"),
                new Card("Diamond", "5"),
                new Card("Diamond", "7")));
        game.setCardsOnTable(cardsOnTable);

        HashMap<Long, Hand> hands = handAnalyser.getHands(game);
        Hand hand1 = hands.get(1L);
        assertEquals("High Card", hand1.getName());
    }

    @Test
    public void analyseHandTestPair() {
        List<Card> deck = new ArrayList<Card>(List.of(
                new Card("Club", "10"),
                new Card("Spade", "10")));

        Player player1 = new Player().toBuilder().id(1L).deck(deck).build();
        List<Player> players = List.of(player1);

        Game game = new Game();
        game.setPlayers(players);
        List<Card> cardsOnTable = new ArrayList<>(List.of(
                new Card("Club", "2"),
                new Card("Spade", "3"),
                new Card("Diamond", "4"),
                new Card("Diamond", "5"),
                new Card("Diamond", "7")));
        game.setCardsOnTable(cardsOnTable);

        HashMap<Long, Hand> hands = handAnalyser.getHands(game);
        Hand hand1 = hands.get(1L);
        assertEquals("Pair", hand1.getName());
    }

    @Test
    public void analyseHandTestTwoPair() {
        List<Card> deck = new ArrayList<Card>(List.of(
                new Card("Club", "King"),
                new Card("Spade", "King")));

        Player player1 = new Player().toBuilder().id(1L).deck(deck).build();
        List<Player> players = List.of(player1);

        Game game = new Game();
        game.setPlayers(players);
        List<Card> cardsOnTable = new ArrayList<>(List.of(
                new Card("Club", "2"),
                new Card("Spade", "2"),
                new Card("Diamond", "4"),
                new Card("Diamond", "5"),
                new Card("Diamond", "6")));
        game.setCardsOnTable(cardsOnTable);

        HashMap<Long, Hand> hands = handAnalyser.getHands(game);
        Hand hand1 = hands.get(1L);
        assertEquals("Two Pair", hand1.getName());
    }

    @Test
    public void analyseHandTestThreeOfAKind() {
        List<Card> deck = new ArrayList<Card>(List.of(
                new Card("Club", "Ace"),
                new Card("Spade", "King")));

        Player player1 = new Player().toBuilder().id(1L).deck(deck).build();
        List<Player> players = List.of(player1);

        Game game = new Game();
        game.setPlayers(players);
        List<Card> cardsOnTable = new ArrayList<>(List.of(
                new Card("Club", "2"),
                new Card("Spade", "2"),
                new Card("Diamond", "2"),
                new Card("Diamond", "5"),
                new Card("Diamond", "6")));
        game.setCardsOnTable(cardsOnTable);

        HashMap<Long, Hand> hands = handAnalyser.getHands(game);
        Hand hand1 = hands.get(1L);
        assertEquals("Three of a Kind", hand1.getName());
    }

    @Test
    public void analyseHandTestStraightAceTo5() {
        List<Card> deck = new ArrayList<Card>(List.of(
                new Card("Club", "Ace"),
                new Card("Spade", "2")));

        Player player1 = new Player().toBuilder().id(1L).deck(deck).build();
        List<Player> players = List.of(player1);

        Game game = new Game();
        game.setPlayers(players);
        List<Card> cardsOnTable = new ArrayList<>(List.of(
                new Card("Club", "3"),
                new Card("Spade", "4"),
                new Card("Diamond", "5"),
                new Card("Diamond", "9"),
                new Card("Diamond", "9")));
        game.setCardsOnTable(cardsOnTable);

        HashMap<Long, Hand> hands = handAnalyser.getHands(game);
        Hand hand1 = hands.get(1L);
        assertEquals("Straight", hand1.getName());
    }

    @Test
    public void analyseHandTestStraight2To6WithExtra3() {
        List<Card> deck = new ArrayList<Card>(List.of(
                new Card("Club", "Queen"),
                new Card("Spade", "2")));

        Player player1 = new Player().toBuilder().id(1L).deck(deck).build();
        List<Player> players = List.of(player1);

        Game game = new Game();
        game.setPlayers(players);
        List<Card> cardsOnTable = new ArrayList<>(List.of(
                new Card("Club", "3"),
                new Card("Spade", "4"),
                new Card("Diamond", "5"),
                new Card("Diamond", "6"),
                new Card("Diamond", "3")));
        game.setCardsOnTable(cardsOnTable);

        HashMap<Long, Hand> hands = handAnalyser.getHands(game);
        Hand hand1 = hands.get(1L);
        assertEquals("Straight", hand1.getName());
    }

    @Test
    public void analyseHandTestStraight10ToAce() {
        List<Card> deck = new ArrayList<Card>(List.of(
                new Card("Club", "10"),
                new Card("Spade", "Jack")));

        Player player1 = new Player().toBuilder().id(1L).deck(deck).build();
        List<Player> players = List.of(player1);

        Game game = new Game();
        game.setPlayers(players);
        List<Card> cardsOnTable = new ArrayList<>(List.of(
                new Card("Club", "Queen"),
                new Card("Spade", "King"),
                new Card("Diamond", "Ace"),
                new Card("Diamond", "2"),
                new Card("Diamond", "3")));
        game.setCardsOnTable(cardsOnTable);

        HashMap<Long, Hand> hands = handAnalyser.getHands(game);
        Hand hand1 = hands.get(1L);
        assertEquals("Straight", hand1.getName());
    }

    @Test
    public void analyseHandTestStraight3To7() {
        List<Card> deck = new ArrayList<Card>(List.of(
                new Card("Club", "3"),
                new Card("Spade", "4")));

        Player player1 = new Player().toBuilder().id(1L).deck(deck).build();
        List<Player> players = List.of(player1);

        Game game = new Game();
        game.setPlayers(players);
        List<Card> cardsOnTable = new ArrayList<>(List.of(
                new Card("Club", "5"),
                new Card("Spade", "6"),
                new Card("Diamond", "7"),
                new Card("Diamond", "Ace"),
                new Card("Diamond", "King")));
        game.setCardsOnTable(cardsOnTable);

        HashMap<Long, Hand> hands = handAnalyser.getHands(game);
        Hand hand1 = hands.get(1L);
        assertEquals("Straight", hand1.getName());
    }

    @Test
    public void analyseHandTestStraight9ToKing() {
        List<Card> deck = new ArrayList<Card>(List.of(
                new Card("Club", "3"),
                new Card("Spade", "3")));

        Player player1 = new Player().toBuilder().id(1L).deck(deck).build();
        List<Player> players = List.of(player1);

        Game game = new Game();
        game.setPlayers(players);
        List<Card> cardsOnTable = new ArrayList<>(List.of(
                new Card("Club", "9"),
                new Card("Spade", "10"),
                new Card("Spade", "Jack"),
                new Card("Diamond", "Queen"),
                new Card("Diamond", "King")));
        game.setCardsOnTable(cardsOnTable);

        HashMap<Long, Hand> hands = handAnalyser.getHands(game);
        Hand hand1 = hands.get(1L);
        assertEquals("Straight", hand1.getName());
    }

    @Test
    public void analyseHandTestStraight3To7InDifferentOrder() {
        List<Card> deck = new ArrayList<Card>(List.of(
                new Card("Club", "3"),
                new Card("Spade", "5")));

        Player player1 = new Player().toBuilder().id(1L).deck(deck).build();
        List<Player> players = List.of(player1);

        Game game = new Game();
        game.setPlayers(players);
        List<Card> cardsOnTable = new ArrayList<>(List.of(
                new Card("Club", "4"),
                new Card("Spade", "6"),
                new Card("Spade", "7"),
                new Card("Diamond", "Queen"),
                new Card("Diamond", "King")));
        game.setCardsOnTable(cardsOnTable);

        HashMap<Long, Hand> hands = handAnalyser.getHands(game);
        Hand hand1 = hands.get(1L);
        assertEquals("Straight", hand1.getName());
    }

    @Test
    public void analyseHandTestFlush() {
        List<Card> deck = new ArrayList<Card>(List.of(
                new Card("Club", "3"),
                new Card("Club", "5")));

        Player player1 = new Player().toBuilder().id(1L).deck(deck).build();
        List<Player> players = List.of(player1);

        Game game = new Game();
        game.setPlayers(players);
        List<Card> cardsOnTable = new ArrayList<>(List.of(
                new Card("Club", "4"),
                new Card("Club", "6"),
                new Card("Spade", "8"),
                new Card("Club", "Queen"),
                new Card("Diamond", "King")));
        game.setCardsOnTable(cardsOnTable);

        HashMap<Long, Hand> hands = handAnalyser.getHands(game);
        Hand hand1 = hands.get(1L);
        assertEquals("Flush", hand1.getName());
    }

    @Test
    public void analyseHandTestFullHouse() {
        List<Card> deck = new ArrayList<Card>(List.of(
                new Card("Club", "5"),
                new Card("Spade", "9")));

        Player player1 = new Player().toBuilder().id(1L).deck(deck).build();
        List<Player> players = List.of(player1);

        Game game = new Game();
        game.setPlayers(players);
        List<Card> cardsOnTable = new ArrayList<>(List.of(
                new Card("Spade", "5"),
                new Card("Heart", "5"),
                new Card("Spade", "Ace"),
                new Card("Club", "8"),
                new Card("Diamond", "9")));
        game.setCardsOnTable(cardsOnTable);

        HashMap<Long, Hand> hands = handAnalyser.getHands(game);
        Hand hand1 = hands.get(1L);
        assertEquals("Full House", hand1.getName());
    }

    @Test
    public void analyseHandTestFourOfAKind() {
        List<Card> deck = new ArrayList<Card>(List.of(
                new Card("Club", "5"),
                new Card("Spade", "9")));

        Player player1 = new Player().toBuilder().id(1L).deck(deck).build();
        List<Player> players = List.of(player1);

        Game game = new Game();
        game.setPlayers(players);
        List<Card> cardsOnTable = new ArrayList<>(List.of(
                new Card("Spade", "5"),
                new Card("Heart", "5"),
                new Card("Club", "Ace"),
                new Card("Club", "8"),
                new Card("Diamond", "5")));
        game.setCardsOnTable(cardsOnTable);

        HashMap<Long, Hand> hands = handAnalyser.getHands(game);
        Hand hand1 = hands.get(1L);
        assertEquals("Four of a Kind", hand1.getName());
    }

    @Test
    public void analyseHandTestStraightFlush() {
        List<Card> deck = new ArrayList<Card>(List.of(
                new Card("Club", "5"),
                new Card("Club", "9")));

        Player player1 = new Player().toBuilder().id(1L).deck(deck).build();
        List<Player> players = List.of(player1);

        Game game = new Game();
        game.setPlayers(players);
        List<Card> cardsOnTable = new ArrayList<>(List.of(
                new Card("Club", "6"),
                new Card("Club", "7"),
                new Card("Spade", "Ace"),
                new Card("Club", "8"),
                new Card("Diamond", "King")));
        game.setCardsOnTable(cardsOnTable);

        HashMap<Long, Hand> hands = handAnalyser.getHands(game);
        Hand hand1 = hands.get(1L);
        assertEquals("Straight Flush", hand1.getName());
    }

    @Test
    public void analyseHandTestRoyalFlush(){
        List<Card> deck = new ArrayList<Card>(List.of(
                new Card("Diamond", "Ace"),
                new Card("Diamond", "Queen")));

        Player player1 = new Player().toBuilder().id(1L).deck(deck).build();
        List<Player> players = List.of(player1);

        Game game = new Game();
        game.setPlayers(players);
        List<Card> cardsOnTable = new ArrayList<>(List.of(
                new Card("Diamond", "10"),
                new Card("Diamond", "Jack"),
                new Card("Spade", "Ace"),
                new Card("Club", "8"),
                new Card("Diamond", "King")));
        game.setCardsOnTable(cardsOnTable);

        HashMap<Long, Hand> hands = handAnalyser.getHands(game);
        Hand hand1 = hands.get(1L);
        assertEquals("Royal Flush", hand1.getName());
    }

    @Test
    public void analyseHandTestTie(){
        List<Card> deck1 = new ArrayList<Card>(List.of(
                new Card("Club", "8"),
                new Card("Club", "9")));

        List<Card> deck2 = new ArrayList<Card>(List.of(
                new Card("Club", "4"),
                new Card("Club", "3")));

        Player player1 = new Player().toBuilder().id(1L).deck(deck1).build();
        Player player2 = new Player().toBuilder().id(2L).deck(deck2).build();
        List<Player> players = List.of(player1, player2);

        Game game = new Game();
        game.setPlayers(players);
        List<Card> cardsOnTable = new ArrayList<>(List.of(
                new Card("Spade", "6"),
                new Card("Diamond", "7"),
                new Card("Spade", "Ace"),
                new Card("Club", "5"),
                new Card("Diamond", "King")));
        game.setCardsOnTable(cardsOnTable);

        HashMap<Long, Hand> hands = handAnalyser.getHands(game);
        Hand hand1 = hands.get(1L);
        Hand hand2 = hands.get(2L);

        assertTrue(hand1.getScore() > hand2.getScore());
    }
}


