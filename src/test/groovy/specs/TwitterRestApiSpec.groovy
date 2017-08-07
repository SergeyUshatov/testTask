package specs

import data.TestData
import model.Rest
import model.Tweet
import spock.lang.Specification
import java.time.LocalDateTime

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*

class TwitterRestApiSpec extends Specification {
    
    void cleanupSpec() { Rest.destroyAllTweets() }

    def "I check tweet fields via Rest API"(){

        given: "I have generated status for tweet"
            def status = TestData.getNewStatus()

        when: "I post new tweet by Rest api"
            Rest.postNewTweet(status)
            def tweets = Rest.getHomeTimeline()

        then: "I see tweet required fields exists in response and they a not empty"
            def tweet = tweets.first()
            assertThat(tweet.created_at, not(isEmptyOrNullString()))
            assertThat(tweet.text, not(isEmptyOrNullString()))
            assertThat(tweet.retweet_count, greaterThanOrEqualTo(0))
    }

    def "I update my twitter status via Rest API"(){
        given:
            def timeline = Rest.getHomeTimeline()
            String status = TestData.getNewStatus()

        when: "I post new tweet by Rest api"
            def newTweet = Rest.postNewTweet(status)

        then: "I see new tweet is posted"
            assertThat(newTweet, isA(Tweet.class))
            assertThat(newTweet.text, equalTo(status))
            def newTimeline = Rest.getHomeTimeline()
            assertThat(newTimeline.size(), equalTo(timeline.size() + 1 ))
            assertThat(newTimeline, hasItem(newTweet))
    }

    def "I destroy my status via rest API"(){
        given: "I have a tweet"
            def status = "my new status ${LocalDateTime.now()}"
            def tweet = Rest.postNewTweet(status)
            def tweets = Rest.getHomeTimeline()

        when: "I delete tweet by id"
            def deletedTweet = Rest.destroyTweet(tweet.id_str)

        then: "I see tweet is deleted"
            def newTweets = Rest.getHomeTimeline()
            assertThat(deletedTweet, equalTo(tweet))
            assertThat(newTweets, not(hasItem(deletedTweet)))
            assertThat(newTweets.size(), equalTo(tweets.size() - 1))
    }

    def "I retweet and unretweet via Rest API"(){
        given: "I have a tweet"
            def status = "my new status ${LocalDateTime.now()}"
            def tweet = Rest.postNewTweet(status)

        when: "I retweet created tweet"
            Rest.retweetTweet(tweet.id_str)

        then: "I see tweet is retweeted"
            def retweetedTweet = Rest.getTweet(tweet.id_str)
            assertThat(retweetedTweet.retweet_count, equalTo(tweet.retweet_count + 1))
            assertThat(retweetedTweet.retweeted, is(true))

        when: "I unretweet created tweet"
            Rest.unretweetTweet(tweet.id_str)

        then: "I see tweet is unretweeted"
            def unretweetedTweet = Rest.getTweet(tweet.id_str)
            assertThat(unretweetedTweet.retweet_count, equalTo(tweet.retweet_count))
            assertThat(unretweetedTweet.retweeted, is(false))
    }

    def "Duplicated tweet should fail with Forbidden error"(){
        expect:
            Rest.duplicatedTweetFail()
    }

}
