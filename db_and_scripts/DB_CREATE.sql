PRAGMA foreign_keys = ON;

create table learn_types (id text primary key, 
							name text);

create table specializations (id text primary key, 
								name text);

create table organizations (id text primary key, 
								name text);

create table protocols (id text primary key, 
						protocol_number text, 
						protocol_date date, 
						protocol_owner text, 
						learn_type text, 
						specialization text, 
						organization text, 
						FOREIGN KEY(learn_type) REFERENCES learn_types(id),
						FOREIGN KEY(specialization) REFERENCES specializations(id),
						FOREIGN KEY(organization) REFERENCES organizations(id)
						);
						
create table certificates (id text primary key,
							certificate_number text,
							certificate_date date,
							fullname text,
							birth_date date,
							protocol_id text,
							FOREIGN KEY(protocol_id) REFERENCES protocols(id));