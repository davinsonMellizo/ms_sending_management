DELETE FROM schalerd.client c where c.document_number = 123;

INSERT INTO schalerd.client
(document_number, id_document_type, key_mdm, enrollment_origin, id_state, creation_user, created_date, modified_date)
VALUES(123, 1, 'key', 'APP', 1, 'user', '2021-02-16 10:10:25-05', '2021-02-16 10:10:25-05');

INSERT INTO schalerd.contact
(document_number, id_document_type, segment, id_contact_medium, value, id_state, created_date, modified_date, previous)
VALUES(123,1, 'Personas', 0, '3217937584', 1, '2021-02-16 10:10:25-05','2021-02-16 10:10:25-05', false);

