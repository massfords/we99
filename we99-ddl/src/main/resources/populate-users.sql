-- This file is meant for local testing. If needed, we can add more users.
-- The gmail account below is real but I won't be putting the password into the
-- source code. We'll need to provision it locally on our dev boxes unless there
-- is a better solution (maybe the Amazon Simple Email Service?)
--
insert into user (id, email, firstname, lastname, password, salt)
values (1, 'we99.2015@gmail.com', 'Test', 'User', '7c877c769756c1182dde9c9cde5e9fe38ae9d371ba080396d564a2e9b06a5433', 'salt');
