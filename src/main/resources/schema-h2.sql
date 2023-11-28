-- ############################################
-- clear

DROP TABLE IF EXISTS followed_channel CASCADE;

DROP TABLE IF EXISTS subscribed_channel CASCADE;

DROP TABLE IF EXISTS users CASCADE;

-- ############################################
-- create table

create table followed_channel (
  followed_channel_id bigint not null auto_increment,
  user_id bigint,
  channel_id bigint not null,
  follow_status tinyint default 'UNFOLLOW',
  followed_at datetime(6),
  unfollowed_at datetime(6),
  followingChannels_KEY bigint,

  create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP not null,
  update_at datetime(6),

  primary key (followed_channel_id)
);

create table subscribed_channel (
  subscribed_channel_id bigint not null auto_increment,
  user_id bigint,
  channel_id bigint not null,
  expired_at datetime(6) not null,
  subscribed_at datetime(6) not null,
  update_subscribed_at datetime(6),
  subscribedChannels_KEY bigint,

  create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP not null,
  update_at datetime(6),

  primary key (subscribed_channel_id)
);

create table users (
  user_id bigint not null auto_increment,
  email varchar(255),
  username varchar(255) not null,
  password varchar(255) not null,

  create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP not null,
  update_at datetime(6),

  primary key (user_id)
);

-- ############################################
-- set FK

alter table followed_channel 
  add constraint followed_channel_users_users_id
  foreign key (user_id) 
  references users (user_id);

alter table subscribed_channel 
  add constraint subscribed_channel_users_users_id
  foreign key (user_id) 
  references users (user_id);

-- ############################################
-- set unique

alter table users 
  add constraint user_email_unique unique (email);

alter table users 
  add constraint user_username_unique unique (username);

