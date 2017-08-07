package model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class Tweet {
    String created_at
    float id
    String id_str
    String text
    boolean  truncated
    Entities entities
    String source
    float in_reply_to_status_id
    String in_reply_to_status_id_str
    float in_reply_to_user_id
    String in_reply_to_user_id_str
    String in_reply_to_screen_name
    User user
    String geo
    String coordinates
    String place
    String contributors
    boolean is_quote_status
    int retweet_count
    int favorite_count
    boolean favorited
    boolean retweeted
    String lang

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof Tweet)) return false

        Tweet tweet = (Tweet) o

        if (Float.compare(tweet.id, id) != 0) return false
        if (id_str != tweet.id_str) return false
        if (text != tweet.text) return false

        return true
    }

    int hashCode() {
        int result
        result = (id != +0.0f ? Float.floatToIntBits(id) : 0)
        result = 31 * result + id_str.hashCode()
        result = 31 * result + text.hashCode()
        return result
    }


    @Override
    public String toString() {
        return "Tweet{" +
                "created_at='" + created_at + '\'' +
                ", id_str='" + id_str + '\'' +
                ", text='" + text + '\'' +
                ", retweet_count=" + retweet_count +
                ", retweeted=" + retweeted +
                '}';
    }
}
