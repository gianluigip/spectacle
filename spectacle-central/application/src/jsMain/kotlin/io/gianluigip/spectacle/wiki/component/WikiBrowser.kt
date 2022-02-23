package io.gianluigip.spectacle.wiki.component

import mui.material.Typography
import react.FC
import react.Props

const val wikiPath = "/wiki"
val WikiBrowser = FC<Props> {

    Typography { variant = "h5"; +"Wiki" }

    WikiPage {
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
    }
}