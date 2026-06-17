# TPI DOO 2026 — Aguas Vital SA

> Trabajo Practico Integrador — Segunda Entrega  
> **Materia:** Diseno Orientado a Objetos  
> **Carrera:** Ingenieria en Sistemas — Universidad Blas Pascal  
> **Anio:** 2026

---

## Descripcion

Sistema de gestion de pedidos para **Aguas Vital SA**, una empresa distribuidora de agua mineral, bebidas y sodas. La aplicacion permite registrar nuevos pedidos y rendir entregas diarias, aplicando los patrones de diseno **DAO** y **MVC** vistos en la materia.

### Casos de uso implementados

| UC | Nombre | Descripcion |
|:--:|--------|-------------|
| UC-4 | Registrar Pedido | Alta de un nuevo pedido con seleccion de cliente, operador, productos y cantidades |
| UC-8 | Registrar Rendicion | Registro de la entrega diaria: cambio de estado, fecha de entrega, forma de pago |

---

## Tecnologias

| Tecnologia | Version | Uso |
|-----------|:-------:|-----|
| Java | 11 | Lenguaje principal |
| JavaFX | 13 | Interfaz grafica (FXML) |
| Maven | 3.x | Build y dependencias |
| SQLite | 3.x | Base de datos embebida |
| ModelMapper | 2.4 | Conversion DTO ↔ Modelo |
| ValidatorFX | 0.4 | Validacion de formularios |
| StarUML | 6.x | Diagrama de clases |

---

## Arquitectura

```
┌──────────────────────────────────────────────────────┐
│                    VISTA (FXML)                       │
│   principal.fxml │ pedidos.fxml │ editarPedido.fxml  │
│                       │ rendirPedido.fxml             │
└──────────┬───────────────────────────────┬───────────┘
           │      <<actualiza>>            │ <<consulta>>
           ▼                               ▼
┌──────────────────────┐     ┌──────────────────────────┐
│    CONTROLADOR        │     │        MODELO            │
│  PrincipalController  │────▶│  Modelo (abstract)       │
│  PedidosController    │     │    ├── Pedido            │
│  EditarPedidoController│    │    ├── Cliente           │
│  RendirPedidoController│    │    └── Producto          │
└──────────┬─────────────┘     └──────────┬───────────────┘
           │                              │ <<usa>>
           ▼                              ▼
┌──────────────────────────────────────────────────────────┐
│                      DAO                                 │
│   Dao<T> ← ClienteDao, ProductoDao, PedidoDao,           │
│            ZonaDao, DistribuidorDao                      │
│                      │                                   │
│               ConexionSql (SQLite)                       │
└──────────────────────────────────────────────────────────┘
```

### Patrones aplicados

| Patron | Fuente | Implementacion |
|--------|--------|----------------|
| **DAO** | Clase 8 (p16-18) | Interfaces DAO por entidad, DTOs, `ConexionSql` singleton. CRUD transaccional en `PedidoDao` |
| **MVC** | Clase 9 (p7-13) | `Modelo` abstracto → entidades concretas. FXML como Vista. Controladores JavaFX como Controller |

---

## Estructura del proyecto

```
TPI_DOO_2026/
├── Codigo Fuente/
│   ├── pom.xml
│   └── src/main/
│       ├── java/org/ubp/edu/doo/tpdooaguasvital2026/
│       │   ├── App.java                        # Entry point JavaFX
│       │   ├── controladores/                   # MVC - Controllers
│       │   │   ├── Controller.java              #   Base abstracta
│       │   │   ├── PrincipalController.java     #   Menu principal
│       │   │   ├── PedidosController.java       #   Listado y busqueda
│       │   │   ├── EditarPedidoController.java  #   UC-4: Registrar Pedido
│       │   │   └── RendirPedidoController.java  #   UC-8: Registrar Rendicion
│       │   ├── dao/                             # DAO - Acceso a datos
│       │   │   ├── Dao.java                     #   Interfaz generica
│       │   │   ├── ConexionSql.java             #   Conexion SQLite
│       │   │   ├── PedidoDao.java               #   CRUD transaccional
│       │   │   ├── ClienteDao.java              #   Consulta clientes
│       │   │   ├── ProductoDao.java             #   Consulta productos
│       │   │   ├── ZonaDao.java                 #   Consulta zonas
│       │   │   ├── DistribuidorDao.java         #   Consulta + buscar por zona
│       │   │   ├── EmpleadoDao.java             #   Listar operadores
│       │   │   └── PrecioDao.java               #   Buscar precio actual
│       │   ├── dto/                             # Data Transfer Objects
│       │   ├── modelo/                          # 19 clases de dominio
│       │   ├── factories/                       # Factory Method
│       │   └── prueba/                          # 38 tests automaticos
│       └── resources/
│           ├── principal.fxml
│           ├── pedidos.fxml
│           ├── editarPedido.fxml
│           ├── rendirPedido.fxml
│           └── esquema-aguas-vital.sql
├── Diagrama StarUML/
│   ├── Diagrama Patrones.mdj                    # Original 6 patrones
│   └── Diagrama Patrones Rediseno.mdj           # DAO + MVC (final)
├── Documentacion/
│   ├── Avances.md
│   ├── CONTEXT.md
│   ├── CHANGELOG.md
│   ├── Planilla UC3.docx
│   └── Planilla UC8.docx
├── PDFs Referencia/                              # Material de catedra
├── ESTRUCTURA_COMPLETA.md
└── README.md
```

---

## Compilar y ejecutar

```powershell
cd "Codigo Fuente"

# Compilar
mvn clean compile

# Ejecutar
mvn javafx:run

# Tests (38/38)
mvn exec:java "-Dexec.mainClass=org.ubp.edu.doo.tpdooaguasvital2026.prueba.TesteoExhaustivo"
```

---

## Formularios

### UC-4 — Registrar Pedido
- Seleccion de Cliente → auto-completa Zona y Distribuidor
- Seleccion de Producto → auto-completa Precio desde BD
- Fecha de entrega estimada simulada (+2 dias)
- Tabla de detalle con calculo automatico de subtotales
- Checkbox de aceptacion obligatorio
- Confirmacion muestra nro de pedido generado

### UC-8 — Registrar Rendicion
- Busqueda de pedido por numero
- Checkbox "Entregado" que alterna entre modo entrega y no-entrega
- Fecha/hora de entrega auto-completada con momento actual
- Forma de pago: Contado / Electronico
- Bloqueo de pedidos ya ENTREGADO o CANCELADO

---

## Autores

| Nombre | Legajo |
|--------|--------|
| Juan Jaramillo | — |
| Valentino Frache | — |
| Gonzalo Rementeria | — |
| Matias Sapa | — |

**Profesor:** Esp. Ing. Agustin Fernandez

---

## Licencia

Proyecto academico — Universidad Blas Pascal, 2026.
