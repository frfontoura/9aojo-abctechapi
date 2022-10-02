create table roles (
    id_roles bigint primary key,
    nam_role varchar(15) not null
);

insert into roles(id_roles, nam_role) values (1, 'ROLE_USER');
insert into roles(id_roles, nam_role) values (2, 'ROLE_ADMIN');

CREATE TABLE users (
    id_users BIGINT auto_increment primary key,
    nam_username varchar(50) NOT NULL,
    des_password varchar(100) NOT NULL,
    des_email varchar(100) NOT NULL,
    cod_user varchar(36) not null DEFAULT (UUID()),
    dat_creation DATETIME DEFAULT CURRENT_TIMESTAMP,
    dat_update DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

create table users_roles (
    id_users bigint not null,
    id_roles bigint not null,
    constraint fk_users_roles_users foreign key (id_users) references users (id_users),
    constraint fk_users_roles_roles foreign key (id_roles) references roles (id_roles)
);