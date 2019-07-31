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
    roles text[] not null,
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
    max_cut numeric default 100
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
    rating numeric check (rating > 0)
);

CREATE TABLE menus(
    restaurant_id int references restaurants(id),
    product_id int references products(id)
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
    courier_id int references users(id),
    cart_id int references carts(id),
    picked_up boolean default false,
    complete boolean default false
);

create or replace function total_cart_price()
returns trigger as '
begin
update carts set price = (select sum(cart_items.price) from cart_items left join carts on cart_items.cart_id = carts.id);
return new;
end;
' language plpgsql;

create trigger total_cart_price
after insert or delete on cart_items for each row
execute procedure total_cart_price();

create or replace function update_rating()
returns trigger as '
begin
update restaurants set rating = (select avg(ratings.rating) from ratings join restaurants on ratings.restaurant_id = restaurants.id);
return new;
end;
' language plpgsql;

create trigger update_rating
after insert on ratings for each row
execute procedure update_rating();

create or replace function update_profit()
returns trigger as '
begin
update users set profit = (select sum(carts.price * 100 / users.cut) from carts left join users on orders.user_id = users.id WHERE orders.complete = true);
return new;
end;
' language plpgsql;

create trigger update_profit
after update on orders for each row
execute procedure update_profit();

INSERT INTO users (name, email, password, roles, location, address) VALUES
	('user1', 'user1@user1.com', 'password1', '{"ROLE_USER"}', 'Miskolc','address1'), --1
    ('user2', 'user2@user2.com', 'password2', '{"ROLE_USER"}', 'Miskolc','address2'), --2
    ('user3', 'user3@user3.com', 'password3', '{"ROLE_USER"}', 'Miskolc','address3'); --3

INSERT INTO restaurants (name, location) VALUES
	('TastyBurger', 'Miskolc'), --1
    ('MammaItaliano', 'Budapest'), --2
    ('Pizzeria', 'Miskolc'), --3
    ('Golden Dragon', 'Miskolc'); --4

INSERT INTO products (name, price, availability,picture,description,category) VALUES
	('CheeseBurger', 300, true, 'https://killerburger.com/wp-content/uploads/2016/03/Meathead-Website-300x300.jpg', 'Tasty burger','appetizer'), --1
    ('Hawaii Pizza', 1000, true, 'https://paragonpizza.ca/wp-content/uploads/2018/11/Tropical-Hawaiian-Meat-Pizza-300x300-1.png', 'Tasty Hawaii Pizza','entree'), --2
    ('9 treasure beef', 700, true, 'https://www.billyparisi.com/wp-content/uploads/stir-fry-page.jpg', '等中区别','entree'), --3
    ('Italiano Pasta', 800, true, 'http://www.mindmegette.hu/images/219/O/milani-spagetti.jpg', 'Tasty Spagetti','entree'); --4

INSERT INTO ratings (restaurant_id, user_id, rating) VALUES
	(1, 1, 3), --1
    (1, 2, 2), --2
    (2, 3,5); --3

INSERT INTO menus (restaurant_id, product_id) VALUES
	(1, 1), --1
    (2, 2), --2
    (2, 4), --3
    (3, 2), --4
    (4, 3); --5

INSERT INTO carts (user_id) VALUES
	(1), --1
    (2); --2
INSERT INTO cart_items (cart_id, product_id, quantity, price) VALUES
	(1, 1, 2, 600), --1
    (1, 2, 1, 1000), --2
    (2, 3, 3, 2100); --3

INSERT INTO orders (user_id, courier_id, cart_id) VALUES
	(1, 2, 1), --1
    (2, 2, 2); --3
