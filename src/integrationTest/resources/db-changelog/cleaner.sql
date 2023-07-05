delete from fake_event;
delete from fake;
select setval('fake_event_sequence', max(sequence_num)) from fake_event;
select setval('fake_sequence', max(sequence_num)) from fake;
