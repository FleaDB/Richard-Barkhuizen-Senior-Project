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
}
