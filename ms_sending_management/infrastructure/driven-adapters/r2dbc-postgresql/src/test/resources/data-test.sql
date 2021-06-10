INSERT INTO document_type
(id, code, name)
VALUES(0, 'CC', 'Cedula de Ciudadania');

INSERT INTO state
(id, name)
VALUES(0, 'Activo'),
(1, 'Inactivo');

INSERT INTO enrollment_contact
(id, code)
VALUES(0, 'ALM');

INSERT INTO contact_medium
(id, code) values
(0, 'SMS'),
(1, 'MAIL');

INSERT INTO provider
(id, name, type, creation_user, created_date)
values
(0, 'TODO 1', 'enail', 'Davinson', '2021-02-16 10:10:25-05'),
(1, 'TODO 1', 'sms', 'Davinson', '2021-02-16 10:10:25-05');


INSERT INTO remitter
(id, remitter_mail, state, creation_user, created_date)
VALUES(0, 'dmellizo@bancolombia.com.co', 0, 'Davinson', '2021-02-16 10:10:25-05');

INSERT INTO service
(id, name, creation_user, created_date)
VALUES(0, 'email', 'Davinson', '2021-02-16 10:10:25-05');

INSERT INTO template_alert
(id, fiel, initial_position, final_position, creation_user, created_date)
VALUES(0, 'campo1', 0, 10, 'davinson', '2021-02-16 10:10:25-05');

INSERT INTO alert
(id, id_template, id_provider_mail, id_provider_sms, id_remitter, id_service, description, nature, obligatory, message, priority, subject_mail, visible_channel, path_attached_mail, attention_line, id_state, creation_user, created_date)
VALUES('HGD', 0, 0, 1, 0, 0, 'Alerta', 'NM', false, 'Alerta', 0, 'Alerta', false, 'Alerta', '3216549', 0, 'UserName', '2021-02-16 10:10:25-05'),
('UPD', 0, 0, 1, 0, 0, 'Alerta', 'NM', false, 'Alerta', 0, 'Alerta', false, 'Alerta', '3216549', 0, 'UserName', '2021-02-16 10:10:25-05');


INSERT INTO client
(document_number, document_type, key_mdm, enrollment_origin, id_state, creation_user, created_date, modified_date, optinal)
VALUES(1061772353, 0, 'KEY', 'ALM', 0, 'Kevin', '2021-02-16 10:10:25-05', '2021-02-16 10:10:25-05', '');

INSERT INTO contact
(id_enrollment_contact, id_contact_medium, document_number, document_type, value, id_state, created_date, modified_date)
VALUES(0, 1, 1061772353, 0, 'dmellizo@bancolombia.com.co', 0, '2021-02-16 10:10:25-05', '2021-02-16 10:10:25-05'),
(0, 0, 1061772353, 0, '1061772353', 0, '2021-02-16 10:10:25-05', '2021-02-16 10:10:25-05');
