package org.example;
import java.util.*;

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

    }
}
