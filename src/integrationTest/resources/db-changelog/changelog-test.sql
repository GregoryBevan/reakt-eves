-- liquibase formatted sql

-- User event log

-- changeset events-k:create-fake-event-sequence
create sequence fake_event_sequence;
--rollback drop sequence fake_event_sequence;

-- changeset events-k:create-fake-event-table
create table if not exists fake_event (
  id uuid primary key,
  sequence_num bigint not null default nextval('fake_event_sequence'),
  created_at timestamp not null default (now() at time zone 'utc'),
  created_by uuid not null,
  version int not null,
  event_type varchar(100) not null,
  aggregate_id uuid not null,
  event jsonb not null);
--rollback drop table if exists fake_event;

