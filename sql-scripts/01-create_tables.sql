CREATE DATABASE mydatabase;
DROP TABLE mydatabase.estado;

CREATE TABLE mydatabase.estado (
	id serial4 NOT NULL,
	nome varchar(255) NOT NULL,
	initial varchar(255) NOT NULL,
	CONSTRAINT estado_pkey PRIMARY KEY (id)
);

DROP TABLE mydatabase.cidade;
CREATE TABLE mydatabase.cidade (
	id serial4 NOT NULL,
	nome varchar(255) NOT NULL,
	estado int4 NOT NULL,
	CONSTRAINT cidade_pkey PRIMARY KEY (id)
);


-- mydatabase.cidade foreign keys

ALTER TABLE mydatabase.cidade ADD CONSTRAINT fkedwmmd3jtkssgrwyugrindb7j FOREIGN KEY (estado) REFERENCES mydatabase.estado(id);

-- DROP TABLE mydatabase.pessoa;

CREATE TABLE mydatabase.pessoa (
	id serial4 NOT NULL,
	cpf varchar(255) NULL,
	nascimento date NULL,
	nome varchar(255) NULL,
	sexo varchar(255) NULL,
	CONSTRAINT pessoa_pkey PRIMARY KEY (id)
);

-- DROP TABLE mydatabase.endereco;

CREATE TABLE mydatabase.endereco (
	id serial4 NOT NULL,
	cep varchar(255) NULL,
	cidade varchar(255) NULL,
	estado varchar(255) NULL,
	logradouro varchar(255) NULL,
	numero int4 NULL,
	pessoa int4 NULL,
	CONSTRAINT endereco_pkey PRIMARY KEY (id)
);
ALTER TABLE mydatabase.endereco ADD CONSTRAINT fkmca6kg0qev694alaob8l7hc1g FOREIGN KEY (pessoa) REFERENCES pessoa(id);