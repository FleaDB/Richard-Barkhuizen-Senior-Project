package org.example;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import static org.junit.Assert.*;

public class ProjectTests
{
    private List<List<String>> sampleDecks()
    {
        List<List<String>> decks = new ArrayList<>();
        decks.add(Arrays.asList("A", "B", "C"));
        decks.add(Arrays.asList("A", "B"));
        decks.add(Arrays.asList("A", "C"));
        decks.add(Arrays.asList("A", "B", "C"));
        decks.add(Arrays.asList("B", "D"));
        return decks;
    }

    private int countOf(List<AprioriAlgorithm.Itemset> itemsets, String cards)
    {
        for(AprioriAlgorithm.Itemset is : itemsets)
        {
            List<String> sorted = new ArrayList<>(is.cards);
            Collections.sort(sorted);
            if(String.join(",", sorted).equals(cards))
            {
                return is.count;
            }
        }
        return 0;
    }

    public void checkMiner(ItemsetMiner miner)
    {
        List<AprioriAlgorithm.Itemset> result = miner.mine(sampleDecks(), 60);

        assertEquals(5, result.size());
        assertEquals(4, countOf(result, "A"));
        assertEquals(4, countOf(result, "B"));
        assertEquals(3, countOf(result, "C"));
        assertEquals(3, countOf(result, "A,B"));
        assertEquals(3, countOf(result, "A,C"));
        assertEquals(0, countOf(result, "B,C"));
    }

    @Test
    public void aprioirMatchesCount()
    {
        checkMiner(new AprioriAlgorithm());
    }

    @Test
    public void zeroSup()
    {
        ItemsetMiner miner = new AprioriAlgorithm();
        for(AprioriAlgorithm.Itemset is : miner.mine(sampleDecks(), 0))
        {
            assertTrue(is.count >= 1);
        }
    }

    @Test
    public void loaderDeckRead () throws IOException
    {
        String sampleJson = "[{\"leader_card_code\":\"Leader1\",\"deck\":[{\"card_id\":\"A\",\"count\":2},{\"card_id\":\"A\",\"count\":2}],\"placement\":\"3\"}]";
        File file = File.createTempFile("decks", ".json");
        FileWriter writer = new FileWriter(file);
        writer.write(sampleJson);
        writer.close();

        deckLoader loader = new deckLoader(file.getPath());
        assertTrue(loader.getTransaction("NOPE").isEmpty());
        assertEquals(1, loader.getTransaction("Leader1").get(0).size());//a needs to get one
        assertEquals(Integer.valueOf(4), loader.getPlacementDeck("Leader1").get(0).copies.get("A"));
    }

    @Test
    public void spearman()
    {
        double[] idk = {1,2,3,4,5};

        assertEquals(1.0, BagOfCardsEvaluation.spearman(idk, new double[]{10,20,30,40,50}), 0.0001);
        assertEquals(-1.0, BagOfCardsEvaluation.spearman(idk, new double[]{5,4,3,2,1}), 0.0001);
        assertEquals(0.8, BagOfCardsEvaluation.spearman(idk, new double[]{2,1,4,3,5}), 0.0001);
    }

    private deckLoader.DeckPlacements makeDeck(int placement, Set<String> cards)
    {
        deckLoader.DeckPlacements deck = new deckLoader.DeckPlacements(cards, placement);
        for(String card : cards)
        {
            deck.copies.put(card, 4);
        }
        return deck;
    }

    @Test
    public void splitTrainingAndTest()
    {
        List<deckLoader.DeckPlacements> decks = new ArrayList<>();
        for(int i = 1; i <= 20; i++)
        {
            Set<String> cards = new HashSet<>();
            cards.add("A");
            decks.add(makeDeck(i, cards));
        }
        List<List<deckLoader.DeckPlacements>> part = BagOfCardsEvaluation.split(decks, 0.2, 42);
        List<List<deckLoader.DeckPlacements>> reRun = BagOfCardsEvaluation.split(decks, 0.2, 42);

        assertEquals(16, part.get(0).size());
        assertEquals(4, part.get(1).size());
        assertEquals(part.get(1), reRun.get(1));

        for(deckLoader.DeckPlacements test : part.get(1))
        {
            assertFalse(part.get(0).contains(test));
        }
    }

    @Test
    public void winningCardsAgainstLosingCards()
    {
        List<deckLoader.DeckPlacements> training = new ArrayList<>();
        for(int placement = 1; placement <= 20; placement++)
        {
            Set<String> cards = new HashSet<>();
            cards.add("n");
            cards.add(placement <= 8 ? "Good" : "Bad");
            training.add(makeDeck(placement, cards));
        }
        BagOfCards model = new BagOfCards(training, new ArrayList<>(), 8, 0);

        Set<String> goodDeck = new HashSet<>();
        Set<String> badDeck = new HashSet<>();
        goodDeck.add("Good");
        badDeck.add("Bad");

        assertTrue(model.rating(goodDeck)>model.rating(badDeck));
    }

    @Test
    public void suggestedDeckContainsFullList()
    {
        List<deckLoader.DeckPlacements> training = new ArrayList<>();
        for(int placement = 1; placement <= 20; placement++)
        {
            Set<String> cards = new HashSet<>();
            int amt = placement <= 8 ? 40 : 10;
            for(int i = 0; i < amt; i++)
            {
                cards.add("A" + i);
            }
            training.add(makeDeck(placement, cards));
        }
        BagOfCards model = new BagOfCards(training, new ArrayList<>(), 8, 0);

        List<String> core = new ArrayList<>();
        core.add("A0");
        Map<String, Integer> suggested = DeckMaker.makeDeck(core, training, model);

        int total = 0;
        for(int copies : suggested.values())
        {
            assertTrue(copies >= 1 && copies <=4);
            total += copies;
        }
        assertEquals(50, total);
    }
}
