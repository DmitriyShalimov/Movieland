

create table "user"(
   id serial primary key
  ,firstName text not null
  ,lastName text not null
  ,email text  unique
  ,password text not null
  ,salt text not null
  ,"role" text  default 'U'
);

create table genre(
   id serial primary key
  ,name text
);

create table country(
   id serial primary key
  ,name text
);


create table movie(
   id serial primary key
  ,nameRussian text  not null
  ,nameNative text not null
  ,yearOfRelease int not null
  ,description text not null
  ,rating float(24)
  ,price float(24)
  , picture text not null
);

create table movie_genre(
   id serial primary key
  ,movie int  references movie(id)
  ,genre int  references genre(id)
);

create table movie_country(
   id serial primary key
  ,movie int  references movie(id)
  ,country int  references country(id)
);

create table review(
  id serial primary key
  ,"user" int  references "user"(id)
  ,movie int  references movie(id)
  ,"text" text
);