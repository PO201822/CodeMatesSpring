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
    roles text[] default ARRAY['ROLE_USER'],
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
    picture text not null,
    description text not null
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
	checked_out boolean default false,
	checkout_date timestamp default null
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
update users set roles = ARRAY[''ROLE_USER''] where roles is null;
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
    ('u', 'user3@user3.com', 'u', 'Miskolc','address3'); --3
INSERT INTO users(name, email, password, roles, location, address) values
('a', 'user1@user1.com', 'a', ARRAY['ROLE_ADMIN'] , 'Miskolc','address1'),
('c', 'user2@user2.com', 'c', ARRAY['ROLE_COURIER'] , 'Miskolc','address2');


INSERT INTO restaurants (name, location, picture, description) VALUES
	('Tasty Burger', 'Miskolc', 'assets/restaurant-logos/burger.png','Burgers. Yes, burgers. We serve the best burgers in this planet!'), --1
    ('Mamma Italiano', 'Budapest', 'assets/restaurant-logos/pasta.png','Un agradecimiento a las madres, que trabajando fuera de casa o no, han comprendido y realizan esa labor de ser madre de familia. Actually, who reads this!'), --2
    ('Pablo''s Pizzeria', 'Miskolc', 'assets/restaurant-logos/pizza.png','We serve original, tasty, and hand crafted pizzas!'), --3
    ('Golden Dragon', 'Miskolc', 'assets/restaurant-logos/ramen.png','弗勒杰迪伊艾 迪艾诶弗勒艾 艾伊娜哦诶屁艾诶艾哦!'), --4
    ('Pizza De La Arte', 'Budapest', 'assets/restaurant-logos/pizza.png','Pizza is love, pizza is life!'), --5
    ('Big Dinner', 'Budapest', 'assets/restaurant-logos/steak.png','Big dinner, big prices!'); --6

INSERT INTO products (name, price, availability,picture,description,category) VALUES
	('Classic Burger', 600, true, 'assets/menu-icons/burger-classic.jpeg', 'Tasty classic burger','appetizer'), --1
    ('Classic Pizza', 1000, true, 'assets/menu-icons/pizza-classic.jpg','Tasty classic pizza', 'entree'), --2
    ('Pork Noodle', 700, true, 'assets/menu-icons/pork-noodle.jpg', '等中区别','entree'), --3
    ('Italiano Pasta', 800, true, 'assets/menu-icons/italiano-pasta.jpg' , 'Tasty spagetti','entree'), --4
    ('Pizza Fruits de Mer', 1100, true, 'assets/menu-icons/pizza-fruits-de-mer.jpg' , 'Tasty Pizza Fruits de Mer', 'entree'), --5
    ('Pizza Picante', 1000, true, 'assets/menu-icons/pizza-picante.jpg', 'Tasty Picante', 'entree'), --6
    ('Extra Bacon Burger', 1000, true, 'assets/menu-icons/burger-bacon.jpg', 'Tasty Burger with Bacon', 'entree'), --7
    ('Double Meat Burger', 1400, true, 'assets/menu-icons/burger-double.jpg', 'Tasty Big Burger', 'entree'), --8
    ('Huge Burger', 2500, true, 'assets/menu-icons/burger-huge.jpg', 'Tasty Huge Burger', 'entree'), --9
    ('Tiramisu', 900, true, 'assets/menu-icons/tiramisu.jpg', 'Tasty Tiramisu', 'dessert'), --10
    ('Lasagne', 1000, true, 'assets/menu-icons/lasagne.jpg', 'Tasty Lasagne', 'entree'), --11
    ('Ramen Soup', 700, true, 'assets/menu-icons/ramen-soup.jpg', 'Tasty Ramen Soup', 'appetizer'), --12
    ('Chicken Wings', 750, true, 'assets/menu-icons/chicken-wings.jpg', 'General Tso Chicken Wings', 'entree'); --13


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
