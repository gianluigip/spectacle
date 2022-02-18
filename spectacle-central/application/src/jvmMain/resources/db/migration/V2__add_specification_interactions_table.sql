/**
  Specifications Interactions
 */
CREATE TABLE IF NOT EXISTS specification_interactions
(
    id            VARCHAR(100)  NOT NULL PRIMARY KEY,
    creation_time TIMESTAMP     NULL,
    update_time   TIMESTAMP     NULL,
    spec_id       VARCHAR(100)  NOT NULL,
    source        VARCHAR(255)  NOT NULL,
    component     VARCHAR(255)  NOT NULL,
    direction     VARCHAR(255)  NOT NULL,
    type          VARCHAR(255)  NOT NULL,
    name          VARCHAR(1000) NOT NULL,
    metadata      TEXT          NOT NULL,
    constraint fk_specification_steps_spec_id foreign key (spec_id) references specifications (id)
);
CREATE INDEX index_specification_interactions_name ON specification_interactions (name);
