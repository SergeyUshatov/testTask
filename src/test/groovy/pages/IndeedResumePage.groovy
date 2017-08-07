package pages

import geb.Page

class IndeedResumePage extends Page {
    static url = 'https://my.indeed.com/resume'
    static at = {editProfile.displayed}
    static content = {
        pageTitle (wait: true) { $('.title', text: 'My Indeed Resume') }
        editProfile { $('[data-shield-id="editor-section-profile"] .edit-button') }
        lastNameField { $('[data-shield-id="last-name-input"]') }
        cityField { $('[data-shield-id="personal-info-city"]') }
        saveProfile { $('.toggleable-link-save') }
    }

    String getLastName(){
        lastNameField.text()
    }

    String updateResume() {
        editProfile.click()
        String text = cityField.value()
        text = (text.contains('Kiev') || text.contains('Киев')) ? 'Kharkov' : 'Kiev'
        cityField = text
        saveProfile.click()
        text
    }
}
