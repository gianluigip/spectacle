package io.gianluigip.spectacle.wiki.component

import csstype.pct
import csstype.px
import io.gianluigip.spectacle.common.component.Spacer
import io.gianluigip.spectacle.wiki.api.model.WikiPageResponse
import kotlinx.datetime.Clock
import kotlinx.js.jso
import mui.material.Grid
import mui.material.GridDirection
import mui.material.Paper
import mui.material.Typography
import mui.system.ResponsiveStyleValue
import react.FC
import react.Props

private const val EXPLORER_SIZE = 3
const val wikiPath = "/wiki"
val WikiBrowser = FC<Props> {

    Grid {
        container = true
        spacing = ResponsiveStyleValue(20.px)
        direction = ResponsiveStyleValue(GridDirection.row)

        Grid {
            item = true
            xs = EXPLORER_SIZE

            Paper {
                sx = jso { padding = 20.px; height = 100.pct }
                elevation = 2

                Typography { variant = "h5"; +"Explorer" }
                Spacer { height = 10.px }
                WikiDirectoryExplorer {}
            }
        }

        Grid {
            item = true
            xs = 12 - EXPLORER_SIZE
            Paper {
                sx = jso { padding = 20.px }
                elevation = 2
                MarkdownViewer {
                    content = landingContent()
                }
            }
        }
    }

}

private fun landingContent(): String = """
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
