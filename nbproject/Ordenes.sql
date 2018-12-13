--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.5
-- Dumped by pg_dump version 9.3.5
-- Started on 2018-12-12 19:38:47 -05

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

DROP DATABASE "Cajero";
--
-- TOC entry 2201 (class 1262 OID 6225005)
-- Name: Cajero; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE "Cajero" WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'C' LC_CTYPE = 'C';


ALTER DATABASE "Cajero" OWNER TO postgres;

\connect "Cajero"

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 5 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO postgres;

--
-- TOC entry 2202 (class 0 OID 0)
-- Dependencies: 5
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- TOC entry 171 (class 3079 OID 12018)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2204 (class 0 OID 0)
-- Dependencies: 171
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 170 (class 1259 OID 6225033)
-- Name: billetes2; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE billetes2 (
    denominacion integer NOT NULL,
    cantidad integer
);


ALTER TABLE public.billetes2 OWNER TO postgres;

--
-- TOC entry 2196 (class 0 OID 6225033)
-- Dependencies: 170
-- Data for Name: billetes2; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO billetes2 (denominacion, cantidad) VALUES (20000, 0);
INSERT INTO billetes2 (denominacion, cantidad) VALUES (10000, 0);
INSERT INTO billetes2 (denominacion, cantidad) VALUES (50000, 3);


--
-- TOC entry 2088 (class 2606 OID 6225037)
-- Name: pk_billetes2; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY billetes2
    ADD CONSTRAINT pk_billetes2 PRIMARY KEY (denominacion);


--
-- TOC entry 2203 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2018-12-12 19:38:48 -05

--
-- PostgreSQL database dump complete
--

