package org.example;
import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        String path = "C:\\Users\\rsbar\\OneDrive\\Desktop\\AUBG\\4th year 1st Sem\\Senior Project\\Deck Data\\optcg-op15-op17-meta.json";

        deckLoader loader = new deckLoader(path);

        loader.printDecks();
    }
}
