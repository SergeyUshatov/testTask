package pages

import geb.Page

class IndeedLoginPage extends Page {
    static url = "http://www.indeed.com/stc?_ga=2.233722652.329882276.1502098198-972169144.1501850417"
    static at = { signInLink.isDisplayed() }
    static content = {
        signInLink (wait: true) { $("#userOptionsLabel") }
        emailField (wait: true) { $("#signin_email") }
        passwordField { $("#signin_password") }
        signInBtn { $(".btn-signin") }
    }

    def login(String usermail, String password) {
        signInLink.click()
        emailField = usermail
        passwordField = password
        signInBtn.click()
    }
}
