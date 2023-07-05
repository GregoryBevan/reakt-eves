-- liquibase formatted sql

-- Fake event log

-- changeset el-gregos:create-fake-event-sequence
create sequence fake_event_sequence;
--rollback drop sequence fake_event_sequence;

-- changeset el-gregos:create-fake-event-table
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

-- changeset el-gregos:create-fake-table-sequence
create sequence fake_sequence;
--rollback drop sequence fake_sequence;

-- changeset el-gregos:create-fake-table
create table if not exists fake (
    id uuid primary key,
    sequence_num bigint not null default nextval('fake_sequence'),
    version int not null,
    created_at timestamp not null,
    created_by uuid not null,
    updated_at timestamp not null,
    updated_by uuid not null,
    details jsonb not null);
--rollback drop table if exists fake;

