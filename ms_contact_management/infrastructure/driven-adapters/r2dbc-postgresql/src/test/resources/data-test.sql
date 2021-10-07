INSERT INTO document_type
(id, code, name)
VALUES(0, 'CC', 'Cedula de Ciudadania');

INSERT INTO state
(id, name)
VALUES(0, 'Active');

INSERT INTO consumer
(id, segment)
VALUES('ALM', 'GNR');

INSERT INTO contact_medium
(id, code) values
(0, 'SMS'),
(1, 'MAIL');


INSERT INTO client
(id,document_number, id_document_type, key_mdm, enrollment_origin, id_state, creation_user, created_date, modified_date)
VALUES(0,1061772353, 0, 'KEY', 'ALM', 0, 'Kevin', '2021-02-16 10:10:25-05', '2021-02-16 10:10:25-05'),
      (1,1061772354, 0, 'KEY', 'ALM', 0, 'Kevin', '2021-02-16 10:10:25-05', '2021-02-16 10:10:25-05');

INSERT INTO contact
(id, segment, id_contact_medium, document_number, id_document_type, value, id_state, created_date, modified_date, previous)
VALUES(0, 'ALM', 0, 1061772353, 0, '1061772353', 0, '2021-02-16 10:10:25-05', '2021-02-16 10:10:25-05', false);



