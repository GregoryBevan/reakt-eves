package me.elgregos.reakteves.cli.generator.database

import org.gradle.api.Project
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Paths

fun generateDomainChangeLog(project: Project, templateParams: Map<String, String>) {

    File(Paths.get(project.projectDir.path,"src/main/resources/db/changelog/db-changelog.sql").toUri())
        .appendText(
            """
                
        -- ${templateParams["domain"]} event log
        
        -- changeset ${project.group}:create-${templateParams["domainTable"]}-event-sequence
        create sequence ${templateParams["domainTable"]}_event_sequence;
        --rollback drop sequence ${templateParams["domainTable"]}_event_sequence;

        -- changeset ${project.group}:create-${templateParams["domainTable"]}-event-table
        create table if not exists ${templateParams["domainTable"]}_event (
          id uuid primary key,
          sequence_num bigint not null default nextval('${templateParams["domainTable"]}_event_sequence'),
          created_at timestamp not null default (now() at time zone 'utc'),
          created_by uuid not null,
          version int not null,
          event_type varchar(100) not null,
          aggregate_id uuid not null,
          event jsonb not null);
        --rollback drop table if exists ${templateParams["domainTable"]}_event;

        -- changeset ${project.group}:create-${templateParams["domainTable"]}-table-sequence
        create sequence ${templateParams["domainTable"]}_sequence;
        --rollback drop sequence ${templateParams["domainTable"]}_sequence;

        -- changeset ${project.group}:create-${templateParams["domainTable"]}-table
        create table if not exists ${templateParams["domainTable"]} (
         id uuid primary key,
         sequence_num bigint not null default nextval('${templateParams["domainTable"]}_sequence'),
         version int not null,
         created_at timestamp not null,
         created_by uuid not null,
         updated_at timestamp not null,
         updated_by uuid not null,
         details jsonb not null);
        --rollback drop table if exists ${templateParams["domainTable"]};
        
    """.trimIndent(), StandardCharsets.UTF_8
        )
}