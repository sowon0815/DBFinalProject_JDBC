create table Singer( #가수의 정보
singer varchar(20) not null, #가수 이름
debut int not null, #데뷔 년도
latestAlbum varchar(20), #가장 최근에 발매한 앨범
primary key (singer));

create table Album( #가수가 발매한 앨범의 정보
album varchar(20) not null, #앨범 이름
singer varchar(20) not null, #앨범 발매한 가수 이름
releaseDate date not null, #앨범 발매일
primary key (album, singer),
foreign key (singer) references Singer (singer));

create table Song( #노래의 정보
title varchar(20) not null, #노래의 제목
album varchar(20) not null, #노래가 들어가있는 앨범
singer varchar(20) not null, #노래를 부른 가수
genre varchar(20) not null, #해당 노래의 장르
highestRanking int, #해당 노래의 가장 상위 랭킹-100위권 안에 든 적이 없는 경우 null
primary key (title),
foreign key (album) references Album(album) on update cascade,
foreign key (singer) references Singer(singer));

insert into Singer (singer, debut, latestAlbum) values
('2NE1', 2009, 'CRUSH'),
('miss A', 2010, 'Colors'),
('OH MY GIRL', 2015, 'NONSTOP'),
('Apink', 2011, 'LOOK'),
('ITZY', 2019, 'ITz ME');

insert into Album (album, singer, releaseDate) values
('CRUSH', '2NE1', '2014-02-27'),
('Falling In Love', '2NE1', '2013-07-08'),
('2NE1 2nd Mini Album', '2NE1', '2011-07-28'),
('Colors', 'miss A', '2015-03-30'),
('Love Alone', 'miss A', '2011-05-02'),
('LOOK', 'Apink', '2020-04-13'),
('Always', 'Apink', '2017-04-19'),
('Mr. Chu', 'Apink', '2015-02-18'),
('ITz ME', 'ITZY', '2020-03-09'),
('ITz Different', 'ITZY', '2019-02-12'),
('NONSTOP', 'OH MY GIRL', '2020-04-27');

insert into Song (title, album, singer, genre, highestRanking) values
('Come Back Home', 'CRUSH', '2NE1', 'dance', 2),
('Falling In Love', 'Falling In Love', '2NE1', 'dance', 1),
('hate you', '2NE1 2nd Mini Album', '2NE1', 'dance', 1),
('Ugly', '2NE1 2nd Mini Album', '2NE1', 'dance', 1),
('Love Song', 'Colors', 'miss A', 'dance', 66),
('I Caught Ya', 'Colors', 'miss A', 'R&B', 89),
('Stuck', 'Colors', 'miss A', 'R&B', null),
('Love Alone', 'Love Alone', 'miss A', 'dance', 70),
('Overwrite', 'LOOK', 'Apink', 'dance', 1),
('Be Myself', 'LOOK', 'Apink', 'dance', null),
('Always', 'Always', 'Apink', 'ballad', 30),
('Mr. Chu', 'Mr. Chu', 'Apink', 'dance', 1),
('Hush', 'Mr. Chu', 'Apink', 'dance', null),
('WANNABE', 'ITz ME', 'ITZY', 'dance', 1),
('24HRS', 'ITz ME', 'ITZY', 'ballad', 99),
('WANT IT?', 'ITz Different', 'ITZY', 'dance', 52),
('Dolphin', 'NONSTOP', 'OH MY GIRL', 'ballad', 76);

#앨범의 발매일을 가리키는 인덱스 생성
create index releaseDate_index on Album(releaseDate);

#노래의 제목을 통해, 그 노래를 부른 가수의 정보(이름, 데뷔년도, 최신앨범)을 불러옴
create view singer_song as
select Song.title, Song.singer, Album.releaseDate, Singer.debut
from Song, Album, Singer
where Song.album=Album.album and
Song.singer=Album.singer and
Album.singer=Singer.singer;
