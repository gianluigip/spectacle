package io.gianluigip.spectacle.wiki.components

import csstype.px
import io.gianluigip.spectacle.common.components.LoadingBar
import io.gianluigip.spectacle.common.components.Spacer
import io.gianluigip.spectacle.common.utils.toNode
import io.gianluigip.spectacle.wiki.api.getWikiPage
import io.gianluigip.spectacle.wiki.api.model.WikiPageMetadataResponse
import io.gianluigip.spectacle.wiki.api.model.WikiPageResponse
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import mui.material.Autocomplete
import mui.material.AutocompleteProps
import mui.material.TextField
import mui.material.Typography
import react.FC
import react.Props
import react.create
import react.useState

external interface WikiBrowserProps : Props {
    var wikiPages: List<WikiPageMetadataResponse>
}

@Suppress("UPPER_BOUND_VIOLATED")
val WikiListBrowser = FC<WikiBrowserProps> { props ->
    val pages = props.wikiPages
    var pageSelected by useState<WikiPageMetadataResponse>()
    var wikiPage by useState<WikiPageResponse>()

    fun loadPage(page: WikiPageMetadataResponse?) {
        pageSelected = page
        MainScope().launch { wikiPage = page?.let { getWikiPage(it.id) } }
    }

    if (pages.isEmpty()) {
        Typography { +"No Wiki Pages found." }
        return@FC
    }

    Autocomplete<AutocompleteProps<WikiPageMetadataResponse>> {
        options = pages.sortedBy { it.title }.toTypedArray()
        value = pageSelected ?: emptyPage()
        renderInput = { params -> TextField.create { +params; label = "Wiki Pages".toNode() } }
        isOptionEqualToValue = { old, new -> old.id == new.id }
        getOptionLabel = { option -> option.title }
        onChange = { _, newValue: WikiPageMetadataResponse?, _, _ -> loadPage(newValue) }
    }

    Spacer { height = 10.px }
    LoadingBar { isLoading = pageSelected != null && wikiPage == null }
    wikiPage?.let {
        WikiPageViewer { wiki = it }
    }
}

private fun emptyPage() = WikiPageMetadataResponse(
    "", "", "", "", "", "", emptyList(), emptyList(), "", "", Clock.System.now(), Clock.System.now()
)
