/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2023/2/28 11:06:40                           */
/*==============================================================*/

drop database if exists intelligentdoorplate; -- 数据库名不能用中划线-

create database intelligentdoorplate charset utf8;

use intelligentdoorplate;

drop table if exists apply;

drop table if exists message;

drop table if exists qrcode;

drop table if exists record;

drop table if exists task;

drop table if exists user;

/*==============================================================*/
/* Table: apply                                                 */
/*==============================================================*/
create table apply
(
    id             bigint not null auto_increment,
    user_id              bigint,
    processor_id         bigint,
    name                 varchar(255) not null,
    gender               bool not null,
    id_number            varchar(255) not null,
    picture_url          varchar(255),
    phone_number         bigint,
    email                varchar(255) not null,
    address              varchar(255) not null,
    verify_status        int not null,
    apply_time           datetime not null,
    start_time           datetime,
    end_time             datetime,
    res_time             datetime,
    res_location         varchar(255),
    type                 int not null,
    is_deleted           tinyint not null default 0,
    primary key (id)
);

/*==============================================================*/
/* Table: message                                               */
/*==============================================================*/
create table message
(
    id               bigint not null auto_increment,
    user_id              bigint,
    task_id              bigint,
    longitude            real not null,
    latitude             real not null,
    address              varchar(255) not null,
    visit_time           datetime not null,
    picture_url          varchar(255),
    document_url         varchar(255),
    description          varchar(255),
    is_deleted           tinyint not null default 0,
    primary key (id)
);

/*==============================================================*/
/* Table: qrcode                                                */
/*==============================================================*/
create table qrcode
(
    id                bigint not null auto_increment,
    user_id              bigint,
    address              varchar(255),
    longitude            real,
    latitude             real,
    is_bound             bool not null default 0,
    is_rented            bool not null default 0,
    picture_url          varchar(255),
    document_url         varchar(255),
    is_checked           bool not null default 0,
    type                 int not null default 1,
    parent_id            bigint,
    is_deleted           tinyint not null default 0,
    primary key (id)
);

/*==============================================================*/
/* Table: record                                                */
/*==============================================================*/
create table record
(
    id            bigint not null auto_increment,
    qr_id                bigint,
    tenant_id            bigint not null,
    apply_id             bigint not null,
    is_rented            bool not null default 0,
    is_deleted           tinyint not null default 0,
    primary key (id)
);

/*==============================================================*/
/* Table: task                                                  */
/*==============================================================*/
create table task
(
    id              bigint not null auto_increment,
    qr_id                bigint,
    user_id              bigint,
    name                 varchar(255) not null default 'Task',
    type                 int not null,
    is_completed         int not null default 0,
    start_time           datetime,
    end_time             datetime,
    complete_time        datetime,
    count                int not null default 3,
    description          varchar(255),
    is_deleted           tinyint not null default 0,
    primary key (id)
);

/*==============================================================*/
/* Table: user                                                  */
/*==============================================================*/
create table user
(
    id              bigint not null auto_increment,
    name                 varchar(255) not null,
    gender               bool not null,
    id_number            varchar(255) not null,
    phone_number         bigint,
    email                varchar(255) not null,
    password             varchar(255) not null default '123456',
    address              varchar(255),
    longitude            real,
    latitude             real,
    is_residence         bool not null default 0,
    role_id              int not null default 4,
    is_deleted           tinyint not null default 0,
    primary key (id)
);

alter table apply add constraint FK_Apply_User foreign key (user_id)
    references user (id) on delete restrict on update restrict;

alter table message add constraint FK_Message_Task foreign key (task_id)
    references task (id) on delete restrict on update restrict;

alter table message add constraint FK_Message_User foreign key (user_id)
    references user (id) on delete restrict on update restrict;

alter table qrcode add constraint FK_Qrcode_User foreign key (user_id)
    references user (id) on delete restrict on update restrict;

alter table record add constraint FK_Record_Qrcode foreign key (qr_id)
    references qrcode (id) on delete restrict on update restrict;

alter table record add constraint FK_Task_Apply foreign key (apply_id)
    references apply (id) on delete restrict on update restrict;

alter table task add constraint FK_Task_Qrcode foreign key (qr_id)
    references qrcode (id) on delete restrict on update restrict;

