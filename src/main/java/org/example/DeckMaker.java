package org.example;
import java.util.*;

public class DeckMaker
{
    public static final int DECK_SIZE = 50;
    public static final int MAX_COPIES = 4;

    public static AprioriAlgorithm.Itemset getCore(List<AprioriAlgorithm.Itemset> itemsets)
    {
        AprioriAlgorithm.Itemset core = null;
        for(AprioriAlgorithm.Itemset is : itemsets)
        {
            if(core == null || is.cards.size() > core.cards.size())
            {
                core = is;
            }
        }
        return core;
    }

    public static int avgCopies(String card, List<deckLoader.DeckPlacements> decks)
    {
        int total = 0;
        int used = 0;
        for(deckLoader.DeckPlacements d : decks)
        {
            if(d.cards.contains(card))
            {
                total += d.copies.getOrDefault(card, 1);
                used++;
            }
        }
        if(used == 0)
        {
            return 1;
        }
        int copies = (int)Math.round((double)total/used);
        return Math.max(1, Math.min(MAX_COPIES, copies));
    }

    public static Map<String, Integer> makeDeck(List<String> core, List<deckLoader.DeckPlacements> decks, BagOfCards model)
    {
        Set<String> seen = new TreeSet<>();
        for(deckLoader.DeckPlacements d : decks)
        {
            seen.addAll(d.cards);
        }
        List<String> extras = new ArrayList<>();
        for(String card : seen)
        {
            if(!core.contains(card) && model.cardWeight(card) > 0)
            {
                extras.add(card);
            }
        }
    }
}
