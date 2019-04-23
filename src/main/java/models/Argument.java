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
     *
     * @param startIndex
     * @return
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

    public boolean hasNoDuplicates(int startIndex, int endIndex, ArgumentRepository argumentRepository){
        int count = argumentRepository.getDuplicateArgumentCount(this, startIndex, endIndex);
        if(count > 0){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean isValidRephrasing(String rephrasing){
        return rephrasing.matches(".*[a-zA-Z]+.*");
    }

    public boolean insertArgumentIntoDatabase(ArgumentRepository argumentRepository){
        return argumentRepository.insertArgument(this);
    }
}
