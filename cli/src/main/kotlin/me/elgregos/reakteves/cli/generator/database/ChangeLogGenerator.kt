package me.elgregos.reakteves.cli.generator.database

import java.nio.charset.StandardCharsets
import java.nio.file.Paths
import kotlin.io.path.appendText
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.notExists

fun generateDomainChangeLog(projectDirPath: String, projectGroup: String, templateParams: Map<String, String>) {
    Paths.get("$projectDirPath/src/main/resources/db/changelog")
        .also {
            if(it.notExists()) it.createDirectories()
        }
        .resolve("db-changelog.sql")
        .also {
            if(it.notExists()) it.createFile()
        }
        .appendText(
            """
                
        -- ${templateParams["domain"]} event log

        -- changeset $projectGroup:create-${templateParams["domainTable"]}-event-table
        create table if not exists ${templateParams["domainTable"]}_event (
          id uuid primary key,
          created_at timestamp not null default (now() at time zone 'utc'),
          created_by uuid not null,
          version int not null,
          event_type varchar(100) not null,
          aggregate_id uuid not null,
          event jsonb not null);
        --rollback drop table if exists ${templateParams["domainTable"]}_event;

        -- changeset $projectGroup:create-${templateParams["domainTable"]}-table
        create table if not exists ${templateParams["domainTable"]} (
         id uuid primary key,
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