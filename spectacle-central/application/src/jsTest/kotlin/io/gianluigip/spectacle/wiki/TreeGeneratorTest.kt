package io.gianluigip.spectacle.wiki

import io.gianluigip.spectacle.dsl.assertions.shouldBe
import io.gianluigip.spectacle.common.fixtures.aWikiMetadata
import kotlin.test.Test

class TreeGeneratorTest {

    @Test
    fun generateWikiTreeShouldDecomposeThePagesMetadata() {
        val pages = listOf(
            aWikiMetadata(component = "C1", path = "/", fileName = "F1"),
            aWikiMetadata(component = "C1", path = "/dir1/dir2", fileName = "F2"),
            aWikiMetadata(component = "C1", path = "/dir1/dir3", fileName = "F3"),
            aWikiMetadata(component = "C1", path = "/", fileName = "F4"),
            aWikiMetadata(component = "C1", path = "/dir1", fileName = "F5"),
            aWikiMetadata(component = "C2", path = "/", fileName = "F6"),
            aWikiMetadata(component = "C2", path = "/", fileName = "F7"),
            aWikiMetadata(component = "C3", path = "/dir1/dir2/dir3", fileName = "F8"),
        )

        TreeGenerator.generateWikiTree(pages) shouldBe listOf(
            ComponentWiki(
                component = "C1",
                rootDir = WikiDirectory(
                    path = "/C1",
                    name = "C1",
                    pages = listOf(
                        aWikiMetadata(component = "C1", path = "/", fileName = "F1"),
                        aWikiMetadata(component = "C1", path = "/", fileName = "F4")
                    ),
                    directories = listOf(
                        WikiDirectory(
                            path = "/C1/dir1",
                            name = "dir1",
                            pages = listOf(aWikiMetadata(component = "C1", path = "/dir1", fileName = "F5")),
                            directories = listOf(
                                WikiDirectory(
                                    path = "/C1/dir1/dir2",
                                    name = "dir2",
                                    pages = listOf(aWikiMetadata(component = "C1", path = "/dir1/dir2", fileName = "F2")),
                                    directories = listOf()
                                ),
                                WikiDirectory(
                                    path = "/C1/dir1/dir3",
                                    name = "dir3",
                                    pages = listOf(aWikiMetadata(component = "C1", path = "/dir1/dir3", fileName = "F3")),
                                    directories = listOf()
                                )
                            )
                        )
                    ),
                )
            ),
            ComponentWiki(
                component = "C2",
                rootDir = WikiDirectory(
                    path = "/C2",
                    name = "C2",
                    pages = listOf(
                        aWikiMetadata(component = "C2", path = "/", fileName = "F6"),
                        aWikiMetadata(component = "C2", path = "/", fileName = "F7"),
                    ),
                    directories = listOf(),
                )
            ),
            ComponentWiki(
                component = "C3",
                rootDir = WikiDirectory(
                    path = "/C3",
                    name = "C3",
                    pages = listOf(),
                    directories = listOf(
                        WikiDirectory(
                            path = "/C3/dir1",
                            name = "dir1",
                            pages = emptyList(),
                            directories = listOf(
                                WikiDirectory(
                                    path = "/C3/dir1/dir2",
                                    name = "dir2",
                                    pages = emptyList(),
                                    directories = listOf(
                                        WikiDirectory(
                                            path = "/C3/dir1/dir2/dir3",
                                            name = "dir3",
                                            pages = listOf(aWikiMetadata(component = "C3", path = "/dir1/dir2/dir3", fileName = "F8")),
                                            directories = listOf()
                                        )
                                    )
                                )
                            )
                        )
                    ),
                )
            ),
        )
    }
}