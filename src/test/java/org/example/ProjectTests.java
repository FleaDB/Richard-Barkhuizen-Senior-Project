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
}
