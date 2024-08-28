CREATE SEQUENCE tb_tenants_seq AS INT INCREMENT 1 START 1;

CREATE TABLE public.tb_tenants (
    id integer NOT NULL DEFAULT nextval('tb_tenants_seq'),
    atualizado_em timestamp(6) NULL,
    cnpj varchar(255) NULL,
    criado_em timestamp(6) NULL,
    email varchar(255) NULL,
    endereco varchar(255) NULL,
    nome varchar(255) NULL,
    status int2 NULL,
    CONSTRAINT tenant_pkey PRIMARY KEY (id),
    CONSTRAINT tenant_status_check CHECK (
        (
            (status >= 0)
            AND (status <= 1)
        )
    ),
    CONSTRAINT unique_tenant_cnpj UNIQUE (cnpj)
);

CREATE SEQUENCE tb_usuarios_seq AS INT INCREMENT 1 START 1;

CREATE TABLE public.tb_usuarios (
    id integer NOT NULL DEFAULT nextval('tb_usuarios_seq'),
    atualizado_em timestamp(6) NULL,
    criado_em timestamp(6) NULL,
    status int2 NULL,
    email varchar(255) NOT NULL,
    nome varchar(255) NOT NULL,
    "password" varchar(255) NOT NULL,
    primeiro_acesso bool NULL,
    token_refresh_password varchar(255) NULL,
    token_refresh_password_valid bool NOT NULL,
    ultimo_acesso timestamp(6) NULL,
    tenant_id integer NOT NULL,
    CONSTRAINT unique_usuario_email UNIQUE (email),
    CONSTRAINT usuario_pkey PRIMARY KEY (id),
    CONSTRAINT usuario_status_check CHECK (
        (
            (status >= 0)
            AND (status <= 1)
        )
    )
);

ALTER TABLE
    public.tb_usuarios
ADD
    CONSTRAINT fk_usuario_tenant FOREIGN KEY (tenant_id) REFERENCES public.tb_tenants(id);

CREATE TABLE public.tb_roles (
    role_id integer NOT NULL,
    role_name varchar(255) NOT NULL,
    CONSTRAINT role_pkey PRIMARY KEY (role_id),
    CONSTRAINT unique_role_name UNIQUE (role_name)
);

CREATE TABLE public.tb_usuario_role (
    usuario_id integer NOT NULL,
    role_id integer NOT NULL,
    CONSTRAINT usuario_role_pkey PRIMARY KEY (usuario_id, role_id)
);

ALTER TABLE
    public.tb_usuario_role
ADD
    CONSTRAINT fka68196081fvovjhkek5m97n3y
    FOREIGN KEY (role_id) REFERENCES public.tb_roles(role_id);

ALTER TABLE
    public.tb_usuario_role
ADD
    CONSTRAINT fk_usuario_role
    FOREIGN KEY (usuario_id) REFERENCES public.tb_usuarios(id);

INSERT INTO
    tb_roles (role_id, role_name)
VALUES
    (
        1,
        'ROLE_ROOT'
    );

INSERT INTO
    tb_roles (role_id, role_name)
VALUES
    (
        2,
        'ROLE_ADMIN'
    );

INSERT INTO
    tb_roles (role_id, role_name)
VALUES
    (
        3,
        'ROLE_USER'
    );