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

        test.add(Arrays.asList(
                "EB04-002", "OP13-016", "OP15-035", "OP01-016", "ST21-003",
                "OP14-022", "OP14-031", "OP13-027", "OP13-118", "OP15-032",
                "EB04-007", "OP13-040", "OP05-038", "OP08-036", "EB02-021"
        ));

        test.add(Arrays.asList(
                "EB04-002", "OP13-016", "OP15-035", "EB02-017", "ST21-003",
                "OP14-022", "OP14-031", "OP13-027", "OP13-118", "OP15-032",
                "EB04-007", "OP06-118", "OP05-038", "OP08-036"
        ));

        test.add(Arrays.asList(
                "OP14-102", "P-096", "OP11-106", "OP15-047", "OP14-110",
                "OP06-104", "OP14-111", "OP12-112", "OP15-113", "EB03-053",
                "EB04-058", "EB03-055", "OP14-104", "OP13-042", "EB03-060"
        ));

        test.add(Arrays.asList(
                "OP14-102", "P-096", "OP11-106", "OP15-047", "OP14-110",
                "OP12-112", "OP14-111", "PRB02-008", "OP15-113", "EB03-053",
                "EB04-058", "OP07-051", "EB03-055", "OP14-104", "OP13-042",
                "EB03-060"
        ));

        AprioriAlgorithm miner = new AprioriAlgorithm();
        miner.mineFrequentItemsets(test, 50); //test with 50 as 60 isnt working, need larger data size to make it more efficient
    }
}
