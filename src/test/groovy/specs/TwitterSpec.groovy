package specs

import data.TestData
import geb.spock.GebReportingSpec
import model.Rest
import pages.TwitterHomePage
import pages.TwitterLoginPage

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*

class TwitterSpec extends GebReportingSpec {

    def username = 'testtask0007'
    def password = 'qwertyzaq'

    def setup(){
        driver.manage().window().maximize()
        def page = to TwitterLoginPage
        page.usernameField = username
        page.passwordField = password
        page.loginButton.click()
        at TwitterHomePage
    }
    void cleanupSpec() { Rest.destroyAllTweets() }

    def "I post new status"(){
        given:
            def hp = at TwitterHomePage
            def tweetsCountByRest = Rest.getHomeTimeline().size()
            def tweetsCountUI = tweetsCountByRest > 0 ? hp.tweets.tweetItems.size() : tweetsCountByRest
            def status = TestData.getNewStatus()

        when: "I post new tweet from home page"
            hp.postNewStatus(status)

        then: "I see tweet is posted"
            def newTweetText = hp.tweets.tweetText.first().text()
            def newTweetTime = hp.tweets.tweetCreatedAt.first().text()
            def newTweetRetweetBtn = hp.tweets.retweetBtn.first().displayed
            assertThat(newTweetText, not(isEmptyOrNullString()))
            assertThat(newTweetText, equalTo(status))
            assertThat(newTweetTime, not(isEmptyOrNullString()))
            assertThat(newTweetRetweetBtn, is(true))
            def newTweetsCountByRest = Rest.getHomeTimeline().size()
            def newTweetsCountUI = hp.tweets.tweetItems.size()
            assertThat(newTweetsCountByRest, equalTo(tweetsCountByRest + 1))
            assertThat(newTweetsCountUI, equalTo(tweetsCountUI + 1))
    }

    def "Duplicated tweet should fail UI"(){
        given: "I have generated status for tweet"
            def hp = at TwitterHomePage
            def status = TestData.getNewStatus()

        when: "I post new tweet from home page"
            hp.postNewStatus(status)

        and: "I duplicate a tweet"
            def tweetsByRest = Rest.getHomeTimeline()
            def tweetsUI = hp.tweets.tweetItems
            hp.postNewStatus(status)

        then: "I see duplicated tweet is not posted"
            waitFor{ hp.message.displayed }
            assertThat(hp.message.text(), equalTo('You have already sent this Tweet.'))
            def newTweetsByRest = Rest.getHomeTimeline()
            def newTweetsUI = hp.tweets.tweetItems
            assertThat(newTweetsUI.size(), equalTo( tweetsUI.size()))
            assertThat(newTweetsByRest.size(), equalTo(tweetsByRest.size()))
    }

    def "I delete my status via UI"(){
        given: "I have a tweet"
            def hp = at TwitterHomePage
            def status = TestData.getNewStatus()
            hp.postNewStatus(status)
            def tweetsByRest = Rest.getHomeTimeline()
            def tweetsUI = hp.tweets.tweetItems

        when: "I delete tweet"
            hp.deleteTweet(status)

        then: "I see tweet is deleted"
            waitFor{ hp.message.displayed }
            assertThat(hp.message.text(), equalTo('Your Tweet has been deleted.'))
            def newTweetsByRest = Rest.getHomeTimeline()
            def newTweetsUI = hp.tweets.tweetItems
            assertThat(hp.tweets.tweetText.any{ t -> t.text() == status }, is(false))
            assertThat(newTweetsByRest.size(), equalTo(tweetsByRest.size() - 1))
            assertThat(newTweetsUI.size(), equalTo(tweetsUI.size() - 1))
    }

    def "I retweet and unretweet a tweet via UI"(){
        given: "I have a tweet"
            def hp = at TwitterHomePage
            def status = TestData.getNewStatus()
            hp.postNewStatus(status)
            waitFor{hp.tweets.tweetText.any { t -> t.text().contains(status) } }
            def tweetIndex = hp.tweets.tweetText.findIndexOf { tweet -> (tweet.text() == status) }
            def tweetId = hp.tweets.tweetItems[tweetIndex].attr('data-item-id')
            def retweetsCount = hp.tweets.getRetweetCountElementFor(tweetId)

        when: "I retweet created tweet on UI"
            hp.retweetTweetByIndex(tweetIndex)

        then: "I see tweet is retweeted on UI"
            def retweetsCountInc = hp.tweets.getRetweetCountElementFor(tweetId)
            assertThat(retweetsCountInc, equalTo(retweetsCount + 1))

        when: "I unretweet created tweet on UI"
            hp.unretweetTweetByIndex(tweetIndex)

        then: "I see tweet is unretweeted on UI"
            def retweetsCountDec = hp.tweets.getRetweetCountElementFor(tweetId)
            assertThat(retweetsCountDec, equalTo(retweetsCount))
    }
}
