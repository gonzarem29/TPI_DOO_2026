# Estructura Completa del Proyecto — Segunda Entrega TP DOO 2026

**Proyecto:** Aguas Vital SA  
**Materia:** Diseño Orientado a Objetos — UBP  
**Fecha:** 16/06/2026  
**Integrantes:** Juan Jaramillo, Valentino Frache, Gonzalo Rementeria, Matias Sapa

---

## Índice

1. [Contexto General del Proyecto](#1-contexto-general-del-proyecto)
2. [Estructura de Archivos y Carpetas](#2-estructura-de-archivos-y-carpetas)
3. [Patrón DAO + Factory Method](#3-patrón-dao--factory-method)
4. [Patrón MVC](#4-patrón-mvc)
5. [Patrón State](#5-patrón-state-solo-diagrama)
6. [Patrón Strategy](#6-patrón-strategy-solo-diagrama)
7. [Patrón Observer](#7-patrón-observer-solo-diagrama)
8. [Clases de Soporte (Modelo)](#8-clases-de-soporte-modelo)
9. [DTOS](#9-dtos)
10. [Controladores JavaFX](#10-controladores-javafx)
11. [Jerarquía de Clases Completa](#11-jerarquía-de-clases-completa)
12. [Cómo Organizar el Diagrama StarUML](#12-cómo-organizar-el-diagrama-staruml)
13. [Resumen de Verificación (40 tests)](#13-resumen-de-verificación-40-tests)

---

## 1. Contexto General del Proyecto

### Qué es esto

Es la **Segunda Entrega** del TP Integrador de Diseño Orientado a Objetos. El proyecto modela un sistema de gestión de pedidos para **Aguas Vital SA**, una empresa que distribuye agua mineral, bebidas y sodas.

### Qué incluye la entrega

| Actividad | Descripción | Estado |
|-----------|-------------|--------|
| **1. Esqueleto Maven** | Proyecto JavaFX 13 + Maven compilable | ✅ `mvn clean compile` BUILD SUCCESS |
| **2. DAOs + SQLite** | Capa de acceso a datos con interfaz genérica `Dao<T>` y SQLite | ✅ 5 DAOs implementados |
| **3. FXML + Controladores** | 3 pantallas (menú, listado, formulario) con validación | ✅ UC-4 (Registrar) + UC-8 (Modificar) |
| **4. Diagrama StarUML** | 25 clases + 23 relaciones con 6 patrones de diseño | ✅ 48 vistas, todo correcto |

### Cómo entender los archivos

| Archivo | Qué contiene |
|---------|-------------|
| `Avance segunda entrega/Diagrama StarUML/Diagrama Patrones.mdj` | El archivo StarUML. Abrir con StarUML 6.x |
| `Avance segunda entrega/Documentacion/Avances.md` | Justificación de cada patrón contra los PDFs de clase, con páginas |
| `Avance segunda entrega/Documentacion/CHANGELOG.md` | Bitácora de todos los cambios técnicos realizados |
| `Avance segunda entrega/Documentacion/CONTEXT.md` | Resumen rápido del proyecto para retomar rápido |
| `Avance segunda entrega/Codigo Fuente/` | Proyecto Maven completo (solo src/, sin target) |
| `Avance segunda entrega/PDFs Referencia/` | PDFs de clase 8 a 14 (solo los de patrones) |
| `Avance segunda entrega/Scripts/` | Scripts Python usados para manipular el .mdj |
| `Avance segunda entrega/Imagenes/` | Imágenes extraídas de los PDFs |
| `Avance segunda entrega/Contenido Extraido/` | Texto extraído de los PDFs relevantes |

### Comandos básicos

```powershell
cd Avance segunda entrega\Codigo Fuente
mvn clean compile          # Compilar
mvn javafx:run              # Ejecutar la app
mvn exec:java "-Dexec.mainClass=org.ubp.edu.doo.tpdooaguasvital2026.prueba.TesteoExhaustivo"  # Tests
```

---

## 2. Estructura de Archivos y Carpetas

```
tp-doo-aguas-vital-2026/
├── pom.xml                              # Maven con JavaFX 13, SQLite, ModelMapper, ValidatorFX
└── src/main/
    ├── java/org/ubp/edu/doo/tpdooaguasvital2026/
    │   ├── App.java                     # Entry point JavaFX
    │   ├── controladores/               # Capa Controller (MVC)
    │   │   ├── Controller.java          #   Clase base abstracta
    │   │   ├── PrincipalController.java #   Menú principal
    │   │   ├── PedidosController.java   #   Listado y búsqueda
    │   │   └── EditarPedidoController.java # Formulario UC-4 / UC-8
    │   ├── dao/                         # Capa DAO (Patrón DAO)
    │   │   ├── ConexionSql.java         #   Conexión SQLite
    │   │   ├── Dao.java                 #   Interfaz genérica <<interface>>
    │   │   ├── ClienteDao.java          #   Implementación Cliente
    │   │   ├── ProductoDao.java         #   Implementación Producto
    │   │   ├── PedidoDao.java           #   Implementación Pedido (transaccional)
    │   │   ├── ZonaDao.java             #   Implementación Zona
    │   │   └── DistribuidorDao.java     #   Implementación Distribuidor
    │   ├── dto/                         # Data Transfer Objects
    │   │   ├── ClienteDto.java
    │   │   ├── ProductoDto.java
    │   │   ├── PedidoDto.java
    │   │   ├── ZonaDto.java
    │   │   ├── DistribuidorDto.java
    │   │   ├── DetallePedidoDto.java
    │   │   └── FacturaDto.java
    │   ├── factories/                   # Factory Method
    │   │   ├── FabricaDao.java          #   Crea DAOs por nombre
    │   │   └── FabricaModelo.java       #   Crea Modelos por nombre
    │   ├── modelo/                      # Capa Modelo (MVC)
    │   │   ├── Modelo.java              #   Clase abstracta base
    │   │   ├── Cliente.java
    │   │   ├── Pedido.java
    │   │   ├── Producto.java
    │   │   ├── Barrio.java
    │   │   ├── DetallePedido.java
    │   │   ├── Distribuidor.java
    │   │   ├── Domicilio.java
    │   │   ├── Empleado.java
    │   │   ├── EncargadoAdmin.java
    │   │   ├── Estado.java              #   Enum: PENDIENTE, EN_REPARTO, ENTREGADO, CANCELADO
    │   │   ├── Factura.java
    │   │   ├── Operador.java
    │   │   ├── Precio.java
    │   │   ├── Presidente.java
    │   │   ├── Stock.java
    │   │   ├── Telefono.java
    │   │   ├── TipoProducto.java
    │   │   └── Zona.java
    │   ├── prueba/                      # Tests
    │   │   ├── PruebaDAO.java
    │   │   └── TesteoExhaustivo.java    # 38 pruebas
    │   └── util/
    │       └── InicializadorBD.java     # Crea esquema SQL + datos al iniciar
    └── resources/org/ubp/edu/doo/tpdooaguasvital2026/
        ├── principal.fxml               # Menú principal
        ├── pedidos.fxml                 # Listado de pedidos
        ├── editarPedido.fxml            # Formulario de pedido
        └── esquema-aguas-vital.sql      # DDL + inserts de prueba
```

---

## 3. Patrón DAO + Factory Method

### 3.1 Dao (interfaz genérica)

```
┌──────────────────────────────────────────────┐
│              <<interface>>                    │
│                  Dao<T>                       │
├──────────────────────────────────────────────┤
│ + buscar(T criterio): T                      │
│ + listarPorCriterio(T criterio): List<T>     │
│ + listarTodos(): List<T>                     │
│ + insertar(T obj): boolean                   │
│ + modificar(T obj): boolean                  │
│ + borrar(T obj): boolean                     │
└──────────────────────────────────────────────┘
```

### 3.2 Implementaciones de DAO

Cada una implementa `Dao<DtoCorrespondiente>` y tiene un atributo `ConexionSql conexion`.

| Clase | Implementa | DTO |
|-------|-----------|-----|
| `ClienteDao` | `Dao<ClienteDto>` | ClienteDto |
| `ProductoDao` | `Dao<ProductoDto>` | ProductoDto |
| `PedidoDao` | `Dao<PedidoDto>` | PedidoDto |
| `ZonaDao` | `Dao<ZonaDto>` | ZonaDto |
| `DistribuidorDao` | `Dao<DistribuidorDto>` | DistribuidorDto |

```
┌──────────────────────┐
│     <<interface>>     │
│       Dao<T>          │
└──────────┬───────────┘
           │ InterfaceRealization (5 flechas)
     ┌─────┼─────┬─────┬─────┐
     │     │     │     │     │
┌────┴┐ ┌─┴──┐ ┌┴───┐ ┌┴──┐ ┌┴──────┐
│Clien│ │Pro-│ │Ped-│ │Zo-│ │Distri-│
│teDao│ │duc-│ │ido-│ │na-│ │buidor-│
│     │ │to- │ │Dao │ │Dao│ │Dao    │
│     │ │Dao │ │    │ │   │ │       │
└─────┘ └────┘ └────┘ └───┘ └───────┘
```

**Nota:** Solo `PedidoDao` tiene los 6 métodos implementados (CRUD completo transaccional con commit/rollback). Los otros 4 DAOs solo tienen `listarTodos()`; el resto lanza `UnsupportedOperationException`.

### 3.3 FabricaDao (Factory Method)

```
┌───────────────────────────┐
│        FabricaDao         │
├───────────────────────────┤
│ (sin atributos)           │
├───────────────────────────┤
│ + fabricar(String): Dao   │
└─────────────┬─────────────┘
              │ <<create>>
              v
        ┌──────────┐
        │   Dao    │
        └──────────┘
```

Usa **reflexión** (`Class.forName()`) para instanciar el DAO según el nombre:
```java
FabricaDao.fabricar("ClienteDao")  → devuelve new ClienteDao()
FabricaDao.fabricar("PedidoDao")   → devuelve new PedidoDao()
```

### 3.4 FabricaModelo (Factory Method)

```
┌───────────────────────────────┐
│         FabricaModelo         │
├───────────────────────────────┤
│ (sin atributos)               │
├───────────────────────────────┤
│ + fabricar(String): Object    │
└─────────────────┬─────────────┘
                  │ <<create>>
                  v
           ┌──────────┐
           │  Modelo  │
           └──────────┘
```

---

## 4. Patrón MVC

### 4.1 Capa Modelo

```
┌──────────────────────────────┐
│          Modelo              │  <<abstract>>
├──────────────────────────────┤
│ + dao: Dao                   │
│ + mapper: ModelMapper        │
└──────────────┬───────────────┘
               │ Generalization (3 flechas)
     ┌─────────┼─────────┐
     │         │         │
┌────┴────┐ ┌──┴───┐ ┌───┴────┐
│ Cliente │ │Pedido│ │Producto│
├─────────┤ ├──────┤ ├────────┤
│ - nroCliente: int      │
│         │ │(13   │ │- cod-  │
│ - documento: String    │
│         │ │atrib)│ │Producto│
│ - nombre: String       │
│         │ │      │ │: String│
│ - apellido: String     │
│         │ │      │ │- nom-  │
│ - razonSocial: String  │
│         │ │      │ │Produ-  │
│ - domicilio: Domicilio │
│         │ │      │ │cto:    │
│ - telefono: Telefono   │
│         │ │      │ │String  │
├─────────┤ ├──────┤ ├────────┤
│ + listarTodos()        │
│         │ │+lis- │ │+listar│
│ + get/set... (16)      │
│         │ │tar-  │ │Todos()│
│ + getNombreCompleto()  │
│         │ │Todos │ │+get/s │
│ + tieneDeuda()         │
│         │ │()    │ │et...  │
│ + getDireccionCompleta │
│         │ │+guar │ │+toStr│
│ + toString()           │
│         │ │dar() │ │ing() │
│                       │
│         │ │+modi │ │       │
│                       │
│         │ │ficar │ │       │
│                       │
│         │ │() +el│ │       │
│                       │
│         │ │iminar│ │       │
│                       │
│         │ │()    │ │       │
│                       │
│         │ │+calc │ │       │
│                       │
│         │ │ularTo│ │       │
│                       │
│         │ │tal() │ │       │
└─────────┘ └──────┘ └───────┘
```

**Pedido** es la clase más compleja. Atributos completos:

| Tipo | Nombre |
|------|--------|
| `int` | `nroPedido` |
| `Date` | `fecha` |
| `Date` | `fechaEntrega` |
| `Factura` | `factura` |
| `String` | `estado` |
| `Cliente` | `cliente` |
| `Operador` | `operador` |
| `Operador` | `opCancela` |
| `Distribuidor` | `distribuidor` |
| `Zona` | `zona` |
| `List<DetallePedido>` | `detallePedido` |

Métodos de Pedido: `listarTodos()`, `listarPorNro(int)`, `guardar()`, `modificar()`, `eliminar()`, `buscarDetalles()`, `calcularTotalDetalle()`, `agregarItemDetallePedido()`, `calcularTotal()`, `asignarDistribuidor()`, getters/setters (22).

### 4.2 Capa Vista (FXML)

Tres archivos FXML en `src/main/resources/`:

| Archivo | Controller asociado | Propósito |
|---------|-------------------|-----------|
| `principal.fxml` | `PrincipalController` | Menú con 3 botones: Registrar / Consultar / Salir |
| `pedidos.fxml` | `PedidosController` | Tabla con pedidos + búsqueda por número |
| `editarPedido.fxml` | `EditarPedidoController` | Formulario único para crear y modificar pedidos |

### 4.3 Capa Controlador

```
┌──────────────────────────────┐
│         Controller           │  <<abstract>>
├──────────────────────────────┤
│ # progress: ProgressBar      │
├──────────────────────────────┤
│ + cerrarVentana(ActionEvent) │
│ + showAlert(...)             │
│ + loadData()                 │
└──────────────┬───────────────┘
               │
     ┌─────────┼───────────────────┐
     │         │                   │
┌────┴────┐ ┌──┴───────┐    ┌─────┴──────────────┐
│Principal│ │Pedidos-  │    │ EditarPedido-       │
│Controller│ │Controller │    │ Controller          │
│         │ │          │    │                     │
│ (menú)  │ │(listado  │    │ (formulario con     │
│         │ │ + búsq.) │    │  ValidatorFX +      │
│         │ │          │    │  combo clientes/    │
│         │ │          │    │  productos)          │
└─────────┘ └──────────┘    └─────────────────────┘
```

---

## 5. Patrón State (solo diagrama)

No implementado en código Java. Solo existe en el diagrama StarUML.

```
┌──────────────────────────────┐
│          Pedido              │  (Context)
├──────────────────────────────┤
│ - estadoActual: PedidoEstado  │
├──────────────────────────────┤
│ + siguienteEstado()          │
│ + cancelar()                 │
└──────────────┬───────────────┘
               │ Association (1 → 1)
               v
┌──────────────────────────────┐
│      <<interface>>           │
│      PedidoEstado            │
├──────────────────────────────┤
│ + siguienteEstado(Pedido)    │
│ + cancelar(Pedido)           │
└──────────────┬───────────────┘
               │ Generalization (6)
     ┌────┬───┼───┬────┬────┬───┐
     │    │   │   │    │    │   │
┌────┴┐ ┌┴──┐ ┌┴──┐ ┌┴──┐ ┌┴──┐ ┌┴───┐
│Ped- │ │Pe-│ │Pe-│ │Pe-│ │Pe-│ │Pe- │
│ido- │ │do-│ │do-│ │do-│ │do-│ │do- │
│Pen- │ │do-│ │do-│ │En-│ │En-│ │Can-│
│dien-│ │nf-│ │En-│ │Re-│ │tre│ │cel-│
│te   │ │ir-│ │Pr-│ │par│ │ga-│ │ado │
│     │ │ma-│ │ep-│ │to │ │do │ │    │
│     │ │do │ │ar-│ │   │ │   │ │    │
│     │ │   │ │ac-│ │   │ │   │ │    │
│     │ │   │ │ión│ │   │ │   │ │    │
└─────┘ └───┘ └───┘ └───┘ └───┘ └────┘
```

**Transiciones:**
```
Pendiente → Confirmado → EnPreparacion → EnReparto → Entregado
    ↓  (desde cualquier estado)
Cancelado
```

---

## 6. Patrón Strategy (solo diagrama)

No implementado en código Java. Solo existe en el diagrama StarUML.

```
┌──────────────────────────────┐    Association    ┌──────────────────────────────┐
│          Producto            │ ────────────────> │      <<interface>>           │
│          (Context)           │     1 → 1        │   EstrategiaPrecio           │
├──────────────────────────────┤                   ├──────────────────────────────┤
│ - estrategia: EstrategiaPrecio│                   │ + calcularPrecio(double):    │
├──────────────────────────────┤                   │   double                    │
│ + calcularPrecio(): double    │                   └──────────────┬───────────────┘
└──────────────────────────────┘                                  │ Generalization (2)
                                                             ┌────┴────┐
                                                             │         │
                                                        ┌────┴────┐ ┌───┴─────┐
                                                        │Estrategia│ │Estrategia│
                                                        │Mayorista │ │Minorista │
                                                        └──────────┘ └──────────┘
```

---

## 7. Patrón Observer (solo diagrama)

No implementado en código Java. Solo existe en el diagrama StarUML.

```
┌──────────────────────────────┐    Association    ┌──────────────────────────────┐
│          Pedido              │ ────────────────> │      <<interface>>           │
│         (Subject)            │     1 → *        │       Observador             │
├──────────────────────────────┤                   ├──────────────────────────────┤
│ - observadores: List<Observador>│                │ + notificarCambioEstado(     │
├──────────────────────────────┤                   │   Pedido)                   │
│ + notificar()                │                   └──────────────┬───────────────┘
│ + agregarObservador()        │                                  │ Generalization (2)
└──────────────────────────────┘                             ┌────┴────┐
                                                              │         │
                                                         ┌───┴────┐ ┌──┴──────┐
                                                         │Notifica-│ │Notifica-│
                                                         │cionEmail│ │cionSMS  │
                                                         └─────────┘ └─────────┘
```

---

## 8. Clases de Soporte (Modelo)

### Empleado y jerarquía

```
┌──────────────────────┐
│       Empleado       │
├──────────────────────┤
│ - nombre: String     │
│ - apellido: String   │
│ - legajo: String     │
├────────────────────┤
│ + getNombreCompleto()│
│ + toString()         │
└─────────┬───────────┘
          │ Generalization (3)
    ┌─────┼─────┐
    │     │     │
┌───┴──┐ ┌┴───┐ ┌┴──────┐
│Encar-│ │Ope-│ │Presi- │
│gado- │ │ra- │ │dente  │
│Admin │ │dor │ │       │
├──────┤ ├────┤ ├──────┤
│+reg- │ │+ca-│ │+ac-  │
│istrar│ │lcu-│ │tuali-│
│Entre-│ │lar-│ │zar-  │
│ga-   │ │Fe- │ │Precio│
│Pedido│ │cha-│ │()    │
│()    │ │Est-│ │      │
│+modi-│ │ima-│ │      │
│ficar-│ │do()│ │      │
│Esta- │ │+ge-│ │      │
│do-   │ │ner-│ │      │
│Pedido│ │ar- │ │      │
│()    │ │Pe- │ │      │
│+efec-│ │dido│ │      │
│tuar- │ │()  │ │      │
│Resu- │ │+an-│ │      │
│men() │ │ular│ │      │
│      │ │Fac-│ │      │
│      │ │tura│ │      │
│      │ │... │ │      │
└──────┘ └────┘ └──────┘
```

### Otras clases de soporte

| Clase | Atributos | Métodos clave |
|-------|-----------|--------------|
| `Barrio` | `codigo: String`, `nombre: String` | get/set, toString |
| `DetallePedido` | `producto: Producto`, `precio: double`, `cantidad: int` | `calcularSubtotal()` |
| `Distribuidor` | `cantMaxEntrega: int`, `radio: String`, `pedidos: List<Pedido>` | `realizarCobranza()` |
| `Domicilio` | `direccion: String`, `barrio: String`, `zona: String` | `getDireccionCompleta()` |
| `Factura` | `nroFactura: int`, `fecha: Date`, `cliente: Cliente`, `detalleFactura: String`, `pedido: Pedido` | `calcularTotal()`, `imprimirFactura()` |
| `Precio` | `monto: double`, `fechaVigencia: Date`, `tipoProducto: TipoProducto` | `getMontoActual()` |
| `Stock` | `cantidad: int`, `producto: Producto`, `fechaActualizacion: Date` | `verificarDisponibilidad()`, `actualizarCantidad()` |
| `Telefono` | `caracteristica: String`, `nroTelefonico: String`, `tipoTelefono: String` | `getNumeroCompleto()` |
| `TipoProducto` | `codTipoproducto: String`, `descripcion: String`, `precios: List<Precio>` | `getPrecioActual()` |
| `Zona` | `codigo: String`, `nombre: String`, `barrios: List<Barrio>` | get/set, toString |
| `Estado` | *(enum)* `PENDIENTE, EN_REPARTO, ENTREGADO, CANCELADO` | — |

---

## 9. DTOs

| DTO | Atributos |
|-----|-----------|
| `ClienteDto` | `int nroCliente`, `String documento`, `String nombre`, `String apellido`, `String razonSocial`, `String direccionCompleta` |
| `ProductoDto` | `String codProducto`, `String nomProducto` |
| `PedidoDto` | `int nroPedido`, `Date fecha`, `Date fechaEntrega`, `String estado`, `ClienteDto cliente`, `List<DetallePedidoDto> detallePedido` |
| `ZonaDto` | `String codigo`, `String nombre` |
| `DistribuidorDto` | `int id`, `int cantMaxEntrega`, `String radio` |
| `DetallePedidoDto` | `ProductoDto producto`, `double precio`, `int cantidad` |
| `FacturaDto` | `int nroFactura`, `Date fecha`, `ClienteDto cliente`, `String detalleFactura` |

---

## 10. Controladores JavaFX

### Controller (abstracto)

```
┌────────────────────────────────────┐
│           Controller               │  <<abstract>>
├────────────────────────────────────┤
│ # progress: ProgressBar            │
├────────────────────────────────────┤
│ + cerrarVentana(ActionEvent)       │
│ + showAlert(tipo, owner, titulo,   │
│             mensaje)               │
│ + loadData()                       │
└───────────┬────────────────────────┘
            │
    ┌───────┼──────────────────────────────┐
    │       │                              │
┌───┴────┐ ┌┴──────────┐    ┌─────────────┴──────────────┐
│Principal│ │Pedidos-   │    │ EditarPedidoController      │
│Controller│ │Controller │    │                             │
├─────────┤ ├──────────┤    ├─────────────────────────────┤
│(menú)   │ │- tableView  │   │- txtNro: TextField          │
│         │ │:TableView   │   │- txtFecha: DatePicker       │
│- salir() │ │- datos:     │   │- txtPrecio: TextField      │
│- opcReg- │ │Obs.List     │   │- txtCantidad: TextField    │
│ istrar() │ │- txtBuscar  │   │- txtTotal: TextField       │
│- opcCon- │ │:TextField   │   │- cmbCliente: ComboBox      │
│ sultar() │ │- btnBuscar  │   │- cmbProducto: ComboBox     │
│- opcAc- │ │:Button      │   │- tableView: TableView       │
│ ercaDe() │ │...          │   │- validador: Validator       │
│         │ ├────────────┤   ├─────────────────────────────┤
│         │ │+ loadData() │   │+ guardarPedido() (UC-4)     │
│         │ │+ buscarPed- │   │+ modificarPedido() (UC-8)   │
│         │ │ idos()      │   │+ agregarItemDetalle()       │
│         │ │+ modificar- │   │+ quitarItemDetalle()        │
│         │ │ Pedido()    │   │+ passData(Pedido, Control-  │
│         │ │+ nuevoPed-  │   │            ler)             │
│         │ │ ido()       │   └─────────────────────────────┘
│         │ │+ eliminar-  │
│         │ │ Pedido()    │
│         │ │+ limpiar-   │
│         │ │ Busqueda()  │
└─────────┘ └─────────────┘
```

---

## 11. Jerarquía de Clases Completa

```
App (extends javafx.application.Application)
│
├── modelo/
│   ├── Modelo (abstract) ─── campos: dao, mapper
│   │   ├── Cliente extends Modelo
│   │   ├── Pedido  extends Modelo
│   │   └── Producto extends Modelo
│   ├── Empleado
│   │   ├── EncargadoAdmin extends Empleado
│   │   ├── Operador extends Empleado
│   │   └── Presidente extends Empleado
│   ├── Barrio
│   ├── DetallePedido
│   ├── Distribuidor
│   ├── Domicilio
│   ├── Estado (enum)
│   ├── Factura
│   ├── Precio
│   ├── Stock
│   ├── Telefono
│   ├── TipoProducto
│   └── Zona
│
├── dao/
│   ├── Dao<T> (interface)
│   ├── ClienteDao implements Dao<ClienteDto>
│   ├── ProductoDao implements Dao<ProductoDto>
│   ├── PedidoDao implements Dao<PedidoDto>
│   ├── ZonaDao implements Dao<ZonaDto>
│   ├── DistribuidorDao implements Dao<DistribuidorDto>
│   └── ConexionSql
│
├── dto/
│   ├── ClienteDto / ProductoDto / PedidoDto / ZonaDto
│   └── DistribuidorDto / DetallePedidoDto / FacturaDto
│
├── factories/
│   ├── FabricaDao        → fabricar(String): Dao
│   └── FabricaModelo     → fabricar(String): Object
│
├── controladores/
│   ├── Controller (abstract)
│   ├── PrincipalController extends Controller implements Initializable
│   ├── PedidosController extends Controller implements Initializable
│   └── EditarPedidoController extends Controller implements Initializable
│
├── util/
│   └── InicializadorBD   → crea BD + datos de prueba
│
└── prueba/
    ├── PruebaDAO          → demo rápida de DAOs
    └── TesteoExhaustivo   → 38 tests automáticos (inner interface Prueba)
```

---

## 12. Cómo Organizar el Diagrama StarUML

### Distribución de las 25 clases en el lienzo

Organizar en **5 zonas horizontales** (columnas):

```
X=50        X=500      X=950       X=1300      X=1700
┌─────────┐ ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐
│DAO+     │ │  MVC   │ │Strategy│ │ State  │ │Observer│
│Factory  │ │        │ │        │ │        │ │        │
│         │ │        │ │        │ │        │ │        │
│Fabrica  │ │Fabrica │ │Estra-  │ │Pedido  │ │Observa-│
│Dao      │ │Modelo  │ │tegia-  │ │Estado  │ │dor     │
│         │ │        │ │Precio  │ │(inter.)│ │(inter.)│
│Dao      │ │Modelo  │ │  │     │ │  │     │ │  │     │
│(inter.) │ │(abstr.)│ │Estra-  │ │6 esta- │ │Notif-  │
│  │      │ │  │     │ │tegia   │ │dos     │ │Email   │
│5 DAOs   │ │Cliente │ │May/    │ │concre- │ │Notif-  │
│concre-  │ │Pedido  │ │Min     │ │tos     │ │SMS     │
│tos      │ │Produc- │ │        │ │        │ │        │
│         │ │to      │ │        │ │        │ │        │
└─────────┘ └────────┘ └────────┘ └────────┘ └────────┘
```

### Posiciones específicas recomendadas (X, Y)

**Zona 1: DAO + Factory Method** (x ≈ 30-400)
```
FabricaDao           (120, 30)
Dao                  (120, 150)     <<interface>>
ClienteDao           (30,  330)
ProductoDao          (180, 330)
PedidoDao            (330, 330)
DistribuidorDao      (30,  430)
ZonaDao              (180, 430)
```

**Zona 2: MVC** (x ≈ 500-800)
```
FabricaModelo        (560, 30)
Modelo               (560, 150)     <<abstract>>
Cliente              (430, 330)
Pedido               (560, 330)     (también se conecta con State y Observer)
Producto             (690, 330)     (también se conecta con Strategy)
```

**Zona 3: Strategy** (x ≈ 900-1150)
```
EstrategiaPrecio     (960, 150)     <<interface>>
EstrategiaMayorista  (900, 330)
EstrategiaMinorista  (1020, 330)
```

**Zona 4: State** (x ≈ 1200-1550)
```
PedidoEstado         (1260, 150)    <<interface>>
PedidoPendiente      (1160, 330)
PedidoConfirmado     (1260, 330)
PedidoEnPreparacion  (1360, 330)
PedidoEnReparto      (1160, 430)
PedidoEntregado      (1260, 430)
PedidoCancelado      (1360, 430)
```

**Zona 5: Observer** (x ≈ 1600-1850)
```
Observador           (1660, 150)    <<interface>>
NotificacionEmail    (1600, 330)
NotificacionSMS      (1720, 330)
```

### Tipos de relación en el diagrama

| Tipo de Edge | Cantidad | Descripción |
|-------------|----------|-------------|
| `UMLGeneralizationView` | 13 | Herencia: 6 State + 2 Strategy + 2 Observer + 3 MVC |
| `UMLInterfaceRealizationView` | 5 | 5 DAOs → Dao |
| `UMLAssociationView` | 3 | Pedido→PedidoEstado, Producto→EstrategiaPrecio, Pedido→Observador |
| `UMLDependencyView` | 2 | FabricaDao→Dao, FabricaModelo→Modelo |

### Conversión a <<interface>> en StarUML

Las clases `Dao`, `EstrategiaPrecio`, `PedidoEstado`, `Observador` deben ser `<<interface>>`:

1. En el panel **Model Explorer** (izquierdo), buscar la clase
2. Properties → `stereotype` = `interface`
3. `isAbstract` = `false` (las interfaces no necesitan isAbstract)

`Modelo` debe quedar como `<<abstract>>` (isAbstract = true, sin stereotype).

---

## 13. Resumen de Verificación (40 tests)

| Eje | Tests | Resultado |
|-----|-------|-----------|
| **1. Compilación** | 5 | ✅ `mvn clean compile` BUILD SUCCESS, `mvn javafx:run` funciona, 43 archivos, 5 dependencias, 3 FXMLs |
| **2. Tests automáticos** | 8 | ✅ 38/38 tests pasan, DAOs funcionales, Factory Methods con reflexión, ValidatorFX activo |
| **3. Diagrama StarUML** | 8 | ✅ 48 vistas (25 class + 23 edge), 13 gen + 5 ireal + 3 assoc + 2 dep, 0 edges rotas |
| **4. Patrones vs PDFs** | 7 | ✅ DAO genérico, Factory Method, MVC abstracto, State/Strategy/Observer modelados, 6/6 páginas PDF citadas |
| **5. Documentación** | 7 | ✅ Avances.md, CHANGELOG.md, CONTEXT.md, carpeta limpia sin target/.idea/.db |
| **Manuales (visuales)** | 5 | ✅ Abre sin crash, stereotypes interface corregidos, Modelo en cursiva |

---

*Documento generado el 16/06/2026 para el equipo del TP Integrador DOO 2026*
