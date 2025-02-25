package model;

import java.util.List;
import java.util.Optional;

public class Vote {
    private String name;
    private String voteDescription;
    private int optionAmount;
    List<String> options; // immutable list

    public Vote(String name, String voteDescription, List<String> options) {
        this.name = name;
        this.voteDescription = voteDescription;
        this.options = List.copyOf(options);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVoteDescription() {
        return voteDescription;
    }

    public void setVoteDescription(String voteDescription) {
        this.voteDescription = voteDescription;
    }

    public int getOptionAmount() {
        return optionAmount;
    }

    public void setOptionAmount(int optionAmount) {
        this.optionAmount = optionAmount;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
