package model

import data.TestData
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.apache.http.HttpStatus

import static io.restassured.RestAssured.given
import static org.hamcrest.Matchers.equalTo

class Rest {
    private static String consumerKey = 'aj0b54YotK0DEeq6aLscF1a2O'
    private static String consumerSecret = 'ZhgEXQf9BPo1ZR2Vf4VeC02NfOQ1Jl4Lvzh1QN7jpQdatwNZ5f'
    private static String token = '890547957229080576-ARulWtE5J45zmR9lxZ4e87M1PFjQSbj'
    private static String tokenSecret = 'NPnSTbPMCgzSYH4ZuBhJaywrho4C27FiHZKNsA2gbjecA'
    private static String baseUrl = "https://api.twitter.com/1.1/"
    private static String homeTimelineUrl = "${baseUrl}statuses/home_timeline.json"
    private static String updateStatusUrl = "${baseUrl}statuses/update.json?status="
    private static String destroyTweetUrl = "${baseUrl}statuses/destroy/"
    private static String retweetUrl = "${baseUrl}statuses/retweet/"
    private static String unretweetUrl = "${baseUrl}statuses/unretweet/"
    private static String showTweetUrl = "${baseUrl}statuses/show/"

    private static RequestSpecification givenRestClientOauth(){
        given().auth().oauth(consumerKey, consumerSecret, token, tokenSecret)
    }

    static void destroyAllTweets() {
        def tweets = getHomeTimeline()
        if (tweets.size() > 0) {
            tweets.each {t -> destroyTweet(t.id_str)}
        }
    }

    static List<Tweet> getHomeTimeline(){
        givenRestClientOauth()
            .contentType(ContentType.JSON)
        .when()
            .get(homeTimelineUrl)
        .then()
            .statusCode(HttpStatus.SC_OK)
            .extract().body().as(Tweet[].class) as List<Tweet>
    }

    static Tweet getTweet(String id) {
        def url = "${showTweetUrl}${id}.json"
        givenRestClientOauth()
        .when()
            .get(url)
        .then()
            .statusCode(HttpStatus.SC_OK)
            .extract().body().as(Tweet.class)
    }

    static Tweet postNewTweet(String status){
        def url = "${updateStatusUrl}${status}"
        givenRestClientOauth()
            .contentType(ContentType.JSON)
        .when()
            .post(url)
        .then()
            .statusCode(HttpStatus.SC_OK)
            .extract().body().as(Tweet.class)
    }

    static Tweet destroyTweet(String id){
        def url = "${destroyTweetUrl}${id}.json"
        givenRestClientOauth()
        .when()
            .post(url)
        .then()
            .statusCode(HttpStatus.SC_OK)
            .extract().body().as(Tweet.class)
    }

    static def retweetTweet(String id) {
        def url = "${retweetUrl}${id}.json"
        givenRestClientOauth()
        .when()
            .post(url)
        .then()
            .statusCode(HttpStatus.SC_OK)
    }

    static def unretweetTweet(String id) {
        def url = "${unretweetUrl}${id}.json"
        givenRestClientOauth()
        .when()
            .post(url)
        .then()
            .statusCode(HttpStatus.SC_OK)
    }

    static def duplicatedTweetFail(){
        String status = TestData.getNewStatus()
        def newTweet = postNewTweet(status)
        def url = "${updateStatusUrl}${newTweet.text}"
        givenRestClientOauth()
            .contentType(ContentType.JSON)
        .when()
            .post(url)
        .then()
            .statusCode(HttpStatus.SC_FORBIDDEN)
            .assertThat().body("errors.message[0]", equalTo("Status is a duplicate."))
    }
}
