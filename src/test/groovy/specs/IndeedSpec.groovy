package specs

import geb.spock.GebSpec
import pages.IndeedHomePage
import pages.IndeedLoginPage
import pages.IndeedResumePage

import static org.hamcrest.CoreMatchers.not
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.equalTo

class IndeedSpec extends GebSpec {

    def "IUpdateMyIndeedProfile"(){
        given:
            def page = to IndeedLoginPage
            at page
            def config = new ConfigSlurper().parse(new File('src/test/groovy/data/data.groovy').toURI().toURL())
            page.login(config.testdata.indeed.usermail, config.testdata.indeed.password)
            at IndeedHomePage
            page = to IndeedResumePage
            at IndeedResumePage
        when:
            String city = page.updateResume()
        then:
            println 'xvdfb'
    }
}
