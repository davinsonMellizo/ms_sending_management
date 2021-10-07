--
-- log table creation
--
CREATE TABLE IF NOT EXISTS log (
	id serial primary KEY,
	document_type int2 NOT NULL,
	document_number int8 NOT NULL,
	id_alert varchar(3) NOT NULL,
	alert_type varchar(15) NOT NULL,
	alert_destination varchar(60) NOT NULL,
	message_type varchar(4) NOT NULL,
	message_sent varchar(500) NOT NULL,
	account_type varchar(2) NOT NULL,
	account_number int8 NOT NULL,
	operation_channel varchar(3) NOT NULL,
	operation_code varchar(4) NOT NULL,
	operation_description varchar(30) NOT NULL,
	operation_number  int2 NOT NULL,
	enabled_amount  int8 NOT NULL,
	send_response_code  int2 NOT NULL,
	send_response_description varchar(50) NOT NULL,
	priority int2 NOT NULL,
	template varchar(40) NOT NULL,
	alert_indicator int2 NOT NULL,
	indicator_description varchar(15) NOT NULL,
	application_code varchar(15) NOT NULL,
	date_creation timestamp NOT NULL,
	user_field1 varchar(30),
	user_field2 varchar(30),
	user_field3 int8,
	user_field4 int8,
	user_field5 varchar(100),
	user_field6 varchar(100)
);