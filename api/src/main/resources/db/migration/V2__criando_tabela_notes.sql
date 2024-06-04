CREATE TABLE public.tb_book (
    id uuid NOT NULL,
    atualizado_em timestamp(6) NULL,
    criado_em timestamp(6) NULL,
    titulo varchar(255) NULL,
    descricao varchar(255) NULL,
    status int2 NULL,
    tenant_id uuid NOT NULL,
    CONSTRAINT book_pkey PRIMARY KEY (id),
    CONSTRAINT book_status_check CHECK (
        (
            (status >= 0)
            AND (status <= 1)
        )
    )
);

ALTER TABLE
    public.tb_book
ADD
    CONSTRAINT fk_book_tenant FOREIGN KEY (tenant_id) REFERENCES public.tb_tenant(id);

CREATE TABLE public.tb_notes (
    id uuid NOT NULL,
    atualizado_em timestamp(6) NULL,
    criado_em timestamp(6) NULL,
    titulo varchar(255) NULL,
    descricao varchar(255) NULL,
    status int2 NULL,
    tenant_id uuid NOT NULL,
    book_id uuid NOT NULL,
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
    CONSTRAINT fk_note_tenant FOREIGN KEY (tenant_id) REFERENCES public.tb_tenant(id);
