package specs

import joptsimple.internal.Strings
import spock.lang.Specification

import static org.hamcrest.CoreMatchers.not
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.isEmptyOrNullString


class NestedSpec extends Specification {

    def "oneLevelStep"(){
        given:
            def str = "abc"
        when:
            print str
        then:
            assertThat(str, not(isEmptyOrNullString()))
    }


    def "top level step"(){
        when:
            this.oneLevelStep()
        then:
            print " success"
    }

}
