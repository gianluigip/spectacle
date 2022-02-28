package io.gianluigip.spectacle.home

import csstype.Color
import csstype.Display
import csstype.JustifyContent
import csstype.TextAlign.Companion.justify
import csstype.pct
import csstype.px
import io.gianluigip.spectacle.navigation.component.GITHUB_URL
import io.gianluigip.spectacle.specification.component.specificationsReportPath
import kotlinx.js.jso
import mui.material.Box
import mui.material.Link
import mui.material.Paper
import mui.material.Stack
import mui.material.Typography
import mui.system.ResponsiveStyleValue
import react.FC
import react.Props
import react.css.css
import react.router.dom.NavLink
import react.useContext

val LandingView = FC<Props> {
    val theme by useContext(ThemeContext)

    Box {
        sx = jso {
            display = Display.flex
            justifyContent = JustifyContent.center
        }

        Paper {
            sx = jso { width = 100.pct; maxWidth = 1000.px; padding = 20.px }
            elevation = 3

            Stack {
                spacing = ResponsiveStyleValue(1)
                Typography {
                    css { color = Color(theme.palette.info.main) }
                    variant = "h5"; +"Repository of Living Documentation for Distributed Systems"
                }
                Paragraph {
                    text = """
                In modern distributed systems the behavior of the product is defined across multiple components and services, and usually developed 
                by multiple teams, as a result, the documentation is fragmented and difficult to find.
                """
                }
                Paragraph {
                    text = """       
                Additionally, the system tends to change faster than the documentation which makes the latter unreliable.
                """
                }
                Paragraph {
                    text = """       
                Spectacle provides a solution for generating documentation from the multiple codebases in a modern system and then storing it in a 
                central repository that anyone can access.
                """
                }

                NavLink {
                    to = specificationsReportPath
                    css { color = Color(theme.palette.info.main) }
                    Typography {
                        variant = "h5"; +"Specifications"
                    }
                }
                Paragraph {
                    text = """       
                You can search and bookmark any specification published, this allows you to easily share the behavior of the product with your 
                stakeholder, and to consolidate the documentation of features that could be defined across several teams and components.
                """
                }

                Typography {
                    css { color = Color(theme.palette.info.main) }
                    variant = "h6"; +"Further Instruction and Documentation in our GitHub page"
                }
                Link { href = GITHUB_URL; +GITHUB_URL }
            }
        }
    }
}

external interface ParagraphProps : Props {
    var text: String
}

val Paragraph = FC<ParagraphProps> {
    Typography {
        css { textAlign = justify }
        +it.text
    }
}
