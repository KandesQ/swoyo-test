package model;


import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Topic implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private List<Vote> votes;

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
    public String toString() {
        return "Topic{" +
                "name='" + name + '\'' +
                ", votes=" + votes +
                '}';
    }
}
