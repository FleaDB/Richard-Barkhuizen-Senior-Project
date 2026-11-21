package org.example;
import java.io.IOException;
import java.util.*;

public class Main
{
    interface Miner
    {
        List<AprioriAlgorithm.Itemset> mine(List<List<String>> transaction, double minSup);
    }

    private static final int TOP_CUT = 8;
    private static final int MAX_PAIRS = 200;

    public static void main(String[] args) throws IOException
    {

        String path = "C:\\Users\\rsbar\\OneDrive\\Desktop\\AUBG\\4th year 1st Sem\\Senior Project\\Deck Data\\optcg-op15-op17-meta.json";

        deckLoader loader = new deckLoader(path);
        AprioriAlgorithm ap = new AprioriAlgorithm();

        Scanner in = new Scanner(System.in);
        boolean check = true;

        String uLeader = "";
        double minSup = 0.0;

        while(check)
        {
            System.out.print("Enter Leader code: ");
            uLeader =in.nextLine().trim().toUpperCase();

            if(loader.getTransaction(uLeader).isEmpty())
            {
                System.out.println("No leader found please enter it again\n");
            }
            else
            {
                System.out.print("Support (0-100)%: ");
                minSup = Double.parseDouble(in.nextLine().trim());
                while(check)
                {
                    if(minSup >=0 && minSup<=100)
                    {
                        check = false;
                    }
                    else
                    {
                        System.out.print("Please pick a support (0-100)%: ");
                        minSup = Double.parseDouble(in.nextLine().trim());
                    }
                }
            }
        }

        System.out.print("Which Algo 1-Apriori, 2-FP Growth: ");
        int userPic = Integer.parseInt(in.nextLine().trim());

        if(userPic == 1)
        {
            List<AprioriAlgorithm.Itemset> results = ap.mineFrequentItemsets(loader.getTransaction(uLeader), minSup);
            System.out.println(results.size() + " itemsets for " + uLeader);
            for(AprioriAlgorithm.Itemset s : results) System.out.println(s);
        }
        Miner miner = pickMiner(in);
    }

    private static Miner pickMiner(Scanner in)
    {
        AprioriAlgorithm apriori = new AprioriAlgorithm();
        FrequentPatternGrowth fgGrowth = new FrequentPatternGrowth();

        System.out.println("\nWhich algorithm would you like to use?");
        System.out.println("\n(1) Apriori - simple and easy to follow. It builds the combinations level by level and counts each one.\n" +
                "It tends to slow down when the support percentage is low or when the decks have many different (not that much in common)");
        System.out.println("\n(2) Frequent Pattern Growth - Works faster at low support percentage. It puts all the decks into one compact tree and reads the\n" +
                "combinations from it without testing candidates, however it is harder to follow its steps.");
        System.out.println("\nThey both find the same combination of cards, however your choice can dictate how fast the results are printed.");

        while(true)
        {
            System.out.println("\nWhich one would you like to use 1 or 2");
            String userPick = in.nextLine().trim();
            if(userPick.equals("1"))
            {
                return apriori::mineFrequentItemsets;
            }
            if (userPick.equals("2"))
            {
                return fgGrowth::mineFreqItemsets;
            }
            System.out.println("\n\nPlease entere either 1 or 2.");
        }

        List<deckLoader.DeckPlacements> decks = loader.getPlacementDeck(uLeader);
        List<List<deckLoader.DeckPlacements>> parts = BagOfCardsEvaluation.split(decks, 0.1, 13); //test seed
        List<deckLoader.DeckPlacements> training = parts.get(0);
        List<deckLoader.DeckPlacements> test = parts.get(0);
        System.out.println("\n\namount of decks -" + decks.size() + " training -" + training.size() + " test -" + test.size());
    }
    
    public static void runFPGrowth(deckLoader loader, String leader, double minSup)
    {
        FrequentPatternGrowth fp = new FrequentPatternGrowth();
        List<AprioriAlgorithm.Itemset> results = fp.mineFreqItemsets(loader.getTransaction(leader), minSup);

        System.out.println("FP-Growth: " + results.size() + " itemsets for " + leader);
        for(AprioriAlgorithm.Itemset s : results) System.out.println(s);
    }
}
