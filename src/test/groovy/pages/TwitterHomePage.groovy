package pages

import geb.Page
import modules.TweetModule

class TwitterHomePage extends Page{
    static at = { userDropDown.isDisplayed() }
    static content = {
        userDropDown { $('#user-dropdown') }
        timelineTweetBtn { $('.timeline-tweet-box button.tweet-action')  }
        timelineTweetField { $('#tweet-box-home-timeline') }
        timelineTweetCounter { $('.timeline-tweet-box .tweet-counter') }
        tweets { module TweetModule }
        message { $('#message-drawer .message-text') }
        closeMessage (wait: true) { $('.message .dismiss') }
        deleteTweetConfirmationBtn { $('.delete-action') }
        retweetConfirmBtn { $('.tweet-button .retweet-action') }
    }

    def postNewStatus(String status){
        timelineTweetField.click()
        def script = "(document.getElementById('tweet-box-home-timeline')).innerText ='${status}'"
        js.exec(script)
        waitFor { timelineTweetCounter.text().toInteger() < 140 }
        timelineTweetBtn.click()
        waitFor { tweets.tweetText.any { t -> t.text().contains(status) } }
        if (closeMessage.displayed) { closeMessage.click() }
    }

    def deleteTweet(String status){
        def tweetIndex = tweets.tweetText.findIndexOf { tweet -> (tweet.text() == status) }
        tweets.tweetDropDown[tweetIndex].click()
        waitFor { tweets.deleteTweetBtn[tweetIndex].displayed }
        tweets.deleteTweetBtn[tweetIndex].click()
        waitFor { deleteTweetConfirmationBtn.displayed }
        deleteTweetConfirmationBtn.click()
    }

    def retweetTweetByIndex(int index){
        tweets.retweetBtn[index].click()
        waitFor { retweetConfirmBtn.displayed }
        retweetConfirmBtn.click()
        waitFor {tweets.unretweetBtn[index].displayed}
    }

    def unretweetTweetByIndex(int index){
        tweets.unretweetBtn[index].click()
    }
}
