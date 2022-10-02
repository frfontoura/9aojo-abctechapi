create table orders(
    id_orders bigint auto_increment primary key,
    cod_order varchar(36) not null DEFAULT (UUID()),
    id_users bigint not null,
    ind_status varchar(8) not null,
    dat_creation DATETIME DEFAULT CURRENT_TIMESTAMP,
    dat_update DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    constraint fk_orders_users foreign key (id_users) references users (id_users)
);

create table orders_locations(
    id_orders_locations bigint auto_increment primary key,
    cod_order_location varchar(36) not null DEFAULT (UUID()),
    id_orders bigint not null,
    dat_order datetime(6) not null,
    num_latitude double not null,
    num_longitude double not null,
    ind_location_type varchar(6) not null,
    constraint fk_orders_location_orders foreign key (id_orders) references orders (id_orders)
);

create table orders_assists(
    id_orders bigint not null,
    id_assists bigint not null,
    constraint fk_orders_assists_order_id foreign key (id_orders) references orders(id_orders),
    constraint fk_orders_assists_assist_id foreign key (id_assists) references assists(id_assists)
);