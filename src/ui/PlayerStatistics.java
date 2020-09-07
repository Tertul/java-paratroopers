package ui;

import exceptions.InvalidStatsFileException;
import model.Game;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class PlayerStatistics {

    public int score;
    public int totalShots;
    public int sessionShots;
    private final ArrayList<Entry> unsortedEntries;
    public List<Entry> highscores;
    private final int MAX_HIGHSCORES = 10;
    private final String dMY;
    private final Game game;

    private String statsFile;
    public PlayerStatistics(Game game) {
        this.game = game;
        score = 0;
        unsortedEntries = new ArrayList<>();
        highscores = new ArrayList<>();
        dMY = new SimpleDateFormat("dd/MM/yy").format(Calendar.getInstance().getTime());
        try {
            statsFile = "stats.txt";       //TODO if not valid, loading highscores will result in index error because it doesnt exist
            loadStats();
            System.out.println("loaded stats");
        } catch (IOException | NullPointerException ioe) {
            //System.out.println("Invalid stats file -- creating a temporary file");
            //File.createTempFile("tempStats", ".txt");
            //File newFile = File.createNew
            //File tempFile = new File(".//tempFile.txt");
            statsFile = "tempfile.txt";
        }
    }

    public void newGame(){
        sessionShots = 0;
        score = 0;
    }


    private void loadStats() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(statsFile));
        int tempTotal = 0;

        for (int s = 0 ; s < MAX_HIGHSCORES ; s++){
                String line = lines.get(s);
                ArrayList<String> partsOfLine = splitOnHyphen(line);
                ArrayList<String> date = splitOnSpace(partsOfLine.get(0));
                String name = partsOfLine.get(1);
                ArrayList<String> shots = splitOnSpace(partsOfLine.get(2));
                ArrayList<String> scores = splitOnSpace(partsOfLine.get(3));
                Entry newEntry = new Entry(name,date.get(1),Integer.parseInt(shots.get(1)), Integer.parseInt(scores.get(1)));

                unsortedEntries.add(newEntry);

                tempTotal += Integer.parseInt(shots.get(1));
        }

        totalShots =  tempTotal;

        sortScores();
        highscores.addAll(unsortedEntries);
    }

    private static ArrayList<String> splitOnSpace(String line) {
        String[] splits = line.split(" ");
        return new ArrayList<>(Arrays.asList(splits));
    }


    public void checkScore (){
        for (int i = 0; i < highscores.size() ; i++){
            if (score > highscores.get(i).score){
                // NOTE: reimplement as JTable
                addIncompleteEntry(i);
                game.scorePanel.askForName(i);
                break;
            }
        }

        game.scorePanel.highscoreTable.updateTable();
        game.scorePanel.restartPanel.setVisible(true);
    }

    void addIncompleteEntry(int i){
        Entry newHighScoreEntry = new Entry("",dMY,sessionShots,score);
        highscores.add(i,newHighScoreEntry);

        if (highscores.size() > MAX_HIGHSCORES) {
            highscores.remove(MAX_HIGHSCORES);
        }
    }

    void addNameToEntry(int i, String name){
        highscores.get(i).name = name;
        game.scorePanel.newHighscoreLabel.setVisible(false);
        game.scorePanel.restartPanel.setVisible(true);
    }

    public void writeStatsToFile() throws IOException, InvalidStatsFileException {
        if (!(statsFile.equals("stats.txt"))) {
            throw new InvalidStatsFileException();
        } else {
            System.out.println("saving stats");
            PrintWriter writer = new PrintWriter("stats.txt");

            for (Entry e : highscores){
                writer.println("Date: " + e.date + " - " + e.name + " - " + "Shots: " + e.shots + " - " + "Score: " + e.score);
            }

            writer.close();
        }
    }


    public void sortScores(){
        unsortedEntries.sort(new EntrySorter());
    }

    List<Entry> getHighscores(){
        return highscores;
    }

    private ArrayList<String> splitOnHyphen(String line){
        String[] splits = line.split(" - ");
        return new ArrayList<>(Arrays.asList(splits));
    }

    public class Entry{
        String name;
        final String date;
        public final int score;
        final int shots;

        public Entry(String name, String date,int shots, int score){
            this.name = name;
            this.date = date;
            this.score = score;
            this.shots = shots;
        }
    }

    class EntrySorter implements Comparator<Entry>{

        public int compare(Entry a, Entry b){
            return b.score - a.score;
        }
    }
}

