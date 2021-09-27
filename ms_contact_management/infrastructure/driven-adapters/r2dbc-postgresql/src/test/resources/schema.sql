CREATE SCHEMA IF NOT EXISTS schalertd;

--
-- base tables creation
--
CREATE TABLE IF NOT EXISTS state (
	id int2 NOT NULL,
	name varchar(10) UNIQUE NOT NULL,
	CONSTRAINT state_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS document_type (
	id int2 NOT NULL,
	code varchar(3) NOT NULL,
	name varchar(100) NOT NULL,
	CONSTRAINT document_type_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS consumer (
	id varchar(3) NOT NULL,
	segment varchar(10),
	CONSTRAINT consumer_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS contact_medium (
	id int2 NOT NULL,
	code varchar(10) UNIQUE NOT NULL,
	CONSTRAINT contact_medium_pkey PRIMARY KEY (id)
);

--
--  Gestion Envìo tables creation
--
CREATE TABLE IF NOT EXISTS provider (
	id varchar(3) NOT NULL,
	name varchar(20) NOT NULL,
	type_service varchar(1) NOT NULL,
	creation_user varchar(20) NULL,
	created_date timestamp NOT NULL,
	CONSTRAINT provider_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS alert_template (
	id int2 NOT NULL,
	field varchar(10) NOT NULL,
	initial_position int2 NOT NULL,
	final_position int2 NOT NULL,
	creation_user varchar(20) NULL,
	created_date timestamp NOT NULL,
	CONSTRAINT alert_template_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS remitter (
	id int2 NOT NULL,
	mail varchar(70) NOT NULL,
	state varchar(10) NOT NULL,
	creation_user varchar(20) NULL,
	created_date timestamp NOT NULL,
	CONSTRAINT remitter_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS service (
	id int2 NOT NULL,
	name varchar(50) NOT NULL,
	state varchar(10) NOT NULL,
	creation_user varchar(20) NULL,
	created_date timestamp NOT NULL,
	CONSTRAINT service_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS alert (
	id varchar(3) NOT NULL,
	id_template int2 NOT NULL,
	id_provider_mail varchar(3) NOT NULL,
	id_provider_sms varchar(3) NOT NULL,
	id_remitter int2 NOT NULL,
	id_service int2 NOT NULL,
	description varchar(50) NOT NULL,
	nature varchar(2) NOT NULL,
	obligatory boolean NOT NULL,
	message varchar(500) NOT NULL,
	priority int2 NOT NULL,
	subject_mail varchar(50) NOT NULL,
	visible_channel boolean NOT NULL,
	path_attached_mail varchar(100) NOT NULL,
	attention_line varchar(15) NOT NULL,
	id_state int2 NOT NULL,
	creation_user varchar(20) NULL,
	created_date timestamp NOT NULL,
	CONSTRAINT alert_pkey PRIMARY KEY (id),
	CONSTRAINT alert_provider_mail_fkey FOREIGN KEY (id_provider_mail) REFERENCES provider(id),
	CONSTRAINT alert_provider_sms_fkey FOREIGN KEY (id_provider_sms) REFERENCES provider(id),
	CONSTRAINT alert_template_fkey FOREIGN KEY (id_template) REFERENCES alert_template(id),
	CONSTRAINT alert_remitter_fkey FOREIGN KEY (id_remitter) REFERENCES remitter(id),
	CONSTRAINT alert_service_fkey FOREIGN KEY (id_service) REFERENCES service(id),
	CONSTRAINT alert_state_fkey FOREIGN KEY (id_state) REFERENCES state(id)
);

CREATE TABLE IF NOT EXISTS alert_transaction (
	id_alert varchar(3) NOT NULL,
	id_consumer varchar(3) NOT NULL,
	id_transaction varchar(4) NOT NULL,
	creation_user varchar(20) NULL,
	created_date timestamp NOT NULL,
	CONSTRAINT alert_transaction_pkey PRIMARY KEY (id_alert, id_consumer, id_transaction),
	CONSTRAINT alert_transaction_consumer_fkey FOREIGN KEY (id_consumer) REFERENCES consumer(id),
	CONSTRAINT alert_transaction_alert_fkey FOREIGN KEY (id_alert) REFERENCES alert(id) on delete cascade
);

CREATE TABLE IF NOT EXISTS alert_client (
	id_alert varchar(3) NOT NULL,
	document_number int8 NOT NULL,
	id_document_type int2 NOT NULL,
	number_operations int2 NOT NULL,
	amount_enable int8 NOT NULL,
	accumulated_operations int2 NULL,
	accumulated_amount int8 NULL,
	association_origin varchar(3) NOT NULL,
	creation_user varchar(20) NULL,
	created_date timestamp NOT NULL,
	modified_date timestamp NULL,
	transaction_date timestamp NULL,
	CONSTRAINT alert_client_pkey PRIMARY KEY (id_alert, document_number, id_document_type),
	CONSTRAINT alert_client_alert_fkey FOREIGN KEY (id_alert) REFERENCES alert(id) on delete cascade
);

CREATE TABLE IF NOT EXISTS log_send (
	id_alert varchar(3) NOT NULL,
	created_date timestamp NOT NULL,
	CONSTRAINT log_send_pkey PRIMARY KEY (id_alert)
);

--
--  Gestion Contacto tables creation
--
CREATE TABLE IF NOT EXISTS client (
	id serial NOT NULL,
	document_number int8 NOT NULL,
	id_document_type int2 NOT NULL,
	key_mdm varchar(20) NOT NULL,
	enrollment_origin varchar(3) NULL,
	id_state int2 NOT NULL,
	creation_user varchar(20) NULL,
	created_date timestamp NOT NULL,
	modified_date timestamp NULL,
	CONSTRAINT client_pkey PRIMARY KEY (id),
 	CONSTRAINT client_document_unique UNIQUE (document_number, id_document_type),
 	CONSTRAINT client_state_fkey FOREIGN KEY (id_state) REFERENCES state(id),
 	CONSTRAINT client_document_type_fkey FOREIGN KEY (id_document_type) REFERENCES document_type(id)
);

CREATE TABLE IF NOT EXISTS contact (
	id serial NOT NULL,
	document_number int8 NOT NULL,
	id_document_type int2 NOT NULL,
	segment varchar(10) NOT NULL,
	id_contact_medium int2 NOT NULL,
	value varchar(60) NOT NULL,
	id_state int2 NOT NULL,
	created_date timestamp NOT NULL,
	modified_date timestamp NULL,
	CONSTRAINT contact_pkey PRIMARY KEY (id),
 	CONSTRAINT contact_unique UNIQUE (document_number, id_document_type,segment,id_contact_medium),
 	CONSTRAINT contact_contact_medium_fkey FOREIGN KEY (id_contact_medium) REFERENCES contact_medium(id),
 	CONSTRAINT contact_state_fkey FOREIGN KEY (id_state) REFERENCES state(id),
 	CONSTRAINT contact_client_type_fkey FOREIGN KEY (document_number, id_document_type) REFERENCES client(document_number, id_document_type) on delete cascade on update cascade
);
