create table if not exists person (
  id SERIAL PRIMARY KEY,
  external_id UUID UNIQUE DEFAULT gen_random_uuid() NOT NULL,
  first_name varchar(50) not null,
  last_name varchar(50) not null
);