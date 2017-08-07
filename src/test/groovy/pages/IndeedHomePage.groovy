package pages

import geb.Page

class IndeedHomePage extends Page {
    static at = {userMenu.displayed}
    static content = {
        userMenu (wait: true) { $('#userOptionsLabel') }
        resumeMenuItem (wait: true) { $('a', href: '/promo/resume') }
    }

    def openMyResume() {
        userMenu.click()
        resumeMenuItem.click()
    }
}
