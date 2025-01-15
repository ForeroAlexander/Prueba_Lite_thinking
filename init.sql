--
-- PostgreSQL database dump
--

-- Dumped from database version 17.2
-- Dumped by pg_dump version 17.2

-- Started on 2025-01-14 21:02:28

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 2 (class 3079 OID 16656)
-- Name: pgcrypto; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA public;


--
-- TOC entry 5001 (class 0 OID 0)
-- Dependencies: 2
-- Name: EXTENSION pgcrypto; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION pgcrypto IS 'cryptographic functions';


--
-- TOC entry 278 (class 1255 OID 17297)
-- Name: login(text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.login(email_input text, password_input text) RETURNS json
    LANGUAGE plpgsql
    AS $$
DECLARE
    user_record usuarios;
    result json;
BEGIN
    -- Buscar usuario por email
    SELECT * INTO user_record
    FROM usuarios
    WHERE email = email_input;

    -- Verificar si el usuario existe y la contraseña es correcta
    IF user_record.id IS NULL OR
       NOT (user_record.password = crypt(password_input, user_record.password)) THEN
        RAISE EXCEPTION 'Credenciales inválidas';
    END IF;

    -- Generar resultado
    result := json_build_object(
        'id', user_record.id,
        'email', user_record.email,
        'role', user_record.role
    );

    RETURN result;
END;
$$;


ALTER FUNCTION public.login(email_input text, password_input text) OWNER TO postgres;

--
-- TOC entry 277 (class 1255 OID 17293)
-- Name: update_updated_at_column(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.update_updated_at_column() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    NEW.updated_at = now();
    RETURN NEW;
END;
$$;


ALTER FUNCTION public.update_updated_at_column() OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 220 (class 1259 OID 17187)
-- Name: categorias; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.categorias (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    nombre text NOT NULL,
    descripcion text,
    created_at timestamp with time zone DEFAULT now()
);


ALTER TABLE public.categorias OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 17227)
-- Name: clientes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.clientes (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    nombre text NOT NULL,
    email text NOT NULL,
    telefono text,
    direccion text,
    created_at timestamp with time zone DEFAULT now()
);


ALTER TABLE public.clientes OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 17178)
-- Name: empresas; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.empresas (
    nit text NOT NULL,
    nombre text NOT NULL,
    direccion text NOT NULL,
    telefono text NOT NULL,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now()
);


ALTER TABLE public.empresas OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 17274)
-- Name: inventario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.inventario (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    producto_codigo text,
    cantidad integer DEFAULT 0 NOT NULL,
    ubicacion text,
    stock_minimo integer DEFAULT 0 NOT NULL,
    stock_maximo integer DEFAULT 0 NOT NULL,
    ultima_actualizacion timestamp with time zone DEFAULT now()
);


ALTER TABLE public.inventario OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 17298)
-- Name: orden_producto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.orden_producto (
    producto_codigo character varying(255) NOT NULL,
    orden_id uuid
);


ALTER TABLE public.orden_producto OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 17238)
-- Name: ordenes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ordenes (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    cliente_id uuid NOT NULL,
    fecha_orden timestamp with time zone DEFAULT now(),
    estado text NOT NULL,
    total numeric(38,2) DEFAULT 0 NOT NULL,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now(),
    fecha timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT ordenes_estado_check CHECK ((estado = ANY (ARRAY['pendiente'::text, 'completada'::text, 'cancelada'::text])))
);


ALTER TABLE public.ordenes OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 17256)
-- Name: ordenes_productos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ordenes_productos (
    orden_id uuid NOT NULL,
    producto_codigo text NOT NULL,
    cantidad integer NOT NULL,
    precio_unitario numeric(10,2) NOT NULL,
    subtotal numeric(12,2) NOT NULL,
    CONSTRAINT ordenes_productos_cantidad_check CHECK ((cantidad > 0))
);


ALTER TABLE public.ordenes_productos OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 17304)
-- Name: producto_categoria; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.producto_categoria (
    producto_codigo character varying(255) NOT NULL,
    categoria_id uuid
);


ALTER TABLE public.producto_categoria OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 17196)
-- Name: productos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.productos (
    codigo text NOT NULL,
    nombre text NOT NULL,
    caracteristicas text NOT NULL,
    precio_usd numeric(10,2) NOT NULL,
    precio_eur numeric(10,2) NOT NULL,
    precio_cop numeric(15,2) NOT NULL,
    empresa_nit text NOT NULL,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now(),
    preciocop numeric(38,2),
    precioeur numeric(38,2),
    preciousd numeric(38,2)
);


ALTER TABLE public.productos OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 17210)
-- Name: productos_categorias; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.productos_categorias (
    producto_codigo text NOT NULL,
    categoria_id uuid NOT NULL
);


ALTER TABLE public.productos_categorias OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 17309)
-- Name: roles_usuario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.roles_usuario (
    rol character varying(255),
    usuario_id uuid
);


ALTER TABLE public.roles_usuario OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 16693)
-- Name: usuarios; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.usuarios (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    email text NOT NULL,
    password text NOT NULL,
    role text NOT NULL,
    created_at timestamp with time zone DEFAULT now()
);


ALTER TABLE public.usuarios OWNER TO postgres;

--
-- TOC entry 4986 (class 0 OID 17187)
-- Dependencies: 220
-- Data for Name: categorias; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.categorias (id, nombre, descripcion, created_at) FROM stdin;
e4a7afee-f14d-4809-a52e-189cd9d9de8d	Electrónicos	Productos electrónicos y tecnología	2025-01-10 23:46:06.32658-05
e2d4f00d-509d-4662-871e-584b5de5d63f	Software	Aplicaciones y programas	2025-01-10 23:46:06.32658-05
d06a4603-6f75-4970-b7f7-d29b96ab2e2e	Servicios	Servicios profesionales	2025-01-10 23:46:06.32658-05
\.


--
-- TOC entry 4989 (class 0 OID 17227)
-- Dependencies: 223
-- Data for Name: clientes; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.clientes (id, nombre, email, telefono, direccion, created_at) FROM stdin;
3e6671ee-122a-4c49-8f53-71e10231ea20	Juan Pérez	juan.perez@email.com	+57 300 1234567	Calle 123 #45-67	2025-01-10 23:46:06.32658-05
9f57d6f8-1187-44f2-b765-e431c4401cfe	María García	maria.garcia@email.com	+57 310 9876543	Carrera 89 #12-34	2025-01-10 23:46:06.32658-05
d22fbc09-f924-444a-85f6-a8ca96e4329a	Carlos López	carlos.lopez@email.com	+57 320 4567890	Avenida 56 #78-90	2025-01-10 23:46:06.32658-05
\.


--
-- TOC entry 4985 (class 0 OID 17178)
-- Dependencies: 219
-- Data for Name: empresas; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.empresas (nit, nombre, direccion, telefono, created_at, updated_at) FROM stdin;
900123456-7	TechCorp S.A.	Calle Principal 123	+57 1234567	2025-01-10 23:46:06.32658-05	2025-01-10 23:46:06.32658-05
900987654-3	InnovaSoft Ltda	Avenida Central 456	+57 7654321	2025-01-10 23:46:06.32658-05	2025-01-10 23:46:06.32658-05
90012348-6	LiteThinking	calle 63b #15-89	+57 3052784963	2025-01-11 12:13:24.2568-05	2025-01-11 12:13:24.2568-05
\.


--
-- TOC entry 4992 (class 0 OID 17274)
-- Dependencies: 226
-- Data for Name: inventario; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.inventario (id, producto_codigo, cantidad, ubicacion, stock_minimo, stock_maximo, ultima_actualizacion) FROM stdin;
984c6ee0-03c1-4cf2-8c7b-9180f07c3951	LAPTOP001	15	Bodega Principal - Estante A1	5	30	2025-01-10 23:46:06.32658-05
5047e062-6952-4863-9a49-757a3bcd6835	SOFT001	100	Servidor de Licencias	20	500	2025-01-10 23:46:06.32658-05
3b607a24-4dd3-403a-9824-68f3bc6bb448	SERV001	50	Pool de Soporte	10	100	2025-01-10 23:46:06.32658-05
154828e0-64ec-4579-ab7f-a8f99ac16bde	MONITOR001	25	Bodega Principal - Estante B2	8	40	2025-01-10 23:46:06.32658-05
afb55cdb-959c-43dc-80bd-8f8ff494b0ad	TECLADO001	45	Bodega Principal - Estante C3	15	60	2025-01-10 23:46:06.32658-05
\.


--
-- TOC entry 4993 (class 0 OID 17298)
-- Dependencies: 227
-- Data for Name: orden_producto; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.orden_producto (producto_codigo, orden_id) FROM stdin;
\.


--
-- TOC entry 4990 (class 0 OID 17238)
-- Dependencies: 224
-- Data for Name: ordenes; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.ordenes (id, cliente_id, fecha_orden, estado, total, created_at, updated_at, fecha) FROM stdin;
4c003b47-a88d-48c0-bda4-b73acb38bddf	3e6671ee-122a-4c49-8f53-71e10231ea20	2025-01-10 23:46:06.32658-05	completada	1499.98	2025-01-10 23:46:06.32658-05	2025-01-10 23:46:06.32658-05	2025-01-11 01:43:34.61861
\.


--
-- TOC entry 4991 (class 0 OID 17256)
-- Dependencies: 225
-- Data for Name: ordenes_productos; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.ordenes_productos (orden_id, producto_codigo, cantidad, precio_unitario, subtotal) FROM stdin;
4c003b47-a88d-48c0-bda4-b73acb38bddf	LAPTOP001	1	1299.99	1299.99
\.


--
-- TOC entry 4994 (class 0 OID 17304)
-- Dependencies: 228
-- Data for Name: producto_categoria; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.producto_categoria (producto_codigo, categoria_id) FROM stdin;
\.


--
-- TOC entry 4987 (class 0 OID 17196)
-- Dependencies: 221
-- Data for Name: productos; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.productos (codigo, nombre, caracteristicas, precio_usd, precio_eur, precio_cop, empresa_nit, created_at, updated_at, preciocop, precioeur, preciousd) FROM stdin;
LAPTOP001	Laptop Pro X1	Intel i7, 16GB RAM, 512GB SSD, 15.6" 4K	1299.99	1199.99	5200000.00	900123456-7	2025-01-10 23:46:06.32658-05	2025-01-10 23:46:06.32658-05	\N	\N	\N
SOFT001	Software Suite Premium	Suite completa de productividad empresarial	299.99	279.99	1200000.00	900987654-3	2025-01-10 23:46:06.32658-05	2025-01-10 23:46:06.32658-05	\N	\N	\N
SERV001	Soporte Técnico Premium	Soporte 24/7, tiempo de respuesta garantizado	99.99	89.99	400000.00	900123456-7	2025-01-10 23:46:06.32658-05	2025-01-10 23:46:06.32658-05	\N	\N	\N
MONITOR001	Monitor UltraWide 34"	Panel IPS, 3440x1440, 144Hz, HDR	699.99	649.99	2800000.00	900123456-7	2025-01-10 23:46:06.32658-05	2025-01-10 23:46:06.32658-05	\N	\N	\N
TECLADO001	Teclado Mecánico Pro	Switches Cherry MX, RGB, Aluminio	149.99	139.99	600000.00	900123456-7	2025-01-10 23:46:06.32658-05	2025-01-10 23:46:06.32658-05	\N	\N	\N
080001	Control	control de xbox diferentes equipo	200.00	300.00	300000.00	90012348-6	2025-01-13 15:06:17.843686-05	\N	\N	\N	\N
\.


--
-- TOC entry 4988 (class 0 OID 17210)
-- Dependencies: 222
-- Data for Name: productos_categorias; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.productos_categorias (producto_codigo, categoria_id) FROM stdin;
LAPTOP001	e4a7afee-f14d-4809-a52e-189cd9d9de8d
SOFT001	e2d4f00d-509d-4662-871e-584b5de5d63f
SERV001	d06a4603-6f75-4970-b7f7-d29b96ab2e2e
MONITOR001	e4a7afee-f14d-4809-a52e-189cd9d9de8d
TECLADO001	e4a7afee-f14d-4809-a52e-189cd9d9de8d
\.


--
-- TOC entry 4995 (class 0 OID 17309)
-- Dependencies: 229
-- Data for Name: roles_usuario; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.roles_usuario (rol, usuario_id) FROM stdin;
\.


--
-- TOC entry 4984 (class 0 OID 16693)
-- Dependencies: 218
-- Data for Name: usuarios; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.usuarios (id, email, password, role, created_at) FROM stdin;
fd2d846d-7007-4a64-bc50-aa3693d62ea0	admin@example.com	$2a$06$7soCxwQkrg6KkvGWqMV62edFUwkmCSVgMlBmjwEmkQEVCOs2YdGt.	admin	2025-01-10 23:30:50.1173-05
f89ae399-e0a0-4d8d-b7e2-450c3ce47f01	user@example.com	$2a$06$SH5uJ4/jDMy4HuFtosLXmO1MCnCHhkcj97Y0tBXyIYjfv/b7ZdCkC	external	2025-01-10 23:30:50.1173-05
\.


--
-- TOC entry 4807 (class 2606 OID 17195)
-- Name: categorias categorias_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categorias
    ADD CONSTRAINT categorias_pkey PRIMARY KEY (id);


--
-- TOC entry 4813 (class 2606 OID 17237)
-- Name: clientes clientes_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.clientes
    ADD CONSTRAINT clientes_email_key UNIQUE (email);


--
-- TOC entry 4815 (class 2606 OID 17235)
-- Name: clientes clientes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.clientes
    ADD CONSTRAINT clientes_pkey PRIMARY KEY (id);


--
-- TOC entry 4805 (class 2606 OID 17186)
-- Name: empresas empresas_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.empresas
    ADD CONSTRAINT empresas_pkey PRIMARY KEY (nit);


--
-- TOC entry 4821 (class 2606 OID 17285)
-- Name: inventario inventario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.inventario
    ADD CONSTRAINT inventario_pkey PRIMARY KEY (id);


--
-- TOC entry 4823 (class 2606 OID 17287)
-- Name: inventario inventario_producto_codigo_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.inventario
    ADD CONSTRAINT inventario_producto_codigo_key UNIQUE (producto_codigo);


--
-- TOC entry 4817 (class 2606 OID 17250)
-- Name: ordenes ordenes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ordenes
    ADD CONSTRAINT ordenes_pkey PRIMARY KEY (id);


--
-- TOC entry 4819 (class 2606 OID 17263)
-- Name: ordenes_productos ordenes_productos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ordenes_productos
    ADD CONSTRAINT ordenes_productos_pkey PRIMARY KEY (orden_id, producto_codigo);


--
-- TOC entry 4811 (class 2606 OID 17216)
-- Name: productos_categorias productos_categorias_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.productos_categorias
    ADD CONSTRAINT productos_categorias_pkey PRIMARY KEY (producto_codigo, categoria_id);


--
-- TOC entry 4809 (class 2606 OID 17204)
-- Name: productos productos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.productos
    ADD CONSTRAINT productos_pkey PRIMARY KEY (codigo);


--
-- TOC entry 4801 (class 2606 OID 16704)
-- Name: usuarios usuarios_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_email_key UNIQUE (email);


--
-- TOC entry 4803 (class 2606 OID 16702)
-- Name: usuarios usuarios_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_pkey PRIMARY KEY (id);


--
-- TOC entry 4836 (class 2620 OID 17294)
-- Name: empresas update_empresas_updated_at; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER update_empresas_updated_at BEFORE UPDATE ON public.empresas FOR EACH ROW EXECUTE FUNCTION public.update_updated_at_column();


--
-- TOC entry 4838 (class 2620 OID 17296)
-- Name: ordenes update_ordenes_updated_at; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER update_ordenes_updated_at BEFORE UPDATE ON public.ordenes FOR EACH ROW EXECUTE FUNCTION public.update_updated_at_column();


--
-- TOC entry 4837 (class 2620 OID 17295)
-- Name: productos update_productos_updated_at; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER update_productos_updated_at BEFORE UPDATE ON public.productos FOR EACH ROW EXECUTE FUNCTION public.update_updated_at_column();


--
-- TOC entry 4831 (class 2606 OID 17323)
-- Name: orden_producto fk1i8j9jb6k086ievl8xqod193x; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orden_producto
    ADD CONSTRAINT fk1i8j9jb6k086ievl8xqod193x FOREIGN KEY (orden_id) REFERENCES public.ordenes(id);


--
-- TOC entry 4835 (class 2606 OID 17333)
-- Name: roles_usuario fk69o485flaet41skdbkc54i2j0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles_usuario
    ADD CONSTRAINT fk69o485flaet41skdbkc54i2j0 FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id);


--
-- TOC entry 4833 (class 2606 OID 17317)
-- Name: producto_categoria fkbamvu2tbe3xwdrukjcosjwxtc; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto_categoria
    ADD CONSTRAINT fkbamvu2tbe3xwdrukjcosjwxtc FOREIGN KEY (producto_codigo) REFERENCES public.productos(codigo);


--
-- TOC entry 4834 (class 2606 OID 17328)
-- Name: producto_categoria fkck76h1dqwbw3rp8gkxkxytqe6; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto_categoria
    ADD CONSTRAINT fkck76h1dqwbw3rp8gkxkxytqe6 FOREIGN KEY (categoria_id) REFERENCES public.categorias(id);


--
-- TOC entry 4832 (class 2606 OID 17312)
-- Name: orden_producto fkryn9q066cfbh7di5b14bgom0t; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orden_producto
    ADD CONSTRAINT fkryn9q066cfbh7di5b14bgom0t FOREIGN KEY (producto_codigo) REFERENCES public.productos(codigo);


--
-- TOC entry 4830 (class 2606 OID 17288)
-- Name: inventario inventario_producto_codigo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.inventario
    ADD CONSTRAINT inventario_producto_codigo_fkey FOREIGN KEY (producto_codigo) REFERENCES public.productos(codigo) ON DELETE CASCADE;


--
-- TOC entry 4827 (class 2606 OID 17251)
-- Name: ordenes ordenes_cliente_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ordenes
    ADD CONSTRAINT ordenes_cliente_id_fkey FOREIGN KEY (cliente_id) REFERENCES public.clientes(id) ON DELETE CASCADE;


--
-- TOC entry 4828 (class 2606 OID 17264)
-- Name: ordenes_productos ordenes_productos_orden_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ordenes_productos
    ADD CONSTRAINT ordenes_productos_orden_id_fkey FOREIGN KEY (orden_id) REFERENCES public.ordenes(id) ON DELETE CASCADE;


--
-- TOC entry 4829 (class 2606 OID 17269)
-- Name: ordenes_productos ordenes_productos_producto_codigo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ordenes_productos
    ADD CONSTRAINT ordenes_productos_producto_codigo_fkey FOREIGN KEY (producto_codigo) REFERENCES public.productos(codigo) ON DELETE CASCADE;


--
-- TOC entry 4825 (class 2606 OID 17222)
-- Name: productos_categorias productos_categorias_categoria_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.productos_categorias
    ADD CONSTRAINT productos_categorias_categoria_id_fkey FOREIGN KEY (categoria_id) REFERENCES public.categorias(id) ON DELETE CASCADE;


--
-- TOC entry 4826 (class 2606 OID 17217)
-- Name: productos_categorias productos_categorias_producto_codigo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.productos_categorias
    ADD CONSTRAINT productos_categorias_producto_codigo_fkey FOREIGN KEY (producto_codigo) REFERENCES public.productos(codigo) ON DELETE CASCADE;


--
-- TOC entry 4824 (class 2606 OID 17205)
-- Name: productos productos_empresa_nit_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.productos
    ADD CONSTRAINT productos_empresa_nit_fkey FOREIGN KEY (empresa_nit) REFERENCES public.empresas(nit) ON DELETE CASCADE;


-- Completed on 2025-01-14 21:02:29

--
-- PostgreSQL database dump complete
--

