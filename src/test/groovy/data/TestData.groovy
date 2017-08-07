package data

import java.time.LocalDateTime

class TestData {

    static String getNewStatus() {
        "my new status ${LocalDateTime.now()}"
    }
}
