package io.gianluigip.spectacle.home

import csstype.pct
import csstype.px
import io.gianluigip.spectacle.common.components.Spacer
import io.gianluigip.spectacle.common.components.xs
import io.gianluigip.spectacle.component.components.ComponentList
import io.gianluigip.spectacle.feature.components.FeaturesList
import io.gianluigip.spectacle.home.Themes.SPACE_PADDING
import io.gianluigip.spectacle.wiki.api.model.WikiPageMetadataResponse
import io.gianluigip.spectacle.wiki.components.WikiDirectoryExplorer
import io.gianluigip.spectacle.wiki.components.wikiPath
import kotlinx.js.jso
import mui.material.Grid
import mui.material.GridDirection
import mui.material.Paper
import mui.material.Typography
import mui.material.styles.TypographyVariant.h5
import mui.system.responsive
import react.FC
import react.Props
import react.router.useNavigate

val LandingView = FC<Props> {
    val navigate = useNavigate()

    fun navigateToWikiPage(page: WikiPageMetadataResponse) {
        navigate.invoke("$wikiPath?id=${page.id}")
    }

    Grid {
        container = true
        columnSpacing = responsive(SPACE_PADDING)
        direction = responsive(GridDirection.row)
        sx = jso { height = 100.pct }

        Grid {
            xs = 3; item = true
            Paper {
                sx = jso { padding = SPACE_PADDING; height = 100.pct }
                elevation = 2

                Typography { variant = h5; +"Wiki Explorer" }
                Spacer { height = 10.px }
                WikiDirectoryExplorer {
                    hideSearchBar = true
                    onPageSelected = { navigateToWikiPage(it) }
                }
            }
        }

        Grid {
            xs = 6; item = true
            Paper {
                sx = jso { padding = SPACE_PADDING; height = 100.pct }
                elevation = 2

                Typography { variant = h5; +"Features" }
                Spacer { height = 10.px }
                FeaturesList {}
            }
        }

        Grid {
            xs = 3; item = true
            Paper {
                sx = jso { padding = SPACE_PADDING; height = 100.pct }
                elevation = 2

                Typography { variant = h5; +"Components" }
                Spacer { height = 10.px }
                ComponentList { }
            }
        }
    }
}
