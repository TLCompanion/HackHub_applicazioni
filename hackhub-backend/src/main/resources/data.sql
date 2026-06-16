INSERT INTO utenti (id_utente, email, nome_utente, password_hash, recapito_bancario) VALUES
 ('U-0c6afd00-cc2c-4c36-9d9b-3e6f27ead172','giudice@gmail','utente3','$2a$10$lp5h7/kmkwJPqQurZSpsYeo.rIgbsAG/n.3ffJNymtet2tRPoC8RS',NULL)
 ,('U-3d24f098-58bb-49d6-a183-c7a4f3b4942d','aaaaa@aaa','demo','$2a$10$zZJFa3tzCNHxE9hU/SN9kOIrRIgA4uWNbRHbc3b2Itplc1CFk.wW6',NULL)
 ,('U-3d4be420-bf62-4b1a-8847-03a77f436e81','giudice@gmail','utente5','$2a$10$ONZ7n.pQ6TNmp25W.7KyVOn3DO6Sxh2FWjJ0R/qzITk5/eATq4vOK',NULL)
 ,('U-4fa11644-a640-4617-aebe-68e9fe78110f','giudice@gmail','mentore','$2a$10$XtuCwW.X3GWb6u9oC4/WbuZI1rHUwYGsbhbIG1R2s3coBub76iWqi',NULL)
 ,('U-6c6cfd72-6292-4b12-a378-2a88e97f89bc','giudice@gmail','utente1','$2a$10$YOFcKfzuHDjKcSC1eD8JrO2YL1urwgmOzpCS1OEmhF4KR/MjxLWkG',NULL)
 ,('U-755c5844-f0b1-4a33-8d88-6c215f8122f3','giudice@gmail','utente10','$2a$10$yRo1AZzL9tcVHI8.ay2F1e75g4IYG8sxuDyA8LU4qSRQ6QtD8iimS',NULL)
 ,('U-860cb735-9ce4-4015-88a1-e6eafd9e1d9a','giudice@gmail','utente7','$2a$10$iT2HCdTIUPUiLnlSps2enOo8vXlHPxxYUmyhb/E2sSMg9Jo29hzL.',NULL)
 ,('U-b3762115-c515-4ae0-b412-483a8ef97905','giudice@gmail','utente6','$2a$10$syP9f8P2HtwnDHa9g1JzduFwJLwLmHDjna25lcTsddDsDrbXbgIMW',NULL)
 ,('U-bbecc208-2efb-4bad-bf91-4df6ef49f9c6','giudice@gmail','utente8','$2a$10$ybBI84V.uyadtjxqPQLfBOwDxDNo..OJNOJCfjNz6o0FMXPOX9g3S',NULL)
 ,('U-d4d983e9-c681-41cf-ad5b-e241b531ead8','giudice@gmail','utente2','$2a$10$3fsTz912zjBKrEM6yeWTPOtuVd2Nw91T03hpK2aw3V1OVEf6gtAPK',NULL)
 ,('U-dc058611-e9bc-4504-8076-e4c1dabbc079','giudice@gmail','utente4','$2a$10$Q94GkSU4wdkJYBGYdBQrme7IFkGpRNk8qEvHJS8TpmnPlZ/tmY.oC',NULL)
 ,('U-e754d506-9320-47a0-bb49-1e169574370a','giudice@gmail','utente9','$2a$10$eCF3a6rvDsEAK8FsuSzWPejc8aQoiTmNKzDYn125QgobBktau7.Qi',NULL)
 ,('U-f82a30cb-9cf6-427d-966d-9f681dfd5665','giudice@gmail','giudice','$2a$10$Vg/EOqUejUR7AcWCeG/.Eu8aXbKZzUBtt16unYhRe7wDXCJu9IwCG',NULL);
INSERT INTO team (id_team, nome) VALUES ('T-2422fe6c-5660-4b34-bd8c-2e0cc5090e88','Miglior Team'),
('T-4002350a-5225-4681-aeb5-45ff5f62f80b','Poker Face'),('T-81b158cc-e202-42ed-9150-8409549fdbca','Unicam Vibes');
INSERT INTO hackathon (id_hackathon,
    luogo,
    max_iscrizioni,
    nome,
    data_fine,
    data_inizio,
    ora_fine,
    ora_inizio,
    premio,
    regolamento,
    scadenza_iscrizioni,
    stato_enum,
    team_max,
    team_min) VALUES 
    ('H-0a36d87e-0d30-4b68-91d2-4148350b1884','Seoul',18,'Hackathon4','2032-06-01','2032-05-29','23:59:00','00:00:00',3435.00,
    'regole','2032-05-25 23:59:00.000000','ISCRIZIONI_APERTE',5,4),('H-0bcb72d6-fc34-4a22-8ca8-40f465c7381d','Rimini',23,'Hackathon5',
    '2027-11-18','2027-11-15','23:59:00','00:00:00',5000.00,'regole','2027-11-13 23:59:00.000000','ISCRIZIONI_APERTE',5,4),
    ('H-26de69c5-1f9a-43e5-ba19-14fb9a231e06','Roma',17,'Hackathon3','2029-09-25','2029-09-22','23:59:00','00:00:00',3270.00,'regole'
    ,'2029-09-19 23:59:00.000000','ISCRIZIONI_APERTE',5,4),('H-661362ff-a851-4543-9c1c-acbb94886e81','Varsavia',14,'Hackathon6',
    '2027-10-21','2027-10-18','23:59:00','00:00:00',3400.00,'regole','2027-10-16 23:59:00.000000','ISCRIZIONI_APERTE',5,4),
    ('H-9e7f4114-10e6-48fc-a15f-313499b35603','Camerino',21,'Hackathon1','2028-02-05','2028-02-02','23:59:00','00:00:00',4605.00,
    'regole','2028-02-01 23:59:00.000000','ISCRIZIONI_APERTE',5,4),('H-f83845d5-f61a-49e9-b6bf-0627ec460948','Ancona',10,'Hackathon2',
    '2028-06-07','2028-06-05','23:59:00','00:00:00',1000.00,'regole','2028-06-02 23:59:00.000000','ISCRIZIONI_APERTE',5,4);
INSERT INTO membro_team (id_membro_team,
    ruolo,
    team_id_team,
    utente_id_utente) VALUES ('MT-1138848e-2f68-4b47-bf9f-cf3533b109c0','LEADER','T-81b158cc-e202-42ed-9150-8409549fdbca',
    'U-b3762115-c515-4ae0-b412-483a8ef97905'),('MT-2915157e-1602-45ed-af08-37f4c8e2ee13','MEMBRO','T-81b158cc-e202-42ed-9150-8409549fdbca',
    'U-0c6afd00-cc2c-4c36-9d9b-3e6f27ead172'),('MT-2e1a093f-2c85-4368-bf5e-e27b27c67442','MEMBRO','T-2422fe6c-5660-4b34-bd8c-2e0cc5090e88',
    'U-e754d506-9320-47a0-bb49-1e169574370a'),('MT-4a22ef50-4bde-464b-9781-2567e472c48d','MEMBRO','T-2422fe6c-5660-4b34-bd8c-2e0cc5090e88',
    'U-3d4be420-bf62-4b1a-8847-03a77f436e81'),('MT-58dac7d7-768a-4e7e-b00f-479f97c349df','MEMBRO','T-2422fe6c-5660-4b34-bd8c-2e0cc5090e88',
    'U-d4d983e9-c681-41cf-ad5b-e241b531ead8'),('MT-735f0d0a-7695-4fd7-b5d0-8a83ebf579f9','MEMBRO','T-2422fe6c-5660-4b34-bd8c-2e0cc5090e88',
    'U-6c6cfd72-6292-4b12-a378-2a88e97f89bc'),('MT-82f52ca1-6dd5-46d8-9b36-f1316b248b49','MEMBRO','T-81b158cc-e202-42ed-9150-8409549fdbca',
    'U-860cb735-9ce4-4015-88a1-e6eafd9e1d9a'),('MT-97ab810f-a670-46f3-b439-04d414f133ce','LEADER','T-2422fe6c-5660-4b34-bd8c-2e0cc5090e88',
    'U-dc058611-e9bc-4504-8076-e4c1dabbc079'),('MT-c17b373d-7821-46a5-ac8e-05c36c8f425e','MEMBRO','T-4002350a-5225-4681-aeb5-45ff5f62f80b',
    'U-bbecc208-2efb-4bad-bf91-4df6ef49f9c6'),('MT-d3993bd3-780a-443c-a27f-7ce2f2783a26','LEADER','T-4002350a-5225-4681-aeb5-45ff5f62f80b',
    'U-755c5844-f0b1-4a33-8d88-6c215f8122f3');
INSERT INTO staff (id_staff, ruolo, hackathon_id_hackathon, utente_id_utente) VALUES (
    'MS-10b00457-4d6d-46ba-bb97-9d856f4cc726','ORGANIZZATORE','H-26de69c5-1f9a-43e5-ba19-14fb9a231e06',
    'U-3d24f098-58bb-49d6-a183-c7a4f3b4942d'),('MS-362412f9-3aec-4538-ac4d-251eca6db871','ORGANIZZATORE',
    'H-661362ff-a851-4543-9c1c-acbb94886e81','U-3d24f098-58bb-49d6-a183-c7a4f3b4942d'),('MS-3cfdb0e6-0b03-4724-b1c3-7cd755fc5421',
    'ORGANIZZATORE','H-f83845d5-f61a-49e9-b6bf-0627ec460948','U-3d24f098-58bb-49d6-a183-c7a4f3b4942d'),
    ('MS-468f7a1b-76d2-4501-8fea-a3589b2ce768','ORGANIZZATORE','H-0bcb72d6-fc34-4a22-8ca8-40f465c7381d',
    'U-3d24f098-58bb-49d6-a183-c7a4f3b4942d'),('MS-5862aaf9-4d08-4595-ad36-c9f00974f571','ORGANIZZATORE',
    'H-9e7f4114-10e6-48fc-a15f-313499b35603','U-3d24f098-58bb-49d6-a183-c7a4f3b4942d'),('MS-e706a7b3-2611-4b7e-a9b5-140119d932b7',
    'ORGANIZZATORE','H-0a36d87e-0d30-4b68-91d2-4148350b1884','U-3d24f098-58bb-49d6-a183-c7a4f3b4942d');