package User;

import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class User {
    private String nickname;
    private String password;
    private AtomicInteger score;
    private Vector<String> friends;
    private AtomicInteger use;

    public User(String nick, String pwd){
        this.nickname = nick;
        this.password = pwd;
        this.score = new AtomicInteger(0);
        this.friends = new Vector<>();
        this.use = new AtomicInteger(0);
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public AtomicInteger getScore() {
        return score;
    }

    public void addScore(Integer score) {
        this.score.addAndGet(score);
    }

    public Integer getUse() {
        return use.intValue();
    }

    public void incrementUse(){
        use.getAndIncrement();
    }

    public void decrementUse(){
        if(use.get() > 0) use.decrementAndGet();
    }

    public Vector<String> getFriends() {
        return friends;
    }


}

