package dto;

import model.Vote;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VoteDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String voteDescription = "";
    private int optionAmount;
    private List<String> options = new ArrayList<>();

    public static Vote DtoToModel(VoteDto voteDto) {
        Vote vote = new Vote();

        vote.setName(voteDto.name);
        vote.setVoteDescription(voteDto.voteDescription);
        vote.setOptions(voteDto.options);
        vote.setOptionAmount(voteDto.optionAmount);

        return vote;
    }

    public static VoteDto ModelToDto(Vote vote) {
        VoteDto voteDto = new VoteDto();

        voteDto.setName(vote.getName());
        voteDto.setOptions(vote.getOptions());
        voteDto.setOptionAmount(vote.getOptionAmount());
        voteDto.setVoteDescription(vote.getVoteDescription());

        return voteDto;
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
        VoteDto voteDto = (VoteDto) o;
        return Objects.equals(name, voteDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
