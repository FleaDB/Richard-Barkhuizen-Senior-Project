package org.example;
import org.junit.Test;

import java.util.*;
import static org.junit.Assert.*;

public class ProjectTests
{
    private List<List<String>> sampleDecks()
    {
        List<List<String>> decks = new ArrayList<>();
        decks.add(Arrays.asList("CardA", "CardB", "CardC"));
        decks.add(Arrays.asList("CardA", "CardB"));
        decks.add(Arrays.asList("CardA", "CardC"));
        decks.add(Arrays.asList("CardA", "CardB", "CardC"));
        decks.add(Arrays.asList("CardB", "CardC"));
        return decks;
    }

    private int countOf(List<AprioriAlgorithm.Itemset> itemsets, String cards)
    {
        for(AprioriAlgorithm.Itemset is : itemsets)
        {
            List<String> sorted = new ArrayList<>(is.cards);
            Collections.sort(sorted);
            if(String.join(",", sorted).equals(cards));
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
}
