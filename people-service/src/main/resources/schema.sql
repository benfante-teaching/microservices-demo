create table if not exists person (
  id SERIAL PRIMARY KEY,
  first_name varchar(50) not null,
  last_name varchar(50) not null,
  age smallint
);