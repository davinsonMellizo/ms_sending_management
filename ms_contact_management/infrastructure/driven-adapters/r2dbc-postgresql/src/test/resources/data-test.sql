INSERT INTO document_type
(id, code, name)
VALUES(0, 'CC', 'Cedula de Ciudadania');

INSERT INTO state
(id, name)
VALUES(0, 'Active');

INSERT INTO enrollment_contact
(id, code)
VALUES(0, 'ALM');

INSERT INTO contact_medium
(id, code) values
(0, 'SMS'),
(1, 'MAIL');


INSERT INTO client
(document_number, document_type, key_mdm, enrollment_origin, id_state, creation_user, created_date, modified_date, optinal)
VALUES(1061772353, 0, 'KEY', 'ALM', 0, 'Kevin', '2021-02-16 10:10:25-05', '2021-02-16 10:10:25-05', '');

INSERT INTO contact
(id_enrollment_contact, id_contact_medium, document_number, document_type, value, id_state, created_date, modified_date)
VALUES(0, 0, 1061772353, 0, '1061772353', 0, '2021-02-16 10:10:25-05', '2021-02-16 10:10:25-05');



