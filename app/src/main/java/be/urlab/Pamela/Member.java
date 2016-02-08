package be.urlab.Pamela;

/**
 * Created by julianschembri on 7/02/16.
 */

public class Member extends Object {
    private boolean has_key;
    private String username;
    private String gravatar;

    public Member(boolean has_key, String username,String gravatar) {
        this.has_key = has_key;
        this.username = username;
        this.gravatar = gravatar;
    }

    public String getUserName() {
        return this.username;
    }

    public String getGravatar() {
        return this.gravatar;
    }

    public boolean hasKey() {
        return this.has_key;
    }
}
