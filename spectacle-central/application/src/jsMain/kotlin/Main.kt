import io.gianluigip.shopping.app
import kotlinx.browser.document

import react.dom.render

fun main() {
    render(document.getElementById("root")!!) {
        child(app)
    }
}