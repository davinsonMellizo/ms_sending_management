INSERT INTO document_type(id, code, name) VALUES
(0, 'CC', 'Cedula de Ciudadania');

INSERT INTO state (id, name) VALUES
(0, 'Activo'),
(1, 'Inactivo');

INSERT INTO enrollment_contact(id, code) VALUES
(0, 'ALM');

INSERT INTO contact_medium(id, code) VALUES
(0, 'SMS'),
(1, 'MAIL');

INSERT INTO provider(id, name, type_service, creation_user, created_date) VALUES
('HJK', 'TODO 1', 'E', 'Davinson', '2021-02-16 10:10:25-05'),
('FGH', 'TODO 1', 'E', 'Davinson', '2021-02-16 10:10:25-05'),
('JKL', 'TODO 1', 'S', 'Davinson', '2021-02-16 10:10:25-05');

INSERT INTO remitter(id, mail, state, creation_user, created_date) VALUES
(0, 'dmellizo@bancolombia.com.co', 'Activo', 'Davinson', '2021-02-16 10:10:25-05'),
(1, 'dmellizo@bancolombia.com.co', 'Activo', 'Davinson', '2021-02-16 10:10:25-05'),
(2, 'dmellizo@bancolombia.com.co', 'Activo', 'Davinson', '2021-02-16 10:10:25-05');

INSERT INTO alert_template(id, field, initial_position, final_position, creation_user, created_date) VALUES
(0, 'campo1', 0, 10, 'davinson', '2021-02-16 10:10:25-05');

INSERT INTO priority (id, code, description, id_provider, creation_user, created_date) VALUES(1, 1, 'description', 'HJK', 'user', '2023-05-20 04:00:00-00'),
(2, 1, 'description', 'HJK', 'user2', '2023-05-20 04:05:00-00');

INSERT INTO category(id, name, creation_user, created_date) VALUES
(1, 'cat1', 'userSys', '2023-05-20 04:00:00-00'),
(2, 'cat2', 'userSys', '2023-05-20 04:05:00-00');

INSERT INTO alert(id, template_name, id_provider_mail, id_provider_sms, id_remitter, id_category, description, nature, obligatory, message, priority, subject_mail, visible_channel, path_attached_mail, attention_line, id_state,basic_kit, push, creation_user, created_date) VALUES
('HGD', 'Compra', 'FGH', 'FGH', 0, 1, 'Alerta', 'NM', false, 'Alerta', 1, 'Alerta', true, 'Alerta', '3216549', 0, true,'SI', 'UserName', '2021-02-16 10:10:25-05'),
('HGS', 'Compra', 'FGH', 'FGH', 0, 1, 'Alerta', 'NM', false, 'Alerta', 1, 'Alerta', true, 'Alerta', '3216549', 0, true,'SI', 'UserName', '2021-02-16 10:10:25-05'),
('UPD', 'Compra', 'FGH', 'FGH', 0, 1, 'Alerta', 'NM', false, 'Alerta', 2, 'Alerta', false, 'Alerta', '3216549', 0, true,'NO', 'UserName', '2021-02-16 10:10:25-05');

INSERT INTO client(id, document_number, id_document_type, key_mdm, enrollment_origin, id_state, creation_user, created_date, modified_date) VALUES
(1, 1061772353, 0, 'KEY', 'ALM', 0, 'Kevin', '2021-02-16 10:10:25-05', '2021-02-16 10:10:25-05');

INSERT INTO contact(id_contact_medium, document_number, id_document_type, segment, value, id_state, created_date, modified_date, previous) VALUES
(1, 1061772353, 0, 'ALM', 'dmellizo@bancolombia.com.co', 0, '2021-02-16 10:10:25-05', '2021-02-16 10:10:25-05', false),
(0, 1061772353, 0, 'ALM', '1061772353', 0, '2021-02-16 10:10:25-05', '2021-02-16 10:10:25-05', false);

INSERT INTO consumer(id, description, segment) VALUES
('ALM', 'Consumer',  '123aaa'),
('VLP', 'Consumer' ,'456bbb');

INSERT INTO alert_transaction(id_alert, id_consumer, id_transaction, creation_user, created_date) VALUES
('HGD', 'VLP', '0520', 'user', '2021-02-16 10:10:25-05'),
('HGD', 'ALM', '0256', 'user', '2021-02-16 10:10:25-05');

INSERT INTO alert_client(id_alert, document_number, id_document_type, number_operations, amount_enable, accumulated_operations, accumulated_amount, association_origin, creation_user, created_date, modified_date, transaction_date) VALUES
('HGD', 1061772353, 0, 5, 2, 1, 4, 'tst', 'tst1', '2023-05-20 04:00:00-00', '2023-05-20 04:00:00-00', '2023-05-20 04:00:00-00'),
('UPD', 1061772353, 0, 1, 3, 4, 5, 'tsa', 'tst2', '2023-05-20 04:00:00-00', '2023-05-20 04:00:00-00', '2023-05-20 04:00:00-00');

INSERT INTO campaign(id_campaign, id_consumer, provider, id_remitter, default_template, description, source_path, attachment, attachment_path, state, creation_user, created_date, modified_user, modified_date, data_enrichment, priority) VALUES
('1', 'ALM', '{"idProvider": "HJK", "channelType": "SMS"}', 0,  'template_1', null, 'sourcePath', true, 'attachmentPath', '1', 'lugomez', '2022-03-09 11:16:37.915029', null, null, true, 1),
('2', 'VLP', '{"idProvider": "FGH", "channelType": "MAIL"}', 1,  'template_2', null, 'sourcePath', false, null, '1', 'lugomez', '2022-10-07 08:30:37.915029', null, null, false, 2);

INSERT INTO schedule(id, id_campaign, id_consumer, schedule_type, start_date, start_time, end_date, end_time, creation_user, created_date, modified_user, modified_date) VALUES
(1, '1', 'ALM', 'DAILY', '2022-03-05', '23:00:00', '2022-04-05', '23:00:00', 'lugomez', '2022-03-10 15:16:05.73199', null, null),
(2, '1', 'ALM', 'ON_DEMAND', '2022-03-10', '05:45:00', '2022-04-10', '05:45:00', 'lugomez', '2022-03-10 15:16:05.73199', null, null),
(3, '2', 'VLP', 'MONTHLY', '2022-04-15', '11:30:00', '2022-05-15',  '11:30:00', 'lugomez', '2022-04-15 09:18:02.037538', null, null);
