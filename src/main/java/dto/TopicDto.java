package dto;

import model.Topic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TopicDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String topicName = "";
    private String voteName = "";

    private List<VoteDto> voteDtos = new ArrayList<>();

    private String callingCmd;

    public static Topic DtoToModel(TopicDto topicDto) {
        Topic topic = new Topic();

        topic.setName(topicDto.getTopicName());
        topic.setVotes(topicDto.getVoteDtos().stream()
                .map(VoteDto::DtoToModel)
                .toList());

        return topic;
    }

    public static TopicDto ModelToDto(Topic topic) {
        TopicDto topicDto = new TopicDto();

        topicDto.setTopicName(topic.getName());
        topicDto.setVoteDtos(topic.getVotes().stream()
                .map(VoteDto::ModelToDto)
                .toList());

        return topicDto;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public List<VoteDto> getVoteDtos() {
        return voteDtos;
    }

    public void setVoteDtos(List<VoteDto> voteDtos) {
        this.voteDtos = voteDtos;
    }

    public String getCallingCmd() {
        return callingCmd;
    }

    public void setCallingCmd(String callingCmd) {
        this.callingCmd = callingCmd;
    }


    public String getVoteName() {
        return voteName;
    }

    public void setVoteName(String voteName) {
        this.voteName = voteName;
    }

    @Override
    public String toString() {
        return "TopicDto{" +
                "topicName='" + topicName + '\'' +
                ", voteName='" + voteName + '\'' +
                ", voteDtos=" + voteDtos +
                ", callingCmd='" + callingCmd + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TopicDto topicDto = (TopicDto) o;
        return Objects.equals(callingCmd, topicDto.callingCmd);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(callingCmd);
    }
}
