drop table if exists activity;
drop table if exists athlete;

create table activity (
      activity_id BIGINT NOT NULL AUTO_INCREMENT UNIQUE PRIMARY KEY,
      strava_activity_id BIGINT not null UNIQUE,
      athlete_id bigint ,
      name  VARCHAR(255) NOT NULL,
      distance INT,
      moving_time INT,
      elapsed_time INT,
      total_elevation_gain INT,
      sport_type VARCHAR(50),
      start_date DATETIME,
      timezone VARCHAR(100),
      average_speed FLOAT(8,1),
      max_speed INT,
      average_cadence FLOAT(8,1),
      average_watts FLOAT(8,1),
      weighted_average_watts INT,
      kilojoules FLOAT(8,1),
      average_heartrate FLOAT(8,1),
      max_heartrate INT,
      max_watts INT,
      suffer_score INT
);

create table athlete (
    athlete_id BIGINT NOT NULL AUTO_INCREMENT UNIQUE PRIMARY KEY,
    strava_athlete_id bigint not null unique,
    firstname varchar(50) not null,
    lastname varchar(50) not null,
    city varchar(50) not null,
    state varchar(50) not null,
    country varchar(50) not null,
    sex varchar(1) not null
);
