package dto;

import model.Topic;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;

public class TopicDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private List<VoteDto> voteDtos;

    public static Topic DtoToModel(TopicDto topicDto) {
        Topic topic = new Topic();

        topic.setName(topicDto.getName());
        topic.setVotes(topicDto.getVoteDtos().stream()
                .map(VoteDto::DtoToModel)
                .toList());

        return topic;
    }

    public static TopicDto ModelToDto(Topic topic) {
        TopicDto topicDto = new TopicDto();

        topicDto.setName(topicDto.getName());
        topicDto.setVoteDtos(topic.getVotes().stream()
                .map(VoteDto::ModelToDto)
                .toList());

        return topicDto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<VoteDto> getVoteDtos() {
        return voteDtos;
    }

    public void setVoteDtos(List<VoteDto> voteDtos) {
        this.voteDtos = voteDtos;
    }
}
