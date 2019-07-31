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
    password varchar(8) not null,
    role text not null,
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
update users set profit = (select sum(orders.price * 100 / users.cut) from orders left join users on orders.user_id = users.id WHERE orders.complete = true);
return new;
end;
' language plpgsql;

create trigger update_profit
after update on orders for each row
execute procedure update_profit();

