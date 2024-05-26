insert into roles (id, role) values (1, 'ROLE_USER'), (2, 'ROLE_ADMIN)');
insert into users (id, age, city, email, hashed_password, name, user_image, role_id, is_banned)
values (1, 20,'Kazan','1@gmail.com','$2a$10$tFdn0BLiWpLtk4i0.x9WAeDwbvhgueLOe6PbkoTH9hmU.M06cWkua','Niyaz','c8fddd96-96cb-4594-89bc-26ef9b23fe42.png',2,false);