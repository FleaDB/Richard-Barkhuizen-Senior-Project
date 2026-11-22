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
    private static String minerUsed = "";

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

        List<deckLoader.DeckPlacements> decks = loader.getPlacementDeck(uLeader);
        List<List<deckLoader.DeckPlacements>> parts = BagOfCardsEvaluation.split(decks, 0.1, 13); //test seed
        List<deckLoader.DeckPlacements> training = parts.get(0);
        List<deckLoader.DeckPlacements> test = parts.get(0);
        System.out.println("\n\namount of decks that contain placements: " + decks.size()
                + "\ntraining: " + training.size()
                + "\ntest: " + test.size());

        List<List<String>> tTransaction = new ArrayList<>();
        for(deckLoader.DeckPlacements d : training)
        {
            tTransaction.add(new ArrayList<>(d.cards));
        }
        Miner miner = pickMiner(in);
        List<AprioriAlgorithm.Itemset> itemsets = miner.mine(tTransaction, minSup);
        printItemsets(itemsets, training.size(), minSup);
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
                minerUsed = "Apriori";
                return apriori::mineFrequentItemsets;
            }
            if (userPick.equals("2"))
            {
                minerUsed = "Frequent Patter Growth";
                return fgGrowth::mineFreqItemsets;
            }
            System.out.println("\n\nPlease entere either 1 or 2.");
        }
    }

    public static void printItemsets(List<AprioriAlgorithm.Itemset> itemsets, int decks, double minSup)
    {
        itemsets.sort((a, b) ->{
            if(a.count != b.count)
            {
                return b.count - a.count;
            }
            return a.cards.toString().compareTo(b.cards.toString());
        });

        System.out.println("\nUsing "+ minerUsed + " " + itemsets.size() + " itemsets in the " + decks + " training decks with minSupport " + minSup + "%\n");
        for(AprioriAlgorithm.Itemset is : itemsets)
        {
            System.out.println(is);
        }
        System.out.println("\n\n");
    }
}
