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

    //need to pass a list of lists being the card ID
    public void mineFrequentItemsets(List<List<String>> transaction, double minSup)
    {
        int decks = transaction.size();
        int minCount = (int)Math.ceil((minSup*decks)/100.0);
        HashMap<String, Integer> singleCount = new HashMap<String, Integer>();

        //realised thats why the error was here, so deck had to also be a list as its one deck at a time
        for(List<String> deck: transaction)
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
        System.out.println(singleCount);//test output with randome sets of data

        List<List<String>> currentLevel = new ArrayList<>();
        for(String card: singleCount.keySet())
        {
            if(singleCount.get(card) >= minCount)
            {
                List<String> single = new ArrayList<>();
                single.add(card);
                currentLevel.add(single);
            }
        }
        System.out.println(currentLevel);
    }

    private int countSup(List<String> candidate,List<List<String>> transaction)
    {
        int count = 0;
        for(List<String> deck: transaction)
        {
            if(deck.contains(candidate)){
                count +=1;
            }
        }
        return count;
    }
}

