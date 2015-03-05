-- This file is meant for local testing. If needed, we can add more users.
-- The gmail account below is real but I won't be putting the password into the
-- source code. We'll need to provision it locally on our dev boxes unless there
-- is a better solution (maybe the Amazon Simple Email Service?)
--

-- Insert all of the Roles
insert into role (id, name) values (1, 'Admin');
insert into role (id, name) values (2, 'Scientist');
insert into role (id, name) values (3, 'Guest');

-- Insert all of the Permissions
-- These permissions are not very granular. We can make them more or
-- less so as neeed.
insert into Permission(id, name) values (10, 'PERM_READ_COMPOUNDS');
insert into Permission(id, name) values (11, 'PERM_MODIFY_COMPOUNDS');

insert into Permission(id, name) values (20, 'PERM_READ_PLATES');
insert into Permission(id, name) values (21, 'PERM_MODIFY_PLATES');

insert into Permission(id, name) values (30, 'PERM_READ_PLATEMAPS');
insert into Permission(id, name) values (31, 'PERM_MODIFY_PLATEMAPS');

insert into Permission(id, name) values (40, 'PERM_READ_PLATETYPES');
insert into Permission(id, name) values (41, 'PERM_MODIFY_PLATETYPES');

insert into Permission(id, name) values (50, 'PERM_READ_PROTOCOLS');
insert into Permission(id, name) values (51, 'PERM_MODIFY_PROTOCOLS');

insert into Permission(id, name) values (61, 'PERM_MODIFY_USERROLE');

insert into Permission(id, name) values (70, 'PERM_READ_EXPERIMENTS');
insert into Permission(id, name) values (71, 'PERM_MODIFY_EXPERIMENTS');


-- Admins get all permissions
-- Note: We're not taking advantage of Spring's Role hierarchy code. That always
-- seemed a bit too complicated to me
insert into Role_Permission (Role_id, permissions_id) values (1,10);
insert into Role_Permission (Role_id, permissions_id) values (1,11);
insert into Role_Permission (Role_id, permissions_id) values (1,20);
insert into Role_Permission (Role_id, permissions_id) values (1,21);
insert into Role_Permission (Role_id, permissions_id) values (1,30);
insert into Role_Permission (Role_id, permissions_id) values (1,31);
insert into Role_Permission (Role_id, permissions_id) values (1,40);
insert into Role_Permission (Role_id, permissions_id) values (1,41);
insert into Role_Permission (Role_id, permissions_id) values (1,50);
insert into Role_Permission (Role_id, permissions_id) values (1,51);
insert into Role_Permission (Role_id, permissions_id) values (1,61);
insert into Role_Permission (Role_id, permissions_id) values (1,70);
insert into Role_Permission (Role_id, permissions_id) values (1,71);

-- Scientists get fewer permissions (currently this is everything except the
-- ability to elevate someone to an admin)
insert into Role_Permission (Role_id, permissions_id) values (2,10);
insert into Role_Permission (Role_id, permissions_id) values (2,11);
insert into Role_Permission (Role_id, permissions_id) values (2,20);
insert into Role_Permission (Role_id, permissions_id) values (2,21);
insert into Role_Permission (Role_id, permissions_id) values (2,30);
insert into Role_Permission (Role_id, permissions_id) values (2,31);
insert into Role_Permission (Role_id, permissions_id) values (2,40);
insert into Role_Permission (Role_id, permissions_id) values (2,41);
insert into Role_Permission (Role_id, permissions_id) values (2,50);
insert into Role_Permission (Role_id, permissions_id) values (2,51);
insert into Role_Permission (Role_id, permissions_id) values (2,70);
insert into Role_Permission (Role_id, permissions_id) values (2,71);

-- Guests get nothing as of yet. This is really only used for initial testing.
-- Proper guest access would require more granularity. Perhaps they can see
-- things but not change them.
insert into Role_Permission (Role_id, permissions_id) values (3,10);
insert into Role_Permission (Role_id, permissions_id) values (3,20);
insert into Role_Permission (Role_id, permissions_id) values (3,30);
insert into Role_Permission (Role_id, permissions_id) values (3,40);
insert into Role_Permission (Role_id, permissions_id) values (3,50);
insert into Role_Permission (Role_id, permissions_id) values (3,70);

insert into user (id, email, firstname, lastname, password, salt, role_id, passwordStatus)
values (1, 'we99.2015@gmail.com', 'Test', 'User',
        '7c877c769756c1182dde9c9cde5e9fe38ae9d371ba080396d564a2e9b06a5433',
        'salt', 1, 0);

-- A dummy guest user for the purposes of testing the Guest privileges
insert into user (id, email, firstname, lastname, password, salt, role_id, passwordStatus)
values (2, 'we99.2015@example', 'Guest', 'User',
        '7c877c769756c1182dde9c9cde5e9fe38ae9d371ba080396d564a2e9b06a5433',
        'salt', 3, 0);
