package org.example;

import java.util.*;

public class AprioriAlgorithm
{
    public static class Itemset
    {
        List<String> cards;
        int count;
        double support;

        Itemset(List<String> cards, int count, double support)
        {
            this.cards = cards;
            this.count = count;
            this.support = support;
        }
    }

    public void mineFrequentItemsets(List<String> transaction, double minSup)
    {
        int decks = transaction.size();
        int minCount = (int)Math.ceil((minSup*decks)/100.0);
        HashMap<String, Integer> singleCount = new HashMap<String, Integer>();

        for(String deck: transaction)
        {
            for(String card : deck)
            {
                if(singleCount.containsKey(card))
                {
                    singleCount.put(card, singleCount.get(card)+1);
                }
                else
                {
                    singleCount.put(card, 1);
                }
            }
        }
    }
}
