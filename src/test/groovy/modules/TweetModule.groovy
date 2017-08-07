package modules

import geb.Module

class TweetModule extends Module {

    static content = {
        tweetItems { $('[data-item-type="tweet"]') }
        tweetText { $('.tweet-text') }
        tweetCreatedAt { $('.time') }
        retweetBtn { $('.js-toggleRt [title="Retweet"]') }
        unretweetBtn { $('.js-toggleRt [title="Undo retweet"]') }
        tweetDropDown { $('.stream-item-header .dropdown-toggle') }
        deleteTweetBtn { $('.stream-item-header .js-actionDelete button') }
    }

    def getRetweetCountElementFor(String id){
        def locator = "div[data-item-id='${id}'] [aria-label='Tweet actions'] .ProfileTweet-action--retweet .ProfileTweet-actionButtonUndo .ProfileTweet-actionCount .ProfileTweet-actionCountForPresentation"
        sleepForNSeconds(1)
        def elements = $(locator)
        def text = elements.first().text()
        text ? text.toInteger() : 0
    }

    def sleepForNSeconds(int seconds) {
        def originalMilliseconds = System.currentTimeMillis()
        waitFor() { (System.currentTimeMillis() - originalMilliseconds) > (seconds * 1000) }
    }
}
