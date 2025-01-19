# Proyecto de Aplicación Java Spring Boot y React para Gestión Empresarial

Este proyecto es una aplicación web desarrollada para gestionar empresas, productos, inventario y autenticación de usuarios utilizando Java, Spring Boot, React y desplegado en AWS.

---

### Funcionalidades Principales

#### Vista Empresa
- Formulario para capturar información de empresas:
  - **NIT** (Llave primaria)
  - Nombre de la empresa
  - Dirección
  - Teléfono

#### Vista Productos
- Formulario para capturar información de productos:
  - Código
  - Nombre del producto
  - Características
  - Precio en varias monedas
  - Relacionado con una empresa

#### Vista Inicio de Sesión
- Formulario para autenticación de usuarios:
  - Correo electrónico
  - Contraseña (encriptada)

#### Vista de Inventario
- Permite descargar un PDF con la información de inventario.
- Utiliza una API de AWS para enviar el PDF a un correo deseado.

---

### Tipos de Usuarios

#### Administrador:
- Acceso a funciones de CRUD de Empresas.
- Registro y edición de productos por empresa.
- Gestión de inventario.

#### Externo:
- Puede visualizar información de empresas como visitante.

---

### Modelo Entidad-Relación

#### Entidades en la base de datos:
- **Empresa**
- **Productos**
- **Categorías**
- **Clientes**
- **Órdenes**

#### Requisitos específicos:
- Un **Producto** puede pertenecer a múltiples **Categorías**.
- Un **Cliente** puede tener múltiples **Órdenes**.
- Las **Órdenes** pueden contener múltiples **Productos**.

---

### Seguridad
- Contraseñas encriptadas para autenticación de usuarios administradores.

---

### Despliegue en AWS
La aplicación está desplegada en un servidor en la nube de AWS.

---

## Entregables

- **Usuarios y Contraseñas**:
  - **Administrador**:
    - Correo: `admin@example.com`
    - Contraseña: `admin123`
  - **Externo**:
    - Correo: `user@example.com`
    - Contraseña: `user123`

---

## Instrucciones de Instalación y Uso

### Prerequisitos
- **Java JDK**
- **Maven**
- **Node.js y npm**
- **PostgreSQL**

### Instalación

1. **Configurar la base de datos en PostgreSQL.**

2. **Configurar las variables de entorno necesarias.**

3. **Construir y ejecutar la aplicación:**
   ```bash
   mvn spring-boot:run

### Uso

