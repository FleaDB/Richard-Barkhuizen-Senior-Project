package org.example;
import java.io.IOException;
import java.util.*;

public class Main
{
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
                System.out.print("Supp (0-100)%: ");
                minSup = Double.parseDouble(in.nextLine().trim());
                check = false;
            }
        }

        List<AprioriAlgorithm.Itemset> results = ap.mineFrequentItemsets(loader.getTransaction(uLeader), minSup);
        System.out.println(results.size() + " itemsets for " + uLeader);
        for(AprioriAlgorithm.Itemset s : results) System.out.println(s);

        System.out.println("\n\n");
        runFPGrowth(loader, uLeader, minSup);
    }
    
    public static void runFPGrowth(deckLoader loader, String leader, double minSup)
    {
        FrequentPatternGrowth fp = new FrequentPatternGrowth();
        List<AprioriAlgorithm.Itemset> results = fp.mineFreqItemsets(loader.getTransaction(leader), minSup);

        System.out.println("FP-Growth: " + results.size() + " itemsets for " + leader);
        for(AprioriAlgorithm.Itemset s : results) System.out.println(s);
    }
}
