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
        @Override
        public String toString()
        {
            return cards + " in " + count + " decks with support level " + String.format("%.2f", support) + "%";
        }
    }

    //need to pass a list of lists being the card ID
    public List<Itemset> mineFrequentItemsets(List<List<String>> transaction, double minSup)
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

        List<Itemset> result = new ArrayList<>();

        List<List<String>> currentLevel = new ArrayList<>();
        for(String card: singleCount.keySet())
        {
            if(singleCount.get(card) >= minCount)
            {
                List<String> single = new ArrayList<>();
                single.add(card);
                currentLevel.add(single);
                result.add(new Itemset(single, singleCount.get(card), (100.0*singleCount.get(card))/decks));
            }
        }
        System.out.println(currentLevel);

        //add compar in doc for the 2 below
        List<List<String>> allPairs = new ArrayList<>();
        for(int i = 0; i < currentLevel.size()-1; i++)
        {
            for(int j = i+1; j < currentLevel.size(); j++)
            {
                List<String> pair = new ArrayList<>();
                pair.add(currentLevel.get(i).get(0));
                pair.add(currentLevel.get(j).get(0));
                Collections.sort(pair);

                int count = countSup(pair, transaction);
                if(count >= minCount){
                    //System.out.println("First test" + pair + " Count " + count);
                    allPairs.add(pair);
                }
            }
        }

        List<List<String>> allFreq = new ArrayList<>(currentLevel);
        int k=2;
        while(!currentLevel.isEmpty())
        {
            List<List<String>> candidates = genCandidates(currentLevel, k);
            List<List<String>> nextLevel = new ArrayList<>();

            for(List<String> cand : candidates)
            {
                int count = countSup(cand, transaction);

                if(count >= minCount){
                    nextLevel.add(cand);
                    //System.out.println("Second Test " + cand + " " + count);
                    result.add(new Itemset(cand, count, (100.0*count)/decks));
                }
            }
            allFreq.addAll(nextLevel);
            currentLevel = nextLevel;
            k++;
        }
        return result;
    }


    private int countSup(List<String> candidate,List<List<String>> transaction)
    {
        int count = 0;
        for(List<String> deck: transaction)
        {
            if(deck.containsAll(candidate)){
                count +=1;
            }
        }
        return count;
    }

    private List<List<String>> genCandidates(List<List<String>> prevLevel, int k)
    {
        List<List<String>> candidates = new ArrayList<>();

        for(int i = 0; i < prevLevel.size()-1; i++){
            for (int j = i+1; j < prevLevel.size(); j++){
                List<String> merg = new ArrayList<>(prevLevel.get(i));
                for(String set : prevLevel.get(j))
                {
                    if(!merg.contains(set))
                    {
                        merg.add(set);
                    }
                }
                if(merg.size()!=k){
                    continue;
                }
                Collections.sort(merg);
                if (candidates.contains(merg))
                {
                    continue;
                }
                if(!allPieceFreq(merg, prevLevel))
                {
                    continue;
                }
                candidates.add(merg);
            }
        }
        return candidates;
    }

    private boolean allPieceFreq(List<String> candidates, List<List<String>> prevLevel)
    {
        for(int i=0; i < candidates.size(); i++)
        {
            List<String> part = new ArrayList<>(candidates);
            part.remove(i);
            if (!prevLevel.contains(part))
            {
                return false;
            }
        }
        return true;
    }

}

