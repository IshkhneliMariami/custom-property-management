CREATE
USER redmed with login password 'redmed';
CREATE
DATABASE redmed owner redmed;

INSERT INTO public.roles (id, name)
VALUES (1, 'ROLE_USER');
INSERT INTO public.roles (id, name)
VALUES (2, 'ROLE_ADMIN');
INSERT INTO public.roles (id, name)
VALUES (3, 'ROLE_PROPERTY_ADMIN');
INSERT INTO public.roles (id, name)
VALUES (4, 'ROLE_SUPPORT');

