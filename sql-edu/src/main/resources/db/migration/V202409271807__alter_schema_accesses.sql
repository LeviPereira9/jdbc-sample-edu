ALTER TABLE accesses
    ADD COLUMN create_data BOOLEAN default FALSE,
    ADD COLUMN read_data BOOLEAN default FALSE,
    ADD COLUMN update_data BOOLEAN default FALSE,
    ADD COLUMN delete_data BOOLEAN default FALSE;