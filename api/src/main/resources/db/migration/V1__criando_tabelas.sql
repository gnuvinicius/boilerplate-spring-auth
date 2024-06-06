CREATE TABLE public.tb_tenants (
    id uuid NOT NULL,
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

CREATE TABLE public.tb_usuarios (
    id uuid NOT NULL,
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
    tenant_id uuid NOT NULL,
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
    role_id uuid NOT NULL,
    role_name varchar(255) NOT NULL,
    CONSTRAINT role_pkey PRIMARY KEY (role_id),
    CONSTRAINT unique_role_name UNIQUE (role_name)
);

CREATE TABLE public.tb_usuario_role (
    usuario_id uuid NOT NULL,
    role_id uuid NOT NULL,
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
        '0d39a2f4-07be-4d72-ac7a-9c6f3b6846ce',
        'ROLE_ROOT'
    );

INSERT INTO
    tb_roles (role_id, role_name)
VALUES
    (
        '64e4b21e-166c-4068-b1ea-4fc1f9d3c2c7',
        'ROLE_ADMIN'
    );

INSERT INTO
    tb_roles (role_id, role_name)
VALUES
    (
        '7de7ec5c-74ad-4dcd-a4ba-7b72b3d8c83f',
        'ROLE_USER'
    );