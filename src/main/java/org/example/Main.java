package org.example;
import java.io.IOException;
import java.util.*;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        /*
        String path = "C:\\Users\\rsbar\\OneDrive\\Desktop\\AUBG\\4th year 1st Sem\\Senior Project\\Deck Data\\optcg-op15-op17-meta.json";

        deckLoader loader = new deckLoader(path);

        loader.printDecks(); */
        List<List<String>> test = new ArrayList<>();
        test.add(Arrays.asList("A", "B", "C"));
        test.add(Arrays.asList("A", "B", "D"));
        test.add(Arrays.asList("A", "B", "C", "D"));
        test.add(Arrays.asList("A", "C"));
        test.add(Arrays.asList("B", "C"));

        AprioriAlgorithm miner = new AprioriAlgorithm();
        miner.mineFrequentItemsets(test, 60);   // 60% of 5 decks = minCount 3
    }
}
