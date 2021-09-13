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
(id, name, type_service, creation_user, created_date)
values
('HJK', 'TODO 1', 'E', 'Davinson', '2021-02-16 10:10:25-05'),
('FGH', 'TODO 1', 'E', 'Davinson', '2021-02-16 10:10:25-05'),
('JKL', 'TODO 1', 'S', 'Davinson', '2021-02-16 10:10:25-05');


INSERT INTO remitter
(id, mail, state, creation_user, created_date)
VALUES(0, 'dmellizo@bancolombia.com.co', 'Activo', 'Davinson', '2021-02-16 10:10:25-05'),
(1, 'dmellizo@bancolombia.com.co', 'Activo', 'Davinson', '2021-02-16 10:10:25-05');

INSERT INTO service
(id, name, creation_user, state, created_date)
VALUES(0, 'email', 'Davinson', "Activo", '2021-02-16 10:10:25-05');

INSERT INTO alert_template
(id, field, initial_position, final_position, creation_user, created_date)
VALUES(0, 'campo1', 0, 10, 'davinson', '2021-02-16 10:10:25-05');

INSERT INTO alert
(id, id_template, id_provider_mail, id_provider_sms, id_remitter, id_category, description, nature, obligatory, message, priority, subject_mail, visible_channel, path_attached_mail, attention_line, id_state, creation_user, created_date)
VALUES('HGD', 0, 1, 1, 0, 1, 'Alerta', 'NM', false, 'Alerta', 0, 'Alerta', false, 'Alerta', '3216549', 0, 'UserName', '2021-02-16 10:10:25-05'),
('UPD', 0, 1, 1, 0, 1, 'Alerta', 'NM', false, 'Alerta', 0, 'Alerta', false, 'Alerta', '3216549', 0, 'UserName', '2021-02-16 10:10:25-05');


INSERT INTO client
(document_number, document_type, key_mdm, enrollment_origin, id_state, creation_user, created_date, modified_date)
VALUES(1061772353, 0, 'KEY', 'ALM', 0, 'Kevin', '2021-02-16 10:10:25-05', '2021-02-16 10:10:25-05');

INSERT INTO contact
(id_enrollment_contact, id_contact_medium, document_number, document_type, value, id_state, created_date, modified_date)
VALUES(0, 1, 1061772353, 0, 'dmellizo@bancolombia.com.co', 0, '2021-02-16 10:10:25-05', '2021-02-16 10:10:25-05'),
(0, 0, 1061772353, 0, '1061772353', 0, '2021-02-16 10:10:25-05', '2021-02-16 10:10:25-05');

INSERT INTO alert_transaction
(id_alert, id_consumer, id_transaction, creation_user, created_date)
VALUES('HGD', 'BLP', '0520', 'user', '2021-02-16 10:10:25-05'),
('HGD', 'BLM', '0256', 'user', '2021-02-16 10:10:25-05');

INSERT INTO alert_client
(id_alert, id_client, number_operations, amount_enable, accumulated_operations, accumulated_amount, association_origin, creation_user, created_date, modified_date, transaction_date)
VALUES('HGD', 1, 5, 2, 1, 4, 'tst', 'tst1', '2023-05-20 04:00:00-00', '2023-05-20 04:00:00-00', '2023-05-20 04:00:00-00'),
('HGD', 1, 1, 3, 4, 5, 'tsa', 'tst2', '2023-05-20 04:00:00-00', '2023-05-20 04:00:00-00', '2023-05-20 04:00:00-00');

INSERT INTO alert_template
(id, field, initial_position, final_position, creation_user, created_date)
VALUES(1, 'field', 1, 6, 'user', '2023-05-20 04:00:00-00');

INSERT INTO consumer
(id, segment)
VALUES(0, '123aaa'),
(1, '456bbb');

INSERT INTO priority
(id, segment)
VALUES(5, '123aaa', 1, "user", '2023-05-20 04:00:00-00'),
(5, '123b', 1, "user2", '2023-05-20 04:05:00-00');

INSERT INTO category
(id, name, creation_user, created_date)
VALUES(1, 'cat1', "userSys", '2023-05-20 04:00:00-00'),
(2, 'cat2', "userSys", '2023-05-20 04:05:00-00');

INSERT INTO provider_service
(id_provider, id_service)
VALUES("TOD", 1),
("MAS", 1);


