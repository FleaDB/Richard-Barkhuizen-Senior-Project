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
        test.add(Arrays.asList("EB04-002", "OP13-016", "OP15-035"));
        test.add(Arrays.asList("EB04-002", "OP13-016", "ST21-003"));
        test.add(Arrays.asList("EB04-002", "OP13-016", "OP15-035", "ST21-003"));
        test.add(Arrays.asList("EB04-002", "OP15-035"));
        test.add(Arrays.asList("OP13-016", "OP15-035"));

        AprioriAlgorithm miner = new AprioriAlgorithm();
        miner.mineFrequentItemsets(test, 60);   //60% of 5 decks = minCount 3
    }
}
