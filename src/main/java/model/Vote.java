package model;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Vote {
    private String name;
    private String voteDescription;
    private int optionAmount;
    List<String> options; // immutable list

    // для Dto
    public Vote() {}

    public Vote(String name, String voteDescription, List<String> options) {
        this.name = name;
        this.voteDescription = voteDescription;
        this.options = List.copyOf(options);
        this.optionAmount = this.options.size();
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return optionAmount == vote.optionAmount && Objects.equals(name, vote.name) && Objects.equals(voteDescription, vote.voteDescription) && Objects.equals(options, vote.options);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, voteDescription, optionAmount, options);
    }
}
