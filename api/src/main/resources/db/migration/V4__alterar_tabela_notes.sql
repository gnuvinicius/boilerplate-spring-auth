DO $$
BEGIN
  IF EXISTS(SELECT *
    FROM information_schema.columns
    WHERE table_name='tb_notes' and column_name='descricao')
  THEN
      ALTER TABLE "public"."tb_notes" RENAME COLUMN "descricao" TO "conteudo";
  END IF;
END $$;