# playlist-api

To Run The Application With Docker First Pull A Postgres Image
docker pull postgres

Then Pull The Latest Image Of The Playlist-Api
docker pull yaxet/playlist-api:latest

make sure the Playlist-Api has the Playlist-Service tag
docker tag yaxet/playlist-api playlist-service

Then Run run docker compose
docker-compose up
