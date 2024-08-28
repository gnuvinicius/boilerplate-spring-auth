CREATE SEQUENCE tb_books_seq AS INT INCREMENT 1 START 1;

CREATE TABLE public.tb_books (
    id integer NOT NULL DEFAULT nextval('tb_books_seq'),
    atualizado_em timestamp(6) NULL,
    criado_em timestamp(6) NULL,
    titulo varchar(255) NULL,
    descricao varchar(255) NULL,
    status int2 NULL,
    tenant_id integer NOT NULL,
    CONSTRAINT book_pkey PRIMARY KEY (id),
    CONSTRAINT book_status_check CHECK (
        (
            (status >= 0)
            AND (status <= 1)
        )
    )
);

ALTER TABLE
    public.tb_books
ADD
    CONSTRAINT fk_book_tenant FOREIGN KEY (tenant_id) REFERENCES public.tb_tenants(id);


CREATE SEQUENCE tb_notes_seq AS INT INCREMENT 1 START 1;

CREATE TABLE public.tb_notes (
    id integer NOT NULL DEFAULT nextval('tb_notes_seq'),
    atualizado_em timestamp(6) NULL,
    criado_em timestamp(6) NULL,
    titulo varchar(255) NULL,
    descricao varchar(255) NULL,
    status int2 NULL,
    tenant_id integer NOT NULL,
    book_id integer NOT NULL,
    CONSTRAINT note_pkey PRIMARY KEY (id),
    CONSTRAINT note_status_check CHECK (
        (
            (status >= 0)
            AND (status <= 1)
        )
    )
);

ALTER TABLE
    public.tb_notes
ADD
    CONSTRAINT fk_note_tenant FOREIGN KEY (tenant_id) REFERENCES public.tb_tenants(id);
