package models;

import datahandling.ArgumentRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;


public class Argument {

    private String rephrasing;
    private Discourse discourse;
    private int startIndex;
    private int endIndex;
    private String[] endingPunctuationMarks = {"!", "?", "."};

    public Argument(Discourse discourse){
        this.discourse = discourse;
    }

    public String getRephrasing(){ return this.rephrasing;}
    public Discourse getDiscourse(){return this.discourse;}
    public int getStartIndex(){return this.startIndex;}
    public int getEndIndex(){return this.endIndex;}

    public void setRephrasing(String rephrasing){this.rephrasing = rephrasing;}
    public void setDiscourse(Discourse discourse){this.discourse = discourse;}
    public void setStartIndex(int startIndex){this.startIndex = startIndex;}
    public void setEndIndex(int endIndex){this.endIndex = endIndex;}

    /**
     * The start index is invalid if the character before it isn't a sentence ending punctuation mark (. or ! or ?)
     * The exception being the very start of the text, which is valid as a start index.
     * If the start index is on whitespace characters, it is shifted to the left to the nearest non whitespace
     * character, and returns the new index if it is valid.
     * If it isn't valid, the function will return -1
     * @param startIndex the start index to check for
     * @return -1 if not valid, the start index if valid
     */
    public int isValidStartIndex(int startIndex){
        if(startIndex < 0 || startIndex >= discourse.getContent().length() - 2){
            return -1;
        }
        else {
            if(startIndex != 0) {
                String startIndexCharacter = String.valueOf(discourse.getContent().charAt(startIndex - 1));
                while (startIndex > 0 && startIndexCharacter.equals(" ")) {
                    startIndex--;
                    if(startIndex != 0) {
                        startIndexCharacter = String.valueOf(discourse.getContent().charAt(startIndex - 1));
                    }
                }
                if (!Arrays.asList(endingPunctuationMarks).contains(startIndexCharacter) && startIndex != 0) {
                    return -1;
                } else {
                    return startIndex;
                }
            }
            else{
                return startIndex;
            }
        }
    }

    /**
     * Check whether the given end indices by the user input is validation.
     * @param endIndex
     * @return endIndex if the end indices is validation otherwise return -1.
     */
    public int isValidEndIndex(int endIndex){
        if(endIndex < 1 || endIndex > discourse.getContent().length() - 1){
            return -1;
        }
        else {
            String endIndexCharacter = String.valueOf(discourse.getContent().charAt(endIndex));
            while (endIndex > 1 && endIndexCharacter.equals(" ")) {
                endIndex--;
                endIndexCharacter = String.valueOf(discourse.getContent().charAt(endIndex));
            }
            if(!Arrays.asList(endingPunctuationMarks).contains(endIndexCharacter)){
                return -1;
            }
            else{
                return endIndex;
            }
        }
    }

    /**
     * Returns true if the argument has no duplicates. A duplicate argument is an argument with the same
     * start index, end index and discourse id
     * @param startIndex the start index
     * @param endIndex the end index
     * @param argumentRepository the repository used to query the database
     * @return true if there's no duplicate argument, false if there is
     */
    public boolean hasNoDuplicates(int startIndex, int endIndex, ArgumentRepository argumentRepository){
        int count = argumentRepository.getDuplicateArgumentCount(this, startIndex, endIndex);
        if(count > 0){
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * A rephrasing is valid if it has at least one alphabet letter in it
     * @param rephrasing the rephrasing string
     * @return true if valid, false if not
     */
    public boolean isValidRephrasing(String rephrasing){
        return rephrasing.matches(".*[a-zA-Z]+.*");
    }

    public boolean insertArgumentIntoDatabase(ArgumentRepository argumentRepository){
        return argumentRepository.insertArgument(this);
    }
}
