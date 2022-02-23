/**
  Wiki
 */
CREATE TABLE IF NOT EXISTS wiki_pages
(
    id            VARCHAR(100)  NOT NULL PRIMARY KEY,
    creation_time TIMESTAMP     NULL,
    update_time   TIMESTAMP     NULL,
    title         VARCHAR(1000) NOT NULL,
    path          VARCHAR(1000) NOT NULL,
    content       TEXT          NOT NULL,
    checksum      VARCHAR(1000) NOT NULL,
    team          VARCHAR(255)  NOT NULL,
    source        VARCHAR(255)  NOT NULL,
    component     VARCHAR(255)  NOT NULL
);
CREATE INDEX index_wiki_title ON wiki_pages (title);
CREATE INDEX index_wiki_source ON wiki_pages (source);
CREATE INDEX index_wiki_component ON wiki_pages (component);
CREATE INDEX index_wiki_team ON wiki_pages (team);

/**
  Wiki Features
 */
CREATE TABLE IF NOT EXISTS wiki_page_features
(
    id            VARCHAR(100)  NOT NULL PRIMARY KEY,
    creation_time TIMESTAMP     NULL,
    update_time   TIMESTAMP     NULL,
    wiki_id       VARCHAR(100)  NOT NULL,
    name          VARCHAR(1000) NOT NULL,
    constraint fk_wiki_features_wiki_id foreign key (wiki_id) references wiki_pages (id)
);
CREATE INDEX index_wiki_features_name ON wiki_page_features (name);

/**
  Wiki Tags
 */
CREATE TABLE IF NOT EXISTS wiki_page_tags
(
    id            VARCHAR(100)  NOT NULL PRIMARY KEY,
    creation_time TIMESTAMP     NULL,
    update_time   TIMESTAMP     NULL,
    wiki_id       VARCHAR(100)  NOT NULL,
    name          VARCHAR(1000) NOT NULL,
    constraint fk_wiki_tags_wiki_id foreign key (wiki_id) references wiki_pages (id)
);
CREATE INDEX index_wiki_tags_name ON wiki_page_tags (name);
