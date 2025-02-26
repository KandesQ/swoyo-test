package dto;

import model.Vote;

import java.io.Serializable;
import java.util.List;

public class VoteDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String voteDescription;
    private int optionAmount;
    private List<String> options; // immutable list

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
        voteDto.setOptionAmount(voteDto.getOptionAmount());
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

}
