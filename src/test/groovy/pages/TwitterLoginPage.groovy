package pages

import geb.Page

class TwitterLoginPage extends Page {
    static url = 'https://twitter.com/'
    static at = { loginForm.isDisplayed() }
    static content = {
        loginForm { $('.signin') }
        usernameField { $('#signin-email') }
        passwordField { $('#signin-password') }
        loginButton { $('.signin button.submit') }
    }
}
