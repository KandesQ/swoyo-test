package model;


import java.util.List;
import java.util.Objects;

public class Topic {

    private String name;
    private List<Vote> votes;

    public Topic() {}

    public Topic(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Topic topic = (Topic) o;
        return Objects.equals(name, topic.name) && Objects.equals(votes, topic.votes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, votes);
    }

    @Override
    public String toString() {
        return "Topic{" +
                "name='" + name + '\'' +
                ", votes=" + votes +
                '}';
    }
}
