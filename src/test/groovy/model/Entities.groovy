package model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class Entities {
    Description description
    List<String> hashtags
    List<String> symbols
    List<UserMention> user_mentions
    List<String> urls
}
