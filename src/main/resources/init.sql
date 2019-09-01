DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS restaurants CASCADE;
DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS ratings CASCADE;
DROP TABLE IF EXISTS menus CASCADE;
DROP TABLE IF EXISTS carts CASCADE;
DROP TABLE IF EXISTS cart_items CASCADE;
DROP TABLE IF EXISTS orders CASCADE;

CREATE TABLE users(
    id serial primary key,
    name text unique not null,
    email text unique not null,
    password varchar(10) not null,
    roles text[] default ARRAY['USER'],
    location text not null,
    address text not null,
    cut numeric default 0,
    premium boolean default false,
    profit numeric default 0
);

CREATE TABLE restaurants(
    id serial primary key,
    name text unique not null,
    location text not null,
    rating numeric default 0,
    max_cut numeric default 100,
    picture text not null
);

CREATE TABLE products(
    id serial primary key,
    name text not null,
    price numeric check (price > 0),
    availability boolean default true,
    picture text not null,
    description text not null,
    category text check (category in('appetizer', 'entree', 'dessert'))
);

CREATE TABLE ratings(
    restaurant_id int references restaurants(id),
    user_id int references users(id) unique,
    rating numeric check (rating > 0),
    primary key (restaurant_id, user_id)
);

CREATE TABLE menus(
    restaurant_id int references restaurants(id),
    product_id int references products(id),
    primary key (product_id, restaurant_id)
);

CREATE TABLE carts(
	id serial primary key,
	user_id int references users(id),
	price numeric default 0,
	checked_out boolean default false
);

CREATE TABLE cart_items(
    id serial primary key,
    cart_id int references carts(id),
    product_id int references products(id),
    quantity numeric not null,
    price numeric not null
);

CREATE TABLE orders(
    id serial primary key,
    user_id int references users(id),
    courier_id int references users(id) check (courier_id != user_id),
    cart_id int references carts(id),
    picked_up boolean default false,
    complete boolean default false
);

create or replace function total_cart_price()
returns trigger as '
begin
update carts set price = (select sum(cart_items.price) from cart_items where cart_items.cart_id = carts.id);
return new;
end;
' language plpgsql;

create trigger total_cart_price
after insert or delete on cart_items for each row
execute procedure total_cart_price();

create or replace function default_role()
returns trigger as '
begin
update users set roles = ARRAY[''USER''] where roles is null;
return new;
end;
' language plpgsql;

create trigger default_role
after insert or update on users for each row
execute procedure default_role();

create or replace function update_rating()
returns trigger as '
begin
update restaurants set rating = (select avg(ratings.rating) from ratings where restaurants.id = ratings.restaurant_id);
return new;
end;
' language plpgsql;

create trigger update_rating
after insert on ratings for each row
execute procedure update_rating();

create or replace function update_profit()
returns trigger as '
begin
update users set profit = (select sum(carts.price / 100 * users.cut) from carts
                           join orders on orders.courier_id = users.id and orders.cart_id = carts.id and orders.complete is true
						   where users.id = orders.courier_id);
return new;
end;
' language plpgsql;

create trigger update_profit
after update on orders for each row
execute procedure update_profit();

INSERT INTO users (name, email, password, location, address) VALUES
	('user1', 'user1@user1.com', 'password1', 'Miskolc','address1'), --1
    ('user2', 'user2@user2.com', 'password2', 'Miskolc','address2'), --2
    ('user3', 'user3@user3.com', 'password3', 'Miskolc','address3'); --3

INSERT INTO restaurants (name, location, picture) VALUES
	('Tasty Burger', 'Miskolc', 'https://s3.amazonaws.com/thumbnails.venngage.com/template/01dcc6e2-4575-405b-b5bd-bd984c2317a7.png'), --1
    ('Mamma Italiano', 'Budapest', 'https://s3.amazonaws.com/thumbnails.venngage.com/template/01dcc6e2-4575-405b-b5bd-bd984c2317a7.png'), --2
    ('Pizzeria', 'Miskolc', 'https://s3.amazonaws.com/thumbnails.venngage.com/template/01dcc6e2-4575-405b-b5bd-bd984c2317a7.png'), --3
    ('Golden Dragon', 'Miskolc', 'https://s3.amazonaws.com/thumbnails.venngage.com/template/01dcc6e2-4575-405b-b5bd-bd984c2317a7.png'), --4
    ('Pizza De La Arte', 'Budapest', 'https://s3.amazonaws.com/thumbnails.venngage.com/template/01dcc6e2-4575-405b-b5bd-bd984c2317a7.png'), --5
    ('Big Dinner', 'Budapest', 'https://s3.amazonaws.com/thumbnails.venngage.com/template/01dcc6e2-4575-405b-b5bd-bd984c2317a7.png'); --6

INSERT INTO products (name, price, availability,picture,description,category) VALUES
	('Classic Burger', 300, true, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQDNJO0zqj4-bqeqk9joALA7LrlCXv29z7TGM_P7WTMgQBFemvrrg', 'Tasty burger','appetizer'), --1
    ('Classic Pizza', 1000, true, 'https://static.netpincer.hu/cms/article/article-3a4ff8b4-c954-58fd-135e-873a6f5a8c0a/pizza_1.jpg','Tasty Classic Pizza', 'entree'), --2
    ('Pork Noodle', 700, true, 'https://cdn-image.myrecipes.com/sites/default/files/styles/medium_2x/public/image/recipes/oh/12/oh-chinese-pork-tenderloin-noodles-x.jpg?itok=5PbGxhAZ', '等中区别','entree'), --3
    ('Italiano Pasta', 800, true, 'http://www.mindmegette.hu/images/219/O/milani-spagetti.jpg', 'Tasty Spagetti','entree'), --4
    ('Pizza Fruit de Mer', 1100, true, 'http://rd-prod.s3.amazonaws.com/images/nouvelles_recettes/Pizza-aux-fruits-de-mer.jpg?1434377034', 'Tasty Pizza Fruit de Mer', 'entree'), --5
    ('Pizza Salame Picante', 1000, true, 'https://st2.depositphotos.com/1692343/5636/i/950/depositphotos_56360353-stock-photo-hot-homemade-pepperoni-pizza.jpg', 'Tasty Picante', 'entree'), --6
    ('Extra Bacon Burger', 1000, true, 'https://fullerssugarhouse.com/wp/wp-content/uploads/maple-bacon-burger-t.jpg', 'Tasty Burger with Bacon', 'entree'), --7
    ('Double Meat Burger', 1400, true, 'https://external-preview.redd.it/7T8abYnPaTVzPB1WnevmR5F8nBmjh0NEpAwjpzNVCak.jpg?width=768&auto=webp&s=84ea9747b7b4c0e075dcbf49966d2e41f2667e34', 'Tasty Big Burger', 'entree'), --8
    ('Huge Burger', 2500, true, 'https://media-cdn.tripadvisor.com/media/photo-s/07/9f/df/ec/biggest-burger-mmmm.jpg', 'Tasty Huge Burger', 'entree'), --9
    ('Tiramisu', 900, true, 'http://www.mindmegette.hu/images/170/O/crop_201611031209_tiramisu.jpg', 'Tasty Tiramisu', 'dessert'), --10
    ('Lasagne', 1000, true, 'http://www.mindmegette.hu/images/23/O/31468_lasagne_n-201510170727.jpg', 'Tasty Lasagne', 'entree'), --11
    ('Ramen Soup', 700, true, 'https://s23209.pcdn.co/wp-content/uploads/2014/10/Easy-Homemade-RamenIMG_9333edit-360x360.jpg', 'Tasty Ramen Soup', 'appetizer'), --12
    ('Chicken Wings', 750, true, 'https://www.rockrecipes.com/wp-content/uploads/2016/02/General-Tso-Chicken-Wings-close-up-photo-of-wings-on-white-plate.jpg', 'General Tso Chicken Wings', 'entree'); --13


INSERT INTO ratings (restaurant_id, user_id, rating) VALUES
	(1, 1, 3), --1
    (1, 2, 2), --2
    (2, 3, 5); --3

INSERT INTO menus (restaurant_id, product_id) VALUES
	(1, 1),
	(1, 7),
	(1, 8),
    (1, 10),
    (2, 4),
    (2, 5),
    (2, 10),
    (2, 11),
	(3, 2),
    (3, 5),
    (3, 6),
    (4, 3),
    (4, 12),
    (4, 13),
	(5, 2),
    (5, 5),
    (5, 6),
	(6, 1),
	(6, 2),
    (6, 11),
	(6, 12);

INSERT INTO carts (user_id) VALUES
	(1), --1
    (2); --2
INSERT INTO cart_items (cart_id, product_id, quantity, price) VALUES
	(1, 1, 2, 600), --1
    (1, 2, 1, 1000), --2
    (2, 3, 3, 2100); --3

INSERT INTO orders (user_id, courier_id, cart_id) VALUES
	(1, 2, 1), --1
    (1, 2, 2); --3
