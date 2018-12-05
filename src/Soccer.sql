/*
!!!IMPORTANT!!!:
Edit configuration file
sudo vim /etc/mysql/my.cnf

The header [mysqld] is already given, just locate it in the file
and add local-infile underneath it.

[mysqld]
local-infile

Start mysql with the flag --local-infile

mysql --local-infile=1
*/

DROP database IF EXISTS soccer;
CREATE database soccer;
USE soccer;

CREATE TABLE country (
    id INT NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

LOAD DATA LOCAL INFILE '/home/codio/workspace/Soccer/data/country.csv'
INTO TABLE country
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES;

/*SELECT * FROM country;*/


CREATE TABLE league (
    id INT NOT NULL UNIQUE,
    country_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (country_id) REFERENCES country(id)
);

LOAD DATA LOCAL INFILE '/home/codio/workspace/Soccer/data/league.csv'
INTO TABLE league
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES;

/*SELECT * FROM league;*/


CREATE TABLE team (
    id INT NOT NULL UNIQUE, /* team_api_id */
    team_long_name VARCHAR(255) NOT NULL,
    team_short_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

LOAD DATA LOCAL INFILE '/home/codio/workspace/Soccer/data/team.csv'
INTO TABLE team
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES;

/*SELECT * FROM team;*/


CREATE TABLE soccer_match (
    id INT NOT NULL AUTO_INCREMENT,
    country_id INT NOT NULL,
    league_id INT NOT NULL,
    home_team_id INT NOT NULL,
    away_team_id INT NOT NULL,
    match_date DATETIME NOT NULL,
    home_team_goal INT NOT NULL,
    away_team_goal INT NOT NULL,
    season VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (country_id) REFERENCES country(id),
    FOREIGN KEY (league_id) REFERENCES league(id),
    FOREIGN KEY (home_team_id) REFERENCES team(id),
    FOREIGN KEY (away_team_id) REFERENCES team(id)
);

SET FOREIGN_KEY_CHECKS=0;

LOAD DATA LOCAL INFILE '/home/codio/workspace/Soccer/data/match.csv'
INTO TABLE soccer_match
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES;

/*SELECT * FROM soccer_match;*/


CREATE TABLE player (
    id INT NOT NULL,  /* player_api_id */
    player_name VARCHAR(255) NOT NULL,
    birthday DATETIME NOT NULL,
    height FLOAT NOT NULL,
    weight INT NOT NULL,
    PRIMARY KEY (id)
);

LOAD DATA LOCAL INFILE '/home/codio/workspace/Soccer/data/player.csv'
INTO TABLE player
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES;

/*SELECT * FROM player;*/


CREATE TABLE player_attributes (
    id INT NOT NULL UNIQUE, /* player_api_id */
    `date` DATETIME,
    overall_rating INT NOT NULL,
    potential INT NOT NULL,
    preferred_foot VARCHAR(255) NOT NULL,
    attacking_work_rate VARCHAR(255) NOT NULL,
    defensive_work_rate VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES player(id)
);

LOAD DATA LOCAL INFILE '/home/codio/workspace/Soccer/data/player_attributes.csv'
INTO TABLE player_attributes
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES;

/*SELECT * FROM player_attributes;*/
