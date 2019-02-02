package utilities;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class HighScores {
    // this is the HighScores class which contains all the functionality of the program which concerns high scores
    private ArrayList<Integer> highScores = new ArrayList<>();
    private BufferedWriter writer;
    private FileWriter fw;

    public HighScores(){
    //empty constructor
    }

    public void addHighScore(int newHighScore){
        // this method adds the newHighScore value to the list of highScores then saves it to a file
        highScores.add(newHighScore);
        saveScoresToFile();
    }

    public ArrayList<Integer> returnList(){
        // this method returns the list of high scores
        return highScores;
    }
    public void saveScoresToFile(){
        // this method saves all the high scores into a file called 'snakerSaveFile.txt'
        try {
            fw = new FileWriter("snakerSaveFile.txt");
            writer = new BufferedWriter(fw);
            String highScore = "";
            for (int i = 0; i < highScores.size(); i++){
                if (highScores.get(i) != null) {
                    highScore += highScores.get(i) + "\n";
                }
            }
            writer.write(highScore);
            writer.flush();

            System.out.println("File saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadScoresFromFile(){
        // this method loads the high scores from a file called 'snakerSaveFile.txt'
        // while appropriately handling exceptions
        try{
            File file = new File("snakerSaveFile.txt");
            BufferedReader in = new BufferedReader(new FileReader(file));
            String temp;
            while((temp = in.readLine()) != null){
                if (temp.equals("0")){
                    highScores.add(0);
                } else {
                    highScores.add(Integer.parseInt(temp));
                }
            }
        }catch (FileNotFoundException e){
            System.out.println("File not found");
        } catch (IOException e){
            System.out.println("io exception");
        }
    }
}
