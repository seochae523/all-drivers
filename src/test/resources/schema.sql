-- we don't know how to generate root <with-no-name> (class Root) :(
create schema alldriver;


create table user
(
    id            bigint auto_increment
        primary key,
    created_at    timestamp        null,
    deleted       bit default false not null,
    license       varchar(50)      null,
    name          varchar(20)      not null,
    nickname      varchar(11)      not null,
    password      varchar(255)     not null,
    phone_number  varchar(11)      not null,
    refresh_token text             null,
    role          varchar(50)      not null,
    user_id       varchar(30)      null
);

create table board
(
    id               bigint auto_increment primary key,
    content          text             not null,
    created_at       datetime(6)      null,
    deleted          bit default false null,
    end_at           date             not null,
    start_at         date             not null,
    pay_type         varchar(5)       not null,
    payment          int              not null,
    recruit_type     varchar(255)     not null,
    title            varchar(50)      not null,
    user_id          bigint           null,
    company_location varchar(100)     not null,
    constraint FKfyf1fchnby6hndhlfaidier1r
        foreign key (user_id) references alldriver.user (id)
);

create table board_image
(
    id       bigint auto_increment
        primary key,
    url      text   null,
    user_id  bigint null,
    board_id bigint null,
    constraint FKil875c0myaxwwf0hty0u1ej2d
        foreign key (board_id) references alldriver.board (id),
    constraint FKlxnnh0ir05khn8iu9tgwh1yyk
        foreign key (user_id) references alldriver.user (id)
);

create table car
(
    id       bigint auto_increment
        primary key,
    category varchar(10) not null
);

create table car_board
(
    id       bigint auto_increment
        primary key,
    car_id   bigint null,
    board_id bigint null,
    constraint FKdhkyqqg9s88rcqbeqywqy1dpg
        foreign key (board_id) references alldriver.board (id),
    constraint FKg9i7pc08r4ih26iwsy7s303ck
        foreign key (car_id) references alldriver.car (id)
);

create table car_image
(
    id          bigint auto_increment
        primary key,
    url         text   not null,
    user_car_id bigint not null,
    constraint car_image_user_car_id_fk
        foreign key (user_car_id) references alldriver.user_car (id)
);

create table job
(
    id       bigint auto_increment
        primary key,
    category varchar(10) not null
);

create table job_board
(
    id       bigint auto_increment
        primary key,
    job_id   bigint null,
    board_id bigint null,
    constraint FKbxjglh0gj158dphbe7c7mlf8e
        foreign key (job_id) references alldriver.job (id),
    constraint FKgk0luud2q4ri0u0iarlgfdwjd
        foreign key (board_id) references alldriver.board (id)
);

create table license
(
    id             bigint auto_increment
        primary key,
    license_number varchar(50) not null,
    user_id        bigint      not null,
    url            text        not null,
    constraint license_user_fk
        foreign key (user_id) references alldriver.user (id)
);

create table location_board
(
    id          bigint auto_increment
        primary key,
    location_id bigint null,
    board_id    bigint null,
    constraint FKaosj6eroc7fdlufaulxr39k7d
        foreign key (board_id) references alldriver.board (id),
    constraint FKs6fbe7dnbx4vki3r1m85t3qc
        foreign key (location_id) references alldriver.sub_location (id)
);

create table main_location
(
    id       bigint auto_increment
        primary key,
    category varchar(10) not null
);

create table sms_session
(
    id           bigint auto_increment
        primary key,
    auth_code    varchar(6)  null,
    created_at   datetime(6) null,
    phone_number varchar(11) null
);

create table sub_location
(
    id               bigint auto_increment
        primary key,
    category         varchar(10) not null,
    main_location_id bigint      not null,
    constraint sub_location_main_location_id_fk
        foreign key (main_location_id) references alldriver.main_location (id)
);



create table user_car
(
    id         bigint auto_increment
        primary key,
    car_number varchar(255) null,
    category   varchar(255) null,
    weight     varchar(255) null,
    user_id    bigint       null,
    constraint FKgs1lsnqcl7dmnbsvc1m8wuy6h
        foreign key (user_id) references alldriver.user (id)
);

