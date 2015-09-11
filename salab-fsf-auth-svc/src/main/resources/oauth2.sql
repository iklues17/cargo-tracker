CREATE SEQUENCE hibernate_sequence
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

create table oauth_client_details (
  client_id VARCHAR(256) PRIMARY KEY,
  resource_ids VARCHAR(256),
  client_secret VARCHAR(256),
  scope VARCHAR(256),
  authorized_grant_types VARCHAR(256),
  web_server_redirect_uri VARCHAR(256),
  authorities VARCHAR(256),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additional_information VARCHAR(4096),
  autoapprove VARCHAR(256)
)
WITH (
  OIDS=FALSE
);

create table oauth_client_token (
  token_id VARCHAR(256),
  token bytea,
  authentication_id VARCHAR(256) PRIMARY KEY,
  user_name VARCHAR(256),
  client_id VARCHAR(256)
)
WITH (
  OIDS=FALSE
);

create table oauth_access_token (
  token_id VARCHAR(256),
  token bytea,
  authentication_id VARCHAR(256) PRIMARY KEY,
  user_name VARCHAR(256),
  client_id VARCHAR(256),
  authentication bytea,
  refresh_token VARCHAR(256)
)
WITH (
  OIDS=FALSE
);

create table oauth_refresh_token (
  token_id VARCHAR(256),
  token bytea,
  authentication bytea
)
WITH (
  OIDS=FALSE
);

create table oauth_code (
  code VARCHAR(256), authentication bytea
)
WITH (
  OIDS=FALSE
);

create table oauth_approvals (
	userId VARCHAR(256),
	clientId VARCHAR(256),
	scope VARCHAR(256),
	status VARCHAR(10),
	expiresAt TIMESTAMP,
	lastModifiedAt TIMESTAMP
)
WITH (
  OIDS=FALSE
);

CREATE TABLE fsf_user
(
  id bigint NOT NULL,
  created_by character varying(50) NOT NULL,
  created_date timestamp without time zone NOT NULL,
  last_modified_by character varying(50) NOT NULL,
  last_modified_date timestamp without time zone NOT NULL,
  activated boolean NOT NULL,
  activation_key character varying(20),
  email character varying(100),
  first_name character varying(50),
  lang_key character varying(5),
  last_name character varying(50),
  login character varying(50) NOT NULL,
  password character varying(100) NOT NULL,
  mobile_phone_number character varying(100),
  authority_base character varying(50),
  login_failure_count INTEGER,
  password_expired_date timestamp without time zone,
  password_updated_date timestamp without time zone,
  CONSTRAINT fsf_user_pkey PRIMARY KEY (id),
  CONSTRAINT uk_buk4r0o8evx2b40lql6umufwv UNIQUE (email),
  CONSTRAINT uk_sa4df0alibrd29bxd4c9e5371 UNIQUE (login)
)
WITH (
  OIDS=FALSE
);

CREATE TABLE fsf_authority
(
  name character varying(50) NOT NULL,
  CONSTRAINT fsf_authority_pkey PRIMARY KEY (name)
)
WITH (
  OIDS=FALSE
);

CREATE TABLE fsf_user_authority
(
  user_id bigint NOT NULL,
  authority_name character varying(50) NOT NULL,
  CONSTRAINT fsf_user_authority_pkey PRIMARY KEY (user_id, authority_name),
  CONSTRAINT fk_9dkl26rn1bl6vbc9r2oi1qi7s FOREIGN KEY (authority_name)
      REFERENCES fsf_authority (name) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_m1yvuskfgyyrk1wjgjb4a8f5a FOREIGN KEY (user_id)
      REFERENCES fsf_user (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

CREATE TABLE fsf_client_grant_default_user_authority
(
  client_id character varying(255) NOT NULL,
  authority_id character varying(50) NOT NULL,
  CONSTRAINT fsf_client_grant_default_user_authority_pkey PRIMARY KEY (client_id, authority_id),
  CONSTRAINT fk_f7rl6q4ooue6gn885osk08m45 FOREIGN KEY (client_id)
      REFERENCES oauth_client_details (client_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_fbbf5mv8cl7avs243j7ojn09q FOREIGN KEY (authority_id)
      REFERENCES fsf_authority (name) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

CREATE TABLE fsf_user_event_history
(
  id bigint NOT NULL,
  event_time timestamp without time zone NOT NULL,
  event_type character varying(255),
  username character varying(255),
  CONSTRAINT fsf_user_event_history_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);