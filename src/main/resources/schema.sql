create table if not exists MESSAGE (
  id      varchar(255) unique not null,
  message varchar(255)        not null
);