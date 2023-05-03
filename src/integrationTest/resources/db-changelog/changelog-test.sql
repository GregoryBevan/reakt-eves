-- liquibase formatted sql

-- User event log

-- changeset events-k:create-test-event-sequence
create sequence test_event_sequence;
--rollback drop sequence test_event_sequence;

-- changeset events-k:create-test-event-table
create table if not exists test_event (
  id uuid primary key,
  sequence_num bigint not null default nextval('test_event_sequence'),
  created_at timestamp not null default (now() at time zone 'utc'),
  created_by uuid not null,
  version int not null,
  event_type varchar(100) not null,
  aggregate_id uuid not null,
  event jsonb not null);
--rollback drop table if exists test_event;

