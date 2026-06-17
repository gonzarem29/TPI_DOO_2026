# Contexto del Proyecto — TP Integrador DOO 2026

## Identidad

| Campo | Valor |
|-------|-------|
| **Proyecto** | Aguas Vital SA |
| **Materia** | Diseño Orientado a Objetos |
| **Carrera** | Ingeniería en Sistemas — UBP |
| **Entrega** | Segunda Entrega |
| **Fecha** | 15/06/2026 |
| **Tecnologías** | Java 11, JavaFX 13, Maven, SQLite, StarUML 6.x |
| **Build** | `mvn clean compile` → BUILD SUCCESS |
| **Ejecución** | `mvn javafx:run` |

## Estructura del proyecto

```
tp-doo-aguas-vital-2026/
├── pom.xml
└── src/main/
    ├── java/org/ubp/edu/doo/tpdooaguasvital2026/
    │   ├── App.java
    │   ├── controladores/   (Controller + PrincipalController + PedidosController + EditarPedidoController)
    │   ├── dao/             (Dao<T> + 5 implementaciones + ConexionSql)
    │   ├── dto/             (7 DTOs)
    │   ├── factories/       (FabricaDao + FabricaModelo)
    │   ├── modelo/          (18 clases + Modelo abstracto + Estado enum)
    │   └── util/            (InicializadorBD)
    └── resources/.../
        ├── principal.fxml / pedidos.fxml / editarPedido.fxml
        └── esquema-aguas-vital.sql
```

## Patrones de Diseño — Resumen

| Patrón | Role | ¿En código? | ¿En diagrama? | PDF fuente |
|--------|------|:-----------:|:-------------:|------------|
| **DAO** | `Dao<T>` interfaz genérica con CRUD; 5 concretos (Cliente, Producto, Pedido, Zona, Distribuidor) | ✅ | ✅ | Clase 8 p16-18 |
| **Factory Method** | `FabricaDao.fabricar(String)` y `FabricaModelo.fabricar(String)` — variante parametrizada | ✅ | ✅ | Clase 12 p16-18 |
| **MVC** | `Modelo` abstracto → `Cliente`, `Producto`, `Pedido` concretos; 3 FXMLs como Vistas; 3 Controladores | ✅ | ✅ | Clase 9 p7-13 |
| **State** | `PedidoEstado` interfaz + 6 estados concretos + `Pedido` como Context | ❌ solo diagrama | ✅ | Clase 14 p4-5 |
| **Strategy** | `EstrategiaPrecio` interfaz + 2 concretos + `Producto` como Context | ❌ solo diagrama | ✅ | Clase 14 p14-15 |
| **Observer** | `Observador` interfaz + 2 concretos + `Pedido` como Subject | ❌ solo diagrama | ✅ | Clase 14 p24-25 |

**Nota:** State, Strategy y Observer solo se exigen en el diagrama StarUML (consigna: "diagrama de clases que incluya los patrones de diseño encontrados"). La implementación Java solo requiere DAO, Factory Method y MVC para cubrir los requisitos funcionales (UC-4, UC-8).

## Diagrama StarUML — "Patrones de Diseno"

- Archivo: `Avances/algo corregido.mdj`
- **48 vistas** en el diagrama: 25 UMLClassView + 23 edge views
- **Relaciones:** 13 Generalization, 5 InterfaceRealization, 3 Association, 2 Dependency
- **Clases reposicionadas** manualmente por el usuario (BACKUP3 tiene la última posición manual)
- **No tiene PackageViews** — el usuario decidió NO agregarlos porque las cajas solapaban el contenido

## Archivos clave

| Archivo | Propósito |
|---------|-----------|
| `Avances/algo corregido.mdj` | StarUML actual — diagrama Patrones con 48 vistas |
| `Avances/algo corregido BACKUP3.mdj` | Backup antes de intentos de PackageViews |
| `Avances/Avances.md` | Justificación de patrones contra PDFs + Mermaid |
| `Avances/CONTEXT.md` | Este archivo — resumen del proyecto |
| `Avances/CHANGELOG.md` | Auditoría detallada de cambios |
| `scripts/reorganize_diagram.py` | Reposiciona clases y elimina points de edges |
| `scripts/route_edges.py` | Computa puntos de ruta para edges sin solapamiento |
| `scripts/reposition_and_packages.py` | Re-layout completo + PackageViews |
| `scripts/add_package_views.py` | Solo agrega PackageViews (no usado) |

## Decisiones de Arquitectura

| Decisión | Opción | Motivo |
|----------|--------|--------|
| Base de datos | SQLite | Mismo que ejemplo de cátedra |
| ORM | ModelMapper (manual para genéricos) | Conversión DTO↔Modelo |
| UI | JavaFX 13 + FXML + SceneBuilder | Requerimiento de entrega |
| Casos de uso | UC-4 (Registrar Pedido) + UC-8 (Modificar Pedido) | Definido en primera entrega |
| CRUD Pedido | Transaccional (commit/rollback explícitos) | Integridad referencial del detalle |

## Estado de la Entrega

- [x] Actividad 1: Esqueleto Maven compilable
- [x] Actividad 2: DAOs + SQLite + BD con datos de prueba
- [x] Actividad 3: FXMLs + Controladores (UC-4 + UC-8)
- [x] Actividad 4: Diagrama StarUML con 6 patrones + trazabilidad
- [x] 38/38 tests pasan
- [x] `Avances.md` con justificación de cada patrón contra PDFs

**Veredicto: Segunda Entrega completa.**
