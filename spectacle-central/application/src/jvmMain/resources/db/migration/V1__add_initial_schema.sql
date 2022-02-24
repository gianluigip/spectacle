/**
  Specifications
 */
CREATE TABLE IF NOT EXISTS specifications
(
    id            VARCHAR(100)  NOT NULL PRIMARY KEY,
    creation_time TIMESTAMP     NULL,
    update_time   TIMESTAMP     NULL,
    feature       VARCHAR(1000) NOT NULL,
    team          VARCHAR(500)  NOT NULL,
    source        VARCHAR(500)  NOT NULL,
    component     VARCHAR(500)  NOT NULL,
    name          VARCHAR(1000) NOT NULL,
    status        VARCHAR(255)  NOT NULL
);
CREATE INDEX index_specifications_feature ON specifications (feature);
CREATE INDEX index_specifications_name ON specifications (name);
CREATE INDEX index_specifications_source ON specifications (source);
CREATE INDEX index_specifications_component ON specifications (component);
CREATE INDEX index_specifications_status ON specifications (status);
CREATE INDEX index_specifications_team ON specifications (team);

/**
  Specifications Steps
 */
CREATE TABLE IF NOT EXISTS specification_steps
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    creation_time TIMESTAMP    NULL,
    update_time   TIMESTAMP    NULL,
    spec_id       VARCHAR(100) NOT NULL,
    type          VARCHAR(255) NOT NULL,
    description   TEXT         NOT NULL,
    spec_index    int          NOT NULL,
    constraint fk_specification_steps_spec_id foreign key (spec_id) references specifications (id)
);

/**
  Tags
 */
create table tags
(
    id            VARCHAR(100)  NOT NULL PRIMARY KEY,
    creation_time TIMESTAMP     NULL,
    update_time   TIMESTAMP     NULL,
    spec_id       VARCHAR(100)  NOT NULL,
    name          VARCHAR(1000) NOT NULL,
    team_name     VARCHAR(500)  NOT NULL,
    source        VARCHAR(500)  NOT NULL,
    component     VARCHAR(500)  NOT NULL,
    constraint fk_tags_spec_id foreign key (spec_id) references specifications (id)
);
CREATE INDEX index_tags_name ON tags (name);
CREATE INDEX index_tags_source ON tags (source);
CREATE INDEX index_tags_component ON tags (component);

/**
  Features
 */
CREATE TABLE IF NOT EXISTS features
(

    id            VARCHAR(100)  NOT NULL PRIMARY KEY,
    creation_time TIMESTAMP     NULL,
    update_time   TIMESTAMP     NULL,
    name          VARCHAR(1000) NOT NULL,
    description   TEXT          NOT NULL,
    source        VARCHAR(500)  NOT NULL,
    component     VARCHAR(500)  NOT NULL
);
CREATE INDEX index_features_name ON features (name);
CREATE INDEX index_features_source ON features (source);
CREATE INDEX index_features_component ON features (component);

/**
  Teams
 */
CREATE TABLE IF NOT EXISTS teams
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    creation_time TIMESTAMP    NULL,
    update_time   TIMESTAMP    NULL,
    name          VARCHAR(500) NOT NULL,
    source        VARCHAR(500) NOT NULL,
    component     VARCHAR(500) NOT NULL
);
CREATE INDEX index_teams_name ON teams (name);
CREATE INDEX index_teams_source ON teams (source);
CREATE INDEX index_teams_component ON teams (component);
