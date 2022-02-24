package io.gianluigip.spectacle.wiki.component

import io.gianluigip.spectacle.wiki.api.model.WikiPageResponse
import kotlinx.datetime.Clock
import mui.material.Typography
import react.FC
import react.Props

const val wikiPath = "/wiki"
val WikiBrowser = FC<Props> {

    Typography { variant = "h5"; +"Wiki" }

    val wiki = WikiPageResponse(
        id = "fake",
        title = "Test Wiki",
        path = "/",
        fileName = "test.md",
        checksum = "fake1234",
        team = "Spectacle",
        tags = emptyList(),
        features = emptyList(),
        source = "hardcoded",
        component = "Hardcoded",
        creationTime = Clock.System.now(),
        updateTime = Clock.System.now(),
        content = """
            # Test Wiki
            ## TODO         
            * [ ] todo
            * [x] done
            
            
            ## Code Examples
            
            ```js
            var s = "JavaScript syntax highlighting";
            alert(s);
            ```

            ```python
            s = "Python syntax highlighting"
            print s
            ```
            
            ## Diagrams
            
            ```mermaid
            graph TD;
                A-->B;
                A-->C;
                B-->D;
                C-->D;
            ```
            
            **Second Diagram**
            
            ```mermaid
            graph LR
                Start --> Stop
            ```                        
        """.trimIndent()
    )
    WikiPageViewer { this.wiki = wiki }
}