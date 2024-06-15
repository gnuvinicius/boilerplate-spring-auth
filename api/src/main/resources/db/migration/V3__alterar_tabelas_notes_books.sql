alter table tb_notes add column usuario_id integer default null;

ALTER TABLE
    public.tb_notes
ADD
    CONSTRAINT fk_note_usuario FOREIGN KEY (usuario_id) REFERENCES public.tb_usuarios(id);



alter table tb_books add column usuario_id integer default null;

ALTER TABLE
    public.tb_books
ADD
    CONSTRAINT fk_book_usuario FOREIGN KEY (usuario_id) REFERENCES public.tb_usuarios(id);